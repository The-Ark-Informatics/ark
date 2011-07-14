/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.customfield;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectCustmFld;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.customfield.form.ContainerForm;


/**
 * @author nivedann
 *
 */
public class CustomFieldContainer  extends  AbstractContainerPanel<CustomFieldVO>{

	private ContainerForm containerForm;
	
	private SearchPanel searchPanel;
	private SearchResultListPanel searchResultListPanel;
	private DetailPanel detailPanel;
	private PageableListView<SubjectCustmFld> pageableListView;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService iArkCommonService;
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	
	/**
	 * @param id
	 */
	public CustomFieldContainer(String id) {
		
		super(id, true);//call the new constructor

		cpModel = new CompoundPropertyModel<CustomFieldVO>(new CustomFieldVO());
		containerForm = new ContainerForm("containerForm",cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		add(containerForm);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseDetailPanel()
	 */
	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchPanel()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		
		SearchPanel searchPanel = new SearchPanel("searchComponentPanel", 
													feedBackPanel,
													pageableListView, 
													containerForm,
													detailPanel,
													arkCrudContainerVO);
		
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchResults()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		
		searchResultListPanel = new SearchResultListPanel("searchResults",containerForm,arkCrudContainerVO);
		
		
		iModel = new LoadableDetachableModel<Object>() {
			@Override
			protected Object load() {
				Collection<SubjectCustmFld> fieldList = new ArrayList<SubjectCustmFld>();
				
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				Study study =	iArkCommonService.getStudy(sessionStudyId);
				//Get the list of Study Related Custom Fields
				containerForm.getModelObject().getCustomField().setStudy(study);
				SubjectCustmFld customField = containerForm.getModelObject().getCustomField();
				fieldList = studyService.searchStudyFields(customField);
				pageableListView.removeAll();
				return fieldList;
			}
		};
		
		pageableListView = searchResultListPanel.buildPageableListView(iModel);
		pageableListView.setReuseItems(true);
		
		//Build Navigator
		PagingNavigator pageNavigator = new PagingNavigator("navigator", pageableListView);
		searchResultListPanel.add(pageNavigator);
		searchResultListPanel.add(pageableListView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultListPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}
	

}
