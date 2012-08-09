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
package au.org.theark.study.web.component.subjectcustomdata.form;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.SubjectCustomFieldData;
import au.org.theark.core.web.component.customfield.dataentry.AbstractCustomDataEditorForm;
import au.org.theark.study.model.vo.SubjectCustomDataVO;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author elam
 * 
 */
@SuppressWarnings("serial")
public class CustomDataEditorForm extends AbstractCustomDataEditorForm<SubjectCustomDataVO> {

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService	studyService;

	public CustomDataEditorForm(String id, CompoundPropertyModel<SubjectCustomDataVO> cpModel, FeedbackPanel feedbackPanel) {
		super(id, cpModel, feedbackPanel);
	}

	public void onEditSave(AjaxRequestTarget target, Form<?> form) {
		
		List<SubjectCustomFieldData> errorList = studyService.createOrUpdateSubjectCustomFieldData(cpModel.getObject().getCustomFieldDataList());
		if (errorList.size() > 0) {
			for (SubjectCustomFieldData subjectCustomFieldData : errorList) {
				CustomField cf = subjectCustomFieldData.getCustomFieldDisplay().getCustomField();
				String fieldType = cf.getFieldType().getName();
				if (fieldType.equals(au.org.theark.core.web.component.customfield.Constants.DATE_FIELD_TYPE_NAME)) {
					this.error("Unable to save this data: " + cf.getFieldLabel() + " = " + subjectCustomFieldData.getDateDataValue());
				}
				else {
					this.error("Unable to save this data: " + cf.getFieldLabel() + " = " + subjectCustomFieldData.getTextDataValue());
				}
			}
		}
		else {
			this.info("Successfully saved all edits");
		}
		/*
		 * Need to update the dataView, which forces a refresh of the model objects from backend. This is because deleted fields still remain in the
		 * model, and are stale objects if we try to use them for future saves.
		 */
		target.add(dataViewWMC);
		target.add(feedbackPanel);
	}
}
