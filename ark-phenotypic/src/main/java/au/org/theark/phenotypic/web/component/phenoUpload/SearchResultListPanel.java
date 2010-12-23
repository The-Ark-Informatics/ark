package au.org.theark.phenotypic.web.component.phenoUpload;

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

import au.org.theark.core.Constants;
import au.org.theark.phenotypic.model.entity.Upload;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.web.component.phenoUpload.form.ContainerForm;

@SuppressWarnings( { "serial", "unchecked" })
public class SearchResultListPanel extends Panel
{

	private WebMarkupContainer	detailsPanelContainer;
	private WebMarkupContainer	searchPanelContainer;
	private WebMarkupContainer	searchResultContainer;
	private ContainerForm		containerForm;
	private DetailPanel					detailPanel;
	private WebMarkupContainer detailPanelFormContainer;
	private WebMarkupContainer viewButtonContainer;
	private WebMarkupContainer editButtonContainer;

	public SearchResultListPanel(String id, WebMarkupContainer detailPanelContainer, WebMarkupContainer searchPanelContainer, ContainerForm studyCompContainerForm, WebMarkupContainer searchResultContainer,
			DetailPanel detail,
			WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer,
			WebMarkupContainer detailPanelFormContainer)
	{
		super(id);
		this.detailsPanelContainer = detailPanelContainer;
		this.containerForm = studyCompContainerForm;
		this.searchPanelContainer = searchPanelContainer;
		this.searchResultContainer = searchResultContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer; 
		this.setDetailPanel(detail);
	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of Upload
	 */
	public PageableListView<Upload> buildPageableListView(IModel iModel)
	{
		PageableListView<Upload> sitePageableListView = new PageableListView<Upload>(Constants.RESULT_LIST, iModel, Constants.ROWS_PER_PAGE)
		{
			@Override
			protected void populateItem(final ListItem<Upload> item)
			{	
				 Upload upload = item.getModelObject();

				// The ID
				if (upload.getId() != null)
				{
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_ID, upload.getId().toString()));
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_ID, ""));
				}

				// Name Link
				item.add(buildLink(upload));

				// TODO when displaying text escape any special characters
				// File Format
				if (upload.getFileFormat() != null)
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILE_FORMAT, upload.getFileFormat().getName()));// the name here
					// must match the
					// ones in mark-up
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILE_FORMAT, ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// UserId
				if (upload.getUserId() != null)
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_USER_ID, upload.getUserId()));// the ID here must match the ones in
					// mark-up
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_USER_ID, ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Insert time
				if (upload.getInsertTime() != null)
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_INSERT_TIME, upload.getInsertTime().toString()));// the ID here must match the
					// ones in mark-up
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_INSERT_TIME, ""));// the ID here must match the ones in mark-up
				}

				// For the alternative stripes 
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel()
				{
					@Override
					public String getObject()
					{
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				})
				);
			}
		};
		return sitePageableListView;
	}

	private AjaxLink buildLink(final Upload upload)
	{
		AjaxLink link = new AjaxLink("ajaxLinkId")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				// Sets the selected object into the model
				UploadVO uploadVo = containerForm.getModelObject();
				uploadVo.setUpload(upload);
				
				detailsPanelContainer.setVisible(true);
				detailPanelFormContainer.setEnabled(false);
				searchResultContainer.setVisible(false);
				searchPanelContainer.setVisible(false);
				
				// Button containers
				// View, thus view container visible
				viewButtonContainer.setVisible(true);
				viewButtonContainer.setEnabled(true);
				editButtonContainer.setVisible(false);
				
				// Have to Edit, before allowing delete
				detailPanel.getDetailForm().getDeleteButton().setEnabled(false);

				target.addComponent(searchResultContainer);
				target.addComponent(detailsPanelContainer);
				target.addComponent(searchPanelContainer);
				target.addComponent(viewButtonContainer);
				target.addComponent(editButtonContainer);
			}
		};

		// Add the label for the link
		// TODO when displaying text escape any special characters
		Label nameLinkLabel = new Label("nameLbl", upload.getFilename());
		link.add(nameLinkLabel);
		return link;
	}

	/**
	 * @param detailPanel
	 *           the detailPanel to set
	 */
	public void setDetailPanel(DetailPanel detailPanel)
	{
		this.detailPanel = detailPanel;
	}

	/**
	 * @return the detailPanel
	 */
	public DetailPanel getDetailPanel()
	{
		return detailPanel;
	}
}
