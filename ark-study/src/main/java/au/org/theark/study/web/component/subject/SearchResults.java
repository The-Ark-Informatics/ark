/**
 * 
 * This is a new file
 *
 *
 */
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
	  
	
	public PageableListView<SubjectVO> buildPageableListView(IModel iModel){
		
		PageableListView<SubjectVO> listView = new PageableListView<SubjectVO>(Constants.SUBJECT_LIST, iModel, 10){

			@Override
			protected void populateItem(final ListItem<SubjectVO> item) {
				// TODO Auto-generated method stub
				SubjectVO subject = item.getModelObject();
				item.add(buildLink(subject));
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
				subject.setSubjectFullName(sb.toString());
				item.add(new Label(Constants.SUBJECT_FULL_NAME, subject.getSubjectFullName()));
			
				if(subject.getPerson().getPreferredName() != null){
					item.add(new Label(Constants.PERSON_PREFERRED_NAME,subject.getPerson().getPreferredName()));
				}else{
					item.add(new Label(Constants.PERSON_PREFERRED_NAME,""));
				}
				
				item.add(new Label(Constants.PERSON_GENDER_TYPE_NAME,subject.getPerson().getGenderType().getName()));
				
				item.add(new Label(Constants.PERSON_VITAL_STATUS_NAME,subject.getPerson().getVitalStatus().getName()));
				
				item.add(new Label(Constants.SUBJECT_STATUS_NAME,subject.getSubjectStatus().getName()));
				
				
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
	
	private AjaxLink buildLink(final SubjectVO subject){
		
		//AjaxLink link = new AjaxLink(Constants.PERSON_PERSON_KEY) {
		AjaxLink link = new AjaxLink(Constants.SUBJECT_UID) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID, subject.getPerson().getId());
				//We specify the type of person here as Subject
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_TYPE, au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_CONTACT);
				
				SubjectVO subjectFromBackend = new SubjectVO();
				Collection<SubjectVO> subjects = iArkCommonService.getSubject(subject);
				for (SubjectVO subjectVO2 : subjects) {
					subjectFromBackend = subjectVO2;
					break;
				}
				
				subjectContainerForm.setModelObject(subjectFromBackend);
				//Refresh the details of the SubjectVO attached to the Model
				SubjectVO subjectVOInModel = subjectContainerForm.getModelObject();
				subjectVOInModel.setPerson(subjectFromBackend.getPerson());
				subjectVOInModel.setSubjectUID(subjectFromBackend.getSubjectUID());
				subjectVOInModel.setLinkSubjectStudyId(subjectFromBackend.getLinkSubjectStudyId());
				subjectVOInModel.setSubjectStatus(subjectFromBackend.getSubjectStatus());
				
				ContextHelper contextHelper = new ContextHelper();
				contextHelper.resetContextLabel(target, arkContextMarkup);
				contextHelper.setStudyContextLabel(target, subjectFromBackend.getStudy().getName(), arkContextMarkup);
				contextHelper.setSubjectContextLabel(target, subjectVOInModel.getSubjectUID(), arkContextMarkup);
				
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
		Label nameLinkLabel = new Label(Constants.SUBJECT_KEY_LBL, subject.getSubjectUID());
		link.add(nameLinkLabel);
		return link;
	}
	
	

}
