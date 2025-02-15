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

package com.twinsoft.convertigo.beans.common;

import java.beans.PropertyDescriptor;

import com.twinsoft.convertigo.beans.core.DatabaseObject;
import com.twinsoft.convertigo.beans.core.MySimpleBeanInfo;

public class XMLHttpUrlBeanInfo extends MySimpleBeanInfo {
    
	public XMLHttpUrlBeanInfo() {
		try {
			beanClass = XMLHttpUrl.class;
			additionalBeanClass = com.twinsoft.convertigo.beans.extractionrules.HtmlExtractionRule.class;

		    iconNameC16 = "/com/twinsoft/convertigo/beans/common/images/xmlhttpurl_color_16x16.png";
		    iconNameC32 = "/com/twinsoft/convertigo/beans/common/images/xmlhttpurl_color_32x32.png";

			resourceBundle = getResourceBundle("res/XMLHttpUrl");

			displayName = getExternalizedString("display_name");
			shortDescription = getExternalizedString("short_description");
			
			properties = new PropertyDescriptor[1];
			
			properties[0] = new PropertyDescriptor("tagName", beanClass, "getTagName", "setTagName");
			properties[0].setDisplayName(getExternalizedString("property.tagName.display_name"));
			properties[0].setShortDescription(getExternalizedString("property.tagName.short_description"));
			properties[0].setValue(DatabaseObject.PROPERTY_XMLNAME, Boolean.TRUE);
			
			PropertyDescriptor property = getPropertyDescriptor("xpath");
			property.setHidden(true);
		}
		catch(Exception e) {
			com.twinsoft.convertigo.engine.Engine.logBeans.error("Exception with bean info; beanClass=" + beanClass.toString(), e);
		}
	}

}
