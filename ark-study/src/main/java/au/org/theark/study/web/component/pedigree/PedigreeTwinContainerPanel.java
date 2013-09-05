package au.org.theark.study.web.component.pedigree;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.study.model.vo.PedigreeVo;
import au.org.theark.study.model.vo.RelationshipVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.pedigree.form.ContainerForm;

public class PedigreeTwinContainerPanel extends AbstractContainerPanel<PedigreeVo> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	protected WebMarkupContainer	savePanelContainer;
	
	protected AbstractDetailModalWindow modalWindow;
	
	private SavePanel								savePanel;
	
	private TwinSearchResultsListPanel		searchResultPanel;

	private PageableListView<RelationshipVo>	pageableListView;

	private ContainerForm							containerForm;

	@SpringBean(name = Constants.STUDY_SERVICE)
	IStudyService studyService;


	public PedigreeTwinContainerPanel(String id,AbstractDetailModalWindow modalWindow) {
		super(id);
		// TODO Auto-generated constructor stub
		
		this.modalWindow = modalWindow;
		
		cpModel = new CompoundPropertyModel<PedigreeVo>(new PedigreeVo());

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);

		containerForm.add(initialiseFeedBackPanel());
		
		containerForm.add(initialiseSearchResults());		

		containerForm.add(initialiseSavePanel());
		
		add(containerForm);
	}

	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		searchResultPanel = new TwinSearchResultsListPanel("searchResults",arkCrudContainerVO,containerForm);
		searchResultPanel.setOutputMarkupId(true);
		
		iModel = new LoadableDetachableModel<Object>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {
				Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				String subjectUID= (String)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);			
				containerForm.getModelObject().setRelationshipList(studyService.getSubjectPedigreeTwinList(subjectUID,studyId));
				pageableListView.removeAll();
				return containerForm.getModelObject().getRelationshipList();
			}
		};

		pageableListView = searchResultPanel.buildPageableListView(iModel);
		pageableListView.setReuseItems(true);
//		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", pageableListView);
//		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(pageableListView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}
	
	
	protected WebMarkupContainer initialiseSavePanel() {
		savePanelContainer = new WebMarkupContainer("saveContainer");
		savePanelContainer.setOutputMarkupPlaceholderTag(true);
		
		savePanel = new SavePanel("saveComponentPanel",feedBackPanel,arkCrudContainerVO, modalWindow);
		savePanel.setOutputMarkupId(true);
		savePanel.initialisePanel(cpModel);
		savePanelContainer.add(savePanel);
		
		return savePanelContainer;
	}

	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
