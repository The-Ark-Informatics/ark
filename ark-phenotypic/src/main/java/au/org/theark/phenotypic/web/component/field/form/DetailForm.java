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
package au.org.theark.phenotypic.web.component.field.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.model.pheno.entity.Field;
import au.org.theark.core.model.pheno.entity.FieldType;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.phenotypic.model.vo.FieldVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "unused" })
public class DetailForm extends AbstractDetailForm<FieldVO> {
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService				iPhenotypicService;

	private ContainerForm					fieldContainerForm;
	private TextField<String>				fieldIdTxtFld;
	private TextField<String>				fieldNameTxtFld;
	private DropDownChoice<FieldType>	fieldTypeDdc;

	private TextArea<String>				fieldDescriptionTxtAreaFld;
	private TextField<String>				fieldUnitsTxtFld;
	private TextField<String>				fieldMinValueTxtFld;
	private TextField<String>				fieldMaxValueTxtFld;
	private TextArea<String>				fieldEncodedValuesTxtFld;
	private TextField<String>				fieldMissingValueTxtFld;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param detailPanel
	 * @param listContainer
	 * @param detailsContainer
	 * @param containerForm
	 * @param viewButtonContainer
	 * @param editButtonContainer
	 * @param detailFormContainer
	 * @param searchPanelContainer
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {

		super(id, feedBackPanel, containerForm,arkCrudContainerVO);
	}

	private void initFieldTypeDdc() {
		java.util.Collection<FieldType> fieldTypeCollection = iPhenotypicService.getFieldTypes();
		ChoiceRenderer fieldTypeRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.FIELD_TYPE_NAME, au.org.theark.phenotypic.web.Constants.FIELD_TYPE_ID);
		fieldTypeDdc = new DropDownChoice<FieldType>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_FIELD_TYPE, (List) fieldTypeCollection, fieldTypeRenderer);
	}

	public void initialiseDetailForm() {
		fieldIdTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_ID);
		fieldNameTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_NAME);
		fieldNameTxtFld.add(new ArkDefaultFormFocusBehavior());
		fieldDescriptionTxtAreaFld = new TextArea<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_DESCRIPTION);
		fieldUnitsTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_UNITS);
		fieldMinValueTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MIN_VALUE);
		fieldMaxValueTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MAX_VALUE);
		fieldEncodedValuesTxtFld = new TextArea<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_ENCODED_VALUES);
		PropertyModel<Field> pm = new PropertyModel<Field>((CompoundPropertyModel<FieldVO>) containerForm.getModel(), "field");
		fieldMissingValueTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MISSING_VALUE);

		// Initialise Drop Down Choices
		initFieldTypeDdc();

		attachValidators();
		addDetailFormComponents();
	}

	/**
	 * A common method that can be used to render Yes/No using RadioChoice controls
	 * 
	 * @param study
	 * @param propertyModelExpr
	 * @param radioChoiceId
	 * @return
	 */
	private RadioChoice<Boolean> initRadioButtonChoice(PropertyModel<Field> pm, String propertyModelExpr, String radioChoiceId) {

		List<Boolean> list = new ArrayList<Boolean>();
		list.add(Boolean.TRUE);
		list.add(Boolean.FALSE);
		/* Implement the IChoiceRenderer */

		IChoiceRenderer<Boolean> radioChoiceRender = new IChoiceRenderer<Boolean>() {
			public Object getDisplayValue(final Boolean choice) {

				String displayValue = au.org.theark.core.Constants.NO;

				if (choice != null && choice.booleanValue()) {
					displayValue = au.org.theark.core.Constants.YES;
				}
				return displayValue;
			}

			public String getIdValue(final Boolean object, final int index) {
				return object.toString();
			}
		};

		PropertyModel<Boolean> propertyModel = new PropertyModel<Boolean>(pm, propertyModelExpr);
		return new RadioChoice<Boolean>(radioChoiceId, propertyModel, list, radioChoiceRender);
	}

	protected void attachValidators() {
		fieldNameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.phenotypic.name.required", this, new Model<String>("Name")));
		fieldTypeDdc.setRequired(true).setLabel(new StringResourceModel("error.phenotypic.fieldType.required", this, new Model<String>("Field Type")));
		// TODO: Add correct validator, possibly custom with better validation message
		// fieldEncodedValuesTxtFld.add(new PatternValidator("\\b[\\w]=[\\w];\\b*")).setLabel(new
		// StringResourceModel("error.phenotypic.encodedValues.validation", this, new Model<String>("Encoded Value definition")));
	}


	@Override
	protected void onSave(Form<FieldVO> containerForm, AjaxRequestTarget target) {

		if (containerForm.getModelObject().getField().getId() == null) {
			// Save the Field
			iPhenotypicService.createField(containerForm.getModelObject().getField());
			this.info("Field " + containerForm.getModelObject().getField().getName() + " was created successfully");
			processErrors(target);
		}
		else {
			// Update the Field
			iPhenotypicService.updateField(containerForm.getModelObject().getField());
			this.info("Field " + containerForm.getModelObject().getField().getName() + " was updated successfully");
			processErrors(target);
		}

		onSavePostProcess(target);
		// TODO:(CE) To handle Business and System Exceptions here
	}

	protected void onCancel(AjaxRequestTarget target) {
		FieldVO fieldVo = new FieldVO();
		containerForm.setModelObject(fieldVo);
		// onCancelPostProcess(target);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	/**
	 * 
	 */
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		try {
			iPhenotypicService.deleteField(containerForm.getModelObject().getField());
			this.info("Field " + containerForm.getModelObject().getField().getName() + " was deleted successfully");
		}
		catch (ArkSystemException e) {
			this.error(e.getMessage());
		}
		catch (EntityCannotBeRemoved e) {
			this.error(e.getMessage());
		}

		// Display delete confirmation message
		target.add(feedBackPanel);
		// TODO Implement Exceptions in PhentoypicService
		// } catch (UnAuthorizedOperation e) { this.error("You are not authorised to manage study components for the given study " +
		// study.getName()); processFeedback(target); } catch (ArkSystemException e) {
		// this.error("A System error occured, we will have someone contact you."); processFeedback(target); }

		
		// Move focus back to Search form
		FieldVO fieldVo = new FieldVO();
		containerForm.setModelObject(fieldVo);
		editCancelProcess(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getField().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#addDetailFormComponents()
	 */
	@Override
	protected void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(fieldIdTxtFld.setEnabled(false));
		arkCrudContainerVO.getDetailPanelFormContainer().add(fieldNameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fieldDescriptionTxtAreaFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fieldTypeDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fieldUnitsTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fieldMinValueTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fieldMaxValueTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fieldEncodedValuesTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fieldMissingValueTxtFld);
		add(arkCrudContainerVO.getDetailPanelFormContainer());
		
	}
}
