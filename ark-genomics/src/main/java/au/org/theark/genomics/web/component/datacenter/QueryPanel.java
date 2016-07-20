package au.org.theark.genomics.web.component.datacenter;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.genomics.model.vo.DataCenterVo;
import au.org.theark.genomics.util.Constants;
import au.org.theark.genomics.web.component.datacenter.form.QueryForm;

public class QueryPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private QueryForm queryForm;
	private FeedbackPanel feedBackPanel;
	private ArkCrudContainerVO arkCrudContainerVO;
	private AbstractDetailModalWindow modalWindow;

	public QueryPanel(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, AbstractDetailModalWindow modalWindow) {
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.modalWindow = modalWindow;
	}

	public void initialisePanel(CompoundPropertyModel<DataCenterVo> dataSourceCpm) {
		queryForm = new QueryForm(Constants.QUERY_FORM, feedBackPanel, dataSourceCpm, arkCrudContainerVO, modalWindow);
		queryForm.initialiseQueryForm();
		add(queryForm);
	}

	public QueryForm getQueryForm() {
		return queryForm;
	}

	public void setSourceForm(QueryForm sourceForm) {
		this.queryForm = sourceForm;
	}

}
