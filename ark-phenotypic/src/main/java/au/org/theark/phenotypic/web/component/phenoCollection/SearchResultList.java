package au.org.theark.phenotypic.web.component.phenoCollection;

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

import au.org.theark.phenotypic.model.entity.Collection;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.vo.CollectionVO;
import au.org.theark.phenotypic.model.vo.FieldVO;
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
	public PageableListView<Collection> buildPageableListView(IModel iModel)
	{

		PageableListView<Collection> sitePageableListView = new PageableListView<Collection>("collectionList", iModel, 10)
		{
			@Override
			protected void populateItem(final ListItem<Collection> item)
			{
				Collection phenoCollection = item.getModelObject();

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

	private AjaxLink buildLink(final Collection phenoCollection)
	{
		AjaxLink link = new AjaxLink("field.name")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				// Sets the selected object into the model
				CollectionVO collectionVo = containerForm.getModelObject();
				collectionVo.setCollection(phenoCollection);
				
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
