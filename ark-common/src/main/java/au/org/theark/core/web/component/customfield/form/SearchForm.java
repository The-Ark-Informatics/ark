/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.core.web.component.customfield.form;

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
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a study");
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
				unitTypeCriteria.setArkFunction(cpModel.getObject().getCustomField().getArkFunction());
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
		target.add(feedbackPanel);
		final Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		// Get a list of all Fields for the Study in context
		Study study = iArkCommonService.getStudy(sessionStudyId);
		getModelObject().getCustomField().setStudy(study);

		int count = iArkCommonService.getCustomFieldCount(getModelObject().getCustomField());
		if (count <= 0) {
			this.info("No records match the specified criteria.");
			target.add(feedbackPanel);
		}

		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}

	// Reset button implemented in AbstractSearchForm

	@Override
	protected void onNew(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		// Instead of having to reset the criteria, we just copy the criteria across
		CustomField cf = getModelObject().getCustomField();
		CompoundPropertyModel<CustomFieldVO> newModel = new CompoundPropertyModel<CustomFieldVO>(new CustomFieldVO());
		CustomField newCF = newModel.getObject().getCustomField();
		// Copy all the customField attributes across from the SearchForm
		newCF.setStudy(cf.getStudy());
		newCF.setArkFunction(cf.getArkFunction());
		newCF.setName(cf.getName());
		newCF.setFieldType(cf.getFieldType());
		newCF.setDescription(cf.getDescription());
		/* 
		 * NB: Do NOT copy unitType across because it is a Textfield on the SearchForm.
		 * If you copy this through, then DetailForm will have transient error during onSave(..).
		 * Also, if the user chooses fieldType==DATE, this and unitType is not a valid combination
		 * (but unitTypeDdc will be disabled, so the user can't make it null for it to be valid).
		 */
		newCF.setMinValue(cf.getMinValue());
		newCF.setMaxValue(cf.getMaxValue());
		newModel.getObject().setUseCustomFieldDisplay(getModelObject().isUseCustomFieldDisplay());
		
		DetailPanel detailPanel = new DetailPanel("detailPanel", feedbackPanel, newModel, arkCrudContainerVO);
		arkCrudContainerVO.getDetailPanelContainer().addOrReplace(detailPanel);
		
		// Reset model's CF object (do NOT replace the CustomFieldVO in the model)
		cf = new CustomField();
		cf.setStudy(newCF.getStudy());
		cf.setArkFunction(newCF.getArkFunction());
		getModelObject().setCustomField(cf);
		
		preProcessDetailPanel(target);
	}

}
