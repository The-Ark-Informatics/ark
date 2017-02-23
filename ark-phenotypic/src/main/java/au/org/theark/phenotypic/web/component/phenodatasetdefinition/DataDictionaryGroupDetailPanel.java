package au.org.theark.phenotypic.web.component.phenodatasetdefinition;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.PhenoDataSetFieldGroupVO;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.phenotypic.web.component.phenodatasetdefinition.form.DetailForm;

/**
 * @author nivedann
 *
 */
public class DataDictionaryGroupDetailPanel extends Panel {
	

	private static final long serialVersionUID = 1L;
	private FeedbackPanel		feedBackPanel;
	private ArkCrudContainerVO	arkCrudContainerVO;
	private CompoundPropertyModel<PhenoDataSetFieldGroupVO> cpmModel;
	private DetailForm detailForm;
	private Boolean addCustomFieldListPanel;
	private ArkDataProvider2<PhenoDataSetFieldDisplay, PhenoDataSetFieldDisplay> cfdProvider;
	
	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public DataDictionaryGroupDetailPanel(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO,CompoundPropertyModel<PhenoDataSetFieldGroupVO> cpmModel, 
			ArkDataProvider2<PhenoDataSetFieldDisplay, PhenoDataSetFieldDisplay> cfdProvider, Boolean addCustomFieldDisplayList){
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
