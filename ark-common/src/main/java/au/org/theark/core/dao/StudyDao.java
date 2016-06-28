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
package au.org.theark.core.dao;


import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.spi.QueryTranslator;
import org.hibernate.hql.spi.QueryTranslatorFactory;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.model.config.entity.ConfigField;
import au.org.theark.core.model.config.entity.UserConfig;
import au.org.theark.core.model.geno.entity.Command;
import au.org.theark.core.model.geno.entity.LinkSubjectStudyPipeline;
import au.org.theark.core.model.geno.entity.Pipeline;
import au.org.theark.core.model.geno.entity.Process;
import au.org.theark.core.model.geno.entity.ProcessInput;
import au.org.theark.core.model.geno.entity.ProcessOutput;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioCollectionCustomFieldData;
import au.org.theark.core.model.lims.entity.BioCollectionUidPadChar;
import au.org.theark.core.model.lims.entity.BioCollectionUidTemplate;
import au.org.theark.core.model.lims.entity.BioCollectionUidToken;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.BiospecimenCustomFieldData;
import au.org.theark.core.model.lims.entity.BiospecimenUidPadChar;
import au.org.theark.core.model.lims.entity.BiospecimenUidTemplate;
import au.org.theark.core.model.lims.entity.BiospecimenUidToken;
import au.org.theark.core.model.pheno.entity.PhenoDataSetData;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.core.model.pheno.entity.PhenoDataSetGroup;
import au.org.theark.core.model.report.entity.BiocollectionField;
import au.org.theark.core.model.report.entity.BiocollectionFieldSearch;
import au.org.theark.core.model.report.entity.BiospecimenField;
import au.org.theark.core.model.report.entity.BiospecimenFieldSearch;
import au.org.theark.core.model.report.entity.ConsentStatusField;
import au.org.theark.core.model.report.entity.ConsentStatusFieldSearch;
import au.org.theark.core.model.report.entity.CustomFieldDisplaySearch;
import au.org.theark.core.model.report.entity.DemographicField;
import au.org.theark.core.model.report.entity.DemographicFieldSearch;
import au.org.theark.core.model.report.entity.Entity;
import au.org.theark.core.model.report.entity.FieldCategory;
import au.org.theark.core.model.report.entity.Operator;
import au.org.theark.core.model.report.entity.PhenoDataSetFieldDisplaySearch;
import au.org.theark.core.model.report.entity.QueryFilter;
import au.org.theark.core.model.report.entity.Search;
import au.org.theark.core.model.report.entity.SearchPayload;
import au.org.theark.core.model.report.entity.SearchResult;
import au.org.theark.core.model.report.entity.SearchSubject;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.AddressStatus;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkFunctionType;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkModuleFunction;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.ArkUserRole;
import au.org.theark.core.model.study.entity.AuditHistory;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.ConsentAnswer;
import au.org.theark.core.model.study.entity.ConsentOption;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldCategoryUpload;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldType;
import au.org.theark.core.model.study.entity.CustomFieldUpload;
import au.org.theark.core.model.study.entity.DelimiterType;
import au.org.theark.core.model.study.entity.EmailStatus;
import au.org.theark.core.model.study.entity.FamilyCustomFieldData;
import au.org.theark.core.model.study.entity.FieldType;
import au.org.theark.core.model.study.entity.FileFormat;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.LinkSubjectTwin;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.OtherID;
import au.org.theark.core.model.study.entity.Payload;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonContactMethod;
import au.org.theark.core.model.study.entity.PersonLastnameHistory;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneStatus;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.Relationship;
import au.org.theark.core.model.study.entity.State;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyCompStatus;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.SubjectCustomFieldData;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.SubjectUidPadChar;
import au.org.theark.core.model.study.entity.SubjectUidToken;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.Upload;
import au.org.theark.core.model.study.entity.UploadStatus;
import au.org.theark.core.model.study.entity.UploadType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.util.CsvListReader;
import au.org.theark.core.vo.DataExtractionVO;
import au.org.theark.core.vo.ExtractionVO;
import au.org.theark.core.vo.LinkedExtractionVO;
import au.org.theark.core.vo.QueryFilterVO;
import au.org.theark.core.vo.SearchVO;
import au.org.theark.core.vo.SubjectVO;

/**
 * @author nivedann
 * @param <T>
 * 
 */
@Repository("commonStudyDao")
public class StudyDao<T> extends HibernateSessionDao implements IStudyDao {

	private static Logger	log	= LoggerFactory.getLogger(StudyDao.class);
	protected static final String			HEXES					= "0123456789ABCDEF";
	private IDataExtractionDao iDataExtractionDao;

	/**
	 * @return the iDataExtractionDao
	 */
	public IDataExtractionDao getiDataExtractionDao() {
		return iDataExtractionDao;
	}

	/**
	 * @param iDataExtractionDao the iDataExtractionDao to set
	 */
	@Autowired
	public void setiDataExtractionDao(IDataExtractionDao iDataExtractionDao) {
		this.iDataExtractionDao = iDataExtractionDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.dao.IStudyDao#getStudy(au.org.theark.core.model.study .entity.Study)
	 */
	@SuppressWarnings("unchecked")
	public List<Study> getStudy(Study study) {

		Criteria studyCriteria = getSession().createCriteria(Study.class);

		if (study.getId() != null) {
			studyCriteria.add(Restrictions.eq(Constants.STUDY_KEY, study.getId()));
		}

		if (study.getName() != null) {
			studyCriteria.add(Restrictions.ilike(Constants.STUDY_NAME, study.getName(), MatchMode.ANYWHERE));
		}

		if (study.getDateOfApplication() != null) {
			studyCriteria.add(Restrictions.eq(Constants.DATE_OF_APPLICATION, study.getDateOfApplication()));
		}

		if (study.getEstimatedYearOfCompletion() != null) {
			studyCriteria.add(Restrictions.eq(Constants.EST_YEAR_OF_COMPLETION, study.getEstimatedYearOfCompletion()));
		}

		if (study.getChiefInvestigator() != null) {
			studyCriteria.add(Restrictions.ilike(Constants.CHIEF_INVESTIGATOR, study.getChiefInvestigator(), MatchMode.ANYWHERE));
		}

		if (study.getContactPerson() != null) {
			studyCriteria.add(Restrictions.ilike(Constants.CONTACT_PERSON, study.getContactPerson(), MatchMode.ANYWHERE));
		}

		if (study.getStudyStatus() != null) {
			studyCriteria.add(Restrictions.eq(Constants.STUDY_STATUS, study.getStudyStatus()));
			try {
				StudyStatus status = getStudyStatus("Archive");
				studyCriteria.add(Restrictions.ne(Constants.STUDY_STATUS, status));
			}
			catch (StatusNotAvailableException notAvailable) {
				log.error("Cannot look up and filter on archive status. Reference data could be missing");
			}
		}
		else {
			try {
				StudyStatus status = getStudyStatus("Archive");
				studyCriteria.add(Restrictions.ne(Constants.STUDY_STATUS, status));
			}
			catch (StatusNotAvailableException notAvailable) {
				log.error("Cannot look up and filter on archive status. Reference data could be missing");
			}

		}

		studyCriteria.addOrder(Order.asc(Constants.STUDY_NAME));
		List<Study> studyList = studyCriteria.list();

		return studyList;
	}

	@SuppressWarnings("unchecked")
	public SubjectStatus getSubjectStatus(String statusName) {

		SubjectStatus statusToReturn = null;

		SubjectStatus subjectStatus = new SubjectStatus();
		subjectStatus.setName(statusName);
		Example example = Example.create(subjectStatus);

		Criteria criteria = getSession().createCriteria(SubjectStatus.class).add(example);

		if (criteria != null) {
			List<SubjectStatus> results = criteria.list();
			if (results != null && !results.isEmpty()) {
				statusToReturn = (SubjectStatus) results.get(0);
			}
		}

		return statusToReturn;
	}

	/**
	 * Given a status name will return the StudyStatus object.
	 */
	@SuppressWarnings("unchecked")
	public StudyStatus getStudyStatus(String statusName) throws StatusNotAvailableException {
		StudyStatus studyStatus = new StudyStatus();
		studyStatus.setName("Archive");
		Example studyStatusExample = Example.create(studyStatus);

		Criteria studyStatusCriteria = getSession().createCriteria(StudyStatus.class).add(studyStatusExample);
		if (studyStatusCriteria != null) {
			List<StudyStatus> results = studyStatusCriteria.list();
			if (results != null && results.size() > 0) {
				return (StudyStatus) results.get(0);
			}
		}

		log.error("Study Status Table maybe out of synch. Please check if it has an entry for Archive status.  Cannot locate a study status with " + statusName + " in the database");
		throw new StatusNotAvailableException();

	}

	@SuppressWarnings("unchecked")
	public List<StudyStatus> getListOfStudyStatus() {
		Example studyStatus = Example.create(new StudyStatus());
		Criteria criteria = getSession().createCriteria(StudyStatus.class).add(studyStatus);
		return criteria.list();

	}

	public Study getStudy(Long id) {
		Study study = (Study) getSession().get(Study.class, id);
		return study;
	}

	@SuppressWarnings("unchecked")
	public Collection<TitleType> getTitleType() {
		Example example = Example.create(new TitleType());
		Criteria criteria = getSession().createCriteria(TitleType.class).add(example);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public Collection<VitalStatus> getVitalStatus() {
		Example example = Example.create(new VitalStatus());
		Criteria criteria = getSession().createCriteria(VitalStatus.class).add(example);
		return criteria.list();
	}

	// TODO: cache?
	@SuppressWarnings("unchecked")
	public Collection<GenderType> getGenderTypes() {
		Example example = Example.create(new GenderType());
		Criteria criteria = getSession().createCriteria(GenderType.class).add(example);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<PhoneType> getListOfPhoneType() {
		Example phoneTypeExample = Example.create(new PhoneType());
		Criteria criteria = getSession().createCriteria(PhoneType.class).add(phoneTypeExample);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<SubjectStatus> getSubjectStatus() {

		Example example = Example.create(new SubjectStatus());
		Criteria criteria = getSession().createCriteria(SubjectStatus.class).add(example);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public Collection<MaritalStatus> getMaritalStatus() {
		Example example = Example.create(new MaritalStatus());
		Criteria criteria = getSession().createCriteria(MaritalStatus.class).add(example);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<EmailStatus> getAllEmailStatuses() {
		Example example = Example.create(new EmailStatus());
		Criteria criteria = getSession().createCriteria(EmailStatus.class).add(example);
		return criteria.list();
	}

	/**
	 * Look up the Link Subject Study for subjects linked to a study
	 * 
	 * @param subjectVO
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<SubjectVO> getSubject(SubjectVO subjectVO) {
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.createAlias("person", "p");
		criteria.add(Restrictions.eq("study.id", subjectVO.getLinkSubjectStudy().getStudy().getId()));

		if (subjectVO.getLinkSubjectStudy().getPerson() != null) {

			if (subjectVO.getLinkSubjectStudy().getPerson().getId() != null) {
				criteria.add(Restrictions.eq("p.id", subjectVO.getLinkSubjectStudy().getPerson().getId()));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getFirstName() != null) {
				criteria.add(Restrictions.ilike("p.firstName", subjectVO.getLinkSubjectStudy().getPerson().getFirstName(), MatchMode.ANYWHERE));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getMiddleName() != null) {
				criteria.add(Restrictions.ilike("p.middleName", subjectVO.getLinkSubjectStudy().getPerson().getMiddleName(), MatchMode.ANYWHERE));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getLastName() != null) {
				criteria.add(Restrictions.ilike("p.lastName", subjectVO.getLinkSubjectStudy().getPerson().getLastName(), MatchMode.ANYWHERE));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getDateOfBirth() != null) {
				criteria.add(Restrictions.eq("p.dateOfBirth", subjectVO.getLinkSubjectStudy().getPerson().getDateOfBirth()));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getGenderType() != null) {
				criteria.add(Restrictions.eq("p.genderType.id", subjectVO.getLinkSubjectStudy().getPerson().getGenderType().getId()));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getVitalStatus() != null) {
				criteria.add(Restrictions.eq("p.vitalStatus.id", subjectVO.getLinkSubjectStudy().getPerson().getVitalStatus().getId()));
			}

		}

		if (subjectVO.getLinkSubjectStudy().getSubjectUID() != null && subjectVO.getLinkSubjectStudy().getSubjectUID().length() > 0) {
			criteria.add(Restrictions.eq("subjectUID", subjectVO.getLinkSubjectStudy().getSubjectUID()));
		}

		if (subjectVO.getLinkSubjectStudy().getSubjectStatus() != null) {
			criteria.add(Restrictions.eq("subjectStatus", subjectVO.getLinkSubjectStudy().getSubjectStatus()));
			SubjectStatus subjectStatus = getSubjectStatus("Archive");
			if (subjectStatus != null) {
				criteria.add(Restrictions.ne("subjectStatus", subjectStatus));
			}
		}
		else {
			SubjectStatus subjectStatus = getSubjectStatus("Archive");
			if (subjectStatus != null) {
				criteria.add(Restrictions.ne("subjectStatus", subjectStatus));
			}
		}

		criteria.addOrder(Order.asc("subjectUID"));
		List<LinkSubjectStudy> list = criteria.list();

		Collection<SubjectVO> subjectVOList = new ArrayList<SubjectVO>();

		for (Iterator iterator = list.iterator(); iterator.hasNext();) {

			LinkSubjectStudy linkSubjectStudy = (LinkSubjectStudy) iterator.next();
			// Place the LinkSubjectStudy instance into a SubjectVO and add the
			// SubjectVO into a List
			SubjectVO subject = new SubjectVO();
			subject.setLinkSubjectStudy(linkSubjectStudy);
			Person person = subject.getLinkSubjectStudy().getPerson();
			subject.setSubjectPreviousLastname(getPreviousLastname(person));
			subjectVOList.add(subject);
		}
		return subjectVOList;

	}

	@SuppressWarnings("unchecked")
	public List<Phone> getPhonesForPerson(Person person) {
		Criteria personCriteria = getSession().createCriteria(Phone.class);
		personCriteria.add(Restrictions.eq("person", person));// Filter the
		// phones linked
		// to this
		// personID/Key
		return personCriteria.list();
	}

	@SuppressWarnings("unchecked")
	public LinkSubjectStudy getLinkSubjectStudy(Long id) throws EntityNotFoundException {

		Criteria linkSubjectStudyCriteria = getSession().createCriteria(LinkSubjectStudy.class);
		linkSubjectStudyCriteria.add(Restrictions.eq("id", id));
		List<LinkSubjectStudy> listOfSubjects = linkSubjectStudyCriteria.list();
		if (listOfSubjects != null && listOfSubjects.size() > 0) {
			return listOfSubjects.get(0);
		}
		else {
			throw new EntityNotFoundException("The entity with id" + id.toString() + " cannot be found.");
		}
	}

	@SuppressWarnings("unchecked")
	public LinkSubjectStudy getSubjectByUID(String subjectUID, Study study) throws EntityNotFoundException {

		Criteria linkSubjectStudyCriteria = getSession().createCriteria(LinkSubjectStudy.class);
		linkSubjectStudyCriteria.add(Restrictions.eq("subjectUID", subjectUID));
		linkSubjectStudyCriteria.add(Restrictions.eq("study", study));
		List<LinkSubjectStudy> listOfSubjects = linkSubjectStudyCriteria.list();
		if (listOfSubjects != null && listOfSubjects.size() > 0) {
			return listOfSubjects.get(0);
		}
		else {
			throw new EntityNotFoundException("There is no subject with the given UID " + subjectUID.toString());
		}
	}

	public LinkSubjectStudy getSubjectRefreshed(LinkSubjectStudy subject){
		if(subject==null || subject.getId()==null){
			return subject;
		}
		else{
			if(subject.getId() != null){
				getSession().refresh(subject);
			}
			/* then if we really need refresh all the gritty underlying details 
			 */
			if(subject.getConsentStatus()!=null){
				getSession().refresh(subject.getConsentStatus());
			}
			return subject;
		}
	}

	/**
	 * returns a the subject (linksubjectystudy) IF there is one, else returns null
	 * 
	 * Note this is actively fetching person
	 * 
	 * @param subjectUID
	 * @param study
	 * @return LinkSubjectStudy
	 */
	public LinkSubjectStudy getSubjectByUIDAndStudy(String subjectUID, Study study) {
		//log.warn("about to create query right now");
		Criteria linkSubjectStudyCriteria = getSession().createCriteria(LinkSubjectStudy.class);
		linkSubjectStudyCriteria.add(Restrictions.eq("subjectUID", subjectUID));
		linkSubjectStudyCriteria.add(Restrictions.eq("study", study));
		return (LinkSubjectStudy) linkSubjectStudyCriteria.uniqueResult();
	}

	/**
	 * Returns a list of Countries
	 */
	@SuppressWarnings("unchecked")
	public List<Country> getCountries() {
		Criteria criteria = getSession().createCriteria(Country.class);
		criteria.addOrder(Order.asc("name"));
		return criteria.list();
	}

	public Country getCountry(Long id) {
		Criteria criteria = getSession().createCriteria(Country.class);
		criteria.add(Restrictions.eq("id", id));
		return (Country) criteria.list().get(0);
	}

	// TODO FIX HARDCODING
	public Country getCountry(String countryCode) {
		Criteria criteria = getSession().createCriteria(Country.class);
		criteria.add(Restrictions.eq("countryCode", countryCode));
		return (Country) criteria.list().get(0);
	}

	public List<State> getStates(Country country) {

		if (country == null) {
			country = getCountry(Constants.DEFAULT_COUNTRY_CODE);
		}
		Criteria criteria = getSession().createCriteria(State.class);
		criteria.add(Restrictions.eq("country", country));
		criteria.addOrder(Order.asc("name"));
		return criteria.list();
	}

	/**
	 * Gets a list of all Address Types
	 * 
	 * @return
	 */
	public List<AddressType> getAddressTypes() {
		List<AddressType> AddressTypeLstNew=new ArrayList<AddressType>();
		Criteria criteria = getSession().createCriteria(AddressType.class);
		criteria.addOrder(Order.asc("name"));

		List<AddressType> AddressTypeLst=criteria.list();
		for (AddressType addressType : AddressTypeLst) {
			String name=addressType.getName().toLowerCase();
			addressType.setName(WordUtils.capitalize(name));
			AddressTypeLstNew.add(addressType);
		}
		return AddressTypeLstNew;
	}

	/**
	 * Gets a list of all Phone Statuses
	 * 
	 * @return
	 */
	public List<PhoneStatus> getPhoneStatuses() {
		Criteria criteria = getSession().createCriteria(PhoneStatus.class);
		return criteria.list();
	}

	/**
	 * Gets a list of all Phone Types
	 * 
	 * @return
	 */
	public List<PhoneType> getPhoneTypes() {
		Criteria criteria = getSession().createCriteria(PhoneType.class);
		criteria.addOrder(Order.asc("name"));
		return criteria.list();
	}

	/**
	 * Gets a list of all Address Statuses
	 * 
	 * @return
	 */
	public List<AddressStatus> getAddressStatuses() {
		Criteria criteria = getSession().createCriteria(AddressStatus.class);
		return criteria.list();
	}

	public List<ConsentStatus> getConsentStatus() {
		Criteria criteria = getSession().createCriteria(ConsentStatus.class);
		return criteria.list();
	}

	public List<StudyCompStatus> getStudyComponentStatus() {
		Criteria criteria = getSession().createCriteria(StudyCompStatus.class);
		return criteria.list();
	}

	public List<StudyComp> getStudyComponentByStudy(Study study) {
		Criteria criteria = getSession().createCriteria(StudyComp.class);
		criteria.add(Restrictions.eq("study", study));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public boolean isSubjectConsentedToComponent(StudyComp studyComponent, Person person, Study study) {
		boolean isConsented = false;
		Criteria criteria = getSession().createCriteria(Consent.class);
		criteria.add(Restrictions.eq("studyComp", studyComponent));
		criteria.add(Restrictions.eq("study", study));
		criteria.createAlias("linkSubjectStudy", "lss");
		criteria.add(Restrictions.eq("lss.person", person));
		List list = criteria.list();
		if (list != null && list.size() > 0) {
			isConsented = true;
		}
		return isConsented;
	}

	/**
	 * Returns a list of Consent types hardcopy, electronic document etc.
	 * 
	 * @return
	 */
	public List<ConsentType> getConsentType() {
		Criteria criteria = getSession().createCriteria(ConsentType.class);
		return criteria.list();
	}

	public List<ConsentAnswer> getConsentAnswer() {
		Criteria criteria = getSession().createCriteria(ConsentAnswer.class);
		return criteria.list();
	}

	public List<YesNo> getYesNoList() {
		Criteria criteria = getSession().createCriteria(YesNo.class);
		return criteria.list();
	}

	public void createAuditHistory(AuditHistory auditHistory, String userId, Study study) {
		Date date = new Date(System.currentTimeMillis());

		if (userId == null) {// if not forcing a userID manually, get
			// currentuser
			Subject currentUser = SecurityUtils.getSubject();
			auditHistory.setArkUserId((String) currentUser.getPrincipal());
		}
		else {
			auditHistory.setArkUserId(userId);
		}
		if (study == null) {
			Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			if (sessionStudyId != null && auditHistory.getStudyStatus() == null) {
				auditHistory.setStudyStatus(getStudy(sessionStudyId).getStudyStatus());
			}
			else {

				if (auditHistory.getEntityType().equalsIgnoreCase(au.org.theark.core.Constants.ENTITY_TYPE_STUDY)) {
					Study studyFromDB = getStudy(auditHistory.getEntityId());
					if (studyFromDB != null) {
						auditHistory.setStudyStatus(studyFromDB.getStudyStatus());
					}
				}
			}
		}
		else {
			auditHistory.setStudyStatus(study.getStudyStatus());
		}
		auditHistory.setDateTime(date);
		getSession().save(auditHistory);
		// getSession().flush();
	}

	public void createAuditHistory(AuditHistory auditHistory) {
		createAuditHistory(auditHistory, null, null);
	}

	public List<PersonContactMethod> getPersonContactMethodList() {
		Criteria criteria = getSession().createCriteria(PersonContactMethod.class);
		return criteria.list();
	}

	public PersonLastnameHistory getPreviousSurnameHistory(PersonLastnameHistory personSurnameHistory) {
		PersonLastnameHistory personLastnameHistoryToReturn = null;

		Example example = Example.create(personSurnameHistory);

		Criteria criteria = getSession().createCriteria(PersonLastnameHistory.class).add(example);
		if (criteria != null) {// should it ever?
			List<PersonLastnameHistory> results = criteria.list();
			if (results != null && !results.isEmpty()) {
				personLastnameHistoryToReturn = (PersonLastnameHistory) results.get(0);
			}
		}
		return personLastnameHistoryToReturn;
	}

	public String getPreviousLastname(Person person) {
		Criteria criteria = getSession().createCriteria(PersonLastnameHistory.class);

		if (person.getId() != null) {
			criteria.add(Restrictions.eq(Constants.PERSON_SURNAME_HISTORY_PERSON, person));
		}
		criteria.addOrder(Order.asc("id"));
		PersonLastnameHistory personLastameHistory = new PersonLastnameHistory();

		List<PersonLastnameHistory> results = criteria.list();
		if (results.size() > 0) {

			// what this is saying is get the second-last last-name to display
			// as "previous lastname"
			personLastameHistory = (PersonLastnameHistory) results.get(results.size() - 1);
		}// else it doesnt have a previous...only a current

		return personLastameHistory.getLastName();
	}

	public List<PersonLastnameHistory> getLastnameHistory(Person person) {
		Criteria criteria = getSession().createCriteria(PersonLastnameHistory.class);

		if (person.getId() != null) {
			criteria.add(Restrictions.eq(Constants.PERSON_SURNAME_HISTORY_PERSON, person));
		}

		return criteria.list();
	}

	public LinkSubjectStudy getSubject(Long personId, Study study) throws EntityNotFoundException {
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("person.id", personId));
		criteria.add(Restrictions.eq("study", study));
		LinkSubjectStudy subject = (LinkSubjectStudy) criteria.uniqueResult();
		if (subject == null) {
			throw new EntityNotFoundException("The Subject does not exist in the system");
		}
		return subject;
	}

	public List<SubjectUidPadChar> getListOfSubjectUidPadChar() {
		Example subjectUidPadChar = Example.create(new SubjectUidPadChar());
		Criteria studyStatusCriteria = getSession().createCriteria(SubjectUidPadChar.class).add(subjectUidPadChar);
		return studyStatusCriteria.list();
	}

	public String getSubjectUidExample(Study study) {
		String subjectUidPrefix = new String("");
		String subjectUidToken = new String("");
		String subjectUidPaddedIncrementor = new String("");
		String subjectUidPadChar = new String("0");
		String subjectUidStart = new String("");
		String subjectUidExample = new String("");

		if (study.getId() != null && study.getAutoGenerateSubjectUid() != null) {
			if (study.getSubjectUidPrefix() != null)
				subjectUidPrefix = study.getSubjectUidPrefix();

			if (study.getSubjectUidToken() != null)
				subjectUidToken = study.getSubjectUidToken().getName();

			if (study.getSubjectUidPadChar() != null) {
				subjectUidPadChar = study.getSubjectUidPadChar().getName().trim();
			}

			if (study.getSubjectUidStart() != null)
				subjectUidStart = study.getSubjectUidStart().toString();

			int size = Integer.parseInt(subjectUidPadChar);
			subjectUidPaddedIncrementor = StringUtils.leftPad(subjectUidStart, size, "0");
			subjectUidExample = subjectUidPrefix + subjectUidToken + subjectUidPaddedIncrementor;
		}
		else {
			subjectUidPrefix = "";
			subjectUidToken = "";
			subjectUidPadChar = "";
			subjectUidPaddedIncrementor = "";
			subjectUidExample = null;
		}
		return subjectUidExample;
	}

	public List<SubjectUidToken> getListOfSubjectUidToken() {
		Example subjectUidToken = Example.create(new SubjectUidToken());
		Criteria studyStatusCriteria = getSession().createCriteria(SubjectUidToken.class).add(subjectUidToken);
		return studyStatusCriteria.list();
	}

	public GenderType getGenderType(String name) {
		Criteria criteria = getSession().createCriteria(GenderType.class);
		criteria.add(Restrictions.eq("name", name));
		GenderType genderType = new GenderType();
		List<GenderType> results = criteria.list();
		if (!results.isEmpty()) {
			genderType = (GenderType) results.get(0);
		}
		return genderType;
	}

	public VitalStatus getVitalStatus(String name) {
		Criteria criteria = getSession().createCriteria(VitalStatus.class);
		criteria.add(Restrictions.eq("name", name));
		VitalStatus vitalStatus = new VitalStatus();
		List<VitalStatus> results = criteria.list();

		if (!results.isEmpty()) {
			vitalStatus = (VitalStatus) results.get(0);
		}
		return vitalStatus;
	}

	public TitleType getTitleType(String name) {
		Criteria criteria = getSession().createCriteria(TitleType.class);
		criteria.add(Restrictions.eq("name", name));
		TitleType titleType = new TitleType();
		List<TitleType> results = criteria.list();
		if (!results.isEmpty()) {
			titleType = (TitleType) results.get(0);
		}
		return titleType;
	}

	public MaritalStatus getMaritalStatus(String name) {
		Criteria criteria = getSession().createCriteria(MaritalStatus.class);
		criteria.add(Restrictions.eq("name", name));
		MaritalStatus maritalStatus = new MaritalStatus();
		List<MaritalStatus> results = criteria.list();
		if (!results.isEmpty()) {
			maritalStatus = (MaritalStatus) results.get(0);
		}
		return maritalStatus;
	}

	public PersonContactMethod getPersonContactMethod(String name) {
		Criteria criteria = getSession().createCriteria(PersonContactMethod.class);
		criteria.add(Restrictions.eq("name", name));
		PersonContactMethod personContactMethod = new PersonContactMethod();
		List<PersonContactMethod> results = criteria.list();
		if (!results.isEmpty()) {
			personContactMethod = (PersonContactMethod) results.get(0);
		}
		return personContactMethod;
	}

	public long getStudySubjectCount(SubjectVO subjectVO) {
		// Handle for study not in context
		// GEORGE - 30/1/15 Removed handle to allow for global search. Need to test to see if this fails anywhere.
//		if (subjectVO.getLinkSubjectStudy().getStudy() == null) {
//			return 0;
//		}

		Criteria criteria = buildGeneralSubjectCriteria(subjectVO);
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		return totalCount.intValue();
	}

	private Criteria buildGeneralSubjectCriteria(SubjectVO subjectVO) {
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.createAlias("person", "p");
		if(subjectVO.getLinkSubjectStudy().getStudy() != null) {
			criteria.add(Restrictions.eq("study.id", subjectVO.getLinkSubjectStudy().getStudy().getId()));
		} else {
			criteria.add(Restrictions.in("study", subjectVO.getStudyList()));
			criteria.createAlias("study", "st");
			criteria.addOrder(Order.asc("st.name"));
		}
		if (subjectVO.getLinkSubjectStudy().getPerson() != null) {

			if (subjectVO.getLinkSubjectStudy().getPerson().getId() != null) {
				criteria.add(Restrictions.eq("p.id", subjectVO.getLinkSubjectStudy().getPerson().getId()));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getFirstName() != null) {
				criteria.add(Restrictions.ilike("p.firstName", subjectVO.getLinkSubjectStudy().getPerson().getFirstName(), MatchMode.ANYWHERE));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getMiddleName() != null) {
				criteria.add(Restrictions.ilike("p.middleName", subjectVO.getLinkSubjectStudy().getPerson().getMiddleName(), MatchMode.ANYWHERE));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getLastName() != null) {
				/* old code pre George adding personlastname lookup criteria.add(Restrictions.ilike("p.lastName", subjectVO.getLinkSubjectStudy().getPerson().getLastName(), MatchMode.ANYWHERE));*/
				//log.info("Lastname: " + subjectVO.getLinkSubjectStudy().getPerson().getLastName());
				DetachedCriteria previousLastNames = DetachedCriteria.forClass(PersonLastnameHistory.class, "l")
						.setProjection(Projections.property("l.lastName"))
						.add(Restrictions.ilike("l.lastName", subjectVO.getLinkSubjectStudy().getPerson().getLastName(), MatchMode.ANYWHERE))
						.add(Restrictions.eqProperty("p.id", "l.person.id"));
				criteria.add(Restrictions.or(Restrictions.ilike("p.lastName", subjectVO.getLinkSubjectStudy().getPerson().getLastName(), MatchMode.ANYWHERE), Subqueries.exists(previousLastNames)));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getDateOfBirth() != null) {
				criteria.add(Restrictions.eq("p.dateOfBirth", subjectVO.getLinkSubjectStudy().getPerson().getDateOfBirth()));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getGenderType() != null) {
				criteria.add(Restrictions.eq("p.genderType.id", subjectVO.getLinkSubjectStudy().getPerson().getGenderType().getId()));
			}

			if (subjectVO.getLinkSubjectStudy().getPerson().getVitalStatus() != null) {	
				criteria.add(Restrictions.eq("p.vitalStatus.id", subjectVO.getLinkSubjectStudy().getPerson().getVitalStatus().getId()));
			}

			if(!subjectVO.getLinkSubjectStudy().getPerson().getOtherIDs().isEmpty()) {
				OtherID o = (OtherID) subjectVO.getLinkSubjectStudy().getPerson().getOtherIDs().toArray()[0];
				if(o != null && o.getOtherID()!= null && !o.getOtherID().isEmpty()) {
					log.info("OtherID search");	
//					DetachedCriteria otherID = DetachedCriteria.forClass(OtherID.class, "O")
//							.setProjection(Projections.projectionList().add(Projections.property("O.otherID")))
//							.add(Restrictions.ilike("O.otherID", ((OtherID) subjectVO.getLinkSubjectStudy().getPerson().getOtherIDs().toArray()[0]).getOtherID(), MatchMode.EXACT))
//							.add(Restrictions.eqProperty("p.id", "O.person.id"));
//					criteria.add(Subqueries.exists(otherID));
					criteria.createAlias("p.otherIDs", "o");
					criteria.add(Restrictions.ilike("o.otherID", ((OtherID) subjectVO.getLinkSubjectStudy().getPerson().getOtherIDs().toArray()[0]).getOtherID(), MatchMode.ANYWHERE));
					criteria.setProjection(Projections.distinct(Projections.projectionList()
							.add(Projections.property("o.personid"), "lss.person.id")));
				}
			}
		}

		if (subjectVO.getLinkSubjectStudy().getSubjectUID() != null && subjectVO.getLinkSubjectStudy().getSubjectUID().length() > 0) {
			criteria.add(Restrictions.ilike("subjectUID", subjectVO.getLinkSubjectStudy().getSubjectUID(), MatchMode.ANYWHERE));
		}

		if (subjectVO.getLinkSubjectStudy().getSubjectStatus() != null) {
			criteria.add(Restrictions.eq("subjectStatus", subjectVO.getLinkSubjectStudy().getSubjectStatus()));
			SubjectStatus subjectStatus = getSubjectStatus("Archive");
			if (subjectStatus != null) {
				criteria.add(Restrictions.ne("subjectStatus", subjectStatus));
			}
		}
		else {
			SubjectStatus subjectStatus = getSubjectStatus("Archive");
			if (subjectStatus != null) {
				criteria.add(Restrictions.ne("subjectStatus", subjectStatus));
			}
		}
		if (subjectVO.getRelativeUIDs().size() > 0) {
			criteria.add(Restrictions.not(Restrictions.in("subjectUID",
					subjectVO.getRelativeUIDs().toArray())));
		}

		criteria.setProjection(Projections.distinct(Projections.projectionList().add(Projections.id())));

		criteria.addOrder(Order.asc("subjectUID"));
		return criteria;
	}

	public List<SubjectVO> searchPageableSubjects(SubjectVO subjectVoCriteria, int first, int count) {		
		Criteria criteria = buildGeneralSubjectCriteria(subjectVoCriteria);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);

		List<Long> longs = criteria.list();

		List<LinkSubjectStudy> list = new ArrayList<LinkSubjectStudy>();//criteria.list();

		for(Long l : longs) {
			try {
				list.add(getLinkSubjectStudy(l));
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
			}
		}

		List<SubjectVO> subjectVOList = new ArrayList<SubjectVO>();

		// TODO analyse
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {

			LinkSubjectStudy linkSubjectStudy = (LinkSubjectStudy) iterator.next();
			// Place the LinkSubjectStudy instance into a SubjectVO and add the
			// SubjectVO into a List
			SubjectVO subject = new SubjectVO();
			subject.setLinkSubjectStudy(linkSubjectStudy);
			// Person person = subject.getLinkSubjectStudy().getPerson();
			subject.setSubjectUID(linkSubjectStudy.getSubjectUID());
			subjectVOList.add(subject);
		}
		return subjectVOList;
	}

	public List<ConsentStatus> getRecordableConsentStatus() {
		Criteria criteria = getSession().createCriteria(ConsentStatus.class);
		criteria.add(Restrictions.not(Restrictions.ilike("name", "Not Consented", MatchMode.ANYWHERE)));
		return criteria.list();
	}

	/**
	 * Look up a Person based on the supplied Long ID that represents a Person primary key. This id is the primary key of the Person table that can
	 * represent a subject or contact.
	 * 
	 * @param personId
	 * @return
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public Person getPerson(Long personId) throws EntityNotFoundException, ArkSystemException {

		Criteria personCriteria = getSession().createCriteria(Person.class);
		personCriteria.add(Restrictions.eq("id", personId));
		List<Person> listOfPerson = personCriteria.list();
		if (listOfPerson != null && listOfPerson.size() > 0) {
			return listOfPerson.get(0);
		}
		else {
			throw new EntityNotFoundException("The entity with id" + personId.toString() + " cannot be found.");
		}
	}

	public ArkFunctionType getArkFunctionType(String reportType) {
		Criteria criteria = getSession().createCriteria(ArkFunctionType.class);
		criteria.add(Restrictions.eq("name", reportType));
		criteria.setMaxResults(1);
		return (ArkFunctionType) criteria.uniqueResult();
	}

	public List<ArkFunction> getModuleFunction(ArkModule arkModule) {

		ArkFunctionType arkFunctionType = getArkFunctionType(Constants.ARK_FUNCTION_TYPE_NON_REPORT);

		Criteria criteria = getSession().createCriteria(ArkModuleFunction.class);
		criteria.createAlias("arkFunction", "aliasArkFunction");
		criteria.add(Restrictions.eq("arkModule", arkModule));
		// Pass in an instance that represents arkFunctionType non-report
		criteria.add(Restrictions.eq("aliasArkFunction.arkFunctionType", arkFunctionType));
		criteria.addOrder(Order.asc("functionSequence"));
		List<ArkModuleFunction> listOfArkModuleFunction = criteria.list();
		List<ArkFunction> arkFunctionList = new ArrayList<ArkFunction>();
		for (ArkModuleFunction arkModuleFunction : listOfArkModuleFunction) {
			arkFunctionList.add(arkModuleFunction.getArkFunction());
		}

		return arkFunctionList;
	}

	public List<PhoneStatus> getPhoneStatus() {
		Criteria criteria = getSession().createCriteria(PhoneStatus.class);
		criteria.addOrder(Order.asc("name"));
		return criteria.list();
	}

	public Boolean studyHasSubjects(Study study) {
		StatelessSession session = getStatelessSession();
		Criteria criteria = session.createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		session.close();
		return totalCount.intValue() > 0;
	}

	public List<Study> getStudiesForUser(ArkUser arkUser, Study study) {

		Criteria criteria = getSession().createCriteria(ArkUserRole.class);
		criteria.createAlias("arkStudy", "arkStudy");

		criteria.add(Restrictions.eq("arkUser", arkUser));// Represents the user
		// either who is
		// logged in or one
		// that is provided
		if (study.getId() != null) {
			criteria.add(Restrictions.eq("arkStudy.id", study.getId()));
		}

		if (study.getName() != null) {
			criteria.add(Restrictions.ilike("arkStudy.name", study.getName(), MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.distinct(Projections.property("study")));
		List<Study> studies = (List<Study>) criteria.list();
		return studies;

	}

	public long getCountOfStudies() {
		int total = 0;
		Long longTotal = ((Long) getSession().createQuery("select count(*) from Study").iterate().next());
		total = longTotal.intValue();
		return total;
	}

	public FileFormat getFileFormatByName(String name) {
		FileFormat fileFormat = null;
		Criteria criteria = getSession().createCriteria(FileFormat.class);
		criteria.add(Restrictions.eq("name", name));

		List<FileFormat> results = criteria.list();
		if (results.size() > 0) {
			fileFormat = (FileFormat) results.get(0);
		}
		return fileFormat;
	}

	public Collection<FileFormat> getFileFormats() {
		Criteria criteria = getSession().createCriteria(FileFormat.class);
		java.util.Collection<FileFormat> fileFormatCollection = criteria.list();
		return fileFormatCollection;
	}

	public DelimiterType getDelimiterType(Long id) {
		DelimiterType delimiterType = (DelimiterType) getSession().get(DelimiterType.class, id);
		return delimiterType;
	}

	public Collection<DelimiterType> getDelimiterTypes() {
		Criteria criteria = getSession().createCriteria(DelimiterType.class);
		java.util.Collection<DelimiterType> delimiterTypeCollection = criteria.list();
		return delimiterTypeCollection;
	}

	public Collection<UploadType> getUploadTypes() {
		Criteria criteria = getSession().createCriteria(UploadType.class);
		java.util.Collection<UploadType> delimiterTypeCollection = criteria.list();
		return delimiterTypeCollection;
	}

	public UploadType getDefaultUploadType() {
		return (UploadType) (getSession().get(UploadType.class, 1L));// TODO: maybe// fix// ALL// such// entities// by// adding// isDefault// boolean// to// table?
	}

	public UploadType getDefaultUploadTypeForLims() {
		return (UploadType) (getSession().get(UploadType.class, 4L));// TODO: maybe// fix// ALL// such// entities// by// adding// isDefault// boolean// to// table?
	}

	public UploadType getCustomFieldDataUploadType() {
		return (UploadType) (getSession().get(UploadType.class, 3L));// TODO: maybe// fix// ALL// such// entities// by// adding// isDefault// boolean// to// table?
	}

	public List<Upload> searchUploads(Upload uploadCriteria) {
		Criteria criteria = getSession().createCriteria(Upload.class);
		// Must be constrained on the arkFunction
		criteria.add(Restrictions.eq("arkFunction", uploadCriteria.getArkFunction()));

		if (uploadCriteria.getId() != null) {
			criteria.add(Restrictions.eq("id", uploadCriteria.getId()));
		}

		if (uploadCriteria.getStudy() != null) {
			criteria.add(Restrictions.eq("study", uploadCriteria.getStudy()));
		}

		if (uploadCriteria.getFileFormat() != null) {
			criteria.add(Restrictions.ilike("fileFormat", uploadCriteria.getFileFormat()));
		}

		if (uploadCriteria.getDelimiterType() != null) {
			criteria.add(Restrictions.ilike("delimiterType", uploadCriteria.getDelimiterType()));
		}

		if (uploadCriteria.getFilename() != null) {
			criteria.add(Restrictions.ilike("filename", uploadCriteria.getFilename()));
		}

		criteria.addOrder(Order.desc("id"));
		List<Upload> resultsList = criteria.list();

		return resultsList;
	}

	public ArkFunction getArkFunctionByName(String functionName) {
		Criteria criteria = getSession().createCriteria(ArkFunction.class);
		criteria.add(Restrictions.eq("name", functionName));
		criteria.setMaxResults(1);
		ArkFunction arkFunction = (ArkFunction) criteria.uniqueResult();
		return arkFunction;
	}

	public List<Upload> searchUploadsForBio(Upload uploadCriteria) {
		Criteria criteria = getSession().createCriteria(Upload.class);
		// - due to nature of table design...we need to specify it like this
		// ideally we might want to just have arkmodule in the upload table?
		// criteria.add(Restrictions.eq("arkFunction",
		// uploadCriteria.getArkFunction()));

		ArkFunction biospecArkFunction = getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN);
		//ArkFunction biocollArkFunction = getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION);
		ArkFunction biocollArkFunction = getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD);

		List<ArkFunction> arkFunctionsForBio = new ArrayList<ArkFunction>();
		arkFunctionsForBio.add(biospecArkFunction);
		arkFunctionsForBio.add(biocollArkFunction);

		criteria.add(Restrictions.in("arkFunction", arkFunctionsForBio));

		if (uploadCriteria.getId() != null) {
			criteria.add(Restrictions.eq("id", uploadCriteria.getId()));
		}

		if (uploadCriteria.getStudy() != null) {
			criteria.add(Restrictions.eq("study", uploadCriteria.getStudy()));
		}

		if (uploadCriteria.getFileFormat() != null) {
			criteria.add(Restrictions.ilike("fileFormat", uploadCriteria.getFileFormat()));
		}

		if (uploadCriteria.getDelimiterType() != null) {
			criteria.add(Restrictions.ilike("delimiterType", uploadCriteria.getDelimiterType()));
		}

		if (uploadCriteria.getFilename() != null) {
			criteria.add(Restrictions.ilike("filename", uploadCriteria.getFilename()));
		}

		criteria.addOrder(Order.desc("id"));
		List<Upload> resultsList = criteria.list();

		return resultsList;
	}

	public List<Upload> searchUploadsForBiospecimen(Upload uploadCriteria, List studyListForUser) {
		Criteria criteria = getSession().createCriteria(Upload.class);
		// - due to nature of table design...we need to specify it like this
		// ideally we might want to just have arkmodule in the upload table?
		// criteria.add(Restrictions.eq("arkFunction",
		// uploadCriteria.getArkFunction()));

		ArkFunction biospecArkFunction = getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN);

		List<ArkFunction> arkFunctionsForBio = new ArrayList<ArkFunction>();
		arkFunctionsForBio.add(biospecArkFunction);

		criteria.add(Restrictions.eq("arkFunction", uploadCriteria.getArkFunction()));

		if (uploadCriteria.getId() != null) {
			criteria.add(Restrictions.eq("id", uploadCriteria.getId()));
		}

		if (!studyListForUser.isEmpty()) {
			criteria.add(Restrictions.in("study", studyListForUser));
		}

		if (uploadCriteria.getFileFormat() != null) {
			criteria.add(Restrictions.ilike("fileFormat", uploadCriteria.getFileFormat()));
		}

		if (uploadCriteria.getDelimiterType() != null) {
			criteria.add(Restrictions.ilike("delimiterType", uploadCriteria.getDelimiterType()));
		}

		if (uploadCriteria.getFilename() != null) {
			criteria.add(Restrictions.ilike("filename", uploadCriteria.getFilename()));
		}

		criteria.addOrder(Order.desc("id"));
		List<Upload> resultsList = criteria.list();

		return resultsList;
	}

	public void createUpload(Upload studyUpload) throws Exception {
		if (studyUpload.getUploadStatus() == null) {
			//studyUpload.setUploadStatus(getUploadStatusForUndefined());
			studyUpload.setUploadStatus(getUploadStatusFor(Constants.UPLOAD_STATUS_STATUS_NOT_DEFINED));
		}
		Subject currentUser = SecurityUtils.getSubject();
		String userId = (String) currentUser.getPrincipal();
		studyUpload.setUserId(userId);
		getSession().save(studyUpload);
		// getSession().flush();
	}

	public void updateUpload(Upload studyUpload) {
		getSession().update(studyUpload);
	}

	public String getDelimiterTypeNameByDelimiterChar(char delimiterCharacter) {
		String delimiterTypeName = null;
		Criteria criteria = getSession().createCriteria(DelimiterType.class);
		criteria.add(Restrictions.eq("delimiterCharacter", delimiterCharacter));
		criteria.setProjection(Projections.property("name"));
		delimiterTypeName = (String) criteria.uniqueResult();
		return delimiterTypeName;
	}
	
	public DelimiterType getDelimiterTypeByDelimiterChar(char delimiterCharacter) {
		Criteria criteria = getSession().createCriteria(DelimiterType.class);
		criteria.add(Restrictions.eq("delimiterCharacter", delimiterCharacter));
		return (DelimiterType) criteria.uniqueResult();
	}

	public void createCustomFieldUpload(CustomFieldUpload cfUpload) {
		getSession().save(cfUpload);
	}

	public List<BiospecimenUidToken> getBiospecimenUidTokens() {
		Criteria criteria = getSession().createCriteria(BiospecimenUidToken.class);
		return criteria.list();
	}

	public List<BiospecimenUidPadChar> getBiospecimenUidPadChars() {
		Criteria criteria = getSession().createCriteria(BiospecimenUidPadChar.class);
		return criteria.list();
	}

	public List<Study> getStudyListAssignedToBiospecimenUidTemplate() {
		Criteria criteria = getSession().createCriteria(BiospecimenUidTemplate.class);
		criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("study")));
		return criteria.list();
	}

	public void createBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate) {
		getSession().save(biospecimenUidTemplate);
	}

	public List<BioCollectionUidToken> getBioCollectionUidToken() {
		Example token = Example.create(new BioCollectionUidToken());
		Criteria criteria = getSession().createCriteria(BioCollectionUidToken.class).add(token);
		return criteria.list();
	}

	public List<BioCollectionUidPadChar> getBioCollectionUidPadChar() {
		Criteria criteria = getSession().createCriteria(BioCollectionUidPadChar.class);
		return criteria.list();
	}

	public void createBioCollectionUidTemplate(BioCollectionUidTemplate bioCollectionUidTemplate) {
		getSession().save(bioCollectionUidTemplate);
	}

	public Boolean studyHasBiospecimen(Study study) {
		StatelessSession session = getStatelessSession();
		Criteria criteria = session.createCriteria(Biospecimen.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		session.close();
		return totalCount.intValue() > 0;
	}

	public Boolean studyHasBioCollection(Study study) {
		StatelessSession session = getStatelessSession();
		Criteria criteria = session.createCriteria(BioCollection.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		session.close();
		return totalCount.intValue() > 0;
	}

	public BiospecimenUidTemplate getBiospecimentUidTemplate(Study study) {
		Criteria criteria = getSession().createCriteria(BiospecimenUidTemplate.class);
		criteria.add(Restrictions.eq("study", study));
		return (BiospecimenUidTemplate) criteria.uniqueResult();
	}

	public BioCollectionUidTemplate getBioCollectionUidTemplate(Study study) {
		Criteria criteria = getSession().createCriteria(BioCollectionUidTemplate.class);
		criteria.add(Restrictions.eq("study", study));
		return (BioCollectionUidTemplate) criteria.uniqueResult();
	}

	public void updateBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate) {
		getSession().saveOrUpdate(biospecimenUidTemplate);
	}

	public void updateBioCollectionUidTemplate(BioCollectionUidTemplate bioCollectionUidTemplate) {
		getSession().saveOrUpdate(bioCollectionUidTemplate);
	}

	public long getCountOfSubjects(Study study) {
		int total = 0;
		total = ((Long) getSession().createQuery("select count(*) from LinkSubjectStudy where study = :study").setParameter("study", study).iterate().next()).intValue();
		return total;
	}

	public List<SubjectVO> matchSubjectsFromInputFile(FileUpload subjectFileUpload, Study study) {
		List<SubjectVO> subjectVOList = new ArrayList<SubjectVO>();
		List<String> subjectUidList = new ArrayList<String>(0);

		if(subjectFileUpload != null) {
			try {
				subjectUidList = CsvListReader.readColumnIntoList(subjectFileUpload.getInputStream());
			}
			catch (IOException e) {
				log.error("Error in Subject list file");
				return subjectVOList;
			}

			Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
			criteria.add(Restrictions.eq("study", study));
			criteria.add(Restrictions.in("subjectUID", subjectUidList));
			List<LinkSubjectStudy> subjectList = criteria.list();

			for (Iterator<LinkSubjectStudy> iterator = subjectList.iterator(); iterator.hasNext();) {
				LinkSubjectStudy linkSubjectStudy = (LinkSubjectStudy) iterator.next();
				// Place the LinkSubjectStudy instance into a SubjectVO and add the SubjectVO into a List
				SubjectVO subject = new SubjectVO();
				subject.setSubjectUID(linkSubjectStudy.getSubjectUID());
				subject.setLinkSubjectStudy(linkSubjectStudy);
				//Person person = subject.getLinkSubjectStudy().getPerson();
				//subject.setSubjectPreviousLastname(getPreviousLastname(person));
				subjectVOList.add(subject);
			}
		}
		return subjectVOList;
	}

	public List<Study> getAssignedChildStudyListForPerson(Study study, Person person) {
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.createAlias("study", "s");
		criteria.add(Restrictions.eq("person", person));
		criteria.add(Restrictions.eq("s.parentStudy", study));
		criteria.add(Restrictions.ne("s.id", study.getId()));
		criteria.add(Restrictions.ne("subjectStatus", getSubjectStatus("Archive")));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("study"), "study");
		criteria.setProjection(projectionList);

		return criteria.list();
	}

	public List<ConsentOption> getConsentOptionList() {
		Criteria criteria = getSession().createCriteria(ConsentOption.class);
		return criteria.list();
	}

	public boolean customFieldHasData(CustomField customField) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT count(*) FROM ");
		sb.append(" CustomField cf, ");
		sb.append(" CustomFieldDisplay cfd, ");
		sb.append(" PhenoData pd ");
		sb.append("WHERE cf.study.id = :studyId ");
		sb.append(" AND cf.arkFunction.id = :arkFunctionId ");
		sb.append(" AND cf.id = cfd.customField.id ");
		sb.append(" AND pd.customFieldDisplay.id = cfd.id");

		Query query = getSession().createQuery(sb.toString());
		query.setParameter("studyId", customField.getStudy().getId());
		query.setParameter("arkFunctionId", customField.getArkFunction().getId());
		return ((Number) query.iterate().next()).intValue() > 0;
	}

	public long countNumberOfSubjectsThatAlreadyExistWithTheseUIDs(Study study, Collection<String> subjectUids) {
		String queryString = "select count(*) " + "from LinkSubjectStudy subject " + "where study =:study " + "and subjectUID in  (:subjects) ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("study", study);
		query.setParameterList("subjects", subjectUids);

		return (Long) query.uniqueResult();
	}

	public List<String> getSubjectUIDsThatAlreadyExistWithTheseUIDs(Study study, Collection<String> subjectUids) {
		String queryString = "select subject.subjectUID " + "from LinkSubjectStudy subject " + "where study =:study " + "and subjectUID in  (:subjects) ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("study", study);
		query.setParameterList("subjects", subjectUids);

		return query.list();
	}

	/**
	 * based on sql concept of;4 select id from custom_field_display where custom_field_id in (SELECT id FROM custom_field where name='AGE' and
	 * study_id = 1 and ark_function_id = 5)
	 * 
	 * @param fieldNameCollection
	 * @param study
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CustomFieldDisplay> getCustomFieldDisplaysIn(List<String> fieldNameCollection, Study study, ArkFunction arkFunction) {

		if (fieldNameCollection == null || fieldNameCollection.isEmpty()) {
			return new ArrayList<CustomFieldDisplay>();
		}
		else {
			List<String> lowerCaseNames = new ArrayList<String>();
			for (String name : fieldNameCollection) {
				lowerCaseNames.add(name.toLowerCase());
			}
			String queryString = "select cfd " + 
								"from CustomFieldDisplay cfd " + 
								"where customField.id in ( " + " SELECT id from CustomField cf " + 
																" where cf.study =:study "
																+ " and lower(cf.name) in (:names) " + " and cf.arkFunction =:arkFunction )";
			Query query = getSession().createQuery(queryString);
			query.setParameter("study", study);
			// query.setParameterList("names", fieldNameCollection);
			query.setParameterList("names", lowerCaseNames);
			query.setParameter("arkFunction", arkFunction);
			return query.list();
		}
	}

	@SuppressWarnings("unchecked")
	public List<CustomFieldDisplay> getCustomFieldDisplaysIn(Study study, ArkFunction arkFunction) {

		String queryString = "select cfd " + " from CustomFieldDisplay cfd " + 
							" where customField.id in ( " + " SELECT id from CustomField cf " + 
															" where cf.study =:study "
															+ " and cf.arkFunction =:arkFunction )";
		Query query = getSession().createQuery(queryString);
		query.setParameter("study", study);
		query.setParameter("arkFunction", arkFunction);
		return query.list();

	}

	public List<PhenoDataSetFieldDisplay> getPhenoFieldDisplaysIn(Study study, ArkFunction arkFunction) {
		String queryString = "select pdsfd from PhenoDataSetFieldDisplay pdsfd " +
				" where phenoDataSetField.id in ( " +
				" SELECT id from PhenoDataSetField pdsf " +
				" where pdsf.study =:study " + " and pdsf.arkFunction =:arkFunction )";
		Query query = getSession().createQuery(queryString);
		query.setParameter("study", study);
		query.setParameter("arkFunction", arkFunction);
		return query.list();
	}

	/**
	 * based on sql concept of; select id from custom_field_display where custom_field_id in (SELECT id FROM custom_field where name='AGE' and study_id
	 * = 1 and ark_function_id = 5)
	 * 
	 * @param fieldNameCollection
	 * @param study
	 * @return
	 */
	/*@SuppressWarnings("unchecked")
	public List<CustomFieldDisplay> getCustomFieldDisplaysIn(List<String> fieldNameCollection, Study study, ArkFunction arkFunction, CustomFieldGroup customFieldGroup) {

		if (fieldNameCollection == null || fieldNameCollection.isEmpty()) {
			return new ArrayList<CustomFieldDisplay>();
		}
		else {
			List<String> lowerCaseNames = new ArrayList<String>();
			for (String name : fieldNameCollection) {
				lowerCaseNames.add(name.toLowerCase());
			}
			String queryString = "select cfd from CustomFieldDisplay cfd " + 
					" where cfd.customFieldGroup =:customFieldGroup and customField.id in ( " + 
					" SELECT id from CustomField cf " + 
					" where cf.study =:study " + " and lower(cf.name) in (:names) " + " and cf.arkFunction =:arkFunction )";
			Query query = getSession().createQuery(queryString); 
			query.setParameter("study", study);
			// query.setParameterList("names", fieldNameCollection);
			query.setParameterList("names", lowerCaseNames);
			query.setParameter("arkFunction", arkFunction);
			query.setParameter("customFieldGroup", customFieldGroup);
			return query.list();
		}
	}*/

	@SuppressWarnings("unchecked")
	public List<LinkSubjectStudy> getSubjectsThatAlreadyExistWithTheseUIDs(Study study, Collection subjectUids) {
		String queryString = "select subject " + "from LinkSubjectStudy subject " + "where study =:study " + "and subjectUID in  (:subjects) ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("study", study);
		query.setParameterList("subjects", subjectUids);

		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllSubjectUIDs(Study study) {
		String queryString = "select subject.subjectUID " + "from LinkSubjectStudy subject " + "where study =:study " + "order by subjectUID ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("study", study);	

		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<SubjectCustomFieldData> getSubjectCustomFieldDataFor(Collection customFieldDisplaysThatWeNeed, List subjectUIDsToBeIncluded) {
		if (customFieldDisplaysThatWeNeed == null || customFieldDisplaysThatWeNeed.isEmpty() || subjectUIDsToBeIncluded == null || subjectUIDsToBeIncluded.isEmpty()) {
			return new ArrayList<SubjectCustomFieldData>();
		}
		else {
			String queryString = "select scfd " + " from SubjectCustomFieldData scfd " + " where scfd.linkSubjectStudy in (:subjectUIDsToBeIncluded) "
					+ " and scfd.customFieldDisplay in (:customFieldDisplaysThatWeNeed) ";
			Query query = getSession().createQuery(queryString);
			query.setParameterList("subjectUIDsToBeIncluded", subjectUIDsToBeIncluded);
			query.setParameterList("customFieldDisplaysThatWeNeed", customFieldDisplaysThatWeNeed);
			return query.list();
		}
	}
	public Payload createPayload(byte[] bytes) {
		Payload payload = new Payload(bytes);
		getSession().save(payload);
		getSession().flush();
		getSession().refresh(payload);
		return payload;
	}

	public Payload getPayloadForUpload(Upload upload) {
		getSession().refresh(upload);// bit paranoid but the code calling this
		// may be from wicket and not be attached?
		return upload.getPayload();
	}
	public UploadStatus getUploadStatusFor(String statusFromConstant) {
		Criteria criteria = getSession().createCriteria(UploadStatus.class);
		criteria.add(Restrictions.eq("name", statusFromConstant));
		return (UploadStatus) criteria.uniqueResult();
	}
	@SuppressWarnings("unchecked")
	public Collection<UploadType> getUploadTypesForSubject(Study study) {
		Criteria criteria = getSession().createCriteria(UploadType.class);
		criteria.add(Restrictions.eq("arkModule", getArkModuleForSubject()));
		if(study != null && study.getParentStudy() != null) { //i.e. study is a child study
			criteria.add(Restrictions.not(Restrictions.eq("name", "Subject Demographic Data")));
		}
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public Collection<UploadType> getUploadTypesForLims() {
		Criteria criteria = getSession().createCriteria(UploadType.class);
		criteria.add(Restrictions.eq("arkModule", getArkModuleForLims()));
		criteria.addOrder(Order.asc("order"));
		return criteria.list();
	}

	public ArkModule getArkModuleForSubject() {
		Criteria criteria = getSession().createCriteria(ArkModule.class);
		criteria.add(Restrictions.eq("name", "Subject"));
		return (ArkModule) criteria.uniqueResult();
	}

	public ArkModule getArkModuleForLims() {
		Criteria criteria = getSession().createCriteria(ArkModule.class);
		criteria.add(Restrictions.eq("name", "LIMS"));
		return (ArkModule) criteria.uniqueResult();
	}

	public YesNo getYes() {
		Criteria criteria = getSession().createCriteria(YesNo.class);
		criteria.add(Restrictions.eq("name", "Yes"));
		return (YesNo) criteria.uniqueResult();
	}

	public YesNo getNo() {
		Criteria criteria = getSession().createCriteria(YesNo.class);
		criteria.add(Restrictions.eq("name", "No"));
		return (YesNo) criteria.uniqueResult();
	}

	public List<Search> getSearchesForThisStudy(Study study) {
		Criteria criteria = getSession().createCriteria(Search.class);
		criteria.add(Restrictions.eq("study", study));
		List<Search> searchList = criteria.list();
		return searchList;
	}

	public boolean create(Search search) throws EntityExistsException {
		boolean success = true;
		if (isSearchNameTaken(search.getName(), search.getStudy(), null)) {
			throw new EntityExistsException("Search name '" + search.getName() + "' is already taken.  Please select a unique name");
		}
		getSession().save(search);
		return success;
	}

	public boolean update(Search search) throws EntityExistsException {
		boolean success = true;
		if (isSearchNameTaken(search.getName(), search.getStudy(), search.getId())) {
			throw new EntityExistsException("Search name '" + search.getName() + "' is already taken.  Please select a unique name");
		}
		getSession().update(search);
		return success;
	}

	public boolean create(SearchVO searchVO) throws EntityExistsException {
		boolean success = true;
		if (isSearchNameTaken(searchVO.getSearch().getName(), searchVO.getSearch().getStudy(), null)) {
			throw new EntityExistsException("Search name '" + searchVO.getSearch().getName() + "' is already taken.  Please select a unique name");
		}
		getSession().save(searchVO.getSearch());
		getSession().refresh(searchVO.getSearch());
	
		createOrUpdateFields(searchVO);
		
		return success;
	}

	public boolean update(SearchVO searchVO) throws EntityExistsException {
		//start save basic search info
		boolean success = true;
		Search search = searchVO.getSearch();
		if (isSearchNameTaken(search.getName(), search.getStudy(), search.getId())) {
			throw new EntityExistsException("Search name '" + search.getName() + "' is already taken.  Please select a unique name");
		}
		getSession().update(search);
		getSession().flush();
		getSession().refresh(search);
		//end save basic search info
		
		createOrUpdateFields(searchVO);
		
		return success;
	}
	
	private void createOrUpdateFields(SearchVO searchVO) { 
		
		Search search = searchVO.getSearch();
		
		//start save demographic fields
		Collection<DemographicField> listOfDemographicFieldsFromVO = searchVO.getSelectedDemographicFields();
		List<DemographicFieldSearch> nonPoppableDFS = new ArrayList<DemographicFieldSearch>();
		nonPoppableDFS.addAll(search.getDemographicFieldsToReturn());
		List<DemographicField> nonPoppableDemographicFieldsFromVO = new ArrayList<DemographicField>();
		nonPoppableDemographicFieldsFromVO.addAll(listOfDemographicFieldsFromVO);
		for (DemographicFieldSearch dfs : nonPoppableDFS) {
			boolean toBeDeleted = true; // if we find no match along the way, conclude that it has been deleted.

			for (DemographicField field : nonPoppableDemographicFieldsFromVO) {
				if (dfs.getDemographicField().getId().equals(field.getId())) {
					toBeDeleted = false;
					listOfDemographicFieldsFromVO.remove(field); // we found it, therefore  remove it  from the list that will ultimately be added as DFS's
				}
			}
			if (toBeDeleted) {
				search.getDemographicFieldsToReturn().remove(dfs);
				getSession().update(search);
				getSession().delete(dfs);
				getSession().flush();
				getSession().refresh(search);
			}
		}

		for (DemographicField field : listOfDemographicFieldsFromVO) {
			DemographicFieldSearch dfs = new DemographicFieldSearch(field, search);
			getSession().save(dfs);
		} 
		searchVO.setSelectedDemographicFields(nonPoppableDemographicFieldsFromVO);
		//end save demographic fields


		//start save biospecimen fields
		Collection<BiospecimenField> listOfBiospecimenFieldsFromVO = searchVO.getSelectedBiospecimenFields();
		List<BiospecimenFieldSearch> nonPoppableBiospecimenFS = new ArrayList<BiospecimenFieldSearch>();
		nonPoppableBiospecimenFS.addAll(search.getBiospecimenFieldsToReturn());
		List<BiospecimenField> nonPoppableBiospecimenFieldsFromVO = new ArrayList<BiospecimenField>();
		nonPoppableBiospecimenFieldsFromVO.addAll(listOfBiospecimenFieldsFromVO);
		for (BiospecimenFieldSearch bfs : nonPoppableBiospecimenFS) {
			boolean toBeDeleted = true; // if we find no match along the way, conclude that it has been deleted.

			for (BiospecimenField field : nonPoppableBiospecimenFieldsFromVO) {
				if (bfs.getBiospecimenField().getId().equals(field.getId())) {
					toBeDeleted = false;
					listOfBiospecimenFieldsFromVO.remove(field);// we found it, therefore  remove it  from the list that will ultimately be added as DFS's
				}
			}
			if (toBeDeleted) {
				search.getBiospecimenFieldsToReturn().remove(bfs);
				getSession().update(search);
				getSession().delete(bfs);
				getSession().flush();
				getSession().refresh(search);
			}
		}

		for (BiospecimenField field : listOfBiospecimenFieldsFromVO) {
			BiospecimenFieldSearch bfs = new BiospecimenFieldSearch(field, search);
			getSession().save(bfs);
		}
		searchVO.setSelectedBiospecimenFields(nonPoppableBiospecimenFieldsFromVO);
//end save biospecimen fields
		
//start save biocollection fields
		Collection<BiocollectionField> listOfBiocollectionFieldsFromVO = searchVO.getSelectedBiocollectionFields();
		List<BiocollectionFieldSearch> nonPoppableBiocollectionFS = new ArrayList<BiocollectionFieldSearch>();
		nonPoppableBiocollectionFS.addAll(search.getBiocollectionFieldsToReturn());
		List<BiocollectionField> nonPoppableBiocollectionFieldsFromVO = new ArrayList<BiocollectionField>();
		nonPoppableBiocollectionFieldsFromVO.addAll(listOfBiocollectionFieldsFromVO);
		for (BiocollectionFieldSearch bfs : nonPoppableBiocollectionFS) {
			boolean toBeDeleted = true; // if we find no match along the way, conclude that it has been deleted.

			for (BiocollectionField field : nonPoppableBiocollectionFieldsFromVO) {
				if (bfs.getBiocollectionField().getId().equals(field.getId())) {
					toBeDeleted = false;
					listOfBiocollectionFieldsFromVO.remove(field);// we found it, therefore  remove it  from the list that will ultimately be added as DFS's
				}
			}
			if (toBeDeleted) {
				search.getBiocollectionFieldsToReturn().remove(bfs);
				getSession().update(search);
				getSession().delete(bfs);
				getSession().flush();
				getSession().refresh(search);
			}
		}

		for (BiocollectionField field : listOfBiocollectionFieldsFromVO) {
			BiocollectionFieldSearch bfs = new BiocollectionFieldSearch(field, search);
			getSession().save(bfs);
		}
		searchVO.setSelectedBiocollectionFields(nonPoppableBiocollectionFieldsFromVO);
		//end save biocollection fields

		//start saving all custom display fields		
		Collection<CustomFieldDisplay> listOfSubjectCustomFieldDisplaysFromVO = searchVO.getSelectedSubjectCustomFieldDisplays();
		Collection<CustomFieldDisplay> listOfBiospecimenCustomFieldDisplaysFromVO = searchVO.getSelectedBiospecimenCustomFieldDisplays();
		Collection<CustomFieldDisplay> listOfBiocollectionCustomFieldDisplaysFromVO = searchVO.getSelectedBiocollectionCustomFieldDisplays();// we really can add them all here and add to one collections
		List<CustomFieldDisplaySearch> nonPoppablePhenoCFDs = new ArrayList<CustomFieldDisplaySearch>();
		nonPoppablePhenoCFDs.addAll(search.getCustomFieldsToReturn());
		List<CustomFieldDisplay> nonPoppableCustomFieldsFromVO = new ArrayList<CustomFieldDisplay>();
		//nonPoppableCustomFieldsFromVO.addAll(listOfPhenoCustomFieldDisplaysFromVO);
		nonPoppableCustomFieldsFromVO.addAll(listOfSubjectCustomFieldDisplaysFromVO);
		nonPoppableCustomFieldsFromVO.addAll(listOfBiospecimenCustomFieldDisplaysFromVO);
		nonPoppableCustomFieldsFromVO.addAll(listOfBiocollectionCustomFieldDisplaysFromVO);
		List<CustomFieldDisplay> poppableCustomFieldsFromVO = new ArrayList<CustomFieldDisplay>();
		//poppableCustomFieldsFromVO.addAll(listOfPhenoCustomFieldDisplaysFromVO);
		poppableCustomFieldsFromVO.addAll(listOfSubjectCustomFieldDisplaysFromVO);
		poppableCustomFieldsFromVO.addAll(listOfBiospecimenCustomFieldDisplaysFromVO);
		poppableCustomFieldsFromVO.addAll(listOfBiocollectionCustomFieldDisplaysFromVO);

		for (CustomFieldDisplaySearch cfds : nonPoppablePhenoCFDs) {
			log.info("fields to return=" + search.getCustomFieldsToReturn().size());
			boolean toBeDeleted = true; // if we find no match along the way, conclude that it has been deleted.

			for (CustomFieldDisplay field : nonPoppableCustomFieldsFromVO) {
				if (cfds.getCustomFieldDisplay().getId().equals(field.getId())) {
					toBeDeleted = false;
					poppableCustomFieldsFromVO.remove(field);// we found it, therefore remove it from the list that will ultimately be added as DFS's
				}
			}
			if (toBeDeleted) {
				search.getCustomFieldsToReturn().remove(cfds);
				getSession().update(search);
				getSession().delete(cfds);
				getSession().flush();
				getSession().refresh(search);
			}
		}

		for (CustomFieldDisplay field : poppableCustomFieldsFromVO) { // listOfPhenoCustomFieldDisplaysFromVO){
			CustomFieldDisplaySearch cfds = new CustomFieldDisplaySearch(field, search);
			getSession().save(cfds);
		}

		//Pheno DataSet Fields:
		Collection<PhenoDataSetFieldDisplay> listOfPhenoDataSetFieldDisplaysFromVO = searchVO.getSelectedPhenoDataSetFieldDisplays();
		List<PhenoDataSetFieldDisplaySearch> nonPoppablePhenoDataSetFieldDisplaySearch = new ArrayList<>();
		nonPoppablePhenoDataSetFieldDisplaySearch.addAll(search.getPhenoDataSetFieldsToReturn());
		List<PhenoDataSetFieldDisplay> nonPoppablePhenoDataSetFieldDisplays = new ArrayList<>();
		nonPoppablePhenoDataSetFieldDisplays.addAll(listOfPhenoDataSetFieldDisplaysFromVO);
		List<PhenoDataSetFieldDisplay> poppablePhenoDataSetFieldDisplays = new ArrayList<>();
		poppablePhenoDataSetFieldDisplays.addAll(listOfPhenoDataSetFieldDisplaysFromVO);

		for (PhenoDataSetFieldDisplaySearch phenoSearch : nonPoppablePhenoDataSetFieldDisplaySearch) {
			log.info("pheno fields to return = " + search.getPhenoDataSetFieldsToReturn().size());
			boolean toBeDeleted = true;
			for (PhenoDataSetFieldDisplay field : nonPoppablePhenoDataSetFieldDisplays) {
				if (phenoSearch.getPhenoDataSetFieldDisplay().getId().equals(field.getId())) {
					toBeDeleted = false;
					poppablePhenoDataSetFieldDisplays.remove(field);
				}
			}

			if (toBeDeleted) {
				search.getPhenoDataSetFieldsToReturn().remove(phenoSearch);
				getSession().update(search);
				getSession().delete(phenoSearch);
				getSession().flush();
				getSession().refresh(search);
			}
		}

		for (PhenoDataSetFieldDisplay field : poppablePhenoDataSetFieldDisplays) {
			PhenoDataSetFieldDisplaySearch phenoSearch = new PhenoDataSetFieldDisplaySearch(field, search);
			getSession().save(phenoSearch);
		}

		// is all of this necessary now...investigate// searchVO.setSelectedPhenoCustomFieldDisplays(nonPoppableCustomFieldsFromVO);
		//end save all custom field displays

		Collection<ConsentStatusField> listOfConsentStatusFieldsFromVO = searchVO.getSelectedConsentStatusFields();
		List<ConsentStatusFieldSearch> nonPoppableConsentStatusFieldSearch = new ArrayList<ConsentStatusFieldSearch>();
		nonPoppableConsentStatusFieldSearch.addAll(search.getConsentStatusFieldsToReturn());
		List<ConsentStatusField> nonPoppableConsentStatusFieldsFromVO = new ArrayList<ConsentStatusField>();
		nonPoppableConsentStatusFieldsFromVO.addAll(listOfConsentStatusFieldsFromVO);
		for(ConsentStatusFieldSearch csfs : nonPoppableConsentStatusFieldSearch) {
			boolean toBeDeleted = true;
			
			for(ConsentStatusField field : nonPoppableConsentStatusFieldsFromVO) {
				log.info("consentstfld = " + field.toString() + " " + field.getPublicFieldName());
				log.info("csfs.getid. == field.getid: " + csfs.getConsentStatusField().getId().equals(field.getId()));
				if(csfs.getConsentStatusField().getId().equals(field.getId())) {
					toBeDeleted = false;
					listOfConsentStatusFieldsFromVO.remove(field);
				}
			}
			if(toBeDeleted) {
				search.getConsentStatusFieldsToReturn().remove(csfs);
				getSession().update(search);
				getSession().delete(csfs);
				getSession().flush();
				getSession().refresh(search);
			}
		}
		
		for(ConsentStatusField field : listOfConsentStatusFieldsFromVO) { 
			ConsentStatusFieldSearch csfs = new ConsentStatusFieldSearch(field, search);
			getSession().save(csfs);
		}
		searchVO.setSelectedConsentStatusFields(getSelectedConsentStatusFieldsForSearch(search));
	}

	/**
	 * 
	 * @param searchName
	 * @param anIdToExcludeFromResults
	 *           : This is if you want to exclude id, the most obvious case being where we want to exclude the current search itself in the case of an
	 *           update
	 * @return
	 */
	public boolean isSearchNameTaken(String searchName, Study study, Long anIdToExcludeFromResults) {
		Criteria criteria = getSession().createCriteria(Search.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("name", searchName));

		if (anIdToExcludeFromResults != null) {
			criteria.add(Restrictions.ne("id", anIdToExcludeFromResults));
		}
		return (criteria.list().size() > 0);
	}

	public Collection<ConsentStatusField> getAllConsentStatusFields() {
		Criteria criteria = getSession().createCriteria(ConsentStatusField.class);
		return criteria.list();
	}
	
	public Collection<DemographicField> getAllDemographicFields() {
		Criteria criteria = getSession().createCriteria(DemographicField.class);
		return criteria.list();
	}

	public Collection<BiospecimenField> getAllBiospecimenFields() {
		Criteria criteria = getSession().createCriteria(BiospecimenField.class);
		return criteria.list();
	}

	public Collection<BiocollectionField> getAllBiocollectionFields() {
		Criteria criteria = getSession().createCriteria(BiocollectionField.class);
		return criteria.list();
	}

	public Collection<ConsentStatusField> getSelectedConsentStatusFieldsForSearch (Search search) {
		String queryString = "select csfs.consentStatusField" + " from ConsentStatusFieldSearch csfs " + " where csfs.search=:search " + " order by csfs.consentStatusField.entity ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		log.info("QueryString: " + query.getQueryString());
		log.info("getselectedconsentstatusfieldforsearch results:");
		for(ConsentStatusField c : (List<ConsentStatusField>) query.list()) {
			log.info(c.getPublicFieldName());
		}
		return query.list();
	}

	public List<ConsentStatusField> getSelectedConsentStatusFieldsForSearch(Search search, Entity entityEnumToRestrictOn) {
		String queryString = "select csfs.consentStatusField from ConsentStatusFieldSearch csfs where csfs.search=:search " + " and csfs.consentStatusField.entity=:entityEnumToRestrictOn ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		query.setParameter("entityEnumToRestrictOn", entityEnumToRestrictOn);
		return query.list();
	}
	
	public List<DemographicField> getSelectedDemographicFieldsForSearch (Search search) {
		String queryString = "select dfs.demographicField " + " from DemographicFieldSearch dfs " + " where dfs.search=:search " + " order by dfs.demographicField.entity ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		return query.list();
	}

	public List<DemographicField> getSelectedDemographicFieldsForSearch(Search search, Entity entityEnumToRestrictOn) {
		String queryString = "select dfs.demographicField from DemographicFieldSearch dfs where dfs.search=:search " + " and dfs.demographicField.entity=:entityEnumToRestrictOn ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		query.setParameter("entityEnumToRestrictOn", entityEnumToRestrictOn);
		return query.list();
	}

	public List<BiospecimenField> getSelectedBiospecimenFieldsForSearch(Search search) {
		String queryString = "select bsfs.biospecimenField from BiospecimenFieldSearch bsfs " + 
				" where bsfs.search=:search ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		return query.list();
	}

	public List<BiocollectionField> getSelectedBiocollectionFieldsForSearch(Search search) {
		String queryString = "select bcfs.biocollectionField " + " from BiocollectionFieldSearch bcfs " + " where bcfs.search=:search ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		return query.list();
	}

	@Deprecated
	public List<CustomFieldDisplay> getSelectedPhenoCustomFieldDisplaysForSearch(Search search) {
		String queryString = "select cfds.customFieldDisplay " + " from CustomFieldDisplaySearch cfds " + " where cfds.search=:search "
				+ " and cfds.customFieldDisplay.customField.arkFunction=:arkFunction ";// +
		// " order by cfds.customFieldDisplay.customFieldGroup.name ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		query.setParameter("arkFunction", getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION));

		return query.list();
	}

	public List<PhenoDataSetFieldDisplay> getSelectedPhenoDataSetFieldDisplaysForSearch(Search search) {
		String queryString = "select pdfds.phenoDataSetFieldDisplay " + " from PhenoDataSetFieldDisplaySearch pdfds " +
				"where pdfds.search=:search " + " and pdfds.phenoDataSetFieldDisplay.phenoDataSetField.arkFunction=:arkFunction";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		query.setParameter("arkFunction", getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY));
		return query.list();
	}

	public List<CustomFieldDisplay> getSelectedSubjectCustomFieldDisplaysForSearch(Search search) {
		String queryString = "select cfds.customFieldDisplay " + " from CustomFieldDisplaySearch cfds " + " where cfds.search=:search "
				+ " and cfds.customFieldDisplay.customField.arkFunction=:arkFunction";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		query.setParameter("arkFunction", getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD));
		return query.list();
	}

	public List<CustomFieldDisplay> getSelectedBiospecimenCustomFieldDisplaysForSearch(Search search) {
		String queryString = "select cfds.customFieldDisplay " + " from CustomFieldDisplaySearch cfds " + " where cfds.search=:search "
				+ " and cfds.customFieldDisplay.customField.arkFunction=:arkFunction";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		query.setParameter("arkFunction", getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN));
		return query.list();
	}

	public List<CustomFieldDisplay> getAllSelectedCustomFieldDisplaysForSearch(Search search) {
		String queryString = "select cfds.customFieldDisplay " + " from CustomFieldDisplaySearch cfds " + " where cfds.search=:search ";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		return query.list();
	}

	public List<CustomFieldDisplay> getSelectedBiocollectionCustomFieldDisplaysForSearch(Search search) {
		String queryString = "select cfds.customFieldDisplay " + " from CustomFieldDisplaySearch cfds " + " where cfds.search=:search "
				+ " and cfds.customFieldDisplay.customField.arkFunction=:arkFunction";
		Query query = getSession().createQuery(queryString);
		query.setParameter("search", search);
		//query.setParameter("arkFunction", getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION));
		query.setParameter("arkFunction", getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD));
		return query.list();
	}

	public void runSearch(Long searchId) {
		runSearch(searchId, null);
	}

	public void runSearch(Long searchId, String currentUser) {
		DataExtractionVO allTheData = new DataExtractionVO();
		Search search = (Search) getSession().get(Search.class, searchId);
		if (search == null) {
			// TODO errors and reports
		}
		else {
			//getMaxAddressesForTheseSubjects(null, null); chris doesn't need this right now
			List<DemographicField> addressDFs = getSelectedDemographicFieldsForSearch(search, Entity.Address);
			List<DemographicField> lssDFs = getSelectedDemographicFieldsForSearch(search, Entity.LinkSubjectStudy);
			List<DemographicField> personDFs = getSelectedDemographicFieldsForSearch(search, Entity.Person);
			List<DemographicField> phoneDFs = getSelectedDemographicFieldsForSearch(search, Entity.Phone);
			List<DemographicField> otherIDDFs = getSelectedDemographicFieldsForSearch(search, Entity.OtherID);
			//added on 2015-11-03 include the twin subject uid and the twin type.
			List<DemographicField> twinDetailDFs = getSelectedDemographicFieldsForSearch(search, Entity.LinkSubjectTwin);
			
			List<DemographicField> allSubjectFields = new ArrayList<DemographicField>();
			allSubjectFields.addAll(addressDFs);
			allSubjectFields.addAll(lssDFs);
			allSubjectFields.addAll(personDFs);
			allSubjectFields.addAll(phoneDFs);
			allSubjectFields.addAll(twinDetailDFs);

			List<BiospecimenField> bsfs = getSelectedBiospecimenFieldsForSearch(search);
			List<BiocollectionField> bcfs = getSelectedBiocollectionFieldsForSearch(search);
			List<CustomFieldDisplay> bccfds = getSelectedBiocollectionCustomFieldDisplaysForSearch(search);
			List<CustomFieldDisplay> bscfds = getSelectedBiospecimenCustomFieldDisplaysForSearch(search);
			List<CustomFieldDisplay> scfds = getSelectedSubjectCustomFieldDisplaysForSearch(search);
			List<PhenoDataSetFieldDisplay> pfds = getSelectedPhenoDataSetFieldDisplaysForSearch(search);
			
			List<ConsentStatusField> consentStatus = (List<ConsentStatusField>) getSelectedConsentStatusFieldsForSearch(search);
			
			/* Making this stuff into an xml document THEN converting it generically to xls/csv/pdf/etc might be an option
			 * other options;
			 *  1 get each of these and apply a filter every time 
			 *  2 a megaquery to get EVERYTHING FOR EVERYONE into our "report/value object/model" 
			 *  3 use the filters to create a set of subjectUIDs and maybe apply that, though may also needs a set of pheno_data_id, subj_custom_ids, etc
			 */

			//DEMOGRAPHIC FILTERING - but not data
			List<Long> idsAfterFiltering = applyDemographicFilters(search);  //from here we need to add 
			log.info("uids afterFilteringdemo=" + idsAfterFiltering.size());
			
			//CONSENT STATUS FILTERING - still being worked on not complete...but doesn't break anything
//			List<Long> consentAfterFiltering = 
			if(!idsAfterFiltering.isEmpty()){
				idsAfterFiltering = applyConsentStatusFilters(allTheData, search, idsAfterFiltering);
			}
			log.info("consent idsAfterFiltering=" + idsAfterFiltering.size());

			//BIOCOLLECTION
			List<Long> bioCollectionIdsAfterFiltering = new ArrayList<Long>();
			if(!idsAfterFiltering.isEmpty()){
				bioCollectionIdsAfterFiltering = addDataFromMegaBiocollectionQuery(allTheData, bcfs, bccfds, search, idsAfterFiltering, bioCollectionIdsAfterFiltering);
			}
			log.info("uidsafterFiltering doing the construction of megaobject=" + idsAfterFiltering.size());
			log.info("uidsafterFiltering biocol=" + idsAfterFiltering.size());

			//BIOCOL CUSTOM
			if(!idsAfterFiltering.isEmpty()){
				idsAfterFiltering = applyBioCollectionCustomFilters(allTheData, search, idsAfterFiltering, bioCollectionIdsAfterFiltering);	//change will be applied to referenced object
			}
			log.info("uidsafterFiltering biocol cust=" + idsAfterFiltering.size());

			//BIOSPECIMEN
			List<Long> biospecimenIdsAfterFiltering = new ArrayList<Long>();

			if(!idsAfterFiltering.isEmpty()){
				biospecimenIdsAfterFiltering = addDataFromMegaBiospecimenQuery(allTheData, bsfs, search, idsAfterFiltering, biospecimenIdsAfterFiltering, bioCollectionIdsAfterFiltering);
			}
			log.info("biospecimenIdsAfterFiltering size: " + biospecimenIdsAfterFiltering.size());
			log.info("uidsafterFilteringbiospec=" + idsAfterFiltering.size());

			//BIOSPEC CUSTOM
			if(!idsAfterFiltering.isEmpty()){
				idsAfterFiltering = applyBiospecimenCustomFilters(allTheData, search, idsAfterFiltering, biospecimenIdsAfterFiltering);	//change will be applied to referenced object
			}
			log.info("uidsafterFiltering=Biospec cust" + idsAfterFiltering.size() + "biospecimenIdsAfterFiltering custom size: " + biospecimenIdsAfterFiltering.size());

			//PHENO CUSTOM

			if(!idsAfterFiltering.isEmpty()){
				idsAfterFiltering = applyPhenoDataSetFilters(allTheData, search, idsAfterFiltering);	//change will be applied to referenced object
			}
			log.info("uidsafterFiltering pheno cust=" + idsAfterFiltering.size());

			//DEMOGRAPHIC DATA
			idsAfterFiltering = applySubjectCustomFilters(allTheData, search, idsAfterFiltering);	//change will be applied to referenced object

			wipeBiospecimenDataNotMatchingThisList(search.getStudy(), allTheData, biospecimenIdsAfterFiltering, bioCollectionIdsAfterFiltering, idsAfterFiltering);
			wipeBiocollectionDataNotMatchThisList(search.getStudy(), allTheData, bioCollectionIdsAfterFiltering, idsAfterFiltering, biospecimenIdsAfterFiltering,  getBiospecimenQueryFilters(search));

			//2015-11-03 include the Twin subject uid with the twin type after the otherIDFs
			addDataFromMegaDemographicQuery(allTheData, personDFs, lssDFs, addressDFs, phoneDFs, otherIDDFs, twinDetailDFs,scfds, search, idsAfterFiltering);//This must go last, as the number of joining tables is going to affect performance

			log.info("uidsafterFiltering SUBJECT cust=" + idsAfterFiltering.size());

			Map<Long, Long> maxInputList = new HashMap<Long, Long>();//pass the index and do a max comparison to minimize a simple grid which will be too bulky
			Map<Long, Long> maxOutputList = new HashMap<Long, Long>();
			Long maxProcessesPerPipeline = new Long(0L);
			if(search.getIncludeGeno()){
				maxProcessesPerPipeline = addGenoData(allTheData, search, idsAfterFiltering, maxInputList, maxOutputList, maxProcessesPerPipeline);//TODO: test
			}
			prettyLoggingOfWhatIsInOurMegaObject(allTheData.getDemographicData(), FieldCategory.DEMOGRAPHIC_FIELD);
			prettyLoggingOfWhatIsInOurMegaObject(allTheData.getSubjectCustomData(), FieldCategory.SUBJECT_CFD);
			prettyLoggingOfWhatIsInOurMegaObject(allTheData.getBiospecimenData(), FieldCategory.BIOSPECIMEN_FIELD);
			prettyLoggingOfWhatIsInOurMegaObject(allTheData.getBiospecimenData(), FieldCategory.BIOCOLLECTION_FIELD);
			// prettyLoggingOfWhatIsInOurMegaObject(allTheData.getConsentStatusData(), FieldCategory.CONSENT_STATUS_FIELD);

			// CREATE CSVs - later will offer options xml, pdf, etc
			SearchResult searchResult = new SearchResult();
			searchResult.setSearch(search);
			Criteria criteria = getSession().createCriteria(SearchResult.class);
			criteria.add(Restrictions.eq("search", search));
			List<SearchResult> searchResults = criteria.list();
			for (SearchResult sr : searchResults) {
				deleteSearchResult(sr);
			}

			createSearchResult(search, iDataExtractionDao.createSubjectDemographicCSV(search, allTheData, allSubjectFields, scfds, FieldCategory.DEMOGRAPHIC_FIELD), currentUser);
			createSearchResult(search, iDataExtractionDao.createBiocollectionCSV(search, allTheData, bccfds, FieldCategory.BIOCOLLECTION_FIELD), currentUser);
			createSearchResult(search, iDataExtractionDao.createBiospecimenCSV(search, allTheData, bsfs, bscfds, FieldCategory.BIOSPECIMEN_FIELD), currentUser);
			createSearchResult(search, iDataExtractionDao.createPhenotypicCSV(search, allTheData, pfds, FieldCategory.PHENO_FD),currentUser);
			if(search.getIncludeGeno()) {
				createSearchResult(search, iDataExtractionDao.createGenoCSV(search, allTheData, FieldCategory.GENO, maxProcessesPerPipeline, maxInputList, maxOutputList),currentUser);				
			}
			createSearchResult(search, iDataExtractionDao.createConsentStatusCSV(search, allTheData, consentStatus, FieldCategory.CONSENT_STATUS_FIELD), currentUser);
			createSearchResult(search, iDataExtractionDao.createMegaCSV(search, allTheData, allSubjectFields, bccfds, bscfds, pfds, consentStatus), currentUser);
			try {
				search.setFinishTime(new java.util.Date(System.currentTimeMillis()));
				search.setStatus("FINISHED");
				update(search);
			}
			catch (EntityExistsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//TODO don't catch exceptions without doing something - and for that matter we should really start having statuses on our extractions so people can know what happened
				log.error("Error while updating search with finish time.");
			}
		}
	}

	private Collection<String> getBiospecimenUIDsNotMatchingTheseBiospecimenIdsOrSubjectIds(Study study, Collection<String> biospecimenUIDs, List<Long> biospecimenIds, List<Long> biocollectionIds, List<Long> subjectIds){

		Query query = null;
		//if there is nothing to start with get out of here.
		if(biospecimenUIDs.isEmpty()){
			return new ArrayList<String>();
		}


		//if there is nothing to reduce the list by...return original list.
		if(biospecimenIds.isEmpty() || subjectIds.isEmpty() || biocollectionIds.isEmpty()){
			return biospecimenUIDs;
		}
		else{
			String queryString = 	" select distinct biospecimen.biospecimenUid " +
									" from Biospecimen biospecimen " +
									" where " +
									" ( " +
										(biospecimenIds.isEmpty()?"":" biospecimen.id not in (:biospecidList) or ") +
										(biocollectionIds.isEmpty()?"":" biospecimen.bioCollection.id not in (:biocollectionIds) or ") +
										(subjectIds.isEmpty()?"":" biospecimen.linkSubjectStudy.id not in (:subjectIdList)  ") +
									" ) " +
									" and biospecimen.biospecimenUid in (:uidList) " +
									" and biospecimen.study =:study ";
			query = getSession().createQuery(queryString);
			if(!biospecimenIds.isEmpty())
				query.setParameterList("biospecidList", biospecimenIds);
			if(!biocollectionIds.isEmpty())
				query.setParameterList("biocollectionIds", biocollectionIds);
			if(!subjectIds.isEmpty())
				query.setParameterList("subjectIdList", subjectIds);
			query.setParameter("study", study);
			query.setParameterList("uidList", biospecimenUIDs);
			return query.list();
		}		
	}

	/*allthedata might not b as good as just the bit we want */
	/**This method modified on 2016-05-25.                                                                                                                                                                                                                                                                    
	Due to "java.util.ConcurrentModificationException".                                                                                                                                                                                                                                                    
	The rule is "You may not modify (add or remove elements from the list) while iterating over it using an Iterator (which happens when you use a for-each loop)".                                                                                                                                       
	JavaDocs:                                                                                                                                                                                                                                                                                             
	The iterators returned by this class's iterator and listIterator methods are fail-fast: if the list is structurally modified at any time after the iterator is created, in any way except through the iterator's own remove or add methods, the iterator will throw a ConcurrentModificationException.
	Hence if you want to modify the list (or any collection in general), use iterator, because then it is aware of the modifications and hence those will be handled properly.                                                                                                                            
	@param study                                                                                                                                                                                                                                                                                           
	@param allTheData                                                                                                                                                                                                                                                                                      
	@param biospecimenIdsAfterFiltering                                                                                                                                                                                                                                                                    
	@param biocollectionIds                                                                                                                                                                                                                                                                                
	@param idsAfterFiltering                                                                                                                                                                                                                                                                               
	 */
	private void wipeBiospecimenDataNotMatchingThisList(Study study, DataExtractionVO allTheData, List<Long> biospecimenIdsAfterFiltering, List<Long> biocollectionIds,
			List<Long> idsAfterFiltering) {
		HashMap<String, ExtractionVO> data = allTheData.getBiospecimenData();
		Collection<String> uidsInData = data.keySet();
		Collection<String> uidsToDelete = getBiospecimenUIDsNotMatchingTheseBiospecimenIdsOrSubjectIds(study, uidsInData, biospecimenIdsAfterFiltering, biocollectionIds, idsAfterFiltering);
		Collection<String> uidsToDeleteCopy =new ArrayList<String>(uidsToDelete);
		//This is an exact copy of the array so  ConcurrentModificationException can not be thrown.
		for(String uid : uidsToDeleteCopy){
			log.info("wipeBiospecimenDataNotMatchingThisList:    removed biospecimen uid = " + uid);
			data.remove(uid);
		}
		
	}

	private void wipeBiocollectionDataNotMatchThisList(Study study, DataExtractionVO allTheData, List<Long> bioCollectionIdsAfterFiltering, List<Long> subjectIds, List<Long> biospecimenIds,
			List<QueryFilter> biospecimenQueryFilters) {

		HashMap<String, ExtractionVO> data = allTheData.getBiocollectionData();
		HashMap<String, ExtractionVO> customData = allTheData.getBiocollectionCustomData();
		Collection<String> uidsInData = new HashSet(data.keySet());
		Collection<String> uidsToDelete = (Collection<String>) new HashSet();
		uidsToDelete =		getBioCollectionUIDsNotMatchingTheseBioCollectionIdsOrSubjectIds(study, uidsInData, bioCollectionIdsAfterFiltering, subjectIds, biospecimenIds, biospecimenQueryFilters);
		for(String uid : uidsToDelete){
			log.info("wipeBioCollectionDataNotMatchingThisList:    removed bioCollection uid = " + uid);
			data.remove(uid);
			customData.remove(uid);
		}
		log.info("what is left in data?" + data);

	}

	private Collection<String> getBioCollectionUIDsNotMatchingTheseBioCollectionIdsOrSubjectIds(Study study, Collection<String> bioCollectionUIDs, List<Long> bioCollectionIds,
			List<Long> subjectIds, List<Long> biospecimenIds, List<QueryFilter> biospecimenFilters){

		Query query = null;
		//		Query query2 = null;
		//if there is nothing to start with get out of here.
		if(bioCollectionUIDs.isEmpty()){
			return new ArrayList<String>();
		}
		
		//if there is nothing to reduce the list by...return original list.
		if((bioCollectionIds.isEmpty() && subjectIds.isEmpty())){
			return bioCollectionUIDs;
		}
		else{
			List<Long> subjectIdsNew=new ArrayList<Long>();
			//add a dummy value=0 to get rid of ".QuerySyntaxException: unexpected end of subtree" due to empty list.
			subjectIds.add(new Long(0));
			String queryString = 	" select distinct bioCollection.biocollectionUid " +
									" from BioCollection bioCollection " +
									" where (" +
									" bioCollection.id not in (:idList) or " +
									" bioCollection.linkSubjectStudy.id not in (:subjectIdList) ) and " +
									" bioCollection.biocollectionUid in (:uidList) " +
									" and bioCollection.study =:study ";
			query = getSession().createQuery(queryString);
			if(!bioCollectionIds.isEmpty())
				query.setParameterList("idList", bioCollectionIds);
			if(!subjectIds.isEmpty())
				query.setParameterList("subjectIdList", subjectIds);
			else{
				query.setParameterList("subjectIdList", subjectIdsNew);
			}
			query.setParameter("study", study);
			query.setParameterList("uidList", bioCollectionUIDs);
			log.info("Query String: "+query.getQueryString());
			List<String> collectionsToDelete = query.list();


			if(biospecimenIds.isEmpty()){
				//if all biospecimens WERE filtered, then all biocollections deleted
				if(!biospecimenFilters.isEmpty()){
					return bioCollectionUIDs;
				}
				else{
					//there were no biospec filters...continue as usual
					return collectionsToDelete;
				}
			}
			else{
				if(!bioCollectionUIDs.isEmpty() && !subjectIds.isEmpty()){

					if(!biospecimenFilters.isEmpty()){
						List<String> biocollectionsCorrespondingOurFilteredBiospecimens = getBiocollectionUIDsForTheseBiospecimens(biospecimenIds, collectionsToDelete, study);
						for(String biocollectionUid : bioCollectionUIDs){
							if(!biocollectionsCorrespondingOurFilteredBiospecimens.contains(biocollectionUid)){
								collectionsToDelete.add(biocollectionUid);
							}
						}
					}
				}
			}
			return collectionsToDelete;
		}		

	}

	/**
	 * 
	 * @param biospecimenIds 
	 * @param collectionsToExclude DO NOT RETURN ANY OF THESE
	 * @return
	 */
	private List<String> getBiocollectionUIDsForTheseBiospecimens(
			List<Long> biospecimenIds, List<String> collectionsToExclude, Study study) {
		if(biospecimenIds == null){
			return new ArrayList<String>();
		}
		else{
			Query query2 = null;
			String queryString2 = "Select distinct biospecimen.bioCollection.biocollectionUid  " +
					" from  Biospecimen biospecimen " +
					" where " +
					" biospecimen.id in (:biospecimenIds)  " + 
					"  and biospecimen.study =:study  ";
			query2 = getSession().createQuery(queryString2);
			query2.setParameterList("biospecimenIds", biospecimenIds);
			query2.setParameter("study", study); 

			return query2.list();
		}

	}

	private List<Long> applyDemographicFilters(Search search){
		List subjectUIDs = new ArrayList<Long>();


		String addressJoinFilters = getAddressFilters(search);

		String personFilters = getPersonFilters(search, "");
		String lssAndPersonFilters = getLSSFilters(search, personFilters);
		
		String otherIDFilters = getOtherIDFilters(search);
		
		List<Long> subjectList = getSubjectIdsforSearch(search);

		/**
		 * note that this is ID not subject_uid being selected
		 */
		String queryString = "select distinct lss.id from LinkSubjectStudy lss " 
				+ addressJoinFilters
				+ otherIDFilters
				//TODO also add filters for phone and address 
				+ " where lss.study.id = " + search.getStudy().getId()
//TODO once we have confidence in entire methodology of including sub studies 				+ (search.getStudy().getParentStudy()==null?"":(" or lss.study.id = " + search.getStudy().getParentStudy() ) ) 
				+ lssAndPersonFilters
				+ " and lss.subjectStatus.name != 'Archive'";
				
		Query query = null;
		if(subjectList.isEmpty()){
			query = getSession().createQuery(queryString);
		}
		else{
			queryString = queryString + " and lss.id in (:subjectIdList) ";
			query = getSession().createQuery(queryString);
			query.setParameterList("subjectIdList", subjectList);
		}		

		subjectUIDs = query.list();
		log.info("size=" + subjectUIDs.size());

		return subjectUIDs;
	}
	
	private String getConsentFilterFieldName(QueryFilter qf) {
		String filterName = qf.getConsentStatusField().getFieldName();
		if(qf.getConsentStatusField().getEntity().name().equalsIgnoreCase("StudyComp")){
			return "csc."+filterName;
		} else if(filterName.equalsIgnoreCase("studyComponentStatus")) {
			return "cscs.name";
		} else if(filterName.equalsIgnoreCase("studyComp")) {
			return "csc.name";
		} else {
			return "c."+filterName;
		}
	}

	private List<Long> applyConsentStatusFilters(DataExtractionVO allTheData, Search search, List<Long> idsToInclude) {
		
		//for(Long l : idsToInclude) {
		//	log.info("including: " + l);
		//}
		boolean hasConsentFilters = false;
		if(search.getQueryFilters().isEmpty()) {
			return idsToInclude;
		}else{
			for(QueryFilter filter : search.getQueryFilters()){
				if(filter.getConsentStatusField() != null){
					hasConsentFilters = true;
				}
			}
		}
		Criteria filter = getSession().createCriteria(Consent.class, "c");
		filter.add(Restrictions.eq("c.study.id", search.getStudy().getId()));
		filter.createAlias("c.linkSubjectStudy", "lss");
		if(!idsToInclude.isEmpty()) {
			filter.add(Restrictions.in("lss.id", idsToInclude));
		}
		filter.createAlias("c.studyComponentStatus", "cscs");
		filter.createAlias("c.studyComp", "csc");
		
		if(!hasConsentFilters){
		
			for(QueryFilter qf : search.getQueryFilters()) {
				if(qf.getConsentStatusField() != null) {
					switch(qf.getOperator()) {
						case EQUAL:
							filter.add(Restrictions.eq(getConsentFilterFieldName(qf), qf.getValue()));
							break;
						case BETWEEN:
							filter.add(Restrictions.between(getConsentFilterFieldName(qf), qf.getValue(), qf.getSecondValue()));
							break;
						case GREATER_THAN:
							filter.add(Restrictions.gt(getConsentFilterFieldName(qf), qf.getValue()));
							break;
						case GREATER_THAN_OR_EQUAL:
							filter.add(Restrictions.ge(getConsentFilterFieldName(qf), qf.getValue()));
							break;
						case IS_EMPTY:
							filter.add(Restrictions.isEmpty(getConsentFilterFieldName(qf)));
							break;
						case IS_NOT_EMPTY:
							filter.add(Restrictions.isNotEmpty(getConsentFilterFieldName(qf)));
							break;
						case LESS_THAN:
							filter.add(Restrictions.lt(getConsentFilterFieldName(qf), qf.getValue()));
							break;
						case LESS_THAN_OR_EQUAL:
							filter.add(Restrictions.le(getConsentFilterFieldName(qf), qf.getValue()));
							break;
						case LIKE:
							filter.add(Restrictions.like(getConsentFilterFieldName(qf), qf.getValue(), MatchMode.ANYWHERE));
							break;
						case NOT_EQUAL:
							filter.add(Restrictions.ne(getConsentFilterFieldName(qf), qf.getValue()));
							break;
						default:
							break;
					}
				}
			}
		}
		filter.setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("lss.id"))));
		
		List<Long> consentStatusIDs = filter.list();

		Collection<Consent> csData = Collections.EMPTY_LIST;
		
		if(!consentStatusIDs.isEmpty()){
			Criteria consentData = getSession().createCriteria(Consent.class, "c");
			consentData.add(Restrictions.eq("c.study.id", search.getStudy().getId()));
			consentData.createAlias("c.linkSubjectStudy", "lss");
			consentData.add(Restrictions.in("lss.id", consentStatusIDs));
			csData = consentData.list();
		}
		
		HashMap<String, ExtractionVO> hashOfConsentStatusData = allTheData.getConsentStatusData();

		ExtractionVO valuesForThisLss = new ExtractionVO();
		HashMap<String, String> map = null;
		LinkSubjectStudy previousLss = null;
		int count = 0;
		//will try to order our results and can therefore just compare to last LSS and either add to or create new Extraction VO
		for (Consent data : csData) {
			if(previousLss==null){
				map = new HashMap<String, String>();
				previousLss = data.getLinkSubjectStudy();
				count = 0;
			}
			else if(data.getLinkSubjectStudy().getId().equals(previousLss.getId())){
				//then just put the data in
				count++;
			}
			else{	//if its a new LSS finalize previous map, etc
				valuesForThisLss.setKeyValues(map);
				valuesForThisLss.setSubjectUid(previousLss.getSubjectUID());
				hashOfConsentStatusData.put(previousLss.getSubjectUID(), valuesForThisLss);	
				previousLss = data.getLinkSubjectStudy();
				map = new HashMap<String, String>();//reset
				valuesForThisLss = new ExtractionVO();
				count = 0;
			}
			if(data.getStudyComp().getName() != null) {
				map.put(count + "_Study Component Name", data.getStudyComp().getName());
			}
			if(data.getStudyComponentStatus() != null) {
				map.put(count + "_Study Component Status", data.getStudyComponentStatus().getName());
			}
			if(data.getConsentDate() != null) {
				map.put(count + "_Consent Date", data.getConsentDate().toString());
			}
			if(data.getConsentedBy() != null) {
				map.put(count + "_Consented By", data.getConsentedBy());				
			}
		}

		//finalize the last entered key value sets/extraction VOs
		if(map!=null && previousLss!=null){
			valuesForThisLss.setKeyValues(map);
			valuesForThisLss.setSubjectUid(previousLss.getSubjectUID());
			hashOfConsentStatusData.put(previousLss.getSubjectUID(), valuesForThisLss);
		}

		//can probably now go ahead and add these to the dataVO...even though inevitable further filters may further axe this list or parts of it.
		allTheData.setConsentStatusData(hashOfConsentStatusData);
		if(hasConsentFilters){
			return consentStatusIDs;
		}
		else{
			return idsToInclude;
		}
	}

	/**
	 * @param allTheData - reference to the object containing our data collected so far, this is to be updated as we continue our refinement.
	 * @param search 
	 * @param idsToInclude - the constantly refined list of ID's passed from the previous extraction step
	 * 
	 * @return the updated list of uids that are still left after the filtering.
	 */
	private List<Long> applySubjectCustomFilters(DataExtractionVO allTheData, Search search, List<Long> idsToInclude){
		
		 
		if(idsToInclude!=null && !idsToInclude.isEmpty()){
			String queryToFilterSubjectIDs = getSubjectCustomFieldQuery(search);
			Collection<CustomFieldDisplay> cfdsToReturn = getSelectedSubjectCustomFieldDisplaysForSearch(search);

			log.info("about to APPLY subjectcustom filters.  UIDs size =" + idsToInclude.size() + " query string = " + queryToFilterSubjectIDs + " cfd to return size = " + cfdsToReturn.size());
			if(!queryToFilterSubjectIDs.isEmpty()){
				Query query = getSession().createQuery(queryToFilterSubjectIDs);
				query.setParameterList("idList", idsToInclude);
				List<Long> returnedSubjectIds =query.list(); 
				idsToInclude.clear();
				for (Long id : returnedSubjectIds) {
					idsToInclude.add(id);
				}
				log.info("rows returned = " + idsToInclude.size());
			}
			else{
				log.info("there were no subject custom data filters, therefore don't run filter query");
			}
		}
		else{
			log.info("there are no id's to filter.  therefore won't run filtering query");
		}

		Collection<CustomFieldDisplay> customFieldToGet = getSelectedSubjectCustomFieldDisplaysForSearch(search);

		/* We have the list of subjects, and therefore the list of subjectcustomdata - now bring back all the custom data rows IF they have any data they need */
		if(idsToInclude!=null && !idsToInclude.isEmpty() && !customFieldToGet.isEmpty()){
			String queryString = "select data from SubjectCustomFieldData data  " +
					" left join fetch data.linkSubjectStudy "  +
					" left join fetch data.customFieldDisplay custFieldDisplay "  +
					" left join fetch custFieldDisplay.customField custField "  +
					" where data.linkSubjectStudy.id in (:idList) " +
					" and data.customFieldDisplay in (:customFieldsList)" + 
					" order by data.linkSubjectStudy " ;
			Query query2 = getSession().createQuery(queryString);
			query2.setParameterList("idList", idsToInclude);
			query2.setParameterList("customFieldsList", customFieldToGet);

			List<SubjectCustomFieldData> scfData = query2.list();
			HashMap<String, ExtractionVO> hashOfSubjectsWithTheirSubjectCustomData = allTheData.getSubjectCustomData();

			ExtractionVO valuesForThisLss = new ExtractionVO();
			HashMap<String, String> map = null;
			LinkSubjectStudy previousLss = null;
			//will try to order our results and can therefore just compare to last LSS and either add to or create new Extraction VO
			for (SubjectCustomFieldData data : scfData) {
				/*				log.info("\t\tprev='" + ((previousLss==null)?"null":previousLss.getSubjectUID()) + "\tsub='" + data.getLinkSubjectStudy().getSubjectUID() + "\terr=" + data.getErrorDataValue() + "\tdate=" + data.getDateDataValue() + "\tnum=" + data.getNumberDataValue() + "\tstr=" + data.getTextDataValue() );*/

				if(previousLss==null){
					map = new HashMap<String, String>();
					previousLss = data.getLinkSubjectStudy();
				}
				else if(data.getLinkSubjectStudy().getId().equals(previousLss.getId())){
					//then just put the data in
				}
				else{	//if its a new LSS finalize previous map, etc
					valuesForThisLss.setKeyValues(map);
					valuesForThisLss.setSubjectUid(previousLss.getSubjectUID());
					hashOfSubjectsWithTheirSubjectCustomData.put(previousLss.getSubjectUID(), valuesForThisLss);	
					previousLss = data.getLinkSubjectStudy();
					map = new HashMap<String, String>();//reset
					valuesForThisLss = new ExtractionVO();
				}

				//if any error value, then just use that - though, yet again I really question the acceptance of error data
				if(data.getErrorDataValue() !=null && !data.getErrorDataValue().isEmpty()) {
					map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getErrorDataValue());
				}
				else {
					// Determine field type and assign key value accordingly
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getDateDataValue().toString());
					}
					else if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getNumberDataValue().toString());
					}
					else if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getTextDataValue());
					}					
				}			
			}

			//finalize the last entered key value sets/extraction VOs
			if(map!=null && previousLss!=null){
				valuesForThisLss.setKeyValues(map);
				valuesForThisLss.setSubjectUid(previousLss.getSubjectUID());
				hashOfSubjectsWithTheirSubjectCustomData.put(previousLss.getSubjectUID(), valuesForThisLss);
			}

			//can probably now go ahead and add these to the dataVO...even though inevitable further filters may further axe this list or parts of it.
			allTheData.setSubjectCustomData(hashOfSubjectsWithTheirSubjectCustomData);
		}

		return idsToInclude;
	}	

	/**
	 * @param allTheData - reference to the object containing our data collected so far, this is to be updated as we continue our refinement.
	 * @param search 
	 * @param biospecimenIdsToInclude - the constantly refined list of ID's passed from the previous extraction step
	 * 
	 * @return the updated list of uids that are still left after the filtering.
	 */
	private List<Long> applyBiospecimenCustomFilters(DataExtractionVO allTheData, Search search, List<Long> idsToInclude, List<Long> biospecimenIdsAfterFiltering){
		//		List<Long> biospecimenIdsToInclude = new ArrayList<Long>();
		if(idsToInclude!=null && !idsToInclude.isEmpty() && biospecimenIdsAfterFiltering!=null && !biospecimenIdsAfterFiltering.isEmpty()){
			String queryToFilterBiospecimenIDs = getBiospecimenDataCustomFieldIdQuery(search);

			//Collection<CustomFieldDisplay> cfdsToReturn = getSelectedBiospecimenCustomFieldDisplaysForSearch(search);
			//log.info("about to APPLY subject  filters.  UIDs size =" + idsToInclude.size() + " query string = " + queryToFilterSubjectIDs + " cfd to return size = " + cfdsToReturn.size());
			if(!queryToFilterBiospecimenIDs.isEmpty()){
				Query query = getSession().createQuery(queryToFilterBiospecimenIDs);
				query.setParameterList("idList", biospecimenIdsAfterFiltering);//TODO ASAP...this should be biospecimen list and not subjuid list now
				List<Long> biopecimenIds = query.list();
				biospecimenIdsAfterFiltering.clear();

				for(Long id : biopecimenIds){
					biospecimenIdsAfterFiltering.add(id);
				}

				log.info("rows returned = " + biospecimenIdsAfterFiltering.size());
				if(biospecimenIdsAfterFiltering.isEmpty()){
					idsToInclude.clear();
				}
				else{
					List<Long> subjectListIds = getSubjectIdsForBiospecimenIds(biospecimenIdsAfterFiltering);
					idsToInclude.clear();
					for (Long id : subjectListIds){
						idsToInclude.add(id);
					}
				}
			}
			else{
				log.info("there were no biospecimen custom data filters, therefore don't run filter query");
			}
		}
		else{
			log.info("there are no id's to filter.  therefore won't run filtering query");
		}

		Collection<CustomFieldDisplay> customFieldToGet = getSelectedBiospecimenCustomFieldDisplaysForSearch(search);
		/* We have the list of biospecimens, and therefore the list of biospecimen custom data - now bring back all the custom data rows IF they have any data they need */
		if(biospecimenIdsAfterFiltering!=null && !biospecimenIdsAfterFiltering.isEmpty() && !customFieldToGet.isEmpty()){
			String queryString = "select data from BiospecimenCustomFieldData data  " +
					" left join fetch data.biospecimen "  +
					" left join fetch data.customFieldDisplay custFieldDisplay "  +
					" left join fetch custFieldDisplay.customField custField "  +
					" where data.biospecimen.id in (:biospecimenIdsToInclude)" +
					" and data.customFieldDisplay in (:customFieldsList)" + 
					" order by data.biospecimen.id " ;
			Query query2 = getSession().createQuery(queryString);
			query2.setParameterList("biospecimenIdsToInclude", biospecimenIdsAfterFiltering);
			query2.setParameterList("customFieldsList", customFieldToGet);

			List<BiospecimenCustomFieldData> scfData = query2.list();
			HashMap<String, ExtractionVO> hashOfBiospecimensWithTheirBiospecimenCustomData = allTheData.getBiospecimenCustomData();

			ExtractionVO valuesForThisBiospecimen = new ExtractionVO();
			HashMap<String, String> map = null;
			String previousBiospecimenUid = null;
			String previousSubjectUid = null;
			//will try to order our results and can therefore just compare to last LSS and either add to or create new Extraction VO
			for (BiospecimenCustomFieldData data : scfData) {

				if(previousBiospecimenUid==null){
					map = new HashMap<String, String>();
					previousBiospecimenUid = data.getBiospecimen().getBiospecimenUid();
					previousSubjectUid = data.getBiospecimen().getLinkSubjectStudy().getSubjectUID();
				}
				else if(data.getBiospecimen().getBiospecimenUid().equals(previousBiospecimenUid)){
					//then just put the data in
				}
				else{	//if its a new LSS finalize previous map, etc
					valuesForThisBiospecimen.setKeyValues(map);
					valuesForThisBiospecimen.setSubjectUid(previousSubjectUid);
					hashOfBiospecimensWithTheirBiospecimenCustomData.put(previousBiospecimenUid, valuesForThisBiospecimen);	
					previousBiospecimenUid = data.getBiospecimen().getBiospecimenUid();
					map = new HashMap<String, String>();//reset
					valuesForThisBiospecimen = new ExtractionVO();
					previousSubjectUid = data.getBiospecimen().getLinkSubjectStudy().getSubjectUID();
				}

				//if any error value, then just use that - though, yet again I really question the acceptance of error data
				if(data.getErrorDataValue() !=null && !data.getErrorDataValue().isEmpty()) {
					map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getErrorDataValue());
				}
				else {
					// Determine field type and assign key value accordingly
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getDateDataValue().toString());
					}
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getNumberDataValue().toString());
					}
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getTextDataValue());
					}
				}			
			}

			//finalize the last entered key value sets/extraction VOs
			if(map!=null && previousBiospecimenUid!=null){
				valuesForThisBiospecimen.setSubjectUid(previousSubjectUid);
				valuesForThisBiospecimen.setKeyValues(map);
				hashOfBiospecimensWithTheirBiospecimenCustomData.put(previousBiospecimenUid, valuesForThisBiospecimen);
			}

			//can probably now go ahead and add these to the dataVO...even though inevitable further filters may further axe this list or parts of it.
			allTheData.setBiospecimenCustomData(hashOfBiospecimensWithTheirBiospecimenCustomData);
		}		
		return idsToInclude;
	}



	/**
	 * @param allTheData - reference to the object containing our data collected so far, this is to be updated as we continue our refinement.
	 * @param search 
	 * @param bioCollectionIdsToInclude - the constantly refined list of ID's passed from the previous extraction step
	 * 
	 * @return the updated list of uids that are still left after the filtering.
	 */
	private List<Long> applyBioCollectionCustomFilters(DataExtractionVO allTheData, Search search, List<Long> idsToInclude, List<Long> bioCollectionIdsAfterFiltering){

		if(idsToInclude!=null && !idsToInclude.isEmpty() && !bioCollectionIdsAfterFiltering.isEmpty()){
			String queryToFilterBioCollectionIDs = getBioCollectionDataCustomFieldIdQuery(search);

			//Collection<CustomFieldDisplay> cfdsToReturn = getSelectedBioCollectionCustomFieldDisplaysForSearch(search);
			//log.info("about to APPLY subject  filters.  UIDs size =" + idsToInclude.size() + " query string = " + queryToFilterSubjectIDs + " cfd to return size = " + cfdsToReturn.size());
			if(!queryToFilterBioCollectionIDs.isEmpty()){
				Query query = getSession().createQuery(queryToFilterBioCollectionIDs);
				query.setParameterList("idList", bioCollectionIdsAfterFiltering);//TODO ASAP...this should be bioCollection list and not subjuid list now
				//bioCollectionIdsAfterFiltering = query.list(); 	
				log.info("rows returned = " + bioCollectionIdsAfterFiltering.size());
				List<Long> bioCollList = query.list();
				//bioCollectionIdsAfterFiltering = new ArrayList<Long>(bioCollectionIdsAfterFiltering);
				bioCollectionIdsAfterFiltering.clear();
				for (Object id : bioCollList) {
					bioCollectionIdsAfterFiltering.add((Long) id);
				}

				if(bioCollectionIdsAfterFiltering.isEmpty()){
					idsToInclude.clear();
				}
				else{
					List<Long> subjectIdsToInclude = getSubjectIdsForBioCollectionIds(bioCollectionIdsAfterFiltering);
					idsToInclude.clear();
					for(Long id : subjectIdsToInclude){
						idsToInclude.add(id);	
					}

				}
			}
			else{
				log.info("there were no biocol custom data filters, therefore don't run filter query");
			}
		}
		else{
			log.info("there are no id's to filter.  therefore won't run filtering query");
		}

		Collection<CustomFieldDisplay> customFieldToGet = getSelectedBiocollectionCustomFieldDisplaysForSearch(search);
		/* We have the list of bioCollections, and therefore the list of bioCollection custom data - now bring back all the custom data rows IF they have any data they need */
		if(bioCollectionIdsAfterFiltering!=null && !bioCollectionIdsAfterFiltering.isEmpty() && !customFieldToGet.isEmpty()){
			String queryString = "select data from BioCollectionCustomFieldData data  " +
					" left join fetch data.bioCollection "  +
					" left join fetch data.customFieldDisplay custFieldDisplay "  +
					" left join fetch custFieldDisplay.customField custField "  +
					" where data.bioCollection.id in (:bioCollectionIdsToInclude)" +
					" and data.customFieldDisplay in (:customFieldsList)" + 
					" order by data.bioCollection.id " ;
			Query query2 = getSession().createQuery(queryString);
			query2.setParameterList("bioCollectionIdsToInclude", bioCollectionIdsAfterFiltering);
			query2.setParameterList("customFieldsList", customFieldToGet);

			List<BioCollectionCustomFieldData> scfData = query2.list();
			HashMap<String, ExtractionVO> hashOfBioCollectionsWithTheirBioCollectionCustomData = allTheData.getBiocollectionCustomData();

			ExtractionVO valuesForThisBiocollection = new ExtractionVO();
			HashMap<String, String> map = null;
			String previousBioCollectionUid = null;
			String previousSubjectUid = null;
			//will try to order our results and can therefore just compare to last LSS and either add to or create new Extraction VO
			for (BioCollectionCustomFieldData data : scfData) {

				if(previousBioCollectionUid==null){
					map = new HashMap<String, String>();
					previousBioCollectionUid = data.getBioCollection().getBiocollectionUid();
					previousSubjectUid = data.getBioCollection().getLinkSubjectStudy().getSubjectUID();
				}
				else if(data.getBioCollection().getBiocollectionUid().equals(previousBioCollectionUid)){
					//then just put the data in
				}
				else{	//if its a new LSS finalize previous map, etc
					valuesForThisBiocollection.setKeyValues(map);
					hashOfBioCollectionsWithTheirBioCollectionCustomData.put(previousBioCollectionUid, valuesForThisBiocollection);	

					map = new HashMap<String, String>();//reset
					valuesForThisBiocollection = new ExtractionVO();
					previousBioCollectionUid = data.getBioCollection().getBiocollectionUid();
					previousSubjectUid = data.getBioCollection().getLinkSubjectStudy().getSubjectUID();
				}

				//if any error value, then just use that - though, yet again I really question the acceptance of error data
				if(data.getErrorDataValue() !=null && !data.getErrorDataValue().isEmpty()) {
					map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getErrorDataValue());
				}
				else {
					// Determine field type and assign key value accordingly
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getDateDataValue().toString());
					}
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getNumberDataValue().toString());
					}
					if (data.getCustomFieldDisplay().getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
						map.put(data.getCustomFieldDisplay().getCustomField().getName(), data.getTextDataValue());
					}
				}			
			}

			//finalize the last entered key value sets/extraction VOs
			if(map!=null && previousBioCollectionUid!=null){
				valuesForThisBiocollection.setSubjectUid(previousSubjectUid);
				valuesForThisBiocollection.setKeyValues(map);
				hashOfBioCollectionsWithTheirBioCollectionCustomData.put(previousBioCollectionUid, valuesForThisBiocollection);
			}

			//can probably now go ahead and add these to the dataVO...even though inevitable further filters may further axe this list or parts of it.
			allTheData.setBiocollectionCustomData(hashOfBioCollectionsWithTheirBioCollectionCustomData);
		}		
		return idsToInclude;
	}	


	private List<Long> getSubjectIdsForBiospecimenIds(List<Long> biospecimenIdsToInclude) {
		if(biospecimenIdsToInclude == null || biospecimenIdsToInclude.isEmpty()){
			return new ArrayList<Long>();
		}
		String queryString = "select bio.linkSubjectStudy.id from Biospecimen bio " 
				+ " where bio.id in (:biospecimenIdsToInclude) ";
		Query query = getSession().createQuery(queryString);
		query.setParameterList("biospecimenIdsToInclude", biospecimenIdsToInclude);
		return query.list();
	}


	private List<Long> getSubjectIdsForPhenoDataIds(List<Long> phenoDataIdsToInclude) {
		if(phenoDataIdsToInclude == null || phenoDataIdsToInclude.isEmpty()){
			return new ArrayList<Long>();
		}
		String queryString = "select pheno.phenoDataSetCollection.linkSubjectStudy.id from PhenoDataSetData pheno "
							+ " where pheno.id in (:phenoDataIdsToInclude) ";
		Query query = getSession().createQuery(queryString);
		query.setParameterList("phenoDataIdsToInclude", phenoDataIdsToInclude);
		return query.list();
	}

	private List<Long> getBiospecimenIdForSubjectIds(List<Long> subjectIds) {
		if(subjectIds == null || subjectIds.isEmpty()){
			return new ArrayList<Long>();
		}
		String queryString = "select bio.id from Biospecimen bio " 
							+ " where bio.linkSubjectStudy.id in (:subjectIds) ";
		Query query = getSession().createQuery(queryString);
		query.setParameterList("subjectIds", subjectIds);
		return query.list();
	}

	private List<Long> getSubjectIdsForBioCollectionIds(List<Long> bioCollectionIdsToInclude) {
		if(bioCollectionIdsToInclude == null || bioCollectionIdsToInclude.isEmpty()){
			return new ArrayList<Long>();
		}
		String queryString = "select bio.linkSubjectStudy.id from BioCollection bio " 
							+ " where bio.id in (:bioCollectionIdsToInclude) ";
		Query query = getSession().createQuery(queryString);
		query.setParameterList("bioCollectionIdsToInclude", bioCollectionIdsToInclude);
		return query.list();
	}

	private List<Long> getBioCollectionIdForSubjectIds(List<Long> subjectIds) {
		if(subjectIds == null || subjectIds.isEmpty()){
			return new ArrayList<Long>();
		}
		String queryString = "select bc.id from BioCollection bc " 
							+ " where bc.linkSubjectStudy.id in (:subjectIds) ";
		Query query = getSession().createQuery(queryString);
		query.setParameterList("subjectIds", subjectIds);
		return query.list();
	}


	/**
	 * This will get all the pheno data for the given subjects FOR THIS ONE CustomFieldGroup aka questionaire (aka data set)
	 * 
	 * @param allTheData
	 * @param search
	 * @param idsToInclude
	 * @return the updated list of uids that are still left after the filtering. 
	 */
	private List<Long> applyPhenoDataSetFilters(DataExtractionVO allTheData, Search search, List<Long> idsToInclude){

		Set<QueryFilter> filters = search.getQueryFilters();

		Collection<PhenoDataSetGroup> pdsgWithFilters = getPhenoDataSetGroupsForPhenoFilters(search, filters);
		List<Long> phenoCollectionIdsSoFar = new ArrayList<Long>();

		for(PhenoDataSetGroup phenoGroup : pdsgWithFilters){
			log.info("Pheno group: " + phenoGroup.getName());
			if(idsToInclude!=null && !idsToInclude.isEmpty()){
				String queryToGetPhenoIdsForGivenSearchAndCFGFilters = getQueryForPhenoIdsForSearchAndCFGFilters(search, phenoGroup);

				if(!queryToGetPhenoIdsForGivenSearchAndCFGFilters.isEmpty()){
					Query query = getSession().createQuery(queryToGetPhenoIdsForGivenSearchAndCFGFilters);
					query.setParameterList("idList", idsToInclude);//TODO ASAP...this should be pheno list and not subjuid list now

					QueryTranslatorFactory translatorFactory = new ASTQueryTranslatorFactory();
					SessionFactoryImplementor factory = (SessionFactoryImplementor) getSession().getSessionFactory();
					QueryTranslator translator = translatorFactory.
							createQueryTranslator(query.getQueryString(), query.getQueryString(), Collections.EMPTY_MAP, factory);
					translator.compile(Collections.EMPTY_MAP, false);
					log.info(translator.getSQLString());



					List<Long> phenosForThisCFG = query.list(); 
					phenoCollectionIdsSoFar.addAll(phenosForThisCFG);
					log.info("rows returned = " + phenoCollectionIdsSoFar.size());
				}
				else{
					log.info("there were no pheno custom data filters, therefore don't run filter query");
				}
			}
			else{
				log.info("there are no id's to filter.  therefore won't run filtering query");
			}
		}
		//now that we have all the phenoCollection IDs...get the updated list of subjects
		if(phenoCollectionIdsSoFar.isEmpty()){
			if(!pdsgWithFilters.isEmpty()){
				//there were no phenocollectionid's returned because they were validly filtered.  leave idsToIncludeAsItWas
				idsToInclude = new ArrayList<Long>();
			}
			else{
				//there were no filters so just leave the list of subjects ias it was
			}
		}
		else{
			idsToInclude = getSubjectIdsForPhenoDataIds(phenoCollectionIdsSoFar);
		}


		//now that we have the pheno collection id, we just find the data for the selected customfields

		if(!idsToInclude.isEmpty()){
			Collection<PhenoDataSetFieldDisplay> customFieldToGet = getSelectedPhenoDataSetFieldDisplaysForSearch(search);//getSelectedPhenoCustomFieldDisplaysForSearch(search);
			// We have the list of phenos, and therefore the list of pheno custom data - now bring back all the custom data rows IF they have any data they need 
			if(	(!phenoCollectionIdsSoFar.isEmpty() || (phenoCollectionIdsSoFar.isEmpty() && pdsgWithFilters.isEmpty()))
					&& !customFieldToGet.isEmpty()
					){
				String queryString = "select data from PhenoDataSetData data  " +
						" left join fetch data.phenoDataSetCollection phenoDataSetCollection"  +
						" left join fetch data.phenoDataSetFieldDisplay phenoDataSetFieldDisplay "  +
						" left join fetch phenoDataSetFieldDisplay.phenoDataSetField phenoField "  		+(
						( (phenoCollectionIdsSoFar.isEmpty() && pdsgWithFilters.isEmpty())			?(
								" where data.phenoDataSetCollection.linkSubjectStudy.id in (:idsToInclude) "):(
								" where data.phenoDataSetCollection.id in (:phenoIdsToInclude)" 			) ) )
																							+
						" and data.phenoDataSetFieldDisplay in (:customFieldsList)" +
						" order by data.phenoDataSetCollection.id" ;
				Query query2 = getSession().createQuery(queryString);
				if(phenoCollectionIdsSoFar.isEmpty() && pdsgWithFilters.isEmpty()){
					query2.setParameterList("idsToInclude", idsToInclude);
				}
				else{
					query2.setParameterList("phenoIdsToInclude", phenoCollectionIdsSoFar);
				}
				query2.setParameterList("customFieldsList", customFieldToGet);

				QueryTranslatorFactory translatorFactory = new ASTQueryTranslatorFactory();
				SessionFactoryImplementor factory = (SessionFactoryImplementor) getSession().getSessionFactory();
				QueryTranslator translator = translatorFactory.
						createQueryTranslator(query2.getQueryString(), query2.getQueryString(), Collections.EMPTY_MAP, factory);
				translator.compile(Collections.EMPTY_MAP, false);
				log.info(translator.getSQLString());
				List<PhenoDataSetData> phenoData = query2.list();

				HashMap<String, ExtractionVO> hashOfPhenosWithTheirPhenoCustomData = allTheData.getPhenoCustomData();

				ExtractionVO valuesForThisPheno = new ExtractionVO();
				HashMap<String, String> map = null;
				Long previousPhenoId = null;
				//will try to order our results and can therefore just compare to last LSS and either add to or create new Extraction VO
				for (PhenoDataSetData data : phenoData) {

					if(previousPhenoId==null){
						map = new HashMap<String, String>();
						previousPhenoId = data.getPhenoDataSetCollection().getId();
						valuesForThisPheno.setSubjectUid(data.getPhenoDataSetCollection().getLinkSubjectStudy().getSubjectUID());
						valuesForThisPheno.setRecordDate(data.getPhenoDataSetCollection().getRecordDate());
						valuesForThisPheno.setCollectionName(data.getPhenoDataSetCollection().getQuestionnaire().getName());
					}
					else if(data.getPhenoDataSetCollection().getId().equals(previousPhenoId)){
						//then just put the data in
					}
					else{	//if its a new LSS finalize previous map, etc
						valuesForThisPheno.setKeyValues(map);
						hashOfPhenosWithTheirPhenoCustomData.put(("" + previousPhenoId), valuesForThisPheno);	
						previousPhenoId = data.getPhenoDataSetCollection().getId();
						map = new HashMap<String, String>();//reset
						valuesForThisPheno = new ExtractionVO();
						valuesForThisPheno.setSubjectUid(data.getPhenoDataSetCollection().getLinkSubjectStudy().getSubjectUID());
						valuesForThisPheno.setRecordDate(data.getPhenoDataSetCollection().getRecordDate());
						valuesForThisPheno.setCollectionName(data.getPhenoDataSetCollection().getQuestionnaire().getName());
					}

					//if any error value, then just use that - though, yet again I really question the acceptance of error data
					if(data.getErrorDataValue() !=null && !data.getErrorDataValue().isEmpty()) {
						map.put(data.getPhenoDataSetFieldDisplay().getPhenoDataSetField().getName(), data.getErrorDataValue());
					}
					else {
						// Determine field type and assign key value accordingly
						if (data.getPhenoDataSetFieldDisplay().getPhenoDataSetField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
							map.put(data.getPhenoDataSetFieldDisplay().getPhenoDataSetField().getName(), data.getDateDataValue().toString());
						}
						if (data.getPhenoDataSetFieldDisplay().getPhenoDataSetField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
							map.put(data.getPhenoDataSetFieldDisplay().getPhenoDataSetField().getName(), data.getNumberDataValue().toString());
						}
						if (data.getPhenoDataSetFieldDisplay().getPhenoDataSetField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
							map.put(data.getPhenoDataSetFieldDisplay().getPhenoDataSetField().getName(), data.getTextDataValue());
						}
					}			
				}

				//finalize the last entered key value sets/extraction VOs
				if(map!=null && previousPhenoId!=null){
					valuesForThisPheno.setKeyValues(map);
					hashOfPhenosWithTheirPhenoCustomData.put("" + previousPhenoId, valuesForThisPheno);
				}

				//can probably now go ahead and add these to the dataVO...even though inevitable further filters may further axe this list or parts of it.
				allTheData.setPhenoCustomData(hashOfPhenosWithTheirPhenoCustomData);
			}		

		}
		return idsToInclude;

	}	


	private Collection<PhenoDataSetGroup> getPhenoDataSetGroupsForPhenoFilters(Search search, Set<QueryFilter> filters) {
		ArkFunction arkFunction = getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);
		List<PhenoDataSetFieldDisplay> phenoDataSetFieldDisplaysForStudy =  getPhenoFieldDisplaysIn(search.getStudy(), arkFunction);
		Set<PhenoDataSetGroup> phenoDataSetGroupsToReturn = new HashSet<PhenoDataSetGroup>();

		for(QueryFilter qf : filters){
			if(qf.getPhenoDataSetFieldDisplay()!=null && phenoDataSetFieldDisplaysForStudy.contains(qf.getPhenoDataSetFieldDisplay())){
				phenoDataSetGroupsToReturn.add(qf.getPhenoDataSetFieldDisplay().getPhenoDataSetGroup());
			}
		}

		return phenoDataSetGroupsToReturn;
	}

	/**
	 * TODO : Chris, please note that we have to complete the hardcoding below after Thileana finishes his insert statements for demographic field.
	 * Alternatively you have reflection to deal with which may be a bit of a nightmare but less lines of code...but completely your call.
	 * 
	 * @param lss
	 * @param personFields
	 * @param lssFields
	 * @param addressFields
	 * @param phoneFields
	 * @return
	 */
	private HashMap<String, String> constructKeyValueHashmap(LinkSubjectStudy lss, Collection<DemographicField> personFields, Collection<DemographicField> lssFields,
			Collection<DemographicField> addressFields, Collection<DemographicField> phoneFields, Collection<DemographicField> otherIDFields,Collection<DemographicField> linkSubjectTwinsFields) {
		HashMap map = new HashMap<String, String>();
		for (DemographicField field : personFields) {
			// TODO: Analyse performance cost of using reflection instead...would be CLEANER code...maybe dangerous/slow
			if (field.getFieldName().equalsIgnoreCase("firstName")) {
				if(lss.getPerson().getFirstName()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getFirstName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("middleName")) {
				if(lss.getPerson().getMiddleName()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getMiddleName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("lastName")) {
				if(lss.getPerson().getLastName()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getLastName());
				}	
			}
			else if (field.getFieldName().equalsIgnoreCase("preferredName")) {
				if(lss.getPerson().getPreferredName()!=null)
					map.put(field.getPublicFieldName(), lss.getPerson().getPreferredName());
			}
			else if (field.getFieldName().equalsIgnoreCase("dateOfBirth")) {
				if(lss.getPerson().getDateOfBirth()!=null){
					map.put(field.getPublicFieldName(), formatDate(lss.getPerson().getDateOfBirth().toString()));
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("dateOfDeath")) {
				if(lss.getPerson().getDateOfDeath()!=null){
					map.put(field.getPublicFieldName(), formatDate(lss.getPerson().getDateOfDeath().toString()));
				} 
			}
			else if (field.getFieldName().equalsIgnoreCase("causeOfDeath")) {
				if(lss.getPerson().getCauseOfDeath()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getCauseOfDeath());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("preferredEmail")) {
				if(lss.getPerson().getPreferredEmail()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getPreferredEmail());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("preferredEmailStatus")) {
				if(lss.getPerson().getPreferredEmailStatus()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getPreferredEmailStatus().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("otherEmail")) {
				if(lss.getPerson().getOtherEmail()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getOtherEmail());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("otherEmailStatus")) {
				if(lss.getPerson().getOtherEmailStatus()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getOtherEmailStatus().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("dateLastKnownAlive")) {
				if(lss.getPerson().getDateLastKnownAlive()!=null){
					map.put(field.getPublicFieldName(), formatDate(lss.getPerson().getDateLastKnownAlive().toString()));					
				}
			}

			else if (field.getFieldName().equalsIgnoreCase("genderType")) {
				if(lss.getPerson().getGenderType()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getGenderType().getName());					
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("title")) {
				if(lss.getPerson().getTitleType()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getTitleType().getName());					
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("vitalStatus")) {
				if(lss.getPerson().getVitalStatus()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getVitalStatus().getName());					
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("maritalStatus")) {
				if(lss.getPerson().getMaritalStatus()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getMaritalStatus().getName());					
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("contactMethod")) {
				if(lss.getPerson().getPersonContactMethod()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getPersonContactMethod().getName());					
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("preferredEmailStatus")) {
				if(lss.getPerson().getPreferredEmailStatus()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getPreferredEmailStatus().getName());					
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("otherEmailStatus")) {
				if(lss.getPerson().getOtherEmailStatus()!=null){
					map.put(field.getPublicFieldName(), lss.getPerson().getOtherEmailStatus().getName());					
				}
			}
		}
		for (DemographicField field : lssFields) {
			if (field.getFieldName().equalsIgnoreCase("subjectStatus")) {
				if(lss.getSubjectStatus()!=null){
					map.put(field.getPublicFieldName(), lss.getSubjectStatus().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("consentDate")) {
				if(lss.getConsentDate()!=null){
					map.put(field.getPublicFieldName(), lss.getConsentDate() == null ? "" : formatDate(lss.getConsentDate().toString()));
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("consentStatus")) {
				if(lss.getConsentStatus()!=null){
					map.put(field.getPublicFieldName(), lss.getConsentStatus().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("consentType")) {
				if(lss.getConsentType()!=null){
					map.put(field.getPublicFieldName(), lss.getConsentType().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("consentDownloaded")) {
				if(lss.getConsentDownloaded()!=null){
					map.put(field.getPublicFieldName(), lss.getConsentDownloaded().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("consentToPassiveDataGathering")) {
				if(lss.getConsentToPassiveDataGathering()!=null){
					map.put(field.getPublicFieldName(), lss.getConsentToPassiveDataGathering().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("consentToActiveContact")) {
				if(lss.getConsentToActiveContact()!=null){
					map.put(field.getPublicFieldName(), lss.getConsentToActiveContact().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("consentToUseData")) {
				if(lss.getConsentToUseData()!=null){
					map.put(field.getPublicFieldName(), lss.getConsentToUseData().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("heardAboutStudy")) {
				if(lss.getHeardAboutStudy()!=null){
					map.put(field.getPublicFieldName(), lss.getHeardAboutStudy());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("comments")) {
				if(lss.getComment()!=null){
					map.put(field.getPublicFieldName(), lss.getComment());
				}
			}
		}
		for (DemographicField field : addressFields) {
			int count = 0;
			// assume they have filtered type/status in hql sql statement
			for (Address a : lss.getPerson().getAddresses()) {	//TODO : I would imagine switching these for loops would be more efficient
				count++;
				if (field.getFieldName().equalsIgnoreCase("addressLine1")) {
					map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), a.getAddressLineOne());
				}
				else if (field.getFieldName().equalsIgnoreCase("streetAddress")) {
					map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), a.getStreetAddress());
				}
				else if (field.getFieldName().equalsIgnoreCase("city")) {
					map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), a.getCity());
				}
				else if (field.getFieldName().equalsIgnoreCase("country")) {
					if(a.getCountry() != null) {
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), a.getCountry().getName());
					}
					else {
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), "");
					}
				}
				else if (field.getFieldName().equalsIgnoreCase("state")) {
					if(a.getState() != null) {
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), a.getState().getName());
					}
					else{ 
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), "");
					}
				}
				else if (field.getFieldName().equalsIgnoreCase("otherState")) {
					map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), a.getOtherState());
				}
				else if (field.getFieldName().equalsIgnoreCase("addressStatus")) {
					map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), a.getAddressStatus().getName());
				}
				else if (field.getFieldName().equalsIgnoreCase("addressType")) {
					map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), a.getAddressType().getName());
				}
				else if (field.getFieldName().equalsIgnoreCase("postCode")) {
					map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), a.getPostCode());
				}
				else if (field.getFieldName().equalsIgnoreCase("dateReceived")) {

					if(a.getDateReceived() != null) {
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), formatDate(a.getDateReceived().toString()));
					}
					else{
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), "");
					}
				}
				//valid From	
				else if (field.getFieldName().equalsIgnoreCase("validFrom")) {

					if(a.getValidFrom() != null) {
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), formatDate(a.getValidFrom().toString()));
					}
					else{
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), "");
					}
				}
				//valid To
				else if (field.getFieldName().equalsIgnoreCase("validTo")) {

					if(a.getValidTo() != null) {
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), formatDate(a.getValidTo().toString()));
					}
					else{
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), "");
					}
				}
				else if (field.getFieldName().equalsIgnoreCase("comments")) {
					map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), a.getComments());
				}
				else if (field.getFieldName().equalsIgnoreCase("preferredMailingAddress")) {
					map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), a.getPreferredMailingAddress());
				}
				else if (field.getFieldName().equalsIgnoreCase("source")) {
					map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), a.getSource());
				}
			}
		}
		for (DemographicField field : phoneFields) {
			int count = 0;
			// assume they have filtered type/status in hql sql statement
			for (Phone phone : lss.getPerson().getPhones()) {
				count++;
				if (field.getFieldName().equalsIgnoreCase("phoneNumber")) {
					map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), phone.getPhoneNumber());
				}
				else if (field.getFieldName().equalsIgnoreCase("phoneType")) {
					map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), phone.getPhoneType().getName());
				}
				else if (field.getFieldName().equalsIgnoreCase("areaCode")) {
					map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), phone.getAreaCode());
				}
				else if (field.getFieldName().equalsIgnoreCase("phoneStatus")) {
					map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), phone.getPhoneStatus().getName());
				}
				else if (field.getFieldName().equalsIgnoreCase("source")) {
					map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), phone.getSource());
				}
				else if (field.getFieldName().equalsIgnoreCase("dateReceived")) {
					if(phone.getDateReceived()!=null) {
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), formatDate(phone.getDateReceived().toString()));
					}
					else {
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), "");
					}
				}
				//valid From	
				else if (field.getFieldName().equalsIgnoreCase("validFrom")) {

					if(phone.getValidFrom() != null) {
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), formatDate(phone.getValidFrom().toString()));
					}
					else{
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), "");
					}
				}
				//valid To
				else if (field.getFieldName().equalsIgnoreCase("validTo")) {

					if(phone.getValidTo() != null) {
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), formatDate(phone.getValidTo().toString()));
					}
					else{
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), "");
					}
				}
				else if (field.getFieldName().equalsIgnoreCase("silentMode")) {
					if(phone.getSilentMode() != null) {
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), phone.getSilentMode().getName());
					}
					else {
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), "");
					}
				}
				else if (field.getFieldName().equalsIgnoreCase("comment")) {
					map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), phone.getComment());
				}
			}
		}
		for(DemographicField field : otherIDFields) {
			if(field.getFieldName().equalsIgnoreCase("otherID")) {
				if(lss.getPerson().getOtherIDs() != null) {
					int index = 1;
					for(OtherID o : lss.getPerson().getOtherIDs()) {
						log.info("Adding otherID, " + o.getOtherID() + " to map");
						map.put(field.getPublicFieldName() + "_" + index, o.getOtherID());
						index++;
					}
				}
			} else if(field.getFieldName().equalsIgnoreCase("otherID_source")) {
				if(lss.getPerson().getOtherIDs() != null) {
					int index = 1;
					for(OtherID o : lss.getPerson().getOtherIDs()) {
						log.info("Adding otherID, " + o.getOtherID_Source() + " to map");
						map.put(field.getPublicFieldName() + "_" + index, o.getOtherID_Source());
						index++;
					}
				}
			}
		}
		// Generate the twin or triplet details for a subject.
		for (DemographicField field : linkSubjectTwinsFields) {
			int count = 0;
			for(LinkSubjectTwin lst: lss.getLinkSubjectTwinsAsFirstSubject()){
				count++;
				if(lss.getId().equals(lst.getFirstSubject().getId())){
					if (field.getFieldName().equalsIgnoreCase("secondSubject")) {
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), lst.getSecondSubject().getSubjectUID());
					}
					else if(field.getFieldName().equalsIgnoreCase("twinType")) {
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), lst.getTwinType().getName());
					}
				}
			}
			for(LinkSubjectTwin lst: lss.getLinkSubjectTwinsAsSecondSubject()){
				count++;
				if(lss.getId().equals(lst.getSecondSubject().getId())){
					if (field.getFieldName().equalsIgnoreCase("secondSubject")) {
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), lst.getFirstSubject().getSubjectUID());
					}
					else if(field.getFieldName().equalsIgnoreCase("twinType")) {
						map.put((field.getPublicFieldName() + ((count > 1) ? ("_" + count) : "")), lst.getTwinType().getName());
					}
				}
			}
		} 
		return map;
	}

	private String formatDate(String valueResult) {
		try {
			DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
			String[] dateFormats = { au.org.theark.core.Constants.DD_MM_YYYY, au.org.theark.core.Constants.yyyy_MM_dd_hh_mm_ss_S, au.org.theark.core.Constants.yyyy_MM_dd};
			Date date = DateUtils.parseDate(valueResult, dateFormats);
			return (dateFormat.format(date));
		}
		catch (ParseException e) {
			return(valueResult);
		}
	}


	private HashMap<String, String> constructKeyValueHashmap(BioCollection bioCollection, Collection<BiocollectionField> bioCollectionFields) {
		HashMap<String,String> map = new HashMap<String, String>();

		for (BiocollectionField field : bioCollectionFields) {
			// TODO: Analyse performance cost of using reflection instead...would be CLEANER code...maybe dangerous/slow
			if (field.getFieldName().equalsIgnoreCase("name")) {
				if(bioCollection.getName() !=null){
					map.put(field.getPublicFieldName(), bioCollection.getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("collectionDate")) {
				if(bioCollection.getCollectionDate()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getCollectionDate()!=null?formatDate(bioCollection.getCollectionDate().toString()):"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("deleted")) {
				if(bioCollection.getDeleted()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getDeleted()!=null?bioCollection.getDeleted().toString():"");
				}
			}			
			else if (field.getFieldName().equalsIgnoreCase("timestamp")) {
				if(bioCollection.getTimestamp()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getTimestamp());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("comments")) {
				if(bioCollection.getComments()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getComments());
				}
			}			
			else if (field.getFieldName().equalsIgnoreCase("hospital")) {
				if(bioCollection.getHospital()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getHospital());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("surgeryDate")) {
				if(bioCollection.getSurgeryDate()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getSurgeryDate()!=null?bioCollection.getSurgeryDate().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("diagCategory")) {
				if(bioCollection.getDiagCategory()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getDiagCategory());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("refDoctor")) {
				if(bioCollection.getRefDoctor()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getRefDoctor());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("patientage")) {
				if(bioCollection.getPatientage()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getPatientage()!=null?bioCollection.getPatientage().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("dischargeDate")) {
				if(bioCollection.getDischargeDate()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getDischargeDate()!=null?bioCollection.getDischargeDate().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("hospitalUr")) {
				if(bioCollection.getHospitalUr()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getHospitalUr());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("diagDate")) {
				if(bioCollection.getDiagDate()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getDiagDate()!=null?bioCollection.getDiagDate().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("collectiongroupId")) {
				if(bioCollection.getCollectiongroupId()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getCollectiongroupId()!=null?bioCollection.getCollectiongroupId().toString():"");
				}
			}			
			else if (field.getFieldName().equalsIgnoreCase("episodeNum")) {
				if(bioCollection.getEpisodeNum()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getEpisodeNum());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("episodeDesc")) {
				if(bioCollection.getEpisodeDesc()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getEpisodeDesc());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("collectiongroup")) {
				if(bioCollection.getCollectiongroup()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getCollectiongroup());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("tissuetype")) {
				if(bioCollection.getTissuetype()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getTissuetype());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("tissueclass")) {
				if(bioCollection.getTissueclass()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getTissueclass());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("pathlabno")) {
				if(bioCollection.getPathlabno()!=null){
					map.put(field.getPublicFieldName(), bioCollection.getPathlabno());
				}
			}			
		}
		return map;		
	}

	private HashMap<String, String> constructKeyValueHashmap(Biospecimen biospecimen, Collection<BiospecimenField> biospecimenFields) {
		HashMap<String,String> map = new HashMap<String, String>();

		for (BiospecimenField field : biospecimenFields) {
			if (field.getFieldName().equalsIgnoreCase("sampleType")) {
				if(biospecimen.getSampleType() !=null){
					map.put(field.getPublicFieldName(), biospecimen.getSampleType().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("storedIn")) {
				if(biospecimen.getStoredIn() !=null){
					map.put(field.getPublicFieldName(), biospecimen.getStoredIn().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("grade")) {
				if(biospecimen.getGrade() !=null){
					map.put(field.getPublicFieldName(), biospecimen.getGrade().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("sampleDate")) {
				if(biospecimen.getSampleDate() !=null){
					map.put(field.getPublicFieldName(), biospecimen.getSampleDate()!=null?biospecimen.getSampleDate().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("sampleTime")) {
				if(biospecimen.getSampleTime() !=null){
//					map.put(field.getPublicFieldName(), biospecimen.getSampleTime()!=null?biospecimen.getSampleTime().toString().substring(8):"");
					SimpleDateFormat ft = new SimpleDateFormat ("H:mm:ss");
					map.put(field.getPublicFieldName(), ft.format(biospecimen.getSampleTime()));
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("processedDate")) {
				if(biospecimen.getProcessedDate() !=null){
					map.put(field.getPublicFieldName(), biospecimen.getProcessedDate()!=null?biospecimen.getProcessedDate().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("processedTime")) {
				if(biospecimen.getProcessedTime() !=null){
					SimpleDateFormat ft = new SimpleDateFormat ("H:mm:ss");
					map.put(field.getPublicFieldName(), ft.format(biospecimen.getProcessedTime()));
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("comments")) {
				if(biospecimen.getComments() != null){
					map.put(field.getPublicFieldName(), biospecimen.getComments());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("unit")) {
				if(biospecimen.getUnit() != null){
					map.put(field.getPublicFieldName(), biospecimen.getUnit().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("treatmentType")) {
				if(biospecimen.getTreatmentType() != null){
					map.put(field.getPublicFieldName(), biospecimen.getTreatmentType().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("barcoded")) {
				if(biospecimen.getBarcoded() != null){
					map.put(field.getPublicFieldName(), biospecimen.getBarcoded()!=null?biospecimen.getBarcoded().toString():"" );
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("anticoag")) {
				if(biospecimen.getAnticoag() != null){
					map.put(field.getPublicFieldName(), biospecimen.getAnticoag().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("status")) {
				if(biospecimen.getStatus() != null){
					map.put(field.getPublicFieldName(), biospecimen.getStatus().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("concentration")) {
				if(biospecimen.getConcentration() != null){
					map.put(field.getPublicFieldName(), biospecimen.getConcentration()!=null ? biospecimen.getConcentration().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("amount")) {
				if(biospecimen.getAmount() != null){
					map.put(field.getPublicFieldName(), biospecimen.getAmount()!=null?biospecimen.getAmount().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("status")) {
				if(biospecimen.getTreatmentType() != null){
					map.put(field.getPublicFieldName(), biospecimen.getStatus().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("biospecimenProtocol")) {
				if(biospecimen.getBiospecimenProtocol() != null){
					map.put(field.getPublicFieldName(), biospecimen.getBiospecimenProtocol().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("purity")) {
				if(biospecimen.getPurity() != null){
					map.put(field.getPublicFieldName(), biospecimen.getPurity()!=null?biospecimen.getPurity().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("quality")) {
				if(biospecimen.getQuality() != null){
					map.put(field.getPublicFieldName(), biospecimen.getQuality().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("quantity")) {
				if(biospecimen.getQuantity() != null){
					map.put(field.getPublicFieldName(), biospecimen.getQuantity()!=null?biospecimen.getQuantity().toString():"");
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("site")) {
				if(biospecimen.getInvCell()!=null){
					map.put(field.getPublicFieldName(), biospecimen.getInvCell().getInvBox().getInvRack().getInvFreezer().getInvSite().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("freezer")) {
					if(biospecimen.getInvCell()!=null){		
						map.put(field.getPublicFieldName(), biospecimen.getInvCell().getInvBox().getInvRack().getInvFreezer().getName());
					}
			}
			else if (field.getFieldName().equalsIgnoreCase("rack")) {
				if(biospecimen.getInvCell()!=null){
					map.put(field.getPublicFieldName(), biospecimen.getInvCell().getInvBox().getInvRack().getName());
				}				
			}
			else if (field.getFieldName().equalsIgnoreCase("box")) {
				if(biospecimen.getInvCell()!=null){
					map.put(field.getPublicFieldName(), biospecimen.getInvCell().getInvBox().getName());
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("column")) {
				if(biospecimen.getInvCell()!=null){
					String colLabel = new String();
					if (biospecimen.getInvCell().getInvBox().getColnotype().getName().equalsIgnoreCase("ALPHABET")) {
						char character = (char) (biospecimen.getInvCell().getColno() + 64);
						colLabel = new Character(character).toString();
					}
					else {
						colLabel = new Integer(biospecimen.getInvCell().getColno().intValue()).toString();
					}

					map.put(field.getPublicFieldName(), colLabel);
				}
			}
			else if (field.getFieldName().equalsIgnoreCase("row")) {
				if(biospecimen.getInvCell()!=null){		
					String rowLabel = new String();
					if (biospecimen.getInvCell().getInvBox().getRownotype().getName().equalsIgnoreCase("ALPHABET")) {
						char character = (char) (biospecimen.getInvCell().getRowno() + 64);
						rowLabel = new Character(character).toString();
					}
					else {
						rowLabel = new Integer(biospecimen.getInvCell().getRowno().intValue()).toString();
					}

					map.put(field.getPublicFieldName(), rowLabel);
				}
			}
			else if(field.getFieldName().equalsIgnoreCase("bioCollectionUid")){
				map.put(field.getPublicFieldName(), biospecimen.getBioCollection().getBiocollectionUid());
			}
			else if(field.getFieldName().equalsIgnoreCase("name")){
				map.put(field.getPublicFieldName(), biospecimen.getBioCollection().getName());
			}
			else if(field.getFieldName().equalsIgnoreCase("parentUID")) {
				if(biospecimen.getParent() != null) {
					map.put(field.getPublicFieldName(), biospecimen.getParent().getBiospecimenUid());
				}
			}
		}
		return map;
	}

	private String getPersonFilters(Search search, String filterThusFar) {
		String filterClause = filterThusFar;
		Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
		for (QueryFilter filter : filters) {
			DemographicField demoField = filter.getDemographicField();
			if ((demoField != null)) {
				if (demoField.getEntity() != null && demoField.getEntity().equals(Entity.Person)) {

					//relationship should really come from a method getRelationshipText(entity)     ---- also there are some null assumptions that should be picked up in form saving logic
					filterClause += makeLineFromOperatorAndValues(" and lss.person.", demoField.getFieldName(), filter.getOperator(), demoField.getFieldType(), filter.getValue(), filter.getSecondValue());

				}
			}
		}
		log.info("filterClause = " + filterClause);
		return (filterClause);
	}

	private String makeLineFromOperatorAndValues(String relationship, String fieldName, Operator operator, FieldType fieldType, String value1, String value2){
		String fieldNameText = fieldType.getName().equalsIgnoreCase(Constants.FIELD_TYPE_LOOKUP)?(fieldName + ".name"):(fieldName); //eg; firstName     //String relationText = relationship; //eg; "lss.person." kinda like saying fully classified table name i guess
		String lineAfterFieldName = "";																								//eg; is not null...... or..... is like 'blah%'
		try{
			if(operator.equals(Operator.IS_EMPTY) || operator.equals(Operator.IS_NOT_EMPTY) ){
				lineAfterFieldName = getHQLForOperator(operator);
			}
			else{
				lineAfterFieldName = getHQLForOperator(operator) + "'" + parseFilterValue(fieldType, value1) + "' ";
				if(operator.equals(Operator.BETWEEN)){
					lineAfterFieldName += (" AND " + "'" + parseFilterValue(fieldType, value2) + "' ");
				}
			}
			return relationship + fieldNameText + lineAfterFieldName;
		}
		catch(ParseException e){
			return "";
		}
	}


	private String makeLineFromOperatorAndValuesWithoutRelationship(String fieldName, Operator operator, FieldType fieldType, String value1, String value2){
		String fieldNameText = fieldType.getName().equalsIgnoreCase(Constants.FIELD_TYPE_LOOKUP)?(fieldName + ".name"):(fieldName); //eg; firstName     //String relationText = relationship; //eg; "lss.person." kinda like saying fully classified table name i guess
		String lineAfterFieldName = "";																								//eg; is not null...... or..... is like 'blah%'
		try{
			if(operator.equals(Operator.IS_EMPTY) || operator.equals(Operator.IS_NOT_EMPTY) ){
				lineAfterFieldName = getHQLForOperator(operator);
			}
			else{
				lineAfterFieldName = getHQLForOperator(operator) + "'" + parseFilterValue(fieldType, value1) + "' ";
				if(operator.equals(Operator.BETWEEN)){
					lineAfterFieldName += (" AND " + "'" + parseFilterValue(fieldType, value2) + "' ");
				}
			}
			return fieldNameText + lineAfterFieldName;
		}
		catch(ParseException e){
			return "";
		}
	}

	/**
	 * dealing with multilevel and many to one and one to many issues
	 * 
	 * 
	 *					
	 * hql document examle 3.3 
	 * from Cat as cat
    							left join cat.kittens as kitten
       	 							with kitten.bodyWeight > 10.0  
	 *					
	 * @param search
	 * @return
	 */
	private String getAddressFilters(Search search) {

		String filterClause = null;
		Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
		for (QueryFilter filter : filters) {
			DemographicField demoField = filter.getDemographicField();
			if ((demoField != null)) {
				if (demoField.getEntity() != null && demoField.getEntity().equals(Entity.Address)) {
					String lineAfterRelationship = makeLineFromOperatorAndValuesWithoutRelationship(demoField.getFieldName(), filter.getOperator(), demoField.getFieldType(), filter.getValue(), filter.getSecondValue());
					if(lineAfterRelationship!=null && !lineAfterRelationship.isEmpty()){

						if(demoField.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_LOOKUP)){

							if (filterClause == null || filterClause.isEmpty()) {
								filterClause = " right join lss.person as person " +
										" right join person.addresses as address " +
										" right join address." + demoField.getFieldName() + " as " + demoField.getFieldName() + " with " + lineAfterRelationship;
							}
							else {//TODO:  does this work with the join???
								//filterClause = filterClause + " and exists address." + nextFilterLine;
								filterClause += " right join address." + demoField.getFieldName() + " as " + demoField.getFieldName() + " with " + lineAfterRelationship;
							}						

						}
						else{
							if (filterClause == null || filterClause.isEmpty()) {
								filterClause = " right join lss.person as person " +
										" right join person.addresses as address with address." + lineAfterRelationship;
							}
							else {//TODO:  does this work with the join???
								filterClause += " and address." + lineAfterRelationship;
							}
						}
					}
				}
			}
		}
		log.info("filterClause = " + filterClause);
		return (filterClause == null ? "" : filterClause);
	}
	
	private String getOtherIDFilters(Search search) {
		String filterClause = null;
		Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
		for (QueryFilter filter : filters) {
			DemographicField demoField = filter.getDemographicField();
			if ((demoField != null)) {
				if (demoField.getEntity() != null && demoField.getEntity().equals(Entity.OtherID)) {				
					String lineAfterRelationship = makeLineFromOperatorAndValuesWithoutRelationship(demoField.getFieldName(), filter.getOperator(), demoField.getFieldType(), filter.getValue(), filter.getSecondValue());
					log.info("lineafterRelationship " + lineAfterRelationship);
					if(lineAfterRelationship!=null && !lineAfterRelationship.isEmpty()){

						if(demoField.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_LOOKUP)){

							if (filterClause == null || filterClause.isEmpty()) {
								filterClause = " right join lss.person as person " +
										" right join person.otherIDs as otherIDs " +
										" right join otherIDs." + demoField.getFieldName() + " as " + demoField.getFieldName() + " with " + lineAfterRelationship;
							}
							else {//TODO:  does this work with the join???
								//filterClause = filterClause + " and exists address." + nextFilterLine;
								filterClause += " right join otherIDs." + demoField.getFieldName() + " as " + demoField.getFieldName() + " with " + lineAfterRelationship;
							}						

						}
						else{
							if (filterClause == null || filterClause.isEmpty()) {
								filterClause = " right join lss.person as person " +
										" right join person.otherIDs as otherIDs with otherIDs." + lineAfterRelationship;
							}
							else {//TODO:  does this work with the join???
								filterClause += " and otherIDs." + lineAfterRelationship;
							}
						}
					}
				}
			}
		}
		log.info("filterClause = " + filterClause);
		return (filterClause == null ? "" : filterClause);
	}

	private String getLSSFilters(Search search, String personFilters) {
		String filterClause = personFilters;
		Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
		for (QueryFilter filter : filters) {
			DemographicField demoField = filter.getDemographicField();
			if ((demoField != null)) {
				if (demoField.getEntity() != null && demoField.getEntity().equals(Entity.LinkSubjectStudy)) {
					filterClause = filterClause + " and ";
					String s = makeLineFromOperatorAndValues("lss.", demoField.getFieldName(), filter.getOperator(), demoField.getFieldType(), filter.getValue(), filter.getSecondValue());
					filterClause = filterClause + s;
				}
			}

		}
		log.info(" filterClauseAfterLSS FILTERS = " + filterClause);
		return (filterClause == null ? "" : filterClause);
	}
	/*
	private String getAddressFilters(Search search, String filterThusFar) {
		String filterClause = filterThusFar;
		Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
		for (QueryFilter filter : filters) {
			DemographicField demoField = filter.getDemographicField();
			if ((demoField != null)) {
				if (demoField.getEntity() != null && demoField.getEntity().equals(Entity.Address)) {
					if(demoField.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_LOOKUP)){

						String nextFilterLine = (demoField.getFieldName() + ".name" + getHQLForOperator(filter.getOperator()) + "'" + parseFilterValue(demoField.getFieldType(), filter.getValue()) + "' ");
						//TODO:  This wouldnt really be a compatible type would it...must do validation very soon.
						if (filter.getOperator().equals(Operator.BETWEEN)) {
							nextFilterLine += (" AND " + "'" + parseFilterValue(demoField.getFieldType(), filter.getSecondValue()) + "' ");
						}
						if (filterClause == null || filterClause.isEmpty()) {
							filterClause = filterClause + " and lss.person.addresses." + nextFilterLine;
						}
						else {
							filterClause = filterClause + " and lss.person.addresses." + nextFilterLine;
						}						

					}
					else{
						String nextFilterLine = (demoField.getFieldName() + getHQLForOperator(filter.getOperator()) + "'" + parseFilterValue(demoField.getFieldType(), filter.getValue()) + "' ");
						if (filter.getOperator().equals(Operator.BETWEEN)) {
							nextFilterLine += (" AND " + "'" + parseFilterValue(demoField.getFieldType(), filter.getSecondValue()) + "' ");
						}
						if (filterClause == null || filterClause.isEmpty()) {
							filterClause = filterClause + " and lss.person.addresses." + nextFilterLine;
						}
						else {
							filterClause = filterClause + " and lss.person.addresses." + nextFilterLine;
						}
					}
				}
			}
		}
		log.info("filterClause = " + filterClause);
		return (filterClause == null ? "" : filterClause);
	}
	 */


	private List<QueryFilter> getBiospecimenQueryFilters(Search search){
		List<QueryFilter> qfs = new ArrayList<QueryFilter>();
		Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
		for (QueryFilter filter : filters) {
			BiospecimenField biospecimenField = filter.getBiospecimenField();
			if ((biospecimenField != null)) {
				if (biospecimenField.getEntity() != null && biospecimenField.getEntity().equals(Entity.Biospecimen)){
					//TODO or biospec custom					|| biospecimenField.getEntity() != null && biospecimenField.getEntity().equals(Entity.)) {
					qfs.add(filter);
				}
			}
		}
		return qfs;
	}

	/**
	 * TODO ASAP
	 * @param search
	 * @return
	 */
	private String getBiospecimenFilters(Search search){//, String filterThusFar) {
		String filterClause = "";// filterThusFar;
		Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
		for (QueryFilter filter : filters) {
			BiospecimenField biospecimenField = filter.getBiospecimenField();
			if ((biospecimenField != null) && filter.getValue() != null) {
				if (biospecimenField.getEntity() != null && biospecimenField.getEntity().equals(Entity.Biospecimen)){
					filterClause += " and " +  makeLineFromOperatorAndValues("biospecimen.", biospecimenField.getFieldName() , filter.getOperator(), biospecimenField.getFieldType(), filter.getValue(), filter.getSecondValue());
				}
				else if (biospecimenField.getEntity() != null && biospecimenField.getEntity().equals(Entity.BioCollection)) {
					filterClause += " and " + makeLineFromOperatorAndValues("biospecimen.bioCollection", biospecimenField.getFieldName() , filter.getOperator(), biospecimenField.getFieldType(), filter.getValue(), filter.getSecondValue());

				}
			}
		}
		log.info("biospecimen filterClause = " + filterClause);
		return filterClause;
	}

	private String getBiocollectionFilters(Search search){//, String filterThusFar) {
			String filterClause = "";// filterThusFar;
			Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
			for (QueryFilter filter : filters) {
				BiocollectionField biocollectionField = filter.getBiocollectionField();
				if ((biocollectionField != null)) {
					String nextLine = makeLineFromOperatorAndValues("biocollection.", biocollectionField.getFieldName(), filter.getOperator(), biocollectionField.getFieldType(), filter.getValue(), filter.getSecondValue());
					if(!nextLine.isEmpty()){
						filterClause = " and " + nextLine;
					}	
				}
			}
			log.info("biocollection filterClause = " + filterClause);
			return filterClause;
	}

	private String parseFilterValue(FieldType fieldType, String value) throws ParseException {
		String parsedValue = null;
		if(fieldType.getName().equalsIgnoreCase("DATE")) {
			String[] dateFormats = { au.org.theark.core.Constants.DD_MM_YYYY, au.org.theark.core.Constants.yyyy_MM_dd_hh_mm_ss_S, "yyyy-MM-dd"};
			try {
				Date date = DateUtils.parseDate(value, dateFormats);
				SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
				parsedValue = (dt1.format(date));
			}
			catch (ParseException e) {
				log.error("exception parsing data " + value);
				throw e;
			}
		}
		else {
			parsedValue = value==null?"":value;
		}
		return parsedValue;
	}

	/**
	 * @param search
	 * @param idsToInclude 
	 * @return a list of subject id's which match the filter we put together.
	 */
	private String getSubjectCustomFieldQuery(Search search) {

		int count = 0;
		String selectComponent = " Select data0.linkSubjectStudy.id ";
		String fromComponent = " from SubjectCustomFieldData data0 ";
		String whereClause = "";
		Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
		for (QueryFilter filter : filters) {
			CustomFieldDisplay customFieldDisplay = filter.getCustomFieldDisplay();
			if ((customFieldDisplay != null) && customFieldDisplay.getCustomField().getArkFunction().getName().equalsIgnoreCase(Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD)) {
				try{
					String tablePrefix = "data" + count++;
					log.info("what is this SUBJECT CUSTOM filter? " + filter.getId() + "     for data row? " + tablePrefix );

					String nextFilterLine =  "";

					// Determine field type and assign key value accordingly    //( data.customFieldDisplay.id=99 AND data.numberDataValue  >  0  )  and ( ( data.customFieldDisplay.id=112 AND data.numberDataValue  >=  0 ) ) 

					//TODO evaluate date entry/validation
					/*if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
						nextFilterLine = (" ( " + tablePrefix + ".customFieldDisplay.id=" + customFieldDisplay.getId() + 
								" AND " + tablePrefix + ".dateDataValue " + getHQLForOperator(filter.getOperator()) + " '" + parseFilterValue(customFieldDisplay.getCustomField().getFieldType(),filter.getValue()) + "' ");
					}
					else if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
						nextFilterLine = (" ( " + tablePrefix + ".customFieldDisplay.id=" + customFieldDisplay.getId() + 
								" AND " + tablePrefix + ".numberDataValue " + getHQLForOperator(filter.getOperator()) + " " + filter.getValue() + " ");
					}
					else if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
						nextFilterLine = (" ( " + tablePrefix + ".customFieldDisplay.id=" + customFieldDisplay.getId() + 
								" AND " + tablePrefix + ".textDataValue " + getHQLForOperator(filter.getOperator()) + " '" + filter.getValue() + "' ");
					}*/
					//TODO evaluate date entry/validation
					if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
						nextFilterLine = (" ( " + tablePrefix + ".customFieldDisplay.id=" + customFieldDisplay.getId() + 
								" AND " + tablePrefix + ".dateDataValue " + createCorrectOpreratorClauseWithOnePassingParameter(filter.getOperator(),parseFilterValue(customFieldDisplay.getCustomField().getFieldType(),filter.getValue())));
					}
					else if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
						nextFilterLine = (" ( " + tablePrefix + ".customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND " + tablePrefix + ".numberDataValue " + createCorrectOpreratorClauseWithOnePassingParameter(filter.getOperator(),filter.getValue()));
					}
					else if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
						nextFilterLine = (" ( " + tablePrefix + ".customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND " + tablePrefix + ".textDataValue " + createCorrectOpreratorClauseWithOnePassingParameter(filter.getOperator() ,filter.getValue()));
					}
					
					else{
						count--;
					}
					//TODO ASAP i think all of these might need to start thinking about is null or is not null?
					if (filter.getOperator().equals(Operator.BETWEEN)) {
						nextFilterLine += (" AND '"+ filter.getSecondValue()+"'");
					}

					if(whereClause.isEmpty()){
						whereClause = " where " + nextFilterLine + " ) ";
					}
					else{
						fromComponent += ",  SubjectCustomFieldData " + tablePrefix ;
						whereClause = whereClause + " and " + nextFilterLine + " )  " +
								" and data0.linkSubjectStudy.id = " + tablePrefix +  ".linkSubjectStudy.id ";
					}
				}
				catch(ParseException e){
					//simply don't apply fornow...in future may wish to throw this up and recommend a fix	return "";
				}
			}
		}
		whereClause += " and data0.linkSubjectStudy.id in (:idList) ";//count>0?"":
		log.info("filterClauseAfterSubjectCustomField FILTERS = " + whereClause);

		if(count>0){
			return selectComponent + fromComponent + whereClause;
		}
		else{
			return "";
		}
	}


	/**
	 * @param search
	 * @param idsToInclude 
	 * @return a query string to attain the updated list of bioscpecimens.
	 */
	private String getBiospecimenDataCustomFieldIdQuery(Search search) {

		int count = 0;
		String selectComponent = " Select data0.biospecimen.id ";
		String fromComponent = " from BiospecimenCustomFieldData data0 ";
		String whereClause = "";
		Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
		for (QueryFilter filter : filters) {
			CustomFieldDisplay customFieldDisplay = filter.getCustomFieldDisplay();
			if ((customFieldDisplay != null) && customFieldDisplay.getCustomField().getArkFunction().getName().equalsIgnoreCase(Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN)) {

				String tablePrefix = "data" + count++;
				log.info("what is this BIOSPECIMEN CUSTOM filter? " + filter.getId() + "     for data row? " + tablePrefix );
				
				String nextFilterLine =  "";

				// Determine field type and assign key value accordingly
				// ( data.customFieldDisplay.id=99 AND data.numberDataValue  >  0  )  and ( ( data.customFieldDisplay.id=112 AND data.numberDataValue  >=  0 ) ) 

				//TODO evaluate date entry/validation
				if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
					nextFilterLine = (" ( " + tablePrefix + ".customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND " + tablePrefix + ".dateDataValue " + getHQLForOperator(filter.getOperator()) + " '" + filter.getValue() + "' ");
				}
				else if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
					nextFilterLine = (" ( " + tablePrefix + ".customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND " + tablePrefix + ".numberDataValue " + getHQLForOperator(filter.getOperator()) + " " + filter.getValue() + " ");
				}
				else if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
					nextFilterLine = (" ( " + tablePrefix + ".customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND " + tablePrefix + ".textDataValue " + getHQLForOperator(filter.getOperator()) + " '" + filter.getValue() + "' ");
				}
				else{
					count--;
				}
				//TODO ASAP i think all of these might need to start thinking about is null or is not null?
				if (filter.getOperator().equals(Operator.BETWEEN)) {
					nextFilterLine += (" AND " + filter.getSecondValue());
				}

				if(whereClause.isEmpty()){
					whereClause = " where " + nextFilterLine + " ) ";
				}
				else{
					fromComponent += ",  BiospecimenCustomFieldData " + tablePrefix ;
					whereClause = whereClause + " and " + nextFilterLine + " )  " +
							" and data0.biospecimen.id = " + tablePrefix +  ".biospecimen.id ";
				}
			}
		}
		whereClause += " and data0.biospecimen.id in (:idList) ";//count>0?"":
		log.info("filterClauseAfterBiospecimenCustomField FILTERS = " + whereClause);

		if(count>0){
			return selectComponent + fromComponent + whereClause;
		}
		else{
			return "";
		}
	}


	/**
	 * @param search
	 * @param idsToInclude 
	 * @return a query string to attain the updated list of biospecimens.
	 */
	private String getBioCollectionDataCustomFieldIdQuery(Search search) {

		int count = 0;
		String selectComponent = " Select data0.bioCollection.id ";
		String fromComponent = " from BioCollectionCustomFieldData data0 ";
		String whereClause = "";
		Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
		for (QueryFilter filter : filters) {
			CustomFieldDisplay customFieldDisplay = filter.getCustomFieldDisplay();
			//if ((customFieldDisplay != null) && customFieldDisplay.getCustomField().getArkFunction().getName().equalsIgnoreCase(Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION)) {
			if ((customFieldDisplay != null) && customFieldDisplay.getCustomField().getArkFunction().getName().equalsIgnoreCase(Constants.FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD)) {
				String tablePrefix = "data" + count++;
				log.info("what is this BIOSPECIMEN CUSTOM filter? " + filter.getId() + "     for data row? " + tablePrefix );

				String nextFilterLine =  "";

				// Determine field type and assign key value accordingly creating something like this;
				// ( data.customFieldDisplay.id=99 AND data.numberDataValue  >  0  )  and ( ( data.customFieldDisplay.id=112 AND data.numberDataValue  >=  0 ) ) 

				//TODO evaluate date entry/validation
				if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
					nextFilterLine = (" ( " + tablePrefix + ".customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND " + tablePrefix + ".dateDataValue " + getHQLForOperator(filter.getOperator()) + " '" + filter.getValue() + "' ");
				}
				else if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
					nextFilterLine = (" ( " + tablePrefix + ".customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND " + tablePrefix + ".numberDataValue " + getHQLForOperator(filter.getOperator()) + " " + filter.getValue() + " ");
				}
				else if (customFieldDisplay.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
					nextFilterLine = (" ( " + tablePrefix + ".customFieldDisplay.id=" + customFieldDisplay.getId() + 
							" AND " + tablePrefix + ".textDataValue " + getHQLForOperator(filter.getOperator()) + " '" + filter.getValue() + "' ");
				}
				else{
					count--;
				}
				//TODO ASAP i think all of these might need to start thinking about is null or is not null?
				if (filter.getOperator().equals(Operator.BETWEEN)) {
					nextFilterLine += (" AND " + filter.getSecondValue());
				}

				if(whereClause.isEmpty()){
					whereClause = " where " + nextFilterLine + " ) ";
				}
				else{
					fromComponent += ",  BioCollectionCustomFieldData " + tablePrefix ;
					whereClause = whereClause + " and " + nextFilterLine + " )  " +
							" and data0.bioCollection.id = " + tablePrefix +  ".bioCollection.id ";
				}
			}
		}
		whereClause += " and data0.bioCollection.id in (:idList) ";//count>0?"":
		log.info("filterClauseAfterBioCollectionCustomField FILTERS = " + whereClause);

		if(count>0){
			return selectComponent + fromComponent + whereClause;
		}
		else{
			return "";
		}
	}

	/**
	 * get pheno filters  FOR THIS ONE CustomFieldGroup aka questionaire (aka data set)
	 * @param search
	 * @param THIS
	 * @return
	 */
	private String getQueryForPhenoIdsForSearchAndCFGFilters(Search search, PhenoDataSetGroup phenoDataSetGroup) {

		int count = 0;
		String selectComponent = " Select data0.phenoDataSetCollection.id ";
		String fromComponent = " from PhenoDataSetData data0 ";
		String whereClause = "";
		Set<QueryFilter> filters = search.getQueryFilters();// or we could run query to just get demographic ones
		for (QueryFilter filter : filters) {
			PhenoDataSetFieldDisplay phenoDataSetFieldDisplay = filter.getPhenoDataSetFieldDisplay();
			if (phenoDataSetFieldDisplay != null){
				if(phenoDataSetFieldDisplay.getPhenoDataSetGroup().equals(phenoDataSetGroup)){
					String tablePrefix = "data" + count++;
					log.info("what is this PHENO CUSTOM filter? " + filter.getId() + "     for data row? " + tablePrefix );

					String nextFilterLine =  "";

					// Determine field type and assign key value accordingly
					// ( data.customFieldDisplay.id=99 AND data.numberDataValue  >  0  )  and ( ( data.customFieldDisplay.id=112 AND data.numberDataValue  >=  0 ) ) 

					//TODO evaluate date entry/validation
					if (phenoDataSetFieldDisplay.getPhenoDataSetField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
						nextFilterLine = (" ( " + tablePrefix + ".phenoDataSetFieldDisplay.id=" + phenoDataSetFieldDisplay.getId() +
								" AND " + tablePrefix + ".dateDataValue " + getHQLForOperator(filter.getOperator()) + " '" + filter.getValue() + "' ");
					}
					else if (phenoDataSetFieldDisplay.getPhenoDataSetField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
						nextFilterLine = (" ( " + tablePrefix + ".phenoDataSetFieldDisplay.id=" + phenoDataSetFieldDisplay.getId() +
								" AND " + tablePrefix + ".numberDataValue " + getHQLForOperator(filter.getOperator()) + " " + filter.getValue() + " ");
					}
					else if (phenoDataSetFieldDisplay.getPhenoDataSetField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
						nextFilterLine = " ( " + tablePrefix + ".phenoDataSetFieldDisplay.id=" + phenoDataSetFieldDisplay.getId() +
								" AND " + tablePrefix + ".textDataValue " + getHQLForOperator(filter.getOperator());
						if(filter.getValue() != null) {
							nextFilterLine += " '" + filter.getValue() + "' ";
						}
					}
					else{ //TODO : if we go for new type of look up does it affect this
						count--;
					}
					//TODO ASAP i think all of these might need to start thinking about is null or is not null?
					if (filter.getOperator().equals(Operator.BETWEEN)) {
						nextFilterLine += (" AND " + filter.getSecondValue());
					}
	
					if(whereClause.isEmpty()){
						whereClause = " where " + nextFilterLine + " ) ";
					}
					else{
						fromComponent += ",  PhenoDataSetData " + tablePrefix ;
						whereClause = whereClause + " and " + nextFilterLine + " )  "
							+ " and data0.phenoDataSetFieldDisplay.id = " + tablePrefix +  ".phenoDataSetFieldDisplay.id ";
					}
				}
			}
		}
		whereClause += " and data0.phenoDataSetCollection.linkSubjectStudy.id in (:idList) ";//count>0?"":
		log.info("filterClauseAfterPhenoCustomField FILTERS = " + whereClause);

		if(count>0){
			log.info("Query> " + selectComponent + fromComponent + whereClause);
			return selectComponent + fromComponent + whereClause;
		}
		else{
			return "";
		}
	}

	/**
	 * 
	 * @param operator
	 * @return the string representing that operator in HQL WITH some white space surrounding it
	 */
	private String getHQLForOperator(Operator operator) {
		switch (operator) {

			case BETWEEN: {
				return " BETWEEN ";
			}
			case EQUAL: {
				return " = ";
			}
			case GREATER_THAN: {
				return " > ";
			}
			case GREATER_THAN_OR_EQUAL: {
				return " >= ";
			}
			case LESS_THAN: {
				return " < ";
			}
			case LESS_THAN_OR_EQUAL: {
				return " <= ";
			}
			case LIKE: {
				return " like ";
			}
			case NOT_EQUAL: {
				return " <> ";
			}
			case IS_EMPTY: {
				return " IS NULL ";
			}
			case IS_NOT_EMPTY: {
				return " IS NOT NULL ";
			}
		}
		return " = ";
	}

	@SuppressWarnings("unchecked")
	public void createQueryFilters(List filterList) throws ArkSystemException {
		List<QueryFilter> queryFilterList = (List<QueryFilter>) filterList;

		log.info("query filter list = " + queryFilterList==null?"0":(""+queryFilterList.size()));
		if (validateQueryFilters(queryFilterList)) {

			for (QueryFilter filter : queryFilterList) {
				getSession().saveOrUpdate(filter);
			}
		}
	}

	/**
	 * 
	 * We want as much of this in wicket as possible (the obvious things like null checks etc will save us a trip to the server I guess)
	 * 
			// TODO ASAP validate type, operator and value are compatible
			// if(filter.getValue()==null) i guess null or empty is valid for
			// string
	 * 
	 * @param queryFilterList - list of all filters we wish to apply
	 * @return
	 * @throws ArkSystemException 
	 */
	public boolean validateQueryFilters(List<QueryFilter> queryFilterList) throws ArkSystemException {

		for (QueryFilter queryfilter: queryFilterList) {

			FieldType ft = getQueryFilterFieldType(queryfilter);
			ft.getName();

			if(ft.getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)){
				// validate date format
				if (queryfilter.getValue() != null) {
					if (!validDateFormat(queryfilter.getValue()))
					{
						throw new ArkSystemException("Value is not a valid date.  Date must be in dd/mm/yyyy format eg; 01/22/2012");
					}
				}
			}

			if (queryfilter.getOperator().equals(Operator.BETWEEN)) {
				//then both values cant be null valueOne and Value2
				//are certain values/fieldstypes valid for this operator?
				//are values needed or should they be ignored?

				if (queryfilter.getValue() == null || queryfilter.getSecondValue() == null) {
					throw new ArkSystemException("Value and Value 2 is required for range filters");
				}

				//if error i guess we return false and give back a list of errors?
			}
			else if (queryfilter.getOperator().equals(Operator.LIKE) || queryfilter.getOperator().equals(Operator.NOT_EQUAL)) {
				//then both values cant be null
				//are certain values/fieldstypes valid for this operator?
				//are values needed or should they be ignored?

				if (queryfilter.getValue() == null) {
					throw new ArkSystemException("A Value is required");
				}
			}
			else if (queryfilter.getOperator().equals(Operator.EQUAL)) {
				//then both values cant be null
				//are certain values/fieldstypes valid for this operator?
				//are values needed or should they be ignored?
				if (queryfilter.getValue() == null) {
					throw new ArkSystemException("A Value is required");
				}

			}
			else if (queryfilter.getOperator().equals(Operator.GREATER_THAN) || queryfilter.getOperator().equals(Operator.GREATER_THAN_OR_EQUAL)) {
				//then both values cant be null
				//are certain values/fieldstypes valid for this operator?
				//are values needed or should they be ignored?

				if (queryfilter.getValue() == null) {
					throw new ArkSystemException("A Value is required");
				}
			}
			else if (queryfilter.getOperator().equals(Operator.LESS_THAN) || queryfilter.getOperator().equals(Operator.LESS_THAN_OR_EQUAL)) {
				//then both values cant be null
				//are certain values/fieldstypes valid for this operator?
				//are values needed or should they be ignored?

				if (queryfilter.getValue() == null) {
					throw new ArkSystemException("A Value is required");
				}
			}
			else{
				log.info("different operator?  that can't happen - can it?  ");
			}

		}

		return true;
	}

	private boolean validDateFormat(String value) {
		String[] formatStrings = {Constants.DD_MM_YYYY};
      boolean isInvalidFormat = false;
      
      for (String formatString : formatStrings) {
          try {
         	 SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getDateInstance();
              sdf.applyPattern(formatString);
              sdf.setLenient(false);
              sdf.parse(value);
              
              if(sdf.format(sdf.parse(value)).equals(value) && value.length() == sdf.toPattern().length()){
            	  isInvalidFormat = true;
              }
          } catch (ParseException e) {
              isInvalidFormat = false;
          }
      }
      return isInvalidFormat;
	}

	/**
	 * Determine the fieldtype for the queryfilter
	 * @param queryfilter
	 * @return fieldType
	 */
	private FieldType getQueryFilterFieldType(QueryFilter queryfilter) {
		DemographicField df = queryfilter.getDemographicField();
		if(df != null) {
			getSession().refresh(df.getFieldType());
			return df.getFieldType();
		}

		BiospecimenField bf = queryfilter.getBiospecimenField();
		if(bf != null) {
			getSession().refresh(bf.getFieldType());
			return bf.getFieldType();
		}

		BiocollectionField bcf = queryfilter.getBiocollectionField();
		if(bcf != null) {
			getSession().refresh(bcf.getFieldType());
			return bcf.getFieldType();
		}
		
		CustomFieldDisplay cfd = queryfilter.getCustomFieldDisplay();
		if(cfd != null) {
			CustomField cf = cfd.getCustomField();
			if(cf !=null) {
				getSession().refresh(cf.getFieldType());
				return cf.getFieldType();
			}
		}

		ConsentStatusField csf = queryfilter.getConsentStatusField();
		if(csf != null) {
			getSession().refresh(csf.getFieldType());
			return csf.getFieldType();
		}

		PhenoDataSetFieldDisplay pdf = queryfilter.getPhenoDataSetFieldDisplay();
		if(pdf != null) {
			PhenoDataSetField field = pdf.getPhenoDataSetField();
			if(field != null) {
				getSession().refresh(field.getFieldType());
				return field.getFieldType();
			}
		}
		
		return null;
	}

	public List<QueryFilterVO> getQueryFilterVOs(Search search) {
		List<QueryFilterVO> filterVOs = new ArrayList<QueryFilterVO>();
		Criteria criteria = getSession().createCriteria(QueryFilter.class);

		if(search !=null && search.getId() !=null) {
			criteria.add(Restrictions.eq("search", search));
			List<QueryFilter> filters = criteria.list();

			for (QueryFilter filter : filters) {
				QueryFilterVO filterVO = new QueryFilterVO();
				filterVO.setQueryFilter(filter);
				if (filter.getDemographicField() != null) {
					filterVO.setFieldCategory(FieldCategory.DEMOGRAPHIC_FIELD);
				}
				else if (filter.getBiocollectionField() != null) {
					filterVO.setFieldCategory(FieldCategory.BIOCOLLECTION_FIELD);
				}
				else if (filter.getBiospecimenField() != null) {
					filterVO.setFieldCategory(FieldCategory.BIOSPECIMEN_FIELD);
				}
				else if (filter.getCustomFieldDisplay() != null) {
					filterVO.setFieldCategory(getFieldCategoryFor(filter.getCustomFieldDisplay().getCustomField().getArkFunction()));
				}
				else if (filter.getConsentStatusField() != null) {
					filterVO.setFieldCategory(FieldCategory.CONSENT_STATUS_FIELD);
				}
				filterVOs.add(filterVO);
			}
		}
		return filterVOs;
	}

	private FieldCategory getFieldCategoryFor(ArkFunction arkFunction) {
		if (arkFunction.getName().equalsIgnoreCase(Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD)) {
			return FieldCategory.SUBJECT_CFD;
		}
		else if (arkFunction.getName().equalsIgnoreCase(Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY)) {
			return FieldCategory.PHENO_FD;
		}
		//else if (arkFunction.getName().equalsIgnoreCase(Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION)) {
		else if (arkFunction.getName().equalsIgnoreCase(Constants.FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD)) {
			return FieldCategory.BIOCOLLECTION_CFD;
		}
		else {// should really have a default! TODO
			return FieldCategory.BIOSPECIMEN_CFD;
		}
	}

	public void deleteQueryFilter(QueryFilter queryFilter) {
		if(queryFilter != null) {
			for(SearchResult result : getSearchResultList(queryFilter.getSearch().getId())) {
				getSession().delete(result);
			}
			getSession().delete(queryFilter);
		}
	}

	public List<Study> getParentAndChildStudies(Long id) {
		Criteria studyCriteria = getSession().createCriteria(Study.class);
		Study study = getStudy(id);

		if (study.getStudyStatus() != null) {
			studyCriteria.add(Restrictions.eq(Constants.STUDY_STATUS, study.getStudyStatus()));
			try {
				StudyStatus status = getStudyStatus("Archive");
				studyCriteria.add(Restrictions.ne(Constants.STUDY_STATUS, status));
			}
			catch (StatusNotAvailableException notAvailable) {
				log.error("Cannot look up and filter on archive status. Reference data could be missing");
			}
		}
		else {
			try {
				StudyStatus status = getStudyStatus("Archive");
				studyCriteria.add(Restrictions.ne(Constants.STUDY_STATUS, status));
			}
			catch (StatusNotAvailableException notAvailable) {
				log.error("Cannot look up and filter on archive status. Reference data could be missing");
			}

		}

		if(study.getParentStudy() != null && !study.getParentStudy().equals(study)) {
			studyCriteria.add(Restrictions.or(Restrictions.idEq(id), Restrictions.eq("parentStudy", study.getParentStudy())));
		}
		else {
			studyCriteria.add(Restrictions.or(Restrictions.idEq(id), Restrictions.eq("parentStudy", study)));
		}

		studyCriteria.addOrder(Order.asc("id"));	
		studyCriteria.addOrder(Order.asc(Constants.STUDY_NAME));
		return studyCriteria.list();
	}	


	/**
	 * For now this is just forcing all the fields into one new file listing the geno info, given that I believe this whole model will change to include real genetic 
	 * analysis going forward, rather than just discussing WHERE the data is and WHAT was done to it.  For now they get all of that info 
	 * 
	 * @param allTheData
	 * @param search
	 * @param idsAfterFiltering
	 * @param maxProcessesPerPipeline 
	 */
	private Long addGenoData(DataExtractionVO allTheData, Search search, List<Long> idsAfterFiltering,
						Map<Long, Long> maxInputList, Map<Long, Long> maxOutputList, Long maxProcessesPerPipeline) {
		log.info("idsAfterFiltering" + idsAfterFiltering);
		
		if (!idsAfterFiltering.isEmpty()) {
			//note.  filtering is happening previously...we then do the fetch when we have narrowed down the list of subjects to save a lot of processing
			String queryString = "select lssp from LinkSubjectStudyPipeline lssp " +
					" where lssp.linkSubjectStudy.id in (:idsToInclude) " // stoing this to an lss means we should fetch lss and pipeline..and process
					+ " order by lssp.linkSubjectStudy.id ";

			Query query = getSession().createQuery(queryString);
			query.setParameterList("idsToInclude", idsAfterFiltering);
			List<LinkSubjectStudyPipeline> subjectPipelines = query.list();

			List<LinkedExtractionVO> allGenoData = allTheData.getGenoData();
			log.info("count=" + ((subjectPipelines==null)?"0":subjectPipelines.size()));

			/* this is putting the data we extracted into a generic kind of VO doc that will be converted to an appopriate format later (such as csv/xls/pdf/xml/etc) */
			for (LinkSubjectStudyPipeline lssp : subjectPipelines) { 	
				log.info("adding geno info for lss= " + lssp.getLinkSubjectStudy().getId());
				LinkedExtractionVO sev = new LinkedExtractionVO();
// todo with geno in some way				sev.setKeyValues(constructKeyValueHashmap(lss, personFields, lssFields, addressFields, phoneFields));
				LinkedHashMap map = new LinkedHashMap<String, String>();
				sev.setSubjectUid(lssp.getLinkSubjectStudy().getSubjectUID());
				/*
				 * 
	public static final String GENO_FIELDS_PIPELINE_ID = "pipelineId";
	public static final String GENO_FIELDS_PIPELINE_NAME = "pipelineName";
	public static final String GENO_FIELDS_PIPELINE_DECSRIPTION = "pipelineDescription";
	public static final String GENO_FIELDS_PROCESS_ID = "processId";
	public static final String GENO_FIELDS_PROCESS_NAME = "processName";
	public static final String GENO_FIELDS_PROCESS_DESCRIPTION = "processDescription";
	public static final String GENO_FIELDS_PROCESS_START_TIME = "startTime";
	public static final String GENO_FIELDS_PROCESS_END_TIME = "endTime";
	public static final String GENO_FIELDS_PROCESS_COMMAND_SERVER_URL = "commandServerUrl";
	public static final String GENO_FIELDS_PROCESS_COMMAND_NAME = "commandName";
	public static final String GENO_FIELDS_PROCESS_COMMAND_LOCATION = "commandLocation";
//	public static final String GENO_FIELDS_PROCESS_COMMAND_INPUT_FILE_FORMAT;
//	public static final String GENO_FIELDS_PROCESS_COMMAND_OUTPUT_FILE_FORMAT;
	public static final String GENO_FIELDS_PROCESS_INPUT_SERVER = "inputServer";
	public static final String GENO_FIELDS_PROCESS_INPUT_LOCATION = "inputLocation";
	public static final String GENO_FIELDS_PROCESS_INPUT_FILE_HASH = "inputFileHash";
	public static final String GENO_FIELDS_PROCESS_INPUT_FILE_TYPE = "inputFileType";
	public static final String GENO_FIELDS_PROCESS_INPUT_KEPT = "outputKept";
	public static final String GENO_FIELDS_PROCESS_OUTPUT_SERVER = "outputServer";
	public static final String GENO_FIELDS_PROCESS_OUTPUT_LOCATION = "outputLocation";
	public static final String GENO_FIELDS_PROCESS_OUTPUT_FILE_HASH = "outputFileHash";
	public static final String GENO_FIELDS_PROCESS_OUTPUT_FILE_TYPE = "outputFileType";
	public static final String GENO_FIELDS_PROCESS_OUTPUT_KEPT = "outputKept";*/
				
				//TODO : NULL CHECK EVERY SINGLE APPROPRIATE PLACE
				
				//TODO ASAP : change this to do all fields in a precise order (possibly defined somewhere common)
				Pipeline pl = lssp.getPipeline();
				map.put(Constants.GENO_FIELDS_PIPELINE_ID, pl.getId().toString());
				map.put(Constants.GENO_FIELDS_PIPELINE_NAME, pl.getName());
				map.put(Constants.GENO_FIELDS_PIPELINE_DECSRIPTION, pl.getDescription());
				
				long processIndex = 0L;
				
				
				log.info("we have process..." + pl.getPipelineProcesses().size());
				for(Process p : pl.getPipelineProcesses()){
					processIndex++;
					if(processIndex >= maxProcessesPerPipeline){
						log.info("processIndex  maxProcessesPerPipeline = " + processIndex + "  " + maxProcessesPerPipeline);
						maxProcessesPerPipeline = Long.valueOf(processIndex);
					} 
					else{
						log.info("processIndex  maxProcessesPerPipeline = " + processIndex + "  " + maxProcessesPerPipeline);
					}
					
					//TODO : obvbiously need to pre=append the pipeline info/count too
					map.put((Constants.GENO_FIELDS_PROCESS_ID + (processIndex>1?("_"+processIndex):"")), p.getId().toString());
					map.put((Constants.GENO_FIELDS_PROCESS_NAME + (processIndex>1?("_"+processIndex):"")), p.getName());
					map.put((Constants.GENO_FIELDS_PROCESS_DESCRIPTION + (processIndex>1?("_"+processIndex):"")), p.getDescription());
					map.put((Constants.GENO_FIELDS_PROCESS_START_TIME + (processIndex>1?("_"+processIndex):"")), p.getStartTime()!=null?p.getStartTime().toLocaleString():"");
					map.put((Constants.GENO_FIELDS_PROCESS_END_TIME + (processIndex>1?("_"+processIndex):"")), p.getEndTime()!=null?p.getEndTime().toLocaleString():"");
					Command command = p.getCommand();
					map.put((Constants.GENO_FIELDS_PROCESS_COMMAND_NAME + (processIndex>1?("_"+processIndex):"")), (command==null?"":command.getName()));
					map.put((Constants.GENO_FIELDS_PROCESS_COMMAND_LOCATION + (processIndex>1?("_"+processIndex):"")), (command==null?"":command.getLocation()));
					map.put((Constants.GENO_FIELDS_PROCESS_COMMAND_SERVER_URL + (processIndex>1?("_"+processIndex):"")), (command==null?"":command.getServerUrl()));
					//map.put((Constants.GENO_FIELDS_PROCESS_COMMAND_LOCATION + (index>1?("_"+index):"")), (command==null?"":command.getName()));//space keeper for file format info
					//map.put((Constants.GENO_FIELDS_PROCESS_COMMAND_LOCATION + (index>1?("_"+index):"")), (command==null?"":command.getName()));
					
					Set<ProcessInput> inputs = p.getProcessInputs();
					long inputIndex = 0L;
					for(ProcessInput input : inputs){
						inputIndex++;
						map.put((Constants.GENO_FIELDS_PROCESS_INPUT_SERVER + "_" + processIndex + "_" + inputIndex ) , (input==null?"":input.getInputServer()));
						map.put((Constants.GENO_FIELDS_PROCESS_INPUT_LOCATION + "_"  + processIndex + "_" + inputIndex ), (input==null?"":input.getinputFileLocation()));
						map.put((Constants.GENO_FIELDS_PROCESS_INPUT_FILE_HASH + "_"  + processIndex + "_" + inputIndex ), (input==null?"":input.getInputFileHash()));
						map.put((Constants.GENO_FIELDS_PROCESS_INPUT_FILE_TYPE + "_"  + processIndex + "_" + inputIndex ), (input==null?"":input.getInputFileType()));
						map.put((Constants.GENO_FIELDS_PROCESS_INPUT_KEPT + "_"  + processIndex + "_" + inputIndex ), (input==null?"":(""+input.getInputKept())));
						//TODO ASAP : now put all the input info in with a similar _<index> suffix
					}

					long maxInputCurrent = (maxInputList.get(processIndex)==null)?0L:maxInputList.get(processIndex);//get the procesIndex'th max input and see if it is bigger than 
					maxInputList.put(processIndex, (maxInputCurrent>inputIndex)?maxInputCurrent:inputIndex);
					
					long outputIndex = 0L;
					Set<ProcessOutput> outputs = p.getProcessOutputs();
					for(ProcessOutput output : outputs){
						outputIndex++;//TODO ASAP : now put all the output info in with a similar _<index> suffix
						map.put((Constants.GENO_FIELDS_PROCESS_OUTPUT_SERVER + "_"  + processIndex + "_" + outputIndex ) , (output==null?"":output.getOutputServer()));
						map.put((Constants.GENO_FIELDS_PROCESS_OUTPUT_LOCATION + "_" + processIndex + "_" + outputIndex ), (output==null?"":output.getOutputFileLocation()));
						map.put((Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_HASH + "_" + processIndex + "_" + outputIndex ), (output==null?"":output.getOutputFileHash()));
						map.put((Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_TYPE + "_" + processIndex + "_" + outputIndex ), (output==null?"":output.getOutputFileType()));
						map.put((Constants.GENO_FIELDS_PROCESS_OUTPUT_KEPT + "_" + processIndex + "_" + outputIndex ), (output==null?"":(""+output.getOutputKept())));						
					}

					long maxOutputCurrent = (maxOutputList.get(processIndex)==null)?0L:maxOutputList.get(processIndex);//get the procesOutdex'th max output and see if it is bigger than 
					maxOutputList.put(processIndex, (maxOutputCurrent>outputIndex)?maxOutputCurrent:outputIndex);
					
					
					/*
					public static final String GENO_FIELDS_PROCESS_INPUT_SERVER = "inputServer";
					public static final String GENO_FIELDS_PROCESS_INPUT_LOCATION = "inputLocation";
					public static final String GENO_FIELDS_PROCESS_INPUT_FILE_HASH = "inputFileHash";
					public static final String GENO_FIELDS_PROCESS_INPUT_FILE_TYPE = "inputFileType";
					public static final String GENO_FIELDS_PROCESS_INPUT_KEPT = "outputKept";
					public static final String GENO_FIELDS_PROCESS_OUTPUT_SERVER = "outputServer";
					public static final String GENO_FIELDS_PROCESS_OUTPUT_LOCATION = "outputLocation";
					public static final String GENO_FIELDS_PROCESS_OUTPUT_FILE_HASH = "outputFileHash";
					public static final String GENO_FIELDS_PROCESS_OUTPUT_FILE_TYPE = "outputFileType";
					public static final String GENO_FIELDS_PROCESS_OUTPUT_KEPT = "outputKept";
					*/
					//map.put((Constants.GENO_FIELDS_PROCESS_INPUT_KEPT + (index>1?("_"+index):"")), p.getId());
				}
				sev.setKeyValues(map);
				allGenoData.add(sev);
			}

		}
		return maxProcessesPerPipeline;
	}
	

	/**
	 * 
	 * 
	 * @param allTheData
	 * @param personFields
	 * @param lssFields
	 * @param addressFields
	 * @param phoneFields
	 * @param otherIDFields
	 * @param subjectCFDs
	 * @param search
	 * @param idsAfterFiltering
	 */
	private void addDataFromMegaDemographicQuery(DataExtractionVO allTheData, Collection<DemographicField> personFields, Collection<DemographicField> lssFields,
			Collection<DemographicField> addressFields, Collection<DemographicField> phoneFields, Collection<DemographicField> otherIDFields, 
			Collection<DemographicField> linkSubjectTwinsFields,Collection<CustomFieldDisplay> subjectCFDs, Search search, List<Long> idsAfterFiltering) {
		log.info("in addDataFromMegaDemographicQuery");																						//if no id's, no need to run this
		if ((!lssFields.isEmpty() || !personFields.isEmpty() || !addressFields.isEmpty() || !phoneFields.isEmpty() || !linkSubjectTwinsFields.isEmpty() ||
				!subjectCFDs.isEmpty()) && !idsAfterFiltering.isEmpty()) { // hasEmailFields(dfs)
			//note.  filtering is happening previously...we then do the fetch when we have narrowed down the list of subjects to save a lot of processing
			String queryString = "select distinct lss " // , address, lss, email " +
					+ " from LinkSubjectStudy lss " 
					+ ((!personFields.isEmpty()) ? " left join fetch lss.person person " : "") 
					+ ((!addressFields.isEmpty()) ? " left join lss.person.addresses a " : "")
					+ ((!phoneFields.isEmpty()) ? " left join lss.person.phones p " : "")
					+ ((!linkSubjectTwinsFields.isEmpty()) ? " left join lss.linkSubjectTwinsAsFirstSubject lstAsFirst  " : "")
					+ ((!linkSubjectTwinsFields.isEmpty()) ? " left join lss.linkSubjectTwinsAsSecondSubject lstAsSecond  " : "")
					+ " where lss.study.id = " + search.getStudy().getId()
					+ " and lss.id in (:idsToInclude) "
					+ " order by lss.subjectUID";

			Query query = getSession().createQuery(queryString);
			query.setParameterList("idsToInclude", idsAfterFiltering);
			List<LinkSubjectStudy> subjects = query.list();

			QueryTranslatorFactory translatorFactory = new ASTQueryTranslatorFactory();
			SessionFactoryImplementor factory = (SessionFactoryImplementor) getSession().getSessionFactory();
			QueryTranslator translator = translatorFactory.
					createQueryTranslator(query.getQueryString(), query.getQueryString(), Collections.EMPTY_MAP, factory);
			translator.compile(Collections.EMPTY_MAP, false);
			log.info(translator.getSQLString());

			// DataExtractionVO devo; = new DataExtractionVO();
			HashMap<String, ExtractionVO> hashOfSubjectsWithTheirDemographicData = allTheData.getDemographicData();

			/* this is putting the data we extracted into a generic kind of VO doc that will be converted to an appopriate format later (such as csv/xls/pdf/xml/etc) */
			for (LinkSubjectStudy lss : subjects) {
				ExtractionVO sev = new ExtractionVO();
				sev.setKeyValues(constructKeyValueHashmap(lss, personFields, lssFields, addressFields, phoneFields, otherIDFields,linkSubjectTwinsFields));
				hashOfSubjectsWithTheirDemographicData.put(lss.getSubjectUID(), sev);
			}

		}
	}
	
	
	private List<Long> addDataFromMegaBiocollectionQuery(DataExtractionVO allTheData,Collection<BiocollectionField> biocollectionFields,Collection<CustomFieldDisplay> collectionCFDs,
			Search search, List<Long> idsToInclude, List<Long> biocollectionIdsAfterFiltering ){
		String bioCollectionFilters = getBiocollectionFilters(search);
		
		Collection<BioCollection> bioCollectionList = Collections.EMPTY_LIST;
		
		if(biocollectionFields.isEmpty() && bioCollectionFilters.isEmpty() ) {
			if(idsToInclude.isEmpty()) {
				// no need - skip querying
			}
			else {
				biocollectionIdsAfterFiltering = getBioCollectionIdForSubjectIds(idsToInclude);
				if(! biocollectionIdsAfterFiltering.isEmpty()){
					bioCollectionList = getSession().createCriteria(BioCollection.class).add(Restrictions.in("id", biocollectionIdsAfterFiltering)).list();
				}
			}
		}
		
		if(!idsToInclude.isEmpty() &&  biocollectionIdsAfterFiltering.isEmpty() && 
				(!bioCollectionFilters.isEmpty() || !biocollectionFields.isEmpty())){
			
			StringBuffer queryBuffer =new StringBuffer("select distinct biocollection ");
			queryBuffer.append("from BioCollection biocollection " );	//	TODO:  improve preformance by prefetch
			queryBuffer.append(	" where biocollection.study.id = " + search.getStudy().getId());
			
			if(!bioCollectionFilters.isEmpty()){
				queryBuffer.append(bioCollectionFilters);
			}
			queryBuffer.append( "  and biocollection.linkSubjectStudy.id in (:idsToInclude) ");
			
			Query query = getSession().createQuery(queryBuffer.toString());
			query.setParameterList("idsToInclude", idsToInclude);
			bioCollectionList = query.list();
		}
		HashSet uniqueSubjectIDs = new HashSet<Long>();
		HashMap<String, ExtractionVO> hashOfBioCollectionData = allTheData.getBiocollectionData();
		
		for (BioCollection bioCollection : bioCollectionList) {
			ExtractionVO sev = new ExtractionVO();
			sev.setKeyValues(constructKeyValueHashmap(bioCollection,biocollectionFields));
			hashOfBioCollectionData.put(bioCollection.getBiocollectionUid(), sev);
			uniqueSubjectIDs.add(bioCollection.getLinkSubjectStudy().getId());
			sev.setSubjectUid(bioCollection.getLinkSubjectStudy().getSubjectUID()); //TODO: mow that we haevb this probably need to fetch join to save us a bunch of hits to the db
			biocollectionIdsAfterFiltering.add(bioCollection.getId());
		}			
		
		//maintaining list of subject IDs for filtering past results
		if(!bioCollectionFilters.isEmpty()) {
			idsToInclude = new ArrayList(uniqueSubjectIDs);
		}
		return biocollectionIdsAfterFiltering;
	}


	private List<Long> addDataFromMegaBiospecimenQuery(DataExtractionVO allTheData,Collection<BiospecimenField> biospecimenFields, //Collection<CustomFieldDisplay> specimenCFDs, 
			Search search, List<Long> idsToInclude, List<Long> biospecimenIdsAfterFiltering, List<Long> bioCollectionIdsAfterFiltering){
		
		String biospecimenFilters = getBiospecimenFilters(search);

		HashMap<String, ExtractionVO> hashOfBiospecimenData = allTheData.getBiospecimenData();
		
		Collection<Biospecimen> biospecimenList = null;
		
		if((biospecimenFields.isEmpty() && biospecimenFilters.isEmpty())){
			if(idsToInclude.isEmpty()) {
				// no need
			}
			else {
				biospecimenIdsAfterFiltering = getBiospecimenIdForSubjectIds(idsToInclude);
				if(biospecimenIdsAfterFiltering.isEmpty()){
					return Collections.EMPTY_LIST;
				}
				else{
					biospecimenList = getSession().createCriteria(Biospecimen.class).add(Restrictions.in("id", biospecimenIdsAfterFiltering)).list();
				}
			}
		}
		else if((!biospecimenFields.isEmpty() || !biospecimenFilters.isEmpty()) && !idsToInclude.isEmpty()){
			
			StringBuffer queryBuffer =new StringBuffer("select distinct biospecimen ");
			queryBuffer.append("from Biospecimen biospecimen " );
			queryBuffer.append(	" 	left join fetch biospecimen.sampleType sampleType ");
			queryBuffer.append(	"	left join fetch biospecimen.invCell invCell " );	//Not lookup compatible
			queryBuffer.append(	"	left join fetch biospecimen.storedIn storedIn " );
			queryBuffer.append(	"	left join fetch biospecimen.grade grade " );
			queryBuffer.append(	"	left join fetch biospecimen.species species " );
			queryBuffer.append(	"	left join fetch biospecimen.unit unit " );
			queryBuffer.append(	"	left join fetch biospecimen.treatmentType treatmentType ");
			queryBuffer.append(	"	left join fetch biospecimen.quality quality ");
			queryBuffer.append(	"	left join fetch biospecimen.anticoag anticoag ");
			queryBuffer.append(	"	left join fetch biospecimen.status status " );
			queryBuffer.append(	"	left join fetch biospecimen.biospecimenProtocol biospecimenProtocol ");
			queryBuffer.append(	"	left join fetch biospecimen.bioCollection biocollection ");
			queryBuffer.append(	" where biospecimen.study.id = " + search.getStudy().getId());
			if(!biospecimenFilters.isEmpty()){
				queryBuffer.append(biospecimenFilters);
			}
				
			queryBuffer.append( "  and biospecimen.linkSubjectStudy.id in (:idsToInclude) ");
			
			if(!bioCollectionIdsAfterFiltering.isEmpty()){
				queryBuffer.append( "  and biospecimen.bioCollection.id in (:biocollectionsToFilter) ");
			}
			else {
				biospecimenIdsAfterFiltering = new ArrayList<Long>();
				return new ArrayList<Long>();
			}

			Query query = getSession().createQuery(queryBuffer.toString());
			query.setParameterList("idsToInclude", idsToInclude);
			if(!bioCollectionIdsAfterFiltering.isEmpty()){
				query.setParameterList("biocollectionsToFilter", bioCollectionIdsAfterFiltering);
			}
			
			biospecimenList = query.list();
		}
		HashSet uniqueSubjectIDs = new HashSet<Long>();
		for (Biospecimen biospecimen : biospecimenList) {
			ExtractionVO sev = new ExtractionVO();
			sev.setKeyValues(constructKeyValueHashmap(biospecimen,biospecimenFields));
			sev.setSubjectUid(biospecimen.getLinkSubjectStudy().getSubjectUID());
			hashOfBiospecimenData.put(biospecimen.getBiospecimenUid(), sev);
			uniqueSubjectIDs.add(biospecimen.getLinkSubjectStudy().getId());
			biospecimenIdsAfterFiltering.add(biospecimen.getId());
		}			
		
		//maintaining list of subject IDs for filtering past results
		if(!biospecimenFilters.isEmpty()){
			
			idsToInclude.clear();
			for(Object id : uniqueSubjectIDs){
				idsToInclude.add((Long)id);
			}
			log.info("LATEST LIST OF IDS SIZE=" + idsToInclude.size());
		}
		allTheData.setBiospecimenData(hashOfBiospecimenData);//wouldnt think I need to set ht
		//log.info("addDataFromMegaBiospecimenQuery.biospecimenIdsAfterFiltering: " + biospecimenIdsAfterFiltering.size());
		return biospecimenIdsAfterFiltering;
	}

	private void prettyLoggingOfWhatIsInOurMegaObject(HashMap<String, ExtractionVO> hashOfSubjectsWithData, FieldCategory fieldCategory) {
		log.info(" we have " + hashOfSubjectsWithData.size() + " entries for category '" + fieldCategory + "'");
		for (String subjectUID : hashOfSubjectsWithData.keySet()) {
			HashMap<String, String> keyValues = hashOfSubjectsWithData.get(subjectUID).getKeyValues();
			// log.info(subjectUID + " has " + keyValues.size() + " " + fieldCategory + " fields"); 
			// remove(subjectUID).getKeyValues().size() + "demo fields");
			//for (String key : keyValues.keySet()) {
				//log.info("     key=" + key + "\t   value=" + keyValues.get(key));
			//}
		}
	}

	public SearchPayload createSearchPayload(byte[] bytes) {
		SearchPayload payload = new SearchPayload(bytes);
		getSession().save(payload);
		getSession().flush();
		getSession().refresh(payload);
		return payload;
	}
	
	public SearchPayload getSearchPayloadForSearchResult(SearchResult searchResult) {
		getSession().refresh(searchResult);
		return searchResult.getSearchPayload();
	}

	public List<SearchResult> getSearchResultList(Long searchResultId) {
		Criteria criteria = getSession().createCriteria(SearchResult.class);
		criteria.add(Restrictions.eq("search.id",searchResultId));
		return criteria.list();
	}
	
	public void createSearchResult(SearchResult searchResult) {
		getSession().saveOrUpdate(searchResult);
	}
	
	public void createSearchResult(Search search, File file, String currentUser) {
		try {
			log.info("what file?" + file.getName());
			SearchResult sr = new SearchResult();
			sr.setSearch(search);
			sr.setFilename(file.getName());
			String fileFormatName = file.getName().substring(file.getName().lastIndexOf('.') + 1).toUpperCase();
			sr.setFileFormat(getFileFormatByName(fileFormatName));
			sr.setStartTime(new Date(System.currentTimeMillis()));
			sr.setDelimiterType(getDelimiterTypeByDelimiterChar(','));
			byte[] bytes = org.apache.commons.io.FileUtils.readFileToByteArray(file);
			sr.setChecksum(DigestUtils.md5DigestAsHex(bytes));
			sr.setSearchPayload(createSearchPayload(bytes));
			sr.setUserId(currentUser);
			createSearchResult(sr);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteSearchResult(SearchResult searchResult) {
		getSession().delete(searchResult);
	}
	
	public static String getHex(byte[] raw) {
		if (raw == null) {
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		for (final byte b : raw) {
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}
	
	public List<Relationship> getFamilyRelationships() {
		Criteria criteria = getSession().createCriteria(Relationship.class);
		return criteria.list();
	}
	
	public List<SearchSubject> getSearchSubjects() {
		Criteria criteria = getSession().createCriteria(SearchSubject.class);
		return criteria.list();
	}
	
	public List<Long> getSubjectIdsforSearch(Search search) {
		Criteria criteria = getSession().createCriteria(SearchSubject.class);
		criteria.add(Restrictions.eq("search", search));
		criteria.setProjection(Projections.property("linkSubjectStudy.id"));
		return criteria.list();
	}
	
	public void createSearchSubjects(Search search, List<SearchSubject> searchSubjects) {
		Criteria criteria = getSession().createCriteria(SearchSubject.class);
		criteria.add(Restrictions.eq("search", search));
		List<SearchSubject> searchResults = criteria.list();
		for (SearchSubject searchSubject : searchResults) {
			deleteSearchSubject(searchSubject);
		}
		
		for (Iterator iterator = searchSubjects.iterator(); iterator.hasNext();) {
			SearchSubject searchSubject = (SearchSubject) iterator.next();
			getSession().save(searchSubject);
		}
	}

	private void deleteSearchSubject(SearchSubject searchSubject) {
		getSession().delete(searchSubject);
	}

	public void delete(Search search) {
		getSession().delete(search);
	}

	public void delete(SearchResult result) {
		getSession().delete(result);
	}

	public ConsentStatus getConsentStatusByName(String name) {
		Criteria criteria = getSession().createCriteria(ConsentStatus.class);
		criteria.add(Restrictions.eq("name", name));
		return (ConsentStatus) criteria.uniqueResult();
	}
	
	public GenderType getSubjectGenderType(final String subjectUID,final Long studyId){
		GenderType genderType=null;
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class,"lss");
		criteria.createAlias("study","st",JoinType.INNER_JOIN);
		criteria.createAlias("person","per",JoinType.INNER_JOIN);
		criteria.createAlias("per.genderType","gen",JoinType.INNER_JOIN);
		
		criteria.setFetchMode("person", FetchMode.JOIN);
		
		criteria.add(Restrictions.eq("st.id", studyId));
		criteria.add(Restrictions.eq("lss.subjectUID", subjectUID));
		
		List list = criteria.list();
		if(list.size()>0){
			LinkSubjectStudy subject = (LinkSubjectStudy)list.get(0);
			genderType = subject.getPerson().getGenderType();
		}
		return genderType;
	}

	public List<OtherID> getOtherIDs(Person person) {
		if(person != null) {
			Criteria c = getSession().createCriteria(OtherID.class);
			c.setMaxResults(100);
			c.add(Restrictions.eq("person", person));
			List<OtherID> results = c.list();
			return results;
		} else {
			return new ArrayList<OtherID>();
		}

	}

	public Collection<PersonLastnameHistory> getPersonLastnameHistory(Person person) {
		Criteria c = getSession().createCriteria(PersonLastnameHistory.class);
		c.setMaxResults(100);
		c.add(Restrictions.eq("person", person));
		Collection<PersonLastnameHistory> results = c.list();
		return results;
	}
	
	public void createUserConfigs(List<UserConfig> userConfigList) throws ArkSystemException {
		
		for(UserConfig uc : userConfigList) {
			getSession().saveOrUpdate(uc);
		}
			
	}
			
	public List<ConfigField> getAllConfigFields() {
		Criteria criteria = getSession().createCriteria(ConfigField.class);
		final List<ConfigField> configFields = criteria.list();
		return configFields;
	}
	
	public List<UserConfig> getUserConfigs(ArkUser arkUser) {
		List<UserConfig> userConfigs = new ArrayList<UserConfig>();
		Criteria criteria = getSession().createCriteria(UserConfig.class);
		log.info("arkuser: " + arkUser);
		log.info("arkuser.id: " + arkUser.getId());
		if(arkUser != null && arkUser.getId() != null) {
			criteria.add(Restrictions.eq("arkUser", arkUser));
			userConfigs = criteria.list();
			log.info("userconfs.size: " + userConfigs.size());
		}
		return userConfigs;
	}
	
	@Override
	public UserConfig getUserConfig(ArkUser arkUser, ConfigField configField) {
		Criteria criteria = getSession().createCriteria(UserConfig.class);
		if(arkUser != null && arkUser.getId() != null) {
			criteria.add(Restrictions.eq("arkUser",  arkUser));
		}
		if(configField != null && configField.getId() != null) {
			criteria.add(Restrictions.eq("configField", configField));
		}
		UserConfig userConfig = null;
		try {
			userConfig = (UserConfig) criteria.uniqueResult();
		} catch (HibernateException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			userConfig = new UserConfig();
			userConfig.setArkUser(arkUser);
			userConfig.setConfigField(configField);
//			userConfig.setValue(configField.getDefaultValue());
		}
		return userConfig;
	}	
	
	public ConfigField getConfigFieldByName(String configField) {
		Criteria criteria = getSession().createCriteria(ConfigField.class);
		criteria.add(Restrictions.eq("name", configField));
		return (ConfigField) criteria.uniqueResult();
	}
		
	public void deleteUserConfig(UserConfig uc) {
		if(uc != null) {
			getSession().delete(uc);
		}
	}

	@Override
	public List<Study> getChildStudiesForStudy(Study parentStudy) {
		Criteria criteria = getSession().createCriteria(Study.class);
		criteria.add(Restrictions.eq("parentStudy", parentStudy));
		List<Study> childStudies = criteria.list();
		return childStudies;
	}
	@Override
	public void createCustomFieldCategoryUpload(CustomFieldCategoryUpload cfcUpload) {
		getSession().save(cfcUpload);
	}
	@Override
	public List<String> getAllFamilyUIDs(Study study) {
		Criteria criteria = getSession().createCriteria(FamilyCustomFieldData.class);
		Set<String>  familyUIDSet=new HashSet<String>();
		criteria.add(Restrictions.eq("study", study));
		List<FamilyCustomFieldData> familyCustomFieldDatalst = criteria.list();
		for (FamilyCustomFieldData familyCustomFieldData : familyCustomFieldDatalst) {
			familyUIDSet.add(familyCustomFieldData.getFamilyUid());
		}
		List<String> familyUIDLst=new ArrayList<String>(familyUIDSet);
		return  familyUIDLst;
	}

	@Override
	public List<FamilyCustomFieldData> getfamilyCustomFieldDataFor(Study study,Collection customFieldDisplaysThatWeNeed,List familyUidsToBeIncluded) {
		if (customFieldDisplaysThatWeNeed == null || customFieldDisplaysThatWeNeed.isEmpty() || familyUidsToBeIncluded == null || familyUidsToBeIncluded.isEmpty()) {
			return new ArrayList<FamilyCustomFieldData>();
		}
		else {
			String queryString = "select fcfd from FamilyCustomFieldData fcfd where fcfd.familyUid in (:familyUidsToBeIncluded) "
					+ " and fcfd.customFieldDisplay in (:customFieldDisplaysThatWeNeed) and fcfd.study=:study";
			Query query = getSession().createQuery(queryString);
			query.setParameterList("familyUidsToBeIncluded", familyUidsToBeIncluded);
			query.setParameterList("customFieldDisplaysThatWeNeed", customFieldDisplaysThatWeNeed);
			query.setParameter("study",study) ;
			return query.list();
			
		}
	}

	@Override
	public List<CustomFieldDisplay> getCustomFieldDisplaysInWithCustomFieldType(List<String> fieldNameCollection, Study study,ArkFunction arkFunction, CustomFieldType customFieldType) {
		if (fieldNameCollection == null || fieldNameCollection.isEmpty()) {
			return new ArrayList<CustomFieldDisplay>();
		}
		else {
			List<String> lowerCaseNames = new ArrayList<String>();
			for (String name : fieldNameCollection) {
				lowerCaseNames.add(name.toLowerCase());
			}
			String queryString = "select cfd " + 
								"from CustomFieldDisplay cfd " + 
								"where customField.id in ( " + " SELECT id from CustomField cf " + 
																" where cf.study =:study "
																+ " and lower(cf.name) in (:names) " + " and cf.arkFunction =:arkFunction and cf.customFieldType=:customFieldType )";
			Query query = getSession().createQuery(queryString);
			query.setParameter("study", study);
			// query.setParameterList("names", fieldNameCollection);
			query.setParameterList("names", lowerCaseNames);
			query.setParameter("arkFunction", arkFunction);
			query.setParameter("customFieldType", customFieldType);
			return query.list();
		}
	}

	@Override
	public UploadType getUploadTypeByModuleAndName(ArkModule arkModule,String name) {
		Criteria criteria = getSession().createCriteria(UploadType.class);
		criteria.add(Restrictions.eq("name", name));
		criteria.add(Restrictions.eq("arkModule", arkModule));
		return (UploadType) criteria.uniqueResult();
	}
	private String createCorrectOpreratorClauseWithOnePassingParameter(Operator operator,String value){
		if(operator.equals(Operator.IS_EMPTY)|| operator.equals(Operator.IS_NOT_EMPTY)){
			return getHQLForOperator(operator);
		}else{
			return getHQLForOperator(operator)+" '"+value+"' ";
		}
	}
	@Override
	public List<Search> getSearchesForSearch(Search search) {
		Criteria criteria = getSession().createCriteria(Search.class);
		criteria.add(Restrictions.eq("study", search.getStudy()));
		if(search.getId()!=null){criteria.add(Restrictions.eq("id", search.getId()));}
		if(search.getName()!=null){criteria.add(Restrictions.like("name", search.getName(),MatchMode.ANYWHERE));}
		List<Search> searchList = criteria.list();
		return searchList;
	}
	@Override
	public List<StudyComp> getStudyComponentsNeverUsedInThisSubject(Study study, LinkSubjectStudy linkSubjectStudy) {
		List<StudyComp> consentStudyCompLst=getDifferentStudyComponentsInConsentForSubject(study, linkSubjectStudy);
		List<Long> consentStudyCompIdLst=new ArrayList<Long>();
		for (StudyComp studyComp : consentStudyCompLst) {
			consentStudyCompIdLst.add(studyComp.getId());
		}
		Criteria criteria = getSession().createCriteria(StudyComp.class);
		criteria.add(Restrictions.eq("study", study));
		if(!consentStudyCompIdLst.isEmpty()) {
			criteria.add(Restrictions.not(Restrictions.in("id", consentStudyCompIdLst)));
		}
		return criteria.list();
	}

	@Override
	public List<StudyComp> getDifferentStudyComponentsInConsentForSubject(Study study, LinkSubjectStudy linkSubjectStudy) {
		Criteria criteria = getSession().createCriteria(Consent.class);
		criteria.add(Restrictions.eq("study",study));
		criteria.add(Restrictions.eq("linkSubjectStudy",linkSubjectStudy));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("studyComp"));
		criteria.setProjection(projectionList);
		criteria.addOrder(Order.asc("id"));
		List<StudyComp> fieldsList = criteria.list();
		return fieldsList;
	}

}