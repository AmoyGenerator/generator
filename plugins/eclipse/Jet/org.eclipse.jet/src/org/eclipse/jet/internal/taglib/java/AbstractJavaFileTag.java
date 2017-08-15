/**
 * <copyright>
 *
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
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
 */
package org.eclipse.jet.internal.taglib.java;

import java.text.MessageFormat;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jet.BodyContentWriter;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.AbstractEmptyTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.taglib.java.JavaActionsUtil;
import org.eclipse.jet.taglib.workspace.WorkspaceContextExtender;
import org.eclipse.jet.transform.TransformContextExtender;

/**
 * Abstract implementation common to &lt;java:class&gt; and &lt;java:resource&gt;
 */
public abstract class AbstractJavaFileTag extends AbstractEmptyTag
{

  /**
   * 
   */
  public AbstractJavaFileTag()
  {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.taglib.EmptyTag#doAction(org.eclipse.jet.taglib.TagInfo, org.eclipse.jet.JET2Context, org.eclipse.jet.JET2Writer)
   */
  public final void doAction(TagInfo td, JET2Context context, JET2Writer out) throws JET2TagException
  {
    String fileName = getFileName();
    
    String pkgName = getAttribute("package"); //$NON-NLS-1$
    String srcFolder = getAttribute("srcFolder"); //$NON-NLS-1$
    String template = getAttribute("template"); //$NON-NLS-1$
    String encoding = getAttribute("encoding"); //$NON-NLS-1$
    boolean derived = Boolean.valueOf(getAttribute("derived")).booleanValue(); //$NON-NLS-1$
    boolean replace = true;
    if(td.hasAttribute("replace")) //$NON-NLS-1$
    {
      replace = Boolean.valueOf(getAttribute("replace")).booleanValue(); //$NON-NLS-1$
    }
    
    WorkspaceContextExtender wce = WorkspaceContextExtender.getInstance(context);
    
    IPath srcFolderPath;
    if (srcFolder == null && pkgName == null)
    {
      // look for a containing <java:package> tag, and copy values from it
      PackageTag_bk pkgTag = JavaActionsUtil.findContainingJavaPackageTag(this);
      pkgName = pkgTag.getPackageName();
      srcFolderPath = pkgTag.getSourceFolderPath();
    }
    else if (srcFolder == null)
    {
      // look for a container tag such as <ws:folder> or <ws:project>
      // and use that as the source folder
      srcFolderPath = wce.getContainer().getFullPath().makeRelative();
    }
    else
    {
      srcFolderPath = new Path(srcFolder).makeRelative();
      if (srcFolderPath.segmentCount() == 0)
      {
        String attribute = "sourceFolder"; //$NON-NLS-1$
        final String msg = JET2Messages.AbstractJavaFileTag_EmptyAttributeNotAllowed;
        throw new JET2TagException(MessageFormat.format(msg, new Object []{attribute}));
      }
    }
    
    BodyContentWriter content = new BodyContentWriter();
    final TransformContextExtender tce = TransformContextExtender.getInstance(context);
    tce.execute(template, content);
    if(srcFolderPath != null && pkgName != null)
    {
      wce.addAction(new JavaFileAction(srcFolderPath, pkgName, fileName, content,
        replace, encoding, derived, td, tce.getTemplatePath()));
    }
    else
    {
      throw new JET2TagException(JET2Messages.AbstractJavaFileTag_CouldNotWrite_UnknownSourceFolder);
    }
  }

  protected abstract String getFileName() throws JET2TagException;
}
