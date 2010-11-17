package au.org.theark.phenotypic.web.component.summaryModule;

import java.text.SimpleDateFormat;

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
import au.org.theark.phenotypic.model.entity.PhenoCollection;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.web.component.summaryModule.form.ContainerForm;

@SuppressWarnings( { "serial", "unchecked" })
public class SearchResultList extends Panel
{

	private WebMarkupContainer	detailsPanelContainer;
	private WebMarkupContainer	searchPanelContainer;
	private WebMarkupContainer	searchResultContainer;
	private ContainerForm		containerForm;
	private Detail					detailPanel;
	private WebMarkupContainer detailPanelFormContainer;
	private WebMarkupContainer viewButtonContainer;
	private WebMarkupContainer editButtonContainer;

	public SearchResultList(String id, WebMarkupContainer detailPanelContainer, WebMarkupContainer searchPanelContainer, ContainerForm studyCompContainerForm, WebMarkupContainer searchResultContainer,
			Detail detail,
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
	 * @return the pageableListView of PhenoCollection
	 */
	public PageableListView<PhenoCollection> buildPageableListView(IModel iModel)
	{

		PageableListView<PhenoCollection> sitePageableListView = new PageableListView<PhenoCollection>("collectionList", iModel, 10)
		{
			@Override
			protected void populateItem(final ListItem<PhenoCollection> item)
			{
				PhenoCollection phenoCollection = item.getModelObject();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DD_MM_YYYY);

				/* The phenoCollection ID */
				if (phenoCollection.getId() != null)
				{
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_ID, phenoCollection.getId().toString()));
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_ID, ""));
				}

				// Component Name Link
				item.add(buildLink(phenoCollection));

				// TODO when displaying text escape any special characters
				// PhenoCollection status
				if (phenoCollection.getStatus() != null)
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_STATUS, phenoCollection.getStatus().getName()));
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_STATUS, ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Description
				if (phenoCollection.getDescription() != null)
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_DESCRIPTION, phenoCollection.getDescription()));// the ID here must match
					// the ones in mark-up
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_DESCRIPTION, ""));// the ID here must match the ones in mark-up
				}
				
				// TODO when displaying text escape any special characters
				// Start Date
				if (phenoCollection.getStartDate() != null)
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_START_DATE, simpleDateFormat.format(phenoCollection.getStartDate())));// the ID here must match
					// the ones in mark-up
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_START_DATE, ""));// the ID here must match the ones in mark-up
				}
				
				// TODO when displaying text escape any special characters
				// Expiry Date
				if (phenoCollection.getExpiryDate() != null)
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_EXPIRY_DATE, simpleDateFormat.format(phenoCollection.getExpiryDate())));// the ID here must match
					// the ones in mark-up
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_EXPIRY_DATE, ""));// the ID here must match the ones in mark-up
				}

				/* For the alternative stripes */
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel()
				{
					@Override
					public String getObject()
					{
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}
		};
		return sitePageableListView;
	}

	private AjaxLink buildLink(final PhenoCollection phenoCollection)
	{
		AjaxLink link = new AjaxLink("phenoCollection.name")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				// Sets the selected object into the model
				PhenoCollectionVO collectionVo = containerForm.getModelObject();
				collectionVo.setPhenoCollection(phenoCollection);
				
				detailsPanelContainer.setVisible(true);
				detailPanelFormContainer.setEnabled(false);
				searchResultContainer.setVisible(false);
				searchPanelContainer.setVisible(false);

				detailPanel.getDetailForm().getIdTxtFld().setEnabled(false);
				
				// Button containers
				// View Field, thus view container visible
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
		Label nameLinkLabel = new Label("nameLbl", phenoCollection.getName());
		link.add(nameLinkLabel);
		return link;
	}

	/**
	 * @param detailPanel
	 *           the detailPanel to set
	 */
	public void setDetailPanel(Detail detailPanel)
	{
		this.detailPanel = detailPanel;
	}

	/**
	 * @return the detailPanel
	 */
	public Detail getDetailPanel()
	{
		return detailPanel;
	}
}
