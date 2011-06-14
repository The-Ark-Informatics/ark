package au.org.theark.study.web.component.manageuser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkUsecase;
import au.org.theark.core.model.study.entity.ArkUserRole;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ArkModuleVO;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.manageuser.form.ContainerForm;

public class SearchResultListPanel extends Panel{

	private ArkCrudContainerVO arkCrudContainerVO;
	private ContainerForm containerForm;
	
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	@SpringBean( name = "userService")
	private IUserService userService;
	
	/**
	 * Constructor
	 * @param id
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public SearchResultListPanel(String id, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
	}
	
	public PageableListView<ArkUserVO> buildPageableListView(IModel iModel,  final WebMarkupContainer searchResultsContainer){
		//This has to be populated earlier

		PageableListView<ArkUserVO>  pageableListView = new PageableListView<ArkUserVO>("userList",  iModel, au.org.theark.core.Constants.ROWS_PER_PAGE)
		{
			@Override
			protected void populateItem(final ListItem<ArkUserVO> item) {
				
				ArkUserVO arkUserVO =  item.getModelObject();
				item.add(buildLink(arkUserVO, searchResultsContainer));
				item.add(new Label("lastName", arkUserVO.getLastName()));//the ID here must match the ones in mark-up
				item.add(new Label("firstName", arkUserVO.getFirstName()));
				
			}
		};
		
		return pageableListView;
	}
	
	private AjaxLink buildLink(final ArkUserVO arkUserVo, final WebMarkupContainer searchResultsContainer){

		AjaxLink link = new AjaxLink("userName") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					try {
						
						//Fetch the user and related details from backend
						Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
						Study study = iArkCommonService.getStudy(sessionStudyId);
						ArkUserVO arkUserVOFromBackend = userService.lookupArkUser(arkUserVo.getUserName(),study);
						
						containerForm.getModelObject().setArkUserRoleList(arkUserVOFromBackend.getArkUserRoleList());
						//prePopulateArkUserRoleList(arkUserVOFromBackend);
						//containerForm.getModelObject().setArkUserRoleList(arkUserVOFromBackend.getArkUserRoleList());
						containerForm.setModelObject(arkUserVOFromBackend);
						
						//Render the UI
						arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
						arkCrudContainerVO.getSearchPanelContainer().setVisible(false);
						arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
						arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(false);
						arkCrudContainerVO.getViewButtonContainer().setVisible(true);//saveBtn
						arkCrudContainerVO.getViewButtonContainer().setEnabled(true);//saveBtn
						arkCrudContainerVO.getEditButtonContainer().setVisible(false);
						
						
						target.addComponent(arkCrudContainerVO.getSearchPanelContainer());
						target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
						target.addComponent(arkCrudContainerVO.getSearchResultPanelContainer());
						target.addComponent(arkCrudContainerVO.getViewButtonContainer());
						target.addComponent(arkCrudContainerVO.getEditButtonContainer());
						
						target.addComponent(arkCrudContainerVO.getDetailPanelFormContainer());
						target.addComponent(containerForm);
					} catch (ArkSystemException e) {
						// TODO:NN Auto-generated catch block
						e.printStackTrace();
					} catch (EntityNotFoundException e) {
						// TODO:NN Auto-generated catch block
						e.printStackTrace();
					}
					
				}
		};
		//Add the label for the link
		Label userNameLinkLabel = new Label("userNameLink", arkUserVo.getUserName());
		link.add(userNameLinkLabel);
		return link;
	}
	
	private void prePopulateArkUserRoleList(ArkUserVO arkUserVOFromBackend){
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		//Get a List of ArkModules and associated ArkRoles linked to the study 
		Collection<ArkModuleVO> listArkModuleVO = iArkCommonService.getArkModulesLinkedToStudy(study);
		
		//Iterate over the List of Modules and Roles 
		for (ArkModuleVO arkModuleVO : listArkModuleVO) {
			//Check if the ArkUser has not been linked to the module/role if so then add that Module into the list
			if(!arkUserVOFromBackend.getArkUserRoleList().contains(arkModuleVO.getArkModule())){
				
				ArkUserRole userRole = new ArkUserRole();
				userRole.setStudy(study);
				userRole.setArkModule(arkModuleVO.getArkModule());
				arkUserVOFromBackend.getArkUserRoleList().add(userRole);
			}
		}
	}
}
