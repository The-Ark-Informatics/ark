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

import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.vo.FieldVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.field.form.ContainerForm;

@SuppressWarnings( { "serial", "unchecked" })
public class SearchResultList extends Panel
{

	private WebMarkupContainer	detailsPanelContainer;
	private WebMarkupContainer	searchPanelContainer;
	private WebMarkupContainer	searchResultContainer;
	private ContainerForm		containerForm;

	// TODO build detailPanel
	// private Details detailPanel;

	// TODO build fieldService
	// @SpringBean( name = "userService")
	// private IUserService userService;

	@SpringBean( name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService phenotypicService; 
	
	// TODO build detailPanel
	public SearchResultList(String id, WebMarkupContainer detailPanelContainer, WebMarkupContainer searchPanelContainer, ContainerForm studyCompContainerForm, WebMarkupContainer searchResultContainer// ,
																																																																		// Details
																																																																		// details
	)
	{
		super(id);
		this.detailsPanelContainer = detailPanelContainer;
		this.containerForm = studyCompContainerForm;
		this.searchPanelContainer = searchPanelContainer;
		this.searchResultContainer = searchResultContainer;
		// TODO build detailPanel
		// this.detailPanel = details;

	}

	/**
	 * 
	 * @param iModel
	 * @return
	 */
	public PageableListView<Field> buildPageableListView(IModel iModel)
	{

		PageableListView<Field> sitePageableListView = new PageableListView<Field>("fieldList", iModel, 10)
		{
			@Override
			protected void populateItem(final ListItem<Field> item)
			{
				Field field = item.getModelObject();

				/* The Field ID */
				if (field.getId() != null)
				{
					// Add the id component here
					item.add(new Label("field.id", field.getId().toString()));
				}
				else
				{
					item.add(new Label("field.id", ""));
				}
				
				// Component Name Link
				item.add(buildLink(field));
				
				/*
				// TODO when displaying text escape any special characters
				// Name 
				if (field.getName() != null)
				{
					item.add(new Label("field.name", field.getName()));// the ID here must match the ones in mark-up
				}
				else
				{
					item.add(new Label("field.name", ""));// the ID here must match the ones in mark-up
				}

				// TODO when displaying text escape any special characters
				// Description
				if (field.getDescription() != null)
				{
					item.add(new Label("field.description", field.getDescription()));// the ID here must match the ones in mark-up
				}
				else
				{
					item.add(new Label("field.description", ""));// the ID here must match the ones in mark-up
				}
				
				// TODO when displaying text escape any special characters
				// Units
				if (field.getName() != null)
				{
					item.add(new Label("field.units", field.getUnits()));// the ID here must match the ones in mark-up
				}
				else
				{
					item.add(new Label("field.units", ""));// the ID here must match the ones in mark-up
				}
				
				// TODO when displaying text escape any special characters
				// Min
				if (field.getMinValue() != null)
				{
					item.add(new Label("field.minValue", field.getMinValue()));// the ID here must match the ones in mark-up
				}
				else
				{
					item.add(new Label("field.minMalue", ""));// the ID here must match the ones in mark-up
				}
				
				// TODO when displaying text escape any special characters
				// Max
				if (field.getMinValue() != null)
				{
					item.add(new Label("field.maxValue", field.getMaxValue()));// the ID here must match the ones in mark-up
				}
				else
				{
					item.add(new Label("field.maxMalue", ""));// the ID here must match the ones in mark-up
				}
				*/

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

		AjaxLink link = new AjaxLink("field.name")
		{

			@Override
			public void onClick(AjaxRequestTarget target)
			{

				FieldVO fieldVo = containerForm.getModelObject();
				fieldVo.setMode(au.org.theark.core.Constants.MODE_EDIT);
				fieldVo.setField(field);// Sets the selected object into the model
				detailsPanelContainer.setVisible(true);
				searchResultContainer.setVisible(false);
				searchPanelContainer.setVisible(false);

				// TODO Build detailPanel
				// detailPanel.getDetailsForm().getComponentIdTxtFld().setEnabled(false);
				target.addComponent(searchResultContainer);
				// target.addComponent(detailsPanelContainer);
				target.addComponent(searchPanelContainer);
			}
		};

		// Add the label for the link
		Label nameLinkLabel = new Label("nameLbl", field.getName());
		link.add(nameLinkLabel);
		return link;
	}
}
