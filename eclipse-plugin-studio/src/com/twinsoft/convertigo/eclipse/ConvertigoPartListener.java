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

package com.twinsoft.convertigo.eclipse;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.internal.console.ConsoleView;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.views.properties.PropertySheet;

import com.twinsoft.convertigo.eclipse.editors.connector.ConnectorEditor;
import com.twinsoft.convertigo.eclipse.editors.jscript.JScriptEditorInput;
import com.twinsoft.convertigo.eclipse.editors.sequence.SequenceEditor;
import com.twinsoft.convertigo.eclipse.swt.SwtUtils;
import com.twinsoft.convertigo.eclipse.views.projectexplorer.ProjectExplorerView;
import com.twinsoft.convertigo.eclipse.views.sourcepicker.SourcePickerView;
import com.twinsoft.convertigo.engine.mobile.MobileBuilder;
import com.twinsoft.convertigo.engine.util.GenericUtils;


@SuppressWarnings("restriction")
public class ConvertigoPartListener implements IPartListener {

	/**
	 * The Convertigo listener for Parts events.
	 */
	public ConvertigoPartListener() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener#partActivated(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partActivated(IWorkbenchPart part) {
		if (part instanceof PropertySheet) {
			PropertySheet view = (PropertySheet)part;
			if (view != null) {
				if (view.getCurrentPage().getControl() instanceof Tree) {
					Tree tree = (Tree) view.getCurrentPage().getControl();
					if (tree != null) {
						if (SwtUtils.isDark()) {
							tree.setLinesVisible(false);
						}
						try {
							tree.getColumn(1).setWidth(tree.getBounds().width);
						} catch (Exception e) {
							
						}
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener#partBroughtToTop(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partBroughtToTop(IWorkbenchPart part) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener#partClosed(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partClosed(IWorkbenchPart part) {
		if (part instanceof ConnectorEditor) {
			((ConnectorEditor)part).close();// close editor properly
		}
		if (part instanceof SequenceEditor) {
			((SequenceEditor)part).close();// close editor properly
		}
		if (part instanceof ProjectExplorerView) {
			((ProjectExplorerView)part).close();// close view properly
		}
		if (part instanceof SourcePickerView) {
			((SourcePickerView)part).close();// close view properly
		}
		if (part instanceof ConsoleView) {
    		ConvertigoPlugin convertigoPlugin = ConvertigoPlugin.getDefault();
    		
			boolean shuttingDown = convertigoPlugin.isShuttingDown();
			if (shuttingDown) {
				IConsole[] tabConsoles = ConsolePlugin.getDefault().getConsoleManager().getConsoles();
        		String openedConsoles = "";
        		
				for (IConsole console : tabConsoles) {
					if (console instanceof MessageConsole) {
		        		if ((console.equals(convertigoPlugin.engineConsole)) && (openedConsoles.indexOf("engine") == -1))
		        			openedConsoles += (openedConsoles.equals("") ? "":",") + "engine";
		        		else if ((console.equals(convertigoPlugin.stdoutConsole)) && (openedConsoles.indexOf("stdout") == -1))
		        			openedConsoles += (openedConsoles.equals("") ? "":",") + "stdout";
		        	}
				}

				ConvertigoPlugin.setProperty(ConvertigoPlugin.PREFERENCE_OPENED_CONSOLES, openedConsoles);
			}
		}
		
		if (part instanceof EditorPart) {
			IEditorInput input = ((EditorPart)part).getEditorInput();
			if (input instanceof com.twinsoft.convertigo.eclipse.editors.mobile.ApplicationComponentEditorInput) {
				try {
					com.twinsoft.convertigo.eclipse.editors.mobile.ApplicationComponentEditorInput acei = GenericUtils.cast(input);
					MobileBuilder mb = acei.getApplication().getProject().getMobileBuilder();
					mb.removeMobileEventListener((com.twinsoft.convertigo.eclipse.editors.mobile.ApplicationComponentEditor)part);
					mb.setAutoBuild(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (input instanceof com.twinsoft.convertigo.eclipse.editors.ngx.ApplicationComponentEditorInput) {
				try {
					com.twinsoft.convertigo.eclipse.editors.ngx.ApplicationComponentEditorInput acei = GenericUtils.cast(input);
					MobileBuilder mb = acei.getApplication().getProject().getMobileBuilder();
					mb.removeMobileEventListener((com.twinsoft.convertigo.eclipse.editors.ngx.ApplicationComponentEditor)part);
					mb.setAutoBuild(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if (input instanceof JScriptEditorInput) {
				try {
					((JScriptEditorInput) input).getFile().getParent().delete(true, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (input instanceof com.twinsoft.convertigo.eclipse.editors.mobile.ComponentFileEditorInput) {
				try {
					((com.twinsoft.convertigo.eclipse.editors.mobile.ComponentFileEditorInput) input).getFile().delete(true, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (input instanceof com.twinsoft.convertigo.eclipse.editors.ngx.ComponentFileEditorInput) {
				try {
					((com.twinsoft.convertigo.eclipse.editors.ngx.ComponentFileEditorInput) input).getFile().delete(true, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener#partDeactivated(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partDeactivated(IWorkbenchPart part) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener#partOpened(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partOpened(IWorkbenchPart part) {
		if (part instanceof PropertySheet) {
			PropertySheet view = (PropertySheet)part;
			if (view != null) {
				if (view.getCurrentPage().getControl() instanceof Tree) {
					Tree tree = (Tree) view.getCurrentPage().getControl();
					if (tree != null) {
						if (SwtUtils.isDark()) {
							tree.setLinesVisible(false);
						}
						try {
							tree.getColumn(1).setWidth(tree.getBounds().width);
						} catch (Exception e) {
						}
						tree.addKeyListener(new KeyAdapter() {
							
							@Override
							public void keyReleased(KeyEvent event) {
								boolean bCtrl = (((event.stateMask & SWT.CONTROL) != 0) || ((event.stateMask & SWT.CTRL) != 0));
								int keyCode = event.keyCode;
								char c = event.character;
								if (bCtrl) {
									if ((c == 's') || (keyCode == 115)) {
										ProjectExplorerView projectExplorerView = ConvertigoPlugin.getDefault().getProjectExplorerView();
										if (projectExplorerView != null) {
											projectExplorerView.projectExplorerSaveAllAction.run();
										}
									}
								}
							}
							
						});
					}
				}
			}
		}
		if (part instanceof EditorPart) {
			IEditorInput input = ((EditorPart)part).getEditorInput();
			if (input instanceof com.twinsoft.convertigo.eclipse.editors.mobile.ApplicationComponentEditorInput) {
				try {
					com.twinsoft.convertigo.eclipse.editors.mobile.ApplicationComponentEditorInput acei = GenericUtils.cast(input);
					MobileBuilder mb = acei.getApplication().getProject().getMobileBuilder();
					mb.addMobileEventListener((com.twinsoft.convertigo.eclipse.editors.mobile.ApplicationComponentEditor)part);
					mb.setAutoBuild(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (input instanceof com.twinsoft.convertigo.eclipse.editors.ngx.ApplicationComponentEditorInput) {
				try {
					com.twinsoft.convertigo.eclipse.editors.ngx.ApplicationComponentEditorInput acei = GenericUtils.cast(input);
					MobileBuilder mb = acei.getApplication().getProject().getMobileBuilder();
					mb.addMobileEventListener((com.twinsoft.convertigo.eclipse.editors.ngx.ApplicationComponentEditor)part);
					mb.setAutoBuild(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
