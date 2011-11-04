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
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
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
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.lims.service.ILimsAdminService;
import au.org.theark.lims.web.Constants;

/**
 * @author cellis
 * 
 */
public class SearchForm extends AbstractSearchForm<BarcodeLabel> {
	/**
	 * 
	 */
	private static final long					serialVersionUID	= -109811767116701473L;
	protected static final Logger				log					= LoggerFactory.getLogger(SearchForm.class);

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

	public SearchForm(String id, CompoundPropertyModel<BarcodeLabel> cpmModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel) {
		super(id, cpmModel, feedBackPanel, arkCrudContainerVO);
		this.feedbackPanel = feedBackPanel;
		initialiseSearchForm();
	}

	protected void initialiseSearchForm() {
		idTxtFld = new TextField<Long>("id");
		nameTxtFld = new TextField<String>("name");

		initialiseStudyDdc();
		initialiseBarcodePrinterDdc();

		descriptionTxtArea = new TextArea<String>("description");
		labelPrefixTxtFld = new TextField<String>("labelPrefix");
		labelSuffixTxtFld = new TextField<String>("labelSuffix");
		addSearchComponentsToForm();
	}

	private void initialiseStudyDdc() {
		PropertyModel<Study> studyPm = new PropertyModel<Study>(getModelObject(), "study");
		List<Study> studyListForUser = new ArrayList<Study>(0);
		studyListForUser = getStudyListForUser();
		ChoiceRenderer<Study> studyRenderer = new ChoiceRenderer<Study>(Constants.NAME, Constants.ID);
		studyDdc = new DropDownChoice<Study>("study", studyPm, (List<Study>) studyListForUser, studyRenderer) {
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
		PropertyModel<BarcodePrinter> BarcodePrinterPm = new PropertyModel<BarcodePrinter>(getModelObject(), "barcodePrinter");
		List<BarcodePrinter> barcodePrinters = new ArrayList<BarcodePrinter>(0);
		barcodePrinters = iLimsAdminService.getBarcodePrinters(getStudyListForUser());
		ChoiceRenderer<BarcodePrinter> barcodePrinterRenderer = new ChoiceRenderer<BarcodePrinter>(Constants.NAME, Constants.ID);
		barcodePrinterDdc = new DropDownChoice<BarcodePrinter>("barcodePrinter", BarcodePrinterPm, (List<BarcodePrinter>) barcodePrinters, barcodePrinterRenderer);
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

	protected void addSearchComponentsToForm() {
		add(idTxtFld);
		add(studyDdc);
		add(barcodePrinterDdc);
		add(nameTxtFld);
		add(descriptionTxtArea);
		add(labelPrefixTxtFld);
		add(labelSuffixTxtFld);
	}

	protected void attachValidators() {
		labelSuffixTxtFld.add(StringValidator.maximumLength(4));
	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		int count = iLimsAdminService.getBarcodeLabelCount(getModelObject());
		if (count == 0) {
			this.info("There are no records that matched your query. Please modify your filter");
			target.add(feedbackPanel);
		}

		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
		preProcessDetailPanel(target);
	}
}
