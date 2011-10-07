package au.org.theark.phenotypic.web.component.customfieldgroup;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldGroupVO;
import au.org.theark.phenotypic.web.component.customfieldgroup.form.DetailForm;

/**
 * @author nivedann
 *
 */
public class CustomFieldGroupDetailPanel extends Panel {
	
	private FeedbackPanel		feedBackPanel;
	private ArkCrudContainerVO	arkCrudContainerVO;
	private CompoundPropertyModel<CustomFieldGroupVO> cpmModel;
	private DetailForm detailForm;

	
	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public CustomFieldGroupDetailPanel(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO,CompoundPropertyModel<CustomFieldGroupVO> cpmModel){
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedBackPanel = feedBackPanel;
		this.cpmModel = cpmModel;
		initialisePanel();
	}
	
	public void initialisePanel() {
		detailForm = new DetailForm("detailsForm",feedBackPanel,cpmModel,arkCrudContainerVO);
		detailForm.initialiseDetailForm();
		add(detailForm);
	}

}
