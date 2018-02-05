package au.org.theark.genomics.web.component.datacenter;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.spark.entity.DataSource;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.genomics.model.vo.DataCenterVo;
import au.org.theark.genomics.model.vo.DataSourceVo;
import au.org.theark.genomics.util.Constants;
import au.org.theark.genomics.web.component.datacenter.form.ContainerForm;
import au.org.theark.genomics.web.component.datacenter.form.SearchForm;

public class SearchPanel extends Panel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArkCrudContainerVO				arkCrudContainerVO;
	private FeedbackPanel					feedBackPanel;
	private PageableListView<DataSourceVo>	listView;
	private PageableListView<DataSource>	sourceView;
	private WebMarkupContainer	        dataSourcePanelContainer;


	public SearchPanel(String id, ArkCrudContainerVO crudContainerVO, FeedbackPanel feedBackPanel, ContainerForm containerForm, PageableListView<DataSourceVo> listView,PageableListView<DataSource> sourceView, WebMarkupContainer dataSourcePanelContainer ) {
		super(id);
		arkCrudContainerVO = crudContainerVO;
		this.feedBackPanel = feedBackPanel;
		this.listView = listView;
		this.sourceView = sourceView;
		this.dataSourcePanelContainer = dataSourcePanelContainer;
	}

	public void initialisePanel(CompoundPropertyModel<DataCenterVo> dataCenterCpm) {
		SearchForm searchDataCenterForm = new SearchForm(Constants.SEARCH_FORM, dataCenterCpm, arkCrudContainerVO, feedBackPanel, listView, sourceView,dataSourcePanelContainer);
		add(searchDataCenterForm);
	}

}
