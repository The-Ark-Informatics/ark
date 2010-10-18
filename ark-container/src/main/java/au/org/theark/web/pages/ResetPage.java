package au.org.theark.web.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.service.ContainerService;

public class ResetPage<T> extends WebPage{
	
	private transient Logger log = LoggerFactory.getLogger(ResetPage.class);
	
	@SpringBean(name = "containerService")
	private ContainerService containerService;
	
	@Autowired
	public ContainerService getContainerService() {
		return containerService;
	}

	public void setContainerService(ContainerService containerService) {
		this.containerService = containerService;
	}

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
		ContextImage bannerImage = new ContextImage("bannerImage", new Model<String>("images/"+Constants.BANNER_IMAGE));
		
		add(hostedByImage);
		add(productImage);
		add(bannerImage);
		
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
