package au.org.theark.genomics.web.component.analysis;

import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.spark.entity.Analysis;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.genomics.model.vo.AnalysisVo;
import au.org.theark.genomics.util.Constants;
import au.org.theark.genomics.web.component.analysis.form.ContainerForm;
import au.org.theark.genomics.web.component.analysis.form.SearchForm;

public class SearchPanel extends Panel {
	private static final long				serialVersionUID	= 1L;

	private ArkCrudContainerVO				arkCrudContainerVO;
	private FeedbackPanel					feedBackPanel;
	private PageableListView<Analysis>	listView;


	public SearchPanel(String id, ArkCrudContainerVO crudContainerVO, FeedbackPanel feedBackPanel, ContainerForm containerForm, PageableListView<Analysis> listView) {
		super(id);
		arkCrudContainerVO = crudContainerVO;
		this.feedBackPanel = feedBackPanel;
		this.listView = listView;
	}

	public void initialisePanel(CompoundPropertyModel<AnalysisVo> studyCompCpm) {
		SearchForm searchStudyCompForm = new SearchForm(Constants.SEARCH_FORM, studyCompCpm, arkCrudContainerVO, feedBackPanel, listView);
		add(searchStudyCompForm);
	}
}