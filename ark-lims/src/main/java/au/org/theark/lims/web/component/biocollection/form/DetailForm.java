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
package au.org.theark.lims.web.component.biocollection.form;

import java.util.ArrayList;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.biocollection.DetailPanel;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings({ "serial", "unused" })
public class DetailForm extends AbstractDetailForm<LimsVO> {
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService					iLimsService;

	private ContainerForm				fieldContainerForm;

	private int								mode;

	private TextField<String>			idTxtFld;
	private TextField<String>			nameTxtFld;
	private TextField<String>			collectionIdTxtFld;
	private TextArea<String>			commentsTxtAreaFld;
	private DateTextField				collectionDateTxtFld;
	private DateTextField				surgeryDateTxtFld;

	private WebMarkupContainer			arkContextMarkup;

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

		this.arkContextMarkup = arkContextMarkup;

		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		// disableDetailForm(sessionPersonId, "There is no subject in context. Please select a Subject.");
	}

	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("bioCollection.id");
		nameTxtFld = new TextField<String>("bioCollection.name");
		commentsTxtAreaFld = new TextArea<String>("bioCollection.comments");
		collectionDateTxtFld = new DateTextField("bioCollection.collectionDate", au.org.theark.core.Constants.DD_MM_YYYY);
		surgeryDateTxtFld = new DateTextField("bioCollection.surgeryDate", au.org.theark.core.Constants.DD_MM_YYYY);

		ArkDatePicker startDatePicker = new ArkDatePicker();
		startDatePicker.bind(collectionDateTxtFld);
		collectionDateTxtFld.add(startDatePicker);

		ArkDatePicker endDatePicker = new ArkDatePicker();
		endDatePicker.bind(surgeryDateTxtFld);
		surgeryDateTxtFld.add(endDatePicker);

		attachValidators();
		addComponents();
		
		// Focus on Collection Date
		collectionDateTxtFld.add(new ArkDefaultFormFocusBehavior());
	}

	protected void attachValidators() {
		idTxtFld.setRequired(true);
		nameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.bioCollection.name.required", this, new Model<String>("Name")));
	}

	private void addComponents() {
		detailPanelFormContainer.add(idTxtFld.setEnabled(false));
		detailPanelFormContainer.add(nameTxtFld);
		detailPanelFormContainer.add(commentsTxtAreaFld);
		detailPanelFormContainer.add(collectionDateTxtFld);
		detailPanelFormContainer.add(surgeryDateTxtFld);
		add(detailPanelFormContainer);
	}

	@Override
	protected void onSave(Form<LimsVO> containerForm, AjaxRequestTarget target) {
		// Subject in context
		LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
		String subjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);

		try {
			linkSubjectStudy = iArkCommonService.getSubjectByUID(subjectUID);
			containerForm.getModelObject().getBioCollection().setLinkSubjectStudy(linkSubjectStudy);

			if (containerForm.getModelObject().getBioCollection().getId() == null) {
				// Save
				iLimsService.createBioCollection(containerForm.getModelObject());
				this.info("Biospecimen collection " + containerForm.getModelObject().getBioCollection().getName() + " was created successfully");
				processErrors(target);
			}
			else {
				// Update
				iLimsService.updateBioCollection(containerForm.getModelObject());
				this.info("Biospecimen collection " + containerForm.getModelObject().getBioCollection().getName() + " was updated successfully");
				processErrors(target);
			}

			onSavePostProcess(target);
		}
		catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void onCancel(AjaxRequestTarget target) {
		LimsVO limsVo = new LimsVO();
		containerForm.setModelObject(limsVo);

		java.util.List<au.org.theark.core.model.lims.entity.BioCollection> bioCollectionList = new ArrayList<BioCollection>(0);
		try {
			bioCollectionList = iLimsService.searchBioCollection(limsVo.getBioCollection());
		}
		catch (ArkSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		containerForm.getModelObject().setBioCollectionList(bioCollectionList);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.addComponent(feedBackPanel);
	}

	public AjaxButton getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(AjaxButton deleteButton) {
		this.deleteButton = deleteButton;
	}

	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		iLimsService.deleteBioCollection(containerForm.getModelObject());
		this.info("Biospecimen collection " + containerForm.getModelObject().getBioCollection().getName() + " was deleted successfully");

		// Display delete confirmation message
		target.addComponent(feedBackPanel);

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
		if (containerForm.getModelObject().getBioCollection().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}
}
