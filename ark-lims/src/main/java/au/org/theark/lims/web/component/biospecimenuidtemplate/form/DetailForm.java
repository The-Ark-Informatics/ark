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
package au.org.theark.lims.web.component.biospecimenuidtemplate.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BiospecimenUidPadChar;
import au.org.theark.core.model.lims.entity.BiospecimenUidTemplate;
import au.org.theark.core.model.lims.entity.BiospecimenUidToken;
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
public class DetailForm extends AbstractDetailForm<BiospecimenUidTemplate> {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5626119640178642204L;

	protected static final Logger							log					= LoggerFactory.getLogger(DetailForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>						iArkCommonService;

	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_ADMIN_SERVICE)
	private ILimsAdminService								iLimsAdminService;

	private TextField<Long>									idTxtFld;
	private DropDownChoice<Study>							studyDdc;
	private TextField<String>								biospecimenUidPrefixTxtFld;
	private DropDownChoice<BiospecimenUidToken>		biospecimenUidTokenDdc;
	private DropDownChoice<BiospecimenUidPadChar>	biospecimenUidPadCharDdc;
	private Label												biospecimenUidExampleLbl;
	private Model<String> biospecimenUidExampleModel = new Model<String>() {

      /**
		 * 
		 */
		private static final long	serialVersionUID	= 1L;

		public String getObject() {
          return getBiospecimenUidExample();
      }
  };

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

		initStudyDdc();		
		biospecimenUidPrefixTxtFld = new TextField<String>("biospecimenUidPrefix");
		biospecimenUidPrefixTxtFld.add(new AjaxFormComponentUpdatingBehavior("onChange") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(biospecimenUidExampleLbl);
			}
		});
		
		initBiospecimenUidTokenDdc();
		initBiospecimenUidPadCharDdc();
		
		biospecimenUidExampleLbl = new Label("biospecimenUid.example", biospecimenUidExampleModel);
		biospecimenUidExampleLbl.setOutputMarkupId(true);

		addDetailFormComponents();
		attachValidators();
	}

	public String getBiospecimenUidExample() {
		String biospecimenUidPrefix = new String("");
		String biospecimenUidToken = new String("");
		String biospecimenUidPaddedIncrementor = new String("");
		String biospecimenUidPadChar = new String("0");
		String biospecimenUidStart = new String("1");
		StringBuilder biospecimenUidExample = new StringBuilder();

		biospecimenUidPrefix = biospecimenUidPrefixTxtFld.getDefaultModelObjectAsString();
		
		if(biospecimenUidTokenDdc.getChoices().get(0) != null){
			biospecimenUidToken  = biospecimenUidTokenDdc.getChoices().get(0).getName();	
		}
		
		if( biospecimenUidPadCharDdc.getChoices().get(0) != null){
			biospecimenUidPadChar = biospecimenUidPadCharDdc.getChoices().get(0).getName();	
		}
		
		
		if(biospecimenUidPadChar.isEmpty()) {
			biospecimenUidPadChar = new String("0");
		}
		int size = Integer.parseInt(biospecimenUidPadChar);
		biospecimenUidPaddedIncrementor = StringUtils.leftPad(biospecimenUidStart, size, "0");
		
		biospecimenUidExample.append(biospecimenUidPrefix);
		biospecimenUidExample.append(biospecimenUidToken);
		biospecimenUidExample.append(biospecimenUidPaddedIncrementor);

		return biospecimenUidExample.toString();
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
				this.setChoices(getStudyListForUser());
			}
		};
	}

	private void initBiospecimenUidTokenDdc() {
		List<BiospecimenUidToken> biospecimenUidTokens = new ArrayList<BiospecimenUidToken>(0);
		biospecimenUidTokens = iLimsAdminService.getBiospecimenUidTokens();
		ChoiceRenderer<BiospecimenUidToken> choiceRenderer = new ChoiceRenderer<BiospecimenUidToken>(Constants.NAME, Constants.ID);
		biospecimenUidTokenDdc = new DropDownChoice<BiospecimenUidToken>("biospecimenUidToken", biospecimenUidTokens, choiceRenderer);
		biospecimenUidTokenDdc.add(new AjaxFormComponentUpdatingBehavior("onChange") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(biospecimenUidExampleLbl);
			}
		});
	}

	private void initBiospecimenUidPadCharDdc() {
		List<BiospecimenUidPadChar> biospecimenUidPadChars = new ArrayList<BiospecimenUidPadChar>(0);
		biospecimenUidPadChars = iLimsAdminService.getBiospecimenUidPadChars();
		ChoiceRenderer<BiospecimenUidPadChar> choiceRenderer = new ChoiceRenderer<BiospecimenUidPadChar>(Constants.NAME, Constants.ID);
		biospecimenUidPadCharDdc = new DropDownChoice<BiospecimenUidPadChar>("biospecimenUidPadChar", biospecimenUidPadChars, choiceRenderer);
		biospecimenUidPadCharDdc.add(new AjaxFormComponentUpdatingBehavior("onChange") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(biospecimenUidExampleLbl);
			}
		});
	}

	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(idTxtFld.setEnabled(false));
		arkCrudContainerVO.getDetailPanelFormContainer().add(studyDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(biospecimenUidTokenDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(biospecimenUidPrefixTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(biospecimenUidPadCharDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(biospecimenUidExampleLbl);
		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		studyDdc.setRequired(true).setLabel(new StringResourceModel("error.study.required", this, new Model<String>("Study")));
		biospecimenUidPrefixTxtFld.setRequired(true).setLabel(new StringResourceModel("error.biospecimenUidPrefix.required", this, new Model<String>("Prefix")));
		biospecimenUidTokenDdc.setRequired(true).setLabel(new StringResourceModel("error.biospecimenUidToken.required", this, new Model<String>("Token")));
		biospecimenUidTokenDdc.setRequired(true).setLabel(new StringResourceModel("error.biospecimenUidPadChars.required", this, new Model<String>("PadChars")));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		containerForm.setModelObject(new BiospecimenUidTemplate());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String,
	 * org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		containerForm.info("The Biospecimen template record was deleted successfully.");
		iLimsAdminService.deleteBiospecimenUidTemplate(containerForm.getModelObject());
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
	protected void onSave(Form<BiospecimenUidTemplate> containerForm, AjaxRequestTarget target) {
		if (isNew()) {
			iLimsAdminService.createBiospecimenUidTemplate(containerForm.getModelObject());
		}
		else {
			iLimsAdminService.updateBiospecimenUidTemplate(containerForm.getModelObject());
		}
		this.info("Biospecimen template was created/updated successfully.");
		onSavePostProcess(target);
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
