package au.org.theark.phenotypic.web.component.phenodatasetdefinition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.pheno.entity.PhenoDataSetGroup;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.CustomFieldGroupVO;
import au.org.theark.core.vo.PhenoDataSetFieldGroupVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenodatasetdefinition.form.ContainerForm;

/**
 * @author nivedann
 *
 */
public class DataDictionaryGroupContainerPanel extends AbstractContainerPanel<PhenoDataSetFieldGroupVO>{

	

	private static final long serialVersionUID = 1L;


	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;
	
	
	private ContainerForm containerForm;
	
	private ArkDataProvider2<PhenoDataSetGroup, PhenoDataSetGroup> arkDataProvider;
	private DataView<PhenoDataSetGroup> dataView;
	
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService	iPhenotypicService;
	
	
	/**
	 * Constructor that uses the ArkCrudContainerVO
	 * @param id
	 * @param useArkCrudContainerVO
	 */
	public DataDictionaryGroupContainerPanel(String id, ArkFunction associatedPrimaryFn) {

		super(id);
		cpModel = new CompoundPropertyModel<PhenoDataSetFieldGroupVO>( new PhenoDataSetFieldGroupVO());
		cpModel.getObject().getPhenoDataSetGroup().setArkFunction(associatedPrimaryFn);//The AssociatedPrimaryFunction is passed in for Pheno it will be DataDictionary
		
		prerenderContextCheck();
		
		containerForm = new ContainerForm("containerForm",cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		
		// Added to allow file uploads of phenoDataSet fields
		containerForm.setMultiPart(true);
		add(containerForm);
	}

	
	protected void prerenderContextCheck() {
		
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Long sessionModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);

		if ((sessionStudyId != null) && (sessionModuleId != null)) {
			Study study =  iArkCommonService.getStudy(sessionStudyId);
			ArkModule arkModule =  iArkCommonService.getArkModuleById(sessionModuleId);
			if (study != null && arkModule != null) {
				cpModel.getObject().getPhenoDataSetGroup().setStudy(study);
			}
			try {
				cpModel.getObject().setArkUser(iArkCommonService.getArkUser((String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_USERID)));
			} catch (InvalidSessionException | EntityNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}
	/* (non-Javadoc)
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseDetailPanel()
	 */
	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		Panel detailsPanel = new EmptyPanel("detailsPanel");
		detailsPanel.setOutputMarkupPlaceholderTag(true);	//ensure this is replaceable
		arkCrudContainerVO.getDetailPanelContainer().add(detailsPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchPanel()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		SearchPanel searchPanel = new SearchPanel("searchPanel", cpModel,arkCrudContainerVO,feedBackPanel);
		searchPanel.initialisePanel();
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchResults()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		
		SearchResultListPanel searchResultListPanel = new SearchResultListPanel("searchResults", cpModel, arkCrudContainerVO, feedBackPanel);

		// Data providor to paginate resultList
		arkDataProvider = new ArkDataProvider2<PhenoDataSetGroup, PhenoDataSetGroup>() {

			private static final long serialVersionUID = 1L;

			public int size() {
				return (int) iPhenotypicService.getPhenoDataSetFieldGroupCount(criteriaModel.getObject());
			}

			public Iterator<PhenoDataSetGroup> iterator(int first, int count) {
				List<PhenoDataSetGroup> listSubjects = new ArrayList<PhenoDataSetGroup>();
				if (isActionPermitted()) {
					listSubjects = iPhenotypicService.getPhenoDataSetGroups(criteriaModel.getObject(), first, count);
				}
				return listSubjects.iterator();
			}
		};
		
		// Set the criteria for the data provider
		arkDataProvider.setCriteriaModel(new PropertyModel<PhenoDataSetGroup>(cpModel, "phenoDataSetGroup"));
		dataView = searchResultListPanel.buildDataView(arkDataProvider);
		dataView.setItemsPerPage(iArkCommonService.getRowsPerPage());

		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", dataView) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(arkCrudContainerVO.getSearchResultPanelContainer());
			}
		};
		searchResultListPanel.add(pageNavigator);
		searchResultListPanel.add(dataView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultListPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}
	
	protected WebMarkupContainer initialiseFeedBackPanel() {
		/* Feedback Panel */
		feedBackPanel = new FeedbackPanel("feedbackMessage");
		feedBackPanel.setOutputMarkupId(true);
		return feedBackPanel;
	}

}
