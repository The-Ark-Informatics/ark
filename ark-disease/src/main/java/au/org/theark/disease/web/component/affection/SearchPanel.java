package au.org.theark.disease.web.component.affection;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.Constants;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.disease.vo.AffectionListVO;
import au.org.theark.disease.vo.AffectionVO;
import au.org.theark.disease.web.component.affection.form.ContainerForm;
import au.org.theark.disease.web.component.affection.form.SearchForm;

public class SearchPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	private FeedbackPanel feedBackPanel;
	private PageableListView<AffectionVO> listView;
	private ArkCrudContainerVO arkCrudContainerVO;
	private ContainerForm containerForm;
	
	public SearchPanel(String id, FeedbackPanel feedBackPanel, PageableListView<AffectionVO> listView, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id);
		this.listView = listView;
		this.feedBackPanel = feedBackPanel;
		this.containerForm = containerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;
	}
	
	public void initialisePanel(CompoundPropertyModel<AffectionVO> affectionVOCPM) {
		SearchForm searchForm = new SearchForm(Constants.SEARCH_FORM, affectionVOCPM, listView, feedBackPanel, arkCrudContainerVO, containerForm);
		add(searchForm);
	}
	
}
