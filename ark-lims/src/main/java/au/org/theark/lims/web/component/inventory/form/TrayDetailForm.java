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

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
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

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.InvTank;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.web.Constants;

/**
 * @author cellis
 * 
 */
@SuppressWarnings({ "serial", "unused" })
public class TrayDetailForm extends AbstractInventoryDetailForm<LimsVO> {
	private static Logger		log	= LoggerFactory.getLogger(TrayDetailForm.class);
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
	private TextArea<String>			descriptionTxtAreaFld;
	private DropDownChoice<InvTank>	invTankDdc;

	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param detailContainer
	 * @param containerForm
	 * @param tree
	 * @param node 
	 */
	public TrayDetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer detailContainer, AbstractContainerForm<LimsVO> containerForm, BaseTree tree, DefaultMutableTreeNode node) {
		super(id, feedBackPanel, detailContainer, containerForm, tree, node);
	}

	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("invTray.id");
		nameTxtFld = new TextField<String>("invTray.name");
		capacityTxtFld = new TextField<String>("invTray.capacity");
		availableTxtFld = new TextField<String>("invTray.available");
		descriptionTxtAreaFld = new TextArea<String>("invTray.description");
		
		initInvTankDdc();
		
		attachValidators();
		addComponents();
		
		// Focus on Name
		nameTxtFld.add(new ArkDefaultFormFocusBehavior());
	}
	
	private void initInvTankDdc() {
		List<InvTank> invTankList = new ArrayList<InvTank>(0);
		InvTank InvTank = new InvTank();

		try {
			invTankList = iInventoryService.searchInvTank(InvTank);
		}
		catch (ArkSystemException e) {
			log.error(e.getMessage());
		}
		ChoiceRenderer<InvTank> choiceRenderer = new ChoiceRenderer<InvTank>(Constants.NAME, Constants.ID);
		invTankDdc = new DropDownChoice<InvTank>("invTray.invTank", (List<InvTank>) invTankList, choiceRenderer);
	}

	protected void attachValidators() {
		idTxtFld.setRequired(true);
		nameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.invTray.name.required", this, new Model<String>("Name")));
		invTankDdc.setRequired(true).setLabel(new StringResourceModel("error.invTank.name.required", this, new Model<String>("Name")));
	}

	private void addComponents() {
		detailFormContainer.add(idTxtFld.setEnabled(false));
		detailFormContainer.add(nameTxtFld);
		detailFormContainer.add(capacityTxtFld);
		detailFormContainer.add(availableTxtFld);
		detailFormContainer.add(descriptionTxtAreaFld);
		detailFormContainer.add(invTankDdc);
		add(detailFormContainer);
	}

	@Override
	protected void onSave(Form<LimsVO> containerForm, AjaxRequestTarget target) {
		if (containerForm.getModelObject().getInvTray().getId() == null) {
			// Save
			iInventoryService.createInvTray(containerForm.getModelObject());
			this.info("Tray " + containerForm.getModelObject().getInvTray().getName() + " was created successfully");
			processErrors(target);
		}
		else {
			// Update
			iInventoryService.updateInvTray(containerForm.getModelObject());
			this.info("Tray " + containerForm.getModelObject().getInvTray().getName() + " was updated successfully");
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
		iInventoryService.deleteInvTray(containerForm.getModelObject());
		this.info("Tank " + containerForm.getModelObject().getInvTray().getName() + " was deleted successfully");

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
		if (containerForm.getModelObject().getInvTray().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}
}