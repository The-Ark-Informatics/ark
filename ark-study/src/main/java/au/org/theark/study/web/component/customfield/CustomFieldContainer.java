/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.customfield;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.study.entity.SubjectCustmFld;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.web.component.customfield.form.ContainerForm;


/**
 * @author nivedann
 *
 */
public class CustomFieldContainer  extends  AbstractContainerPanel<CustomFieldVO>{

	private ContainerForm containerForm;
	
	private  SearchPanel searchPanel;
	private SearchResultListPanel searchResultListPanel;
	private DetailPanel detailPanel;
	private PageableListView<SubjectCustmFld> pageableListView;
	
	/**
	 * @param id
	 */
	public CustomFieldContainer(String id) {
		
		super(id, true);//call the new constructor

		cpModel = new CompoundPropertyModel<CustomFieldVO>(new CustomFieldVO());
		containerForm = new ContainerForm("containerForm",cpModel);
		containerForm.add(initialiseFeedBackPanel());
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
		
		SearchPanel searchPanel = new SearchPanel("searchComponentPanel", feedBackPanel, pageableListView, containerForm,arkCrudContainerVO);
		
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchResults()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
