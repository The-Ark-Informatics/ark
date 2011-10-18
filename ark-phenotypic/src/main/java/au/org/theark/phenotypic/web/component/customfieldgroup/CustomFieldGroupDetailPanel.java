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
	private Boolean addCustomFieldListPanel;
	
	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public CustomFieldGroupDetailPanel(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO,CompoundPropertyModel<CustomFieldGroupVO> cpmModel,Boolean addCustomFieldDisplayList){
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedBackPanel = feedBackPanel;
		this.cpmModel = cpmModel;
		this.addCustomFieldListPanel = addCustomFieldDisplayList;
		initialisePanel();
	}
	
	public void initialisePanel() {
		detailForm = new DetailForm("detailsForm",feedBackPanel,cpmModel,arkCrudContainerVO,addCustomFieldListPanel);
		detailForm.initialiseDetailForm();
		add(detailForm);
	}

}
