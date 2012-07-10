package au.org.theark.worktracking.web.component.billableitemtype;

import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.worktracking.entity.BillableItemType;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.worktracking.model.vo.BillableItemTypeVo;
import au.org.theark.worktracking.util.Constants;
import au.org.theark.worktracking.web.component.billableitemtype.form.SearchForm;
import au.org.theark.worktracking.web.component.billableitemtype.form.ContainerForm;

public class SearchPanel extends Panel {

	private static final long				serialVersionUID	= 1L;

	private ArkCrudContainerVO				arkCrudContainerVO;
	private FeedbackPanel					feedBackPanel;
	private PageableListView<BillableItemType>	listView;

	public SearchPanel(String id, ArkCrudContainerVO crudContainerVO, FeedbackPanel feedBackPanel, ContainerForm containerForm, PageableListView<BillableItemType> listView) {
		super(id);
		arkCrudContainerVO = crudContainerVO;
		this.feedBackPanel = feedBackPanel;
		this.listView = listView;
	}

	public void initialisePanel(CompoundPropertyModel<BillableItemTypeVo> studyCompCpm) {

		SearchForm searchStudyCompForm = new SearchForm(Constants.SEARCH_FORM, studyCompCpm, arkCrudContainerVO, feedBackPanel, listView);
		add(searchStudyCompForm);
	}

}