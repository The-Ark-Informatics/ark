package au.org.theark.study.web.component.consentFile;

import java.sql.SQLException;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ConsentFile;
import au.org.theark.core.web.component.AjaxDeleteButton;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.component.consentFile.form.ContainerForm;

@SuppressWarnings({ "serial", "unchecked", "unused", "rawtypes" })
public class SearchResultListPanel extends Panel {
	@SpringBean(name = au.org.theark.study.web.Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	private transient Logger log = LoggerFactory.getLogger(SearchResultListPanel.class);

	private WebMarkupContainer detailPanelContainer;
	private WebMarkupContainer detailPanelFormContainer;
	private WebMarkupContainer searchPanelContainer;
	private WebMarkupContainer searchResultContainer;
	private WebMarkupContainer viewButtonContainer;
	private WebMarkupContainer editButtonContainer;
	private ContainerForm containerForm;
	
	/**
	 * @param id
	 */
	public SearchResultListPanel(String id, 
			WebMarkupContainer  detailPanelContainer,
			WebMarkupContainer  detailPanelFormContainer, 
			WebMarkupContainer searchPanelContainer,
			WebMarkupContainer searchResultContainer,
			WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer,
			ContainerForm containerForm) {
		
		super(id);
		// TODO Auto-generated constructor stub
	 	this.detailPanelContainer = detailPanelContainer;
		this.searchPanelContainer = searchPanelContainer;
		this.searchResultContainer = searchResultContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
		this.containerForm = containerForm;
	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of Upload
	 */
	public PageableListView<ConsentFile> buildPageableListView(IModel iModel) 
	{
		PageableListView<ConsentFile> sitePageableListView = new PageableListView<ConsentFile>(
				Constants.RESULT_LIST, iModel, Constants.ROWS_PER_PAGE) {
			@Override
			protected void populateItem(final ListItem<ConsentFile> item) 
			{
				ConsentFile consentFile = item.getModelObject();
				// The ID
				if (consentFile.getId() != null) 
				{
					// Add the id component here
					item.add(new Label(
							au.org.theark.study.web.Constants.CONSENT_FILE_ID,
							consentFile.getId().toString()));
				} 
				else 
				{
					item.add(new Label(
							au.org.theark.study.web.Constants.CONSENT_FILE_ID,
							""));
				}

				// / The filename
				if (consentFile.getFilename() != null) 
				{
					// Add the id component here
					item.add(new Label(
							au.org.theark.study.web.Constants.CONSENT_FILE_FILENAME,
							consentFile.getFilename()));
				} 
				else 
				{
					item.add(new Label(
							au.org.theark.study.web.Constants.CONSENT_FILE_FILENAME,
							""));
				}


				// TODO when displaying text escape any special characters
				// UserId
				if (consentFile.getUserId() != null) 
				{
					item.add(new Label(
							au.org.theark.study.web.Constants.CONSENT_FILE_USER_ID,
							consentFile.getUserId()));// the ID here must match the
					// ones in
					// mark-up
				} 
				else 
				{
					item.add(new Label(
							au.org.theark.study.web.Constants.CONSENT_FILE_USER_ID,
							""));// the ID here must match the ones in mark-up
				}

				// Download file link button
				item.add(buildDownloadButton(consentFile));

				// Delete the upload file
				item.add(buildDeleteButton(consentFile));

				// For the alternative stripes
				item.add(new AttributeModifier("class", true,
						new AbstractReadOnlyModel() {
							@Override
							public String getObject() {
								return (item.getIndex() % 2 == 1) ? "even"
										: "odd";
							}
						}));
			}
		};
		return sitePageableListView;
	}

	private Link buildDownloadLink(final ConsentFile consentFile) {
		Link link = new Link(
				au.org.theark.study.web.Constants.DOWNLOAD_FILE) 
		{
			@Override
			public void onClick() {
				// Attempt to download the Blob as an array of bytes
				byte[] data = null;
				try {
					data = consentFile.getPayload().getBytes(1,
							(int) consentFile.getPayload().length());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getRequestCycle().setRequestTarget(
						new au.org.theark.core.util.ByteDataRequestTarget(
								"text/plain", data, consentFile.getFilename()));

			};
		};

		// Add the label for the link
		// TODO when displaying text escape any special characters
		Label nameLinkLabel = new Label("downloadFileLbl", "Download File");
		link.add(nameLinkLabel);
		return link;
	}

	private AjaxButton buildDownloadButton(final ConsentFile consentFile) {
		AjaxButton ajaxButton = new AjaxButton(
				au.org.theark.study.web.Constants.DOWNLOAD_FILE,
				new StringResourceModel("downloadKey", this, null)) 
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Attempt to download the Blob as an array of bytes
				byte[] data = null;
				try {
					data = consentFile.getPayload().getBytes(1,
							(int) consentFile.getPayload().length());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getRequestCycle().setRequestTarget(
						new au.org.theark.core.util.ByteDataRequestTarget(
								"text/plain", data, consentFile.getFilename()));
			};
		};

		ajaxButton.setVisible(true);
		ajaxButton.setDefaultFormProcessing(false);

		if (consentFile.getPayload() == null)
			ajaxButton.setVisible(false);

		return ajaxButton;
	}

	private AjaxDeleteButton buildDeleteButton(final ConsentFile consentFile)
	{
		DeleteButton ajaxButton = new DeleteButton(consentFile, SearchResultListPanel.this)
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Attempt to delete upload
				if (consentFile.getId() != null)
				{
					try {
						studyService.delete(consentFile);
					} catch (ArkSystemException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (EntityNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				containerForm.info("Consent file " + consentFile.getFilename() + " was deleted successfully.");
				
				// Update the result panel
				target.addComponent(searchResultContainer);
				target.addComponent(containerForm);
			}
		};

		// TODO: Check permissions for delete
		ajaxButton.setVisible(true);
		ajaxButton.setDefaultFormProcessing(false);

		return ajaxButton;
	}
}