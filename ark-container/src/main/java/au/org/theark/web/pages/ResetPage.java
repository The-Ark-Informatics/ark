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
package au.org.theark.web.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.vo.ArkUserVO;

public class ResetPage<T> extends WebPage{
	
	private transient Logger log = LoggerFactory.getLogger(ResetPage.class);
	

	private String userId;
	private String emailAddress;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getEmailAddress() {
		return emailAddress;
	}


	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@SuppressWarnings("serial")
	public ResetPage(){
		log.info("ResetPage() constructor");
		ContextImage hostedByImage = new ContextImage("hostedByImage",new Model<String>("images/"+Constants.HOSTED_BY_IMAGE));
		ContextImage productImage = new ContextImage("productImage", new Model<String>("images/"+Constants.PRODUCT_IMAGE));
		
		add(hostedByImage);
		add(productImage);
		
		ResetForm resetForm = new ResetForm("resetForm"){
			
			protected void onCancel(){
				setResponsePage(LoginPage.class);
			}
			
			protected void onReset(){
				log.info("onReset() invoked on Reset Password page");
			}
		};
		add(resetForm);
	}
	
	@SuppressWarnings("serial")
	class ResetForm extends StatelessForm<T>{
		@SuppressWarnings("unchecked")
		public ResetForm(String id) {
			super(id, new CompoundPropertyModel(new ArkUserVO()));
			
			this.add(emailAddressId);
			this.add(resetButton);
			this.add(cancelButton);
		}

		protected void onCancel(){};
		protected void onReset(){};

		TextField<String> emailAddressId = new TextField<String>("email");

		Button resetButton =  new Button("resetButton")
		{
			public void onSubmit()
			{
				// Reset user password
				onReset();
			}
		};
		
		Button cancelButton = new Button("cancelButton")
		{
			public void onSubmit()
			{
				// Go to Login page
				onCancel();
			}		
		};
	}
}
