package au.org.theark.web.pages;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPostprocessingCallDecorator;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;

/**
 * <p>
 * The <code>LoginForm</code> class that extends the {@link org.apache.wicket.markup.html.form.StatelessForm StatelessForm} class. It provides the implementation of the
 * login form of The Ark application.
 * </p>
 * 
 * @author nivedann
 * @author cellis
 */
public class LoginForm extends StatelessForm<ArkUserVO> {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7482996457044513022L;
	private transient static Logger	log					= LoggerFactory.getLogger(LoginForm.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;
	private FeedbackPanel		feedbackPanel;
	private TextField<String>	userNameTxtFld		= new TextField<String>("userName");
	private PasswordTextField	passwordTxtFld		= new PasswordTextField("password");
	private AjaxButton			submitButton;

	/**
	 * LoginForm constructor
	 * 
	 * @param id
	 *           the Component identifier
	 */
	public LoginForm(String id, final FeedbackPanel feedbackPanel) {
		// Pass in the Model to the Form so the IFormSubmitListener can set the Model Object with values that were submitted.
		super(id, new CompoundPropertyModel<ArkUserVO>(new ArkUserVO()));
		this.feedbackPanel = feedbackPanel;
		
		submitButton = new AjaxButton("submitBtn") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				ArkUserVO user = (ArkUserVO) getForm().getModelObject();
				if (authenticate(target, user)) {
					// Place a default use case into session
					ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_STUDY);
					// Place a default module into session
					ArkModule arkModule = iArkCommonService.getArkModuleByName(au.org.theark.core.Constants.ARK_MODULE_STUDY);

					SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_FUNCTION_KEY, arkFunction.getId());
					SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY, arkModule.getId());
					setResponsePage(HomePage.class);
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() {
				return new AjaxPostprocessingCallDecorator(super.getAjaxCallDecorator()) {
					private static final long	serialVersionUID		= 1L;

					private String					setBusyIndicatorOn	= "document.body.style.cursor = 'wait';";
					private String					setBusyIndicatorOff	= "document.body.style.cursor = 'auto';";

					@Override
					public CharSequence postDecorateScript(Component component, CharSequence script) {
						return script + setBusyIndicatorOn;
					}

					@Override
					public CharSequence postDecorateOnFailureScript(Component component, CharSequence script) {
						return script + setBusyIndicatorOff;
					}

					@Override
					public CharSequence postDecorateOnSuccessScript(Component component, CharSequence script) {
						return script + setBusyIndicatorOff;
					}
				};
			}
		};
		addComponentsToForm();
	}

	private void addComponentsToForm() {
		add(userNameTxtFld.setRequired(true));
		add(passwordTxtFld.setRequired(true));
		add(submitButton);
	}

	/**
	 * Authenticate the given user
	 * 
	 * @param target
	 *           the AjaxRequestTarget
	 * @param user
	 *           the given user to authenticate
	 * @return
	 */
	public final boolean authenticate(AjaxRequestTarget target, ArkUserVO user) {
		Subject subject = SecurityUtils.getSubject();
		// Disable Remember me
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUserName(), user.getPassword(), false);

		try {
			// This will propagate to the Realm
			subject.login(usernamePasswordToken);
			return true;
		}
		catch (IncorrectCredentialsException e) {
			String errMessage = getLocalizer().getString("page.incorrect.password", LoginForm.this, "Password is incorrect");
			error(errMessage);
			log.error(e.getMessage());
		}
		catch (UnknownAccountException e) {
			String errMessage = getLocalizer().getString("page.account.notfound", LoginForm.this, "User account not found.");
			error(errMessage);
			log.error(e.getMessage());
		}
		catch (AuthenticationException e) {
			String errMessage = getLocalizer().getString("page.invalid.username.password", LoginForm.this, "Invalid username and/or password.");
			error(errMessage);
			log.error(e.getMessage());
		}
		catch (Exception e) {
			String errMessage = getLocalizer().getString("page.login.failed", LoginForm.this, "Login Failed.");
			error(errMessage);
			log.error(e.getMessage());
		}

		target.add(feedbackPanel);
		return false;
	}
}
