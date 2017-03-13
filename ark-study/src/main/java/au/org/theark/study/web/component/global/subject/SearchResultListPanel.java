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
package au.org.theark.study.web.component.global.subject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import au.org.theark.core.util.EventPayload;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.LinkSubjectPedigree;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.OtherID;
import au.org.theark.core.model.study.entity.PersonLastnameHistory;
import au.org.theark.core.model.study.entity.Relationship;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.StudyHelper;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.core.web.component.link.AjaxConfirmLink;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.study.model.vo.RelationshipVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.util.PedigreeUploadValidator;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.global.subject.form.ContainerForm;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings({ "unchecked", "serial" })
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

	private TabbedPanel mainTabs;
	
	public SearchResultListPanel(String id, WebMarkupContainer arkContextMarkup, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO, WebMarkupContainer studyNameMarkup,
			WebMarkupContainer studyLogoMarkup, TabbedPanel mainTabs) {

		super(id);
		this.subjectContainerForm = containerForm;
		this.arkContextMarkup = arkContextMarkup;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		
		this.mainTabs = mainTabs;
	}

	public DataView<SubjectVO> buildDataView(ArkDataProvider<SubjectVO, IArkCommonService> subjectProvider) {

		DataView<SubjectVO> studyCompDataView = new DataView<SubjectVO>("subjectList", subjectProvider) {

			@Override
			protected void populateItem(final Item<SubjectVO> item) {
				LinkSubjectStudy subject = item.getModelObject().getLinkSubjectStudy();
				item.add(buildLink(item.getModelObject()));
				item.add(new Label("study", item.getModelObject().getLinkSubjectStudy().getStudy().getName()));
				item.add(new Label(Constants.SUBJECT_FULL_NAME, item.getModelObject().getSubjectFullName()));
/*
				if (subject != null && subject.getPerson() != null && subject.getPerson().getPreferredName() != null) {
					item.add(new Label("linkSubjectStudy.person.preferredName", subject.getPerson().getPreferredName()));
				}
				else {
					item.add(new Label("linkSubjectStudy.person.preferredName", ""));
				}
	*/			
				List<PersonLastnameHistory> lastnameHistory = (List<PersonLastnameHistory>) iArkCommonService.getPersonLastNameHistory(subject.getPerson());
				String lastNameString = "";
				if(!lastnameHistory.isEmpty()) {
					lastNameString = lastnameHistory.get(0).getLastName();
					for(int i = 1; i < lastnameHistory.size(); i++) {
						lastNameString += ", " + lastnameHistory.get(i).getLastName();
					}
				}
								
				if (subject != null && subject.getPerson() != null && subject.getPerson().getPersonLastnameHistory() != null && !lastNameString.isEmpty()) {
					item.add(new Label("linkSubjectStudy.person.previouslastnamehistory.lastname" ,lastNameString));
				} else {
					item.add(new Label("linkSubjectStudy.person.previouslastnamehistory.lastname", ""));
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
				
				List<OtherID> otherIDs = iArkCommonService.getOtherIDs(subject.getPerson());
				String otherIDstring = "";
				for(OtherID o : otherIDs) {
					otherIDstring += o.getOtherID_Source() + ": " + o.getOtherID() + "\n";
				}
				if (!otherIDs.isEmpty()) {
					item.add(new MultiLineLabel("linkSubjectStudy.person.otherIDs.otherID", otherIDstring));
				} else {
					item.add(new Label("linkSubjectStudy.person.otherIDs.otherID", ""));
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

	public DataView<SubjectVO> buildDataView(ArkDataProvider<SubjectVO, IArkCommonService> subjectProvider, final AbstractDetailModalWindow modalWindow, final List<RelationshipVo> relatives,
			final FeedbackPanel feedbackPanel) {

		DataView<SubjectVO> studyCompDataView = new DataView<SubjectVO>("subjectList", subjectProvider) {

			@Override
			protected void populateItem(final Item<SubjectVO> item) {
				LinkSubjectStudy subject = item.getModelObject().getLinkSubjectStudy();
				item.add(buildLink(item, modalWindow, relatives, feedbackPanel));
				item.add(new Label(Constants.SUBJECT_FULL_NAME, item.getModelObject().getSubjectFullName()));
/*
				if (subject != null && subject.getPerson() != null && subject.getPerson().getPreferredName() != null) {
					item.add(new Label("linkSubjectStudy.person.preferredName", subject.getPerson().getPreferredName()));
				}
				else {
					item.add(new Label("linkSubjectStudy.person.preferredName", ""));
				}
*/
				List<PersonLastnameHistory> lastnameHistory = (List<PersonLastnameHistory>) iArkCommonService.getPersonLastNameHistory(subject.getPerson());
				String lastNameString = "";
				if(!lastnameHistory.isEmpty()) {
					lastNameString = lastnameHistory.get(0).getLastName();
					for(int i = 1; i < lastnameHistory.size(); i++) {
						lastNameString += ", " + lastnameHistory.get(i).getLastName();
					}
				}
				
				if (subject != null && subject.getPerson() != null && subject.getPerson().getPersonLastnameHistory() != null && !lastNameString.isEmpty()) {
					item.add(new Label("linkSubjectStudy.person.previouslastnamehistory.lastname" ,lastNameString));
				} else {
					item.add(new Label("linkSubjectStudy.person.previouslastnamehistory.lastname", ""));
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
				
				List<OtherID> otherIDs = iArkCommonService.getOtherIDs(subject.getPerson());
				String otherIDstring = "";
				for(OtherID o : otherIDs) {
					otherIDstring += o.getOtherID_Source() + ": " + o.getOtherID() + "\n";
				}
				if (!otherIDs.isEmpty()) {
					item.add(new MultiLineLabel("linkSubjectStudy.person.otherIDs.otherID", otherIDstring));
				} else {
					item.add(new Label("linkSubjectStudy.person.otherIDs.otherID", ""));
				}
			}
		};
		return studyCompDataView;
	}

	public PageableListView<SubjectVO> buildListView(IModel iModel) {

		PageableListView<SubjectVO> listView = new PageableListView<SubjectVO>(Constants.SUBJECT_LIST, iModel, iArkCommonService.getRowsPerPage()) {

			@Override
			protected void populateItem(final ListItem<SubjectVO> item) {
				LinkSubjectStudy subject = item.getModelObject().getLinkSubjectStudy();
				item.add(buildLink(item.getModelObject()));
				item.add(new Label(Constants.SUBJECT_FULL_NAME, item.getModelObject().getSubjectFullName()));

		/*		if (subject != null && subject.getPerson() != null && subject.getPerson().getPreferredName() != null) {
					item.add(new Label("linkSubjectStudy.person.preferredName", subject.getPerson().getPreferredName()));
				}
				else {
					item.add(new Label("linkSubjectStudy.person.preferredName", ""));
				}
		 */
				List<PersonLastnameHistory> lastnameHistory = (List<PersonLastnameHistory>) iArkCommonService.getPersonLastNameHistory(subject.getPerson());
				String lastNameString = "";
				if(!lastnameHistory.isEmpty()) {
					lastNameString = lastnameHistory.get(0).getLastName();
					for(int i = 1; i < lastnameHistory.size(); i++) {
						lastNameString += ", " + lastnameHistory.get(i).getLastName();
					}
				}
								
				if (subject != null && subject.getPerson() != null && subject.getPerson().getPersonLastnameHistory() != null && !lastNameString.isEmpty()) {
					item.add(new Label("linkSubjectStudy.person.previouslastnamehistory.lastname" ,lastNameString));
				} else {
					item.add(new Label("linkSubjectStudy.person.previouslastnamehistory.lastname", ""));
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
				
				List<OtherID> otherIDs = iArkCommonService.getOtherIDs(subject.getPerson());
				String otherIDstring = "";
				for(OtherID o : otherIDs) {
					otherIDstring += o.getOtherID_Source() + ": " + o.getOtherID() + "\n";
				}
				if (!otherIDs.isEmpty()) {
					item.add(new MultiLineLabel("linkSubjectStudy.person.otherIDs.otherID", otherIDstring));
				} else {
					item.add(new Label("linkSubjectStudy.person.otherIDs.otherID", ""));
				}
			}
		};
		return listView;
	}

	private AjaxLink buildLink(final SubjectVO subject) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.SUBJECT_UID) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				// subject.getLinkSubjectStudy().setStudy(iArkCommonService.getStudy(sessionStudyId));

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

//				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);				
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
				
				//Move to subject Tab
				ListIterator<ITab> lIter = mainTabs.getTabs().listIterator();
				while(lIter.hasNext()) {
					int index = lIter.nextIndex();
					ITab currentTab = lIter.next();
					System.out.println("TAB: " + currentTab.getTitle().toString());
					if(currentTab.getTitle().getObject() == au.org.theark.core.Constants.ARK_MODULE_SUBJECT) {
						mainTabs.setSelectedTab(index);
						break;
					}
				}
				target.add(mainTabs);
				this.send(target.getPage(), Broadcast.DEPTH, new EventPayload(au.org.theark.core.Constants.EVENT_RELOAD_LOGO_IMAGES, target));
			}
		};
		Label nameLinkLabel = new Label(Constants.SUBJECT_KEY_LBL, subject.getLinkSubjectStudy().getSubjectUID());
		link.add(nameLinkLabel);
		return link;
	}

	private AjaxLink buildLink(Item<SubjectVO> item, final AbstractDetailModalWindow modalWindow, final List<RelationshipVo> relatives, final FeedbackPanel feedbackPanel) {
		
		AjaxLink link=null;
		final SubjectVO subject = item.getModelObject();
		if("Male".equalsIgnoreCase(subject.getLinkSubjectStudy().getPerson().getGenderType().getName())){
			subject.setParentType("Father");
		}else{
			subject.setParentType("Mother");
		}
		
		if(isConsiderParentAge(subject)){
			link = new AjaxConfirmLink(Constants.SUBJECT_UID, new StringResourceModel("pedigree.parent.dob.warning", this, item.getModel()),item.getModel()) {
				@Override
				public void onClick(AjaxRequestTarget target) {
					processParentSelection(subject, modalWindow, relatives, feedbackPanel, target);
					this.send(getWebPage(), Broadcast.DEPTH, new EventPayload(au.org.theark.core.Constants.EVENT_RELOAD_LOGO_IMAGES, target));
				}
			};
		}
		else{
			link = new ArkBusyAjaxLink(Constants.SUBJECT_UID) {
				@Override
				public void onClick(AjaxRequestTarget target) {
					processParentSelection(subject, modalWindow, relatives, feedbackPanel, target);
					this.send(getWebPage(), Broadcast.DEPTH, new EventPayload(au.org.theark.core.Constants.EVENT_RELOAD_LOGO_IMAGES, target));
				}
			};
		}
		Label nameLinkLabel = new Label(Constants.SUBJECT_KEY_LBL, subject.getLinkSubjectStudy().getSubjectUID());
		link.add(nameLinkLabel);
		return link;
	}
	
	/**
	 * Check is the parent age is to be consider for the validation.
	 * @param parentSubject
	 * @return
	 */
	private boolean isConsiderParentAge(SubjectVO parentSubject){
		boolean check=false;
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		String subjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
		SubjectVO criteriaSubjectVo = new SubjectVO();
		criteriaSubjectVo.getLinkSubjectStudy().setStudy(study);
		criteriaSubjectVo.getLinkSubjectStudy().setSubjectUID(subjectUID);
		Collection<SubjectVO> subjects = iArkCommonService.getSubject(criteriaSubjectVo);
		SubjectVO subjectVo = subjects.iterator().next();
		
		Date parentDOB = parentSubject.getLinkSubjectStudy().getPerson().getDateOfBirth();
		Date subjectDOB = subjectVo.getLinkSubjectStudy().getPerson().getDateOfBirth();
		
		if( parentDOB != null &&
				 subjectDOB!=null &&
				 	parentDOB.compareTo(subjectDOB) >= 0){
				check =true;	
		}
		return check;
	}
	
	/**After select a parent validate the pedigree for circular relationships and create new parent relationship in database.
	 * @param subject
	 * @param modalWindow
	 * @param relatives
	 * @param feedbackPanel
	 * @param target
	 */
	private void processParentSelection(final SubjectVO subject, final AbstractDetailModalWindow modalWindow, final List<RelationshipVo> relatives, final FeedbackPanel feedbackPanel,
			AjaxRequestTarget target) {
		LinkSubjectPedigree pedigreeRelationship = new LinkSubjectPedigree();

		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

		Study study = iArkCommonService.getStudy(sessionStudyId);

		String subjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);

		String parentUID = subject.getLinkSubjectStudy().getSubjectUID();

		// Circular validation
		StringBuilder pedigree = new StringBuilder();
		ArrayList<String> dummyParents = new ArrayList<String>();
		boolean firstLine = true;

		List<RelationshipVo> existingRelatives = new ArrayList<RelationshipVo>();
		existingRelatives.addAll(relatives);

		RelationshipVo proband = new RelationshipVo();
		proband.setIndividualId(subjectUID);
		for (RelationshipVo relative : existingRelatives) {
			if ("Father".equalsIgnoreCase(relative.getRelationship())) {
				proband.setFatherId(relative.getIndividualId());
			}

			if ("Mother".equalsIgnoreCase(relative.getRelationship())) {
				proband.setMotherId(relative.getIndividualId());
			}
		}

		if (subject.getLinkSubjectStudy().getPerson().getGenderType().getName().startsWith("M")) {
			proband.setFatherId(parentUID);
		}
		else if (subject.getLinkSubjectStudy().getPerson().getGenderType().getName().startsWith("F")) {
			proband.setMotherId(parentUID);
		}
		existingRelatives.add(proband);

		List<RelationshipVo> newRelatives = iStudyService.generateSubjectPedigreeRelativeList(parentUID, sessionStudyId);

		RelationshipVo parent = new RelationshipVo();
		parent.setIndividualId(parentUID);
		for (RelationshipVo relative : newRelatives) {
			if ("Father".equalsIgnoreCase(relative.getRelationship())) {
				parent.setFatherId(relative.getIndividualId());
			}

			if ("Mother".equalsIgnoreCase(relative.getRelationship())) {
				parent.setMotherId(relative.getIndividualId());
			}
		}

		newRelatives.add(parent);

		for (RelationshipVo relative : newRelatives) {
			if (!existingRelatives.contains(relative)) {
				existingRelatives.add(relative);
			}
			else {
				for (RelationshipVo existingRelative : existingRelatives) {
					if (relative.getIndividualId().equals(existingRelative.getIndividualId())) {
						if (existingRelative.getFatherId() == null) {
							existingRelative.setFatherId(relative.getFatherId());
						}
						if (existingRelative.getMotherId() == null) {
							existingRelative.setMotherId(relative.getMotherId());
						}
					}
				}
			}
		}

		for (RelationshipVo relative : existingRelatives) {
			String dummyParent = "D-";
			String father = relative.getFatherId();
			String mother = relative.getMotherId();
			String individual = relative.getIndividualId();

			if (father != null) {
				dummyParent = dummyParent + father;
			}

			if (mother != null) {
				dummyParent = dummyParent + mother;
			}

			if (!"D-".equals(dummyParent) && !dummyParents.contains(dummyParent)) {
				dummyParents.add(dummyParent);
				if (father != null) {
					if (firstLine) {
						pedigree.append(father + " " + dummyParent);
						firstLine = false;
					}
					else {
						pedigree.append("\n" + father + " " + dummyParent);
					}
				}
				if (mother != null) {
					if (firstLine) {
						pedigree.append(mother + " " + dummyParent);
						firstLine = false;
					}
					else {
						pedigree.append("\n" + mother + " " + dummyParent);
					}
				}
				pedigree.append("\n" + dummyParent + " " + individual);
			}
			else if (!"D-".equals(dummyParent)) {
				if (firstLine) {
					pedigree.append(dummyParent + " " + individual);
					firstLine = false;
				}
				else {
					pedigree.append("\n" + dummyParent + " " + individual);
				}
			}
		}

		//TODO comment this block to disable circular validations for inbred relatives
		Set<String> circularUIDs = PedigreeUploadValidator.getCircularUIDs(pedigree);
		if (circularUIDs.size() > 0) {
			this.error("Performing this action will create a circular relationship in the pedigree.");
			StringBuffer sb = new StringBuffer("The proposed action will cause a pedigree cycle involving subjects with UID:");
			boolean first = true;
			for (String uid : circularUIDs) {
				if (first) {
					sb.append(uid);
					first = false;
				}
				else {
					sb.append(", " + uid);
				}
			}
			sb.append(".");
			this.error(sb.toString());
			target.add(feedbackPanel);
			return;
		}

		// Assign new parent relationships

		SubjectVO criteriaSubjectVo = new SubjectVO();
		criteriaSubjectVo.getLinkSubjectStudy().setStudy(study);
		criteriaSubjectVo.getLinkSubjectStudy().setSubjectUID(subjectUID);
		Collection<SubjectVO> subjects = iArkCommonService.getSubject(criteriaSubjectVo);
		SubjectVO subjectVo = subjects.iterator().next();
		pedigreeRelationship.setSubject(subjectVo.getLinkSubjectStudy());

		criteriaSubjectVo.getLinkSubjectStudy().setSubjectUID(parentUID);
		subjects = iArkCommonService.getSubject(criteriaSubjectVo);
		subjectVo = subjects.iterator().next();
		pedigreeRelationship.setRelative(subjectVo.getLinkSubjectStudy());

		String gender = subject.getLinkSubjectStudy().getPerson().getGenderType().getName();

		List<Relationship> relationships = iArkCommonService.getFamilyRelationships();
		for (Relationship relationship : relationships) {
			if ("Male".equalsIgnoreCase(gender) && "Father".equalsIgnoreCase(relationship.getName())) {
				pedigreeRelationship.setRelationship(relationship);
				break;
			}

			if ("Female".equalsIgnoreCase(gender) && "Mother".equalsIgnoreCase(relationship.getName())) {
				pedigreeRelationship.setRelationship(relationship);
				break;
			}
		}

		iStudyService.create(pedigreeRelationship);
		modalWindow.close(target);
	}

}
