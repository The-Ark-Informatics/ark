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
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.wicketstuff.progressbar.ProgressBar;
import org.wicketstuff.progressbar.Progression;
import org.wicketstuff.progressbar.ProgressionModel;

import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;

/**
 * The final step of this wizard.
 */
public class SubjectUploadStep5 extends AbstractWizardStepPanel {

	private static final long	serialVersionUID	= -6803600838428204753L;
	private Form<UploadVO>						containerForm;
	private  WebMarkupContainer					progressPanel;
	private org.wicketstuff.progressbar.ProgressBar progressBar;

	public SubjectUploadStep5(String id, Form<UploadVO> containerForm) {
		super(id, "Step 5/5: Data Upload Commenced",
				"The data is currently being processed for import. On returning to the Study Data Upload tab, the status of this upload will become \"Successfully Completed \""
				+ " when the import process is complete.<br/>In the event that the import process fails, the status of the upload will become \"Error while importing data\". Please contact the system administrator if this occurs."); 
		this.containerForm = containerForm;
		progressPanel=new WebMarkupContainer("progressPanel");
		progressPanel.setOutputMarkupId(true);
		progressBar = new ProgressBar("bar", new ProgressionModel() {
	 		private static final long serialVersionUID = 1L;

			protected Progression getProgression() {
	            return new Progression(containerForm.getModelObject().getProgress());
	        }
	    });
		progressBar.setWidth(1000);
		//(2017-01-13)For the moment progress bar has been hidden from the page.
		//Because it will shows correct value for the custom filed data but not for the rest of the values any more
		//Later when time permit hope to revisit for the issue and complete.
		// Because of the exception ( No Page found for component [ProgressBar [Component id = bar]]) 
		//I think I haven't properly integrated the progress bar.
		//I guess it should be in different page "AbstractWizardForm" probably.
		progressPanel.setVisible(false);
		progressPanel.add(progressBar);
		add(progressPanel);
	}
	@Override
	public void handleWizardState(AbstractWizardForm<?> form, AjaxRequestTarget target) {
		if (this.containerForm.getModelObject().getValidationMessages() != null) {
			containerForm.getModelObject().setStrMessage(containerForm.getModelObject().getValidationMessagesAsString());
			form.getNextButton().setEnabled(false);
			form.getFinishButton().setEnabled(true);
			target.add(form.getWizardButtonContainer());
		}
		log.info("Progress:"+containerForm.getModelObject().getProgress().toString());
		if (containerForm.getModelObject().getProgress()>0){
			progressPanel.setVisible(true);
			target.add(progressPanel);
		}
		//progressBar.start(target);
		
		
		
	}
	
}
