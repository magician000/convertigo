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

package com.twinsoft.convertigo.beans.statements;

import java.beans.PropertyDescriptor;

import com.twinsoft.convertigo.beans.core.MySimpleBeanInfo;


public class HTTPUploadStatementBeanInfo extends MySimpleBeanInfo {
	public HTTPUploadStatementBeanInfo() {
		try {
			beanClass = HTTPUploadStatement.class;
			additionalBeanClass = HTTPStatement.class;

			iconNameC16 = "/com/twinsoft/convertigo/beans/statements/images/httpupload_16x16.png";
			iconNameC32 = "/com/twinsoft/convertigo/beans/statements/images/httpupload_32x32.png";
			
			properties = new PropertyDescriptor[2];
			
			resourceBundle = getResourceBundle("res/HTTPUploadStatement");
			
			displayName = getExternalizedString("display_name");
			shortDescription = getExternalizedString("short_description");
			
			properties[0] = new PropertyDescriptor("filename", beanClass, "getFilename", "setFilename");
			properties[0].setDisplayName(getExternalizedString("property.filename.display_name"));
			properties[0].setShortDescription(getExternalizedString("property.filename.short_description"));
            properties[0].setValue("scriptable", Boolean.TRUE);
			
			properties[1] = new PropertyDescriptor("httpFilename", beanClass, "getHttpFilename", "setHttpFilename");
			properties[1].setDisplayName(getExternalizedString("property.httpfilename.display_name"));
			properties[1].setShortDescription(getExternalizedString("property.httpfilename.short_description"));
            properties[1].setValue("scriptable", Boolean.TRUE);
            
            PropertyDescriptor property = getPropertyDescriptor("httpVerb");
            property.setHidden(true);            
		} catch(Exception e) {
			com.twinsoft.convertigo.engine.Engine.logBeans.error("Exception with bean info; beanClass=" + beanClass.toString(), e);
		}
	}
}