package au.org.theark.phenotypic.web.component.customfieldgroup.form;

import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldGroupVO;
import au.org.theark.core.web.component.palette.ArkPalette;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;

/**
 * @author nivedann
 *
 */
public class DetailForm extends AbstractDetailForm<CustomFieldGroupVO>{

	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService	iPhenotypicService;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	
	private TextField<String> customFieldGroupTxtFld;
	private TextArea<String> description;
	private Palette<CustomField> customFieldPalette;
	private CheckBox publishedStatusCb;				
	
	/**
	 * @param id
	 * @param feedBackPanel
	 * @param cpModel
	 * @param arkCrudContainerVO
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel,CompoundPropertyModel<CustomFieldGroupVO> cpModel,ArkCrudContainerVO arkCrudContainerVO) {
		super(id, feedBackPanel, cpModel, arkCrudContainerVO);
	}
	
	public void initialiseDetailForm(){
		customFieldGroupTxtFld = new TextField<String>("customFieldGroup.name");
		description = new TextArea<String>("customFieldGroup.description");
		publishedStatusCb = new CheckBox("customFieldGroup.published");
		initialiseArkModulePalette();
		addDetailFormComponents();
		attachValidators();
	}
	
	private void initialiseArkModulePalette() {
		
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("name", "name");
		PropertyModel<Collection<CustomField>> selectedPm = new PropertyModel<Collection<CustomField>>(cpModel, "selectedCustomFields");
		PropertyModel<Collection<CustomField>> availablePm = new PropertyModel<Collection<CustomField>>(cpModel, "availableCustomFields");
		customFieldPalette = new ArkPalette("selectedCustomFields", selectedPm, availablePm, renderer,au.org.theark.core.Constants.PALETTE_ROWS, false);
	}
	

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		customFieldGroupTxtFld.setRequired(true).setLabel(new StringResourceModel("customFieldGroup.name", this, new Model<String>("Custom Field Group Name")));
		customFieldGroupTxtFld.add(StringValidator.maximumLength(1000));
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		Boolean flag = false;
		if( getModelObject().getCustomFieldGroup() != null && getModelObject().getCustomFieldGroup().getId() == null){
			flag= true;
		}else{
			this.error("There was an internal error. Please contact Administrator.");
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<CustomFieldGroupVO> containerForm,	AjaxRequestTarget target) {
		
		if(getModelObject().getCustomFieldGroup().getId() == null){
			//Create
			
			ArkFunction arkFunction  = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);
			Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			Study study = iArkCommonService.getStudy(studyId);
			getModelObject().getCustomFieldGroup().setArkFunction(arkFunction);
			getModelObject().getCustomFieldGroup().setStudy(study);
			iPhenotypicService.createCustomFieldGroup(getModelObject());
			this.info("Custom Field Group has been created successfully.");
			
			
		}else{
			//Update
		}
		onSavePostProcess(target);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#addDetailFormComponents()
	 */
	@Override
	protected void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(customFieldGroupTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(description);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(customFieldPalette);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(publishedStatusCb);
		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}
	

}
