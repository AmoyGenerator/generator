/*******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *
 * </copyright>
 *
 * $Id$
 * /
 *******************************************************************************/
package org.eclipse.jet.taglib.workspace;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.SequenceInputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Arrays;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jet.BufferedJET2Writer;
import org.eclipse.jet.IWriterListener;
import org.eclipse.jet.IWriterListenerExtension;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.JET2TagException;


/**
 * Utility class for workspace actions. Provides implementations of typical workspace operations needed by 
 * Workspace actions.
 * <p>
 * This class is not intended to be instantiated or subclassed by clients.
 * </p>
 */
public final class ActionsUtil
{

  /**
   * Constant that identifies the UTF-8 character encoding schema.
   */
  private static final String CHARSET_UTF_8 = "UTF-8"; //$NON-NLS-1$

  /**
   * Constant that identifies the Byte-Order-Mark for contents encoded with 
   * the UTF-8 character encoding scheme. 
   */
  private final static byte[] BOM_UTF_8 = { (byte)0xEF, (byte)0xBB, (byte)0xBF };

  /**
   * 
   */
  private ActionsUtil()
  {
    super();
  }

  /**
   * Write the file with the specified options from a JET writer.
   * <p>
   * This method delegates to {@link #writeTextFile(IFile, boolean, String, boolean, String, IProgressMonitor) writeTextFile}
   * after finalizing the writer's content and notifying writer listeners via {@link IWriterListener#finalizeContent(JET2Writer, Object)}.
   * If the content is written, then the writer listeners are informed of this after the write via 
   * {@link IWriterListener#postCommitContent(JET2Writer, Object)}.
   * </p> 
   * @param file the file to write
   * @param replace <code>true</code> if existing verions of the file should be overwritten.
   * @param encoding the file encoding or <code>null</code> for the default encoding.
   * @param derived <code>true</code> if the file is derived (and will be ignored by the team environment.
   * @param writer the writer containing the file's content.
   * @param monitor a progress monitor
   * @return <code>true</code> if the file was written, <code>false</code> if <code>replace</code> is <code>true</code>
   * and the file already exists..
   * @throws JET2TagException if the file cannot be created or written, or the encoding is invalid.
   * @see IWriterListener#finalizeContent(JET2Writer, Object)
   * @see IWriterListener#postCommitContent(JET2Writer, Object)
   */
  public static boolean writeTextFileFromWriter(
    IFile file,
    boolean replace,
    String encoding,
    boolean derived,
    JET2Writer writer,
    IProgressMonitor monitor) throws JET2TagException
  {
    finalizeContent(writer, file, null);

    boolean written = writeTextFile(file, replace, encoding, derived, writer.toString(), monitor);

    if (written)
    {
      contentCommitted(writer, file);
    }

    return written;
  }

  private static final void finalizeContent(JET2Writer writer, Object committedObject, String existingContent) throws JET2TagException
  {
    final IWriterListener[] eventListeners = writer.getEventListeners();
    for (int i = 0; i < eventListeners.length; i++)
    {
      if(existingContent != null && eventListeners[i] instanceof IWriterListenerExtension) {
        ((IWriterListenerExtension)eventListeners[i]).finalizeContent(writer, committedObject, existingContent);
      } else {
        eventListeners[i].finalizeContent(writer, committedObject);
      }
    }
  }

  static final void contentCommitted(JET2Writer writer, Object committedObject) throws JET2TagException
  {
    final IWriterListener[] eventListeners = writer.getEventListeners();
    for (int i = 0; i < eventListeners.length; i++)
    {
      eventListeners[i].postCommitContent(writer, committedObject);
    }
  }

  /**
   * Write the specified text file from a string.
   * @param file the file to write
   * @param replace <code>true</code> if existing verions of the file should be overwritten.
   * @param encoding the file encoding or <code>null</code> for the default encoding.
   * @param derived <code>true</code> if the file is derived (and will be ignored by the team environment.
   * @param contents the contents to write.
   * @param monitor a progress monitor
   * @return <code>false</code> <code>replace</code> was <code>true</code> and the file existed; <code>true</code> otherwise.
   * @throws JET2TagException if the file cannot be created or written, or the encoding is invalid.
   */
  public static boolean writeTextFile(
    IFile file,
    boolean replace,
    String encoding,
    boolean derived,
    final String contents,
    IProgressMonitor monitor) throws JET2TagException
  {
    try
    {
      final String fileMessage = MessageFormat.format(
        JET2Messages.WsFileFromWriterAction_WritingFile,
        new Object []{ file.getFullPath().toString() });
    
      monitor.beginTask(fileMessage, 5);
      monitor.setTaskName(fileMessage);
    
      if (file.exists() && !replace)
      {
        return false;
      }
    
      // Step 1 (work = 1): make sure folder exists
      if (!file.exists())
      {
        // Step 1a: ensure containing folder exists...
        if (file.getParent() instanceof IFolder)
        {
          ensureFolderExists((IFolder)file.getParent(), new SubProgressMonitor(monitor, 1));
        }
        else
        {
          monitor.worked(1);
        }
      }
    
      // Step 2 (work = 1): convert the contents to a byte stream of the proper type
      
      // get the content description to see if it provides a hint on charset to use...
      final IContentDescription contentDescription = getContentDescription(file, contents);
    
      String sourceEncoding = encoding != null ? encoding : 
          contentDescription != null ? contentDescription.getCharset() : null;
      if(sourceEncoding == null) {
        sourceEncoding = ResourcesPlugin.getWorkspace().getRoot().getDefaultCharset();
      }
      InputStream source = new ByteArrayInputStream(contents.getBytes(sourceEncoding));
    
      // work around SUN bug with UTF-8 encoding and byte-order marks.
      // See: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4508058
      // Get default content description (based on file name) to see whether this type of file
      // requires a UTF-8 BOM
      final IContentType defaultContentType = Platform.getContentTypeManager().findContentTypeFor(file.getName());
      final IContentDescription defaultDescription = 
          defaultContentType != null ? defaultContentType.getDefaultDescription() : null;
      final boolean requiresBOM = defaultDescription != null 
        && defaultDescription.getProperty(IContentDescription.BYTE_ORDER_MARK) != null;
      if (requiresBOM && CHARSET_UTF_8.equals(sourceEncoding))
      {
        source = new SequenceInputStream(new ByteArrayInputStream(IContentDescription.BOM_UTF_8), source);
      }
      monitor.worked(1);
    
      // Step 3 (work = 3): write the contents - creating the file if needed
      if(!file.exists() && encoding == null) {
        // if we don't need to specify the encoding, just create the file...
        file.create(source, false, new SubProgressMonitor(monitor, 3));
      } else {
        if (!file.exists())
        {
          // can't set encoding until the file exists, create the file with null-contents
          // now, and set it's contents later.
          file.create(new ByteArrayInputStream(new byte [0]), false, new SubProgressMonitor(monitor, 1));
          file.setCharset(encoding.length() > 0 ? encoding : null, new SubProgressMonitor(monitor, 1));
        }
        else
        {
          monitor.worked(2);
        }
        file.setContents(source, false, true, new SubProgressMonitor(monitor, 1));
      }
    
      // Step 4: Apply generated file properties...
      file.setDerived(derived);
    
      return true;
    }
    catch (UnsupportedEncodingException e)
    {
      throw new JET2TagException(e);
    }
    catch (CoreException e)
    {
      throw new JET2TagException(e);
    }
    finally
    {
      monitor.done();
    }
  }

  private static IContentDescription getContentDescription(IFile file, final String contents)
  {
    try
    {
      return Platform.getContentTypeManager().getDescriptionFor(new StringReader(contents), file.getName(), 
        new QualifiedName[] {IContentDescription.CHARSET});
    }
    catch (IOException e)
    {
      // should happen. Log and return
      InternalJET2Platform.getDefault().getLog().log(
        InternalJET2Platform.newStatus(IStatus.ERROR, "IO error determining content type for generated content", e)); //$NON-NLS-1$
      return null;
    }
  }

  /**
   * Ensure that a folder exists by creating it and any of its parents if necessary.
   * @param folder the folder to ensure exists.
   * @param monitor a progress monitor
   * @return <code>true</code> if folders were created...
   * @throws JET2TagException if the folder cannot be created
   */
  public static boolean ensureFolderExists(IFolder folder, IProgressMonitor monitor) throws JET2TagException
  {
    boolean foldersCreated = false;
    IProject project = folder.getProject();
    IPath relPath = folder.getProjectRelativePath();
    final String taskMessage = MessageFormat.format(JET2Messages.WsFolderAction_CreatingFolder, new Object []{ folder.getFullPath().toString() });
    monitor.beginTask(
      taskMessage,
      relPath.segmentCount());
    monitor.setTaskName(taskMessage);
    for (int i = 1; i <= relPath.segmentCount(); i++)
    {
      IPath subPath = relPath.uptoSegment(i);
      IFolder iFolder = project.getFolder(subPath);
      if (!iFolder.exists())
      {
        try
        {
          iFolder.create(false, true, new SubProgressMonitor(monitor, 1));
          foldersCreated = true;
        }
        catch (CoreException e)
        {
          throw new JET2TagException(e.getMessage(), e);
        }
      }
      else
      {
        monitor.worked(1);
      }
    }
    return foldersCreated;
  }

  /**
   * Write a binary file from a byte buffer.
   * @param file the file to write
   * @param replace <code>true</code> if existing versions of the file should be overwritten.
   * @param contents the contents to write.
   * @param monitor a progress monitor
   * @return <code>true</code> if the file was written, <code>false</code> if <code>replace</code> was <code>true</code> and the
   * file already exists.
   * @throws JET2TagException if the file cannot be created or updated.
   */
  public static boolean writeBinaryFile(IFile file, boolean replace, byte[] contents, IProgressMonitor monitor) throws JET2TagException
  {
    try
    {
      final String fileMessage = MessageFormat.format(
        JET2Messages.WsCopyBinaryFileAction_WritingFile,
        new Object []{ file.getFullPath().toString() });
      monitor.beginTask(fileMessage, 2);

      final boolean fileExists = file.exists();
      if (fileExists && !replace)
      {
        return false;
      }

      // Step 1: ensure containing folder exists...
      if (file.getParent() instanceof IFolder)
      {
        ensureFolderExists((IFolder)file.getParent(), new SubProgressMonitor(monitor, 1));
      }
      else
      {
        monitor.worked(1);
      }

      if (fileExists && contents.equals(readBinaryStream(file.getContents())))
      {
        // no content change, avoid write...
        return false;
      }
      
      // Step 2: write the file
      final InputStream input = new ByteArrayInputStream(contents);
      if (fileExists)
      {
        file.setContents(input, false, true, new SubProgressMonitor(monitor, 1));
      }
      else if (!fileExists)
      {
        file.create(input, false, new SubProgressMonitor(monitor, 1));
      }
      //      RuntimeLoggerContextExtender.log(context, fileMessage, tagInfo, templatePath);
      return true;
    }
    catch (IOException e)
    {
      throw new JET2TagException(e);
    }
    catch (CoreException e)
    {
      throw new JET2TagException(e);
    }
    finally
    {
      monitor.done();
    }
  }

  /**
   * Return the contents of the text file at the specified URL.
   * @return the file contents
   * @throws JET2TagException if an IOException is thrown (the IOException is wrapped). 
   */
  public static String readTextFile(URL url, String encoding) throws JET2TagException
  {
    InputStream input = null;
    try
    {
      input = url.openStream();
      return readTextStream(input, encoding);
    }
    catch (IOException e)
    {
      throw new JET2TagException(e);
    }
    finally
    {
      // ensure stream is closed.
      if (input != null)
      {
        try
        {
          input.close();
        }
        catch (IOException e)
        {
          // ignore
        }
      }
    }
  }

  private static String readTextStream(InputStream input, String encoding) throws IOException {
    Reader reader = null;
    try
    {
      if (encoding != null)
      {
        if (encoding.equals(CHARSET_UTF_8))
        {
          input = stripByteOrderMark(input);
        }
        reader = new InputStreamReader(input, encoding);
      }
      else
      {
        reader = new InputStreamReader(input);
      }

      StringBuffer inputContents = new StringBuffer();
      char readChars[] = new char [1024];
      for (int read = reader.read(readChars); read != -1; read = reader.read(readChars))
      {
        inputContents.append(readChars, 0, read);
      }
      return inputContents.toString();
    }
    finally
    {
      // ensure stream and reader are closed.
      if (reader != null)
      {
        try
        {
          reader.close();
        }
        catch (IOException e)
        {
          // ignore
        }
      }
      if (input != null)
      {
        try
        {
          input.close();
        }
        catch (IOException e)
        {
          // ignore
        }
      }
    }
    
  }
  /**
   * Remove the optional Byte Order Mark from UTF-8 input streams. A JRE bug means that Java doesn't do this
   * automatically for UTF-8 files.
   * @param input an input stream positioned at the start of a UTF-8 encoded file.
   * @return an input stream positioned immediately after any UTF-8 BOM
   * @throws IOException if an IO Error occurs
   */
  private static InputStream stripByteOrderMark(InputStream input) throws IOException
  {
    // need to look for and remove any Byte-order-mark - a JRE bug means that Java doesn't do this for UTF-8.
    if (!input.markSupported())
    {
      // wrap with a buffered input stream, we'll need to rewind if we don't find the BOM.
      input = new BufferedInputStream(input);
    }
    input.mark(BOM_UTF_8.length + 1);
    int bomLength = BOM_UTF_8.length;
    byte[] bomStore = new byte [bomLength];
    int bytesRead = 0;
    do
    {
      int bytes = input.read(bomStore, bytesRead, bomLength - bytesRead);
      if (bytes == -1)
        throw new IOException();
      bytesRead += bytes;
    }
    while (bytesRead < bomLength);
    if (!Arrays.equals(BOM_UTF_8, bomStore))
    {
      input.reset(); // wasn't a bom, rewind to our mark and continue.
    }
    return input;
  }

  /**
   * Read a binary file from the given URL.
   * @param url the file URL
   * @return a byte buffer containing the file's contents
   * @throws JET2TagException if an IOException occurs (the exception is wrapped).
   */
  public static byte[] readBinaryFile(URL url) throws JET2TagException
  {
    try
    {
      return readBinaryStream(new BufferedInputStream(url.openStream()));
    }
    catch (IOException e)
    {
      throw new JET2TagException(e);
    }
  }

  public static byte[] readBinaryStream(InputStream input) throws IOException
  {
    ByteArrayOutputStream output = null;
    try
    {
      output = new ByteArrayOutputStream();
      byte buffer[] = new byte [1024];
      for (int read = input.read(buffer); read != -1; read = input.read(buffer))
      {
        output.write(buffer, 0, read);
      }
      return output.toByteArray();
    }
    finally
    {
      if (input != null)
      {
        try
        {
          input.close();
        }
        catch (IOException e)
        {
          // can't do anything, cascading from a finally block is bad, so just suppress it.
        }
      }
      if (output != null)
      {
        try
        {
          output.close();
        }
        catch (IOException e)
        {
          // can't do anything, cascading from a finally block is bad, so just suppress it.
        }
      }
    }

  }

  /**
   * Create a new project.
   * @param project the project to create
   * @param monitor a progress monitor
   * @throws JET2TagException if an error occurs
   */
  public static void createProject(IProject project, IProgressMonitor monitor) throws JET2TagException
  {
    createProject(project, null, monitor);
  }

  /**
   * Create a new project.
   * @param project the project to create
   * @param description an option project description
   * @param monitor a progress monitor
   * @return <code>true</code> if the project was created, <code>false</code> if the project already existed.
   * @throws JET2TagException if an error occurs
   * @since 0.7.1
   */
  public static boolean createProject(IProject project, IProjectDescription description, IProgressMonitor monitor) throws JET2TagException
  {
    boolean projectCreated = false;
    final String createMessage = MessageFormat.format(JET2Messages.WsProjectAction_CreatingProject, new Object []{ project.getName() });
    monitor.beginTask(createMessage, 2); // two ticks: create & open

    try
    {
      // create...
      if (!project.exists())
      {
        project.create(description, new SubProgressMonitor(monitor, 1));
        projectCreated = true;
      }
      else
      {
        monitor.worked(1);
      }
      // open...
      if (!project.isOpen())
      {
        project.open(new SubProgressMonitor(monitor, 1));
      }
      else
      {
        monitor.worked(1);
      }
    }
    catch (IllegalArgumentException e)
    {
      // create project throws this if the project name is invalid
      throw new JET2TagException(e);
    }
    catch (CoreException e)
    {
      throw new JET2TagException(e);
    }
    finally
    {
      monitor.done();
    }
    return projectCreated;
  }

  /**
   * Force the workspace to broadcast accumulated workspace change events, and optionally do a build.
   * This method is useful if a pending workspace action depends on prior actions being processed
   * by other actors. For example, if a Project and corresponding .project file are created that describe
   * the project as having the Java nature, then JDT must be informed of these changes before JDT APIs will
   * see the new project properly.
   * <p>
   * Generally, workspace actions authors will not need this tag.
   * </p>
   * @param build <code>true</code> if a workspace build should be performed after the event broadcast.
   * @see org.eclipse.core.resources.IWorkspace#checkpoint(boolean)
   * @deprecated Use {@link #checkpointWorkspace(boolean,IProgressMonitor)} instead
   */
  public static void checkpointWorkspace(boolean build)
  {
    checkpointWorkspace(build, new NullProgressMonitor());
  }

  /**
   * Force the workspace to broadcast accumulated workspace change events, and optionally do a build.
   * This method is useful if a pending workspace action depends on prior actions being processed
   * by other actors. For example, if a Project and corresponding .project file are created that describe
   * the project as having the Java nature, then JDT must be informed of these changes before JDT APIs will
   * see the new project properly.
   * <p>
   * Generally, workspace actions authors will not need this tag.
   * </p>
   * @param build <code>true</code> if a workspace build should be performed after the event broadcast.
   * @param monitor TODO
   * @see org.eclipse.core.resources.IWorkspace#checkpoint(boolean)
   */
  public static void checkpointWorkspace(boolean build, IProgressMonitor monitor)
  {
    ResourcesPlugin.getWorkspace().checkpoint(build);
    if (build)
    {
      waitForBuildToComplete(monitor);
    }

  }

  private static void waitForBuildToComplete(IProgressMonitor monitor)
  {
    monitor.beginTask(JET2Messages.ProjectTemplateBundleDescriptor_WaitingForBuild, 1);
    try {
      ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, new SubProgressMonitor(monitor, 1));
    }
    catch (CoreException e)
    {
      // XXX should consider doing something here. The build failed...
    } finally {
      monitor.done();
    }
  }

  /**
   * Finalize contents to write and determine if it must be written.
   * @param file
   * @param writer
   * @param existingContents
   * @return <code>true</code> if contents requires writing, <code>false</code> otherwise
   */
  static boolean finalizeAndTestForChange(IFile file, BufferedJET2Writer writer, InputStream existingContents)
  {
    String existingText = null;
    if(existingContents != null) {
      try
      {
        existingText = readTextStream(existingContents, file.getCharset());
      }
      catch (IOException e)
      {
        throw new JET2TagException(e);
      }
      catch (CoreException e)
      {
        throw new JET2TagException(e);
      }
    }
    
    finalizeContent(writer, file, existingText);
    
    if(existingText != null) {
      return !existingText.equals(writer.getContent());
    } else {
      return true;
    }
  }

}
