package au.org.theark.core.web.component.customfield.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.FieldType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.UnitType;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.web.component.customfield.Constants;
import au.org.theark.core.web.component.customfield.DetailPanel;
import au.org.theark.core.web.form.AbstractSearchForm;

/**
 * @author elam
 * 
 */
@SuppressWarnings({ "serial", "unchecked" })
public class SearchForm extends AbstractSearchForm<CustomFieldVO> {

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	private CompoundPropertyModel<CustomFieldVO>	cpModel;
	private ArkCrudContainerVO 				arkCrudContainerVO;

	private TextField<String>					fieldIdTxtFld;
	private TextField<String>					fieldNameTxtFld;
	private DropDownChoice<FieldType>		fieldTypeDdc;
	private TextArea<String>					fieldDescriptionTxtAreaFld;
	private TextField<String>					fieldUnitsTxtFld;
	private TextField<String>					fieldMinValueTxtFld;
	private TextField<String>					fieldMaxValueTxtFld;
	
	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<CustomFieldVO> cpModel, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO) {

		super(id, cpModel, feedBackPanel, arkCrudContainerVO);

		this.cpModel = cpModel;
		this.feedbackPanel = feedBackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		initialiseFieldForm();

		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a study", arkCrudContainerVO);
	}

	private void initFieldTypeDdc() {
		java.util.Collection<FieldType> fieldTypeCollection = iArkCommonService.getFieldTypes();
		ChoiceRenderer fieldTypeRenderer = new ChoiceRenderer(Constants.FIELDTYPE_NAME, Constants.FIELDTYPE_ID);
		fieldTypeDdc = new DropDownChoice<FieldType>(Constants.FIELDVO_CUSTOMFIELD_FIELD_TYPE, (List) fieldTypeCollection, fieldTypeRenderer);
	}

	public void initialiseFieldForm() {
		fieldIdTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELD_ID);
		fieldNameTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELD_NAME);
		fieldDescriptionTxtAreaFld = new TextArea<String>(Constants.FIELDVO_CUSTOMFIELD_DESCRIPTION);
		fieldUnitsTxtFld = new AutoCompleteTextField<String>(Constants.FIELDVO_CUSTOMFIELD_UNIT_TYPE + ".name") {
			@Override
			protected Iterator getChoices(String input) {
				UnitType unitTypeCriteria = new UnitType();
				unitTypeCriteria.setName(input);
				unitTypeCriteria.setArkModule(cpModel.getObject().getCustomField().getArkModule());
				return iArkCommonService.getUnitTypeNames(unitTypeCriteria, 10).iterator();
			}
		};
		fieldMinValueTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELD_MIN_VALUE);
		fieldMaxValueTxtFld = new TextField<String>(Constants.FIELDVO_CUSTOMFIELD_MAX_VALUE);
		initFieldTypeDdc();
		addFieldComponents();
	}

	private void addFieldComponents() {
		add(fieldIdTxtFld);
		add(fieldNameTxtFld);
		add(fieldTypeDdc);
		add(fieldDescriptionTxtAreaFld);
		add(fieldUnitsTxtFld);
		add(fieldMinValueTxtFld);
		add(fieldMaxValueTxtFld);
	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		target.addComponent(feedbackPanel);
		final Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		// Get a list of all Fields for the Study in context
		Study study = iArkCommonService.getStudy(sessionStudyId);
		getModelObject().getCustomField().setStudy(study);

		int count = iArkCommonService.getCustomFieldCount(getModelObject().getCustomField());
		if (count <= 0) {
			this.info("Fields with the specified criteria does not exist in the system.");
			target.addComponent(feedbackPanel);
		}

		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.addComponent(arkCrudContainerVO.getSearchResultPanelContainer());
	}

	// Reset button implemented in AbstractSearchForm

	@Override
	protected void onNew(AjaxRequestTarget target) {
		// Due to ARK-108 :: No longer reset the VO onNew(..)
//		CustomFieldVO fieldVo = getModelObject();
//		fieldVo.getCustomField().setId(null); // must ensure Id is blank onNew

		// Set study for the new field
//		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
//		Study study = iArkCommonService.getStudy(studyId);
//		fieldVo.getCustomField().setStudy(study);

		CustomField cf = getModelObject().getCustomField();
		CompoundPropertyModel<CustomFieldVO> newModel = new CompoundPropertyModel<CustomFieldVO>(new CustomFieldVO());
		CustomField newCF = newModel.getObject().getCustomField();
		// Copy all the customField attributes across from the SearchForm
		newCF.setName(cf.getName());
		newCF.setFieldType(cf.getFieldType());
		newCF.setDescription(cf.getDescription());
		newCF.setUnitType(cf.getUnitType());
		newCF.setMinValue(cf.getMinValue());
		newCF.setMaxValue(cf.getMaxValue());
		newModel.getObject().setUseCustomFieldDisplay(getModelObject().isUseCustomFieldDisplay());

		DetailPanel detailsPanel = new DetailPanel("detailsPanel", feedbackPanel, newModel, arkCrudContainerVO);
		arkCrudContainerVO.getDetailPanelContainer().addOrReplace(detailsPanel);
		
		preProcessDetailPanel(target, arkCrudContainerVO);
	}
	
	@Override
	protected void onBeforeRender() {
		// Ensure that the UnitType object is instantiated for searches
		getModelObject().getCustomField().setUnitType(new UnitType());
		super.onBeforeRender();
	}

}