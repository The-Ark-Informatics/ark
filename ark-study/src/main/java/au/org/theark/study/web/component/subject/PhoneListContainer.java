/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.subject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.subject.form.ContainerForm;
import au.org.theark.study.web.component.subject.form.DetailsForm;
import au.org.theark.study.web.component.subject.form.PhoneContainerForm;

/**
 * The container class for Subject related List Items. The lists like
 * Phone/Address or Email will be listed using this container.
 * 
 * @author nivedann
 * 
 */
public class PhoneListContainer extends Panel {

	private PhoneDetail phoneDetailPanel;
	private PhoneList phoneListPanel;

	private WebMarkupContainer phoneListPanelContainer;
	private WebMarkupContainer phoneDetailPanelContainer;

	private IModel<Object> iModel;
	private PageableListView<Phone> pageableListView;
	private PhoneContainerForm phoneContainerForm;
	private DetailsForm subjectForm;
	private AjaxButton addPhoneButton;
	private FeedbackPanel feedBackPanel;

	private void onAddPhone(SubjectVO subjectVO, AjaxRequestTarget target) {
		System.out.println("onAddPhone Invoked");
		phoneDetailPanelContainer.setVisible(true);
		target.addComponent(phoneDetailPanelContainer);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param containerForm
	 * @param subjectDetailForm
	 */
	public PhoneListContainer(String id,
								ContainerForm containerForm,
								FeedbackPanel feedbackPanel, 
								WebMarkupContainer phoneListPanelContainer, 
								WebMarkupContainer phoneDetailPanelContainer) {
		super(id);
		this.feedBackPanel = feedbackPanel;
		this.phoneListPanelContainer = phoneListPanelContainer;
		this.phoneDetailPanelContainer = phoneDetailPanelContainer;
		this.pageableListView = pageableListView;
		
		/* The container Form for Managing Phone Numbers */
		//initialiseMarkupContainers();
		phoneContainerForm = new PhoneContainerForm("phoneContainerForm",containerForm.getModel());

		addPhoneButton = new AjaxButton(Constants.ADD_PHONE,new StringResourceModel("addPhoneKey", this, null)) {

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {

				phoneContainerForm.getModelObject().setPhone(new Phone());
				onAddPhone(phoneContainerForm.getModelObject(), target);
				
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				// processErrors(target);
			}
		};

		phoneListPanelContainer.add(addPhoneButton.setDefaultFormProcessing(false));

		phoneContainerForm.add(initialisePhoneList());
		phoneContainerForm.add(initialiseDetailPanel(phoneListPanelContainer, feedBackPanel));
		add(phoneContainerForm);
	}

//	private void initialiseMarkupContainers() {
//
//		phoneDetailPanelContainer = new WebMarkupContainer("phoneDetailMarkupContainer");
//		phoneDetailPanelContainer.setOutputMarkupPlaceholderTag(true);
//		phoneDetailPanelContainer.setVisible(false);
//
//		phoneListPanelContainer = new WebMarkupContainer("phoneListMarkupContainer");
//		phoneListPanelContainer.setOutputMarkupPlaceholderTag(true);
//		phoneListPanelContainer.setVisible(true);
//
//	}

	private WebMarkupContainer initialiseDetailPanel(WebMarkupContainer phoneListPanelContainer, FeedbackPanel feedbackPanel) {
		phoneDetailPanel = new PhoneDetail("phoneDetailPanel",phoneContainerForm, pageableListView, phoneListPanelContainer,phoneDetailPanelContainer, feedBackPanel);
		phoneDetailPanel.initialisePanel();
		phoneDetailPanelContainer.add(phoneDetailPanel);
		return phoneDetailPanelContainer;
	}

	public WebMarkupContainer initialisePhoneList() {

		// Set a default list here
		phoneListPanel = new PhoneList("phoneListPanel", phoneContainerForm,	phoneListPanelContainer, phoneDetailPanelContainer);
		
		iModel = new LoadableDetachableModel<Object>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				return phoneContainerForm.getModelObject().getPhoneList();
			}
		};
		pageableListView = phoneListPanel.buildPageableListView(iModel);
		pageableListView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("phoneNavigator",	pageableListView);
		phoneListPanel.add(pageNavigator);
		phoneListPanel.add(pageableListView);
		phoneListPanelContainer.add(phoneListPanel);
		
		return phoneListPanelContainer;
	}

}
