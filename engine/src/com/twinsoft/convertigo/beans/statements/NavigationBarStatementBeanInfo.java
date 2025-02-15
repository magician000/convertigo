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

public class NavigationBarStatementBeanInfo extends MySimpleBeanInfo {
    
	public NavigationBarStatementBeanInfo() {
		try {
			beanClass = NavigationBarStatement.class;
			additionalBeanClass = com.twinsoft.convertigo.beans.core.Statement.class;

			iconNameC16 = "/com/twinsoft/convertigo/beans/statements/images/navigationbar_16x16.png";
			iconNameC32 = "/com/twinsoft/convertigo/beans/statements/images/navigationbar_32x32.png";
			
			resourceBundle = getResourceBundle("res/NavigationBarStatement");
			
			displayName = resourceBundle.getString("display_name");
			shortDescription = resourceBundle.getString("short_description");
			
			properties = new PropertyDescriptor[3];
			
			properties[0] = new PropertyDescriptor("action", beanClass, "getAction", "setAction");
			properties[0].setDisplayName(getExternalizedString("property.action.display_name"));
			properties[0].setShortDescription(getExternalizedString("property.action.short_description"));
			properties[0].setPropertyEditorClass(getEditorClass("PropertyWithTagsEditorAdvance"));
			
			properties[1] = new PropertyDescriptor("jsUrl", beanClass, "getJsUrl", "setJsUrl");
			properties[1].setDisplayName(getExternalizedString("property.jsurl.display_name"));
			properties[1].setShortDescription(getExternalizedString("property.jsurl.short_description"));
            properties[1].setValue("scriptable", Boolean.TRUE);
			
			properties[2] = new PropertyDescriptor("trigger", beanClass, "getTrigger", "setTrigger");
			properties[2].setDisplayName(getExternalizedString("property.trigger.display_name"));
			properties[2].setShortDescription(getExternalizedString("property.trigger.short_description"));
			properties[2].setExpert(true);
			properties[2].setPropertyEditorClass(getEditorClass("HttpTriggerEditor"));
		}
		catch(Exception e) {
			com.twinsoft.convertigo.engine.Engine.logBeans.error("Exception with bean info; beanClass=" + beanClass.toString(), e);
		}
	}

}
