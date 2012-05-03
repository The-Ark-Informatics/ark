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
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
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
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.lims.service.ILimsAdminService;
import au.org.theark.lims.web.Constants;

/**
 * @author cellis
 * 
 */
public class SearchForm extends AbstractSearchForm<BarcodePrinter> {
	/**
	 * 
	 */
	private static final long			serialVersionUID	= -109811767116701473L;
	protected static final Logger		log					= LoggerFactory.getLogger(SearchForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;
	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_ADMIN_SERVICE)
	private ILimsAdminService			iLimsAdminService;

	private TextField<Long>				idTxtFld;
	private DropDownChoice<Study>		studyDdc;
	private TextField<String>			nameTxtFld;
	private TextArea<String>			descriptionTxtArea;

	public SearchForm(String id, CompoundPropertyModel<BarcodePrinter> cpmModel, final ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel) {
		super(id, cpmModel, feedBackPanel, arkCrudContainerVO);
		this.feedbackPanel = feedBackPanel;
		initialiseSearchForm();
		
		newButton = new ArkBusyAjaxButton(Constants.NEW) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1666656098281624401L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Make the details panel visible, disabling delete button (if found)
				// AjaxButton ajaxButton = (AjaxButton) editButtonContainer.get("delete");
				AjaxButton ajaxButton = (AjaxButton) arkCrudContainerVO.getEditButtonContainer().get("delete");
				if (ajaxButton != null) {
					ajaxButton.setEnabled(false);
					target.add(ajaxButton);
				}
				// Call abstract method
				onNew(target);
			}

			@Override
			public boolean isVisible() {
				return true;
			}

			@Override
			protected void onError(final AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}
		};
		addOrReplace(newButton);
	}

	protected void initialiseSearchForm() {
		idTxtFld = new TextField<Long>("id");

		initialiseStudyDdc();

		nameTxtFld = new TextField<String>("name");
		descriptionTxtArea = new TextArea<String>("description");

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
		add(nameTxtFld);
		add(descriptionTxtArea);
	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		long count = iLimsAdminService.getBarcodePrinterCount(getModelObject());
		if (count == 0L) {
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
