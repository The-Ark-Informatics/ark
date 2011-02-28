package au.org.theark.study.web.component.subject;

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
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.subject.form.ContainerForm;

/**
 * @author nivedann
 *
 */
public class SearchResults extends Panel{
	
	
	private WebMarkupContainer detailPanelContainer;
	private WebMarkupContainer detailPanelFormContainer;
	private WebMarkupContainer searchPanelContainer;
	private WebMarkupContainer searchResultContainer;
	private WebMarkupContainer viewButtonContainer;
	private WebMarkupContainer editButtonContainer;
	private WebMarkupContainer arkContextMarkup;
	private ContainerForm subjectContainerForm;
	

	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	

	public SearchResults(String id, 
							WebMarkupContainer  detailPanelContainer,
							WebMarkupContainer  detailPanelFormContainer, 
							WebMarkupContainer searchPanelContainer,
							WebMarkupContainer searchResultContainer,
							WebMarkupContainer viewButtonContainer,
							WebMarkupContainer editButtonContainer,
							WebMarkupContainer arkContextMarkup,
							ContainerForm containerForm){
		
		super(id);
		
		this.detailPanelContainer = detailPanelContainer;
		this.subjectContainerForm = containerForm;
		this.searchPanelContainer = searchPanelContainer;
		this.searchResultContainer = searchResultContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
		this.arkContextMarkup = arkContextMarkup;
	}
	  
	
	public PageableListView<SubjectVO> buildListView(IModel iModel){
		
		PageableListView<SubjectVO> listView = new PageableListView<SubjectVO>(Constants.SUBJECT_LIST, iModel, 10){

			@Override
			protected void populateItem(final ListItem<SubjectVO> item) {
				SubjectVO subjectVO = item.getModelObject();
				LinkSubjectStudy subject = item.getModelObject().getSubjectStudy();
				item.add(buildLink(item.getModelObject()));
				
				StringBuffer sb = new StringBuffer();
				sb.append(subject.getPerson().getFirstName());
				sb.append(" ");
				if(subject.getPerson().getMiddleName() != null){
					sb.append(subject.getPerson().getMiddleName());
				}
				sb.append(" ");
				if(subject.getPerson().getLastName() != null){
					sb.append(subject.getPerson().getLastName());
				}
				item.getModelObject().setSubjectFullName(sb.toString());
				item.add(new Label(Constants.SUBJECT_FULL_NAME, item.getModelObject().getSubjectFullName()));
			
				if(subject != null && subject.getPerson() != null && subject.getPerson().getPreferredName() != null){
					item.add(new Label("subjectStudy.person.preferredName", subject.getPerson().getPreferredName()));
				}else{
					item.add(new Label("subjectStudy.person.preferredName",""));
				}
				
				item.add(new Label("subjectStudy.person.genderType.name",subject.getPerson().getGenderType().getName()));
				
				item.add(new Label("person.vitalStatus.statusName",subject.getPerson().getVitalStatus().getName()));
				
				item.add(new Label("subjectStudy.subjectStatus.name",subject.getSubjectStatus().getName()));
				
				
				item.add(new AttributeModifier(Constants.CLASS, true, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));
			}
		};
		return listView;
	}
	
	private AjaxLink buildLink(final  SubjectVO subject){
		AjaxLink link = new AjaxLink(Constants.SUBJECT_UID) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				
				
				Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				subject.getSubjectStudy().setStudy(iArkCommonService.getStudy(sessionStudyId));
				
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID, subject.getSubjectStudy().getPerson().getId());
				//We specify the type of person here as Subject
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_TYPE, au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_CONTACT);
				SubjectVO subjectFromBackend = new SubjectVO();
				Collection<SubjectVO> subjects = iArkCommonService.getSubject(subject);
				for (SubjectVO subjectVO2 : subjects) {
					subjectFromBackend = subjectVO2;
					break;
				}
				
				subjectContainerForm.setModelObject(subjectFromBackend);
				ContextHelper contextHelper = new ContextHelper();
				contextHelper.resetContextLabel(target, arkContextMarkup);
				contextHelper.setStudyContextLabel(target, subjectFromBackend.getSubjectStudy().getStudy().getName(), arkContextMarkup);
				contextHelper.setSubjectContextLabel(target, subjectFromBackend.getSubjectStudy().getSubjectUID(), arkContextMarkup);
				
				detailPanelContainer.setVisible(true);
				viewButtonContainer.setVisible(true);
				viewButtonContainer.setEnabled(true);
				detailPanelFormContainer.setEnabled(false);
				searchResultContainer.setVisible(false);
				searchPanelContainer.setVisible(false);
				editButtonContainer.setVisible(false);
				
				target.addComponent(searchResultContainer);
				target.addComponent(detailPanelContainer);
				target.addComponent(detailPanelFormContainer);
				target.addComponent(searchPanelContainer);
				target.addComponent(viewButtonContainer);
				target.addComponent(editButtonContainer);
				
			}
		};
		Label nameLinkLabel = new Label(Constants.SUBJECT_KEY_LBL, subject.getSubjectStudy().getSubjectUID());
		link.add(nameLinkLabel);
		return link;
	}
}
