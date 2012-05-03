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
package au.org.theark.study.web.component.managestudy;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.lims.entity.BioCollectionUidTemplate;
import au.org.theark.core.model.lims.entity.BiospecimenUidTemplate;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkLdapRealm;
import au.org.theark.core.security.ModuleConstants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.StudyCrudContainerVO;
import au.org.theark.core.web.StudyHelper;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.component.managestudy.form.Container;
import au.org.theark.study.web.component.managestudy.form.DetailForm;

public class SearchResultListPanel extends Panel {
	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService			studyService;

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;
	private NonCachingImage			studyLogoImage;
	private transient StudyHelper	studyHelper;
	private TabbedPanel				moduleTabbedPanel;

	@SpringBean(name = "arkLdapRealm")
	private ArkLdapRealm				realm;

	private Container					studyContainerForm;
	private StudyCrudContainerVO	studyCrudContainerVO;
	private Label						childStudyNote		= new Label("childStudyNote", "+ Indicates a child study");
	private boolean					childStudiesExist	= false;

	public SearchResultListPanel(String id, StudyCrudContainerVO studyCrudContainerVO, Container containerForm) {
		super(id);
		this.studyCrudContainerVO = studyCrudContainerVO;
		studyContainerForm = containerForm;
	}

	public SearchResultListPanel(String id, StudyCrudContainerVO studyCrudContainerVO, Container containerForm, TabbedPanel moduleTabbedPanel) {
		super(id);
		this.studyCrudContainerVO = studyCrudContainerVO;
		this.studyContainerForm = containerForm;
		this.moduleTabbedPanel = moduleTabbedPanel;

		childStudyNote.setOutputMarkupId(true);
		childStudyNote.setVisible(false);
		add(childStudyNote);
	}

	@SuppressWarnings("unchecked")
	public PageableListView<Study> buildPageableListView(IModel iModel, final WebMarkupContainer searchResultsContainer) {
		PageableListView<Study> studyPageableListView = new PageableListView<Study>("studyList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<Study> item) {

				Study study = item.getModelObject();

				if (study.getId() != null) {
					item.add(new Label("id", study.getId().toString()));
				}
				else {
					item.add(new Label("id", ""));
				}

				childStudiesExist = (study.getParentStudy() != null);
				// Add "+" indicator if study has a parent (but handle for actual parent)
				Label childStudyLabel = new Label("childStudyIndicator", "+");
				childStudyLabel.setVisible(childStudiesExist && (study != study.getParentStudy()));
				childStudyNote.setVisible(childStudiesExist);

				item.add(childStudyLabel);
				item.add(buildLink(study, searchResultsContainer));

				if (study.getContactPerson() != null) {
					item.add(new Label("contact", study.getContactPerson()));// the ID here must match the ones in mark-up
				}
				else {
					item.add(new Label("contact", ""));// the ID here must match the ones in mark-up
				}

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DD_MM_YYYY);
				String dateOfApplication = "";
				if (study.getDateOfApplication() != null) {
					dateOfApplication = simpleDateFormat.format(study.getDateOfApplication());
					item.add(new Label("dateOfApplication", dateOfApplication));
				}
				else {
					item.add(new Label("dateOfApplication", dateOfApplication));
				}

				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}
		};
		return studyPageableListView;
	}

	private AjaxLink<Study> buildLink(final Study study, final WebMarkupContainer searchResultsContainer) {
		ArkBusyAjaxLink<Study> link = new ArkBusyAjaxLink<Study>("studyName") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(AjaxRequestTarget target) {
				Subject currentUser = SecurityUtils.getSubject();

				// Place the selected study in session context for the user
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID, study.getId());
				SecurityUtils.getSubject().getSession().removeAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
				SecurityUtils.getSubject().getSession().removeAttribute(au.org.theark.core.Constants.PERSON_TYPE);
				// Clear out any Subject UID placed in session via LIMS
				SecurityUtils.getSubject().getSession().removeAttribute(au.org.theark.core.Constants.SUBJECTUID);
				// Force clearing of Cache to re-load roles for the user for the study
				realm.clearCachedAuthorizationInfo(currentUser.getPrincipals());

				Study searchStudy = iArkCommonService.getStudy(study.getId());
				Study parentStudy = searchStudy.getParentStudy();

				// Check if the study has been linked to a Main Study and if so get a reference to the main study.
				// studyContainerForm.getModelObject().setLinkedToStudy(linkedToStudy)

				studyContainerForm.getModelObject().setStudy(searchStudy);
				studyContainerForm.getModelObject().setSubjectUidExample(iArkCommonService.getSubjectUidExample(searchStudy));

				WebMarkupContainer wmc = (WebMarkupContainer) studyCrudContainerVO.getDetailPanelContainer();
				DetailPanel detailsPanel = (DetailPanel) wmc.get("detailPanel");
				DetailForm detailForm = (DetailForm) detailsPanel.get("detailForm");

				// All SubjectUID generator fields grouped within a container(s)
				WebMarkupContainer autoSubjectUidcontainer = detailForm.getAutoSubjectUidContainer();
				WebMarkupContainer subjectUidcontainer = detailForm.getSubjectUidContainer();

				// Disable all SubjectUID generation fields if subjects exist or is a child study
				if (iArkCommonService.studyHasSubjects(searchStudy) || parentStudy != null) {
					autoSubjectUidcontainer.setEnabled(false);
					subjectUidcontainer.setEnabled(false);
				}
				else {
					autoSubjectUidcontainer.setEnabled(true);
					if (studyContainerForm.getModelObject().getStudy().getAutoGenerateSubjectUid()) {
						subjectUidcontainer.setEnabled(true);
					}
					else {
						subjectUidcontainer.setEnabled(false);
					}
				}

				// Enable bioSpecimenUidContainer if there are no biospecimens created as yet
				WebMarkupContainer biospecimenUidContainer = detailForm.getBiospecimenUidContainer();
				if (iArkCommonService.studyHasBiospecimen(studyContainerForm.getModelObject().getStudy())) {
					biospecimenUidContainer.setEnabled(false);
				}
				else {
					biospecimenUidContainer.setEnabled(parentStudy == null);
				}

				WebMarkupContainer bioCollectionUidContainer = detailForm.getBioCollectionUidContainer();
				if (iArkCommonService.studyHasBioCollection(studyContainerForm.getModelObject().getStudy())) {
					bioCollectionUidContainer.setEnabled(false);
				}
				else {
					bioCollectionUidContainer.setEnabled(parentStudy == null);
				}

				// Get the Source Modules from database
				Collection<ArkModule> availableArkModules = iArkCommonService.getEntityList(ArkModule.class);
				// Hide Admin and Reporting modules from "Available" view
				availableArkModules.remove(iArkCommonService.getArkModuleByName(ModuleConstants.ARK_MODULE_ADMIN));
				availableArkModules.remove(iArkCommonService.getArkModuleByName(ModuleConstants.ARK_MODULE_REPORTING));

				// Get the Modules for the Study from database
				Collection<ArkModule> arkModulesLinkedToStudy = iArkCommonService.getArkModulesLinkedWithStudy(searchStudy);

				studyContainerForm.getModelObject().setAvailableArkModules(availableArkModules);
				studyContainerForm.getModelObject().setSelectedArkModules(arkModulesLinkedToStudy);

				// Store module names linked to study in session
				for (Iterator iterator = arkModulesLinkedToStudy.iterator(); iterator.hasNext();) {
					ArkModule arkModule = (ArkModule) iterator.next();
					SecurityUtils.getSubject().getSession().setAttribute(arkModule.getName(), arkModule.getName());
				}
				studyCrudContainerVO.getSummaryContainer().setVisible(true);

				// Set Study Logo
				studyHelper = new StudyHelper();
				studyHelper.setStudyLogo(searchStudy, target, studyCrudContainerVO.getStudyNameMarkup(), studyCrudContainerVO.getStudyLogoMarkup());

				// Set Context items
				ContextHelper contextHelper = new ContextHelper();
				contextHelper.resetContextLabel(target, studyCrudContainerVO.getArkContextMarkup());
				contextHelper.setStudyContextLabel(target, searchStudy.getName(), studyCrudContainerVO.getArkContextMarkup());

				target.add(studyCrudContainerVO.getSummaryContainer());
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, studyCrudContainerVO);

				// Example auto-generated SubjectUID
				Label subjectUidExampleLbl = detailForm.getSubjectUidExampleLbl();

				// Get the Biospecimen UID Pattern if present for the given study
				BiospecimenUidTemplate biospecimentUidTemplate = null;
				// Get the BioCollection UID pattern if present
				BioCollectionUidTemplate bioCollectionUidTemplate = null;

				if (parentStudy == null || searchStudy == parentStudy) {
					subjectUidExampleLbl.setDefaultModelObject(iArkCommonService.getSubjectUidExample(searchStudy));
					biospecimentUidTemplate = iArkCommonService.getBiospecimenUidTemplate(searchStudy);
					bioCollectionUidTemplate = iArkCommonService.getBioCollectionUidTemplate(searchStudy);
				}
				else {
					// Disable parent study drop down
					detailForm.getLinkedToStudyDDContainer().setEnabled(false);
					subjectUidExampleLbl.setDefaultModelObject(iArkCommonService.getSubjectUidExample(parentStudy));
					biospecimentUidTemplate = iArkCommonService.getBiospecimenUidTemplate(parentStudy);
					bioCollectionUidTemplate = iArkCommonService.getBioCollectionUidTemplate(parentStudy);
				}

				if (biospecimentUidTemplate != null) {
					studyContainerForm.getModelObject().setBiospecimenUidTemplate(biospecimentUidTemplate);
				}

				if (bioCollectionUidTemplate != null) {
					studyContainerForm.getModelObject().setBioCollectionUidTemplate(bioCollectionUidTemplate);
				}
				target.add(subjectUidExampleLbl);

				boolean isChildStudy = (parentStudy != null && parentStudy != searchStudy);
				detailForm.getSubjectFileUploadContainer().setVisible(isChildStudy);
				detailForm.getSubjectFileUploadContainer().setEnabled(isChildStudy);

				long totalSubjects = iArkCommonService.getCountOfSubjects(searchStudy);
				long totalSubjectsOfParent = iArkCommonService.getCountOfSubjects(searchStudy.getParentStudy());

				studyContainerForm.getModelObject().setTotalSubjects(totalSubjects);
				studyContainerForm.getModelObject().setTotalSubjectsOfParent(totalSubjectsOfParent);

				// Refresh base container form to remove any feedBack messages
				target.add(autoSubjectUidcontainer);
				target.add(subjectUidcontainer);
				target.add(bioCollectionUidContainer);
				target.add(biospecimenUidContainer);
				target.add(studyContainerForm);
				target.add(moduleTabbedPanel);
			}
		};

		// Add the label for the link
		Label studyNameLinkLabel = new Label("studyNameLink", study.getName());
		link.add(studyNameLinkLabel);
		return link;
	}

	/**
	 * @param studyLogoImage
	 *           the studyLogoImage to set
	 */
	public void setStudyLogoImage(NonCachingImage studyLogoImage) {
		this.studyLogoImage = studyLogoImage;
	}

	/**
	 * @return the studyLogoImage
	 */
	public NonCachingImage getStudyLogoImage() {
		return studyLogoImage;
	}
}