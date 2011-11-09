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
package au.org.theark.lims.web.component.barcodeprinter.form;

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
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
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
public class DetailForm extends AbstractDetailForm<BarcodePrinter> {
	/**
	 * 
	 */
	private static final long			serialVersionUID		= -9040147188276890390L;
	protected static final Logger		log						= LoggerFactory.getLogger(DetailForm.class);
	private static final String		IP_V4_DOMAIN_PATTERN	= "^(\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})$";

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_ADMIN_SERVICE)
	private ILimsAdminService			iLimsAdminService;

	private TextField<String>			idTxtFld;
	private DropDownChoice<Study>		studyDdc;
	private TextField<String>			nameTxtFld;
	private TextField<String>			locationTxtFld;
	private TextField<String>			hostTxtFld;
	private TextField<String>			portTxtFld;
	private TextArea<String>			descriptionTxtArea;

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
		idTxtFld = new TextField<String>("id");
		nameTxtFld = new TextField<String>("name");
		descriptionTxtArea = new TextArea<String>("description");
		locationTxtFld = new TextField<String>("location");
		hostTxtFld = new TextField<String>("host");
		portTxtFld = new TextField<String>("port");
		initStudyDdc();

		addDetailFormComponents();
		attachValidators();
	}

	private void initStudyDdc() {
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
				studyDdc.setEnabled(isNew());
				studyDdc.setChoices(getStudyListForUser());
			}
		};
	}

	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(idTxtFld.setEnabled(false));
		arkCrudContainerVO.getDetailPanelFormContainer().add(nameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(studyDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(descriptionTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(locationTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(hostTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(portTxtFld);
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
		hostTxtFld.setRequired(true).setLabel(new StringResourceModel("error.host.required", this, new Model<String>("Host")));
		hostTxtFld.add(new PatternValidator(IP_V4_DOMAIN_PATTERN));

		portTxtFld.setRequired(true).setLabel(new StringResourceModel("error.port.required", this, new Model<String>("Port")));
		portTxtFld.add(StringValidator.minimumLength(4));
		portTxtFld.add(StringValidator.maximumLength(4));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		containerForm.setModelObject(new BarcodePrinter());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String,
	 * org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		// studyService.delete(containerForm.getModelObject().getPhone());
		iLimsAdminService.deleteBarcodePrinter(containerForm.getModelObject());
		containerForm.info("The Barcode Printer record was deleted successfully.");
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
	protected void onSave(Form<BarcodePrinter> containerForm, AjaxRequestTarget target) {
		if (containerForm.getModelObject().getId() == null) {
			iLimsAdminService.createBarcodePrinter(containerForm.getModelObject());
		}
		else {
			iLimsAdminService.updateBarcodePrinter(containerForm.getModelObject());
		}
		this.info("Barcode printer: " + containerForm.getModelObject().getName() + " was created/updated successfully.");
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

			if (isNew()) {
				List<Study> studyListAssignedToBarcodePrinter = iLimsAdminService.getStudyListAssignedToBarcodePrinter();
				studyListForUser.removeAll(studyListAssignedToBarcodePrinter);
			}
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());
		}
		return studyListForUser;
	}
}
