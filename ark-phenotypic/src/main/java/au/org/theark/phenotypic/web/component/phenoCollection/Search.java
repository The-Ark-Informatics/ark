/**
 * 
 */
package au.org.theark.phenotypic.web.component.phenoCollection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.phenotypic.model.entity.Collection;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.vo.CollectionVO;
import au.org.theark.phenotypic.model.vo.FieldVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.summaryModule.form.ContainerForm;
import au.org.theark.phenotypic.web.component.summaryModule.form.SearchForm;

/**
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public class Search extends Panel
{
	private FeedbackPanel				feedBackPanel;
	private WebMarkupContainer			searchMarkupContainer;
	private WebMarkupContainer			listContainer;
	private WebMarkupContainer			detailContainer;
	private PageableListView<Collection>	listView;
	private ContainerForm				containerForm;
	private Detail							detail;
	private WebMarkupContainer viewButtonContainer;
	private WebMarkupContainer editButtonContainer;
	private WebMarkupContainer detailPanelFormContainer;

	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService			phenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService			iArkCommonService;

	/* Constructor */
	public Search(	String id, 
						FeedbackPanel feedBackPanel, 
						WebMarkupContainer searchMarkupContainer, 
						PageableListView<Collection> listView, 
						WebMarkupContainer resultListContainer,
						WebMarkupContainer detailPanelContainer, 
						Detail detail, 
						ContainerForm containerForm,
						WebMarkupContainer viewButtonContainer,
						WebMarkupContainer editButtonContainer,
						WebMarkupContainer detailPanelFormContainer)
	{
		super(id);
		this.searchMarkupContainer = searchMarkupContainer;
		this.listView = listView;
		this.feedBackPanel = feedBackPanel;
		this.detailContainer = detailPanelContainer;
		this.detail = detail;
		this.containerForm = containerForm;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
		listContainer = resultListContainer;
	}

	public void processDetail(AjaxRequestTarget target)
	{
		searchMarkupContainer.setVisible(false);
		detail.getDetailForm().getIdTxtFld().setEnabled(false);
		detailContainer.setVisible(true);
		listContainer.setVisible(false);
		
		// Button containers
		// New Field, thus Edit container visible
		viewButtonContainer.setVisible(false);
		editButtonContainer.setVisible(true);
		editButtonContainer.setEnabled(true);
		
		// Hide Delete button on New
		detail.getDetailForm().getDeleteButton().setVisible(false);
		
		// Enable all fields to be edited
		detailPanelFormContainer.setEnabled(true);
		
		target.addComponent(detailContainer);
		target.addComponent(searchMarkupContainer);
		target.addComponent(listContainer);
		target.addComponent(viewButtonContainer);
		target.addComponent(editButtonContainer);
	}

	public void initialisePanel()
	{
		// Get the study id from the session and get the study
		final Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

		SearchForm searchForm = new SearchForm(au.org.theark.core.Constants.SEARCH_FORM, (CompoundPropertyModel<CollectionVO>) containerForm.getModel())
		{
			protected void onSearch(AjaxRequestTarget target)
			{
				// Refresh the FB panel if there was an old message from previous search result
				target.addComponent(feedBackPanel);

				// Get a list of all Fields for the Study in context
				Study study = iArkCommonService.getStudy(sessionStudyId);
				//Field searchField = containerForm.getModelObject().getField();
				//searchField.setStudy(study);
				Collection phenoCollection = containerForm.getModelObject().getCollection();

				java.util.Collection<Collection> phenoCollectionCollection = phenotypicService.searchPhenotypicCollection(phenoCollection);

				if (phenoCollectionCollection != null && phenoCollectionCollection.size() == 0)
				{
					this.info("Phenotypic Collections with the specified criteria does not exist in the system.");
					target.addComponent(feedBackPanel);
				}
				containerForm.getModelObject().setPhenoCollectionCollection(phenoCollectionCollection);
				listView.removeAll();
				listContainer.setVisible(true);// Make the WebMarkupContainer that houses the search results visible
				target.addComponent(listContainer);// For ajax this is required so
			}

			protected void onNew(AjaxRequestTarget target)
			{
				// Show the details panel name and description
				CollectionVO phenoCollectionVo = new CollectionVO();
				phenoCollectionVo.setMode(au.org.theark.core.Constants.MODE_NEW);
				
				// Set study for the new field
				Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				Study	study = iArkCommonService.getStudy(studyId);
				phenoCollectionVo.getField().setStudy(study);
				
				containerForm.setModelObject(phenoCollectionVo);
				processDetail(target);
			}
		};
		add(searchForm);
	}
}