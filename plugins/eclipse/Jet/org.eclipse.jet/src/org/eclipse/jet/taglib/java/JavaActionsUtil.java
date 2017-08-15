/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
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
package org.eclipse.jet.taglib.java;


import java.text.MessageFormat;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.codegen.util.ImportManager;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jet.BufferedJET2Writer;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.internal.taglib.DocumentHelper;
import org.eclipse.jet.internal.taglib.java.ImportsLocationTag;
import org.eclipse.jet.internal.taglib.java.PackageTag_bk;
import org.eclipse.jet.internal.taglib.java.ImportsLocationTag.ImportsPosition;
import org.eclipse.jet.taglib.CustomTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.workspace.ActionsUtil;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;


/**
 * Utility class for Java related actions.
 */
public class JavaActionsUtil
{

  /**
   * 
   */
  private JavaActionsUtil()
  {
    super();
  }

  /**
   * Return the {@link IFolder} corresponding to the given Java package name.
   * @param sourceFolder the source folder containing the package
   * @param packageName the package name
   * @return the folder corresponding to the package
   * @throws JET2TagException if the source folder is not valid.
   * @deprecated Use {@link #getContainerForPackage(IPath,String)} instead
   */
  public static IFolder getFolderForPackage(IPath sourceFolder, String packageName) throws JET2TagException
  {
    return (IFolder)getContainerForPackage(sourceFolder, packageName);
  }

  private static void buildProject(String projectName)
  {
    final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
    try
    {
      project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, new NullProgressMonitor());
    }
    catch (CoreException e)
    {
      //e.printStackTrace();
    }
  }
    
  /**
   * Return the {@link IFolder} corresponding to the given Java package name.
   * @param sourceFolder the source folder containing the package
   * @param packageName the package name
   * @return the folder corresponding to the package
   * @throws JET2TagException if the source folder is not valid.
   */
  public static IContainer getContainerForPackage(IPath sourceFolder, String packageName) throws JET2TagException
  {
    IContainer result = null;
    if (sourceFolder == null || packageName == null)
    {
      throw new NullPointerException();
    }
    if (sourceFolder.segmentCount() == 0)
    {
      throw new IllegalArgumentException();
    }

    // if the JDT is not sync'd with the workspace changes, this may return null.
    IJavaElement jElementForFolder = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot().findMember(sourceFolder));

    if (jElementForFolder == null)
    {
      // checkpoint the workspace (inform the builders) of our changes to date...
      ActionsUtil.checkpointWorkspace(false, new NullProgressMonitor());
      buildProject(sourceFolder.segment(0));
      // and try again...
      jElementForFolder = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot().findMember(sourceFolder));
    }

    if (jElementForFolder != null)
    {
      // find the package fragment root corresponding to sourceFolder
      if (jElementForFolder instanceof IPackageFragmentRoot)
      {
        IPackageFragmentRoot root = (IPackageFragmentRoot)jElementForFolder;
        final IPackageFragment packageFragment = root.getPackageFragment(packageName);
        result = (IContainer)packageFragment.getResource();
      }
      else if (jElementForFolder instanceof IJavaProject)
      {
        try
        {
          result = (IContainer)findOrCreateJavaPackage((IJavaProject)jElementForFolder, packageName);
        }
        catch (JavaModelException e)
        {
          final String msg = JET2Messages.JavaActionsUtil_CannotFindSourceFolder;
          // it's serious this time, throw an exception;
          throw new JET2TagException(MessageFormat.format(msg, new Object []{ sourceFolder.segment(0), packageName, }));
        }
      }
      else
      {
        throw new JET2TagException(MessageFormat.format(JET2Messages.JavaActionsUtil_InvalidSourceFolder, new Object []{ sourceFolder.toString() }));
      }
    }
    else
    {
      throw new JET2TagException(MessageFormat.format(JET2Messages.JavaActionsUtil_InvalidSourceFolder, new Object []{ sourceFolder.toString() }));
    }
    return result;
  }

  /**
   * Find the a container corresponding the the given package in the specified project.
   * Traverse all project source package roots, looking for an existing instance of a package fragment corresponding to packageName.
   * If not found, return the first non-existant fragment found.
   * If the project has no source package roots, then null is returned.
   * @param jProject the Java project to search
   * @param packageName the Java package for which a container is sought.
   * @return the container corresponding to the package, or null.
   * @throws JavaModelException if the package roots or root kinds cannot be determined.
   */
  private static IContainer findOrCreateJavaPackage(IJavaProject jProject, String packageName) throws JavaModelException
  {
    IPackageFragment firstNonExistantFragment = null;
    // Traverse package roots, looking for an existing instance of the package fragment corresponding to packageName.
    // Otherwise, return the first non-existant fragment
    final IPackageFragmentRoot[] roots = jProject.getPackageFragmentRoots();
    for (int i = 0; i < roots.length; i++)
    {
      if (roots[i].getKind() == IPackageFragmentRoot.K_SOURCE)
      {
        IPackageFragment fragment = roots[i].getPackageFragment(packageName);
        if(fragment.exists())
        {
          return (IContainer)fragment.getResource();
        }
        else if(firstNonExistantFragment == null)
        {
          firstNonExistantFragment = fragment;
        }
      }
    }
    return firstNonExistantFragment != null ? (IContainer)firstNonExistantFragment.getResource() : null;
  }

  /**
   * Return the imports manager installed in the current writer.
   * @param writer the current writer. Cannot be <code>null</code>.
   * @return the imports manager installed on the writer.
   * @throws JET2TagException if no &lt;java:importsLocation&gt; tag has executed on the writer.
   * @throws NullPointerException if <code>writer</code> is <code>null</code>.
   */
  public static ImportManager getImportManager(JET2Writer writer) throws JET2TagException
  {
    if(!(writer instanceof BufferedJET2Writer)) {
      throw new IllegalArgumentException();
    }
    BufferedJET2Writer bufferedWriter = (BufferedJET2Writer)writer;
    IDocument document = (IDocument)bufferedWriter.getAdapter(IDocument.class);
    
    // defensive add of the category...
    DocumentHelper.installPositionCategory(document, ImportsLocationTag.IMPORTS_POSITION_CATEGORY);

    try
    {
      Position[] positions = document.getPositions(ImportsLocationTag.IMPORTS_POSITION_CATEGORY);
      if (positions.length > 0)
      {
        ImportsPosition position = (ImportsPosition)positions[0];
        return position.getImportManager();
      }
      else if (writer.getParentWriter() != null)
      {
        return getImportManager(writer.getParentWriter());
      }
      else
      {
        throw new JET2TagException(JET2Messages.ImportsLocationTag_MissingImportsLocation);
      }
    }
    catch (BadPositionCategoryException e)
    {
      // Should not happen. Track with a runtime exception
      throw new RuntimeException(e);
    }
  }

  public static PackageTag_bk findContainingJavaPackageTag(CustomTag tag) throws JET2TagException
  {
    for (CustomTag parent = tag.getParent(); parent != null; parent = parent.getParent())
    {
      if (parent instanceof PackageTag_bk)
      {
        return (PackageTag_bk)parent;
      }
    }

    final String msg = JET2Messages.JavaActionsUtil_RequiresAttrOrContainerTag;
    String attrName = "packageName"; //$NON-NLS-1$
    String tagName = "<java:package>"; //$NON-NLS-1$
    throw new JET2TagException(MessageFormat.format(msg, new Object []{ attrName, tagName }));
  }

  public static IFile getResourceForJavaResource(IPath srcFolderPath, String pkgName, String fileName) throws JET2TagException
  {
    IContainer container = getContainerForPackage(srcFolderPath, pkgName);
    if(container instanceof IFolder)
    {
      return ((IFolder)container).getFile(fileName);
    }
    else if(container instanceof IProject)
    {
      return ((IProject)container).getFile(fileName);
    }
    else
    {
      final String msg = JET2Messages.JavaActionsUtil_CannotFindSourceFolder;
      // it's serious this time, throw an exception;
      throw new JET2TagException(MessageFormat.format(msg, new Object []{ srcFolderPath.toString(), pkgName, }));
    }
  }

}
