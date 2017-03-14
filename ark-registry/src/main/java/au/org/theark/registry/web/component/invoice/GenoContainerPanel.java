/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.registry.web.component.invoice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.geno.entity.Pipeline;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.registry.web.component.invoice.form.ContainerForm;

/**
 * @author nivedann
 * 
 */
public class GenoContainerPanel extends AbstractContainerPanel<Pipeline> {

	private static final long serialVersionUID = 1L;
	
	private ContainerForm					containerForm;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService										iArkCommonService;
	
	// Panels
	private DetailPanel						detailPanel;
	private SearchPanel												searchPanel;
	private SearchResultListPanel									searchResultsPanel;

	
	private ArkDataProvider2 arkDataProvider;
	private DataView<Pipeline>									dataView;
	private PageableListView<Pipeline>						listView;
	private ArkDataProvider<Pipeline, IArkCommonService>	subjectProvider;
	protected WebMarkupContainer resultsWmc = new WebMarkupContainer("resultsWmc");
	
	/**
	 * @param id
	 */
	public GenoContainerPanel(String id) {
		super(id);
		cpModel = new CompoundPropertyModel<Pipeline>(new Pipeline());
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		
		add(containerForm);
			
	}
	
	@Override
	protected void onBeforeRender() {
		//arkCrudContainerVO.showDetailPanelInEditMode(AjaxRequestTarget.get());
		super.onBeforeRender();
	}
	
	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		detailPanel = new DetailPanel("detailsPanel", feedBackPanel, arkCrudContainerVO, containerForm);
		detailPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(detailPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}
	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		searchPanel =new SearchPanel("searchPanel", feedBackPanel, listView, containerForm, arkCrudContainerVO);
		searchPanel.initialisePanel();
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}
	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		searchResultsPanel = new SearchResultListPanel("searchResults", feedBackPanel, containerForm, arkCrudContainerVO);

		// Data providor to paginate resultList
		subjectProvider = new ArkDataProvider<Pipeline, IArkCommonService>(iArkCommonService) {

			private static final long	serialVersionUID	= 1L;

			public int size() {
				Study study = iArkCommonService.getStudy((Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID));
				model.getObject().setStudy(study);
				return (int) service.getPipelineCount(model.getObject());
			}

			public Iterator<Pipeline> iterator(int first, int count) {
				List<Pipeline> listSubjects = new ArrayList<Pipeline>();
				if (isActionPermitted()) {
					listSubjects = iArkCommonService.searchPageablePipelines(model.getObject(), first, count);
				}
				return listSubjects.iterator();
			}
		};
		subjectProvider.setModel(this.cpModel);

		dataView = searchResultsPanel.buildDataView(subjectProvider);
		dataView.setItemsPerPage(iArkCommonService.getRowsPerPage());

		PagingNavigator pageNavigator = new PagingNavigator("navigator", dataView);
		resultsWmc.add(pageNavigator);
		resultsWmc.add(dataView);
		searchResultsPanel.add(resultsWmc);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultsPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

}
