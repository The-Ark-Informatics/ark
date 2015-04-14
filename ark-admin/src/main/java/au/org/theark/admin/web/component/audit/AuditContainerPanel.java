package au.org.theark.admin.web.component.audit;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.admin.web.component.audit.form.ContainerForm;
import au.org.theark.core.vo.AuditVO;
import au.org.theark.core.web.component.AbstractContainerPanel;

public class AuditContainerPanel extends AbstractContainerPanel {

	private static final long serialVersionUID = 1L;
	
	private ContainerForm containerForm;
	private SearchPanel searchPanel;
	private DetailPanel detailPanel;
	private SearchResultsPanel searchResultsPanel;
	
	public AuditContainerPanel(String id) {
		super(id);
			
		initCrudContainerVO();
		containerForm = new ContainerForm("containerForm", new CompoundPropertyModel<AuditVO>(new AuditVO()));
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchPanel());
		containerForm.add(initialiseSearchResults());
		
		add(containerForm);		
	}

	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		detailPanel = new DetailPanel("detailPanel", feedBackPanel, containerForm, arkCrudContainerVO);
		detailPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(detailPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		searchPanel = new SearchPanel("searchPanel", feedBackPanel, containerForm, cpModel, arkCrudContainerVO);
		searchPanel.initialisePanel();
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}
	
	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		searchResultsPanel = new SearchResultsPanel("searchResultsPanel", containerForm, arkCrudContainerVO);
		searchResultsPanel.initialisePanel();
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultsPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}
}