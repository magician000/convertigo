/*
 * Copyright (c) 2001-2022 Convertigo SA.
 * 
 * This program  is free software; you  can redistribute it and/or
 * Modify  it  under the  terms of the  GNU  Affero General Public
 * License  as published by  the Free Software Foundation;  either
 * version  3  of  the  License,  or  (at your option)  any  later
 * version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY;  without even the implied warranty of
 * MERCHANTABILITY  or  FITNESS  FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program;
 * if not, see <http://www.gnu.org/licenses/>.
 */

package com.twinsoft.convertigo.eclipse.popup.actions;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.twinsoft.convertigo.beans.core.MobileApplication;
import com.twinsoft.convertigo.beans.core.MobilePlatform;
import com.twinsoft.convertigo.eclipse.ConvertigoPlugin;
import com.twinsoft.convertigo.eclipse.dialogs.BuildLocallyEndingDialog;
import com.twinsoft.convertigo.eclipse.property_editors.MobileApplicationEndpointEditorComposite;
import com.twinsoft.convertigo.eclipse.views.projectexplorer.ProjectExplorerView;
import com.twinsoft.convertigo.eclipse.views.projectexplorer.model.TreeObject;
import com.twinsoft.convertigo.engine.Engine;
import com.twinsoft.convertigo.engine.ProductVersion;
import com.twinsoft.convertigo.engine.localbuild.BuildLocally;

public class BuildLocallyAction extends MyAbstractAction {
	
	private BuildLocally buildLocally = null;
	private Shell parentShell = null;
	private MobilePlatform mobilePlatform = null;
	
	public BuildLocallyAction() {
		super();
	}

	@Override
	public void run() {
		String actionID = action.getId();
		Engine.logEngine.debug("Running " + actionID + " action");
		parentShell = getParentShell();
		mobilePlatform = getMobilePlatform();
		final String buildDir = ConvertigoPlugin.getLocalBuildFolder();
		
		buildLocally = new BuildLocally(getMobilePlatform()) {
			
			@Override
			protected void logException(Throwable e, String message) {
				ConvertigoPlugin.logException(e, message);
			}
			
			@Override
			protected String getLocalBuildAdditionalPath() {
				return ConvertigoPlugin.getLocalBuildAdditionalPath();
			}
			
			@Override
			protected void showLocationInstallFile(final MobilePlatform mobilePlatform, 
					final int exitValue, final String errorLines, final String buildOption) {
				
				ConvertigoPlugin.getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						File builtFile = getAbsolutePathOfBuiltFile(mobilePlatform, buildOption);
						
						BuildLocallyEndingDialog buildSuccessDialog = new BuildLocallyEndingDialog(
							ConvertigoPlugin.getMainShell(), builtFile, exitValue, errorLines, mobilePlatform
						);
						
						buildSuccessDialog.open();
					}
		        });
			}
			
			@Override
			public File getCordovaDir() {
				return new File(buildDir + "/"
					+ ProductVersion.productVersion + "/"
					+ mobilePlatform.getProject().getName() + "/"
					+ mobilePlatform.getName());
			}
		};
		
		if (actionID.equals("convertigo.action.buildLocallyRelease")){
			build("release", false, "");
		}
		
		if (actionID.equals("convertigo.action.buildLocallyDebug")){
			build("debug", false, "");
		}
		
		if (actionID.equals("convertigo.action.runLocally")){
			build("debug", true, "device");
		}
		
		if (actionID.equals("convertigo.action.emulateLocally")){
			build("debug", true, "emulator");
		}
		if (actionID.equals("convertigo.action.removeCordovaDirectory")){
			buildLocally.removeCordovaDirectory();
		}
	}
	
	/***
	 * Function which made the build
	 * @param option
	 * @param run
	 * @param target
	 */
	public void build(final String option, final boolean run, final String target) {
		Cursor waitCursor = null;
		
		if (parentShell != null) {
			waitCursor = new Cursor(ConvertigoPlugin.getDisplay(), SWT.CURSOR_WAIT);
			parentShell.setCursor(waitCursor);
		}
		
		try {
			if (mobilePlatform != null) {
				//Check endpoint url is empty or not
				MobileApplication mobileApplication = mobilePlatform.getParent();
				String exEndpoint[] = {mobileApplication.getEndpoint()};
				String curEndpoint[] = {exEndpoint[0]};
				
				try {
					MobileApplicationEndpointEditorComposite[] mob = new MobileApplicationEndpointEditorComposite[1];
					Dialog dialog = new Dialog(parentShell) {
						
						@Override
						protected void createButtonsForButtonBar(Composite parent) {
							createButton(parent, IDialogConstants.OK_ID, "Build and Restore", true);
							createButton(parent, IDialogConstants.FINISH_ID, "Build and Keep", false);
							createButton(parent, IDialogConstants.CANCEL_ID, "Cancel Build", false);
						}
						
						@Override
						protected void buttonPressed(int buttonId) {
							super.buttonPressed(buttonId);
							if (IDialogConstants.FINISH_ID == buttonId) {
								setReturnCode(buttonId);
								close();
							}
						}
						
						@Override
						protected Control createDialogArea(Composite parent) {
							Composite composite = (Composite) super.createDialogArea(parent);
							mob[0] = new MobileApplicationEndpointEditorComposite(composite, SWT.NONE, mobileApplication);
							return composite;
						}
						
					};
					int code = dialog.open();
					if (code == Dialog.OK || code == IDialogConstants.FINISH_ID) {
						curEndpoint[0] = mob[0].getValue();
					} else {
						return;
					}
					if (code == IDialogConstants.FINISH_ID) {
						mobileApplication.setEndpoint(curEndpoint[0]);
						mobileApplication.hasChanged = true;
						exEndpoint[0] = null;
					}
				} catch (Exception e) {
				}
				
				if (curEndpoint[0].equals("")) {
					if (parentShell != null) {
						MessageBox informDialog = new MessageBox(parentShell, SWT.ICON_INFORMATION | SWT.OK);
						informDialog.setText("Endpoint URL is empty");
						informDialog.setMessage(
							"You need to have an endpoint URL to continue the local build.\n" +
							"Please enter a valid endpoint URL in the property \"Convertigo server endpoint\" present on \"" + 
							mobileApplication.getName() + "\" object.");
						informDialog.open();
					} else {
						//TODO
					}
					return;
				}

				// OK we are sure we have a Cordova environment.. Start the build
				Job buildJob = new Job("Local Cordova Build " + (run ? "and Run " : "") + "in progress...") {
					
					@Override
					protected IStatus run(IProgressMonitor progressMonitor) {

						BuildLocally.Status status = buildLocally.installCordova();
						if (status == BuildLocally.Status.CANCEL) {
							return org.eclipse.core.runtime.Status.CANCEL_STATUS;
						}

						try {
							mobileApplication.setEndpoint(curEndpoint[0]);
							status = buildLocally.runBuild(option, run, target);
							if (status == BuildLocally.Status.OK) {
								return org.eclipse.core.runtime.Status.OK_STATUS;
							}
							return org.eclipse.core.runtime.Status.CANCEL_STATUS;
						} finally {
							if (exEndpoint[0] != null) {
								mobileApplication.setEndpoint(exEndpoint[0]);
							}
						}
					}

					@Override
					protected void canceling() {
						buildLocally.cancelBuild(run);
					}

				};

				buildJob.setUser(true);
				buildJob.schedule();

			}
//		} catch (IOException ee) {
//			MessageBox customDialog = new MessageBox(
//					parentShell,
//					SWT.ICON_INFORMATION | SWT.OK);
//			customDialog.setText("Cordova installation not found");
//			customDialog.setMessage("In order to use local build you must install on your workstation a valid" +
//					"Cordova build system.\n You can download and install Cordova from: \n" +
//					"http://cordova.apache.org \nBe sure to follow all instruction on Cordova\n" +
//					"Website to setup your local Cordova build system. \n\n" +
//					"This message can also appear if cordova is not in your PATH."
//					);
//			customDialog.open();
		} catch (Throwable e) {
			ConvertigoPlugin.logException(e, "Unable to build locally with Cordova"/*, !buildLocally.isProcessCanceled()*/);
		}
		finally {
			parentShell.setCursor(null);
			waitCursor.dispose();
		}
	}

	private MobilePlatform getMobilePlatform() {
		ProjectExplorerView explorerView = getProjectExplorerView();
		
		if (explorerView != null) {
			TreeObject treeObject = explorerView.getFirstSelectedTreeObject();
			Object databaseObject = treeObject.getObject();

			if ((databaseObject != null) && (databaseObject instanceof MobilePlatform)) {
				 return (MobilePlatform) treeObject.getObject();
			} 
		}
		return null;
	}
	
	/***
	 * Dialog yes/no which ask to user if we want
	 * remove the cordova directory present into "_private" directory
	 * We also explain, what we do and how to recreate the cordova environment
	 */
	public void removeCordovaDirectory() {
		String mobilePlatformName = mobilePlatform.getName();
		if (parentShell != null) {
			MessageBox customDialog = new MessageBox(parentShell, SWT.ICON_INFORMATION | SWT.YES | SWT.NO);
	    	
			customDialog.setText("Remove cordova directory");
	    	customDialog.setMessage("Do you want to remove the Cordova directory located in \"_private\\localbuild\\" + 
	    			mobilePlatformName + "\" directory?\n\n" +
					"It will also remove this project's Cordova environment!\n\n" +
					"To recreate the project's Cordova environment, you just need to run a new local build."
			);
			
			if (customDialog.open() == SWT.YES) {
				buildLocally.removeCordovaDirectory();
			} else {
				return;
			}	
		} else {
			//TODO
		}
	}
}
