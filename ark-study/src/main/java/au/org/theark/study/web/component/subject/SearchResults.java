/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.subject;

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

import au.org.theark.core.vo.SubjectVO;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.subject.form.ContainerForm;

/**
 * @author nivedann
 *
 */
public class SearchResults extends Panel{
	
	private WebMarkupContainer detailsPanelContainer;
	private WebMarkupContainer searchPanelContainer;
	private WebMarkupContainer searchResultContainer;
	
	private ContainerForm subjectContainerForm;
	private Details detailsPanel;
	
	/**
	 * @param id
	 */
	public SearchResults(	String id,
							WebMarkupContainer  detailPanelContainer, 
							WebMarkupContainer searchPanelContainer,
							ContainerForm containerForm,
							WebMarkupContainer searchResultContainer,
							Details detailPanel) {
		
		super(id);
		this.detailsPanelContainer = detailPanelContainer;
		this.searchPanelContainer = searchPanelContainer;
		this.searchResultContainer = searchResultContainer;
		this.detailsPanel = detailPanel;
		this.subjectContainerForm = containerForm;
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
				
				item.add(new Label(Constants.PERSON_VITAL_STATUS_NAME,subject.getPerson().getVitalStatus().getStatusName()));
				
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
		
		AjaxLink link = new AjaxLink(Constants.PERSON_PERSON_KEY) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				
				SubjectVO subjectVO = subjectContainerForm.getModelObject();
				subjectVO.setStudy(subject.getStudy());
				subjectVO.setPerson(subject.getPerson());//The selected person on the containerForm's Model
				subjectVO.setSubjectStatus(subject.getSubjectStatus());
				subjectVO.setLinkSubjectStudyId(subject.getLinkSubjectStudyId());
				
				detailsPanelContainer.setVisible(true);
				searchResultContainer.setVisible(false);
				searchPanelContainer.setVisible(false);
				//TODO detailPanel.getDetailsForm().getComponentIdTxtFld().setEnabled(false);
				target.addComponent(searchResultContainer);
				target.addComponent(detailsPanelContainer);
				target.addComponent(searchPanelContainer);
			}
		};
		
		Label nameLinkLabel = new Label(Constants.SUBJECT_KEY_LBL, subject.getPerson().getPersonKey().toString());
		link.add(nameLinkLabel);
		return link;
	}
	
	

}
