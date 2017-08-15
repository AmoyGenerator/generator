/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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

package org.eclipse.jet.ui.newproject;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.environments.IExecutionEnvironment;
import org.eclipse.jet.JET2Platform;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.ui.l10n.Messages;
import org.eclipse.jet.transform.IJETBundleDescriptor;
import org.eclipse.jet.ui.Activator;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

/**
 * Implement New Project Wizard for JET2 transforms.
 *
 */
public class NewProjectWizard extends Wizard implements INewWizard {

	private static final String NEW_PROJECT_WIZARD_TX_ID = "org.eclipse.jet.transforms.newproject"; //$NON-NLS-1$
	private NewJETProjectPage1 projectPage;
	
	protected final NewProjectInfo newProjectInfo = new NewProjectInfo();
	private NewJETProjectPage2 projectPropertiesPage;

	/**
	 * 
	 */
	public NewProjectWizard() {
		super();
		setWindowTitle(Messages.NewProjectWizard_WindowTitle);
		setDefaultPageImageDescriptor(Activator.getImageDescriptor("/icons/JET2ProjectWizardBanner.gif")); //$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		try {
			getContainer().run(false, false, new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					IStatus result = JET2Platform.runTransformOnString(NEW_PROJECT_WIZARD_TX_ID, getBuildModel(), "xml", monitor); //$NON-NLS-1$
					if(!result.isOK()) {
						InternalJET2Platform.log(result);
						new ErrorDialog(getShell(), NewProjectWizard.this.getWindowTitle(),
								Messages.NewProjectWizard_ErrorInJETTransform,
								result, IStatus.ERROR | IStatus.WARNING).open();
					} else {
						final IPath mainTemplatePath = new Path(projectPage
								.getProjectName()).append(
								projectPage.getTemplateFolder()).append(
								"main.jet"); //$NON-NLS-1$
						final IFile mainTemplate = ResourcesPlugin
								.getWorkspace().getRoot().getFile(
										mainTemplatePath);
						if (mainTemplate.exists()) {
							try {
								IDE.openEditor(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage(), 
										mainTemplate);
							} catch (PartInitException e) {
								InternalJET2Platform.logError(Messages.NewProjectWizard_ErrorCreatingProject, e);
							}
						}
					}
				}});
		} catch (InvocationTargetException e) {
			InternalJET2Platform.logError(Messages.NewProjectWizard_ErrorCreatingProject, e);
		} catch (InterruptedException e) {
			// cancelled, nothing to do
		}
		return true;
	}

	protected final String getBuildModel() {
		newProjectInfo.setProjectName(projectPage.getProjectName());
		if(!projectPage.useDefaults()) {
			newProjectInfo.setLocation(projectPage.getLocationPath().toString());
		}
		newProjectInfo.setProjectAttribute("binDir", projectPage.getJavaOutputFolder()); //$NON-NLS-1$
		newProjectInfo.setProjectAttribute("jet2javaDir", projectPage.getJETOutputFolder()); //$NON-NLS-1$
		newProjectInfo.setProjectAttribute("templatesDir", projectPage.getTemplateFolder()); //$NON-NLS-1$
		newProjectInfo.setProjectAttribute("bundleName", projectPropertiesPage.getTransformName()); //$NON-NLS-1$
		newProjectInfo.setProjectAttribute("bundleSymbolicName", projectPropertiesPage.getTransformID()); //$NON-NLS-1$
		newProjectInfo.setProjectAttribute("dfltJavaPackage", projectPage.getDefaultJavaPackage()); //$NON-NLS-1$
		newProjectInfo.setProjectAttribute("description", projectPropertiesPage.getTransformDescription()); //$NON-NLS-1$
		newProjectInfo.setProjectAttribute("templateLoader", projectPropertiesPage.getTemplateLoader()); //$NON-NLS-1$
		final String defaultEE = getDefaultExecutionEnv();
		if(defaultEE != null) {
			newProjectInfo.setProjectAttribute("executionEnv", defaultEE); //$NON-NLS-1$
			newProjectInfo.setProjectAttribute("useJava5", Boolean.toString(isDefaultEEJava5Compatible())); //$NON-NLS-1$
		}
		if(projectPropertiesPage.isExtension()) {
			newProjectInfo.setProjectAttribute("baseID", projectPropertiesPage.getBaseTransformID()); //$NON-NLS-1$
			final IJETBundleDescriptor descriptor = JET2Platform.getJETBundleManager().getDescriptor(projectPropertiesPage.getBaseTransformID());
			if(descriptor != null) {
				newProjectInfo.setProjectAttribute("baseStartTemplate", descriptor.getMainTemplate()); //$NON-NLS-1$
			}
		}
		
		return newProjectInfo.toXmlString();
	}

	private boolean isDefaultEEJava5Compatible() {

		final IVMInstall defaultVMInstall = JavaRuntime.getDefaultVMInstall();
		final IExecutionEnvironment java5ee = JavaRuntime.getExecutionEnvironmentsManager().getEnvironment("J2SE-1.5"); //$NON-NLS-1$
		return Arrays.asList(java5ee.getCompatibleVMs()).contains(defaultVMInstall);
	}

	private String getDefaultExecutionEnv() {
		final IVMInstall defaultVMInstall = JavaRuntime.getDefaultVMInstall();
		final IExecutionEnvironment[] ee = JavaRuntime.getExecutionEnvironmentsManager().getExecutionEnvironments();
		for (int i = 0; i < ee.length; i++) {
			if(ee[i].isStrictlyCompatible(defaultVMInstall)) {
				return ee[i].getId();
			}
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// nothing to do here
	}

	public void addPages() {
		super.addPages();
		
		projectPage= new NewJETProjectPage1(Messages.NewProjectWizard_MainPageTabText);
		projectPage.setTitle(Messages.NewProjectWizard_MainPageTitle);
		projectPage.setDescription(Messages.NewProjectWizard_MainPageDescription);
		addPage(projectPage);
		
		projectPropertiesPage = new NewJETProjectPage2(Messages.NewProjectWizard_MainPageTabText);
		projectPropertiesPage.setJETProjectPage1(projectPage);
		projectPropertiesPage.showExtensionsGroup(allowExtensionSpecification());
		addPage(projectPropertiesPage);
	}
	
	
	
	/**
	 * Indicates whether the wizard will allow specification of JET transformation extensions.
	 * Clients may override this method. Default implementation returns <code>true</code>.
	 * @return <code>true</code> if extension transformations will be allowed.
	 */
	protected boolean allowExtensionSpecification() {
		return true;
	}

	public boolean canFinish() {
		final IWizardPage currentPage = getContainer().getCurrentPage();
		return super.canFinish() && currentPage != projectPage;
	}
	
}
