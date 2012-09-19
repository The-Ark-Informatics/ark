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
package au.org.theark.lims.web.component.biotransaction.form;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.AccessRequest;
import au.org.theark.core.model.lims.entity.BioTransaction;
import au.org.theark.core.model.lims.entity.BioTransactionStatus;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;

/**
 * @author cellis
 * 
 */
public class BioTransactionListForm extends Form<BioTransaction> {
	private static final long								serialVersionUID	= 1L;

	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_SERVICE)
	private ILimsService											iLimsService;
	
	private CompoundPropertyModel<LimsVO> 					cpModel;
	private FeedbackPanel										feedbackPanel;
	private Label													transactionDateLbl;
	private TextField<Number>									quantity;
	private Label													unitsLbl;
	private Label													recorderLbl;
	private TextField<String>									reasonLbl;
	private DropDownChoice<BioTransactionStatus>			status;
	private DropDownChoice<AccessRequest>					accessRequest;
	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
	private ArkBusyAjaxButton							saveButton;

	public BioTransactionListForm(String id, FeedbackPanel feedbackPanel, CompoundPropertyModel<LimsVO> cpModel) {
		super(id);
		this.cpModel = cpModel;
		BioTransaction bioTransaction = new BioTransaction();
		bioTransaction.setBiospecimen(cpModel.getObject().getBiospecimen());
		cpModel.getObject().setBioTransaction(bioTransaction);
	}

	public void initialiseForm() {
		feedbackPanel = new FeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupPlaceholderTag(true);
		add(feedbackPanel);
		
		transactionDateLbl = new Label("bioTransaction.transactionDate", simpleDateFormat.format(new Date()));
		quantity = new TextField<Number>("bioTransaction.quantity");
		
		unitsLbl = new Label("bioTransaction.biospecimen.unit.name", cpModel.getObject().getBiospecimen().getUnit().getName());
		reasonLbl = new TextField<String>("bioTransaction.reason");
		recorderLbl = new Label("bioTransaction.recorder", SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal().toString());
		
		initStatus();
		initAccessRequest();
		initSaveButton();
		addFormComponents();
	}

	private void initStatus() {
		List<BioTransactionStatus> list = iLimsService.getBioTransactionStatusChoices();		
		ChoiceRenderer<BioTransactionStatus> choiceRenderer = new ChoiceRenderer<BioTransactionStatus>(Constants.NAME, Constants.ID);
		status = new DropDownChoice<BioTransactionStatus>("bioTransaction.status", list, choiceRenderer);
	}
	
	private void initAccessRequest() {
		List<AccessRequest> list = iLimsService.getAccessRequests();
		ChoiceRenderer<AccessRequest> choiceRenderer = new ChoiceRenderer<AccessRequest>(Constants.NAME, Constants.ID);
		accessRequest = new DropDownChoice<AccessRequest>("bioTransaction.accessRequest", list, choiceRenderer);
	}

	private void initSaveButton() {
		saveButton = new ArkBusyAjaxButton("saveButton") {
			private static final long	serialVersionUID	= 1L;

			@Override
			public boolean isVisible() {
				return true;
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Set defaults
				cpModel.getObject().getBioTransaction().setTransactionDate(new Date());
				cpModel.getObject().getBioTransaction().setRecorder(SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal().toString());
				
				Double qtyAvail = iLimsService.getQuantityAvailable(cpModel.getObject().getBiospecimen());
				Double txnQuantity = cpModel.getObject().getBioTransaction().getQuantity();
				
				if(txnQuantity == null) {
					error("Field 'Quantity' is required");
					target.add(feedbackPanel);
				}
				else {
					// Make quantity minus if positive and Aliquoted or Processed
					String status = cpModel.getObject().getBioTransaction().getStatus().getName();
					if(status != null && (status.equalsIgnoreCase("Aliquoted") || status.equalsIgnoreCase("Processed")) && txnQuantity != null && txnQuantity > 0) {
						cpModel.getObject().getBioTransaction().setQuantity(-1 * txnQuantity);
					}
					
					// Check that quantity specified not greater than available
					if(txnQuantity < 0 && (Math.abs(txnQuantity) > qtyAvail)) {
						error("When aliquoting or processing, transaction quantity may not exceed total quantity available.");
						target.add(feedbackPanel);
					}
					else {
						iLimsService.createBioTransaction(cpModel.getObject());
						info("Transaction saved successfully");
						
						// update biospecimen (qty avail)
						qtyAvail = iLimsService.getQuantityAvailable(cpModel.getObject().getBiospecimen());
						cpModel.getObject().getBiospecimen().setQuantity(qtyAvail);
						try {
							iLimsService.updateBiospecimen(cpModel.getObject());
						} catch (ArkSystemException e) {
							this.error(e.getMessage());
						}
						
						// refresh transaction form
						BioTransaction bioTransaction = new BioTransaction();
						bioTransaction.setBiospecimen(cpModel.getObject().getBiospecimen());
						cpModel.getObject().setBioTransaction(bioTransaction);
						
						target.add(form.getParent().getParent());
						target.add(feedbackPanel);
					}
				}
			}
			
			@Override
			public boolean isEnabled() {
				Biospecimen b = cpModel.getObject().getBiospecimen();
				if(b==null){
					return false;
				}
				Double qa = iLimsService.getQuantityAvailable(b);
				return (qa != null && qa > 0);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}
		};
	}
	
	private void addFormComponents() {
		add(transactionDateLbl);
		add(quantity);
		add(unitsLbl);
		add(recorderLbl);
		add(reasonLbl);
		add(status);
		add(accessRequest);
		add(saveButton);	
	}
}