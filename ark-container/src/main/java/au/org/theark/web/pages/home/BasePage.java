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
package au.org.theark.web.pages.home;

import au.org.theark.core.exception.ArkCheckSumNotSameException;
import au.org.theark.core.exception.ArkFileNotFoundException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.config.entity.SettingFile;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.service.IArkSettingService;
import au.org.theark.core.util.EventPayload;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.event.IEventSource;
import org.apache.wicket.markup.MarkupException;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.study.web.component.mydetails.MyDetailsContainer;
import au.org.theark.web.pages.Constants;
import au.org.theark.web.pages.login.LoginPage;
import au.org.theark.web.pages.mydetails.MyDetailModalWindow;
import org.springframework.beans.factory.annotation.Autowired;

import javax.naming.Context;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * <p>
 * The <code>BasePage</code> class that extends the {@link org.apache.wicket.markup.html.WebPage WebPage} class. This page will be inherited by all
 * wicket pages. Contains basic functionality that all pages will require:
 * <ul>
 * <li>e.g. add the menu in here so all pages can inherit it by default.</li>
 * </ul>
 * Access to each menu item will be determined by the subclass via annotations:
 * <ul>
 * <li>e.g if one of the menu item was Admin the class linked or invoked will place the constraints via annotations</li>
 * </ul>
 * Using IStrategyAuthorization, the application will determine if the link is accessible to the current logged in user.
 * </p>
 * 
 * @author nivedann
 * @author cellis
 * 
 */
public abstract class BasePage extends WebPage {
	private static final long		serialVersionUID	= 4173121872289013698L;
	private transient static Logger	log =  LoggerFactory.getLogger(BasePage.class);
	private transient Subject		currentUser;
	private String						principal;
	private Label						userNameLbl;
	private Label						studyNameLbl;

	protected WebMarkupContainer	studyNameMarkup;
	protected WebMarkupContainer	studyLogoMarkup;
	protected WebComponent			productImage;
	protected WebComponent			hostedByImage;

	private MyDetailModalWindow	modalWindow;
	private ArkBusyAjaxLink<Void> ajaxLogoutLink;

	@SpringBean(name = au.org.theark.core.Constants.ARK_SETTING_SERVICE)
	private IArkSettingService iArkSettingService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	static {
		try{
			System.loadLibrary("madeline");
		}catch(Error e)	{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public BasePage() {
		currentUser = SecurityUtils.getSubject();

		if (currentUser.getPrincipal() != null) {
			ContextImage studyLogoImage = new ContextImage("studyLogoImage", new Model<String>("images/" + Constants.NO_STUDY_LOGO_IMAGE));

			principal = (String) currentUser.getPrincipal();
			userNameLbl = new Label("loggedInUser", new Model<String>(principal));
			studyNameLbl = new Label("studyNameLabel", new Model<String>(" "));

			// Markup for Study name
			studyNameMarkup = new WebMarkupContainer("studyNameMarkupContainer");
			studyNameMarkup.add(studyNameLbl);
			studyNameMarkup.setOutputMarkupPlaceholderTag(true);

			// Markup for Study Logo
			studyLogoMarkup = new WebMarkupContainer("studyLogoMarkupContainer");
			studyLogoMarkup.add(studyLogoImage);
			studyLogoMarkup.setOutputMarkupPlaceholderTag(true);
			
			// Add images
			add(studyNameMarkup);
			add(studyLogoMarkup);
			productImage = iArkCommonService.getProductImage();
			hostedByImage = iArkCommonService.getHostedByImage();
			add(productImage);
			add(hostedByImage);

			ArkBusyAjaxLink myDetailLink = new ArkBusyAjaxLink("myDetailLink") {

				/**
				 * 
				 */
				private static final long	serialVersionUID	= 422053857225833627L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					showModalWindow(target);
				}
			};

			myDetailLink.add(userNameLbl);
			add(myDetailLink);
			modalWindow = new MyDetailModalWindow("modalWindow") {
				/**
				 * 
				 */
				private static final long	serialVersionUID	= -1351016643735035753L;

				@Override
				protected void onCloseModalWindow(AjaxRequestTarget target) {
					// target.addComponent(BasePage.this);
				}
			};
			add(modalWindow);

			ajaxLogoutLink = new ArkBusyAjaxLink("ajaxLogoutLink") {
				/**
				 * 
				 */
				private static final long	serialVersionUID	= 422053857225833627L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					currentUser = SecurityUtils.getSubject();
					principal = (String) currentUser.getPrincipal();
					log.info("\n -- " + principal + " has logged out. ----");
					currentUser.logout();
					
					Session.get().invalidateNow(); // invalidate the wicket session
					setResponsePage(LoginPage.class);
				}
			};
			add(ajaxLogoutLink);
		}
		else {
			setResponsePage(LoginPage.class);
		}
	}

	@Override
	protected void onBeforeRender() {
		productImage = iArkCommonService.getProductImage();
		hostedByImage = iArkCommonService.getHostedByImage();
		addOrReplace(productImage);
		addOrReplace(hostedByImage);
		super.onBeforeRender();
	}

	@Override
	protected void onRender() {
		try{
			super.onRender();
		}
		catch(MarkupException mue) {
			log.warn("Session was expired, redirecting to login page");
			setResponsePage(LoginPage.class);
		}
	}

	protected void configureResponse(final org.apache.wicket.request.http.WebResponse webResponse) {
		super.configureResponse(webResponse);
		webResponse.setHeader("Cache-Control", "no-cache, max-age=0,must-revalidate, no-store");
		webResponse.setHeader("Expires", "-1");
		webResponse.setHeader("Pragma", "no-cache");
	}

	protected void showModalWindow(AjaxRequestTarget target) {
		MyDetailsContainer modalContentPanel = new MyDetailsContainer("content", new ArkUserVO(), currentUser, modalWindow);
		modalWindow.setTitle("My Detail");
		modalWindow.setContent(modalContentPanel);
		modalWindow.show(target);
	}

	/**
	 * Build module tabs. All sub classes must implement this method
	 */
	protected abstract void buildModuleTabs();

	abstract String getTitle();

	public void onEvent(IEvent<?> event) {
		if(event.getPayload() instanceof EventPayload) {
			EventPayload payload = (EventPayload) event.getPayload();
			AjaxRequestTarget target = payload.getTarget();
			productImage = iArkCommonService.getProductImage();
			hostedByImage = iArkCommonService.getHostedByImage();
			addOrReplace(productImage);
			addOrReplace(hostedByImage);
			if(payload.getEventName().equalsIgnoreCase(au.org.theark.core.Constants.EVENT_RELOAD_LOGO_IMAGES)) {
				target.add(productImage);
				target.add(hostedByImage);
			}
			this.dirty();
		}
	}
}
