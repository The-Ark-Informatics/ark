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
package au.org.theark.study.web.component.subject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.LinkSubjectPedigree;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Relationship;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.StudyHelper;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.subject.form.ContainerForm;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings( { "unchecked", "serial" })
public class SearchResultListPanel extends Panel {


	private static final long	serialVersionUID	= -8517602411833622907L;
	private WebMarkupContainer	arkContextMarkup;
	private ContainerForm		subjectContainerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;
	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService		iStudyService;
	private WebMarkupContainer studyNameMarkup;
	private WebMarkupContainer studyLogoMarkup;

	public SearchResultListPanel(String id, WebMarkupContainer arkContextMarkup, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup) {

		super(id);
		this.subjectContainerForm = containerForm;
		this.arkContextMarkup = arkContextMarkup;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
	}

	public DataView<SubjectVO> buildDataView(ArkDataProvider<SubjectVO, IArkCommonService> subjectProvider) {

		DataView<SubjectVO> studyCompDataView = new DataView<SubjectVO>("subjectList", subjectProvider) {

			@Override
			protected void populateItem(final Item<SubjectVO> item) {
				LinkSubjectStudy subject = item.getModelObject().getLinkSubjectStudy();
				item.add(buildLink(item.getModelObject()));
				item.add(new Label(Constants.SUBJECT_FULL_NAME, item.getModelObject().getSubjectFullName()));

				if (subject != null && subject.getPerson() != null && subject.getPerson().getPreferredName() != null) {
					item.add(new Label("linkSubjectStudy.person.preferredName", subject.getPerson().getPreferredName()));
				}
				else {
					item.add(new Label("linkSubjectStudy.person.preferredName", ""));
				}

				item.add(new Label("linkSubjectStudy.person.genderType.name", subject.getPerson().getGenderType().getName()));

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				String dateOfBirth = "";
				if (subject != null && subject.getPerson() != null && subject.getPerson().getDateOfBirth() != null) {
					dateOfBirth = simpleDateFormat.format(subject.getPerson().getDateOfBirth());
					item.add(new Label("linkSubjectStudy.person.dateOfBirth", dateOfBirth));
				}
				else {
					item.add(new Label("linkSubjectStudy.person.dateOfBirth", ""));
				}

				item.add(new Label("linkSubjectStudy.person.vitalStatus.name", subject.getPerson().getVitalStatus().getName()));

				item.add(new Label("linkSubjectStudy.subjectStatus.name", subject.getSubjectStatus().getName()));

				if (subject.getConsentStatus() != null) {
					item.add(new Label("linkSubjectStudy.consentStatus.name", subject.getConsentStatus().getName()));
				}
				else {
					item.add(new Label("linkSubjectStudy.consentStatus.name", ""));
				}

				item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));
			}
		};
		return studyCompDataView;
	}
	
	public DataView<SubjectVO> buildDataView(ArkDataProvider<SubjectVO, IArkCommonService> subjectProvider,final AbstractDetailModalWindow modalWindow) {

		DataView<SubjectVO> studyCompDataView = new DataView<SubjectVO>("subjectList", subjectProvider) {

			@Override
			protected void populateItem(final Item<SubjectVO> item) {
				LinkSubjectStudy subject = item.getModelObject().getLinkSubjectStudy();
				item.add(buildLink(item.getModelObject(),modalWindow));
				item.add(new Label(Constants.SUBJECT_FULL_NAME, item.getModelObject().getSubjectFullName()));

				if (subject != null && subject.getPerson() != null && subject.getPerson().getPreferredName() != null) {
					item.add(new Label("linkSubjectStudy.person.preferredName", subject.getPerson().getPreferredName()));
				}
				else {
					item.add(new Label("linkSubjectStudy.person.preferredName", ""));
				}

				item.add(new Label("linkSubjectStudy.person.genderType.name", subject.getPerson().getGenderType().getName()));

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				String dateOfBirth = "";
				if (subject != null && subject.getPerson() != null && subject.getPerson().getDateOfBirth() != null) {
					dateOfBirth = simpleDateFormat.format(subject.getPerson().getDateOfBirth());
					item.add(new Label("linkSubjectStudy.person.dateOfBirth", dateOfBirth));
				}
				else {
					item.add(new Label("linkSubjectStudy.person.dateOfBirth", ""));
				}

				item.add(new Label("linkSubjectStudy.person.vitalStatus.name", subject.getPerson().getVitalStatus().getName()));

				item.add(new Label("linkSubjectStudy.subjectStatus.name", subject.getSubjectStatus().getName()));

				if (subject.getConsentStatus() != null) {
					item.add(new Label("linkSubjectStudy.consentStatus.name", subject.getConsentStatus().getName()));
				}
				else {
					item.add(new Label("linkSubjectStudy.consentStatus.name", ""));
				}

				item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));
			}
		};
		return studyCompDataView;
	}


	public PageableListView<SubjectVO> buildListView(IModel iModel) {

		PageableListView<SubjectVO> listView = new PageableListView<SubjectVO>(Constants.SUBJECT_LIST, iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {

			@Override
			protected void populateItem(final ListItem<SubjectVO> item) {
				LinkSubjectStudy subject = item.getModelObject().getLinkSubjectStudy();
				item.add(buildLink(item.getModelObject()));
				item.add(new Label(Constants.SUBJECT_FULL_NAME, item.getModelObject().getSubjectFullName()));

				if (subject != null && subject.getPerson() != null && subject.getPerson().getPreferredName() != null) {
					item.add(new Label("linkSubjectStudy.person.preferredName", subject.getPerson().getPreferredName()));
				}
				else {
					item.add(new Label("linkSubjectStudy.person.preferredName", ""));
				}

				item.add(new Label("linkSubjectStudy.person.genderType.name", subject.getPerson().getGenderType().getName()));

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				String dateOfBirth = "";
				if (subject != null && subject.getPerson() != null && subject.getPerson().getDateOfBirth() != null) {
					dateOfBirth = simpleDateFormat.format(subject.getPerson().getDateOfBirth());
					item.add(new Label("linkSubjectStudy.person.dateOfBirth", dateOfBirth));
				}
				else {
					item.add(new Label("linkSubjectStudy.person.dateOfBirth", ""));
				}

				item.add(new Label("linkSubjectStudy.person.vitalStatus.statusName", subject.getPerson().getVitalStatus().getName()));

				item.add(new Label("linkSubjectStudy.subjectStatus.name", subject.getSubjectStatus().getName()));

				item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));
			}
		};
		return listView;
	}

	private AjaxLink buildLink(final SubjectVO subject) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.SUBJECT_UID) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				//subject.getLinkSubjectStudy().setStudy(iArkCommonService.getStudy(sessionStudyId));

				// We specify the type of person here as Subject
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID, subject.getLinkSubjectStudy().getStudy().getId());
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID, subject.getLinkSubjectStudy().getPerson().getId());
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_TYPE, au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT);

				SubjectVO subjectFromBackend = new SubjectVO();
				Collection<SubjectVO> subjects = iArkCommonService.getSubject(subject);
				for (SubjectVO subjectVO2 : subjects) {
					subjectFromBackend = subjectVO2;
					break;
				}

				// Available/assigned child studies
				List<Study> availableChildStudies = new ArrayList<Study>(0);
				List<Study> selectedChildStudies = new ArrayList<Study>(0);

				if (subjectFromBackend.getLinkSubjectStudy().getStudy().getParentStudy() != null) {
					availableChildStudies = iStudyService.getChildStudyListOfParent(subjectFromBackend.getLinkSubjectStudy().getStudy());
					selectedChildStudies = iArkCommonService.getAssignedChildStudyListForPerson(subjectFromBackend.getLinkSubjectStudy().getStudy(), subjectFromBackend.getLinkSubjectStudy().getPerson());
				}

				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
				subjectFromBackend.setStudyList(subjectContainerForm.getModelObject().getStudyList());
				subjectContainerForm.setModelObject(subjectFromBackend);
				subjectContainerForm.getModelObject().setAvailableChildStudies(availableChildStudies);
				subjectContainerForm.getModelObject().setSelectedChildStudies(selectedChildStudies);

				// Set SubjectUID into context
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.SUBJECTUID, subjectFromBackend.getLinkSubjectStudy().getSubjectUID());
				ContextHelper contextHelper = new ContextHelper();
				contextHelper.setStudyContextLabel(target, subjectFromBackend.getLinkSubjectStudy().getStudy().getName(), arkContextMarkup);
				contextHelper.setSubjectContextLabel(target, subjectFromBackend.getLinkSubjectStudy().getSubjectUID(), arkContextMarkup);
				
				// Set Study Logo
				StudyHelper studyHelper = new StudyHelper();
				studyHelper.setStudyLogo(subjectFromBackend.getLinkSubjectStudy().getStudy(), target, studyNameMarkup, studyLogoMarkup);
			}
		};
		Label nameLinkLabel = new Label(Constants.SUBJECT_KEY_LBL, subject.getLinkSubjectStudy().getSubjectUID());
		link.add(nameLinkLabel);
		return link;
	}
	
	private AjaxLink buildLink(final SubjectVO subject,final AbstractDetailModalWindow modalWindow) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.SUBJECT_UID) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				
				LinkSubjectPedigree pedigreeRelationship = new LinkSubjectPedigree();
				pedigreeRelationship.setFamilyId(0);
				
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				
				Study study = iArkCommonService.getStudy(sessionStudyId);

				String subjectUID = (String)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
				
				SubjectVO criteriaSubjectVo = new SubjectVO();
				criteriaSubjectVo.getLinkSubjectStudy().setStudy(study);
				criteriaSubjectVo.getLinkSubjectStudy().setSubjectUID(subjectUID);
				Collection<SubjectVO> subjects = iArkCommonService.getSubject(criteriaSubjectVo);
				SubjectVO subjectVo = subjects.iterator().next();
				pedigreeRelationship.setSubject(subjectVo.getLinkSubjectStudy());
				
				criteriaSubjectVo.getLinkSubjectStudy().setSubjectUID(subject.getLinkSubjectStudy().getSubjectUID());
				subjects = iArkCommonService.getSubject(criteriaSubjectVo);
				subjectVo = subjects.iterator().next();
				pedigreeRelationship.setRelative(subjectVo.getLinkSubjectStudy());
				
				String gender = subject.getLinkSubjectStudy().getPerson().getGenderType().getName();
				
				List<Relationship> relationships=iArkCommonService.getFamilyRelationships();
				for(Relationship relationship : relationships){
					if("Male".equalsIgnoreCase(gender) 
							&& "Father".equalsIgnoreCase(relationship.getName())){
						pedigreeRelationship.setRelationship(relationship);
						break;
					}
					
					if("Female".equalsIgnoreCase(gender) 
							&& "Mother".equalsIgnoreCase(relationship.getName())){
						pedigreeRelationship.setRelationship(relationship);
						break;
					}	
				}
				
				iStudyService.create(pedigreeRelationship);
				modalWindow.close(target);
			}
		};
		Label nameLinkLabel = new Label(Constants.SUBJECT_KEY_LBL, subject.getLinkSubjectStudy().getSubjectUID());
		link.add(nameLinkLabel);
		return link;
	}
	
}
