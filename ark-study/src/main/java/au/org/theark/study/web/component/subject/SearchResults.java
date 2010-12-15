/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.subject;

import java.util.Collection;

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

import au.org.theark.core.service.IArkCommonService;
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
	//private WebMarkupContainer phoneListWebMarkupContainer;
	private ContainerForm subjectContainerForm;
	private Details detailsPanel;
	

	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
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
				
				SubjectVO subjectFromBackend = new SubjectVO();
				Collection<SubjectVO> subjects = iArkCommonService.getSubject(subject);
				for (SubjectVO subjectVO2 : subjects) {
					subjectFromBackend = subjectVO2;
					break;
				}
				
				subjectContainerForm.setModelObject(subject);
				//Refresh the details of the SubjectVO attached to the Model
				SubjectVO subjectVOInModel = subjectContainerForm.getModelObject();
				subjectVOInModel.setPerson(subject.getPerson());
				subjectVOInModel.setSubjectUID(subject.getSubjectUID());
				subjectVOInModel.setLinkSubjectStudyId(subject.getLinkSubjectStudyId());
				subjectVOInModel.setSubjectStatus(subject.getSubjectStatus());
				//subjectVOInModel.setPhoneList(subject.getPhoneList());
				
//				PhoneList phoneListPanel  = (PhoneList) phoneListWebMarkupContainer.get("phoneListPanel");
//				PageableListView<Phone> phonePageableListView = (PageableListView<Phone>)phoneListPanel.get("phoneNumberList");
//				phonePageableListView.removeAll();
//				phoneListWebMarkupContainer.setVisible(true);
//				target.addComponent(phoneListWebMarkupContainer);
				
				detailsPanelContainer.setVisible(true);
				searchResultContainer.setVisible(false);
				searchPanelContainer.setVisible(false);
				
				target.addComponent(searchPanelContainer);
				target.addComponent(searchResultContainer);
				target.addComponent(detailsPanelContainer);
				
			}
		};
		Label nameLinkLabel = new Label(Constants.SUBJECT_KEY_LBL, subject.getSubjectUID());
		link.add(nameLinkLabel);
		return link;
	}
	
	

}
