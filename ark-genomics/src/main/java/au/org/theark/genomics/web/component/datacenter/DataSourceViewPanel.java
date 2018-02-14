package au.org.theark.genomics.web.component.datacenter;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.genomics.model.vo.DataSourceVo;

public class DataSourceViewPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private FeedbackPanel feedBackPanel;
	private ArkCrudContainerVO arkCrudContainerVO;
	
	Label label;


	public DataSourceViewPanel(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;

	}

	public void initialisePanel(CompoundPropertyModel<DataSourceVo> dataSourceCpm) {

		this.label = new Label("source.message", "------------------------------------  "+dataSourceCpm.getObject().getPath()+ "--------------------");
		add(label);
	}

}
