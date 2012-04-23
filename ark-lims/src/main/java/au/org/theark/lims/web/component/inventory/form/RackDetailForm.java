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
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.IntegerConverter;
import org.apache.wicket.validation.validator.MinimumValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.InvFreezer;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.inventory.tree.InventoryLinkTree;

/**
 * @author cellis
 * 
 */
@SuppressWarnings({ "serial", "unused" })
public class RackDetailForm extends AbstractInventoryDetailForm<LimsVO> {
	private static Logger		log	= LoggerFactory.getLogger(RackDetailForm.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	@SpringBean(name = Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService					iInventoryService;

	private ContainerForm				fieldContainerForm;

	private int								mode;

	private TextField<String>			idTxtFld;
	private TextField<String>			nameTxtFld;
	private TextField<Integer>			capacityTxtFld;
	private TextField<Integer>			availableTxtFld;
	private TextArea<String>			descriptionTxtAreaFld;
	private DropDownChoice<InvFreezer>	invTankDdc;

	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param detailContainer
	 * @param containerForm
	 * @param tree
	 * @param node 
	 */
	public RackDetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer detailContainer, AbstractContainerForm<LimsVO> containerForm, InventoryLinkTree tree, DefaultMutableTreeNode node) {
		super(id, feedBackPanel, detailContainer, containerForm, tree, node);
	}

	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("invRack.id");
		nameTxtFld = new TextField<String>("invRack.name");
		capacityTxtFld = new TextField<Integer>("invRack.capacity"){
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@SuppressWarnings("unchecked")
			@Override
			public <C> IConverter<C> getConverter(Class<C> type) {
				IntegerConverter integerConverter = new IntegerConverter();
				return (IConverter<C>) integerConverter;
			}
		};
		capacityTxtFld.setEnabled(isNew());
		capacityTxtFld.add(new AjaxFormComponentUpdatingBehavior("onChange") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Integer capacity = containerForm.getModelObject().getInvRack().getCapacity();
				containerForm.getModelObject().getInvRack().setAvailable(capacity);
				target.add(availableTxtFld);
			}
		});
		availableTxtFld = new TextField<Integer>("invRack.available");
		availableTxtFld.setEnabled(false);
		descriptionTxtAreaFld = new TextArea<String>("invRack.description");
		
		initInvFreezerDdc();
		
		attachValidators();
		addComponents();
		
		// Focus on Name
		nameTxtFld.add(new ArkDefaultFormFocusBehavior());
		
		deleteButton.setEnabled(containerForm.getModelObject().getInvRack().getChildren().isEmpty());
	}
	
	private void initInvFreezerDdc() {
		List<InvFreezer> invFreezerList = new ArrayList<InvFreezer>(0);
		try {
			invFreezerList = iInventoryService.searchInvFreezer(new InvFreezer());
		}
		catch (ArkSystemException e) {
			log.error(e.getMessage());
		}
		ChoiceRenderer<InvFreezer> choiceRenderer = new ChoiceRenderer<InvFreezer>("siteFreezer", Constants.ID);
		invTankDdc = new DropDownChoice<InvFreezer>("invRack.invFreezer", (List<InvFreezer>) invFreezerList, choiceRenderer);
	}

	protected void attachValidators() {
		nameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.name.required", this, new Model<String>("Name")));
		invTankDdc.setRequired(true).setLabel(new StringResourceModel("error.freezer.required", this, new Model<String>("Freezer")));
		capacityTxtFld.setRequired(true).setLabel(new StringResourceModel("error.capacity.required", this, new Model<String>("Capacity")));
		MinimumValidator<Integer> minValue = new MinimumValidator<Integer>(new Integer(0));
		capacityTxtFld.add(minValue);
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
		if (containerForm.getModelObject().getInvRack().getId() == null) {
			// Save
			iInventoryService.createInvRack(containerForm.getModelObject());
			this.info("Rack " + containerForm.getModelObject().getInvRack().getName() + " was created successfully");
			processErrors(target);
			
			if(node != null) {
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(containerForm.getModelObject().getInvRack());
				node.add(newNode);
				tree.getTreeState().selectNode(newNode, true);
				tree.getTreeState().expandNode(node);
				node = newNode;
			}
		}
		else {
			// Update
			iInventoryService.updateInvRack(containerForm.getModelObject());
			this.info("Rack " + containerForm.getModelObject().getInvRack().getName() + " was updated successfully");
			processErrors(target);
		}

		onSavePostProcess(target);
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
		iInventoryService.deleteInvRack(containerForm.getModelObject());
		this.info("Rack " + containerForm.getModelObject().getInvRack().getName() + " was deleted successfully");

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
		if (containerForm.getModelObject().getInvRack().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	protected boolean canDelete() {
		return containerForm.getModelObject().getInvRack().getChildren().isEmpty();
	}
}