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
package au.org.theark.web.application;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import au.org.theark.web.pages.login.LoginPage;

public class ArkWebApplication extends BaseApplication{
	
	
	public void init(){
		log.info("In Constructor of ArkApplication");
		super.init();
		getComponentInstantiationListeners().add(new SpringComponentInjector(this,context(),true));
		SecurePackageResourceGuard guard = new SecurePackageResourceGuard();
      guard.addPattern("+*.js");
      guard.addPattern("+*.jar");
      getResourceSettings().setPackageResourceGuard(guard); 
	}
	
	@Override
	public Class<? extends Page> getHomePage() {
		
		return LoginPage.class;
	}
	
	public ApplicationContext context(){
		
		return WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	}

}
