package au.org.theark.disease.web.component.disease;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.Constants;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.DiseaseVO;
import au.org.theark.disease.web.component.disease.form.SearchForm;

public class SearchPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	private FeedbackPanel feedBackPanel;
	private PageableListView<DiseaseVO> listView;
	private ArkCrudContainerVO arkCrudContainerVO;
	protected WebMarkupContainer arkContextMarkup;
	
	public SearchPanel(String id, FeedbackPanel feedBackPanel, PageableListView<DiseaseVO> listView, ArkCrudContainerVO arkCrudContainerVO, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.listView = listView;
		this.feedBackPanel = feedBackPanel;
		this.arkContextMarkup = arkContextMarkup;
		this.arkCrudContainerVO = arkCrudContainerVO;
	}
	
	public void initialisePanel(CompoundPropertyModel<DiseaseVO> diseaseVOCPM) {
		SearchForm searchForm = new SearchForm(Constants.SEARCH_FORM, diseaseVOCPM, listView, feedBackPanel, arkCrudContainerVO, arkContextMarkup);
		add(searchForm);
	}
	
}
