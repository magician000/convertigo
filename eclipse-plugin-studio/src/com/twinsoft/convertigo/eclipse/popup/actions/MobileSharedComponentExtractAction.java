/*
 * Copyright (c) 2001-2021 Convertigo SA.
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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.twinsoft.convertigo.beans.core.DatabaseObject;
import com.twinsoft.convertigo.beans.mobile.components.ApplicationComponent;
import com.twinsoft.convertigo.beans.mobile.components.UIComponent;
import com.twinsoft.convertigo.beans.mobile.components.UIDynamicAction;
import com.twinsoft.convertigo.beans.mobile.components.UIElement;
import com.twinsoft.convertigo.beans.mobile.components.UIUseShared;
import com.twinsoft.convertigo.eclipse.ConvertigoPlugin;
import com.twinsoft.convertigo.eclipse.editors.CompositeEvent;
import com.twinsoft.convertigo.eclipse.views.projectexplorer.ProjectExplorerView;
import com.twinsoft.convertigo.eclipse.views.projectexplorer.model.DatabaseObjectTreeObject;
import com.twinsoft.convertigo.eclipse.views.projectexplorer.model.TreeObject;
import com.twinsoft.convertigo.eclipse.wizards.new_mobile.SharedComponentWizard;

public class MobileSharedComponentExtractAction extends MyAbstractAction {
	public MobileSharedComponentExtractAction() {
		super();
	}
	
	public void selectionChanged(IAction action, ISelection selection) {
		try {
			boolean enable = false;
			super.selectionChanged(action, selection);
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.size() == 1) {
				TreeObject treeObject = (TreeObject) structuredSelection.getFirstElement();
				if (treeObject instanceof DatabaseObjectTreeObject) {
					DatabaseObjectTreeObject doto = (DatabaseObjectTreeObject) treeObject;
					if (doto.isEnabled() && !doto.hasAncestorDisabled()) {
						DatabaseObject dbo = doto.getObject();
						if (dbo instanceof UIElement) {
							UIElement uie = (UIElement)dbo;
							boolean isUIUseShared = uie instanceof UIUseShared;
							boolean isUIDynamicAction = uie instanceof UIDynamicAction;
							boolean isInForm = uie.getUIForm() != null && !uie.equals(uie.getUIForm());
							
							if (!isUIUseShared && !isUIDynamicAction && !isInForm) {
								enable = true;
							}
						}
					}
				}
			}
			action.setEnabled(enable);
		}
		catch (Exception e) {}
	}
	
	public void run() {
		Display display = Display.getDefault();
		Cursor waitCursor = new Cursor(display, SWT.CURSOR_WAIT);		
		
		Shell shell = getParentShell();
		shell.setCursor(waitCursor);
		
        try {
        	TreeObject appTreeObject = null;
     		TreeObject selectedTreeObject = null;
     		DatabaseObjectTreeObject parentTreeObject = null;
    		DatabaseObject databaseObject = null;
    		
    		ProjectExplorerView explorerView = getProjectExplorerView();
    		if (explorerView != null) {
    			selectedTreeObject = explorerView.getFirstSelectedTreeObject();
   				databaseObject = (DatabaseObject) selectedTreeObject.getObject();
   				if (!(databaseObject instanceof UIComponent))
   					throw new Exception("Invalid selection");
   				
    			appTreeObject = getAppTreeObject(selectedTreeObject);
    			if (appTreeObject == null) {
    				throw new Exception("Unable to retrieve target application");
    			}
    			
   				parentTreeObject = ((DatabaseObjectTreeObject)selectedTreeObject).getParentDatabaseObjectTreeObject();
   				
    			SharedComponentWizard newObjectWizard = new SharedComponentWizard(databaseObject);
        		WizardDialog wzdlg = new WizardDialog(shell, newObjectWizard);
        		wzdlg.setPageSize(850, 650);
        		wzdlg.open();
        		int result = wzdlg.getReturnCode();
        		if ((result != Window.CANCEL) && (newObjectWizard.newBean != null)) {
        			if (databaseObject.getParent() == null) {
        				parentTreeObject.removeChild(selectedTreeObject);
        			}
        			explorerView.reloadTreeObject(appTreeObject);
        			explorerView.objectSelected(new CompositeEvent(newObjectWizard.newBean));
        		}
    		}
        }
        catch (Throwable e) {
        	ConvertigoPlugin.logException(e, "Unable to create a new shared component!");
        }
        finally {
			shell.setCursor(null);
			waitCursor.dispose();
        }
	}

	private TreeObject getAppTreeObject(TreeObject parentTreeObject) {
		while (parentTreeObject != null) {
			Class<?> c = parentTreeObject.getObject().getClass();
			if (c.equals(ApplicationComponent.class)) {
				return parentTreeObject;
			}
			parentTreeObject = parentTreeObject.getParent();
		}
		return null;
	}
}
