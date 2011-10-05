package au.org.theark.web.pages.reset;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.util.MailClient;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.component.panel.recaptcha.ReCaptchaPanel;
import au.org.theark.study.service.IUserService;
import au.org.theark.web.pages.login.LoginPage;

/**
 * <p>
 * The <code>ResetForm</code> class that extends the {@link org.apache.wicket.markup.html.form.Form Form} class. It provides the
 * implementation of the reset password form of The Ark application.
 * </p>
 * 
 * @author nivedann
 * @author cellis
 */
public class ResetForm extends Form<ArkUserVO> implements Serializable {
	/**
	 * 
	 */
	private static final long			serialVersionUID	= 5759125957232986063L;
	private transient static Logger	log					= LoggerFactory.getLogger(ResetForm.class);
	private FeedbackPanel				feedbackPanel;
	private TextField<String>			email;
	private ReCaptchaPanel				reCaptchaPanel;
	private AjaxButton					resetButton;
	private AjaxButton					cancelButton;
	
	@SpringBean(name = "userService")
	private IUserService				userService;

	/**
	 * 
	 * @param id
	 */
	public ResetForm(String id, final FeedbackPanel feedbackPanel) {
		super(id, new CompoundPropertyModel<ArkUserVO>(new ArkUserVO()));
		this.feedbackPanel = feedbackPanel;
		
		email =  new TextField<String>("email");
		email.setRequired(true);
		
		reCaptchaPanel = new ReCaptchaPanel("reCaptchaPanel");
		
		resetButton = new AjaxButton("resetButton") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				log.error("Error on resetButton click");
				target.add(feedbackPanel);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onReset(target);
			}
		};

		cancelButton = new AjaxButton("cancelButton") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				log.error("Error on cancelButton click");
				target.add(feedbackPanel);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onCancel(target);
				
			}
		};
		cancelButton.setDefaultFormProcessing(false);
		
		addFormComponents();
	}

	private void addFormComponents() {
		add(email);
		add(reCaptchaPanel);
		add(resetButton);
		add(cancelButton);
	}
	
	protected void onReset(AjaxRequestTarget target) {
		try {
			String password = new String(org.apache.commons.lang.RandomStringUtils.randomAlphanumeric(9));
			log.info("New password: " + password);
			
			// Get the ArkUser
			setModelObject(userService.getCurrentUser(getModelObject().getEmail()));
			
			// Update to new password
			getModelObject().setPassword(password);
			getModelObject().setConfirmPassword(password);
			
			//userService.updateArkUser(getModelObject());
			getModelObject().setArkUserPresentInDatabase(true);
			
			sendNotificationEmail(password);
			
			this.info(new StringResourceModel("password.updated", this, null).getString());
			setResponsePage(LoginPage.class);

		}
		catch (ArkSystemException e) {
			this.error(new StringResourceModel("ark.system.error", this, null).getString());
		}
		
		target.add(feedbackPanel);
	};

	/**
	 * Email the user with new password
	 */
	private void sendNotificationEmail(String password) {
		log.info("Password: " + password);
		
		MailClient mailClient = new MailClient();
		mailClient.setTo(getModelObject().getEmail());
		mailClient.setSubject("Message from the-ARK: " + getModelObject().getUserName() + " password reset");
		
		StringBuffer messageBody = new StringBuffer();
		messageBody.append("Dear ");
		messageBody.append(getModelObject().getUserName());
		messageBody.append(",\n");
		messageBody.append("Your password has been reset to: ");
		messageBody.append(getModelObject().getPassword());
		messageBody.append("\n");
		messageBody.append("Please log in and change it as soon as possible.");
		messageBody.append("\n");
		messageBody.append("Regards");
		messageBody.append("The ARK");
		
		mailClient.setMessageBody(messageBody);
		mailClient.sendMail();
	}

	protected void onCancel(AjaxRequestTarget target) {
		setResponsePage(LoginPage.class);
		target.add(feedbackPanel);
	};
}