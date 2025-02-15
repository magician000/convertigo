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

package com.twinsoft.convertigo.beans.criteria.siteclipper;

import java.util.regex.Matcher;

import com.twinsoft.convertigo.beans.common.ISiteClipperResponseCriteria;
import com.twinsoft.convertigo.beans.connectors.SiteClipperConnector.Shuttle;
import com.twinsoft.convertigo.engine.Engine;

public class MatchHeaderOfResponse extends MatchHeader implements ISiteClipperResponseCriteria {

	private static final long serialVersionUID = -318982694615135898L;

	public MatchHeaderOfResponse() {
		super();
	}

	@Override
	public boolean isMatchingResponse(Shuttle shuttle) {
		String headerName = getHeaderName();
		String headerValue = shuttle.getResponseHeader(headerName);
		if (headerValue == null) {
			Engine.logSiteClipper.trace("(MatchHeaderOfResponse) header \"" + headerName + "\" doesn't exist");
			return false;
		} else {
			Matcher regex_matcher = getRegexPattern().matcher(headerValue);
			Engine.logSiteClipper.trace("(MatchHeaderOfResponse) header \"" + headerName + "\" found : " + headerValue);
			return regex_matcher.find();
		}
	}
}
