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
package au.org.theark.study.web.component.phone.form;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.PhoneVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.model.vo.StudyCompVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.phone.DetailPanel;

/**
 * @author Nivedan
 * 
 */
@SuppressWarnings({ "serial", "unchecked" })
public class SearchForm extends AbstractSearchForm<PhoneVO> {

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService						studyService;

	private DetailPanel							detailPanel;
	private PageableListView<Phone>			pageableListView;
	private CompoundPropertyModel<PhoneVO>	cpmModel;

	private TextField<Long>						phoneIdTxtFld;
	private TextField<String>					areaCodeTxtFld;
	private TextField<String>					phoneNumberTxtFld;
	private DropDownChoice<PhoneType>		phoneTypeChoice;
	private ArkCrudContainerVO				arkCrudContainerVO;
	
	
	public SearchForm(String id, CompoundPropertyModel<PhoneVO> cpmModel,ArkCrudContainerVO arkCrudContainerVO,FeedbackPanel feedBackPanel,PageableListView<Phone> listView){
		super(id,cpmModel,feedBackPanel,arkCrudContainerVO);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
		Label generalTextLbl = new Label("generalLbl", new StringResourceModel("search.panel.text", new Model() ));
		add(generalTextLbl);
		resetButton.setVisible(false);
		searchButton.setVisible(false);
		//initialiseSearchForm();
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		disableSearchForm(sessionPersonId, "There is no subject or contact in context. Please select a Subject or Contact.");
	}

	protected void initialiseSearchForm() {
		phoneIdTxtFld = new TextField<Long>("phone.id");
		phoneIdTxtFld.setType(Long.class);
		areaCodeTxtFld = new TextField<String>("phone.areaCode");
		phoneNumberTxtFld = new TextField<String>("phone.phoneNumber");

		List<PhoneType> phoneTypeList = iArkCommonService.getListOfPhoneType();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		phoneTypeChoice = new DropDownChoice("phone.phoneType", phoneTypeList, defaultChoiceRenderer);
		//addSearchComponentsToForm();

	}

	protected void addSearchComponentsToForm() {
		add(phoneIdTxtFld);
		add(areaCodeTxtFld);
		add(phoneNumberTxtFld);
		add(phoneTypeChoice);
	}

	protected void attachValidators() {
		phoneIdTxtFld.add(new RangeValidator<Long>(new Long(0), Long.MAX_VALUE));
		areaCodeTxtFld.add(StringValidator.maximumLength(10));
	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		String sessionPersonType = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_TYPE);// Subject or
																																														// Contact:
																																														// Denotes
																																														// if it was
																																														// a subject
																																														// or
																																														// contact
																																														// placed in
																																														// session
		try {

			Phone phone = getModelObject().getPhone();
			phone.setPerson(studyService.getPerson(sessionPersonId));

			// if(sessionPersonType.equalsIgnoreCase(au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT)){
			//
			// }else if(sessionPersonType.equalsIgnoreCase(au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_CONTACT)){
			//
			// }
			Collection<Phone> phones = studyService.getPersonPhoneList(sessionPersonId, getModelObject().getPhone());
			if (phones != null && phones.size() == 0) {
				this.info("No records match the specified criteria.");
				target.add(feedbackPanel);
			}

			getModelObject().setPhoneList(phones);
			pageableListView.removeAll();
			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
			target.add(arkCrudContainerVO.getSearchResultPanelContainer());
		}
		catch (EntityNotFoundException entityNotFoundException) {
			this.warn("There are no phone items available for the specified criteria.");
			target.add(feedbackPanel);

		}
		catch (ArkSystemException arkException) {
			this.error("The Ark Application has encountered a system error.");
			target.add(feedbackPanel);
		}

	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
		// ARK-108:: no longer do full reset to VO
		getModelObject().getPhone().setId(null); // only reset ID (not user definable)
		preProcessDetailPanel(target);
	}

}
