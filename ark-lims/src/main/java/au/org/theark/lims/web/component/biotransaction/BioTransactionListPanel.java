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
package au.org.theark.lims.web.component.biotransaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioTransaction;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.link.AjaxConfirmLink;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.biotransaction.form.BioTransactionListForm;

public class BioTransactionListPanel extends Panel {
	/**
	 * 
	 */
	private static final long									serialVersionUID	= 7224168117680252835L;

	private static Logger										log					= LoggerFactory.getLogger(BioTransactionListPanel.class);
	protected CompoundPropertyModel<LimsVO>				cpModel;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService											iLimsService;

	protected FeedbackPanel										feedbackPanel;
	private BioTransactionListForm							bioTransactionListForm;

	private Label													idLbl;
	private Label													transactionDateLbl;
	private Label													quantityLbl;
	private Label													unitsLbl;
	private Label													recorderLbl;
	private Label													reasonLbl;
	private Label													statusLbl;
	private Label													requestLbl;

	protected WebMarkupContainer								dataViewListWMC;
	private DataView<BioTransaction>							dataView;
	private ArkDataProvider2<LimsVO, BioTransaction>	bioTransactionProvider;

	public BioTransactionListPanel(String id, FeedbackPanel feedbackPanel, CompoundPropertyModel<LimsVO> cpModel) {
		super(id);
		this.feedbackPanel = feedbackPanel;
		this.cpModel = cpModel;
		setOutputMarkupPlaceholderTag(true);
	}

	// Allow chaining of initialiosPanel and constructor...
	public BioTransactionListPanel initialisePanel() {
		final BioTransactionListPanel panelToRepaint = this;
		AbstractDetailModalWindow modalWindow = new AbstractDetailModalWindow("detailModalWindow") {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {
				target.add(panelToRepaint);
			}

		};

		bioTransactionListForm = new BioTransactionListForm("bioTransactionListForm", feedbackPanel, modalWindow, cpModel);
		bioTransactionListForm.initialiseForm();
		add(bioTransactionListForm);

		initialiseDataView();

		return this;
	}

	private void initialiseDataView() {
		dataViewListWMC = new WebMarkupContainer("dataViewListWMC");
		dataViewListWMC.setOutputMarkupId(true);
		// Data provider to paginate resultList
		bioTransactionProvider = new ArkDataProvider2<LimsVO, BioTransaction>() {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			public int size() {
				return iLimsService.getBioTransactionCount(criteriaModel.getObject().getBioTransaction());
			}

			public Iterator<BioTransaction> iterator(int first, int count) {
				List<BioTransaction> biospecimenList = new ArrayList<BioTransaction>();
				if (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.SEARCH)) {
					biospecimenList = iLimsService.searchPageableBioTransactions(criteriaModel.getObject().getBioTransaction(), first, count);
				}
				return biospecimenList.iterator();
			}
		};
		// Set the criteria into the data provider's model
		bioTransactionProvider.setCriteriaModel(cpModel);

		dataView = buildDataView(bioTransactionProvider);
		dataView.setItemsPerPage(au.org.theark.core.Constants.ROWS_PER_PAGE);

		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", dataView) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(dataViewListWMC);
			}
		};
		dataViewListWMC.add(pageNavigator);
		dataViewListWMC.add(dataView);
		add(dataViewListWMC);

	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of BioCollection
	 */
	public DataView<BioTransaction> buildDataView(ArkDataProvider2<LimsVO, BioTransaction> bioTransactionProvider) {

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);

		DataView<BioTransaction> bioTransactionDataView = new DataView<BioTransaction>("bioTransactionList", bioTransactionProvider) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final Item<BioTransaction> item) {
				item.setOutputMarkupId(true);
				// DO NOT store the item.getModelObject! Checking it is ok...
				final BioTransaction bioTransaction = item.getModelObject();

				WebMarkupContainer rowDetailsWMC = new WebMarkupContainer("rowDetailsWMC", item.getModel());
				AjaxConfirmLink<BioTransaction> rowDeleteLink = new AjaxConfirmLink<BioTransaction>("rowDeleteLink", new StringResourceModel("bioTransaction.confirmDelete", this, item.getModel()), item
						.getModel()) {

					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						BioTransaction bioTransaction = getModelObject();
						iLimsService.deleteBioTransaction(bioTransaction);
						this.info("Successfully removed the transaction");
						target.add(feedbackPanel);
						target.add(dataViewListWMC); // repaint the list
					}

				};
				rowDetailsWMC.add(rowDeleteLink);
				if ((bioTransaction.getStatus() != null) && (bioTransaction.getStatus().getName().equals(Constants.BIOTRANSACTION_STATUS_INITIAL_QTY))) {
					// do not allow the initial quantity to be deleted
					rowDetailsWMC.setVisible(false);
				}

				idLbl = new Label("bioTransaction.id", bioTransaction.getId().toString());
				String dateOfTransaction = "";
				if (bioTransaction.getTransactionDate() != null) {
					dateOfTransaction = simpleDateFormat.format(bioTransaction.getTransactionDate());
				}
				transactionDateLbl = new Label("bioTransaction.transactionDate", dateOfTransaction);

				if (bioTransaction.getQuantity() == null) {
					quantityLbl = new Label("bioTransaction.quantity", "0");
				}
				else {
					quantityLbl = new Label("bioTransaction.quantity", bioTransaction.getQuantity().toString());
				}
				if (bioTransaction.getBiospecimen().getUnit() == null) {
					unitsLbl = new Label("bioTransaction.biospecimen.unit.name", "");
				}
				else {
					unitsLbl = new Label("bioTransaction.biospecimen.unit.name", bioTransaction.getBiospecimen().getUnit().getName());
				}
				reasonLbl = new Label("bioTransaction.reason", bioTransaction.getReason());
				recorderLbl = new Label("bioTransaction.recorder", bioTransaction.getRecorder());
				if (bioTransaction.getStatus() == null) {
					statusLbl = new Label("bioTransaction.status.name", "");
				}
				else {
					statusLbl = new Label("bioTransaction.status.name", bioTransaction.getStatus().getName());
				}
				if (bioTransaction.getAccessRequest() == null) {
					requestLbl = new Label("bioTransaction.accessRequest.id", "");
				}
				else {
					requestLbl = new Label("bioTransaction.accessRequest.id", bioTransaction.getAccessRequest().getId().toString());
				}

				item.add(idLbl);
				item.add(transactionDateLbl);
				item.add(quantityLbl);
				item.add(unitsLbl);
				item.add(reasonLbl);
				item.add(recorderLbl);
				item.add(statusLbl);
				item.add(requestLbl);
				item.add(rowDetailsWMC);

				item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel<String>() {

					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));
			}

		};

		return bioTransactionDataView;
	}

	@Override
	protected void onBeforeRender() {
		Long sessionBiospecimenId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.lims.web.Constants.BIOSPECIMEN);

		if (sessionBiospecimenId != null) {
			Biospecimen biospecimen = null;
			boolean contextLoaded = false;
			try {
				biospecimen = iLimsService.getBiospecimen(sessionBiospecimenId);
				if (biospecimen != null) {
					contextLoaded = true;
				}
			}
			catch (EntityNotFoundException e) {
				log.error(e.getMessage());
			}

			if (contextLoaded) {
				cpModel.getObject().setBiospecimen(biospecimen);
				cpModel.getObject().getBioTransaction().setBiospecimen(biospecimen);
			}
		}

		super.onBeforeRender();
	}
}
