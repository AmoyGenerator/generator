/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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

package org.eclipse.jet.internal.ui.launch;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

/**
 * @author pelder
 *
 */
public class JET2TransformationTabGroup extends
		AbstractLaunchConfigurationTabGroup {

	/**
	 * 
	 */
	public JET2TransformationTabGroup() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
//				new JET2TransformMainTab(),
				new JETTransformMainTab(),
				new CommonTab(),
		};
		
		setTabs(tabs);
	}
}
