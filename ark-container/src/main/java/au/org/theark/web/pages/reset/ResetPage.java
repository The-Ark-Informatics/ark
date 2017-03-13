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
package au.org.theark.web.pages.reset;

import au.org.theark.core.service.IArkCommonService;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.UrlRenderer;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.util.Protocol;
import au.org.theark.core.web.component.panel.recaptcha.ReCaptchaPanel;
import au.org.theark.web.pages.Constants;

/**
 * <p>
 * The <code>ResetPage</code> class that extends the {@link org.apache.wicket.markup.html.WebPage WebPage} class. It provides the implementation of the
 * reset username page of The Ark application.
 * </p>
 * 
 * @author nivedann
 * @author cellis
 */
public class ResetPage<T> extends WebPage {

	static Logger log = LoggerFactory.getLogger(ResetPage.class);
	
	private static final long	serialVersionUID	= -8767984428141993995L;
	private FeedbackPanel		feedbackPanel;
	private ResetForm				resetForm;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	public ResetPage() {
		String protocol = new UrlRenderer(this.getRequest()).renderFullUrl(this.getRequest().getUrl()).split("://")[0];
		Protocol p = Protocol.HTTP;
		if(protocol.equals("http")) {
			p = Protocol.HTTP;
		} else if(protocol.equals("https")) {
			p = Protocol.HTTPS;
		}
		log.info("protocol = " + protocol);
		
		for(String s : this.getRequest().getRequestParameters().getParameterNames()) {
			log.info(s);
		}
		feedbackPanel = new FeedbackPanel("feedbackMessage");
		feedbackPanel.setOutputMarkupPlaceholderTag(true);
		
		resetForm = new ResetForm("resetForm", feedbackPanel, p);
		addComponents();
	}

	private void addComponents() {
		add(feedbackPanel);
		add(iArkCommonService.getHostedByImage());
		add(iArkCommonService.getProductImage());
		add(resetForm);
	}
}
