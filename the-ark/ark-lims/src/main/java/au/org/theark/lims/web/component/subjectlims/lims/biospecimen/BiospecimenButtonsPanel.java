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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.web.component.button.ArkAjaxButton;
import au.org.theark.lims.web.component.button.brady.biospecimen.PrintBiospecimenStrawLabelButton;
import au.org.theark.lims.web.component.button.zebra.biospecimen.PrintBiospecimenLabelButton;

/**
 * Panel used for all Biospecimen related workflow (Clone, Process, Aliquot)
 * 
 * @author cellis
 * 
 */
public abstract class BiospecimenButtonsPanel extends Panel {

	/**
	 * 
	 */
	private static final long				serialVersionUID	= 1L;
	private static final Logger			log					= LoggerFactory.getLogger(BiospecimenButtonsPanel.class);

	protected AjaxButton						cloneButton;
	protected AjaxButton						processButton;
	protected AjaxButton						aliquotButton;
	protected AjaxButton						printBarcodeButton;
	protected AjaxButton						printStrawBarcodeButton;

	public BiospecimenButtonsPanel(String id, IModel<Biospecimen> model) {
		super(id, model);
		setOutputMarkupPlaceholderTag(true);
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
				return super.isVisible() && ArkPermissionHelper.isActionPermitted(Constants.SAVE);
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
				return super.isVisible() && ArkPermissionHelper.isActionPermitted(Constants.SAVE);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Make sure the button is visible and enabled before allowing it to proceed
				if (aliquotButton.isVisible() && aliquotButton.isEnabled()) {
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

		printBarcodeButton = new PrintBiospecimenLabelButton("printBarcode", BiospecimenButtonsPanel.this.getDefaultModel()) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onPostSubmit(AjaxRequestTarget target, Form<?> form) {
				onPrintBarcode(target);
			}
		};
		this.add(printBarcodeButton);
		
		printStrawBarcodeButton = new PrintBiospecimenStrawLabelButton("printStrawBarcode", BiospecimenButtonsPanel.this.getDefaultModel()) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onPostSubmit(AjaxRequestTarget target, Form<?> form) {
				onPrintStrawBarcode(target);
			}
		};
		this.add(printStrawBarcodeButton);
	}

	/**
	 * Calling class to implement clone functionality 
	 * @param target
	 */
	public abstract void onClone(AjaxRequestTarget target);

	/**
	 * Calling class to implement process functionality 
	 * @param target
	 */
	public abstract void onProcess(AjaxRequestTarget target);

	/**
	 * Calling class to implement aliquot functionality 
	 * @param target
	 */
	public abstract void onAliquot(AjaxRequestTarget target);

	/**
	 * Calling class to implement print barcode functionality 
	 * @param target
	 */
	public abstract void onPrintBarcode(AjaxRequestTarget target);
	
	/**
	 * Calling class to implement clone functionality 
	 * @param target
	 * @return 
	 */
	public abstract void onPrintStrawBarcode(AjaxRequestTarget target);

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