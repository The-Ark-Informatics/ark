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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.StudyHelper;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.core.web.component.palette.ArkPalette;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.component.subject.form.ContainerForm;

public class ChildStudySubjectPanel extends Panel {

	private static final long		serialVersionUID	= 1L;
	protected Label					assignedChildStudiesLabel;
	protected ArkPalette<Study>	assignedChildStudiesPalette;
	
	private WebMarkupContainer	arkContextMarkup;
	private ContainerForm		subjectContainerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;
	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService		iStudyService;
	
	List<LinkSubjectStudy> childSubjectIds = new ArrayList<LinkSubjectStudy>(0);
	ListView<LinkSubjectStudy> listView;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedbackPanel
	 * @param studyCrudContainerVO
	 * @param iModel
	 */
	public ChildStudySubjectPanel(String id, final IModel<SubjectVO> iModel, WebMarkupContainer arkContextMarkup, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, iModel);
		
		this.subjectContainerForm = containerForm;
		this.arkContextMarkup = arkContextMarkup;
		this.arkCrudContainerVO = arkCrudContainerVO;
		
		LinkSubjectStudy lss =new LinkSubjectStudy();
		lss = iModel.getObject().getLinkSubjectStudy();
		List<Study> studies = iModel.getObject().getSelectedChildStudies();
		
		for (Iterator<Study> iterator = studies.iterator(); iterator.hasNext();) {
			Study study = (Study) iterator.next();
			try {
				if(!lss.getSubjectStatus().getName().equalsIgnoreCase("Archive")) {
					childSubjectIds.add(iArkCommonService.getSubject(lss.getPerson().getId(), study));
				}
			}
			catch (EntityNotFoundException e) {
				e.printStackTrace();
			}	
		}
		
		Label label = new Label("linkChildSubjectLabel", "Link to Child Study Subject:"){ 
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			public boolean isVisible() {
				return !childSubjectIds.isEmpty();
			};
		};
		listView = new ListView<LinkSubjectStudy>("list", childSubjectIds){
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<LinkSubjectStudy> item) {
				ArkBusyAjaxLink<String> link = new ArkBusyAjaxLink<String>("link") {

					private static final long	serialVersionUID	= 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						iModel.getObject().setLinkSubjectStudy(item.getModelObject());
						setSubjectIntoContext(target, iModel.getObject());
					}									
				};
				
				Label label = new Label("label", item.getModelObject().getStudy().getName());
				link.add(label);
				item.add(link);
			}
		};
		
		add(label);
		add(listView);
	}
	
	private void setSubjectIntoContext(AjaxRequestTarget target, final SubjectVO subject) {
		Study study = subject.getLinkSubjectStudy().getStudy();
		SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID, study.getId());

		// We specify the type of person here as Subject
		SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID, subject.getLinkSubjectStudy().getPerson().getId());
		SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_TYPE, au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT);

		SubjectVO subjectFromBackend = new SubjectVO();
		
		LinkSubjectStudy subjectRefreshed = iArkCommonService.getSubjectRefreshed(subject.getLinkSubjectStudy());
		
		subjectFromBackend.setLinkSubjectStudy(subjectRefreshed);

		// Available/assigned child studies
		List<Study> availableChildStudies = new ArrayList<Study>(0);
		List<Study> selectedChildStudies = new ArrayList<Study>(0);

		if (study.getParentStudy() != null) {
			availableChildStudies = iStudyService.getChildStudyListOfParent(study);
			Person person = null;
			try {
				person = iStudyService.getPerson(subject.getLinkSubjectStudy().getPerson().getId());
			}
			catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ArkSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			selectedChildStudies = iArkCommonService.getAssignedChildStudyListForPerson(study, person);
		}

		ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);

		subjectContainerForm.setModelObject(subjectFromBackend);
		subjectContainerForm.getModelObject().setAvailableChildStudies(availableChildStudies);
		subjectContainerForm.getModelObject().setSelectedChildStudies(selectedChildStudies);

		// Set SubjectUID into context
		SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.SUBJECTUID, subjectFromBackend.getLinkSubjectStudy().getSubjectUID());
		ContextHelper contextHelper = new ContextHelper();
		contextHelper.setStudyContextLabel(target, study.getName(), arkContextMarkup);
		contextHelper.setSubjectContextLabel(target, subject.getLinkSubjectStudy().getSubjectUID(), arkContextMarkup);
		contextHelper.setSubjectNameContextLabel(target, subject.getLinkSubjectStudy().getPerson().getFullName(), arkContextMarkup);

		// Set Study Logo
		StudyHelper studyHelper = new StudyHelper();
		WebMarkupContainer wmc = (WebMarkupContainer) getParent();
		au.org.theark.study.web.component.subject.form.DetailForm detailForm = (au.org.theark.study.web.component.subject.form.DetailForm) wmc.getParent();
		au.org.theark.study.web.component.subject.DetailPanel detailPanel = (au.org.theark.study.web.component.subject.DetailPanel) detailForm.getParent();
		
		studyHelper.setStudyLogo(study, target, detailPanel.studyNameMarkup, detailPanel.studyLogoMarkup, iArkCommonService);
	}
}
