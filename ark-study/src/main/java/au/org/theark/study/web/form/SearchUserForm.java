package au.org.theark.study.web.form;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.study.web.Constants;

@SuppressWarnings("serial")
public class SearchUserForm extends Form<ArkUserVO>{
	
	TextField<String> userNameTxtField =new TextField<String>(Constants.USER_NAME);
	TextField<String> firstNameTxtField = new TextField<String>(Constants.FIRST_NAME);
	TextField<String> lastNameTxtField = new TextField<String>(Constants.LAST_NAME);
	TextField<String> emailTxtField = new TextField<String>(Constants.EMAIL);
	
	private void initFormFields(){
		emailTxtField.add(EmailAddressValidator.getInstance());
		firstNameTxtField.add(StringValidator.lengthBetween(3, 50));
		lastNameTxtField.add(StringValidator.lengthBetween(3, 50));
		userNameTxtField.add(StringValidator.lengthBetween(3, 50));
	}

	/* Form Constructor */
	public SearchUserForm(String id, ArkUserVO userVO, String panelId) {
	
		super(id, new CompoundPropertyModel<ArkUserVO>(userVO));
		initFormFields();
		/* Add the look up fields */
		add(userNameTxtField);
		add(firstNameTxtField);
		add(lastNameTxtField);
		add(emailTxtField);
		
		add(new Button(Constants.SEARCH, new StringResourceModel("page.search", this, null))
		{
			public void onSubmit()
			{
				
				onSearch((ArkUserVO) getForm().getModelObject());
			}
		});
		
		/**
		 * Allow to create a New User
		 */
		add(new Button(Constants.NEW, new StringResourceModel("page.new", this, null))
		{
			public void onSubmit()
			{
				//Go to Search users page
				//The mode will be new here
				ArkUserVO etaUserVO = new ArkUserVO();
				etaUserVO.setMode(Constants.MODE_NEW);
				onNew(etaUserVO);
			}
			@Override
			public boolean isVisible(){
				
				SecurityManager securityManager =  ThreadContext.getSecurityManager();
				Subject currentUser = SecurityUtils.getSubject();		
				boolean flag = false;
				if(		securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN) ||
						securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN)){
					flag = true;
				}
				//if it is a Super or Study admin then make the new available
				return flag;
			}
		
		});
	}

	/* Processing logic for search */
	protected  void onSearch(ArkUserVO userVO){}
	
	/* Processing logic for New User */
	protected void onNew(ArkUserVO etaUserVO){}
	
}



