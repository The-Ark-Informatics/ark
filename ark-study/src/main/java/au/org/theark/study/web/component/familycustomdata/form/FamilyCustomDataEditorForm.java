package au.org.theark.study.web.component.familycustomdata.form;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.FamilyCustomFieldData;
import au.org.theark.core.vo.FamilyCustomDataVO;
import au.org.theark.core.web.component.customfield.dataentry.AbstractCustomDataEditorForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

public class FamilyCustomDataEditorForm extends AbstractCustomDataEditorForm<FamilyCustomDataVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService studyService;

	private ModalWindow modalWindow;

	public FamilyCustomDataEditorForm(String id, CompoundPropertyModel<FamilyCustomDataVO> cpModel, FeedbackPanel feedbackPanel, ModalWindow modalWindow) {
		super(id, cpModel, feedbackPanel);
		this.modalWindow=modalWindow;
		this.setMultiPart(true);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onEditSave(AjaxRequestTarget target, Form<?> form) {
		List<FamilyCustomFieldData> errorList = studyService.createOrUpdateFamilyCustomFieldData(cpModel.getObject().getCustomFieldDataList());
		if (errorList.size() > 0) {
			for (FamilyCustomFieldData familyCustomFieldData : errorList) {
				CustomField cf = familyCustomFieldData.getCustomFieldDisplay().getCustomField();
				String fieldType = cf.getFieldType().getName();
				if (fieldType.equals(au.org.theark.core.web.component.customfield.Constants.DATE_FIELD_TYPE_NAME)) {
					this.error("Unable to save this data: " + cf.getFieldLabel() + " = " + familyCustomFieldData.getDateDataValue());
				} else {
					this.error("Unable to save this data: " + cf.getFieldLabel() + " = " + familyCustomFieldData.getTextDataValue());
				}
			}
		} else {
			this.info("Successfully saved all edits");
		}
		/*
		 * Need to update the dataView, which forces a refresh of the model
		 * objects from backend. This is because deleted fields still remain in
		 * the model, and are stale objects if we try to use them for future
		 * saves.
		 */
		target.add(dataViewWMC);
		target.add(feedbackPanel);
		
	}
	
	@Override
	public void onEditCancel(AjaxRequestTarget target, Form<?> form) {
		// TODO Auto-generated method stub
		if (this.modalWindow != null) {
			this.modalWindow.close(target);
		} else {
			super.onEditCancel(target, form);
		}
	}

}
