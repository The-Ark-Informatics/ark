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
package au.org.theark.study.web.component.subjectUpload;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;

import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;

/**
 * The final step of this wizard.
 */
public class SubjectUploadStep5 extends AbstractWizardStepPanel {

	private static final long	serialVersionUID	= -6803600838428204753L;
	private Form<UploadVO>						containerForm;
	

	public SubjectUploadStep5(String id, Form<UploadVO> containerForm) {
		super(id, "Step 5/5: Data Upload Commenced",
				"The data is currently being processed for import. On returning to the Study Data Upload tab, the status of this upload will become \"Successfully Completed \""
				+ " when the import process is complete.<br/>In the event that the import process fails, the status of the upload will become \"Error while importing data\". Please contact the system administrator if this occurs."); 
		this.containerForm = containerForm;
	}
	@Override
	public void handleWizardState(AbstractWizardForm<?> form, AjaxRequestTarget target) {
		if (this.containerForm.getModelObject().getValidationMessages() != null) {
			containerForm.getModelObject().setStrMessage(containerForm.getModelObject().getValidationMessagesAsString());
			form.getNextButton().setEnabled(false);
			form.getFinishButton().setEnabled(true);
			target.add(form.getWizardButtonContainer());
		}
	}
}
