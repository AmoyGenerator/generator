/**
 * <copyright>
 *
 * Copyright (c) 2006, 2008 IBM Corporation and others.
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

package org.eclipse.jet.ui.newproject;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents information required to create a new JET2 Project
 *
 */
public final class NewProjectInfo {
	
	private String projectName;
	private Map otherInfo = null;
	private static final String NL = System.getProperty("line.separator"); //$NON-NLS-1$
	private static final String INDENT = "    "; //$NON-NLS-1$
	private final Map projectAttributes = new LinkedHashMap();

	public NewProjectInfo() {
		// nothing to do
	}
	/**
	 * Return the currently set project Name.
	 * @return the projectName or <code>null</code>
	 */
	public final String getProjectName() {
		return projectName;
	}

	/**
	 * Set the projectName.
	 * @param projectName the projectName to set
	 */
	public final void setProjectName(String projectName) {
		this.projectName = projectName;
		setProjectAttribute("name", projectName); //$NON-NLS-1$
	}
	
	public String toXmlString() {
		if(projectName == null) {
			throw new IllegalStateException("projectName not set"); //$NON-NLS-1$
		}
		StringBuffer xml = new StringBuffer();
		xml.append("<newProjectModel>").append(NL); //$NON-NLS-1$
		xml.append(INDENT).append("<project"); //$NON-NLS-1$
		for (Iterator i = projectAttributes.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			addAttribute(xml, (String)entry.getKey(), (String)entry.getValue());
		}
//		addAttribute(xml, "name", projectName); //$NON-NLS-1$
//		addAttribute(xml, "location", location); //$NON-NLS-1$
		xml.append("/>").append(NL); //$NON-NLS-1$
		if(otherInfo != null) {
			for (Iterator i = otherInfo.values().iterator(); i.hasNext();) {
				String otherElement = (String) i.next();
				xml.append(INDENT).append(otherElement).append(NL);
			}
		}
		xml.append("</newProjectModel>").append(NL); //$NON-NLS-1$
		return xml.toString();
	}
	/**
	 * @param xml
	 * @param name
	 * @param value
	 */
	private void addAttribute(StringBuffer xml, String name, final String value) {
		if(value != null) {
			xml.append(' ').append(name).append("=\"").append(value).append("\""); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	
	public void setOtherInfo(String key, String xmlContent) {
		if(otherInfo == null) {
			otherInfo = new LinkedHashMap();
		}
		if(xmlContent != null) {
			otherInfo.put(key, xmlContent);
		} else {
			otherInfo.remove(key);
		}
		
	}
	public void setLocation(String location) {
		setProjectAttribute("location", location); //$NON-NLS-1$
	}
	
	public void setProjectAttribute(String attributeName, String value) {
		projectAttributes.put(attributeName, value);
	}
}
