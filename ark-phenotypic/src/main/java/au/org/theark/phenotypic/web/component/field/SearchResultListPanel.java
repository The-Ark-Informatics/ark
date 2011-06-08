package au.org.theark.phenotypic.web.component.field;

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

import au.org.theark.core.model.pheno.entity.Field;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.phenotypic.model.vo.FieldVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.field.form.ContainerForm;

@SuppressWarnings( { "serial", "unchecked" })
public class SearchResultListPanel extends Panel
{
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService				iPhenotypicService;

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
	 * @return the pageableListView of Field
	 */
	public PageableListView<Field> buildPageableListView(IModel iModel)
	{

		PageableListView<Field> sitePageableListView = new PageableListView<Field>("fieldList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE)
		{
			@Override
			protected void populateItem(final ListItem<Field> item)
			{
				Field field = item.getModelObject();

				/* The Field ID */
				if (field.getId() != null)
				{
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_ID, field.getId().toString()));
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_ID, ""));
				}

				// Component Name Link
				item.add(buildLink(field));

				// TODO when displaying text escape any special characters
				// Field Type
				if (field.getFieldType() != null)
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_FIELD_TYPE, field.getFieldType().getName()));// the ID here
					// must match the
					// ones in mark-up
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_FIELD_TYPE, ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Description
				if (field.getDescription() != null)
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_DESCRIPTION, field.getDescription()));// the ID here must match
					// the ones in mark-up
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_DESCRIPTION, ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Units
				if (field.getName() != null)
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_UNITS, field.getUnits()));// the ID here must match the ones in
					// mark-up
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_UNITS, ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Min
				if (field.getMinValue() != null)
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MIN_VALUE, field.getMinValue()));// the ID here must match the
					// ones in mark-up
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MIN_VALUE, ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Max
				if (field.getMinValue() != null)
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MAX_VALUE, field.getMaxValue()));// the ID here must match the
					// ones in mark-up
				}
				else
				{
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MAX_VALUE, ""));// the ID here must match the ones in mark-up
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

	private AjaxLink buildLink(final Field field)
	{
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("field.name")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				// Sets the selected object into the model
				FieldVO fieldVo = containerForm.getModelObject();
				fieldVo.setField(field);
				
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
				//detailPanel.getDetailForm().getDeleteButton().setEnabled(false);
				
				// Disable fieldType dropdown if data exists
				boolean hasData = iPhenotypicService.fieldHasData(field);
				detailPanel.getDetailForm().getFieldTypeDdc().setEnabled(!hasData);

				target.addComponent(searchResultContainer);
				target.addComponent(detailsPanelContainer);
				target.addComponent(searchPanelContainer);
				target.addComponent(viewButtonContainer);
				target.addComponent(editButtonContainer);
			}
		};

		// Add the label for the link
		// TODO when displaying text escape any special characters
		Label nameLinkLabel = new Label("nameLbl", field.getName());
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
