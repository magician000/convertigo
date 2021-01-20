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

package com.twinsoft.convertigo.engine.studio.responses.popup.actions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.twinsoft.convertigo.engine.studio.responses.AbstractResponse;

public class DatabaseObjectDeleteActionResponse extends AbstractResponse {

	private boolean doDelete;
	
	public DatabaseObjectDeleteActionResponse(boolean doDelete) {
		this.doDelete = doDelete;
	}
	
	@Override
	public Element toXml(Document document, String qname) throws Exception {
		Element response =  super.toXml(document, qname);
		response.setAttribute("doDelete", Boolean.toString(doDelete));
		return response;
	}

}
