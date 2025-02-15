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

import com.twinsoft.convertigo.beans.core.MySimpleBeanInfo;

public class XMLSplitNodesBeanInfo extends MySimpleBeanInfo {
    
	public XMLSplitNodesBeanInfo() {
		try {
			beanClass = XMLSplitNodes.class;
			additionalBeanClass = com.twinsoft.convertigo.beans.extractionrules.HtmlExtractionRule.class;

		    iconNameC16 = "/com/twinsoft/convertigo/beans/common/images/xmlsplitnodes_color_16x16.png";
		    iconNameC32 = "/com/twinsoft/convertigo/beans/common/images/xmlsplitnodes_color_32x32.png";

		    properties = new PropertyDescriptor[3];
		    
			resourceBundle = getResourceBundle("res/XMLSplitNodes");

			displayName = getExternalizedString("display_name");
			shortDescription = getExternalizedString("short_description");
			
			properties[0] = new PropertyDescriptor("resultXpath", beanClass, "getResultXpath", "setResultXpath");
			properties[0].setExpert(true);
			properties[0].setDisplayName(getExternalizedString("property.resultXpath.display_name"));
			properties[0].setShortDescription(getExternalizedString("property.resultXpath.short_description"));			
			
			properties[1] = new PropertyDescriptor("regExp", beanClass, "getRegExp", "setRegExp");
			properties[1].setDisplayName(getExternalizedString("property.regExp.display_name"));
			properties[1].setShortDescription(getExternalizedString("property.regExp.short_description"));
			
			properties[2] = new PropertyDescriptor("keepSeparator", beanClass, "isKeepSeparator", "setKeepSeparator");
			properties[2].setDisplayName(getExternalizedString("property.keepSeparator.display_name"));
			properties[2].setShortDescription(getExternalizedString("property.keepSeparator.short_description"));
			
		}
		catch(Exception e) {
			com.twinsoft.convertigo.engine.Engine.logBeans.error("Exception with bean info; beanClass=" + beanClass.toString(), e);
		}
	}

}
