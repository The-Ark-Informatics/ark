package au.org.theark.web.pages;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.component.ArkIndicatingAjaxButton;
import au.org.theark.core.web.form.ArkFormVisitor;

public class LoginPage<T> extends WebPage
{
	private transient Logger log = LoggerFactory.getLogger(LoginPage.class);

	private FeedbackPanel feedBackPanel	= new FeedbackPanel("feedbackMessage");

	// Add a visitor class for required field marking/validation/highlighting
	private IVisitor	formVisitor		= new ArkFormVisitor();

	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	@SuppressWarnings("unchecked")
	public void onBeforeRender()
	{
		super.onBeforeRender();
		visitChildren(formVisitor);
	}

	/**
	 * Page Constructor
	 */
	@SuppressWarnings( { "serial", "unchecked" })
	public LoginPage()
	{
		//log.info("LoginPage() constructor");

		feedBackPanel.setOutputMarkupId(true);

		LoginForm form = new LoginForm("loginForm");

		this.add(form);
		this.add(feedBackPanel);

		//AjaxFormValidatingBehavior.addToAllFormComponents(form, "onKeyup", Duration.seconds(2));
		ContextImage hostedByImage = new ContextImage("hostedByImage", new Model<String>("images/" + Constants.HOSTED_BY_IMAGE));
		ContextImage productImage = new ContextImage("productImage", new Model<String>("images/" + Constants.PRODUCT_IMAGE));

		Link resetPasswordLink = new Link("resetPasswordLink")
		{
			@Override
			public void onClick()
			{
				setResponsePage(ResetPage.class);
			}
		};

		// TODO: Implement ResetPage.resetForm.onReset
		resetPasswordLink.setVisible(false);
		this.add(resetPasswordLink);

		// Add images
		add(hostedByImage);
		add(productImage);
	}

	@Override
	protected void configureResponse()
	{
		super.configureResponse();
		WebResponse response = getWebRequestCycle().getWebResponse();
		response.setHeader("Cache-Control", "no-cache, max-age=0,must-revalidate, no-store");
		response.setHeader("Expires", "-1");
		response.setHeader("Pragma", "no-cache");
	}

	/**
	 * The form class that the LoginPage will use to capture input.
	 */
	@SuppressWarnings("serial")
	class LoginForm extends StatelessForm<ArkUserVO>
	{
		TextField<String>	userNameTxtFld	= new TextField<String>("userName");
		PasswordTextField	passwordTxtFld	= new PasswordTextField("password");
		AjaxButton			submitButton;

		private void decorateComponents()
		{
			ThemeUiHelper.buttonRoundedFocused(submitButton);
			ThemeUiHelper.componentRounded(userNameTxtFld);
			ThemeUiHelper.componentRounded(passwordTxtFld);
		}

		private void addComponentsToForm()
		{
			add(userNameTxtFld.setRequired(true));
			add(passwordTxtFld.setRequired(true));
			add(submitButton);
		}

		/* Constructor */
		public LoginForm(String id)
		{
			// Pass in the Model to the Form so the IFormSubmitListener can set the Model Object with values that were submitted.
			super(id, new CompoundPropertyModel<ArkUserVO>(new ArkUserVO()));

			submitButton = new ArkIndicatingAjaxButton("submitBtn", new StringResourceModel("submit", null))
			{
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form)
				{
					ArkUserVO user = (ArkUserVO) getForm().getModelObject();
					if (authenticate(target, user))
					{
						ArkFunction  arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_STUDY); //Place a default use case into session
						ArkModule arkModule = iArkCommonService.getArkModuleByName(au.org.theark.core.Constants.ARK_MODULE_STUDY); //Place a default module into session
						SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_FUNCTION_KEY, arkFunction.getId());
						SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY, arkModule.getId());
						setResponsePage(HomePage.class);
						
					}
				}
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form)
				{
					target.addComponent(feedBackPanel);
				}
			};

			decorateComponents();
			addComponentsToForm();
		}

		public final boolean authenticate(AjaxRequestTarget target, ArkUserVO user)
		{
			Subject subject = SecurityUtils.getSubject();
			UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUserName(), user.getPassword(), false);// Disable Remember me

			try
			{
				subject.login(usernamePasswordToken);// this will propagate to the Realm
				return true;
			}
			catch (IncorrectCredentialsException ice)
			{
				String errMessage = getLocalizer().getString("page.incorrect.password", LoginPage.this, "Password is incorrect");
				error(errMessage);
			}
			catch (UnknownAccountException uae)
			{
				String errMessage = getLocalizer().getString("page.account.notfound", LoginPage.this, "User account not found.");
				error(errMessage);
			}
			catch (AuthenticationException ae)
			{
				String errMessage = getLocalizer().getString("page.invalid.username.password", LoginPage.this, "Invalid username and/or password.");
				error(errMessage);
			}
			catch (Exception ex)
			{
				String errMessage = getLocalizer().getString("page.login.failed", LoginPage.this, "Login Failed.");
				error(errMessage);
			}

			target.addComponent(feedBackPanel);
			return false;
		}
	}
}