package au.org.theark.phenotypic.web.component.customfieldgroup;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldGroupVO;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.phenotypic.web.component.customfieldgroup.form.DetailForm;

/**
 * @author nivedann
 *
 */
public class CustomFieldGroupDetailPanel extends Panel {
	

	private static final long serialVersionUID = 1L;
	private FeedbackPanel		feedBackPanel;
	private ArkCrudContainerVO	arkCrudContainerVO;
	private CompoundPropertyModel<CustomFieldGroupVO> cpmModel;
	private DetailForm detailForm;
	private Boolean addCustomFieldListPanel;
	private ArkDataProvider2<CustomFieldDisplay, CustomFieldDisplay> cfdProvider;
	
	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public CustomFieldGroupDetailPanel(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO,CompoundPropertyModel<CustomFieldGroupVO> cpmModel, ArkDataProvider2<CustomFieldDisplay, CustomFieldDisplay> cfdProvider, Boolean addCustomFieldDisplayList){
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedBackPanel = feedBackPanel;
		this.cpmModel = cpmModel;
		this.addCustomFieldListPanel = addCustomFieldDisplayList;
		this.cfdProvider = cfdProvider;
		initialisePanel();
	}
	
	public void initialisePanel() {
		detailForm = new DetailForm("detailsForm",feedBackPanel,cpmModel,arkCrudContainerVO,cfdProvider,addCustomFieldListPanel);
		detailForm.initialiseDetailForm();
		add(detailForm);
	}

}
