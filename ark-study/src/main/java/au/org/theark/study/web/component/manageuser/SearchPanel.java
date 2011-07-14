package au.org.theark.study.web.component.manageuser;

import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.manageuser.form.ContainerForm;
import au.org.theark.study.web.component.manageuser.form.SearchForm;

public class SearchPanel extends Panel{
	
	private FeedbackPanel feedBackPanel;
	private ArkCrudContainerVO arkCrudContainerVO;
	private ContainerForm containerForm;
	private PageableListView<ArkUserVO> pageableListView;
	/**
	 * Constructor
	 * @param id
	 * @param arkCrudContainerVO
	 * @param feedbackPanel
	 * @param containerForm
	 */
	public SearchPanel(String id, ArkCrudContainerVO arkCrudContainerVO,FeedbackPanel feedbackPanel,ContainerForm containerForm,PageableListView<ArkUserVO> pageableListView){
		super(id);
		this.feedBackPanel = feedbackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
		this.pageableListView  = pageableListView;
	}
	
	public void initialisePanel(CompoundPropertyModel<ArkUserVO> arkUserVOCPM){
		SearchForm searchForm = new SearchForm(Constants.SEARCH_FORM,arkUserVOCPM,arkCrudContainerVO,feedBackPanel,containerForm,pageableListView);
		add(searchForm);
	}
	

}
