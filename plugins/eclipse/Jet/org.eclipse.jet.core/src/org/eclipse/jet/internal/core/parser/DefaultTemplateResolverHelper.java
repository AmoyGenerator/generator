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
 * $Id: DefaultTemplateResolverHelper.java,v 1.3 2007/07/11 18:37:28 pelder Exp $
 */
package org.eclipse.jet.internal.core.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.jet.core.parser.ITemplateInput;
import org.eclipse.jet.core.parser.ITemplateResolverHelper;
import org.eclipse.jet.internal.core.url.URLUtility;

/**
 * @author pelder
 */
public class DefaultTemplateResolverHelper implements ITemplateResolverHelper
{

  private final URI baseLocation;

  public DefaultTemplateResolverHelper(URI baseLocation)
  {
    this.baseLocation = baseLocation;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.parser.resources.ITemplateResolverHelper#createTemplateInput(java.net.URI, java.lang.String)
   */
  public ITemplateInput createTemplateInput(String templatePath)
  {
    String encoding = "UTF-8"; //$NON-NLS-1$
    return new DefaultTemplateInput(baseLocation, templatePath, encoding);
  }

    /**
     * Close an input stream, supressing IOExceptions for which we can do nothing.
     * @param input an input stream
     */
    private void closeStream(InputStream input) {
        try {
            input.close();
        } catch (IOException e) {
            // do nothing
        }
    }

    private InputStream openStream(URI baseLocation, String templatePath) {
        try {
            URI resolvedURI = baseLocation.resolve(new URI(null, templatePath, null));
			URL templateURL = URLUtility.toURL(resolvedURI);
            final InputStream input = templateURL.openStream();
            return input;
        } catch (MalformedURLException e) {
            // ignore;
        } catch (IOException e) {
            // ignore;
        } catch (URISyntaxException e) {
			IllegalArgumentException wrapper = new IllegalArgumentException();
			wrapper.initCause(e);
			throw wrapper;
		}
        return null;
    }


  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.parser.resources.ITemplateResolverHelper#inputExists(java.net.URI, java.lang.String)
   */
  public boolean inputExists(String templatePath)
  {
    boolean exists = false;
    final InputStream input = openStream(baseLocation, templatePath);
    
    if(input != null) {
        exists = true;
        closeStream(input);
    }

    return exists;
  }

}
