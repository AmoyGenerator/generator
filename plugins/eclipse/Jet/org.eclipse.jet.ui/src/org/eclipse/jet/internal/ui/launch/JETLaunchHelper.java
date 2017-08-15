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
package org.eclipse.jet.internal.ui.launch;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jet.JET2Platform;
import org.eclipse.jet.internal.ui.l10n.Messages;
import org.eclipse.jet.transform.IJETBundleDescriptor;
import org.eclipse.jet.transform.JETLaunchConstants;
import org.eclipse.jet.ui.Activator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class JETLaunchHelper {
	
	private static ILaunchConfigurationType jetLaunchType = getConfigurationType();
	/**
	 * @param resource
	 * @param mode
	 */
	public static ILaunchConfiguration createConfig(String id,
			IResource resource) {
		try {
			final ILaunchConfigurationWorkingCopy wConfig = jetLaunchType
					.newInstance(null, generateLaunchName(id, resource,
							Messages.LaunchShortcut_DefaultLaunchName));
			wConfig.setAttribute(JETLaunchConstants.ID, id);
			wConfig.setAttribute(JETLaunchConstants.SOURCE, 
					resource != null 
						? resource.getFullPath().toString()
						: ""); //$NON-NLS-1$
			final ILaunchConfiguration config = wConfig.doSave();
			return config;
		} catch (CoreException e) {
			throw convertToRuntimeException(e);
		}
	}

	public static void findAndLaunchForTransformAndResource(Shell shell, String mode, String transformId, IResource resource) {
		ILaunchConfiguration[] configs = findConfigsForTransformAndResource(mode, transformId, resource);
		ILaunchConfiguration configToLaunch;
		switch(configs.length) {
		case 0:
			configToLaunch = createConfig(transformId, resource);
			break;
		case 1:
			configToLaunch = configs[0];
			break;
		default:
			configToLaunch = chooseExistingConfig(shell, configs);
			break;
		}
		if(configToLaunch != null) {
			DebugUITools.launch(configToLaunch, mode);
		}
	}
	
	public static boolean findAndLaunchForTransform(Shell shell, String mode, String transformId, boolean showConfigFirst) {
		ILaunchConfiguration[] configs = findConfigsForTransform(transformId);
		ILaunchConfiguration configToLaunch = null;
		switch(configs.length) {
		case 0:
			IResource resource = chooseResource(shell);
			if(resource != null) {
				configToLaunch = createConfig(transformId, resource);
			}
			break;
		case 1:
			configToLaunch = configs[0];
			break;
		default:
			configToLaunch = chooseExistingConfig(shell, configs);
			break;
		}
		if(configToLaunch != null) {
			boolean doLaunch = true;
			if(showConfigFirst) {
				int dialogResult = DebugUITools.openLaunchConfigurationDialog(shell, configToLaunch, 
						IDebugUIConstants.ID_RUN_LAUNCH_GROUP, null);
				doLaunch = dialogResult != Window.CANCEL;
			}
			if(doLaunch) {
				DebugUITools.launch(configToLaunch, mode);
			}
		}
		return configToLaunch != null;
	}
	
	private static IResource chooseResource(Shell shell) {
		IResource resource = null;
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
				shell, new WorkbenchLabelProvider(),
				new WorkbenchContentProvider());

		dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
		dialog.setAllowMultiple(false);
		dialog.setTitle(Messages.JETTransformMainTab_SelectResourceDialogTitle);
		dialog.setMessage(Messages.JETTransformMainTab_SelectResourceDialogTitle);
		dialog.setValidator(new ISelectionStatusValidator() {
			public IStatus validate(Object[] selection) {
				if (selection.length > 0) {
					return new Status(IStatus.OK, Activator.getDefault()
							.getBundle().getSymbolicName(), IStatus.OK,
							"", null); //$NON-NLS-1$
				}

				return new Status(IStatus.ERROR, Activator.getDefault()
						.getBundle().getSymbolicName(), IStatus.ERROR, "", null); //$NON-NLS-1$
			}
		});
		if (dialog.open() == ElementTreeSelectionDialog.OK) {
			resource = (IResource) dialog.getFirstResult();
		}
		return resource;
	}

	public static boolean findAndLauchForResource(Shell shell, String mode, IResource resource) {
		ILaunchConfiguration[] configs = findConfigsForResource(mode, resource);
		ILaunchConfiguration configToLaunch = null;
		switch(configs.length) {
		case 0:
//			MessageDialog.openError(shell, "Run JET Transform", "No JET transforms found.");
			IJETBundleDescriptor jetBundleDesc = JET2Platform.getProjectDescription(resource.getProject().getName());
			if(jetBundleDesc != null) {
				configToLaunch = createConfig(jetBundleDesc.getId(), resource);
			} else {
				configToLaunch = createConfig("", resource); //$NON-NLS-1$
				if(configToLaunch != null) {
					int dialogResult = DebugUITools.openLaunchConfigurationPropertiesDialog(shell, configToLaunch, 
							IDebugUIConstants.ID_RUN_LAUNCH_GROUP, null);
					if(dialogResult == Window.CANCEL) {
						try {
							configToLaunch.delete();
						} catch (CoreException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						configToLaunch = null;
					} else {
						configs = findConfigsForResource(mode, resource);
						configToLaunch = configs.length > 0 ? configs[0] : null;
					}
				}
			}
			break;
		case 1:
			configToLaunch = configs[0];
			break;
		default:
			configToLaunch = chooseExistingConfig(shell, configs);
			break;
		}
		if(configToLaunch != null) {
			DebugUITools.launch(configToLaunch, mode);
		}
		return configToLaunch != null;
	}
	
	private static ILaunchConfiguration chooseExistingConfig(Shell shell,
			ILaunchConfiguration[] configs) {
		ILabelProvider labelProvider = new LabelProvider() {

			public String getText(Object element) {
				return ((ILaunchConfiguration)element).getName();
			} };
			
		ElementListSelectionDialog dlg = new ElementListSelectionDialog(shell, labelProvider);
		dlg.setElements(configs);
		dlg.setTitle(Messages.LaunchShortcut_DefaultLaunchName);
		dlg.setMessage(Messages.LaunchShortcut_DefaultLaunchName);
		dlg.setMultipleSelection(false);
		int result = dlg.open();
		labelProvider.dispose();
		if (result == Window.OK) {
			return (ILaunchConfiguration) dlg.getFirstResult();
		}
		return null;		
	}

	public static ILaunchConfiguration[] findConfigsForResource(String mode, IResource resource) {
		final List finds = new ArrayList();
		final ILaunchConfiguration[] configs = getJETLaunchConfigs();
		final IPath fullPath = resource.getFullPath().makeRelative();
		for (int i = 0; i < configs.length; i++) {
			if(fullPath.equals(new Path(getSource(configs[i])).makeRelative())){
				finds.add(configs[i]);
			}
		}
		return (ILaunchConfiguration[]) finds.toArray(new ILaunchConfiguration[finds.size()]);
	}

	public static ILaunchConfiguration[] findConfigsForTransformAndResource(String mode, String transformId, IResource resource) {
		final List finds = new ArrayList();
		final ILaunchConfiguration[] configs = getJETLaunchConfigs();
		final String fullPath = resource.getFullPath().toString();
		for (int i = 0; i < configs.length; i++) {
			if(fullPath.equals(getSource(configs[i]))
					&& transformId.equals(getTransformID(configs[i]))){
				finds.add(configs[i]);
			}
		}
		
		return (ILaunchConfiguration[]) finds.toArray(new ILaunchConfiguration[finds.size()]);
	}
	
	public static ILaunchConfiguration[] findConfigsForTransform(String transformId) {
		final ILaunchConfiguration[] configs = getJETLaunchConfigs();
		List finds = new ArrayList();
		for (int i = 0; i < configs.length; i++) {
			if(transformId.equals(getTransformID(configs[i]))){
				finds.add(configs[i]);
			}
		}
		return (ILaunchConfiguration[]) finds.toArray(new ILaunchConfiguration[finds.size()]);
	}


	private static String getSource(ILaunchConfiguration config) {
		try {
			return config.getAttribute(JETLaunchConstants.SOURCE, (String)null);
		} catch (CoreException e) {
			throw convertToRuntimeException(e);
		}
	}

	private static String getTransformID(ILaunchConfiguration config) {
		try {
			return config.getAttribute(JETLaunchConstants.ID, (String)null);
		} catch (CoreException e) {
			throw convertToRuntimeException(e);
		}
	}

	private static RuntimeException convertToRuntimeException(CoreException e) {
		return new RuntimeException("Unexpected exception", e); //$NON-NLS-1$
	}

	private static ILaunchConfiguration[] getJETLaunchConfigs() {
		try {
			ILaunchManager lm = DebugPlugin.getDefault().getLaunchManager();
			return lm.getLaunchConfigurations(jetLaunchType);
		} catch (CoreException e) {
			throw convertToRuntimeException(e);
		}
	}
	
	private static ILaunchConfigurationType getConfigurationType() {
		ILaunchManager lm= DebugPlugin.getDefault().getLaunchManager();
		return lm.getLaunchConfigurationType(JETLaunchConstants.CONFIG_ID);		
	}

	public static String generateLaunchName(String id, IResource input, String defaultName)
	{
		ILaunchManager lm = DebugPlugin.getDefault().getLaunchManager();
		String prefix;
		if (id == null && input == null) {
			prefix = defaultName;
		} else if(input == null) {
			prefix = id;
		} else if(id == null)
		{
			prefix = input.getName();
		} else {
			prefix = id + " (" + input.getName() + ")";  //$NON-NLS-1$//$NON-NLS-2$
		}
	
		return lm.generateUniqueLaunchConfigurationNameFrom(prefix);
	}

	public static ILaunchConfiguration[] findAllConfigs() {
		return getJETLaunchConfigs();
	}

}
