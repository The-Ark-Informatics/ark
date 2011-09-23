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
package au.org.theark.lims.web.component.biospecimen.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioSampletype;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.biospecimen.DetailPanel;
import au.org.theark.lims.web.component.biospecimen.SearchPanel;

/**
 * @author cellis
 * 
 */
public class DetailForm extends AbstractDetailForm<LimsVO> {
	/**
	 * 
	 */
	private static final long					serialVersionUID	= 5940802332582675794L;
	private static final Logger				log					= LoggerFactory.getLogger(DetailForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService							iLimsService;

	private int										mode;

	private TextField<String>					idTxtFld;
	private TextField<String>					biospecimenUidTxtFld;
	private TextArea<String>					commentsTxtAreaFld;
	private DateTextField						sampleDateTxtFld;
	private DropDownChoice<BioSampletype>	sampleTypeDdc;
	private DropDownChoice<BioCollection>	bioCollectionDdc;
	private TextField<String>					quantityTxtFld;
	private CheckBox								barcodedChkBox;

	private WebMarkupContainer					arkContextMarkup;
	private String									subjectUIDInContext;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param detailPanel
	 * @param listContainer
	 * @param detailsContainer
	 * @param containerForm
	 * @param viewButtonContainer
	 * @param editButtonContainer
	 * @param detailFormContainer
	 * @param searchPanelContainer
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, DetailPanel detailPanel, WebMarkupContainer listContainer, WebMarkupContainer detailsContainer,
			AbstractContainerForm<LimsVO> containerForm, WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer, WebMarkupContainer detailFormContainer,
			WebMarkupContainer searchPanelContainer, WebMarkupContainer arkContextMarkup) {

		super(id, feedBackPanel, listContainer, detailsContainer, detailFormContainer, searchPanelContainer, viewButtonContainer, editButtonContainer, containerForm);

		this.setArkContextMarkup(arkContextMarkup);
	}

	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("biospecimen.id");
		biospecimenUidTxtFld = new TextField<String>("biospecimen.biospecimenUid");
		commentsTxtAreaFld = new TextArea<String>("biospecimen.comments");
		sampleDateTxtFld = new DateTextField("biospecimen.sampleDate", au.org.theark.core.Constants.DD_MM_YYYY);
		quantityTxtFld = new TextField<String>("biospecimen.quantity");

		ArkDatePicker startDatePicker = new ArkDatePicker();
		startDatePicker.bind(sampleDateTxtFld);
		sampleDateTxtFld.add(startDatePicker);

		initSampleTypeDdc();
		initBioCollectionDdc();
		
		barcodedChkBox = new CheckBox("biospecimen.barcoded");
		barcodedChkBox.setVisible(true);

		attachValidators();
		addComponents();
		
		// Focus on Sample Type dropdown
		sampleTypeDdc.add(new ArkDefaultFormFocusBehavior());
	}

	private void initSampleTypeDdc() {
		List<BioSampletype> sampleTypeList = iLimsService.getBioSampleTypes();
		ChoiceRenderer<BioSampletype> sampleTypeRenderer = new ChoiceRenderer<BioSampletype>(Constants.NAME, Constants.ID);
		sampleTypeDdc = new DropDownChoice<BioSampletype>("biospecimen.sampleType", (List<BioSampletype>) sampleTypeList, sampleTypeRenderer);
	}

	private void initBioCollectionDdc() {
		// Get a list of collections for the subject in context by default
		BioCollection bioCollection = new BioCollection();
		bioCollection.setLinkSubjectStudy(containerForm.getModelObject().getLinkSubjectStudy());
		bioCollection.setStudy(containerForm.getModelObject().getStudy());
		try {
			containerForm.getModelObject().setBioCollectionList(iLimsService.searchBioCollection(bioCollection));

			ChoiceRenderer<BioCollection> bioCollectionRenderer = new ChoiceRenderer<BioCollection>(Constants.NAME, Constants.ID);
			bioCollectionDdc = new DropDownChoice<BioCollection>("biospecimen.bioCollection", containerForm.getModelObject().getBioCollectionList(), bioCollectionRenderer);
		}
		catch (ArkSystemException e) {
			log.error(e.getMessage());
			this.error("Operation could not be performed - if this persists, contact your Administrator or Support");
		}
	}

	protected void attachValidators() {
		idTxtFld.setRequired(true);
		biospecimenUidTxtFld.setRequired(true).setLabel(new StringResourceModel("error.biospecimen.biospecimenId.required", this, new Model<String>("Name")));
		sampleTypeDdc.setRequired(true).setLabel(new StringResourceModel("error.biospecimen.sampleType.required", this, new Model<String>("Name")));
		bioCollectionDdc.setRequired(true).setLabel(new StringResourceModel("error.biospecimen.bioCollection.required", this, new Model<String>("Name")));
	}

	private void addComponents() {
		detailPanelFormContainer.add(idTxtFld.setEnabled(false));
		detailPanelFormContainer.add(biospecimenUidTxtFld.setEnabled(false));
		detailPanelFormContainer.add(commentsTxtAreaFld);
		detailPanelFormContainer.add(sampleDateTxtFld);
		detailPanelFormContainer.add(sampleTypeDdc);
		detailPanelFormContainer.add(bioCollectionDdc);
		detailPanelFormContainer.add(quantityTxtFld);
		detailPanelFormContainer.add(barcodedChkBox);
		add(detailPanelFormContainer);
	}

	@Override
	protected void onSave(Form<LimsVO> containerForm, AjaxRequestTarget target) {
		// Subject in context
		LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
		subjectUIDInContext = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);

		try {
			linkSubjectStudy = iArkCommonService.getSubjectByUID(subjectUIDInContext);
			containerForm.getModelObject().getBiospecimen().setLinkSubjectStudy(linkSubjectStudy);

			if (containerForm.getModelObject().getBiospecimen().getId() == null) {
				// Save
				iLimsService.createBiospecimen(containerForm.getModelObject());
				this.info("Biospecimen " + containerForm.getModelObject().getBiospecimen().getBiospecimenUid() + " was created successfully");
				processErrors(target);
			}
			else {
				// Update
				iLimsService.updateBiospecimen(containerForm.getModelObject());
				this.info("Biospecimen " + containerForm.getModelObject().getBiospecimen().getBiospecimenUid() + " was updated successfully");
				processErrors(target);
			}

			onSavePostProcess(target);
		}
		catch (EntityNotFoundException e) {
			this.error(e.getMessage());
		}
		catch (NullPointerException e) {
			this.error("Cannot save a Biospecimen without a Subject in context");
		}
	}

	protected void onCancel(AjaxRequestTarget target) {
		LimsVO limsVo = new LimsVO();
		containerForm.setModelObject(limsVo);

		// Switched to the new Biospecimen results list based on DataView/DataProvidor
		// (i.e. does not use the SearchResultsListPanel in this package parent)
//		java.util.List<Biospecimen> biospecimenList = new ArrayList<Biospecimen>(0);
//		try {
//			biospecimenList = iLimsService.searchBiospecimen(limsVo.getBiospecimen());
//		}
//		catch (ArkSystemException e) {
//			this.error(e.getMessage());
//		}
//		containerForm.getModelObject().setBiospecimenList(biospecimenList);

		// Enable New button now SubjectUID in context (from biospecimen selection)
		WebMarkupContainer wmc = searchPanelContainer;
		SearchPanel searchPanel = (SearchPanel) wmc.get("searchPanel");
		SearchForm searchForm = (SearchForm) searchPanel.get("searchForm");
		AjaxButton newButton = searchForm.getNewButton();
		newButton.setEnabled(true);
		// target.add(newButton);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		iLimsService.deleteBiospecimen(containerForm.getModelObject());
		this.info("Biospecimen " + containerForm.getModelObject().getBiospecimen().getBiospecimenUid() + " was deleted successfully");

		// Display delete confirmation message
		target.add(feedBackPanel);

		// Move focus back to Search form
		LimsVO limsVo = new LimsVO();
		containerForm.setModelObject(limsVo);
		editCancelProcess(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getBiospecimen().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * @return the deleteButton
	 */
	public AjaxButton getDeleteButton() {
		return deleteButton;
	}

	/**
	 * @param deleteButton
	 *           the deleteButton to set
	 */
	public void setDeleteButton(AjaxButton deleteButton) {
		this.deleteButton = deleteButton;
	}

	/**
	 * @param arkContextMarkup
	 *           the arkContextMarkup to set
	 */
	public void setArkContextMarkup(WebMarkupContainer arkContextMarkup) {
		this.arkContextMarkup = arkContextMarkup;
	}

	/**
	 * @return the arkContextMarkup
	 */
	public WebMarkupContainer getArkContextMarkup() {
		return arkContextMarkup;
	}

	/**
	 * @param mode
	 *           the mode to set
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}
}
