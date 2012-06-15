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
package au.org.theark.web.pages.login;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.web.pages.home.HomePage;

/**
 * <p>
 * The <code>AAFLoginForm</code> class that extends the {@link au.org.theark.web.pages.login.LoginForm LoginForm} class. It provides the implementation of the
 * login form of The Ark application.
 * </p>
 * 
 * @author nivedann
 * @author cellis
 */
public class AAFLoginForm extends LoginForm {

	private static final long			serialVersionUID	= -7482996457044513022L;
	private transient static Logger	log					= LoggerFactory.getLogger(AAFLoginForm.class);
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	/**
	 * An extension of LoginForm, used for AAF logins. never really seen, as Shibboleth should redirect to the AAF WAYF login form that handles authentication. If authenticated ok, redirects back into the application
	 * 
	 * @param id
	 *           the Component identifier
	 */
	public AAFLoginForm(String id) {
		super(id);
		checkAAFAuthentication();
	}

	private void checkAAFAuthentication() {
		final WebRequest webRequest = (WebRequest) RequestCycle.get().getRequest();
	   final HttpServletRequest httpReq = (HttpServletRequest) webRequest.getContainerRequest();
	   
	   ArkUserVO user = (ArkUserVO) getModelObject();
	   
	   // TODO: UserName: httpReq.getHeader("AJP_mail").. should it be httpReq.getHeader("AJP_persistent-id") ?
	   String userName = httpReq.getHeader("AJP_mail");
	   String password = httpReq.getHeader("AJP_Shib-Session-ID");
	   
	   if(userName != null || password != null) {
	   	user.setUserName(userName);
		   user.setPassword(password);
		   setModelObject(user);
		   
		   if(authenticate(user)) {
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				log.info( "\n ---- " + user.getUserName() + " logged in successfully at: " + dateFormat.format(new Date()) + " ---- \n");
					
				// Place a default module into session
				ArkModule arkModule = iArkCommonService.getArkModuleByName(au.org.theark.core.Constants.ARK_MODULE_STUDY);
				// Place a default function into session
				ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_STUDY);
		
				// Set session attributes
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_USERID, user.getUserName());
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY, arkModule.getId());
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_FUNCTION_KEY, arkFunction.getId());
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.SHIB_SESSION_ID, password);
				
				setResponsePage(HomePage.class);
		   }	
	   }
	   else {
	   	throw new AuthenticationException();
	   }
	}
}
