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

package com.twinsoft.convertigo.beans.common;

import java.beans.*;

import com.twinsoft.convertigo.beans.core.MySimpleBeanInfo;

public class MoveBlocksBeanInfo extends MySimpleBeanInfo {

	public MoveBlocksBeanInfo() {
		try {
			beanClass = MoveBlocks.class;
			additionalBeanClass = com.twinsoft.convertigo.beans.extractionrules.JavelinExtractionRule.class;

			iconNameC16 = "/com/twinsoft/convertigo/beans/common/images/moveblocks_color_16x16.png";
			iconNameC32 = "/com/twinsoft/convertigo/beans/common/images/moveblocks_color_32x32.png";

 			resourceBundle = getResourceBundle("res/MoveBlocks");

			displayName = getExternalizedString("display_name");
			shortDescription = getExternalizedString("short_description");

			properties = new PropertyDescriptor[2];
			
			properties[PROPERTY_isRelative] = new PropertyDescriptor( "isRelative", MoveBlocks.class, "isRelative", "setRelative" );
			properties[PROPERTY_isRelative].setDisplayName( getExternalizedString("property.isrelative.display_name") );
			properties[PROPERTY_isRelative].setShortDescription( getExternalizedString("property.isrelative.short_description") );
			
			properties[PROPERTY_fieldDesc] = new PropertyDescriptor ( "fieldDesc", MoveBlocks.class, "getFieldDesc", "setFieldDesc" );
			properties[PROPERTY_fieldDesc].setDisplayName ( getExternalizedString("property.fielddesc.display_name") );
			properties[PROPERTY_fieldDesc].setShortDescription ( getExternalizedString("property.fielddesc.short_description") );
			properties[PROPERTY_fieldDesc].setPropertyEditorClass( getEditorClass("ZoneEditor") );
					
		}
		catch(Exception e) {
			com.twinsoft.convertigo.engine.Engine.logBeans.error("Exception with bean info; beanClass=" + beanClass.toString(), e);
		}
	}
	
	private static final int PROPERTY_isRelative = 0;
	private static final int PROPERTY_fieldDesc = 1;
	
}

