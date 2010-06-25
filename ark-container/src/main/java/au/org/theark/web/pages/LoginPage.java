package au.org.theark.web.pages;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.vo.EtaUserVO;
import au.org.theark.web.component.FormFeedbackBorder;

public class LoginPage<T> extends WebPage {
	
	private transient Logger log = LoggerFactory.getLogger(LoginPage.class);

	FeedbackPanel feedBackPanel = new FeedbackPanel("feedbackMessage");	
	
	String userName;
	String password;
	
	//Validators
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Page Constructor
	 */
	public LoginPage(){

		log.info("LoginPage() constructor");
		
		LoginForm form = new LoginForm("loginForm");
		feedBackPanel.setOutputMarkupId(true);
		
		this.add(form);
		this.add(feedBackPanel);//Add the Feedback panel to the form
		
		//Declare the form components here
	
		TextField<String> userNameTxtFld = new TextField<String>("userName");
		PasswordTextField passwordTxtFld = new PasswordTextField("password");
		Button subButton =  new Button("submitBtn", new StringResourceModel("submit",form, null));
		userNameTxtFld.setOutputMarkupId(true);//added now to see if we get the * next to the field.
		userNameTxtFld.setRequired(true);
		userNameTxtFld.add( EmailAddressValidator.getInstance());
		
		//addWithBorder(form, userNameTxtFld, "userNameBorder");
		AjaxFormValidatingBehavior.addToAllFormComponents(form, "onKeyup", Duration.seconds(2));
		
		//Add rest of the forms
		form.add(userNameTxtFld);
		form.add(passwordTxtFld);
		form.add(subButton);
		
		//Create an new form object and add it to the Page
		ContextImage contextHostedByImage = new ContextImage("hostedByImage",new Model("images/uwa-logo.gif"));
		ContextImage productImage = new ContextImage("productImage", new Model("images/obiba-logo.png"));
		//ContextImage footerImage = new ContextImage("footerImage", new Model("images/obiba-logo.png"));
		 this.add( new Link("resetPasswordLink"){
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				setResponsePage(ResetPage.class);
			}
		 });
		 
		 add(contextHostedByImage);//hosted by image
		 add(productImage);
		 //add(footerImage);
	}
	
	
	Border addWithBorder(LoginForm form, FormComponent component, String   borderId){
		//Custom border class with its own mark up
		FormFeedbackBorder border = new FormFeedbackBorder(borderId);
		
		//FormComponentFeedbackBorder border = new FormComponentFeedbackBorder(borderId);
		border.setOutputMarkupId(true);
		border.add(component);
		form.add(border);
		return border;
	}

	
	/**
	 * The form class that the LoginPage will use to capture input.
	 */
	class LoginForm extends StatelessForm<T>{

		private static final long serialVersionUID = 1L;
		/* Form Input Wicket Controls */
		
		//GeneralBorder border = new GeneralBorder("loginBorder");
		/* Constructor */
		public LoginForm(String id) {
			//Pass in the Model to the Form so the IFormSubmitListener can set the Model Object with values that were submitted.
 			super(id, new CompoundPropertyModel(new EtaUserVO()));
		}
		
		
		@Override
		public void onSubmit(){
			EtaUserVO user = (EtaUserVO) getModel().getObject();
			if(authenticate(user)){
				setResponsePage(HomePage.class);//Should be a common or index page that can eventually display all the modules ETA,Study Manager, GWAS etc.. and using proper
				//security to hide/un-hide tabs based on role.Clicking on the Module tabs then should render the Home page for each module with next level of context sensitive sub menus.
			}
		}
		
		public final boolean authenticate(EtaUserVO user){
			
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
