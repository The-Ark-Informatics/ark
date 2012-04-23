/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.security;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;

import au.org.theark.web.pages.login.LoginPage;

public class CustomUnauthorizedStrategy implements IUnauthorizedComponentInstantiationListener {
	
	public void onUnauthorizedInstantiation(Component component) {
		
		System.out.println("Unauthorized Instantiation");
		if(component instanceof Page){
			System.out.println("\nComponent is instance of Page " + component.getClass().getName());
			throw new RestartResponseAtInterceptPageException(LoginPage.class);	
		}else{
			System.out.println("\n SetVisible False for component " + component.getClass().getName());
			component.setVisible(false);
		}
		
	}

}
