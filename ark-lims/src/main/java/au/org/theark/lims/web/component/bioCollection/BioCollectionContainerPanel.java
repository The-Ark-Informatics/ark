package au.org.theark.lims.web.component.bioCollection;

import java.util.ArrayList;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.bioCollection.form.ContainerForm;

public class BioCollectionContainerPanel extends AbstractContainerPanel<LimsVO>
{
	/**
	 * 
	 */
	private static final long																		serialVersionUID	= -573200292391828047L;
	private static final Logger																	log					= LoggerFactory.getLogger(BioCollectionContainerPanel.class);

	// Panels
	private SearchPanel																				searchComponentPanel;
	private SearchResultListPanel																	searchResultPanel;
	private DetailPanel																				detailPanel;
	private PageableListView<au.org.theark.core.model.lims.entity.BioCollection>	listView;
	private WebMarkupContainer																		arkContextMarkup;

	private ContainerForm																			containerForm;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService																				iLimsService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>																iArkCommonService;

	public BioCollectionContainerPanel(String id)
	{
		super(id);

		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<LimsVO>(new LimsVO());

		initialiseMarkupContainers();

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());

		add(containerForm);
	}

	public BioCollectionContainerPanel(String id, WebMarkupContainer arkContextMarkup)
	{
		super(id);

		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
		this.arkContextMarkup = arkContextMarkup;

		initialiseMarkupContainers();

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());

		add(containerForm);
	}

	protected WebMarkupContainer initialiseSearchResults()
	{

		searchResultPanel = new SearchResultListPanel("searchResults", detailPanelContainer, searchPanelContainer, containerForm, searchResultPanelContainer, detailPanel, viewButtonContainer,
				editButtonContainer, detailPanelFormContainer, arkContextMarkup);

		iModel = new LoadableDetachableModel<Object>()
		{
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load()
			{
				// Get a list of collections for the study/subject in context by default
				java.util.List<au.org.theark.core.model.lims.entity.BioCollection> bioCollectionList = new ArrayList<au.org.theark.core.model.lims.entity.BioCollection>();
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				
				if (sessionStudyId != null && sessionStudyId > 0)
				{
					Study study = iArkCommonService.getStudy(sessionStudyId);
					containerForm.getModelObject().getBioCollection().setStudy(study);
					
					try
					{
						bioCollectionList = iLimsService.searchBioCollection(containerForm.getModelObject().getBioCollection());
					}
					catch (ArkSystemException e)
					{
						log.error(e.getMessage());
					}
				}

				listView.removeAll();
				containerForm.getModelObject().setBioCollectionList(bioCollectionList);
				return containerForm.getModelObject().getBioCollectionList();
			}
		};

		listView = searchResultPanel.buildPageableListView(iModel);
		listView.setReuseItems(true);
		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", listView) {
			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.addComponent(searchResultPanelContainer);
			}
		};
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(listView);
		searchResultPanelContainer.add(searchResultPanel);
		return searchResultPanelContainer;
	}

	protected WebMarkupContainer initialiseDetailPanel()
	{
		detailPanel = new DetailPanel("detailPanel", searchResultPanelContainer, feedBackPanel, detailPanelContainer, searchPanelContainer, containerForm, viewButtonContainer, editButtonContainer,
				detailPanelFormContainer, arkContextMarkup);
		detailPanel.initialisePanel();
		detailPanelContainer.add(detailPanel);
		return detailPanelContainer;
	}

	protected WebMarkupContainer initialiseSearchPanel()
	{
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

		if (sessionStudyId != null && sessionStudyId > 0)
		{
			Study study = iArkCommonService.getStudy(sessionStudyId);
			BioCollection bioCollection = new BioCollection();
			bioCollection.setStudy(study);
			containerForm.getModelObject().setBioCollection(bioCollection);
		}

		searchComponentPanel = new SearchPanel("searchPanel", feedBackPanel, searchPanelContainer, listView, searchResultPanelContainer, detailPanelContainer, detailPanel, containerForm,
				viewButtonContainer, editButtonContainer, detailPanelFormContainer, arkContextMarkup);
		searchComponentPanel.initialisePanel();

		searchPanelContainer.add(searchComponentPanel);
		return searchPanelContainer;
	}
}