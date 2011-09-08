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
package au.org.theark.lims.web.component.inventory.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.web.Constants;

/**
 * @author cellis
 * 
 */
@SuppressWarnings({ "serial", "unused" })
public class TankDetailForm extends AbstractInventoryDetailForm<LimsVO> {
	private static Logger		log	= LoggerFactory.getLogger(TankDetailForm.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	@SpringBean(name = Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService					iInventoryService;

	private ContainerForm				fieldContainerForm;

	private int								mode;

	private TextField<String>			idTxtFld;
	private TextField<String>			nameTxtFld;
	private TextField<String>			capacityTxtFld;
	private TextField<String>			availableTxtFld;
	private TextArea<String>			lastservicenoteTxtAreaFld;
	private DateTextField				decommissiondateDateTxtFld;
	private TextArea<String>			descriptionTxtAreaFld;

	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param detailContainer
	 * @param containerForm
	 * @param tree
	 */
	public TankDetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer detailContainer, AbstractContainerForm<LimsVO> containerForm, BaseTree tree) {

		super(id, feedBackPanel, detailContainer, containerForm, tree);
	}

	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("invTank.id");
		nameTxtFld = new TextField<String>("invTank.name");
		capacityTxtFld = new TextField<String>("invTank.capacity");
		availableTxtFld = new TextField<String>("invTank.available");
		lastservicenoteTxtAreaFld = new TextArea<String>("invTank.lastservicenote");
		decommissiondateDateTxtFld = new DateTextField("invTank.decommissiondate");
		descriptionTxtAreaFld = new TextArea<String>("invTank.description");
		
		ArkDatePicker arkDatePicker = new ArkDatePicker();
		arkDatePicker.bind(decommissiondateDateTxtFld);
		decommissiondateDateTxtFld.add(arkDatePicker);
		
		attachValidators();
		addComponents();
		
		// Focus on Name
		nameTxtFld.add(new ArkDefaultFormFocusBehavior());
	}

	protected void attachValidators() {
		idTxtFld.setRequired(true);
		nameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.invTank.name.required", this, new Model<String>("Name")));
	}

	private void addComponents() {
		detailFormContainer.add(idTxtFld.setEnabled(false));
		detailFormContainer.add(nameTxtFld);
		detailFormContainer.add(capacityTxtFld);
		detailFormContainer.add(availableTxtFld);
		detailFormContainer.add(lastservicenoteTxtAreaFld);
		detailFormContainer.add(decommissiondateDateTxtFld);
		detailFormContainer.add(descriptionTxtAreaFld);
		add(detailFormContainer);
	}

	@Override
	protected void onSave(Form<LimsVO> containerForm, AjaxRequestTarget target) {
		if (containerForm.getModelObject().getInvTank().getId() == null) {
			// Save
			iInventoryService.createInvTank(containerForm.getModelObject());
			this.info("Tank " + containerForm.getModelObject().getInvTank().getName() + " was created successfully");
			processErrors(target);
		}
		else {
			// Update
			iInventoryService.updateInvTank(containerForm.getModelObject());
			this.info("Tank " + containerForm.getModelObject().getInvTank().getName() + " was updated successfully");
			processErrors(target);
		}

		onSavePostProcess(target);
	}

	protected void onCancel(AjaxRequestTarget target) {
		LimsVO limsVo = new LimsVO();
		containerForm.setModelObject(limsVo);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.addComponent(feedbackPanel);
	}

	public AjaxButton getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(AjaxButton deleteButton) {
		this.deleteButton = deleteButton;
	}

	protected void onDeleteConfirmed(AjaxRequestTarget target) {
		iInventoryService.deleteInvTank(containerForm.getModelObject());
		this.info("Tank " + containerForm.getModelObject().getInvTank().getName() + " was deleted successfully");

		// Display delete confirmation message
		target.addComponent(feedbackPanel);

		// Move focus back to Search form
		LimsVO limsVo = new LimsVO();
		containerForm.setModelObject(limsVo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getInvTank().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}
}