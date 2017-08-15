/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *
 * /
 *******************************************************************************/
package org.eclipse.jet.ui.newproject;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.jet.ui.newproject.messages"; //$NON-NLS-1$
	public static String NewJETProjectPage2_description;
	public static String NewJETProjectPage2_InvalidID;
	public static String NewJETProjectPage2_title;
	public static String NewJETProjectPage2Controls__groupgrpTransProps_text;
	public static String NewJETProjectPage2Controls__labellblID_text;
	public static String NewJETProjectPage2Controls__labellblName_text;
	public static String NewJETProjectPage2Controls__labellblDescription_text;
	public static String NewJETProjectPage2Controls__labellblTemplateLoader_text;
	public static String NewJETProjectPage2Controls_grpExtensions_text;
	public static String NewJETProjectPage2Controls_cbxExtends_text;
	public static String NewJETProjectPage2Controls_lblBaseTx_text;
	public static String NewJETProjectPage1Controls__groupgProjSet_text;
	public static String NewJETProjectPage1Controls__labellblTemplateFolder_text;
	public static String NewJETProjectPage1Controls__labellblJETOutput_text;
	public static String NewJETProjectPage1Controls__labellblJavaOutput_text;
	public static String NewJETProjectPage1Controls__labellblDefJavaPackage_text;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
