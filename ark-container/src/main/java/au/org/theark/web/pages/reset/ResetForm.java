package au.org.theark.web.pages.reset;

import java.io.Serializable;

import org.apache.velocity.exception.VelocityException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ArkUserPasswordGenerator;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.study.service.IUserService;
import au.org.theark.web.pages.login.LoginPage;

/**
 * <p>
 * The <code>ResetForm</code> class that extends the {@link org.apache.wicket.markup.html.form.Form Form} class. It provides the implementation of the
 * reset password form of The Ark application.
 * </p>
 * 
 * @author nivedann
 * @author cellis
 */
public class ResetForm extends Form<ArkUserVO> implements Serializable {

	private static final long			serialVersionUID	= 5759125957232986063L;
	private transient static Logger	log					= LoggerFactory.getLogger(ResetForm.class);
	private FeedbackPanel				feedbackPanel;
	private TextField<String>			userName;
	private Panel							reCaptchaPanel;
	private AjaxButton					resetButton;
	private AjaxButton					cancelButton;
	private AjaxButton					signInButton;

	@SpringBean(name = "userService")
	private IUserService					iUserService;
	
	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;

	/**
	 * 
	 * @param id
	 */
	public ResetForm(String id, final FeedbackPanel feedbackPanel) {
		super(id, new CompoundPropertyModel<ArkUserVO>(new ArkUserVO()));
		this.feedbackPanel = feedbackPanel;

		userName = new TextField<String>("userName");
		userName.setRequired(true);
		userName.setOutputMarkupId(true);

		// TODO: Possibly reimplement reCAPTCHA
		//reCaptchaPanel = new ReCaptchaPanel("reCaptchaPanel");
		reCaptchaPanel = new EmptyPanel("reCaptchaPanel");
		reCaptchaPanel.setOutputMarkupId(true);

		resetButton = new AjaxButton("resetButton") {

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
		resetButton.setOutputMarkupId(true);

		cancelButton = new AjaxButton("cancelButton") {

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
		cancelButton.setOutputMarkupId(true);

		signInButton = new AjaxButton("signInButton") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				log.error("Error on loginButton click");
				target.add(feedbackPanel);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onCancel(target);

			}
		};
		signInButton.setDefaultFormProcessing(false);
		signInButton.setVisible(false);
		signInButton.setOutputMarkupPlaceholderTag(true);

		addFormComponents();
	}

	private void addFormComponents() {
		add(userName);
		add(reCaptchaPanel);
		add(resetButton);
		add(cancelButton);
		add(signInButton);
	}

	protected void onReset(AjaxRequestTarget target) {
		try {
			String password = ArkUserPasswordGenerator.generate();

			// Get the ArkUser from back-end to be sure
			ArkUserVO arkUserVo = iUserService.getCurrentUser(getModelObject().getUserName());
			
			// Deny reset of password on super administrators!
			if(!iArkCommonService.isSuperAdministrator(arkUserVo.getUserName())){
				// Set new password
				arkUserVo.setPassword(password);
				setModelObject(arkUserVo);
	
				// Send an email. If send failed (eg no smtp access, password not reset)
				sendNotificationEmail(target);
				
				// Save new password to LDAP
				iUserService.resetArkUserPassword(arkUserVo);
				this.info(new StringResourceModel("password.updated", this, null).getString());
				postResetPassword(target);	
			}
			else {
				this.error(new StringResourceModel("ark.super.user.error", this, null).getString());
				target.add(feedbackPanel);
				log.error(getModelObject().getUserName() + " was attempted to have password reset");
			}
		}
		catch (ArkSystemException e) {
			this.error(new StringResourceModel("ark.system.error", this, null).getString());
			target.add(feedbackPanel);
			log.error(e.getMessage());
		}
		catch (EntityNotFoundException e) {
			this.error(new StringResourceModel("user.notFound", this, null).getString());
			target.add(feedbackPanel);
			log.error(e.getMessage());
		}
		catch(MailSendException e){
			this.error(new StringResourceModel("mail.system.error", this, null).getString());
			target.add(feedbackPanel);
			log.error(e.getMessage());
		}
		target.appendJavaScript("Recaptcha.reload()");
	};

	private void postResetPassword(AjaxRequestTarget target) {
		userName.setEnabled(false);
		target.add(userName);
		target.appendJavaScript("Recaptcha.reload()");
		
		reCaptchaPanel.setEnabled(false);
		target.add(reCaptchaPanel);

		resetButton.setEnabled(false);
		cancelButton.setEnabled(false);
		target.add(resetButton);
		target.add(cancelButton);

		signInButton.setVisible(true);
		target.add(signInButton);
		target.add(feedbackPanel);
	}

	/**
	 * Email the user with new reset password
	 * @param target
	 * @throws MailSendException
	 */
	private void sendNotificationEmail(AjaxRequestTarget target) throws MailSendException {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(getModelObject().getUserName());
		mail.setSubject("Your ARK password has been reset");
		
		try {
			String text = iArkCommonService.setResetPasswordMessage(getFullName(), getModelObject().getPassword());
			mail.setText(text);
			iArkCommonService.sendEmail(mail);
		}
		catch(MailSendException e){
			this.error(new StringResourceModel("mail.system.error", this, null).getString());
			target.add(feedbackPanel);
			log.error(e.getMessage());
		}
		catch(VelocityException e){
			this.error(new StringResourceModel("velocity.system.error", this, null).getString());
			target.add(feedbackPanel);
			log.error(e.getMessage());
		}
	}

	/**
	 * Get the full name of the ArkUser
	 * 
	 * @return
	 */
	private String getFullName() {
		StringBuffer sb = new StringBuffer();
		String firstName = getModelObject().getFirstName();
		String lastName = getModelObject().getLastName();

		if (firstName != null) {
			sb.append(getModelObject().getFirstName());
			sb.append(" ");
		}
		if (lastName != null) {
			sb.append(getModelObject().getLastName());
		}
		return sb.toString();
	}

	protected void onCancel(AjaxRequestTarget target) {
		setResponsePage(LoginPage.class);
	};
}