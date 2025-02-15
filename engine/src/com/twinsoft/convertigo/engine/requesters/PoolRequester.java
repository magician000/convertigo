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

package com.twinsoft.convertigo.engine.requesters;

import java.io.UnsupportedEncodingException;

import org.w3c.dom.Document;

import com.twinsoft.convertigo.engine.Context;
import com.twinsoft.convertigo.engine.EngineException;
import com.twinsoft.convertigo.engine.translators.PoolTranslator;
import com.twinsoft.convertigo.engine.translators.Translator;

public class PoolRequester extends GenericRequester {
	
    public PoolRequester() {
    }
    
    public String getName() {
        return "PoolRequester";
    }
    
	public Context getContext() {
		return (Context) inputData;
	}

	public void preGetDocument() throws Exception {
		// Do nothing
	}

	public void setStyleSheet(Document document) {
		// Do nothing		
	}

	public void initContext(Context context) throws Exception {
		// Do nothing
	}

	public Translator getTranslator() {
		return new PoolTranslator(this);
	}

	protected void initInternalVariables() throws EngineException {
		// Do nothing
	}
	
	protected Object addStatisticsAsData(Object result) { 
		return result; 
	} 
	
	protected Object addStatisticsAsText(String stats, Object result) throws UnsupportedEncodingException{ 
		return result; 
	}
	
	public void handleParameter(String parameterName, String parameterValue) {
		handleParameter(context, parameterName, parameterValue);
	}
}
