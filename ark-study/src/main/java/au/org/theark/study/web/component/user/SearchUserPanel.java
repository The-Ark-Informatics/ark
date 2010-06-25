package au.org.theark.study.web.component.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.vo.EtaUserVO;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
/**
 * A custom domain(eta) component for searching for users in the system. The results of the search can 
 * be fed into another custom component that accepts a List<?> (need to define what a Custom DataView component will receive)
 * @author nivedann
 *
 */
public class SearchUserPanel extends Panel{

	private transient Logger log = LoggerFactory.getLogger(SearchUserPanel.class);
	
	/* 
	 * Spring injected reference for User service implementation. Use it to look up users or persist using the userService
	 * userService.create(EtaUserVo user);
	 **/
	@SpringBean( name = "userService")
	private IUserService userService;
	
	
	private String panelId;
	
	/* Will contain the Search results */
	private DisplayUserListPanel displayUserListPanel;
	
	private FeedbackPanel feedBackPanel = new FeedbackPanel("feedbackMessage");
	
	public DisplayUserListPanel getDisplayUserListPanel() {
		return displayUserListPanel;
	}

	public void setDisplayUserListPanel(DisplayUserListPanel displayUserListPanel) {
		this.displayUserListPanel = displayUserListPanel;
	}

	/* The detailed view of the person */
	private UserDetailsPanel detailsPanel;

	/**
	 * Pass in a boolean value to toggle the visibility status of User Details panel.
	 * @param flag
	 */
	public void setDetailsPanelVisible(boolean flag){
		detailsPanel.setVisible(flag);
	}
	
	/**
	 * Standard Constructor for the Search Panel 
	 * @param id
	 */
	public SearchUserPanel(String id) {
		super(id);
		
		//Create a new instance of the details panel with empty user object
		detailsPanel = new UserDetailsPanel("userDetailsPanel", new EtaUserVO(), this);
		//Hide it since we have not looked up a user as yet
		setDetailsPanelVisible(false);
		
		// Uses an entirely new VO for the search so each time the search panel is loaded. The values provided will be refreshed.
		SearchUserForm searchUserForm = new SearchUserForm(Constants.SEARCH_USER_FORM, new EtaUserVO(), id){
			
			/*When user has clicked on the Search Button*/
			protected  void onSearch(EtaUserVO userVO){
				log.info("Look up the user");
				try {
					setDetailsPanelVisible(false);//Set the User Details panel to false/hide it
					List<EtaUserVO> userResultList = userService.searchUser(userVO);
					if(userResultList != null && userResultList.size() == 0){
						this.info("A user with the specified criteria does not exist in the system.");	
					}
					
					//Render the Search Results
					//TODO NN Removing the panel is not efficient, I need to use another technique (Ajax)
					remove(displayUserListPanel);//Since we already have the panel.We need to partially update the list the panel uses  rather than action it at the panel level
					displayUserListPanel = new DisplayUserListPanel("displayUserListPanel", userResultList,detailsPanel);
					//Temp TODO remove this once Layouts are corrected
					add(displayUserListPanel);
					
					//Provide the list as an argument into the results rendering panel
				}
				catch (ArkSystemException e) {
					log.error("Exception occured when looking up a user in LDAP" + e.getMessage());
				}
			}
		};

		//Add the Form to the Panel. The Form object that will contain the child or UI components that will be part of the search or be affected by the search.
		add(searchUserForm);
		add(feedBackPanel); //Add feedback panel
		/*
		 * Create an instance of the Details panel. When the mode is in lookup hide the panel.
		 * The details panel is only visible when user navigates an item via the ResultsList or when creating a New User.
		 */
		/* Add the UserDetailsPanel into the scope of the SearchUserForm instance*/
		
		searchUserForm.add(detailsPanel);
		
		//Just an empty list
		List<EtaUserVO> userList = new ArrayList<EtaUserVO>();
		EtaUserVO user = new EtaUserVO();
		userList.add(user);
		//TODO Remove comments after testing layouts
		displayUserListPanel = new DisplayUserListPanel("displayUserListPanel", userList,detailsPanel);
		searchUserForm.add(displayUserListPanel);
	}

	/**
	 * The Form object for the Search.
	 */
	public class SearchUserForm extends Form<EtaUserVO>{

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
		private static final long serialVersionUID = 1L;

		/* Form Constructor */
		public SearchUserForm(String id, EtaUserVO userVO, String panelId) {
		
			super(id, new CompoundPropertyModel<EtaUserVO>(userVO));
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
					
					onSearch((EtaUserVO) getForm().getModelObject());
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
					EtaUserVO etaUserVO = new EtaUserVO();
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
		protected  void onSearch(EtaUserVO userVO){}
		
		/* Processing logic for New User */
		protected void onNew(EtaUserVO etaUserVO){
			//Set the Model Object for the existing Form object in DetailsPanel
			detailsPanel.getUserForm().getGroupPasswordContainer().setVisible(true);
			detailsPanel.getUserForm().setModelObject(etaUserVO);
			detailsPanel.setVisible(true);
			detailsPanel.getUserForm().getUserNameTxtField().setEnabled(true);
			//Hide the Search and Search Results panels.
			displayUserListPanel.setVisible(false);
		}
		
	}
}
