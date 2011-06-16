package au.org.theark.lims.web.component.bioCollection;

import java.text.SimpleDateFormat;

import org.apache.shiro.SecurityUtils;
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

import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.bioCollection.form.ContainerForm;

@SuppressWarnings( { "serial", "unchecked" })
public class SearchResultListPanel extends Panel
{
	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService									iLimsService;
	
	private WebMarkupContainer	detailsPanelContainer;
	private WebMarkupContainer	searchPanelContainer;
	private WebMarkupContainer	searchResultContainer;
	private ContainerForm		containerForm;
	private DetailPanel			detailPanel;
	private WebMarkupContainer detailPanelFormContainer;
	private WebMarkupContainer viewButtonContainer;
	private WebMarkupContainer editButtonContainer;
	private WebMarkupContainer arkContextMarkup;

	public SearchResultListPanel(String id, WebMarkupContainer detailPanelContainer, WebMarkupContainer searchPanelContainer, ContainerForm studyCompContainerForm, WebMarkupContainer searchResultContainer,
			DetailPanel detail,
			WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer,
			WebMarkupContainer detailPanelFormContainer,
			WebMarkupContainer arkContextMarkup)
	{
		super(id);
		this.detailsPanelContainer = detailPanelContainer;
		this.containerForm = studyCompContainerForm;
		this.searchPanelContainer = searchPanelContainer;
		this.searchResultContainer = searchResultContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
		this.arkContextMarkup = arkContextMarkup;
		this.setDetailPanel(detail);
		
	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of Collection
	 */
	public PageableListView<BioCollection> buildPageableListView(IModel iModel)
	{

		PageableListView<BioCollection> sitePageableListView = new PageableListView<BioCollection>("collectionList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE)
		{
			@Override
			protected void populateItem(final ListItem<BioCollection> item)
			{
				BioCollection limsCollection = item.getModelObject();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);

				/* The phenoCollection ID */
				if (limsCollection.getId() != null)
				{
					// Add the id component here
					item.add(new Label("id", limsCollection.getId().toString()));
				}
				else
				{
					item.add(new Label("id", ""));
				}

				// Component Name Link
				item.add(buildLink(limsCollection));

				// TODO when displaying text escape any special characters
				if (limsCollection.getComments() != null)
				{
					item.add(new Label("comments", limsCollection.getComments()));// the ID here must match
					// the ones in mark-up
				}
				else
				{
					item.add(new Label("comments", ""));// the ID here must match the ones in mark-up
				}
				
				// TODO when displaying text escape any special characters
				// Start Date
				if (limsCollection.getCollectionDate() != null)
				{
					item.add(new Label("collectionDate", simpleDateFormat.format(limsCollection.getCollectionDate())));// the ID here must match
					// the ones in mark-up
				}
				else
				{
					item.add(new Label("collectionDate", ""));// the ID here must match the ones in mark-up
				}
				
				// TODO when displaying text escape any special characters
				// Expiry Date
				if (limsCollection.getSurgeryDate() != null)
				{
					item.add(new Label("surgeryDate", simpleDateFormat.format(limsCollection.getSurgeryDate())));// the ID here must match
					// the ones in mark-up
				}
				else
				{
					item.add(new Label("surgeryDate", ""));// the ID here must match the ones in mark-up
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

	private AjaxLink buildLink(final BioCollection limsCollection)
	{
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("limsCollection.name")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				// Sets the selected object into the model
				LimsVO limsVo = containerForm.getModelObject();
				limsVo.setBioCollection(limsCollection);
				containerForm.setModelObject(limsVo);
				
				// Place the selected collection in session context for the user
				//SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID, limsCollection.getId());
		
				detailsPanelContainer.setVisible(true);
				detailPanelFormContainer.setEnabled(false);
				searchResultContainer.setVisible(false);
				searchPanelContainer.setVisible(false);
				
				// Button containers
				// View Field, thus view container visible
				viewButtonContainer.setVisible(true);
				viewButtonContainer.setEnabled(true);
				editButtonContainer.setVisible(false);
				
				ContextHelper contextHelper = new ContextHelper();
				//contextHelper.setStudyContextLabel(target, limsCollection.getStudy().getName(), arkContextMarkup);
				//contextHelper.setPhenoContextLabel(target, limsCollection.getName(), arkContextMarkup);

				target.addComponent(searchResultContainer);
				target.addComponent(detailsPanelContainer);
				target.addComponent(searchPanelContainer);
				target.addComponent(viewButtonContainer);
				target.addComponent(editButtonContainer);
			}
		};

		// Add the label for the link
		// TODO when displaying text escape any special characters
		Label nameLinkLabel = new Label("nameLbl", limsCollection.getName());
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