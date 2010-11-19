package au.org.theark.phenotypic.web.component.phenotypicImport;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.phenotypic.model.entity.PhenoCollection;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.Constants;
import au.org.theark.phenotypic.web.component.phenotypicImport.form.ContainerForm;

@SuppressWarnings( { "serial" ,"unused"})
public class PhenotypicImportContainer extends AbstractContainerPanel<PhenoCollectionVO>
{
	private static final long						serialVersionUID	= 1L;

	// Panels
	private SearchPanel								searchComponentPanel;
	private SearchResultListPanel					searchResultPanel;
	private DetailPanel								detailPanel;
	private PageableListView<PhenoCollection>	listView;
	private ContainerForm							containerForm;
	
	@SpringBean(name = "phenotypicService")
	private IPhenotypicService	serviceInterface;

	private transient Logger	log					= LoggerFactory.getLogger(PhenotypicImportContainer.class);
	private boolean phenoCollectionInContext		= false;

	public PhenotypicImportContainer(String id)
	{
		super(id);

		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<PhenoCollectionVO>(new PhenoCollectionVO());

		initialiseMarkupContainers();

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		
		initialiseTestButtons();

		add(containerForm);
	}
	
	public void initialiseTestButtons(){
		
		// Test button to validate pheno data file
		containerForm.add(new Button(au.org.theark.phenotypic.web.Constants.VALIDATE_PHENOTYPIC_DATA_FILE, new StringResourceModel("page.validatePhenotypicDataFile", this, null))
		{
			public void onSubmit()
			{
				log.info("Validate Phenotypic Data File");
				
				java.util.Collection<String> validationMessages = null;
				//TODO Add placeholder to store the validation messages 
				validationMessages = serviceInterface.validatePhenotypicDataFile();
			}
			
			public boolean isVisible(){
				boolean flag = false;
				Long sessionCollectionId = (Long)SecurityUtils.getSubject().getSession().getAttribute(Constants.SESSION_PHENO_COLLECTION_ID);
				
				if(sessionCollectionId != null && sessionCollectionId.longValue() > 0){
					flag = true;
					phenoCollectionInContext = true;
				}
				return flag;
			}
		});
		
		// Test button to import pheno data file
		containerForm.add(new Button(au.org.theark.phenotypic.web.Constants.IMPORT_PHENOTYPIC_DATA_FILE, new StringResourceModel("page.importPhenotypicDataFile", this, null))
		{
			public void onSubmit()
			{
				log.info("Import Phenotypic Data File");
				serviceInterface.importPhenotypicDataFile();
			}
			
			public boolean isVisible(){
				boolean flag = false;
				Long sessionCollectionId = (Long)SecurityUtils.getSubject().getSession().getAttribute(Constants.SESSION_PHENO_COLLECTION_ID);
				
				if(sessionCollectionId != null && sessionCollectionId.longValue() > 0){
					flag = true;
					phenoCollectionInContext = true;
				}
				return flag;
			}
		});
		
		add(containerForm);
	}

	protected WebMarkupContainer initialiseSearchResults()
	{
		searchResultPanel = new SearchResultListPanel("searchResults", detailPanelContainer, searchPanelContainer, containerForm, searchResultPanelContainer, detailPanel, viewButtonContainer,
				editButtonContainer, detailPanelFormContainer);

		iModel = new LoadableDetachableModel<Object>()
		{
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load()
			{
				return containerForm.getModelObject().getPhenoCollectionCollection();
			}
		};

		listView = searchResultPanel.buildPageableListView(iModel);
		listView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", listView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(listView);
		searchResultPanelContainer.add(searchResultPanel);
		
		// for Summary Module, disable search result list
		searchResultPanel.setVisible(false);
		
		return searchResultPanelContainer;
	}

	protected WebMarkupContainer initialiseDetailPanel()
	{
		detailPanel = new DetailPanel("detailPanel", searchResultPanelContainer, feedBackPanel, detailPanelContainer, searchPanelContainer, containerForm, viewButtonContainer, editButtonContainer,
				detailPanelFormContainer);
		detailPanel.initialisePanel();
		detailPanelContainer.add(detailPanel);
		return detailPanelContainer;
	}

	protected WebMarkupContainer initialiseSearchPanel()
	{
		searchComponentPanel = new SearchPanel("searchPanel", feedBackPanel, searchPanelContainer, listView, searchResultPanelContainer, detailPanelContainer, detailPanel, containerForm,
				viewButtonContainer, editButtonContainer, detailPanelFormContainer);
		searchComponentPanel.initialisePanel();

		searchPanelContainer.add(searchComponentPanel);
		return searchPanelContainer;
	}
}