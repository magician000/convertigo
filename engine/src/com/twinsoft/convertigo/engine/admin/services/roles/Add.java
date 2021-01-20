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

package com.twinsoft.convertigo.engine.admin.services.roles;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.twinsoft.convertigo.engine.AuthenticatedSessionManager;
import com.twinsoft.convertigo.engine.AuthenticatedSessionManager.Role;
import com.twinsoft.convertigo.engine.Engine;
import com.twinsoft.convertigo.engine.admin.services.XmlService;
import com.twinsoft.convertigo.engine.admin.services.at.ServiceDefinition;

@ServiceDefinition(
		name = "Add",
		roles = { Role.WEB_ADMIN },
		parameters = {},
		returnValue = "add a new global user"
	)
public class Add extends XmlService {

	protected void getServiceResult(HttpServletRequest request, Document document) throws Exception {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String[] roles = request.getParameterValues("roles");
		
		Element root = document.getDocumentElement();
		Element response = document.createElement("response");

		try {
			if (StringUtils.isBlank(username)) {
				throw new IllegalArgumentException("Blank username not allowed");
			}
			
			if (StringUtils.isBlank(password)) {
				throw new IllegalArgumentException("Blank password not allowed");
			}
			
			if (Engine.authenticatedSessionManager.hasUser(username)) {
				throw new IllegalArgumentException("User '" + username + "' already exists");
			}
			
			Set<Role> set;
			if (roles == null) {
				set = Collections.emptySet();
			} else {
				set = new HashSet<Role>(roles.length);
				for (String role: roles) {
					set.add(Role.valueOf(role));
				}
			}
			AuthenticatedSessionManager.validatePassword(password);
			Engine.authenticatedSessionManager.setUser(username, DigestUtils.sha512Hex(password), set);
			response.setAttribute("state", "success");
			response.setAttribute("message","User '" + username + "' have been successfully declared!");
		} catch (Exception e) {
			Engine.logAdmin.error("Error during adding the user!\n" + e.getMessage());
			
			response.setAttribute("state", "error");
			response.setAttribute("message","Error during adding the user!\n" + e.getMessage());
		}
		root.appendChild(response);		
	}
}
