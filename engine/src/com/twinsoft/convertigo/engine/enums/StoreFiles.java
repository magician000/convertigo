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

package com.twinsoft.convertigo.engine.enums;


public enum StoreFiles {
	css, 
	fonts,
	i18n,
	images,
	scripts,
	index("index.html");
	
	public final static String STORE_DIRECTORY_NAME = "store";
	String filename;
	
	StoreFiles() {
		filename = name();
	}
	
	StoreFiles(String filename) {
		this.filename = filename;
	}
	
	public String filename() {
		return filename;
	}
	
	static public int size() {
		return values().length;
	}
}
