package au.org.theark.study.web.component.pedigree;

import java.text.SimpleDateFormat;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
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
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.LinkSubjectPedigree;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.StudyHelper;
import au.org.theark.core.web.component.link.AjaxConfirmLink;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.study.model.vo.RelationshipVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.pedigree.form.ContainerForm;


public class SearchResultListPanel extends Panel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;
	
	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService		iStudyService;
	
	private ArkCrudContainerVO	 arkCrudContainerVO;

	private WebMarkupContainer	 					arkContextMarkup;
	protected WebMarkupContainer 				studyNameMarkup;
	protected WebMarkupContainer 				studyLogoMarkup;
	
	ContainerForm							containerForm;
	
	private PageableListView<RelationshipVo> sitePageableListView;
	
	public SearchResultListPanel(String id, ArkCrudContainerVO crudContainerVO,ContainerForm containerForm, WebMarkupContainer arkContextMarkup, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup) {
		super(id);
		
		this.arkContextMarkup = arkContextMarkup;
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		
		this.containerForm = containerForm;
		
		arkCrudContainerVO = crudContainerVO;

	}

	public PageableListView<RelationshipVo> buildPageableListView(IModel iModel) {
		
		sitePageableListView = new PageableListView<RelationshipVo>("relationshipList", iModel, iArkCommonService.getRowsPerPage()) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<RelationshipVo> item) {

				RelationshipVo relationshipVo = item.getModelObject();

				item.add(buildUidLink(relationshipVo));
				
				if (relationshipVo.getFirstName() != null) {
					item.add(new Label(Constants.PEDIGREE_FIRST_NAME, relationshipVo.getFirstName()));
				}
				else {
					item.add(new Label(Constants.PEDIGREE_FIRST_NAME, ""));
				}
				
				if (relationshipVo.getLastName() != null) {
					item.add(new Label(Constants.PEDIGREE_LAST_NAME, relationshipVo.getLastName()));
				}
				else {
					item.add(new Label(Constants.PEDIGREE_LAST_NAME, ""));
				}
				
				if (relationshipVo.getRelationship() != null) {
					item.add(new Label(Constants.PEDIGREE_RELATIONSHIP, relationshipVo.getRelationship()));
				}
				else {
					item.add(new Label(Constants.PEDIGREE_RELATIONSHIP, ""));
				}
				
				if(relationshipVo.isInbreed()){
					item.add(new Label(Constants.PEDIGREE_INBREED, "*"));
				}
				else{
					item.add(new Label(Constants.PEDIGREE_INBREED, ""));
				}
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);	
				String dob = "";
				if (relationshipVo.getDob() != null) {
					dob = simpleDateFormat.format(relationshipVo.getDob());
					item.add(new Label(Constants.PEDIGREE_DOB	, dob));
				}
				else {
					item.add(new Label(Constants.PEDIGREE_DOB, dob));
				}
				if (relationshipVo.getTwin() != null) {
					item.add(new Label(Constants.PEDIGREE_TWIN, relationshipVo.getTwin()));
				}
				else {
					item.add(new Label(Constants.PEDIGREE_TWIN, ""));
				}
				
				AjaxLink unsetLink= buildUnsetLink(item);
				item.add(unsetLink);
				if(!ArkPermissionHelper.isActionPermitted(Constants.SAVE) || !("Father".equalsIgnoreCase(relationshipVo.getRelationship())
						||
						"Mother".equalsIgnoreCase(relationshipVo.getRelationship()))){
					unsetLink.setVisible(false);
				}
				
				if(!ArkPermissionHelper.isActionPermitted(Constants.SAVE) ||"Father".equalsIgnoreCase(relationshipVo.getRelationship())){
					arkCrudContainerVO.getSearchPanelContainer().get("searchComponentPanel").get("searchForm").get("father").setEnabled(false);
					
				}
				
				if(!ArkPermissionHelper.isActionPermitted(Constants.SAVE) ||"Mother".equalsIgnoreCase(relationshipVo.getRelationship())){
					arkCrudContainerVO.getSearchPanelContainer().get("searchComponentPanel").get("searchForm").get("mother").setEnabled(false);
				}
				
				if("Brother".equalsIgnoreCase(relationshipVo.getRelationship()) || "Sister".equalsIgnoreCase(relationshipVo.getRelationship())){
					arkCrudContainerVO.getSearchPanelContainer().get("searchComponentPanel").get("searchForm").get("twin").setEnabled(true);
				}
				
				
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					private static final long	serialVersionUID	= 1L;
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		return sitePageableListView;
	}
	
	
	@SuppressWarnings( { "serial" })
	private AjaxLink buildUidLink(final RelationshipVo relationshipVo) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.PEDIGREE_INDIVIDUAL_ID) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				
				Study study = iArkCommonService.getStudy(sessionStudyId);
				
				SubjectVO subjectFromBackend = new SubjectVO();
				subjectFromBackend.getLinkSubjectStudy().setStudy(study);
				subjectFromBackend.getLinkSubjectStudy().setSubjectUID(relationshipVo.getIndividualId());
				Collection<SubjectVO> subjects = iArkCommonService.getSubject(subjectFromBackend);
				subjectFromBackend = subjects.iterator().next();
				
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID, subjectFromBackend.getLinkSubjectStudy().getPerson().getId());
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_TYPE, au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT);

				// Set SubjectUID into context
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.SUBJECTUID, subjectFromBackend.getLinkSubjectStudy().getSubjectUID());
				ContextHelper contextHelper = new ContextHelper();
				contextHelper.setStudyContextLabel(target, subjectFromBackend.getLinkSubjectStudy().getStudy().getName(), arkContextMarkup);
				contextHelper.setSubjectContextLabel(target, subjectFromBackend.getLinkSubjectStudy().getSubjectUID(), arkContextMarkup);
				
				// Set Study Logo
				StudyHelper studyHelper = new StudyHelper();
				studyHelper.setStudyLogo(subjectFromBackend.getLinkSubjectStudy().getStudy(), target, studyNameMarkup, studyLogoMarkup, iArkCommonService);
				
				//Refresh relationship list
				arkCrudContainerVO.getSearchPanelContainer().get("searchComponentPanel").get("searchForm").get("father").setEnabled(true);
				arkCrudContainerVO.getSearchPanelContainer().get("searchComponentPanel").get("searchForm").get("mother").setEnabled(true);
				arkCrudContainerVO.getSearchPanelContainer().get("searchComponentPanel").get("searchForm").get("twin").setEnabled(false);
				
				//Enable OR Disable family data button
				String familyId = iStudyService.getSubjectFamilyId(sessionStudyId, relationshipVo.getIndividualId());
				if(familyId == null){
					arkCrudContainerVO.getSearchPanelContainer().get("searchComponentPanel").get("searchForm").get(au.org.theark.core.Constants.FAMILY).setEnabled(false);
				}else{
					arkCrudContainerVO.getSearchPanelContainer().get("searchComponentPanel").get("searchForm").get(au.org.theark.core.Constants.FAMILY).setEnabled(true);
				}
				
				sitePageableListView.removeAll();
				containerForm.getModelObject().setRelationshipList(iStudyService.generateSubjectPedigreeRelativeList(subjectFromBackend.getLinkSubjectStudy().getSubjectUID(),sessionStudyId));
				target.add(arkCrudContainerVO.getSearchResultPanelContainer());
				target.add(arkCrudContainerVO.getSearchPanelContainer());
				
			}
		};
		Label nameLinkLabel = new Label("uidLbl", relationshipVo.getIndividualId());
		link.add(nameLinkLabel);
		return link;
	}
	

	@SuppressWarnings( { "serial" })
	private AjaxLink buildUnsetLink(final ListItem<RelationshipVo> item) {	
		final RelationshipVo relationshipVo = item.getModelObject();
		
		AjaxConfirmLink<RelationshipVo> link = new AjaxConfirmLink<RelationshipVo>(Constants.PEDIGREE_RELATIONSHIP_DELETE, new StringResourceModel("pedigree.relationship.warning", this, item.getModel()),item.getModel()) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				LinkSubjectPedigree relationship=new LinkSubjectPedigree();
				relationship.setId(relationshipVo.getId());
				
				iStudyService.deleteRelationship(relationship);
				
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				
				Study study = iArkCommonService.getStudy(sessionStudyId);
				
				SubjectVO subjectFromBackend = new SubjectVO();
				subjectFromBackend.getLinkSubjectStudy().setStudy(study);
				subjectFromBackend.getLinkSubjectStudy().setSubjectUID(relationshipVo.getIndividualId());
				Collection<SubjectVO> subjects = iArkCommonService.getSubject(subjectFromBackend);
				subjectFromBackend = subjects.iterator().next();
				
				arkCrudContainerVO.getSearchPanelContainer().get("searchComponentPanel").get("searchForm").get("father").setEnabled(true);
				arkCrudContainerVO.getSearchPanelContainer().get("searchComponentPanel").get("searchForm").get("mother").setEnabled(true);
				sitePageableListView.removeAll();
				containerForm.getModelObject().setRelationshipList(iStudyService.generateSubjectPedigreeRelativeList(subjectFromBackend.getLinkSubjectStudy().getSubjectUID(),sessionStudyId));
				target.add(arkCrudContainerVO.getSearchResultPanelContainer());
				target.add(arkCrudContainerVO.getSearchPanelContainer());				
			}			
		};
		
		return link;
	}

}
