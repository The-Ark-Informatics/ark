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
package au.org.theark.lims.web.component.biocollectioncustomdata.form;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.lims.entity.BioCollectionCustomFieldData;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.web.component.customfield.dataentry.AbstractCustomDataEditorForm;
import au.org.theark.core.web.form.ArkFormVisitor;
import au.org.theark.lims.model.vo.BioCollectionCustomDataVO;
import au.org.theark.lims.service.ILimsService;

/**
 * @author elam
 * 
 */
@SuppressWarnings("serial")
public class CustomDataEditorForm extends AbstractCustomDataEditorForm<BioCollectionCustomDataVO> {

	private static final Logger log = LoggerFactory.getLogger(CustomDataEditorForm.class);

	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_SERVICE)
	private ILimsService				iLimsService;
	
	protected FeedbackPanel	feedbackPanel;
	protected WebMarkupContainer dataViewWMC;
	protected WebMarkupContainer buttonsPanelWMC;
	
	// Add a visitor class for required field marking/validation/highlighting
	protected ArkFormVisitor formVisitor = new ArkFormVisitor();

	public CustomDataEditorForm(String id, CompoundPropertyModel<BioCollectionCustomDataVO> cpModel, FeedbackPanel feedbackPanel) {
		super(id, cpModel, feedbackPanel);
	}

	public void onEditSave(AjaxRequestTarget target, Form<?> form) {
		List<BioCollectionCustomFieldData> errorList = iLimsService.createOrUpdateBioCollectionCustomFieldData(cpModel.getObject().getCustomFieldDataList());
		if (errorList.size() > 0) {
			for (BioCollectionCustomFieldData bioCollectionCustomFieldData : errorList) {
				CustomField cf = bioCollectionCustomFieldData.getCustomFieldDisplay().getCustomField();
				String fieldType = cf.getFieldType().getName();
				if (fieldType.equals(au.org.theark.core.web.component.customfield.Constants.DATE_FIELD_TYPE_NAME)) {
					this.error("Unable to save this data: " + cf.getFieldLabel() + " = " + bioCollectionCustomFieldData.getDateDataValue());
				}
				else {
					this.error("Unable to save this data: " + cf.getFieldLabel() + " = " + bioCollectionCustomFieldData.getTextDataValue());					
				}
			}
		}
		else {
			this.info("Successfully saved all edits");
		}
		/*
		 * Need to update the dataView, which forces a refresh of the model objects from backend.
		 * This is because deleted fields still remain in the model, and are stale objects if we 
		 * try to use them for future saves.
		 */
		target.addComponent(dataViewWMC);
		target.addComponent(feedbackPanel);
	}
}
