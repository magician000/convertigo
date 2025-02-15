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

package com.twinsoft.convertigo.eclipse.wizards.setup;

import java.io.IOException;
import java.util.Properties;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.twinsoft.convertigo.eclipse.ConvertigoPlugin;
import com.twinsoft.convertigo.eclipse.ConvertigoPlugin.PscException;
import com.twinsoft.convertigo.eclipse.DeploymentKey;
import com.twinsoft.convertigo.eclipse.wizards.setup.SetupWizard.RegisterCallback;
import com.twinsoft.convertigo.eclipse.wizards.setup.SetupWizard.SummaryGenerator;

public class PscKeyValidationPage extends WizardPage implements RegisterCallback, SummaryGenerator {
	
	private Composite container;
	
	private Text pscKey;
	private Link infoLink;
	private Properties decodedPSC;
	
	public PscKeyValidationPage () {
		super("PscKeyPage");
		setTitle("Personal Studio Configuration");
		setDescription("Paste your PSC or register...");
	}

	@Override
	public IWizard getWizard() {		
		setErrorMessage(null);
		setMessage(getDescription());
		
		SetupWizard wizard = (SetupWizard) super.getWizard();
		wizard.postRegisterState(this.getClass().getSimpleName().toLowerCase());

		return super.getWizard();
	}

	public void createControl(final Composite parent) {	
		ScrolledComposite sc = new ScrolledComposite(parent, SWT.V_SCROLL);
		
		container = new Composite(sc, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		layout.marginWidth = 30;
		container.setLayout(layout);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		GridData layoutDataText = new GridData(GridData.FILL_HORIZONTAL);
		layoutDataText.verticalIndent = 5;
		
		Link details = new Link(container, SWT.WRAP);
		details.setText(
				"\nIncluded with Convertigo Community Edition, you get access to a 15 days free « Convertigo Cloud » account. You will be able to deploy your projects on this cloud account, and when the trial expires, " +
				"you will have the opportunity to buy production Convertigo Cloud devices licences.\n\n" +
				"As a result of the cloud signup process, you will receive a PSC by email that will configure your Studio for projects deployment on your Convertigo Cloud account. \n\n" +
				"You can access the Convertigo Community support on Stack overflow by clicking this link: <a href=\"https://stackoverflow.com/questions/ask?tags=convertigo\">https://stackoverflow.com/questions/ask?tags=convertigo</a>.\n\n" +
				"Registered users will also get a free 30 minutes \"Getting Started\" web meeting session with one of our support engineers. The link to choose an available slot will be available in the same email providing you the PSC. \n\n"
		);
		details.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Composite registerComposite = new Composite(container, SWT.NONE);
		registerComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		registerComposite.setLayout(new GridLayout(2, false));
		
		Button goToRegisterWebsite = new Button(registerComposite, SWT.NONE);
		goToRegisterWebsite.setText("Click now to Signup");
		
		Link registerLink = new Link(registerComposite, SWT.WRAP);
		registerLink.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		registerLink.setText("or go to <a href=\"https://www.convertigo.com/signup\">https://www.convertigo.com/signup</a>");
		
		Label label = new Label (container, SWT.NONE);
		FontData fontDefaultData = label.getFont().getFontData()[0];
		fontDefaultData.setStyle(SWT.BOLD);
		fontDefaultData.setHeight(14);
		
		label.setFont(new Font(parent.getDisplay(), fontDefaultData));
		Color color = new  Color(container.getDisplay(), 51, 102, 255);
		label.setForeground(color);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		label.setText("PSC");
		
		infoLink = new Link(container, SWT.WRAP);
		infoLink.setText("Please paste your PSC here and click the 'Next >' button...");
		infoLink.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
				
		pscKey = new Text(container, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		pscKey.setFont(JFaceResources.getFont(JFaceResources.TEXT_FONT));
		pscKey.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Composite buttons = new Composite(container, SWT.NONE);
		buttons.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		buttons.setLayout(new GridLayout(3, false));
		
		label = new Label(buttons, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Button paste = new Button(buttons, SWT.NONE);
		paste.setText("Paste from clipboard");
		
		Button clear = new Button(buttons, SWT.NONE);
		clear.setText("Clear");

		
		// Events management
		goToRegisterWebsite.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				org.eclipse.swt.program.Program.launch("https://www.convertigo.com/signup");
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) { }
		});
		
		SelectionListener goToTheLink = new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				org.eclipse.swt.program.Program.launch(e.text);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) { }
		};
		
		details.addSelectionListener(goToTheLink);
		registerLink.addSelectionListener(goToTheLink);
		
		pscKey.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String psc = getCertificateKey();
				if (psc.length() != 0) {
					try {
						decodedPSC = ConvertigoPlugin.decodePsc(psc);
						setErrorMessage(null);
						setMessage(getDescription());
						setPageComplete(true);
					} catch (PscException exception) {
						setErrorMessage(exception.getMessage());
						setPageComplete(false);
					}
				} else {
					setErrorMessage("Please enter your PSC!");
					setPageComplete(false);
				}
			}
		});
		
		paste.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Clipboard clipboard = new Clipboard(container.getDisplay());
				String data = (String) clipboard.getContents(TextTransfer.getInstance());
				if (data != null && data.length() > 0) {
					pscKey.setText(data.trim());
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		clear.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				pscKey.setText("");
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) { }
		});
		
		parent.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				container.setSize(parent.getSize());
			}
		});

		sc.setMinSize(400, 500);
		sc.setContent(container);
    	sc.setExpandVertical(true);
		
		// Required to avoid an error in the system
		setControl(sc);
		setPageComplete(false);
	}

	public String getCertificateKey() {
		return pscKey.getText().trim();
	}
	
	public void clearCertificateKey() {
		pscKey.setText("");
	}
	
	public void setAnonymousCertificateKey() {
		try {
			pscKey.setText(ConvertigoPlugin.makeAnonymousPsc());
		} catch (IOException e) {
			ConvertigoPlugin.logWarning(e, "Unable to make an anonymous PSC");
		}
	}
	
	public void onRegister(final boolean success, final String message) {
		Display.getDefault().asyncExec(new Runnable() {
			
			public void run() {
				if (success) {
					infoLink.setText("Please click on the link you received by mail and paste the generated PSC in the following text area and click the 'Next >' button...");
				} else {
					infoLink.setText("Some error occure during the online registration: " + message + "\n" +
							"Try to fix in the previous screen or register manually on " + RegistrationPage.registrationLink);
					setErrorMessage("Error during the only registration!");
				}
				infoLink.getParent().layout();
			}
		});
	}

	public String getSummary() {
		StringBuffer summary = new StringBuffer("PSC server configuration for:\n");
		
		int i = 0;
		while (++i > 0) {
			String server = DeploymentKey.server.value(decodedPSC, i);
			if (server != null && !server.equals("")) {
				summary.append("\t" + server + "\n");
			} else {
				if (i == 1) {
					summary = new StringBuffer();
				}
				i = -1;
			}
		}	
		
		return summary.toString();
	}
	
}
