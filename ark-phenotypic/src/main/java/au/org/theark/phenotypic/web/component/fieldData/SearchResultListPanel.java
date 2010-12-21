package au.org.theark.phenotypic.web.component.fieldData;

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
import au.org.theark.phenotypic.model.entity.FieldData;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.web.component.fieldData.form.ContainerForm;

@SuppressWarnings( { "serial", "unchecked" })
public class SearchResultListPanel extends Panel
{

	private WebMarkupContainer	detailsPanelContainer;
	private WebMarkupContainer	searchPanelContainer;
	private WebMarkupContainer	searchResultContainer;
	private ContainerForm		containerForm;
	private DetailPanel			detailPanel;
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
	 * @return the pageableListView of FieldData
	 */
	public PageableListView<FieldData> buildPageableListView(IModel iModel)
	{

		PageableListView<FieldData> sitePageableListView = new PageableListView<FieldData>("fieldDataList", iModel, 10)
		{
			@Override
			protected void populateItem(final ListItem<FieldData> item)
			{
				FieldData fieldData = item.getModelObject();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DD_MM_YYYY);

				// Link of FieldData ID
				item.add(buildLink(fieldData));
				
				/* The FieldData Collection */
				if (fieldData.getCollection() != null)
				{
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_COLLECTION, fieldData.getCollection().getName()));
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_COLLECTION, ""));
				}
				
				/* The FieldData SubjectUid */
				if (fieldData.getLinkSubjectStudy() != null)
				{
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_SUBJECTUID, fieldData.getLinkSubjectStudy().getSubjectUID()));
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_SUBJECTUID, ""));
				}
				
				/* The FieldData Date Collected */
				if (fieldData.getDateCollected() != null)
				{
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_DATE_COLLECTED, simpleDateFormat.format(fieldData.getDateCollected())));
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_DATE_COLLECTED, ""));
				}
				
				/* The FieldData Field */
				if (fieldData.getField() != null)
				{
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_FIELD, fieldData.getField().getName()));
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_FIELD, ""));
				}
				
				
				/* The FieldData Value */
				if (fieldData.getValue() != null)
				{
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_VALUE, fieldData.getValue().toString()));
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_VALUE, ""));
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

	private AjaxLink buildLink(final FieldData fieldData)
	{
		AjaxLink link = new AjaxLink("fieldData.id")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				// Sets the selected object into the model
				PhenoCollectionVO phenoCollectionVo = containerForm.getModelObject();
				phenoCollectionVo.setFieldData(fieldData);
				
				detailsPanelContainer.setVisible(true);
				detailPanelFormContainer.setEnabled(false);
				searchResultContainer.setVisible(false);
				searchPanelContainer.setVisible(false);
				
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
		Label nameLinkLabel = new Label("nameLbl", fieldData.getId().toString());
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
