package au.org.theark.phenotypic.web.component.customfieldgroup.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldGroupVO;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.palette.ArkPalette;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.customfieldgroup.CustomFieldDisplayListPanel;

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
	private AjaxCheckBox publishedStatusCb;		
	private Boolean addCustomFieldDisplayList;
	private ArkDataProvider2<CustomFieldDisplay, CustomFieldDisplay> cfdProvider;
	private DataView<CustomFieldDisplay> dataView;

	
	
	/**
	 * @param id
	 * @param feedBackPanel
	 * @param cpModel
	 * @param arkCrudContainerVO
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel,CompoundPropertyModel<CustomFieldGroupVO> cpModel,ArkCrudContainerVO arkCrudContainerVO, ArkDataProvider2<CustomFieldDisplay, CustomFieldDisplay> cfdProvider, 
			Boolean addCustomFieldDisplayList) {
		super(id, feedBackPanel, cpModel, arkCrudContainerVO);
		this.addCustomFieldDisplayList = addCustomFieldDisplayList;
		this.cfdProvider = cfdProvider;
	}
	
	
	private void initialiseCFDListPanel(){
		
		
		cfdProvider.setCriteriaModel(new PropertyModel<CustomFieldDisplay>(cpModel, "customFieldGroup"));
		ArrayList<CustomField> selectedList  = (ArrayList)iPhenotypicService.getCustomFieldsLinkedToCustomFieldGroup(getModelObject().getCustomFieldGroup());
		Boolean disableEditButton = false;
		if(getModelObject().getCustomFieldGroup().getPublished()){
			for (CustomField customField : selectedList) {
				if(customField.getCustomFieldHasData()){
					disableEditButton = true;
					break;
				}
			}
		}
		
		CustomFieldDisplayListPanel cfdListPanel = new CustomFieldDisplayListPanel("cfdListPanel", feedBackPanel,arkCrudContainerVO,disableEditButton);
		cfdListPanel.setOutputMarkupId(true);
		cfdListPanel.initialisePanel();
		dataView = cfdListPanel.buildDataView(cfdProvider);
		dataView.setItemsPerPage(au.org.theark.core.Constants.ROWS_PER_PAGE);
		
		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("cfDisplayNavigator", dataView) {
			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(arkCrudContainerVO.getWmcForCustomFieldDisplayListPanel());
			}
		};
		cfdListPanel.addOrReplace(pageNavigator);
		cfdListPanel.addOrReplace(dataView);
		arkCrudContainerVO.getWmcForCustomFieldDisplayListPanel().addOrReplace(cfdListPanel);
	}
	
	public void initialiseDetailForm(){
		customFieldGroupTxtFld = new TextField<String>("customFieldGroup.name");
		description = new TextArea<String>("customFieldGroup.description");
		publishedStatusCb = new AjaxCheckBox("customFieldGroup.published", new PropertyModel<Boolean>(getModelObject(), "customFieldGroup.published")) {
			@Override
			protected void onUpdate(AjaxRequestTarget arg0) {
				//This onUpdate is not being invoked.
				if(publishedStatusCb.getModelObject().booleanValue()){
					//TODO
				}else{
					//TODO
				}
			}
		};
		if(addCustomFieldDisplayList){
			initialiseCFDListPanel();
		}else{
			arkCrudContainerVO.getWmcForCustomFieldDisplayListPanel().addOrReplace(new EmptyPanel("cfdListPanel"));
		}
		
		initialiseArkModulePalette();
		addDetailFormComponents();
		attachValidators();
	}
	
	private void initialiseArkModulePalette() {

		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("name", "name");
		Collection<CustomField>  list = cpModel.getObject().getSelectedCustomFields();
		
		List<CustomField> selectedListCF = cpModel.getObject().getSelectedCustomFields();
		
		PropertyModel<Collection<CustomField>> selectedPm = new PropertyModel<Collection<CustomField>>(cpModel, "selectedCustomFields");
		PropertyModel<Collection<CustomField>> availablePm = new PropertyModel<Collection<CustomField>>(cpModel, "availableCustomFields");
		customFieldPalette = new ArkPalette("selectedCustomFields", new ListModel(selectedListCF), availablePm, renderer,au.org.theark.core.Constants.PALETTE_ROWS, true );
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
		CustomFieldGroupVO vo  = getModelObject();
		if( getModelObject().getCustomFieldGroup() != null && getModelObject().getCustomFieldGroup().getId() == null){
			flag= true;
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		setModelObject(new CustomFieldGroupVO());
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {

		//Get a list of CustomFields for the give group
		ArrayList<CustomField> selectedList  = (ArrayList)iPhenotypicService.getCustomFieldsLinkedToCustomFieldGroup(getModelObject().getCustomFieldGroup());
		
		Boolean allowDelete = true;
		for (CustomField customField : selectedList) {
			if(customField.getCustomFieldHasData()){
				allowDelete = false;
				break;
			}
		}
		if(allowDelete){
			iPhenotypicService.deleteCustomFieldGroup(getModelObject());
			this.info("Custom Field Group has been deleted successfully.");	
			editCancelProcess(target);
			
		}else{
			this.error("This Questionnaire cannot be deleted.");
		}
		
		
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
			try {
				
				
				iPhenotypicService.createCustomFieldGroup(getModelObject());
				initialiseCFDListPanel();
				this.info("Custom Field Group has been created successfully.");	
			}
			catch (EntityExistsException e) {
				this.error("A Questionnaire with the same name already exisits. Please choose a unique one.");
			}
			catch (ArkSystemException e) {
				this.error("A System error occured. Please contact Administrator.");
			}
		}else{
			
			try {
					iPhenotypicService.updateCustomFieldGroup(getModelObject());
					initialiseCFDListPanel();
					this.info("Custom Field Group has been updated successfully.");	
				
			}catch (EntityExistsException e) {
				this.error("A Questionnaire with the same name already exisits. Please choose a unique one.");
				e.printStackTrace();
			} catch (ArkSystemException e) {
				this.error("A System error occured. Please contact Administrator.");
				e.printStackTrace();
			}
			
		}
		target.add(arkCrudContainerVO.getWmcForCustomFieldDisplayListPanel());//Repaint this List of Custom Field Displays
		onSavePostProcess(target);//Post process
		
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
		addOrReplace(arkCrudContainerVO.getWmcForCustomFieldDisplayListPanel());
		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}
	

}
