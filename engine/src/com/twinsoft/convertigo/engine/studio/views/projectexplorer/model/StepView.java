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

package com.twinsoft.convertigo.engine.studio.views.projectexplorer.model;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import com.twinsoft.convertigo.beans.core.DatabaseObject;
import com.twinsoft.convertigo.beans.core.Project;
import com.twinsoft.convertigo.beans.core.Step;
import com.twinsoft.convertigo.beans.steps.SimpleStep;
import com.twinsoft.convertigo.engine.studio.WrapStudio;
import com.twinsoft.convertigo.engine.studio.responses.projectexplorer.actions.OpenEditableEditorActionResponse;
import com.twinsoft.convertigo.engine.util.FileUtils;

public class StepView extends DatabaseObjectView implements IEditableTreeViewWrap {

    private final static String JSCRIPT_STEP_EDITOR = "c8o_jscriptstepeditor";

    public StepView(Step dbo, WrapStudio studio) {
        super(dbo, studio);
    }

    @Override
    public void launchEditor(String editorType) {
        // Retrieve the project name
       // String projectName = ((DatabaseObject) getObject()).getProject().getName();
//        try {
            // Refresh project resource
            //IProject project = ConvertigoPlugin.getDefault().getProjectPluginResource(projectName);

            // Get editor type
            if (editorType == null) {
                if (((DatabaseObject) getObject()) instanceof SimpleStep) {
                    editorType = "JscriptStepEditor";
                }
            }

            // Open editor
            if ((editorType != null) && (editorType.equals("JscriptStepEditor"))) {
                openJscriptStepEditor();
            }
//            if ((editorType != null) && (editorType.equals("XMLTransactionStepEditor"))) {
//                //openXMLTransactionStepEditor(project);
//            }
//            if ((editorType != null) && (editorType.equals("XMLSequenceStepEditor"))) {
//                //openXMLSequenceStepEditor(project);
//            }
//        }
//        catch (CoreException e) {
//            ConvertigoPlugin.logException(e, "Unable to open project named '" + projectName + "'!");
//        }
    }

    private void openJscriptStepEditor() {
        Project project = dbo.getProject();
        try {
            // Create private folder if it does not exist
            FileUtils.createFolderIfNotExist(project.getDirPath(), "_private");

            String fileName = FileUtils.createTmpFileWithUTF8Data(
                project.getDirPath(),
                "_private" + "/" + Base64.encodeBase64URLSafeString(DigestUtils.sha1(dbo.getQName())) + " " + dbo.getName() + "." + JSCRIPT_STEP_EDITOR,
                ((SimpleStep) dbo).getExpression()
            );

            studio.createResponse(
                new OpenEditableEditorActionResponse(
                    project.getQName() + "/" + "_private" + "/" +  fileName,
                    JSCRIPT_STEP_EDITOR
                ).toXml(studio.getDocument(), getObject().getQName())
            );
        }
        catch (Exception e) {
        }
    }
}
