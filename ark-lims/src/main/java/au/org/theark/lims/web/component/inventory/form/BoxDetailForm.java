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
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.IntegerConverter;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.model.lims.entity.InvColRowType;
import au.org.theark.core.model.lims.entity.InvRack;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.LimsVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.button.AjaxDeleteButton;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.inventory.panel.box.BoxAllocationPanel;
import au.org.theark.lims.web.component.inventory.tree.InventoryLinkTree;

/**
 * @author cellis
 * 
 */
public class BoxDetailForm extends AbstractInventoryDetailForm<LimsVO> {

	private static final long					serialVersionUID	= 3950256468509804325L;

	private static Logger						log					= LoggerFactory.getLogger(BoxDetailForm.class);
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	@SpringBean(name = Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService					iInventoryService;

	private TextField<String>					idTxtFld;
	private TextField<String>					nameTxtFld;
	private TextField<Integer>					capacityTxtFld;
	private TextField<Integer>					availableTxtFld;
	private TextField<Integer>					noOfColTxtFld;
	private TextField<Integer>					noOfRowTxtFld;
	private DropDownChoice<InvColRowType>	colNoTypeDdc;
	private DropDownChoice<InvColRowType>	rowNoTypeDdc;
	private DropDownChoice<InvRack>			invTrayDdc;
	private AjaxButton 							batchAllocate;
	private AjaxButton 							emptyBox;
	private BoxAllocationPanel 				boxAllocationPanel;
	
	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param detailContainer
	 * @param containerForm
	 * @param tree
	 * @param node 
	 */
	public BoxDetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer detailContainer, ContainerForm containerForm, InventoryLinkTree tree, DefaultMutableTreeNode node, Panel containerPanel) {
		super(id, feedBackPanel, detailContainer, containerForm, tree, node, containerPanel);
		boxAllocationPanel = new BoxAllocationPanel("detailPanel", feedbackPanel, detailContainer, containerForm, tree, node);
	}

	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("invBox.id");
		nameTxtFld = new TextField<String>("invBox.name");
		nameTxtFld.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long	serialVersionUID	= 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				String boxName = (getComponent().getDefaultModelObject().toString() != null ? getComponent().getDefaultModelObject().toString() : new String());
				InvBox invBox=iInventoryService.getInvBoxByNameForRack(invTrayDdc.getModelObject(), boxName);
				if (invBox != null && invBox.getId() != null) {
					error("Box name must be unique for a Rack. Please try again.");
					target.focusComponent(getComponent());
				}
					target.add(feedbackPanel);
			}
		});
		capacityTxtFld = new TextField<Integer>("invBox.capacity");
		capacityTxtFld.setEnabled(false);
		availableTxtFld = new TextField<Integer>("invBox.available");
		availableTxtFld.setEnabled(false);
		noOfColTxtFld = new TextField<Integer>("invBox.noofcol"){

			private static final long	serialVersionUID	= 1L;

			@SuppressWarnings("unchecked")
			@Override
			public <C> IConverter<C> getConverter(Class<C> type) {
				IntegerConverter integerConverter = new IntegerConverter();
				return (IConverter<C>) integerConverter;
			}
		};
		
		noOfColTxtFld.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				updateCapacityAndAvailable(target);
			}
		});
		
		noOfRowTxtFld = new TextField<Integer>("invBox.noofrow"){

			private static final long	serialVersionUID	= 1L;

			@SuppressWarnings("unchecked")
			@Override
			public <C> IConverter<C> getConverter(Class<C> type) {
				IntegerConverter integerConverter = new IntegerConverter();
				return (IConverter<C>) integerConverter;
			}
		};
		
		noOfRowTxtFld.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				updateCapacityAndAvailable(target);
			}
		});
		
		noOfColTxtFld.setEnabled(isNew());
		noOfRowTxtFld.setEnabled(isNew());

		initInvTrayDdc();
		initColNoTypeDdc();
		initRowNoTypeDdc();
		
		batchAllocate = new ArkBusyAjaxButton("batchAllocate") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Refresh entire detail panel to allocation panel
				boxAllocationPanel.initialisePanel();
				detailContainer.addOrReplace(boxAllocationPanel);
				target.add(detailContainer);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
			
			@Override
			protected void onBeforeRender() {
				setEnabled(!isNew() && containerForm.getModelObject().getInvBox().getAvailable() > 0);
				super.onBeforeRender();
			}
		};
		batchAllocate.setDefaultFormProcessing(false);
		
		
		emptyBox = new AjaxDeleteButton("emptyBox", new StringResourceModel("emptyBoxConfirm", this, null), new StringResourceModel("emptyBox", this, null)) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				iInventoryService.unallocateBox(containerForm.getModelObject().getInvBox());
				// Refresh entire detail panel 
				target.add(detailContainer);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
			
			@Override
			protected void onBeforeRender() {
				setEnabled(!isNew() && containerForm.getModelObject().getInvBox().getAvailable() > 0);
				super.onBeforeRender();
			}
		};
		emptyBox.setDefaultFormProcessing(false);

		attachValidators();
		addComponents();

		// Focus on Name
		nameTxtFld.add(new ArkDefaultFormFocusBehavior());
	}
	
	protected void updateCapacityAndAvailable(AjaxRequestTarget target) {
		if (noOfColTxtFld.getModelObject() != null && noOfRowTxtFld.getModelObject() != null) {
			Integer capacity = noOfColTxtFld.getModelObject() * noOfRowTxtFld.getModelObject();
			Integer available = noOfColTxtFld.getModelObject() * noOfRowTxtFld.getModelObject();
			
			if(capacity != null && available != null) {
				containerForm.getModelObject().getInvBox().setCapacity(capacity);
				containerForm.getModelObject().getInvBox().setAvailable(available);
				target.add(capacityTxtFld);
				target.add(availableTxtFld);
			}
		}
	}

	private void initInvTrayDdc() {
		List<InvRack> invTankList = new ArrayList<InvRack>(0);
		
		List<Study> studyListForUser = new ArrayList<Study>(0);
		studyListForUser.add(containerForm.getModelObject().getStudy());
		/*
		try {
			Subject currentUser = SecurityUtils.getSubject();
			ArkUser arkUser = iArkCommonService.getArkUser(currentUser.getPrincipal().toString());
			ArkUserVO arkUserVo = new ArkUserVO();
			arkUserVo.setArkUserEntity(arkUser);
			
			Long sessionArkModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
			ArkModule arkModule = null;
			arkModule = iArkCommonService.getArkModuleById(sessionArkModuleId);
			studyListForUser = iArkCommonService.getStudyListForUserAndModule(arkUserVo, arkModule);
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());
		}
		*/
		try {
			invTankList = iInventoryService.searchInvRack(new InvRack(), studyListForUser);
		}
		catch (ArkSystemException e) {
			log.error(e.getMessage());
		}
		ChoiceRenderer<InvRack> choiceRenderer = new ChoiceRenderer<InvRack>("siteFreezerRack", Constants.ID);
		invTrayDdc = new DropDownChoice<InvRack>("invBox.invRack", (List<InvRack>) invTankList, choiceRenderer);
		invTrayDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long	serialVersionUID	= 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				String boxName = (nameTxtFld.getModelObject().toString() != null ? nameTxtFld.getModelObject().toString() : new String());
				InvBox invBox=iInventoryService.getInvBoxByNameForRack(invTrayDdc.getModelObject(), boxName);
				if (invBox != null && invBox.getId() != null) {
					error("Box name must be unique for a Rack. Please try again.");
					target.focusComponent(getComponent());
				}
					target.add(feedbackPanel);
			}
		});
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
		nameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.name.required", this, new Model<String>("Name")));
		nameTxtFld.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_NAME_MAX_LENGTH_50));
		invTrayDdc.setRequired(true).setLabel(new StringResourceModel("error.rack.required", this, new Model<String>("Rack")));
		noOfColTxtFld.setRequired(true);
		RangeValidator<Integer> minValidator = new RangeValidator<>(new Integer(0), null);
		noOfColTxtFld.add(minValidator);
		noOfRowTxtFld.setRequired(true);
		noOfRowTxtFld.add(minValidator);
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
		
		add(batchAllocate);
		add(emptyBox);
	}

	@Override
	protected void onSave(Form<LimsVO> containerForm, AjaxRequestTarget target) {
		if (containerForm.getModelObject().getInvBox().getId() == null) {
			// Save
			iInventoryService.createInvBox(containerForm.getModelObject());
			this.info("Box " + containerForm.getModelObject().getInvBox().getName() + " was successfully created.");
			processErrors(target);
			
			if(node != null) {
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(containerForm.getModelObject().getInvBox());
				node.add(newNode);
				tree.getTreeState().selectNode(newNode, true);
				tree.getTreeState().expandNode(node);
				node = newNode;
			}
		}
		else {
			// Update
			iInventoryService.updateInvBox(containerForm.getModelObject());
			this.info("Box " + containerForm.getModelObject().getInvBox().getName() + " was successfully updated.");
			processErrors(target);
		}

		// Repaint container (shoe box/cells)
		target.add(detailContainer);
		
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
		iInventoryService.deleteInvBox(containerForm.getModelObject());
		this.info("Box " + containerForm.getModelObject().getInvBox().getName() + " was successfully deleted.");
		// Display delete confirmation message
		target.add(feedbackPanel);
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