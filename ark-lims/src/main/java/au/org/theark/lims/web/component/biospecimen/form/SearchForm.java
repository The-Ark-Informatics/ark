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


import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioSampletype;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;

/**
 * @author cellis
 * 
 */
public class SearchForm extends AbstractSearchForm<LimsVO> {

	private static final long					serialVersionUID	= 3103311665813442088L;
	protected static final Logger				log					= LoggerFactory.getLogger(SearchForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;
	
	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService							iLimsService;

	private CompoundPropertyModel<LimsVO>	cpmModel;
	
	private DropDownChoice<Study>				studyDdc;
	
	private TextField<String>					idTxtFld;
	private TextField<String>					biospecimenUidTxtFld;
	private DateTextField						sampleDateTxtFld;
	private DropDownChoice<BioSampletype>	sampleTypeDdc;
	
	public SearchForm(String id, CompoundPropertyModel<LimsVO> compoundPropertyModel, FeedbackPanel feedbackPanel, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, compoundPropertyModel, feedbackPanel, arkCrudContainerVO);
		this.cpmModel = compoundPropertyModel;
		initialiseFieldForm();
		
		// Override New button, disabling
		newButton = new ArkBusyAjaxButton(Constants.NEW) {


			private static final long	serialVersionUID	= 4695227309689500914L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			}

			@Override
			public boolean isVisible() {
				return false;
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO: Fix hardcoded message::
				log.error("Error occured on the click of the New Biospecimen AjaxButton");
			}
		};
		addOrReplace(newButton);
	}

	public void initialiseFieldForm() {
		initStudyDdc();
		
		idTxtFld = new TextField<String>("biospecimen.id");
		
		biospecimenUidTxtFld = new TextField<String>("biospecimen.biospecimenUid");
		sampleDateTxtFld = new DateTextField("biospecimen.sampleDate", au.org.theark.core.Constants.DD_MM_YYYY);

		ArkDatePicker startDatePicker = new ArkDatePicker();
		startDatePicker.bind(sampleDateTxtFld);
		sampleDateTxtFld.add(startDatePicker);

		initSampleTypeDdc();

		addFieldComponents();
	}

	private void initStudyDdc() {
		CompoundPropertyModel<LimsVO> limsCpm = cpmModel;
		PropertyModel<Study> studyPm = new PropertyModel<Study>(limsCpm, "study");
		List<Study> studyListForUser = new ArrayList<Study>(0);
		try {
			Subject currentUser = SecurityUtils.getSubject();
			ArkUser arkUser = iArkCommonService.getArkUser(currentUser.getPrincipal().toString());
			ArkUserVO arkUserVo = new ArkUserVO();
			arkUserVo.setArkUserEntity(arkUser);
			
			Long sessionArkModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
			ArkModule arkModule = null;
			arkModule = iArkCommonService.getArkModuleById(sessionArkModuleId);
			//studyListForUser = iArkCommonService.getStudyListForUserAndModule(arkUserVo, arkModule);
			
			Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			Study study = null;
			if(sessionStudyId != null) {
				study = iArkCommonService.getStudy(sessionStudyId);
				studyListForUser.add(study);
			}
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());
		}
		ChoiceRenderer<Study> studyRenderer = new ChoiceRenderer<Study>(Constants.NAME, Constants.ID);
		studyDdc = new DropDownChoice<Study>("study", studyPm, (List<Study>) studyListForUser, studyRenderer);	
	}

	private void initSampleTypeDdc() {
		CompoundPropertyModel<LimsVO> limsCpm = cpmModel;
		PropertyModel<Biospecimen> biospecimenPm = new PropertyModel<Biospecimen>(limsCpm, "biospecimen");
		PropertyModel<BioSampletype> sampleTypePm = new PropertyModel<BioSampletype>(biospecimenPm, "sampleType");
		List<BioSampletype> sampleTypeList = iLimsService.getBioSampleTypes();
		ChoiceRenderer<BioSampletype> sampleTypeRenderer = new ChoiceRenderer<BioSampletype>(Constants.NAME, Constants.ID);
		sampleTypeDdc = new DropDownChoice<BioSampletype>("biospecimen.sampleType", sampleTypePm, (List<BioSampletype>) sampleTypeList, sampleTypeRenderer);
	}

	private void addFieldComponents() {
		add(studyDdc);
		add(idTxtFld);
		add(biospecimenUidTxtFld);
		add(sampleDateTxtFld);
		add(sampleTypeDdc);
	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
		// New not available from search form. Need a Subject/BioCollection in context
	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		// Refresh the FB panel if there was an old message from previous search result
		target.add(feedbackPanel);
		
		long count = iLimsService.getBiospecimenCount(cpmModel.getObject());
		if (count == 0L) {
			this.info("There are no Biospecimens with the specified criteria.");
			target.add(feedbackPanel);
		}
		
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}

	/**
	 * @param cpmModel
	 *           the cpmModel to set
	 */
	public void setCpmModel(CompoundPropertyModel<LimsVO> cpmModel) {
		this.cpmModel = cpmModel;
	}

	/**
	 * @return the cpmModel
	 */
	public CompoundPropertyModel<LimsVO> getCpmModel() {
		return cpmModel;
	}

	/**
	 * @return the newButton
	 */
	public AjaxButton getNewButton() {
		return newButton;
	}
}
