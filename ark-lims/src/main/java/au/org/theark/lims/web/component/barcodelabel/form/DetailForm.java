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
package au.org.theark.lims.web.component.barcodelabel.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.AjaxRequestTarget;
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

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BarcodeLabel;
import au.org.theark.core.model.lims.entity.BarcodePrinter;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.lims.service.ILimsAdminService;
import au.org.theark.lims.web.Constants;

/**
 * @author cellis
 * 
 */
public class DetailForm extends AbstractDetailForm<BarcodeLabel> {

	/**
	 * 
	 */
	private static final long					serialVersionUID	= 6808980290575067265L;

	protected static final Logger				log					= LoggerFactory.getLogger(DetailForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_ADMIN_SERVICE)
	private ILimsAdminService						iLimsAdminService;

	private TextField<Long>						idTxtFld;
	private DropDownChoice<Study>				studyDdc;
	private DropDownChoice<BarcodePrinter>	barcodePrinterDdc;
	private TextField<String>					nameTxtFld;
	private TextArea<String>					descriptionTxtArea;
	private TextField<String>					labelPrefixTxtFld;
	private TextField<String>					labelSuffixTxtFld;

	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.feedBackPanel = feedBackPanel;
	}

	public void initialiseDetailForm() {
		idTxtFld = new TextField<Long>("id");
		nameTxtFld = new TextField<String>("name");
		descriptionTxtArea = new TextArea<String>("description");
		labelPrefixTxtFld = new TextField<String>("labelPrefix");
		labelSuffixTxtFld = new TextField<String>("labelSuffix");

		initialiseStudyDdc();
		initialiseBarcodePrinterDdc();

		addDetailFormComponents();
		attachValidators();
	}

	private void initialiseStudyDdc() {
		List<Study> studyListForUser = new ArrayList<Study>(0);
		studyListForUser = getStudyListForUser();
		ChoiceRenderer<Study> choiceRenderer = new ChoiceRenderer<Study>(Constants.NAME, Constants.ID);
		studyDdc = new DropDownChoice<Study>("study", studyListForUser, choiceRenderer) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				this.setChoices(getStudyListForUser());
			}
		};
	}
	
	private void initialiseBarcodePrinterDdc() {
		List<BarcodePrinter> barcodePrinters = new ArrayList<BarcodePrinter>(0);
		barcodePrinters = iLimsAdminService.getBarcodePrinters(getStudyListForUser());
		ChoiceRenderer<BarcodePrinter> choiceRenderer = new ChoiceRenderer<BarcodePrinter>(Constants.NAME, Constants.ID);
		barcodePrinterDdc = new DropDownChoice<BarcodePrinter>("barcodePrinter", barcodePrinters, choiceRenderer);
	}

	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(idTxtFld.setEnabled(false));
		arkCrudContainerVO.getDetailPanelFormContainer().add(nameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(studyDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(barcodePrinterDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(descriptionTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(labelPrefixTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(labelSuffixTxtFld);
		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		nameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.name.required", this, new Model<String>("Name")));
		studyDdc.setRequired(true).setLabel(new StringResourceModel("error.study.required", this, new Model<String>("Study")));
		barcodePrinterDdc.setRequired(true).setLabel(new StringResourceModel("error.barcodePrinter.required", this, new Model<String>("BarcodePrinter")));
		labelPrefixTxtFld.setRequired(true).setLabel(new StringResourceModel("error.labelPrefix.required", this, new Model<String>("Prefix")));
		labelSuffixTxtFld.setRequired(true).setLabel(new StringResourceModel("error.labelSuffix.required", this, new Model<String>("Suffix")));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		containerForm.setModelObject(new BarcodeLabel());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String,
	 * org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		iLimsAdminService.deleteBarcodeLabel(containerForm.getModelObject());
		containerForm.info("The Barcode label record was deleted successfully.");
		editCancelProcess(target);
		onCancel(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	@Override
	protected void onSave(Form<BarcodeLabel> containerForm, AjaxRequestTarget target) {
		if (isNew()) {
			iLimsAdminService.createBarcodeLabel(containerForm.getModelObject());
		}
		else {
			iLimsAdminService.updateBarcodeLabel(containerForm.getModelObject());
		}
		this.info("Barcode label: " + containerForm.getModelObject().getName() + " was created/updated successfully.");
		target.add(feedBackPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Returns a list of Studies the user is permitted to access
	 * 
	 * @return
	 */
	private List<Study> getStudyListForUser() {
		List<Study> studyListForUser = new ArrayList<Study>(0);
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
		return studyListForUser;
	}
}
