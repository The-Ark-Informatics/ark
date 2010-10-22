/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.subject;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import au.org.theark.study.model.entity.Phone;
import au.org.theark.study.web.component.subject.form.ContainerForm;
import au.org.theark.study.web.component.subject.form.SubjectDetailForm;

/**
 * The container class for Subject related List Items.
 * The lists like Phone/Address or Email will be listed using this container.
 * @author nivedann
 *
 */
public class PhoneListContainer extends Panel{

	private PhoneDetail phoneDetailPanel;
	private PhoneList phoneListPanel;
	
	private WebMarkupContainer phoneListPanelContainer;
	private WebMarkupContainer phoneDetailPanelContainer;
	
	private IModel<Object> iModel;
	private PageableListView<Phone> pageableListView;
	private ContainerForm subjectContainerForm;//This maybe the subjectDetailForm to which we want this to be added not the top level containerForm
	
	private SubjectDetailForm subjectForm;
	private FeedbackPanel feedBack;
	
	
	public PhoneListContainer(String id, ContainerForm containerForm,SubjectDetailForm subjectDetailForm, FeedbackPanel feedBackPanel){
		super(id);
		subjectContainerForm = containerForm;
		feedBack = feedBackPanel;
		subjectForm = subjectDetailForm;
		
		initialiseMarkupContainers();
		
		subjectForm.add(initialiseDetailPanel());
		
		subjectForm.add(initialisePhoneList());
		
	}
	
	private void initialiseMarkupContainers(){
		
		phoneDetailPanelContainer = new WebMarkupContainer("phoneDetailContainer");
		phoneDetailPanelContainer.setOutputMarkupPlaceholderTag(true);
		phoneDetailPanelContainer.setVisible(false);

		phoneListPanelContainer = new WebMarkupContainer("phoneListContainer");
		phoneListPanelContainer.setOutputMarkupPlaceholderTag(true);
		phoneListPanelContainer.setVisible(true);
	
	}
	
	private WebMarkupContainer initialiseDetailPanel(){
		phoneDetailPanel = new PhoneDetail("phoneDetail", phoneListPanelContainer, subjectContainerForm, feedBack);
		phoneDetailPanel.initialisePanel();
		phoneListPanelContainer.add(phoneDetailPanel);
		return phoneDetailPanelContainer;
	}
	
	private WebMarkupContainer initialisePhoneList(){
		
		phoneListPanel = new PhoneList("phoneList", subjectContainerForm, phoneListPanelContainer, phoneDetailPanelContainer);
		
		iModel = new LoadableDetachableModel<Object>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				return subjectContainerForm.getModelObject().getPhoneList(); 
			}
		};
		pageableListView =phoneListPanel.buildPageableListView(iModel);
		pageableListView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", pageableListView);
		phoneListPanel.add(pageNavigator);
		phoneListPanel.add(pageableListView);
		
		phoneListPanelContainer.add(phoneListPanel);
		return phoneListPanelContainer;
	}
	
}
