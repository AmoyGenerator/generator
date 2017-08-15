/**
 * <copyright>
 *
 * Copyright (c) 2007 IBM Corporation and others.
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
 * $Id: ResourceTemplateInput.java,v 1.2 2007/05/01 19:49:11 pelder Exp $
 */
package org.eclipse.jet.internal.parser.resources;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jet.core.parser.ITemplateInput;
import org.eclipse.jet.core.parser.TemplateInputException;

/**
 * Implementation of JET template input based on an Eclipse workspace IResource
 */
public class ResourceTemplateInput implements ITemplateInput
{

  private final IFile file;

  public ResourceTemplateInput(IFile file)
  {
    this.file = file;
  }
  /* (non-Javadoc)
   * @see org.eclipse.jet.tools.parser.ITemplateInput#getBaseLocation()
   */
  public URI getBaseLocation()
  {
    // The following doesn't work - we get a file:/ uri which is inconsistent with FileLocation.resolve(URL) uris
    // return URI.create(file.getProject().getLocationURI().toString() + "/"); //$NON-NLS-1$
    // Instead, return a platform URL:
    try
    {
      return new URI("platform", "/resource/" + file.getProject().getName() + "/", null);
    }
    catch (URISyntaxException e)
    {
      // This convoluted code is Java 1.4 friendly. new IllegalArgumentException(e) is only in 1.5 and later
      IllegalArgumentException wrapper = new IllegalArgumentException();
      wrapper.initCause(e);
      throw wrapper;
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.tools.parser.ITemplateInput#getReader()
   */
  public Reader getReader() throws TemplateInputException
  {
    try
    {
      return new BufferedReader(new InputStreamReader(file.getContents(), file.getCharset()));
    }
    catch (UnsupportedEncodingException e)
    {
      throw new TemplateInputException(e);
    }
    catch (CoreException e)
    {
      throw new TemplateInputException(e);
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.tools.parser.ITemplateInput#getTemplatePath()
   */
  public String getTemplatePath()
  {
    return file.getProjectRelativePath().toString();
  }

  public String getEncoding() throws TemplateInputException
  {
    try
    {
      return file.getCharset();
    }
    catch (CoreException e)
    {
      throw new TemplateInputException(e);
    }
  }

}
