package au.org.theark.genomics.web.component.datacenter;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.genomics.model.vo.DataSourceVo;
import au.org.theark.genomics.util.Constants;
import au.org.theark.genomics.web.component.datacenter.form.SourceForm;

public class DataSourcePanel extends Panel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SourceForm sourceForm;
	private FeedbackPanel feedBackPanel;
	private ArkCrudContainerVO arkCrudContainerVO;
	private AbstractDetailModalWindow modalWindow;

	public DataSourcePanel(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, AbstractDetailModalWindow modalWindow) {
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.modalWindow = modalWindow;
	}

	public void initialisePanel(CompoundPropertyModel<DataSourceVo> dataSourceCpm) {
		sourceForm = new SourceForm(Constants.DATA_SOURCE_FORM, feedBackPanel, dataSourceCpm, arkCrudContainerVO, modalWindow);
		sourceForm.initialiseSourceForm();
		add(sourceForm);
	}

	public SourceForm getSourceForm() {
		return sourceForm;
	}

	public void setSourceForm(SourceForm sourceForm) {
		this.sourceForm = sourceForm;
	}

}