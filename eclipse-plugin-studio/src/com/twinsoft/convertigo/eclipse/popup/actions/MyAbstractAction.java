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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.twinsoft.convertigo.beans.core.Connector;
import com.twinsoft.convertigo.beans.core.Transaction;
import com.twinsoft.convertigo.eclipse.ConvertigoPlugin;
import com.twinsoft.convertigo.eclipse.editors.connector.ConnectorEditorInput;
import com.twinsoft.convertigo.eclipse.editors.jscript.JScriptEditorInput;
import com.twinsoft.convertigo.eclipse.views.projectexplorer.ProjectExplorerView;
import com.twinsoft.convertigo.engine.Engine;
import com.twinsoft.convertigo.engine.helpers.BatchOperationHelper;
import com.twinsoft.convertigo.engine.mobile.MobileBuilder;
import com.twinsoft.convertigo.engine.util.GenericUtils;

public abstract class MyAbstractAction extends Action implements IObjectActionDelegate {
	protected ISelection selection = null;
	protected IWorkbenchPart targetPart = null;
	protected IAction action = null;
	
	public MyAbstractAction() {
		super();
		this.action = this;
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.targetPart = targetPart;
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public String getId() {
		String id = super.getId();
		if (id == null) {
			id = this.getClass().getSimpleName();
		}
		return id;
	}

	@Override
	public void runWithEvent(Event event) {
		run(this);
	}

	@Override
	public void run(IAction action) {
		this.action = action;
		
		MobileBuilder mb = null;
		
		IEditorPart editorPart = ConvertigoPlugin.getDefault().getApplicationComponentEditor();
		if (editorPart != null) {
			IEditorInput input = editorPart.getEditorInput();
			if (input instanceof com.twinsoft.convertigo.eclipse.editors.mobile.ApplicationComponentEditorInput) {
				com.twinsoft.convertigo.eclipse.editors.mobile.ApplicationComponentEditorInput editorInput = GenericUtils.cast(input);
				mb = editorInput.getApplication().getProject().getMobileBuilder();
			}
			if (input instanceof com.twinsoft.convertigo.eclipse.editors.ngx.ApplicationComponentEditorInput) {
				com.twinsoft.convertigo.eclipse.editors.ngx.ApplicationComponentEditorInput editorInput = GenericUtils.cast(input);
				mb = editorInput.getApplication().getProject().getMobileBuilder();
			}
		}
		
		try {
			Engine.logStudio.info("---------------------- Action started: "+ action.getId() + "----------------------");
			if (mb != null) {
				mb.prepareBatchBuild();
			}
			BatchOperationHelper.start();
			
			run();
			
			BatchOperationHelper.stop();
		} finally {
			BatchOperationHelper.cancel();
			Engine.logStudio.info("---------------------- Action ended:   "+ action.getId() + "----------------------");
		}
	}
	
	@Override
	public void run() {
		Display display = Display.getDefault();
		Cursor waitCursor = new Cursor(display, SWT.CURSOR_WAIT);		
		
		Shell shell = getParentShell();
		shell.setCursor(waitCursor);
		
		MessageDialog.openInformation(
			shell,
			"Convertigo Plug-in",
			"The choosen operation is not yet implemented : '"+ action.getId() + "'.");
		
		shell.setCursor(null);
		waitCursor.dispose();
	}
	
	public Shell getParentShell() {
		return targetPart == null ? ConvertigoPlugin.getMainShell() : targetPart.getSite().getShell();
	}
	
	public IWorkbenchPage getActivePage() {
		return PlatformUI
				.getWorkbench()
				.getActiveWorkbenchWindow()
				.getActivePage();
	}
	
	public IWorkbenchPart getActivePart() {
		if (targetPart == null)
			return getActivePage().getActivePart();
		else
			return targetPart;
	}
	
	public ProjectExplorerView getProjectExplorerView() {
		ProjectExplorerView projectExplorerView = null;
		
		if ((targetPart != null) && (targetPart instanceof ProjectExplorerView)) {
			projectExplorerView = (ProjectExplorerView)targetPart;
		}
		else {
			projectExplorerView = ConvertigoPlugin.getDefault().getProjectExplorerView();
		}
		return projectExplorerView;
	}
	
	public IEditorPart getConnectorEditor(Connector connector) {
		IEditorPart editorPart = null;
		IWorkbenchPage activePage = getActivePage();
		if (activePage != null) {
			if (connector != null) {
				IEditorReference[] editorRefs = activePage.getEditorReferences();
				for (int i=0;i<editorRefs.length;i++) {
					IEditorReference editorRef = (IEditorReference)editorRefs[i];
					try {
						IEditorInput editorInput = editorRef.getEditorInput();
						if ((editorInput != null) && (editorInput instanceof ConnectorEditorInput)) {
							if (((ConnectorEditorInput)editorInput).is(connector)) {
								editorPart = editorRef.getEditor(false);
								break;
							}
						}
					}
					catch(PartInitException e) {
						//ConvertigoPlugin.logException(e, "Error while retrieving the connector editor '" + editorRef.getName() + "'");
					}
				}
			}
		}
		return editorPart;
	}

	public IEditorPart getJscriptTransactionEditor(Transaction transaction) {
		IEditorPart editorPart = null;
		IWorkbenchPage activePage = getActivePage();
		if (activePage != null) {
			if (transaction != null) {
				IEditorReference[] editorRefs = activePage.getEditorReferences();
				for (int i = 0; i < editorRefs.length; i++) {
					IEditorReference editorRef = (IEditorReference)editorRefs[i];
					try {
						IEditorInput editorInput = editorRef.getEditorInput();
						if ((editorInput != null) && (editorInput instanceof JScriptEditorInput)) {
							if (transaction.equals(((JScriptEditorInput) editorInput).getDatabaseObject())) {
								editorPart = editorRef.getEditor(false);
								break;
							}
						}
					} catch(PartInitException e) {
						//ConvertigoPlugin.logException(e, "Error while retrieving the jscript transaction editor '" + editorRef.getName() + "'");
					}
				}
			}
		}
		return editorPart;
	}
}
