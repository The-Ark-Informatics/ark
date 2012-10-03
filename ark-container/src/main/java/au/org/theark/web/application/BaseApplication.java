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

import net.ftlines.wicketsource.WicketSource;

import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.settings.IApplicationSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.web.pages.home.HomePage;
import au.org.theark.web.pages.login.AAFLoginPage;
import au.org.theark.web.pages.login.LoginPage;

public abstract class BaseApplication extends WebApplication {
	static final Logger	log	= LoggerFactory.getLogger(BaseApplication.class);

	@Override
	protected void init() {
		super.init();
		getMarkupSettings().setStripWicketTags(true);
		IApplicationSettings settings = getApplicationSettings();
		
		// Mount the pages for nicer looking URL's
		mountPage("login",LoginPage.class);
		mountPage("aaf-login",AAFLoginPage.class);
		mountPage("home", HomePage.class);
		
		settings.setPageExpiredErrorPage(LoginPage.class);
		
		// Alow wicket-source for development debugging
		if(getConfigurationType().equals(RuntimeConfigurationType.DEVELOPMENT)){
			WicketSource.configure(this);
		}
	}

	public Class<? extends Page> getHomePage() {
		return LoginPage.class;
	}
}
