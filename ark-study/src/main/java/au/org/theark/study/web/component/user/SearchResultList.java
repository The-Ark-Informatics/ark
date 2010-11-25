package au.org.theark.study.web.component.user;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.UIHelper;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.core.vo.StudyVO;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.user.form.ContainerForm;
import au.org.theark.study.web.form.AppRoleForm;
import au.org.theark.study.web.form.UserForm;

@SuppressWarnings("serial")
public class SearchResultList extends Panel{


	private List<ArkUserVO> userList;
	private Details detailsPanel;
	
	
	private WebMarkupContainer searchMarkupContainer;
	private WebMarkupContainer detailsMarkupContainer;
	private ContainerForm userContainerForm;
	
	@SpringBean( name = "userService")
	private transient IUserService userService;
	
	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	/**
	 * Current constructor
	 * @param id
	 * @param searchWebMarkupContainer
	 * @param detailsWebMarkupContainer
	 * @param containerForm
	 */
	public SearchResultList(String id, 
							WebMarkupContainer searchWebMarkupContainer, 
							WebMarkupContainer detailsWebMarkupContainer,
							ContainerForm containerForm,
							Details detailPanel){
		
		super(id);
		
		this.searchMarkupContainer = searchWebMarkupContainer;
		this.detailsMarkupContainer = detailsWebMarkupContainer;
		this.userContainerForm = containerForm;	
		this.detailsPanel = detailPanel;
		
	}
	
	/**
	 * Previous Constructor:
	 * @param id
	 * @param userVOList
	 * @param component
	 */
//	public SearchResultList(String id, List<ArkUserVO> userVOList, Component component) {
//		super(id);
//		userList = userVOList;
//		detailsPanel = (Details) component;
//		PageableListView<ArkUserVO> pageableUserList = buildUserPageableListView(userList, 10);
//		PagingNavigator pageNavigator = new PagingNavigator("navigator", pageableUserList);
//		add(pageNavigator);
//		add(pageableUserList);
//		
//	}

	/**
	 * User of IModel to render the list
	 * @param iModel
	 * @param searchResultsContainer
	 * @return
	 */
	public PageableListView<ArkUserVO> buildUserPageableListView(IModel iModel, final WebMarkupContainer searchResultsContainer){
		
		PageableListView<ArkUserVO>  pageableListView = new PageableListView<ArkUserVO>("userList", iModel, 10){

			@Override
			protected void populateItem(final ListItem<ArkUserVO> item) {
				
				ArkUserVO arkUserVO =  item.getModelObject();

				//Link userNameLink = buildUserNameLink(item, detailsPanel);
				/* Build the caption for the Link*/
				//Label userNameLinkLabel = new Label("userNameLink", arkUserVO.getUserName());
				//userNameLink.add(userNameLinkLabel);
				//item.add(userNameLink);
				
				item.add(buildLink(arkUserVO, searchResultsContainer));
				
				item.add(new Label("lastName", arkUserVO.getLastName()));//the ID here must match the ones in mark-up
				item.add(new Label("firstName", arkUserVO.getFirstName()));
				item.add(new Label("email", arkUserVO.getEmail()));
				
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
	
//	public PageableListView<ArkUserVO> buildUserPageableListView(List<ArkUserVO> userVOList, int rowsPerPage){
//		
//		PageableListView<ArkUserVO>  pageableListView = new PageableListView<ArkUserVO>("userList", userVOList, rowsPerPage){
//
//			@Override
//			protected void populateItem(final ListItem<ArkUserVO> item) {
//				
//				ArkUserVO arkUserVO =  item.getModelObject();
//
//				Link userNameLink = buildUserNameLink(item, detailsPanel);
//				/* Build the caption for the Link*/
//				Label userNameLinkLabel = new Label("userNameLink", arkUserVO.getUserName());
//				userNameLink.add(userNameLinkLabel);
//				item.add(userNameLink);
//				
//				item.add(new Label("lastName", arkUserVO.getLastName()));//the ID here must match the ones in mark-up
//				item.add(new Label("firstName", arkUserVO.getFirstName()));
//				item.add(new Label("email", arkUserVO.getEmail()));
//				
//				//If we used DataView then can override newItem and return a Wicket Extension module back. EvenorOdd
//				//For a PageableListView we will implement the even odd logic
//				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
//					@Override
//					public String getObject() {
//						return (item.getIndex() % 2 == 1) ? "even" : "odd";
//					}
//				}));
//				
//			}
//		
//		};
//		return pageableListView;
//		
//	}
	
	
	private AjaxLink buildLink(final ArkUserVO arkUserVo, final WebMarkupContainer searchResultsContainer){
		
		AjaxLink link = new AjaxLink("userName") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				if(sessionStudyId == null ){
					info("There is no study in context.Please select a study from Study Module to view the user details");
				}else{
					
					userContainerForm.setModelObject(arkUserVo);
					detailsMarkupContainer.setVisible(true);
					UserForm userForm = detailsPanel.getUserForm();
					
					SecurityManager securityManager =  ThreadContext.getSecurityManager();
					Subject currentUser = SecurityUtils.getSubject();
					
					//If the selected record belongs to Subject or if the logged in user is an Administrator then allow edit
					String subject = (String)currentUser.getPrincipal();
					if(subject.equals(arkUserVo.getUserName()) 	||	securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN) 
																||  securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN) )
					{
						updateFormState(userForm, true);
						
						if(subject.equals(arkUserVo.getUserName())){
							userForm.getGroupPasswordContainer().setVisible(true);	
						}else{
							userForm.getGroupPasswordContainer().setVisible(false);
						}
						
					
					}else
					{
						updateFormState(userForm, false);
					}
					arkUserVo.setMode(Constants.MODE_EDIT);
					//userForm.setModelObject(arkUserVo);
					
					userForm.getUserNameTxtField().setEnabled(false);
					//Create the Details.
					//Gain access to the existing accordion control in UserForm
					AppRoleAccordion appRoleAccordion = (AppRoleAccordion) userForm.get("appRoleAccordion");
					AppRoleForm appRoleForm = (AppRoleForm) appRoleAccordion.get(Constants.APP_ROLE_FORM);
					//Rebuild the accordion and attach it to the Form
					try {
						//Lookup this study
						Study study = iArkCommonService.getStudy(sessionStudyId);
						StudyVO studyVO = new StudyVO();
						studyVO.setStudyName(study.getName());
						arkUserVo.setStudyVO(studyVO);
						userForm.remove(appRoleAccordion);
						List<ModuleVO> modules = userService.getModules(false);//List of all available modules and list of roles define under these modules and which will apply for studies under this module
						
						List<ModuleVO> userModules = userService.getUserRoles(arkUserVo, studyVO.getStudyName());
						arkUserVo.setModules(userModules);
						//userService.getUserRole(etaUserVO,modules);//Gets a list of currents roles for the given study for each of the module
						
						UIHelper.getDisplayModuleName(modules);
						appRoleAccordion = new AppRoleAccordion(Constants.APP_ROLE_ACCORDION, arkUserVo, modules);
						

					} catch (ArkSystemException e) {
						e.printStackTrace();
					}
					//Accordion will have its own form object
					userForm.add(appRoleAccordion);
					searchMarkupContainer.setVisible(false);
					searchResultsContainer.setVisible(false);
					target.addComponent(detailsMarkupContainer);
					target.addComponent(searchMarkupContainer);
					target.addComponent(searchResultsContainer);
				}
				
			}

		};
		
		//Add the label for the link
		Label userNameLinkLabel = new Label("userNameLink", arkUserVo.getUserName());
		link.add(userNameLinkLabel);
		return link;
		
	}

//	@SuppressWarnings("unchecked")
//	private Link buildUserNameLink(final ListItem<ArkUserVO> item, final Component detailsPanel) {
//		
//		final ArkUserVO etaUserVO = item.getModelObject();
//
//		return new Link("userName", item.getModel()) {
//			@Override
//			public void onClick() {
//				
//				Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
//				if(sessionStudyId == null ){
//					info("There is no study in context.Please select a study from Study Module to view the user details");
//				}
//				else{
//
//					//If the selected record is the same as the logged in user then allow for an edit
//					Details userDetailsPanel = (Details)detailsPanel;
//					UserForm userForm = userDetailsPanel.getUserForm();
//					
//					SecurityManager securityManager =  ThreadContext.getSecurityManager();
//					Subject currentUser = SecurityUtils.getSubject();
//					
//					//If the selected record belongs to Subject or if the logged in user is an Administrator then allow edit
//					String subject = (String)currentUser.getPrincipal();
//					if(subject.equals(etaUserVO.getUserName()) 	||	securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN) 
//																||  securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN) )
//					{
//						updateFormState(userForm, true);
//						
//						if(subject.equals(etaUserVO.getUserName())){
//							userForm.getGroupPasswordContainer().setVisible(true);	
//						}else{
//							userForm.getGroupPasswordContainer().setVisible(false);
//						}
//						
//					
//					}else
//					{
//						updateFormState(userForm, false);
//					}
//					etaUserVO.setMode(Constants.MODE_EDIT);
//					userForm.setModelObject(etaUserVO);
//					userDetailsPanel.setVisible(true);
//					userDetailsPanel.getUserForm().getUserNameTxtField().setEnabled(false);
//					//Create the Details.
//					//Gain access to the existing accordion control in UserForm
//					AppRoleAccordion appRoleAccordion = (AppRoleAccordion) userForm.get("appRoleAccordion");
//					AppRoleForm appRoleForm = (AppRoleForm) appRoleAccordion.get(Constants.APP_ROLE_FORM);
//					//Rebuild the accordion and attach it to the Form
//					try {
//						//Lookup this study
//						Study study = studyService.getStudy(sessionStudyId);
//						StudyVO studyVO = new StudyVO();
//						studyVO.setStudyName(study.getName());
//						etaUserVO.setStudyVO(studyVO);
//						userForm.remove(appRoleAccordion);
//						List<ModuleVO> modules = userService.getModules(false);//List of all available modules and list of roles define under these modules and which will apply for studies under this module
//						
//						List<ModuleVO> userModules = userService.getUserRoles(etaUserVO, studyVO.getStudyName());
//						etaUserVO.setModules(userModules);
//						//userService.getUserRole(etaUserVO,modules);//Gets a list of currents roles for the given study for each of the module
//						
//						UIHelper.getDisplayModuleName(modules);
//						appRoleAccordion = new AppRoleAccordion(Constants.APP_ROLE_ACCORDION, etaUserVO, modules);
//						
//
//					} catch (ArkSystemException e) {
//						e.printStackTrace();
//					}
//					//Accordion will have its own form object
//					userForm.add(appRoleAccordion);
//				}
//					
//				}
//		};
//		
//	}
	
	private void updateFormState(UserForm userForm, boolean enable){
		userForm.getUserNameTxtField().setEnabled(enable);
		userForm.getFirstNameTxtField().setEnabled(enable);
		userForm.getLastNameTxtField().setEnabled(enable);
		userForm.getEmailTxtField().setEnabled(enable);
	}
	
}
