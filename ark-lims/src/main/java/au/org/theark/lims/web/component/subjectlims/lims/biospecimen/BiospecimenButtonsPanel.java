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
package au.org.theark.lims.web.component.subjectlims.lims.biospecimen;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.web.component.button.ArkAjaxButton;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.util.UniqueIdGenerator;
import au.org.theark.lims.web.component.button.zebra.biospecimen.PrintBiospecimenLabelButton;
import au.org.theark.lims.web.component.subjectlims.lims.biospecimen.form.BiospecimenModalDetailForm;

/**
 * Panel used for all Biospecimen related workflow (Clone, Process, Aliquot)
 * 
 * @author cellis
 * 
 */
public class BiospecimenButtonsPanel extends Panel {

	/**
	 * 
	 */
	private static final long				serialVersionUID	= 1L;

	private static final Logger			log					= LoggerFactory.getLogger(BiospecimenButtonsPanel.class);

	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_SERVICE)
	private ILimsService						iLimsService;

	private BiospecimenModalDetailForm	biospecimenModalDetailForm;
	protected FeedbackPanel					feedbackPanel;

	protected AjaxButton						cloneButton;
	protected AjaxButton						processButton;
	protected AjaxButton						aliquotButton;
	protected AjaxButton						printBarcodeButton;

	public BiospecimenButtonsPanel(String id, BiospecimenModalDetailForm biospecimenModalDetailForm, FeedbackPanel feedbackPanel) {
		super(id, biospecimenModalDetailForm.getModel());
		setOutputMarkupPlaceholderTag(true);
		this.biospecimenModalDetailForm = biospecimenModalDetailForm;
		this.feedbackPanel = feedbackPanel;
		initialisePanel();
	}

	protected void initialisePanel() {
		cloneButton = new ArkAjaxButton("clone") {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public boolean isVisible() {
				// Ark-Security implemented
				return super.isVisible() && ArkPermissionHelper.isActionPermitted(Constants.SAVE);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Make sure the button is visible and enabled before allowing it to proceed
				if (cloneButton.isVisible() && cloneButton.isEnabled()) {
					onClone(target);
				}
				else {
					log.error("Illegal Clone button submit: button is not enabled and/or not visible.");
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				if (!cloneButton.isVisible() || !cloneButton.isEnabled()) {
					log.error("Illegal onError for Clone button submit: button is not enabled and/or not visible.");
				}
			}
		};
		this.add(cloneButton);

		processButton = new ArkAjaxButton("process") {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public boolean isVisible() {
				// Ark-Security implemented
				// return super.isVisible() && ArkPermissionHelper.isActionPermitted(Constants.SAVE);
				// TODO: Implement process correctly
				return super.isVisible() && false;
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Make sure the button is visible and enabled before allowing it to proceed
				if (processButton.isVisible() && processButton.isEnabled()) {
					onProcess(target);
				}
				else {
					log.error("Illegal Process button submit: button is not enabled and/or not visible.");
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				if (!processButton.isVisible() || !processButton.isEnabled()) {
					log.error("Illegal onError for Process button submit: button is not enabled and/or not visible.");
				}
			}
		};
		processButton.setDefaultFormProcessing(false);
		this.add(processButton);

		aliquotButton = new ArkAjaxButton("aliquot") {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public boolean isVisible() {
				// Ark-Security implemented
				// return super.isVisible() && ArkPermissionHelper.isActionPermitted(Constants.SAVE);
				// TODO: Implement process correctly
				return super.isVisible() && false;
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Make sure the button is visible and enabled before allowing it to proceed
				if (processButton.isVisible() && processButton.isEnabled()) {
					onAliquot(target);
				}
				else {
					log.error("Illegal Aliquot button submit: button is not enabled and/or not visible.");
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				if (!processButton.isVisible() || !processButton.isEnabled()) {
					log.error("Illegal onError for Aliquot button submit: button is not enabled and/or not visible.");
				}
			}
		};

		aliquotButton.setDefaultFormProcessing(false);
		this.add(aliquotButton);

		printBarcodeButton = new PrintBiospecimenLabelButton("printBarcode", biospecimenModalDetailForm.getModelObject().getBiospecimen()) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onPostSubmit(AjaxRequestTarget target, Form<?> form) {
				// Set barcoded flag to true, as barcode has been printed
				biospecimenModalDetailForm.getModelObject().getBiospecimen().setBarcoded(true);
				// Update/refresh
				iLimsService.updateBiospecimen(biospecimenModalDetailForm.getModelObject());
				target.add(biospecimenModalDetailForm.getBarcodedChkBox());
				target.add(biospecimenModalDetailForm.getBarcodeImage());
			}
		};
		this.add(printBarcodeButton);
	}

	/**
	 * Clone all Biospecimen details to a new Biospecimen
	 * 
	 * @param target
	 */
	private void onClone(AjaxRequestTarget target) {
		try {
			LimsVO oldlimsVo = (LimsVO) biospecimenModalDetailForm.getModelObject();
			final String biospecimenUid = oldlimsVo.getBiospecimen().getBiospecimenUid();
			LimsVO limsVo = new LimsVO();
			PropertyUtils.copyProperties(limsVo, oldlimsVo);
			limsVo.getBiospecimen().setId(null);
			limsVo.getBiospecimen().setBiospecimenUid(UniqueIdGenerator.generateUniqueId());
			limsVo.getBiospecimen().setParentId(oldlimsVo.getBiospecimen().getId());
			limsVo.getBiospecimen().setComments("Clone of " + biospecimenUid);

			// Inital transaction detail (quantity grabbed from previous biospecimen)
			limsVo.getBioTransaction().setId(null);
			org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
			limsVo.getBioTransaction().setQuantity(oldlimsVo.getBiospecimen().getQuantity());
			limsVo.getBioTransaction().setRecorder(currentUser.getPrincipal().toString());

			iLimsService.createBiospecimen(limsVo);
			biospecimenModalDetailForm.setModelObject(limsVo);
			this.info("Biospecimen " + limsVo.getBiospecimen().getBiospecimenUid() + " was cloned from " + biospecimenUid + " successfully");
			target.add(feedbackPanel);
			target.add(biospecimenModalDetailForm);
		}
		catch (IllegalAccessException e) {
			log.error(e.getMessage());
		}
		catch (InvocationTargetException e) {
			log.error(e.getMessage());
		}
		catch (NoSuchMethodException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * Process some, or all Biospecimen into a new Biospecimen
	 * 
	 * @param target
	 */
	private void onProcess(AjaxRequestTarget target) {
		try {
			LimsVO oldlimsVo = (LimsVO) biospecimenModalDetailForm.getModelObject();
			final String biospecimenUid = oldlimsVo.getBiospecimen().getBiospecimenUid();
			LimsVO limsVo = new LimsVO();
			PropertyUtils.copyProperties(limsVo, oldlimsVo);
			limsVo.getBiospecimen().setId(null);
			limsVo.getBiospecimen().setBiospecimenUid(UniqueIdGenerator.generateUniqueId());
			limsVo.getBiospecimen().setParentId(oldlimsVo.getBiospecimen().getId());
			limsVo.getBiospecimen().setQuantity(null);
			limsVo.getBiospecimen().setComments("Processed " + biospecimenUid);

			// Inital transaction detail
			org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
			limsVo.getBioTransaction().setRecorder(currentUser.getPrincipal().toString());

			iLimsService.createBiospecimen(limsVo);
			biospecimenModalDetailForm.setModelObject(limsVo);
			this.info("Biospecimen " + limsVo.getBiospecimen().getBiospecimenUid() + " was processed from " + biospecimenUid + " successfully");
			target.add(feedbackPanel);
			target.add(biospecimenModalDetailForm);
		}
		catch (IllegalAccessException e) {
			log.error(e.getMessage());
		}
		catch (InvocationTargetException e) {
			log.error(e.getMessage());
		}
		catch (NoSuchMethodException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * Aliquote a specified amount from a parent Biospecimen into a new child Biospecimen
	 * 
	 * @param target
	 */
	private void onAliquot(AjaxRequestTarget target) {
		try {
			LimsVO oldlimsVo = (LimsVO) biospecimenModalDetailForm.getModelObject();
			final String biospecimenUid = oldlimsVo.getBiospecimen().getBiospecimenUid();
			LimsVO limsVo = new LimsVO();
			PropertyUtils.copyProperties(limsVo, oldlimsVo);
			limsVo.getBiospecimen().setId(null);
			limsVo.getBiospecimen().setBiospecimenUid(UniqueIdGenerator.generateUniqueId());
			limsVo.getBiospecimen().setParentId(oldlimsVo.getBiospecimen().getId());
			limsVo.getBiospecimen().setQuantity(null);
			limsVo.getBiospecimen().setComments("Sub-aliquot of " + biospecimenUid);

			// Inital transaction detail
			org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
			limsVo.getBioTransaction().setRecorder(currentUser.getPrincipal().toString());

			iLimsService.createBiospecimen(limsVo);
			biospecimenModalDetailForm.setModelObject(limsVo);
			this.info("Biospecimen " + limsVo.getBiospecimen().getBiospecimenUid() + " was aliquoted from " + biospecimenUid + " successfully");
			target.add(feedbackPanel);
			target.add(biospecimenModalDetailForm);
		}
		catch (IllegalAccessException e) {
			log.error(e.getMessage());
		}
		catch (InvocationTargetException e) {
			log.error(e.getMessage());
		}
		catch (NoSuchMethodException e) {
			log.error(e.getMessage());
		}
	}

	public boolean isCloneButtonVisible() {
		return cloneButton.isVisible();
	}

	public void setCloneButtonVisible(boolean visible) {
		cloneButton.setVisible(visible);
	}

	public boolean isCloneButtonEnabled() {
		return cloneButton.isEnabled();
	}

	public void setCloneButtonEnabled(boolean enabled) {
		cloneButton.setEnabled(enabled);
	}

	public boolean isProcessButtonVisible() {
		return processButton.isVisible();
	}

	public void setProcessButtonVisible(boolean visible) {
		processButton.setVisible(visible);
	}

	public boolean isProcessButtonEnabled() {
		return processButton.isEnabled();
	}

	public void setProcessButtonEnabled(boolean enabled) {
		processButton.setEnabled(enabled);
	}

	public boolean isAliquotButtonVisible() {
		return aliquotButton.isVisible();
	}

	public void setAliquotButtonVisible(boolean visible) {
		aliquotButton.setVisible(visible);
	}

	public boolean isAliquotButtonEnabled() {
		return aliquotButton.isEnabled();
	}

	public void setAliquotButtonEnabled(boolean enabled) {
		aliquotButton.setEnabled(enabled);
	}
}