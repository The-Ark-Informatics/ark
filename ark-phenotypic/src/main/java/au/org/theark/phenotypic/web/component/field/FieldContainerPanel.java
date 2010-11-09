package au.org.theark.phenotypic.web.component.field;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.vo.FieldVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.field.form.ContainerForm;


public class FieldContainerPanel extends Panel{

	private static final long serialVersionUID = 1L;

	private FeedbackPanel feedBackPanel;
	
	//Panels
	private Search searchComponentPanel;
	private SearchResultList searchResultPanel;
	//private Details detailsPanel;
	
	private CompoundPropertyModel<FieldVO> fieldCpm;

	private IModel<Object> iModel;
	private PageableListView<Field> listView;

	//Mark-up Containers
	private WebMarkupContainer searchPanelContainer;
	private WebMarkupContainer resultListContainer;
	private WebMarkupContainer detailPanelContainer;
	private WebMarkupContainer detailPanelFormContainer;
	
	private ContainerForm containerForm;

	@SpringBean( name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService phenotypicService;
	
	public FieldContainerPanel(String id) {
		super(id);
		
		/*Initialise the CPM */
		fieldCpm = new CompoundPropertyModel<FieldVO>(new FieldVO());
	
		initialiseMarkupContainers();
		
		/*Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", fieldCpm);
		containerForm.add(initialiseFeedBackPanel());
		//containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		
		add(containerForm);
	}
	
	private void initialiseMarkupContainers(){
		
		searchPanelContainer = new WebMarkupContainer("searchContainer");
		searchPanelContainer.setOutputMarkupPlaceholderTag(true);
		
		detailPanelContainer = new WebMarkupContainer("detailsContainer");
		detailPanelContainer.setOutputMarkupPlaceholderTag(true);
		detailPanelContainer.setVisible(false);

		//Contains the controls of the details
		detailPanelFormContainer = new WebMarkupContainer("detailFormContainer");
		detailPanelFormContainer.setOutputMarkupPlaceholderTag(true);
		detailPanelFormContainer.setEnabled(false);
		
		//The wrapper for ResultsList panel that will contain a ListView
		resultListContainer = new WebMarkupContainer("resultListContainer");
		resultListContainer.setOutputMarkupPlaceholderTag(true);
		resultListContainer.setVisible(true);
	
	}
	
	private WebMarkupContainer initialiseFeedBackPanel(){
		/* Feedback Panel */
		feedBackPanel= new FeedbackPanel("feedbackMessage");
		feedBackPanel.setOutputMarkupId(true);
		return feedBackPanel;
	}
	
	
	private WebMarkupContainer initialiseSearchResults(){
		
		searchResultPanel = new SearchResultList("searchResults",detailPanelContainer,searchPanelContainer,containerForm,resultListContainer
				//TODO Implement detailsPanel
				//,detailsPanel
				);
		
		iModel = new LoadableDetachableModel<Object>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				return containerForm.getModelObject().getFieldCollection();
			}
		};

		listView  = searchResultPanel.buildPageableListView(iModel);
		listView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", listView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(listView);
		resultListContainer.add(searchResultPanel);
		return resultListContainer;
	}
	
	/*
	
	private WebMarkupContainer initialiseDetailPanel(){
		
		detailsPanel = new Details("detailsPanel", resultListContainer, feedBackPanel, detailPanelContainer,searchPanelContainer,containerForm);
		detailsPanel.initialisePanel();
		detailPanelContainer.add(detailsPanel);
		return detailPanelContainer;
		
	}
	*/
	
	private WebMarkupContainer initialiseSearchPanel(){
		//FieldVO fieldVo = new FieldVO();
		
		//Get a result-set by default
		Collection<Field> fieldCollection = new ArrayList<Field>();
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		
		try {
			if(sessionStudyId != null && sessionStudyId > 0){
				fieldCollection = phenotypicService.searchField(containerForm.getModelObject().getField());	
			}
			
		//} catch (ArkSystemException e) {
		} catch (Exception e) {
			this.error("A System error occured  while initializing Search Panel");
		}
		
		
		//fieldCpm.getObject().setFieldCollection(fieldCollection);
		containerForm.getModelObject().setFieldCollection(fieldCollection);
		
		searchComponentPanel = new Search("searchComponentPanel", 
											feedBackPanel, 
											searchPanelContainer, 
											listView,
											resultListContainer,
											detailPanelContainer,
											//detailsPanel,
											containerForm
											);
		
		searchComponentPanel.initialisePanel();
		searchPanelContainer.add(searchComponentPanel);
		return searchPanelContainer;
	}
}
