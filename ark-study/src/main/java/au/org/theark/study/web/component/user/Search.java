package au.org.theark.study.web.component.user;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.user.form.ContainerForm;
import au.org.theark.study.web.form.SearchUserForm;
/**
 * A custom domain(eta) component for searching for users in the system. The results of the search can 
 * be fed into another custom component that accepts a List<?> (need to define what a Custom DataView component will receive)
 * @author nivedann
 *
 */
@SuppressWarnings("serial")
public class Search extends Panel{
	/* 
	 * Spring injected reference for User service implementation. Use it to look up users or persist using the userService
	 * userService.create(EtaUserVo user);
	 **/
	@SpringBean( name = "userService")
	private IUserService userService;
	
	private PageableListView<ArkUserVO> listView;

	private WebMarkupContainer searchMarkupContainer;
	//The container to wrap the Search Result List
	private WebMarkupContainer listContainer;
	//The Container to wrap the details panel
	private WebMarkupContainer detailsContainer;
	
	private ContainerForm containerForm;

	private FeedbackPanel fbPanel;
	private Details userDetailPanel;
	/**
	 * Standard Constructor for the Search Panel 
	 * @param id
	 */
	public Search(	String id,
					FeedbackPanel feedBackPanel,
					WebMarkupContainer searchPanelMarkupContainer,
					PageableListView<ArkUserVO> listView,
					WebMarkupContainer resultListContainer,
					WebMarkupContainer detailsPanelContainer,
					Details detailPanel,
					ContainerForm containerForm) {
		
		super(id);
		this.fbPanel = feedBackPanel;
		this.searchMarkupContainer = searchPanelMarkupContainer;
		this.listView = listView;
		this.listContainer = resultListContainer;
		this.detailsContainer = detailsPanelContainer;
		this.userDetailPanel = detailPanel;
		this.containerForm = containerForm;
	}
	
	
	public void initialisePanel(CompoundPropertyModel<ArkUserVO> arkUserVoModel){
		
		//Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		
		/* Implement a solution to hide the panels if there is no study in context */
		
		SearchUserForm searchUserForm = new SearchUserForm(Constants.SEARCH_USER_FORM, arkUserVoModel){
			
			protected  void onSearch(AjaxRequestTarget target){
				//Refresh the FB panel if there was an old message from previous search result
				target.addComponent(fbPanel);
				
				try {
					
					List<ArkUserVO> userResultList = userService.searchUser(containerForm.getModelObject());
					
					if(userResultList != null && userResultList.size() == 0){
						this.info("A user with the specified criteria does not exist in the system.");	
					}else{
						
						containerForm.getModelObject().setUserList(userResultList);
						listView.removeAll();
						listContainer.setVisible(true);
						target.addComponent(listContainer);
					}
				}
				catch (ArkSystemException e) {
					//log.error("Exception occurred when looking up a user in LDAP" + e.getMessage());
				}
			}
			
			/**
			 * When user has selected New button
			 */
			protected void onNew(AjaxRequestTarget target){
				ArkUserVO arkUserVO = new ArkUserVO();
				arkUserVO.setMode(Constants.MODE_NEW);
				containerForm.setModelObject(arkUserVO);
				processDetail(target, arkUserVO.getMode());
			}
			
		};
		
		add(searchUserForm);
	}
	
	public void processDetail(AjaxRequestTarget target, int mode){
		
		/*Make the Details Panel visible and hide the search and search result panels*/
		userDetailPanel.getUserForm().getUserNameTxtField().setEnabled(true);
		detailsContainer.setVisible(true);
		listContainer.setVisible(false);
		searchMarkupContainer.setVisible(false);
		
		/*Associate the containers with Ajax request target*/
		target.addComponent(detailsContainer);
		target.addComponent(listContainer);
		target.addComponent(searchMarkupContainer);
	}
	
	

//	public void process(String id){
//
//		//Create a new instance of the details panel with empty user object
//		detailsPanel = new Details("userDetailsPanel", new ArkUserVO(), this);
//		//Hide it since we have not looked up a user as yet
//		setDetailsPanelVisible(false);
//		
//		// Uses an entirely new VO for the search so each time the search panel is loaded. The values provided will be refreshed.
//		SearchUserForm searchUserForm = new SearchUserForm(Constants.SEARCH_USER_FORM, new ArkUserVO(), id){
//			
//			/*When user has clicked on the Search Button*/
//			protected  void onSearch(ArkUserVO userVO){
//				//log.info("Look up the user");
//				try {
//					setDetailsPanelVisible(false);//Set the User Details panel to false/hide it
//					List<ArkUserVO> userResultList = userService.searchUser(userVO);
//					if(userResultList != null && userResultList.size() == 0){
//						this.info("A user with the specified criteria does not exist in the system.");	
//					}
//					
//					//Render the Search Results
//					//TODO NN Removing the panel is not efficient, I need to use another technique (Ajax)
//					remove(displayUserListPanel);//Since we already have the panel.We need to partially update the list the panel uses  rather than action it at the panel level
//					displayUserListPanel = new SearchResultList("displayUserListPanel", userResultList,detailsPanel);
//					add(displayUserListPanel);
//					//Provide the list as an argument into the results rendering panel
//				}
//				catch (ArkSystemException e) {
//					//log.error("Exception occured when looking up a user in LDAP" + e.getMessage());
//				}
//			}
//			
//			protected void onNew(ArkUserVO etaUserVO){
//				//Set the Model Object for the existing Form object in DetailsPanel
//				detailsPanel.getUserForm().getGroupPasswordContainer().setVisible(true);
//				detailsPanel.getUserForm().setModelObject(etaUserVO);
//				detailsPanel.setVisible(true);
//				detailsPanel.getUserForm().getUserNameTxtField().setEnabled(true);
//				//Hide the Search and Search Results panels.
//				displayUserListPanel.setVisible(false);
//			}
//			
//		};
//
//		//Add the Form to the Panel. The Form object that will contain the child or UI components that will be part of the search or be affected by the search.
//		add(searchUserForm);
//	
//		searchUserForm.add(detailsPanel);
//		//Just an empty list
//		List<ArkUserVO> userList = new ArrayList<ArkUserVO>();
//		ArkUserVO user = new ArkUserVO();
//		userList.add(user);
//		displayUserListPanel = new SearchResultList("displayUserListPanel", userList,detailsPanel);
//		searchUserForm.add(displayUserListPanel);
//
//		
//	}
}
