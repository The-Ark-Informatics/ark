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

import au.org.theark.core.vo.EtaUserVO;
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


	public ResetPage(){
		log.info("ResetPage() constructor");
		ContextImage contextHostedByImage = new ContextImage("hostedByImage",new Model("images/uwa-logo.gif"));
		ContextImage productImage = new ContextImage("productImage", new Model("images/obiba-logo.png"));
		ResetForm resetForm = new ResetForm("resetForm");
		add(resetForm);
		add(contextHostedByImage);
		add(productImage);
	}
	
	class ResetForm extends StatelessForm<T>{

		
		TextField<String> emailAddressId = new TextField<String>("email");
		Button subButton =  new Button("submitBtn");
		
		
		public ResetForm(String id) {
			super(id, new CompoundPropertyModel(new EtaUserVO()));
			
			// TODO Auto-generated constructor stub
			this.add(emailAddressId);
			this.add(subButton);
		}
		
		@Override
		public void onSubmit(){
			log.info("onSubmit invoked on Reset Password page");
		}
	}
	

}
