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
package au.org.theark.phenotypic.web.component.fieldDataUpload.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.pheno.entity.FileFormat;
import au.org.theark.core.model.pheno.entity.PhenoUpload;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.fieldDataUpload.DetailPanel;
import au.org.theark.phenotypic.web.component.fieldDataUpload.WizardPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings({ "serial", "unused" })
public class SearchForm extends AbstractSearchForm<UploadVO> {
	@SpringBean(name = au.org.theark.phenotypic.service.Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService						phenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>				iArkCommonService;

	private PageableListView<PhenoUpload>		listView;
	private CompoundPropertyModel<UploadVO>	cpmModel;
	private DetailPanel								detailPanel;
	private WizardPanel								wizardPanel;
	private WebMarkupContainer						wizardContainer;

	private TextField<String>						uploadIdTxtFld;
	private TextField<String>						uploadFilenameTxtFld;
	private DropDownChoice<FileFormat>			fileFormatDdc;

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<UploadVO> model, PageableListView<PhenoUpload> listView, FeedbackPanel feedBackPanel, WizardPanel wizardPanel, WebMarkupContainer listContainer,
			WebMarkupContainer searchMarkupContainer, WebMarkupContainer wizardContainer, WebMarkupContainer wizardPanelFormContainer, WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer) {

		super(id, model, wizardContainer, wizardPanelFormContainer, viewButtonContainer, editButtonContainer, searchMarkupContainer, listContainer, feedBackPanel);

		this.cpmModel = model;
		this.listView = listView;
		this.wizardPanel = wizardPanel;
		this.wizardContainer = wizardContainer;
		initialiseFieldForm();

		Long sessionPhenoCollectionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);
		disableSearchForm(sessionPhenoCollectionId, "There is no Phenotypic Collection in context. Please select a Phenotypic Collection");
	}

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<UploadVO> model, PageableListView<PhenoUpload> listView, FeedbackPanel feedBackPanel, DetailPanel detailPanel, WebMarkupContainer listContainer,
			WebMarkupContainer searchMarkupContainer, WebMarkupContainer detailContainer, WebMarkupContainer detailPanelFormContainer, WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer) {

		super(id, model, detailContainer, detailPanelFormContainer, viewButtonContainer, editButtonContainer, searchMarkupContainer, listContainer, feedBackPanel);

		this.cpmModel = model;
		this.listView = listView;
		this.detailPanel = detailPanel;
		initialiseFieldForm();

		Long sessionPhenoCollectionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);
		disableSearchForm(sessionPhenoCollectionId, "There is no Phenotypic Collection in context. Please select a Phenotypic Collection");
	}

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<UploadVO> compoundPropertyModel) {
		super(id, compoundPropertyModel);
		this.cpmModel = compoundPropertyModel;
		initialiseFieldForm();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initDropDownChoice() {
		// Initialise any drop-downs
		java.util.Collection<FileFormat> fileFormatCollection = phenotypicService.getFileFormats();
		CompoundPropertyModel<UploadVO> uploadCpm = cpmModel;
		PropertyModel<PhenoUpload> uploadPm = new PropertyModel<PhenoUpload>(uploadCpm, au.org.theark.phenotypic.web.Constants.UPLOAD);
		PropertyModel<FileFormat> fileFormatPm = new PropertyModel<FileFormat>(uploadPm, au.org.theark.phenotypic.web.Constants.FILE_FORMAT);
		ChoiceRenderer fileFormatRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.FILE_FORMAT_NAME, au.org.theark.phenotypic.web.Constants.FILE_FORMAT_ID);
		fileFormatDdc = new DropDownChoice<FileFormat>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILE_FORMAT, fileFormatPm, (List) fileFormatCollection, fileFormatRenderer);
	}

	public void initialiseFieldForm() {
		uploadIdTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_ID);
		uploadFilenameTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILENAME);

		// Set up fields on the form
		initDropDownChoice();
		addFieldComponents();
	}

	private void addFieldComponents() {
		// Add the field components
		add(uploadIdTxtFld);
		add(uploadFilenameTxtFld);
		add(fileFormatDdc);
	}

	// Reset button implemented in AbstractSearchForm

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		target.addComponent(feedbackPanel);

		// Set study in context
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		// Get a list of all Fields for the Study in context
		Study study = iArkCommonService.getStudy(studyId);

		PhenoUpload searchUpload = getModelObject().getUpload();
		searchUpload.setStudy(study);

		java.util.Collection<PhenoUpload> uploadCollection = phenotypicService.searchUpload(searchUpload);

		if (uploadCollection != null && uploadCollection.size() == 0) {
			this.info("Uploads with the specified criteria does not exist in the system.");
			target.addComponent(feedbackPanel);
		}

		getModelObject().setUploadCollection(uploadCollection);

		listView.removeAll();
		listContainer.setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.addComponent(listContainer);
	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
		// NB: Should not be possible to get here (GUI should be using Wizard for new)
		// Due to ARK-108 :: No longer reset the VO onNew(..)
		UploadVO uploadVo = getModelObject();
		uploadVo.setMode(au.org.theark.core.Constants.MODE_NEW);
		uploadVo.getUpload().setId(null); // must ensure Id is blank onNew
		setModelObject(uploadVo);

		listContainer.setVisible(false);
		searchMarkupContainer.setVisible(false);

		// Explicitly Show Wizard panel
		wizardContainer.setVisible(true);
		wizardContainer.setEnabled(true);

		target.addComponent(listContainer);
		target.addComponent(searchMarkupContainer);
		target.addComponent(detailFormCompContainer);
		target.addComponent(wizardContainer);
	}

}
