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

import au.org.theark.study.model.vo.SubjectVO;
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
		
		PageableListView<SubjectVO> listView = new PageableListView<SubjectVO>("subjectList", iModel, 10){

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
				item.add(new Label("subjectFullName", subject.getSubjectFullName()));
			
				if(subject.getPerson().getPreferredName() != null){
					item.add(new Label("person.preferredName",subject.getPerson().getPreferredName()));
				}else{
					item.add(new Label("person.preferredName",""));
				}
				
				item.add(new Label("person.genderType.name",subject.getPerson().getGenderType().getName()));
				
				item.add(new Label("person.vitalStatus.statusName",subject.getPerson().getVitalStatus().getStatusName()));
				
				item.add(new Label("subjectStatus.name",subject.getSubjectStatus().getName()));
				
				
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		return listView;
	}
	
	private AjaxLink buildLink(final SubjectVO subject){
		
		AjaxLink link = new AjaxLink("person.personKey") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				SubjectVO subjectVO = subjectContainerForm.getModelObject();
				subjectVO.setPerson(subject.getPerson());//The selected person on the containerForm's Model
				subjectVO.setSubjectStatus(subject.getSubjectStatus());
				detailsPanelContainer.setVisible(true);
				searchResultContainer.setVisible(false);
				searchPanelContainer.setVisible(false);
				//detailPanel.getDetailsForm().getComponentIdTxtFld().setEnabled(false);
				target.addComponent(searchResultContainer);
				target.addComponent(detailsPanelContainer);
				target.addComponent(searchPanelContainer);
			}
		};
		
		Label nameLinkLabel = new Label("subjectKeyLbl", subject.getPerson().getPersonKey().toString());
		link.add(nameLinkLabel);
		return link;
	}
	
	

}
