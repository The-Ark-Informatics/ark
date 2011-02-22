package au.org.theark.web.pages;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
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
import org.apache.wicket.util.time.Duration;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.vo.ArkUserVO;

public class LoginPage<T> extends WebPage {
	
	private transient Logger log = LoggerFactory.getLogger(LoginPage.class);

	FeedbackPanel feedBackPanel = new FeedbackPanel("feedbackMessage");	
	
	/**
	 * Page Constructor
	 */
	@SuppressWarnings({ "serial", "unchecked" })
	public LoginPage(){

		log.info("LoginPage() constructor");
		
		feedBackPanel.setOutputMarkupId(true);
		
		LoginForm form = new LoginForm("loginForm");
		
		this.add(form);
		this.add(feedBackPanel);//Add the Feedback panel
		
		AjaxFormValidatingBehavior.addToAllFormComponents(form, "onKeyup", Duration.seconds(2));
		//Create an new form object and add it to the Page
		ContextImage hostedByImage = new ContextImage("hostedByImage",new Model<String>("images/"+Constants.HOSTED_BY_IMAGE));
		ContextImage productImage = new ContextImage("productImage", new Model<String>("images/"+Constants.PRODUCT_IMAGE));
		ContextImage bannerImage = new ContextImage("bannerImage", new Model<String>("images/"+Constants.BANNER_IMAGE));
		
		this.add( new Link("resetPasswordLink"){
			@Override
			public void onClick() {
				setResponsePage(ResetPage.class);
			}
		 });
		 
		 // Add images
		 add(hostedByImage);
		 add(productImage);
		 add(bannerImage);
		 
		 
	}
	
	@Override
	protected void configureResponse() {
	    super.configureResponse();
	    WebResponse response = getWebRequestCycle().getWebResponse();
	    response.setHeader("Cache-Control",
	          "no-cache, max-age=0,must-revalidate, no-store");
	    response.setHeader("Expires", "-1");
	    response.setHeader("Pragma", "no-cache");
	}
	
	/**
	 * The form class that the LoginPage will use to capture input.
	 */
	@SuppressWarnings("serial")
	class LoginForm extends StatelessForm<ArkUserVO>{

		TextField<String> userNameTxtFld = new TextField<String>("userName");
		PasswordTextField passwordTxtFld = new PasswordTextField("password");	
		Button subButton;
		
		
		private void decorateComponents(){
			ThemeUiHelper.buttonRoundedFocused(subButton);
			ThemeUiHelper.componentRounded(userNameTxtFld);
			ThemeUiHelper.componentRounded(passwordTxtFld);
		}
		
		private void addComponentsToForm(){
			add(userNameTxtFld);
			add(passwordTxtFld);
			add(subButton);
		}
		
		/* Constructor */
		public LoginForm(String id) {
			
			//Pass in the Model to the Form so the IFormSubmitListener can set the Model Object with values that were submitted.
 			
			super(id, new CompoundPropertyModel<ArkUserVO>(new ArkUserVO()));
			
			subButton =  new Button("submitBtn", new StringResourceModel("submit",null)){
			
				@Override
				public void onSubmit()
				{
					ArkUserVO user = (ArkUserVO)getForm().getModelObject();
					if(authenticate(user))
					{
						setResponsePage(HomePage.class);
					}
				}
			};
			
			decorateComponents();
			addComponentsToForm();
		}
		
		public final boolean authenticate(ArkUserVO user){
			
			Subject subject = SecurityUtils.getSubject();
			UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUserName(),user.getPassword(),false);//Disable Remember me

			try
		    {
		    	subject.login(usernamePasswordToken);//this will propagate to the Realm
		    	return true;
		    }
		    catch (IncorrectCredentialsException ice)
		    {
		    	String errMessage = getLocalizer().getString("page.incorrect.password", LoginPage.this,"Password is incorrect");
		    	error(errMessage);
		    }
		    catch (UnknownAccountException uae)
		    {
		    	
		      String errMessage = getLocalizer().getString("page.account.notfound", LoginPage.this,"User account not found.");
		      error(errMessage);
		    }
		    catch (AuthenticationException ae)
		    {
		    	
		    	String errMessage = getLocalizer().getString("page.invalid.username.password", LoginPage.this,"Invalid username and/or password."); 	
		    	error(errMessage);
		    }
		    catch( Exception ex ) {
		    	String errMessage = getLocalizer().getString("page.login.failed", LoginPage.this,"Login Failed.");
		    	error(errMessage);
		    }

			return false;
		}
	}
}
