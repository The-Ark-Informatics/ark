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

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.lims.model.vo.BiospecimenLocationVO;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.inventory.panel.box.display.GridBoxPanel;

/**
 * @author cellis
 * 
 */
public class BoxAllocationDetailForm extends AbstractInventoryDetailForm<LimsVO> {

	/**
	 * 
	 */
	private static final long				serialVersionUID	= -2954575722360792047L;

	private static Logger					log					= LoggerFactory.getLogger(BoxAllocationDetailForm.class);

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService						iLimsService;

	@SpringBean(name = Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService				iInventoryService;

	private TextField<String>				invBoxNameTxtFld;
	private TextField<String>				biospecimenUidTxtFld;
	private GridBoxPanel						gridBoxPanel;
	private AbstractDetailModalWindow	modalWindow;

	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param detailContainer
	 * @param containerForm
	 * @param tree
	 * @param node
	 * @param modalWindow
	 */
	public BoxAllocationDetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer detailContainer, AbstractContainerForm<LimsVO> containerForm, BaseTree tree, DefaultMutableTreeNode node,
			GridBoxPanel gridBoxPanel, AbstractDetailModalWindow modalWindow) {
		super(id, feedBackPanel, detailContainer, containerForm, tree, node);
		this.gridBoxPanel = gridBoxPanel;
		this.feedbackPanel = feedBackPanel;
		this.modalWindow = modalWindow;
	}
	
	@Override
	public void onBeforeRender() {
		boolean cellsAvailable = (containerForm.getModelObject().getInvBox().getAvailable() > 0);
		biospecimenUidTxtFld.setEnabled(cellsAvailable);
		saveButton.setEnabled(cellsAvailable);
		super.onBeforeRender();
	}

	public void initialiseDetailForm() {
		invBoxNameTxtFld = new TextField<String>("invBox.name");
		invBoxNameTxtFld.setEnabled(false);
		biospecimenUidTxtFld = new TextField<String>("biospecimen.biospecimenUid");

		attachValidators();
		addComponents();

		// Focus on biospecimenUid
		biospecimenUidTxtFld.add(new ArkDefaultFormFocusBehavior());
	}

	protected void attachValidators() {
		// biospecimenUidTxtFld.setRequired(true).setLabel(new StringResourceModel("error.name.required", this, new Model<String>("Name")));
	}

	private void addComponents() {
		detailFormContainer.add(invBoxNameTxtFld);
		detailFormContainer.add(biospecimenUidTxtFld);
		add(detailFormContainer);
	}

	@Override
	protected void onSave(Form<LimsVO> containerForm, AjaxRequestTarget target) {
		if(containerForm.getModelObject().getInvBox().getAvailable() == 0) {
			this.error("This box has no available locations");
		}
		else {
			String biospecimenUid = containerForm.getModelObject().getBiospecimen().getBiospecimenUid();
	
			if (biospecimenUid != null && !biospecimenUid.isEmpty()) {
				Biospecimen biospecimen = iLimsService.getBiospecimenByUid(biospecimenUid);
				
				if(biospecimen != null && biospecimen.getId() != null) {
					InvCell invCell = iInventoryService.getInvCellByBiospecimen(biospecimen);
		
					if (invCell.getBiospecimen() != null) {
						try {
							BiospecimenLocationVO biospecimenLocationVO = iInventoryService.getBiospecimenLocation(biospecimen);
							StringBuilder location = new StringBuilder();
							location.append("The Biospecimen: ");
							location.append(biospecimenUid);
							location.append(" is already allocated at ");
							location.append("Box: ");
							location.append(biospecimenLocationVO.getBoxName());
							location.append(" ");
							location.append("Row: ");
							location.append(biospecimenLocationVO.getRowLabel());
							location.append(", ");
							location.append("Column: ");
							location.append(biospecimenLocationVO.getColLabel());
							this.error(location.toString());
						}
						catch (ArkSystemException e) {
							log.error(e.getMessage());
						}
					}
					else {
						invCell = iInventoryService.getNextAvailableInvCell(containerForm.getModelObject().getInvBox());
						containerForm.getModelObject().setBiospecimen(biospecimen);
						invCell.setBiospecimen(containerForm.getModelObject().getBiospecimen());
						// TODO: use Reference CellStatus
						invCell.setStatus("Not Empty");
		
						containerForm.getModelObject().setInvCell(invCell);
						iInventoryService.updateInvCell(invCell);
						iLimsService.updateBiospecimen(containerForm.getModelObject());
		
						// Refresh the gridpanel
						gridBoxPanel = new GridBoxPanel("gridBoxPanel", containerForm.getModelObject(), modalWindow, false);
						BoxAllocationDetailForm.this.getParent().addOrReplace(gridBoxPanel);
						target.add(gridBoxPanel);
		
						try {
							BiospecimenLocationVO biospecimenLocationVO = iInventoryService.getBiospecimenLocation(biospecimen);
							StringBuilder location = new StringBuilder();
							location.append("The Biospecimen: ");
							location.append(biospecimenUid);
							location.append(" allocated to ");
							location.append("Row: ");
							location.append(biospecimenLocationVO.getRowLabel());
							location.append(", ");
							location.append("Column: ");
							location.append(biospecimenLocationVO.getColLabel());
							this.info(location.toString());
						}
						catch (ArkSystemException e) {
							log.error(e.getMessage());
						}
		
						// Refresh text box model reference
						containerForm.getModelObject().setBiospecimen(new Biospecimen());
					}
				}
				else
				{
					this.error("Biospecimen:" + biospecimenUid + " does not exist in the system. Please check and try again");
				}
			}
			else {
				this.error("Scan or manually enter the Biospecimen UID to allocate to the next available position");
			}
		}

		// refresh feedback
		target.add(feedbackPanel);
	}

	protected void onCancel(AjaxRequestTarget target) {
		LimsVO limsVo = new LimsVO();
		containerForm.setModelObject(limsVo);
		target.add(feedbackPanel);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedbackPanel);
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
		target.add(feedbackPanel);

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

	@Override
	protected boolean canDelete() {
		return !iInventoryService.hasAllocatedCells(containerForm.getModelObject().getInvBox());
	}
}