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

package com.twinsoft.convertigo.engine.studio.responses.projectexplorer.actions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.twinsoft.convertigo.engine.studio.responses.AbstractResponse;

public class OpenEditableEditorActionResponse extends AbstractResponse {

    private String filePath;
    private String typeEditor;

    public OpenEditableEditorActionResponse(String filePath, String typeEditor) {
        super();
        this.filePath = filePath;
        this.typeEditor = typeEditor;
    }

    @Override
    public Element toXml(Document document, String qname) throws Exception {
        Element response = super.toXml(document, qname);

        // Send file path
        Element eFilePath = document.createElement("filepath");
        eFilePath.setTextContent(filePath);

        response.setAttribute("type_editor", typeEditor);
        response.appendChild(eFilePath);

        return response;
    }
}
