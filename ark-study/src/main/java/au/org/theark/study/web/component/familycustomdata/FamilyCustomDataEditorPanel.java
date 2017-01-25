package au.org.theark.study.web.component.familycustomdata;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.CustomFieldType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.CustomFieldCategoryOrderingHelper;
import au.org.theark.core.web.component.customfield.dataentry.AbstractCustomDataEditorForm;
import au.org.theark.study.model.vo.FamilyCustomDataVO;
import au.org.theark.study.web.component.familycustomdata.form.FamilyCustomDataEditorForm;

public class FamilyCustomDataEditorPanel extends Panel {

	private static final long serialVersionUID = -1L;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService<Void>							iArkCommonService;
	
	protected CompoundPropertyModel<FamilyCustomDataVO> cpModel;

	protected FeedbackPanel feedbackPanel;
	protected AbstractCustomDataEditorForm<FamilyCustomDataVO> customDataEditorForm;
	protected FamilyCustomDataDataViewPanel dataViewPanel;
	protected Label warnSaveLabel;
	protected ModalWindow modalWindow;
	private DropDownChoice<CustomFieldCategory>		customeFieldCategoryDdc;

	public FamilyCustomDataEditorPanel(String id, CompoundPropertyModel<FamilyCustomDataVO> cpModel, FeedbackPanel feedBackPanel,ModalWindow modalWindow) {
		super(id);

		this.cpModel = cpModel;
		this.feedbackPanel = feedBackPanel;
		this.modalWindow=modalWindow;
	}

	public FamilyCustomDataEditorPanel initialisePanel() {

		customDataEditorForm = new FamilyCustomDataEditorForm("customDataEditorForm", cpModel, feedbackPanel,modalWindow).initialiseForm(true);
		Collection<CustomFieldCategory> customFieldCategoryCollection=getAvailableAllCategoryListInStudyByCustomFieldType();
		List<CustomFieldCategory> customFieldCatLst=CustomFieldCategoryOrderingHelper.getInstance().orderHierarchicalyCustomFieldCategories((List<CustomFieldCategory>)customFieldCategoryCollection);
		ChoiceRenderer customfieldCategoryRenderer = new ChoiceRenderer(Constants.CUSTOMFIELDCATEGORY_NAME, Constants.CUSTOMFIELDCATEGORY_ID){
						@Override
						public Object getDisplayValue(Object object) {
						CustomFieldCategory cuscat=(CustomFieldCategory)object;
							return CustomFieldCategoryOrderingHelper.getInstance().preTextDecider(cuscat)+ super.getDisplayValue(object);
						}
		};
		customeFieldCategoryDdc = new DropDownChoice<CustomFieldCategory>(Constants.FIELDVO_CUSTOMFIELD_CUSTOEMFIELDCATEGORY, customFieldCatLst, customfieldCategoryRenderer);
		customeFieldCategoryDdc.setOutputMarkupId(true);
		customeFieldCategoryDdc.setNullValid(true);
		customeFieldCategoryDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				
				customDataEditorForm.getDataViewWMC().remove(dataViewPanel);
				dataViewPanel = new FamilyCustomDataDataViewPanel("dataViewPanel", cpModel).initialisePanel(null,customeFieldCategoryDdc.getModelObject());
				customDataEditorForm.getDataViewWMC().add(dataViewPanel);
				target.add(dataViewPanel);
				target.add(customDataEditorForm);
			}
		});
		
		
		dataViewPanel = new FamilyCustomDataDataViewPanel("dataViewPanel", cpModel).initialisePanel(null, customeFieldCategoryDdc.getModelObject());
		
		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", dataViewPanel.getDataView()) {
			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(customDataEditorForm.getDataViewWMC());
				target.add(this);
			}
		};
		pageNavigator.setVisible(false);
		customDataEditorForm.add(customeFieldCategoryDdc);
		customDataEditorForm.getDataViewWMC().add(dataViewPanel);

		warnSaveLabel = new Label("warnSaveLabel", new ResourceModel("warnSaveLabel"));
		warnSaveLabel.setVisible(ArkPermissionHelper.isActionPermitted(Constants.NEW));

		this.add(customDataEditorForm);
		this.add(pageNavigator);
		this.add(warnSaveLabel);

		return this;
	}
	/**
	 * Get custom field category collection from model.
	 * @return
	 */
	private Collection<CustomFieldCategory> getAvailableAllCategoryListInStudyByCustomFieldType(){
		
		Study study =cpModel.getObject().getLinkSubjectStudy().getStudy();
		
		ArkFunction arkFunction=iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD_CATEGORY);
		
		CustomFieldType customFieldType=iArkCommonService.getCustomFieldTypeByName(au.org.theark.core.Constants.FAMILY);
		Collection<CustomFieldCategory> customFieldCategoryCollection = null;
		try {
			customFieldCategoryCollection =  iArkCommonService.getAvailableAllCategoryListInStudyByCustomFieldType(study,arkFunction, customFieldType);
		} catch (ArkSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return customFieldCategoryCollection;
	}

}
