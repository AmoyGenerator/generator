/**
 * <copyright>
 *
 * Copyright (c) 2009 IBM Corporation and others.
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
 * $Id: ResourceBundleUtil.java,v 1.1 2009/03/06 20:53:09 pelder Exp $
 */
package org.eclipse.jet.internal.taglib.format;


import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.taglib.CustomTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.transform.TransformContextExtender;
import org.eclipse.osgi.util.NLS;


/**
 * Utility class for implementing resource bundle finding for message tags
 */
public class ResourceBundleUtil
{
  private static final String DEFAULT_BUNDLE_VAR = "org.eclipse.jet.taglib.format.resourceBundle"; //$NON-NLS-1$

  private static final String DEFAULT_LOCALE_VAR = "org.eclipse.jet.taglib.format.locale"; //$NON-NLS-1$

  private static ResourceBundle EMPTY_BUNDLE = new ResourceBundle()
    {

      protected Object handleGetObject(String key)
      {
        return null;
      }

      public Enumeration getKeys()
      {
        return null;
      }
    };

  public static ResourceBundle loadBundle(JET2Context context, String baseName)
  {
    final ClassLoader classLoader = TransformContextExtender.getInstance(context).getLoader().getClass().getClassLoader();
    try
    {
      return ResourceBundle.getBundle(baseName, getLocale(context), classLoader);
    }
    catch (MissingResourceException e)
    {
      throw new JET2TagException(NLS.bind(Messages.ResourceBundleUtil_UnableToLoadBundle, baseName), e);
    }
  }

  public static ResourceBundle findBundle(JET2Context context, CustomTag currentTag)
  {
    for (CustomTag parent = currentTag.getParent(); parent != null; parent = parent.getParent())
    {
      if (parent instanceof BundleTag)
      {
        return ((BundleTag)parent).getBundle();
      }
    }
    final Object defaultBundle = context.hasVariable(DEFAULT_BUNDLE_VAR) ? context.getVariable(DEFAULT_BUNDLE_VAR) : null;

    return defaultBundle instanceof ResourceBundle ? (ResourceBundle)defaultBundle : EMPTY_BUNDLE;
  }

  public static void setDefaultBundle(JET2Context context, ResourceBundle bundle)
  {
    context.setVariable(DEFAULT_BUNDLE_VAR, bundle);
  }

  public static void setLocale(JET2Context context, String value, String variant)
  {
    value = value.trim();
    variant = variant.trim();

    final Locale locale;

    final String[] parts = value.split("[_\\-]"); //$NON-NLS-1$
    final String language = parts.length > 0 ? parts[0] : ""; //$NON-NLS-1$
    final String country = parts.length > 1 ? parts[1] : ""; //$NON-NLS-1$

    locale = new Locale(language, country, variant);

    context.setVariable(DEFAULT_LOCALE_VAR, locale);
  }

  private static Locale getLocale(JET2Context context)
  {
    final Object locale = context.hasVariable(DEFAULT_LOCALE_VAR) ? context.getVariable(DEFAULT_LOCALE_VAR) : null;
    
    return locale instanceof Locale ? (Locale)locale : Locale.getDefault();
  }
}
