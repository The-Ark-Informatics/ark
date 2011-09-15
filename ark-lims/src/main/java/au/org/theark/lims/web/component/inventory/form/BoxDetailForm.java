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
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.InvColRowType;
import au.org.theark.core.model.lims.entity.InvTray;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.web.Constants;

/**
 * @author cellis
 * 
 */
public class BoxDetailForm extends AbstractInventoryDetailForm<LimsVO> {
	/**
	 * 
	 */
	private static final long					serialVersionUID	= 3950256468509804325L;

	private static Logger						log					= LoggerFactory.getLogger(BoxDetailForm.class);

	@SpringBean(name = Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService					iInventoryService;

	private TextField<String>					idTxtFld;
	private TextField<String>					nameTxtFld;
	private TextField<String>					capacityTxtFld;
	private TextField<String>					availableTxtFld;
	private TextField<String>					noOfColTxtFld;
	private TextField<String>					noOfRowTxtFld;
	private DropDownChoice<InvColRowType>	colNoTypeDdc;
	private DropDownChoice<InvColRowType>	rowNoTypeDdc;
	private DropDownChoice<InvTray>			invTrayDdc;
	
	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param detailContainer
	 * @param containerForm
	 * @param tree
	 * @param node 
	 */
	public BoxDetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer detailContainer, AbstractContainerForm<LimsVO> containerForm, BaseTree tree, DefaultMutableTreeNode node) {
		super(id, feedBackPanel, detailContainer, containerForm, tree, node);
	}
	
	@Override
	public void onBeforeRender() {
		super.onBeforeRender();
		noOfColTxtFld.setEnabled(containerForm.getModelObject().getInvBox().getId() == null);
		noOfRowTxtFld.setEnabled(containerForm.getModelObject().getInvBox().getId() == null);
	}

	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("invBox.id");
		nameTxtFld = new TextField<String>("invBox.name");
		capacityTxtFld = new TextField<String>("invBox.capacity");
		capacityTxtFld.setEnabled(false);
		availableTxtFld = new TextField<String>("invBox.available");
		availableTxtFld.setEnabled(false);
		noOfColTxtFld = new TextField<String>("invBox.noofcol");
		noOfRowTxtFld = new TextField<String>("invBox.noofrow");

		initInvTrayDdc();
		initColNoTypeDdc();
		initRowNoTypeDdc();

		attachValidators();
		addComponents();

		// Focus on Name
		nameTxtFld.add(new ArkDefaultFormFocusBehavior());
	}
	
	private void initInvTrayDdc() {
		List<InvTray> invTankList = new ArrayList<InvTray>(0);
		InvTray invTray = new InvTray();

		try {
			invTankList = iInventoryService.searchInvTray(invTray);
		}
		catch (ArkSystemException e) {
			log.error(e.getMessage());
		}
		ChoiceRenderer<InvTray> choiceRenderer = new ChoiceRenderer<InvTray>(Constants.NAME, Constants.ID);
		invTrayDdc = new DropDownChoice<InvTray>("invBox.invTray", (List<InvTray>) invTankList, choiceRenderer);
	}

	private void initColNoTypeDdc() {
		List<InvColRowType> invColRowNoTypeList = iInventoryService.getInvColRowTypes();
		ChoiceRenderer<InvColRowType> invColRowTypeRenderer = new ChoiceRenderer<InvColRowType>(Constants.NAME, Constants.ID);
		colNoTypeDdc = new DropDownChoice<InvColRowType>("invBox.colnotype", (List<InvColRowType>) invColRowNoTypeList, invColRowTypeRenderer);
		colNoTypeDdc.setNullValid(false);
	}

	private void initRowNoTypeDdc() {
		List<InvColRowType> invColRowNoTypeList = iInventoryService.getInvColRowTypes();
		ChoiceRenderer<InvColRowType> invColRowTypeRenderer = new ChoiceRenderer<InvColRowType>(Constants.NAME, Constants.ID);
		rowNoTypeDdc = new DropDownChoice<InvColRowType>("invBox.rownotype", (List<InvColRowType>) invColRowNoTypeList, invColRowTypeRenderer);
		rowNoTypeDdc.setNullValid(false);
	}

	protected void attachValidators() {
		idTxtFld.setRequired(true);
		nameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.invBox.name.required", this, new Model<String>("Name")));
		invTrayDdc.setRequired(true).setLabel(new StringResourceModel("error.invTray.name.required", this, new Model<String>("Name")));
		colNoTypeDdc.setRequired(true);
		rowNoTypeDdc.setRequired(true);
	}

	private void addComponents() {
		detailFormContainer.add(idTxtFld.setEnabled(false));
		detailFormContainer.add(nameTxtFld);
		detailFormContainer.add(invTrayDdc);
		detailFormContainer.add(capacityTxtFld);
		detailFormContainer.add(availableTxtFld);
		detailFormContainer.add(noOfColTxtFld);
		detailFormContainer.add(noOfRowTxtFld);
		detailFormContainer.add(colNoTypeDdc);
		detailFormContainer.add(rowNoTypeDdc);
		add(detailFormContainer);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onSave(Form<LimsVO> containerForm, AjaxRequestTarget target) {
		if (containerForm.getModelObject().getInvBox().getId() == null) {
			// Save
			iInventoryService.createInvBox(containerForm.getModelObject());
			this.info("Box " + containerForm.getModelObject().getInvBox().getName() + " was created successfully");
			processErrors(target);
		}
		else {
			// Update
			iInventoryService.updateInvBox(containerForm.getModelObject());
			this.info("Box " + containerForm.getModelObject().getInvBox().getName() + " was updated successfully");
			processErrors(target);
		}

		onSavePostProcess(target);
		
		//InvBox invBox = iInventoryService.getInvBox(containerForm.getModelObject().getInvBox().getId());
		/* Set new path
		List path = iInventoryService.getInventoryPathOfNode(containerForm.getModelObject().getInvBox());
		for (Iterator iterator = path.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			tree.getTreeState().expandNode(object);
			tree.updateTree();
		}
		
		tree.getTreeState().selectNode(containerForm.getModelObject().getInvBox(), true);
		tree.updateTree();
		*/
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
		iInventoryService.deleteInvBox(containerForm.getModelObject());
		this.info("Box " + containerForm.getModelObject().getInvBox().getName() + " was deleted successfully");
		log.info("Box " + containerForm.getModelObject().getInvBox().getName() + " was deleted successfully");
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
		if (containerForm.getModelObject().getInvBox().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}
}