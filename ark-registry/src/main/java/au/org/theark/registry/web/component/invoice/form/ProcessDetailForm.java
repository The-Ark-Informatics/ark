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
package au.org.theark.registry.web.component.invoice.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.geno.entity.Process;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.form.AbstractDetailForm;

/**
 * @author nivedann
 * @author cellis
 * 
 */
public class ProcessDetailForm extends AbstractDetailForm<Process> {

	private static final long					serialVersionUID	= 1423759632793367263L;

	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	private TextField<String>					name;
	private TextField<String>					description;
	private TextField<String>					startTime;
	private TextField<String>					endTime;

	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public ProcessDetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, Form<Process> containerForm) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.feedBackPanel = feedBackPanel;
	}

	public void initialiseDetailForm() {
		name = new TextField<String>("name");
		name.add(new ArkDefaultFormFocusBehavior());
		description = new TextField<String>("description");
		startTime = new TextField<String>("startTime");//TODO: analyze textfield construction?
		endTime = new TextField<String>("endTime");
		
		attachValidators();
		addDetailFormComponents();
	}

	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(name);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(description);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(startTime);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(endTime);
		this.addOrReplace(arkCrudContainerVO.getDetailPanelFormContainer());
	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		containerForm.setModelObject(new Process());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String,
	 * org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {

		iArkCommonService.deleteProcess(containerForm.getModelObject());
		this.info("Process was successfully deleted.");
		onCancel(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override	
	protected void onSave(Form<Process> containerForm, AjaxRequestTarget target) {
	//	Long personSessionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		//StringBuffer feedBackMessageStr = new StringBuffer();
		
		if(containerForm.getModelObject()!=null && containerForm.getModelObject() .getId()==null){
			
			iArkCommonService.createProcess(containerForm.getModelObject());
			this.info("Process was successfully saved.");
		}
		else {
			iArkCommonService.updateProcess(containerForm.getModelObject());
			this.info("Process was successfully updated.");
		}
		
		target.add(feedBackPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}

	@Override
	protected void attachValidators() {
		name.setRequired(true).setLabel((new StringResourceModel("name.required", this, new Model<String>("Name"))));
	}
}
