package au.org.theark.study.web.component.user;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.util.UIHelper;
import au.org.theark.core.vo.EtaUserVO;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.core.vo.StudyVO;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.form.AppRoleForm;
import au.org.theark.study.web.form.UserForm;

public class DisplayUserListPanel extends Panel{


	private List<EtaUserVO> userList;
	private UserDetailsPanel detailsPanel;
	
	@SpringBean( name = "userService")
	private transient IUserService userService;
	
	public DisplayUserListPanel(String id, List<EtaUserVO> userVOList, Component component) {
		super(id);
		userList = userVOList;
		detailsPanel = (UserDetailsPanel) component;
		PageableListView pageableUserList = buildUserPageableListView(userList, 10);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", pageableUserList);
		add(pageNavigator);
		add(pageableUserList);
		
	}


	
	@SuppressWarnings("unchecked")
	public PageableListView buildUserPageableListView(List<EtaUserVO> userVOList, int rowsPerPage){
		
		PageableListView  pageableListView = new PageableListView("userList", userVOList, rowsPerPage){

			@Override
			protected void populateItem(final ListItem item) {
				EtaUserVO userVO = (EtaUserVO) item.getModelObject();

				Link userNameLink = buildUserNameLink(item, detailsPanel);
				/* Build the caption for the Link*/
				Label userNameLinkLabel = new Label("userNameLink", userVO.getUserName());
				userNameLink.add(userNameLinkLabel);
				item.add(userNameLink);
				
				item.add(new Label("lastName", userVO.getLastName()));//the ID here must match the ones in mark-up
				item.add(new Label("firstName", userVO.getFirstName()));
				item.add(new Label("email", userVO.getEmail()));
				
				//If we used DataView then can override newItem and return a Wicket Extension module back. EvenorOdd
				//For a PageableListView we will implement the even odd logic
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
				
			}
		
		};
		return pageableListView;
		
	}
	

	@SuppressWarnings("unchecked")
	private Link buildUserNameLink(final ListItem item, final Component detailsPanel) {
		
		final EtaUserVO etaUserVO = (EtaUserVO) item.getModelObject();

		return new Link("userName", item.getModel()) {
			@Override
			public void onClick() {
				//If the selected record is the same as the logged in user then allow for an edit
				UserDetailsPanel userDetailsPanel = (UserDetailsPanel)detailsPanel;
				UserForm userForm = userDetailsPanel.getUserForm();
				
				SecurityManager securityManager =  ThreadContext.getSecurityManager();
				Subject currentUser = SecurityUtils.getSubject();
				
				//If the selected record belongs to Subject or if the logged in user is an Administrator then allow edit
				String subject = (String)currentUser.getPrincipal();
				if(subject.equals(etaUserVO.getUserName()) 	||	securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN) 
															||  securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN) )
				{
					updateFormState(userForm, true);
					
					if(subject.equals(etaUserVO.getUserName())){
						userForm.getGroupPasswordContainer().setVisible(true);	
					}else{
						userForm.getGroupPasswordContainer().setVisible(false);
					}
					
				
				}else
				{
					updateFormState(userForm, false);
				}
				etaUserVO.setMode(Constants.MODE_EDIT);
				userForm.setModelObject(etaUserVO);
				userDetailsPanel.setVisible(true);
				//Create the UserDetailsPanel.
				//Gain access to the existing accordion control in UserForm
				AppRoleAccordion appRoleAccordion = (AppRoleAccordion) userForm.get("appRoleAccordion");
				AppRoleForm appRoleForm = (AppRoleForm) appRoleAccordion.get(Constants.APP_ROLE_FORM);
				//Rebuild the accordion and attach it to the Form
				try {
					
					//Get the study in context
					//TODO NN Remove this once we have Study Module Testing
					StudyVO studyVO = new StudyVO();
					studyVO.setStudyName("demo");
					etaUserVO.setStudyVO(studyVO);
					userForm.remove(appRoleAccordion);
					List<ModuleVO> modules = userService.getModules(false);//List of all available modules and list of roles define under these modules and which will apply for studies under this module
					
					List<ModuleVO> userModules = userService.getUserRoles(etaUserVO, studyVO.getStudyName());
					etaUserVO.setModules(userModules);
					//userService.getUserRole(etaUserVO,modules);//Gets a list of currents roles for the given study for each of the module
					
					UIHelper.getDisplayModuleName(modules);
					appRoleAccordion = new AppRoleAccordion(Constants.APP_ROLE_ACCORDION, etaUserVO, modules);
					

				} catch (ArkSystemException e) {
					e.printStackTrace();
				}
				//Accordion will have its own form object
				userForm.add(appRoleAccordion);
			}
		};
		
	}
	
	private void updateFormState(UserForm userForm, boolean enable){
		userForm.getUserNameTxtField().setEnabled(enable);
		userForm.getFirstNameTxtField().setEnabled(enable);
		userForm.getLastNameTxtField().setEnabled(enable);
		userForm.getEmailTxtField().setEnabled(enable);
	}
	
}
