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
package au.org.theark.core.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import au.org.theark.core.Constants;
import au.org.theark.core.dao.ArkLdapContextSource;
import au.org.theark.core.dao.IArkAuthorisation;
import au.org.theark.core.dao.ICSVLoaderDao;
import au.org.theark.core.dao.ICustomFieldDao;
import au.org.theark.core.dao.IGenoDao;
import au.org.theark.core.dao.IStudyDao;
import au.org.theark.core.dao.ReCaptchaContextSource;
import au.org.theark.core.exception.ArkRunTimeException;
import au.org.theark.core.exception.ArkRunTimeUniqueException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.config.entity.ConfigField;
import au.org.theark.core.model.config.entity.UserConfig;
import au.org.theark.core.model.geno.entity.Command;
import au.org.theark.core.model.geno.entity.Pipeline;
import au.org.theark.core.model.geno.entity.Process;
import au.org.theark.core.model.lims.entity.BioCollectionUidPadChar;
import au.org.theark.core.model.lims.entity.BioCollectionUidTemplate;
import au.org.theark.core.model.lims.entity.BioCollectionUidToken;
import au.org.theark.core.model.lims.entity.BiospecimenUidPadChar;
import au.org.theark.core.model.lims.entity.BiospecimenUidTemplate;
import au.org.theark.core.model.lims.entity.BiospecimenUidToken;
import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.report.entity.BiocollectionField;
import au.org.theark.core.model.report.entity.BiospecimenField;
import au.org.theark.core.model.report.entity.ConsentStatusField;
import au.org.theark.core.model.report.entity.DemographicField;
import au.org.theark.core.model.report.entity.QueryFilter;
import au.org.theark.core.model.report.entity.Search;
import au.org.theark.core.model.report.entity.SearchPayload;
import au.org.theark.core.model.report.entity.SearchResult;
import au.org.theark.core.model.report.entity.SearchSubject;
import au.org.theark.core.model.study.entity.AddressStatus;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkModuleRole;
import au.org.theark.core.model.study.entity.ArkPermission;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.ArkUserRole;
import au.org.theark.core.model.study.entity.AuditHistory;
import au.org.theark.core.model.study.entity.ConsentAnswer;
import au.org.theark.core.model.study.entity.ConsentOption;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.CustomFieldCategoryUpload;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.CustomFieldType;
import au.org.theark.core.model.study.entity.CustomFieldUpload;
import au.org.theark.core.model.study.entity.DelimiterType;
import au.org.theark.core.model.study.entity.EmailStatus;
import au.org.theark.core.model.study.entity.FieldType;
import au.org.theark.core.model.study.entity.FileFormat;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkStudyArkModule;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.OtherID;
import au.org.theark.core.model.study.entity.Payload;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonContactMethod;
import au.org.theark.core.model.study.entity.PersonLastnameHistory;
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
import au.org.theark.core.model.study.entity.UnitType;
import au.org.theark.core.model.study.entity.Upload;
import au.org.theark.core.model.study.entity.UploadLevel;
import au.org.theark.core.model.study.entity.UploadStatus;
import au.org.theark.core.model.study.entity.UploadType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.vo.ArkModuleVO;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.CustomFieldCategoryVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.vo.QueryFilterVO;
import au.org.theark.core.vo.SearchVO;
import au.org.theark.core.vo.SubjectVO;

/**
 * The implementation of IArkCommonService. We want to auto-wire and hence use
 * the @Service annotation.
 * 
 * @author nivedann
 * @param <T>
 */

@Transactional
@Service(Constants.ARK_COMMON_SERVICE)
public class ArkCommonServiceImpl<T> implements IArkCommonService {
	private static Logger log = LoggerFactory.getLogger(ArkCommonServiceImpl.class);

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 8;

	private IArkAuthorisation arkAuthorisationDao;
	private ICustomFieldDao customFieldDao;
	private IStudyDao studyDao;
	private ICSVLoaderDao csvLoaderDao;
	private ArkLdapContextSource ldapDataContextSource;
	private ReCaptchaContextSource reCaptchaContextSource;
	private JavaMailSender javaMailSender;
	private VelocityEngine velocityEngine;
	private IGenoDao genoDao;
	
	
	
	@Value("${file.attachment.dir}")
	private String fileAttachmentDir;

	public IGenoDao getGenoDao() {
		return genoDao;
	}

	@Autowired
	public void setGenoDao(IGenoDao genoDao) {
		this.genoDao = genoDao;
	}
	
	public ICustomFieldDao getCustomFieldDao() {
		return customFieldDao;
	}

	public Blob createBlob(byte[] bytes) {
		return csvLoaderDao.createBlob(bytes);
	}

	@Autowired
	public void setCustomFieldDao(ICustomFieldDao customFieldDao) {
		this.customFieldDao = customFieldDao;
	}

	/**
	 * @return the velocityEngine
	 */
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	/**
	 * @param velocityEngine
	 *            the velocityEngine to set
	 */
	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	/**
	 * @return the javaMailSender
	 */
	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	/**
	 * @param javaMailSender
	 *            the javaMailSender to set
	 */
	@Autowired
	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public ReCaptchaContextSource getRecaptchaContextSource() {
		return reCaptchaContextSource;
	}

	@Autowired
	public void setRecaptchaContextSource(ReCaptchaContextSource recaptchaContextSource) {
		this.reCaptchaContextSource = recaptchaContextSource;
	}

	public IArkAuthorisation getArkAuthorisationDao() {
		return arkAuthorisationDao;
	}

	@Autowired
	public void setArkAuthorisationDao(IArkAuthorisation arkAuthorisationDao) {
		this.arkAuthorisationDao = arkAuthorisationDao;
	}

	public IStudyDao getStudyDao() {
		return studyDao;
	}

	@Autowired
	public void setStudyDao(IStudyDao studyDao) {
		this.studyDao = studyDao;
	}

	public ArkLdapContextSource getLdapDataContextSource() {
		return ldapDataContextSource;
	}

	@Autowired
	public void setLdapDataContextSource(ArkLdapContextSource ldapDataContextSource) {
		this.ldapDataContextSource = ldapDataContextSource;
	}

	public ICSVLoaderDao getCsvLoaderDao() {
		return csvLoaderDao;
	}

	@Autowired
	public void setCsvLoaderDao(ICSVLoaderDao csvLoaderDao) {
		this.csvLoaderDao = csvLoaderDao;
	}

	private static class PersonContextMapper implements ContextMapper {

		public Object mapFromContext(Object ctx) {

			DirContextAdapter context = (DirContextAdapter) ctx;

			ArkUserVO etaUserVO = new ArkUserVO();
			etaUserVO.setUserName(context.getStringAttribute("cn"));
			etaUserVO.setFirstName(context.getStringAttribute("givenName"));
			etaUserVO.setLastName(context.getStringAttribute("sn"));
			etaUserVO.setEmail(context.getStringAttribute("mail"));
			String ldapPassword = new String((byte[]) context.getObjectAttribute("userPassword"));
			etaUserVO.setPassword(ldapPassword);
			return etaUserVO;
		}
	}

	public ArkUserVO getUser(String username) throws ArkSystemException, EntityNotFoundException {
		ArkUserVO userVO = new ArkUserVO();
		try {

			LdapName ldapName = new LdapName(ldapDataContextSource.getBasePeopleDn());
			ldapName.add(new Rdn("cn", username));
			Name nameObj = (Name) ldapName;

			userVO = (ArkUserVO) ldapDataContextSource.getLdapTemplate().lookup(nameObj, new PersonContextMapper());

		} catch (InvalidNameException ne) {
			throw new ArkSystemException("A System error has occured");
		} catch (NameNotFoundException ex) {
			log.error(username + " not found in LDAP");
			throw new EntityNotFoundException();
		}

		return userVO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.service.IArkCommonService#getListOfStudyStatus()
	 */
	public List<StudyStatus> getListOfStudyStatus() {
		return studyDao.getListOfStudyStatus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * au.org.theark.core.service.IArkCommonService#getStudy(au.org.theark.core
	 * .model.study.entity.Study)
	 */
	public List<Study> getStudy(Study study) {
		return studyDao.getStudy(study);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * au.org.theark.core.service.IArkCommonService#getStudy(java.lang.Long)
	 */
	public Study getStudy(Long id) {

		return studyDao.getStudy(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * au.org.theark.core.service.IArkCommonService#getSubject(au.org.theark
	 * .core.vo.SubjectVO)
	 */
	public Collection<SubjectVO> getSubject(SubjectVO subjectVO) {

		return studyDao.getSubject(subjectVO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.service.IArkCommonService#getGenderType()
	 */
	public Collection<GenderType> getGenderTypes() {

		return studyDao.getGenderTypes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.service.IArkCommonService#getListOfPhoneType()
	 */
	public List<PhoneType> getListOfPhoneType() {

		return studyDao.getListOfPhoneType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.service.IArkCommonService#getSubjectStatus()
	 */
	public List<SubjectStatus> getSubjectStatus() {

		return studyDao.getSubjectStatus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.service.IArkCommonService#getTitleType()
	 */
	public Collection<TitleType> getTitleType() {

		return studyDao.getTitleType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.service.IArkCommonService#getVitalStatus()
	 */
	public Collection<VitalStatus> getVitalStatus() {

		return studyDao.getVitalStatus();
	}

	public LinkSubjectStudy getSubjectByUID(String subjectUID, Study study) throws EntityNotFoundException {

		return studyDao.getSubjectByUID(subjectUID, study);
	}

	public LinkSubjectStudy getSubjectRefreshed(LinkSubjectStudy subject) {
		return studyDao.getSubjectRefreshed(subject);
	}

	/**
	 * returns a the subject (linksubjectystudy) IF there is one, else returns
	 * null
	 * 
	 * @param subjectUID
	 * @param study
	 * @return LinkSubjectStudy
	 */
	public LinkSubjectStudy getSubjectByUIDAndStudy(String subjectUID, Study study) {
		return studyDao.getSubjectByUIDAndStudy(subjectUID, study);
	}

	public Collection<MaritalStatus> getMaritalStatus() {
		return studyDao.getMaritalStatus();
	}

	public List<Country> getCountries() {
		return studyDao.getCountries();
	}

	public List<State> getStates(Country country) {
		return studyDao.getStates(country);
	}

	public List<AddressType> getAddressTypes() {
		return studyDao.getAddressTypes();
	}

	public List<AddressStatus> getAddressStatuses() {
		return studyDao.getAddressStatuses();
	}

	public List<PhoneType> getPhoneTypes() {
		return studyDao.getPhoneTypes();
	}

	public List<PhoneStatus> getPhoneStatuses() {
		return studyDao.getPhoneStatuses();
	}

	public List<ConsentStatus> getConsentStatus() {
		return studyDao.getConsentStatus();
	}

	public List<ConsentStatus> getRecordableConsentStatus() {
		return studyDao.getRecordableConsentStatus();
	}

	public List<StudyComp> getStudyComponentByStudy(Study study) {
		return studyDao.getStudyComponentByStudy(study);
	}

	public List<ConsentType> getConsentType() {
		return studyDao.getConsentType();
	}

	public List<StudyCompStatus> getStudyComponentStatus() {
		return studyDao.getStudyComponentStatus();
	}

	public List<ConsentAnswer> getConsentAnswer() {
		return studyDao.getConsentAnswer();
	}

	public List<YesNo> getYesNoList() {
		return studyDao.getYesNoList();
	}

	public YesNo getYes() {
		return studyDao.getYes();
	}

	public YesNo getNo() {
		return studyDao.getNo();
	}

	public void createAuditHistory(AuditHistory auditHistory) {
		studyDao.createAuditHistory(auditHistory);
	}

	/**
	 * create audit history, forcing userID, necessary due to batch job not
	 * maintaining session info
	 * 
	 * @param auditHistory
	 * @param userID
	 */
	public void createAuditHistory(AuditHistory auditHistory, String userId, Study study) {
		studyDao.createAuditHistory(auditHistory, userId, study);
	}

	public List<PersonContactMethod> getPersonContactMethodList() {
		return studyDao.getPersonContactMethodList();
	}

	public boolean isSubjectConsentedToComponent(StudyComp studyComponent, Person subject, Study study) {
		return studyDao.isSubjectConsentedToComponent(studyComponent, subject, study);
	}

	public LinkSubjectStudy getSubject(Long personId, Study study) throws EntityNotFoundException {
		return studyDao.getSubject(personId, study);
	}

	public List<SubjectUidPadChar> getListOfSubjectUidPadChar() {
		return studyDao.getListOfSubjectUidPadChar();
	}

	public String getSubjectUidExample(Study study) {
		return studyDao.getSubjectUidExample(study);
	}

	public List<SubjectUidToken> getListOfSubjectUidToken() {
		return studyDao.getListOfSubjectUidToken();
	}

	public Country getCountry(String countryCode) {
		return studyDao.getCountry(countryCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * au.org.theark.core.service.IArkCommonService#isAdministator(java.lang
	 * .String)
	 */
	public boolean isAdministator(String userName) throws EntityNotFoundException {

		return arkAuthorisationDao.isAdministator(userName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * au.org.theark.core.service.IArkCommonService#isSuperAdmin(java.lang.String
	 * )
	 */
	public boolean isSuperAdministrator(String userName) throws EntityNotFoundException {
		return arkAuthorisationDao.isSuperAdministrator(userName);
	}

	public GenderType getGenderType(String name) {
		return studyDao.getGenderType(name);
	}

	public VitalStatus getVitalStatus(String name) {
		return studyDao.getVitalStatus(name);
	}

	public TitleType getTitleType(String name) {
		return studyDao.getTitleType(name);
	}

	public MaritalStatus getMaritalStatus(String name) {
		return studyDao.getMaritalStatus(name);
	}

	public PersonContactMethod getPersonContactMethod(String name) {
		return studyDao.getPersonContactMethod(name);
	}

	public SubjectStatus getSubjectStatus(String name) {
		return studyDao.getSubjectStatus(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * au.org.theark.core.service.IArkCommonService#getUserAdminRoles(java.lang
	 * .String)
	 */
	@SuppressWarnings("unchecked")
	public Collection<String> getUserAdminRoles(String ldapUserName) throws EntityNotFoundException {

		return arkAuthorisationDao.getUserAdminRoles(ldapUserName);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * au.org.theark.core.service.IArkCommonService#getUserRoleForStudy(java
	 * .lang.String, au.org.theark.core.model.study.entity.Study)
	 */
	public String getUserRoleForStudy(String ldapUserName, Study study) throws EntityNotFoundException {
		return arkAuthorisationDao.getUserRoleForStudy(ldapUserName, study);
	}

	public ArkFunction getArkFunctionByName(String functionName) {
		return arkAuthorisationDao.getArkFunctionByName(functionName);
	}

	public ArkModule getArkModuleByName(String moduleName) {
		return arkAuthorisationDao.getArkModuleByName(moduleName);
	}

	public String getUserRole(String ldapUserName, ArkFunction arkFunction, ArkModule arkModule, Study study) throws EntityNotFoundException {
		return arkAuthorisationDao.getUserRole(ldapUserName, arkFunction, arkModule, study);
	}

	public ArkFunction getArkFunctionById(Long functionId) {
		return arkAuthorisationDao.getArkFunctionById(functionId);
	}

	public ArkModule getArkModuleById(Long moduleId) {
		return arkAuthorisationDao.getArkModuleById(moduleId);
	}

	/**
	 * Returns All Permissions as collection of Strings
	 * 
	 * @return Collection<String> that represents ArkPermission
	 */
	@SuppressWarnings("unchecked")
	public Collection<String> getArkPermission() {
		return arkAuthorisationDao.getArkPermission();
	}

	public boolean isSuperAdministator(String ldapUserName, ArkFunction arkFunction, ArkModule arkModule) throws EntityNotFoundException {
		return arkAuthorisationDao.isSuperAdministator(ldapUserName, arkFunction, arkModule);
	}

	public ArkUser getArkUser(String ldapUserName) throws EntityNotFoundException {
		return arkAuthorisationDao.getArkUser(ldapUserName);
	}

	@SuppressWarnings("unchecked")
	public Collection<Class<T>> getEntityList(Class aClass) {
		return arkAuthorisationDao.getEntityList(aClass);
	}

	public long getStudySubjectCount(SubjectVO subjectVoCriteria) {
		return studyDao.getStudySubjectCount(subjectVoCriteria);
	}

	public List<SubjectVO> searchPageableSubjects(SubjectVO subjectVoCriteria, int first, int count) {
		return studyDao.searchPageableSubjects(subjectVoCriteria, first, count);
	}

	public Collection getArkRolePermission(ArkFunction arkFunction, String userRole, ArkModule arkModule) throws EntityNotFoundException {

		return arkAuthorisationDao.getArkRolePermission(arkFunction, userRole, arkModule);
	}

	public Collection getArkRolePermission(String userRole) throws EntityNotFoundException {

		return arkAuthorisationDao.getArkRolePermission(userRole);
	}

	public Collection<ArkModuleRole> getArkModuleAndLinkedRoles() {
		return arkAuthorisationDao.getArkModuleAndLinkedRoles();
	}

	public Collection<ArkModuleVO> getArkModulesAndRolesLinkedToStudy(Study study) {
		return arkAuthorisationDao.getArkModulesAndRolesLinkedToStudy(study);
	}

	public ArrayList<ArkRole> getArkRoleLinkedToModule(ArkModule arkModule) {
		return arkAuthorisationDao.getArkRoleLinkedToModule(arkModule);
	}

	public Collection<ArkModule> getArkModulesLinkedWithStudy(Study study) {
		return arkAuthorisationDao.getArkModulesLinkedWithStudy(study);
	}

	public List<ArkFunction> getModuleFunction(ArkModule arkModule) {
		return studyDao.getModuleFunction(arkModule);
	}

	public List<ArkUserRole> getArkUserLinkedModule(Study study, ArkModule arkModule) {
		return arkAuthorisationDao.getArkUserLinkedModule(study, arkModule);
	}

	public List<LinkStudyArkModule> getLinkStudyArkModulesList(Study study) {
		return arkAuthorisationDao.getLinkStudyArkModulesList(study);
	}

	public List<PhoneStatus> getPhoneStatus() {
		return studyDao.getPhoneStatus();
	}

	public Boolean studyHasSubjects(Study study) {
		return studyDao.studyHasSubjects(study);
	}

	public List<Study> getStudiesForUser(ArkUser arkUser, Study study) {
		return studyDao.getStudiesForUser(arkUser, study);
	}

	public long getCustomFieldCount(CustomField customFieldCriteria) {
		return customFieldDao.getCustomFieldCount(customFieldCriteria);
	}

	public List<CustomField> searchPageableCustomFields(CustomField customFieldCriteria, int first, int count) {
		return customFieldDao.searchPageableCustomFields(customFieldCriteria, first, count);
	}

	public List<CustomField> searchPageableCustomFieldsForPheno(CustomField customFieldCriteria, int first, int count) {
		return customFieldDao.searchPageableCustomFieldsForPheno(customFieldCriteria, first, count);
	}

	public List<FieldType> getFieldTypes() {
		return customFieldDao.getFieldTypes();
	}

	public List<String> getUnitTypeNames(UnitType unitTypeCriteria, int maxResults) {
		return customFieldDao.getUnitTypeNames(unitTypeCriteria, maxResults);
	}

	public List<UnitType> getUnitTypes(UnitType unitTypeCriteria) {
		return customFieldDao.getUnitTypes(unitTypeCriteria);
	}

	public CustomField getCustomField(Long id) {
		return customFieldDao.getCustomField(id);
	}

	public CustomFieldDisplay getCustomFieldDisplayByCustomField(CustomField cfCriteria) {
		return customFieldDao.getCustomFieldDisplayByCustomField(cfCriteria);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void createCustomField(CustomFieldVO customFieldVO) throws ArkSystemException, ArkUniqueException {
		try {
			// Create Both CustomField and CustomFieldDisplay
			AuditHistory ah = new AuditHistory();

			// Force uppercase and replace erroneous characters
			customFieldVO.getCustomField().getName().toUpperCase();
			customFieldVO.getCustomField().getName().replaceAll(" ", "_");

			// Remove any encoded values if DATE or NUMBER
			if (customFieldVO.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE) || customFieldVO.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
				customFieldVO.getCustomField().setEncodedValues(null);
			}

			// Field can not have data yet (since it's new)
			customFieldVO.getCustomField().setCustomFieldHasData(false);
			customFieldDao.createCustomField(customFieldVO.getCustomField());

			// Custom Field History
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
			ah.setComment("Created Custom " + customFieldVO.getCustomField().getName());
			ah.setEntityId(customFieldVO.getCustomField().getId());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CUSTOM_FIELD);

			createAuditHistory(ah);
			// Create CustomFieldDisplay only if allowed
			if (customFieldVO.isUseCustomFieldDisplay()) {
				// Set the CustomField this CustomFieldDisplay entity is linked
				// to
				customFieldVO.getCustomFieldDisplay().setCustomField(customFieldVO.getCustomField());
				customFieldDao.createCustomFieldDisplay(customFieldVO.getCustomFieldDisplay());
				// Put in the sequence based on the ID
				customFieldVO.getCustomFieldDisplay().setSequence(customFieldVO.getCustomFieldDisplay().getId());
				customFieldDao.updateCustomFieldDisplay(customFieldVO.getCustomFieldDisplay());

				// Custom Field Display History
				ah = new AuditHistory();
				ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
				ah.setComment("Created Custom Field Display" + customFieldVO.getCustomField().getName());
				ah.setEntityId(customFieldVO.getCustomField().getId());
				ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CUSTOM_FIELD_DISPLAY);
				createAuditHistory(ah);
			}
		} catch (ConstraintViolationException cvex) {
			log.error("Custom Field Already Exists.: " + cvex);
			throw new ArkUniqueException("A Custom Field already exits.");
		} catch (Exception ex) {
			log.error("Problem creating Custom Field: " + ex);
			throw new ArkSystemException("Problem creating Custom Field: " + ex.getMessage());
		}
	}

	/**
	 * Update a Custom Field if it is not yet any data and update the Custom
	 * Field display details.
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateCustomField(CustomFieldVO customFieldVO) throws ArkSystemException, ArkUniqueException {

		boolean isUnique = customFieldDao.isCustomFieldUnqiue(customFieldVO.getCustomField().getName(), customFieldVO.getCustomField().getStudy(), customFieldVO.getCustomField());
		if (!isUnique) {
			log.error("Custom Field of this name Already Exists.: ");
			throw new ArkUniqueException("A Custom Field of this name already exists.");
		}
		try {
			// Remove any encoded values if DATE or NUMBER
			if (customFieldVO.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE) || customFieldVO.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
				customFieldVO.getCustomField().setEncodedValues(null);
			}

			customFieldDao.updateCustomField(customFieldVO.getCustomField());
			// Custom Field History
			AuditHistory ah = new AuditHistory();
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
			ah.setComment("Updated Custom Field " + customFieldVO.getCustomField().getName());
			ah.setEntityId(customFieldVO.getCustomField().getId());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CUSTOM_FIELD);
			createAuditHistory(ah);

			// Only Update CustomFieldDisplay when it is allowed
			if (customFieldVO.isUseCustomFieldDisplay()) {
				customFieldVO.getCustomFieldDisplay().setCustomField(customFieldVO.getCustomField());
				customFieldDao.updateCustomFieldDisplay(customFieldVO.getCustomFieldDisplay());
				// Custom Field Display History
				ah = new AuditHistory();
				ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
				ah.setComment("Updated Custom Field Display " + customFieldVO.getCustomField().getName());
				ah.setEntityId(customFieldVO.getCustomField().getId());
				ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CUSTOM_FIELD_DISPLAY);
				createAuditHistory(ah);
			}

		} catch (ConstraintViolationException cvex) {
			log.error("Custom Field Already Exists.: " + cvex);
			throw new ArkUniqueException("A Custom Field already exits.");
		} catch (Exception ex) {
			log.error("Problem updating Custom Field: " + ex);
			throw new ArkSystemException("Problem updating Custom Field: " + ex.getMessage());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteCustomField(CustomFieldVO customFieldVO) throws ArkSystemException, EntityCannotBeRemoved {
		try {
			if (!customFieldVO.getCustomField().getCustomFieldHasData()) {
				String fieldName = customFieldVO.getCustomField().getName();

				if (customFieldVO.isUseCustomFieldDisplay()) {
					customFieldDao.deleteCustomDisplayField(customFieldVO.getCustomFieldDisplay());

					// History for Custom Field Display
					AuditHistory ah = new AuditHistory();
					ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
					ah.setComment("Deleted Custom Display Field For Custom Field " + fieldName);
					ah.setEntityId(customFieldVO.getCustomFieldDisplay().getId());
					ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CUSTOM_FIELD_DISPLAY);
					createAuditHistory(ah);
				}
				customFieldDao.deleteCustomField(customFieldVO.getCustomField());

				// History for Custom Field
				AuditHistory ah = new AuditHistory();
				ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
				ah.setComment("Deleted Custom Field " + fieldName);
				ah.setEntityId(customFieldVO.getCustomField().getId());
				ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CUSTOM_FIELD);
				createAuditHistory(ah);
			} else {
				throw new EntityCannotBeRemoved("Custom Field cannot be removed, it is used in the system");
			}
		} catch (Exception ex) {
			log.error("Unable to delete CustomField. " + ex);
			throw new ArkSystemException("Unable to delete Custom Field: " + ex.getMessage());
		}
	}

	public List<ArkUserRole> getArkRoleListByUser(ArkUserVO arkUserVo) {
		return arkAuthorisationDao.getArkRoleListByUser(arkUserVo);
	}

	public List<ArkRolePolicyTemplate> getArkRolePolicyTemplate(ArkRole arkRole, ArkModule arkModule) {
		return arkAuthorisationDao.getArkRolePolicyTemplate(arkRole, arkModule);
	}

	public List<ArkUserRole> getArkRoleListByUserAndStudy(ArkUserVO arkUserVo, Study study) {
		return arkAuthorisationDao.getArkRoleListByUserAndStudy(arkUserVo, study);
	}

	public List<Study> getStudyListForUser(ArkUserVO arkUserVo) {
		return arkAuthorisationDao.getStudyListForUser(arkUserVo);
	}

	public List<Study> getStudyListForUserAndModule(ArkUserVO arkUserVo, ArkModule arkModule) {
		return arkAuthorisationDao.getStudyListForUserAndModule(arkUserVo, arkModule);
	}

	public boolean arkUserHasModuleAccess(ArkUser arkUser, ArkModule arkModule) {
		return arkAuthorisationDao.arkUserHasModuleAccess(arkUser, arkModule);
	}

	public List<ArkModule> getArkModuleListByArkUser(ArkUser arkUser) {
		return arkAuthorisationDao.getArkModuleListByArkUser(arkUser);
	}

	public long getCountOfStudies() {
		return studyDao.getCountOfStudies();
	}

	public Boolean isArkUserLinkedToStudies(ArkUser arkUser) {
		return arkAuthorisationDao.isArkUserLinkedToStudies(arkUser);
	}

	public List<CustomFieldGroup> getCustomFieldGroups(CustomFieldGroup customFieldGroup, int first, int count) {
		return customFieldDao.getCustomFieldGroups(customFieldGroup, first, count);
	}

	public long getCustomFieldGroupCount(CustomFieldGroup customFieldGroup) {
		return customFieldDao.getCustomFieldGroupCount(customFieldGroup);
	}

	public CustomField getFieldByNameAndStudyAndFunction(String fieldName, Study study, ArkFunction arkFunction) throws EntityNotFoundException {
		return customFieldDao.getFieldByNameAndStudyAndFunction(fieldName, study, arkFunction);
	}

	public FieldType getFieldTypeByName(String typeName) throws EntityNotFoundException {
		return customFieldDao.getFieldTypeByName(typeName);
	}

	public ArkRole getArkRoleByName(String roleName) {

		return arkAuthorisationDao.getArkRoleByName(roleName);
	}

	public List<CustomField> getCustomFieldList(CustomField customFieldCriteria) {
		return customFieldDao.getCustomFieldList(customFieldCriteria);
	}

	public void sendEmail(final SimpleMailMessage simpleMailMessage) throws MailSendException, VelocityException {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setTo(simpleMailMessage.getTo());

				// The "from" field is required
				if (simpleMailMessage.getFrom() == null) {
					simpleMailMessage.setFrom(Constants.ARK_ADMIN_EMAIL);
				}

				message.setFrom(simpleMailMessage.getFrom());
				message.setSubject(simpleMailMessage.getSubject());

				// Map all the fields for the email template
				Map<String, Object> model = new HashMap<String, Object>();

				// Add the host name into the footer of the email
				String host = InetAddress.getLocalHost().getHostName();

				// Message title
				model.put("title", "Message from The ARK");
				// Message header
				model.put("header", "Message from The ARK");
				// Message subject
				model.put("subject", simpleMailMessage.getSubject());
				// Message text
				model.put("text", simpleMailMessage.getText());
				// Hostname in message footer
				model.put("host", host);

				// TODO: Add inline image(s)??
				// Add inline image header
				// FileSystemResource res = new FileSystemResource(new
				// File("c:/Sample.jpg"));
				// message.addInline("bgHeaderImg", res);

				// Set up the email text
				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "au/org/theark/core/velocity/resetPasswordEmail.vm", model);
				message.setText(text, true);
			}
		};

		// send out the email
		javaMailSender.send(preparator);
	}

	public String setResetPasswordMessage(final String fullName, final String password) throws VelocityException {
		// map all the fields for the message template
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("fullName", fullName);
		model.put("password", password);

		/* get the text and replace all the mapped fields */
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "au/org/theark/core/velocity/resetPasswordMessage.vm","UTF-8", model);
		/* send out the email */
		return text;
	}

	public void updateCustomFieldDisplay(CustomFieldDisplay customFieldDisplay) throws ArkSystemException {
		customFieldDao.updateCustomFieldDisplay(customFieldDisplay);
	}

	public CustomFieldDisplay getCustomFieldDisplay(Long id) {
		return customFieldDao.getCustomFieldDisplay(id);
	}

	public FileFormat getFileFormatByName(String fileFormatName) {
		return studyDao.getFileFormatByName(fileFormatName);
	}

	public Collection<FileFormat> getFileFormats() {
		return studyDao.getFileFormats();
	}

	public DelimiterType getDelimiterType(Long id) {
		return studyDao.getDelimiterType(id);
	}

	public UploadType getDefaultUploadType() {
		return studyDao.getDefaultUploadType();
	}

	public UploadType getDefaultUploadTypeForLims() {
		return studyDao.getDefaultUploadTypeForLims();
	}

	public UploadType getCustomFieldDataUploadType() {
		return studyDao.getCustomFieldDataUploadType();
	}

	public Collection<DelimiterType> getDelimiterTypes() {
		return studyDao.getDelimiterTypes();
	}

	public Collection<UploadType> getUploadTypes() {
		return studyDao.getUploadTypes();
	}

	public CustomField getCustomFieldByNameStudyArkFunction(String customFieldName, Study study, ArkFunction arkFunction) {
		return customFieldDao.getCustomFieldByNameStudyArkFunction(customFieldName, study, arkFunction);
	}

	public CustomField getCustomFieldByNameStudyCFG(String customFieldName, Study study, ArkFunction arkFunction, CustomFieldGroup customFieldGroup) {
		return customFieldDao.getCustomFieldByNameStudyCFG(customFieldName, study, arkFunction, customFieldGroup);
	}

	public UnitType getUnitTypeByNameAndArkFunction(String name, ArkFunction arkFunction) {
		return customFieldDao.getUnitTypeByNameAndArkFunction(name, arkFunction);
	}

	public List<Upload> searchUploads(Upload uploadCriteria) {
		return studyDao.searchUploads(uploadCriteria);
	}

	public List<Upload> searchUploadsForBio(Upload uploadCriteria) {
		return studyDao.searchUploadsForBio(uploadCriteria);
	}

	public void createUpload(Upload studyUpload) {
		// log.debug("about to studydao.createupload");
		studyDao.createUpload(studyUpload);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created studyUpload " + studyUpload.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY_UPLOAD);
		ah.setEntityId(studyUpload.getId());
		this.createAuditHistory(ah);

	}

	public void updateUpload(Upload studyUpload) {
		studyDao.updateUpload(studyUpload);
		String userId = studyUpload.getUserId();
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated studyUpload " + studyUpload.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY_UPLOAD);
		ah.setEntityId(studyUpload.getId());
		this.createAuditHistory(ah, userId, studyUpload.getStudy());
	}

	public String getDelimiterTypeNameByDelimiterChar(char delimiterCharacter) {
		return studyDao.getDelimiterTypeNameByDelimiterChar(delimiterCharacter);
	}

	public void createCustomFieldUpload(CustomFieldUpload cfUpload) {
		studyDao.createCustomFieldUpload(cfUpload);
	}

	public CustomFieldDisplay getCustomFieldDisplayByCustomField(CustomField cfCriteria, CustomFieldGroup customFieldGroup) {
		return customFieldDao.getCustomFieldDisplayByCustomField(cfCriteria, customFieldGroup);
	}

	public List<BiospecimenUidToken> getBiospecimenUidTokens() {
		return studyDao.getBiospecimenUidTokens();
	}

	public List<BiospecimenUidPadChar> getBiospecimenUidPadChars() {
		return studyDao.getBiospecimenUidPadChars();
	}

	public List<Study> getStudyListAssignedToBiospecimenUidTemplate() {
		return studyDao.getStudyListAssignedToBiospecimenUidTemplate();
	}

	public void createBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate) {
		studyDao.createBiospecimenUidTemplate(biospecimenUidTemplate);
	}

	public List<BioCollectionUidToken> getBioCollectionUidToken() {
		return studyDao.getBioCollectionUidToken();
	}

	public List<BioCollectionUidPadChar> getBioCollectionUidPadChar() {
		return studyDao.getBioCollectionUidPadChar();
	}

	public void createBioCollectionUidTemplate(BioCollectionUidTemplate bioCollectionUidTemplate) {
		studyDao.createBioCollectionUidTemplate(bioCollectionUidTemplate);
	}

	public Boolean studyHasBiospecimen(Study study) {
		return studyDao.studyHasBiospecimen(study);
	}

	public Boolean studyHasBioCollection(Study study) {
		return studyDao.studyHasBioCollection(study);
	}

	public BiospecimenUidTemplate getBiospecimenUidTemplate(Study study) {
		return studyDao.getBiospecimentUidTemplate(study);
	}

	public BioCollectionUidTemplate getBioCollectionUidTemplate(Study study) {
		return studyDao.getBioCollectionUidTemplate(study);
	}

	public void updateBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate) {
		studyDao.updateBiospecimenUidTemplate(biospecimenUidTemplate);
	}

	public void updateBioCollectionUidTemplate(BioCollectionUidTemplate bioCollectionUidTemplate) {
		studyDao.updateBioCollectionUidTemplate(bioCollectionUidTemplate);
	}

	public List<ArkUser> getArkUserListByStudy(ArkUser arkUser,Study study) {
		return arkAuthorisationDao.getArkUserListByStudy(arkUser,study);
	}

	@SuppressWarnings("unchecked")
	public List<Study> getParentStudyList() {
		return arkAuthorisationDao.getParentStudyList();
	}

	public ArkUserVO getDefaultAdministratorRoles(String userName, Study study, Set arkModuleNames) {
		ArkUserVO arkUserVo = new ArkUserVO();
		try {
			ArkUser arkUser;
			arkUser = getArkUser(userName);
			arkUserVo.setArkUserEntity(arkUser);
			arkUserVo.setUserName(userName);
			List<ArkUserRole> arkUserRoleList = new ArrayList<ArkUserRole>(0);

			for (Iterator<String> iterator = arkModuleNames.iterator(); iterator.hasNext();) {
				String arkModuleName = (String) iterator.next();
				ArkUserRole arkUserRole = new ArkUserRole();
				arkUserRole.setArkUser(arkUser);
				StringBuilder adminName = new StringBuilder();
				adminName.append(arkModuleName);
				adminName.append(" ");
				adminName.append(RoleConstants.ARK_ROLE_ADMINISTATOR);
				arkUserRole.setArkRole(getArkRoleByName(adminName.toString()));
				arkUserRole.setArkModule(getArkModuleByName(arkModuleName));
				arkUserRole.setStudy(study);
				arkUserRoleList.add(arkUserRole);
			}

			arkUserVo.setArkUserRoleList(arkUserRoleList);
		} catch (EntityNotFoundException e) {
			log.error(e.getMessage());
		}
		return arkUserVo;
	}

	public List<Study> getAssignedChildStudyListForUser(ArkUserVO arkUserVo) {
		return arkAuthorisationDao.getAssignedChildStudyListForUser(arkUserVo);
	}

	public void deleteArkUserRole(ArkUserRole arkUserRole) {
		arkAuthorisationDao.deleteArkUserRole(arkUserRole);
	}

	public long getCountOfSubjects(Study study) {
		return studyDao.getCountOfSubjects(study);
	}

	public List<SubjectVO> matchSubjectsFromInputFile(FileUpload subjectFileUpload, Study study) {
		return studyDao.matchSubjectsFromInputFile(subjectFileUpload, study);
	}

	public List<Study> getAssignedChildStudyListForPerson(Study study, Person person) {
		return studyDao.getAssignedChildStudyListForPerson(study, person);
	}

	public List<ConsentOption> getConsentOptionList() {
		return studyDao.getConsentOptionList();
	}

	public boolean customFieldHasData(CustomField customField) {
		return studyDao.customFieldHasData(customField);
	}

	public long countNumberOfUniqueSubjectsWithTheseUIDs(Study study, List subjectUIDs) {
		if (study != null && subjectUIDs != null) {
			return studyDao.countNumberOfSubjectsThatAlreadyExistWithTheseUIDs(study, subjectUIDs);
		}
		return 0;
	}

	public List<String> getUniqueSubjectUIDsWithTheseUIDs(Study study, Collection subjectUIDs) {
		if (study != null && subjectUIDs != null) {
			return studyDao.getSubjectUIDsThatAlreadyExistWithTheseUIDs(study, subjectUIDs);
		}
		return new ArrayList<String>();// maybe exception actually good here
	}

	public List<LinkSubjectStudy> getUniqueSubjectsWithTheseUIDs(Study study, Collection subjectUIDs) {
		if (study != null && subjectUIDs != null) {
			return studyDao.getSubjectsThatAlreadyExistWithTheseUIDs(study, subjectUIDs);
		}
		return new ArrayList<LinkSubjectStudy>();// maybe exception actually
													// good here
	}

	public List<String> getAllSubjectUIDs(Study study) {
		if (study != null) {
			return studyDao.getAllSubjectUIDs(study);
		}
		return new ArrayList<String>();// maybe exception actually good here
	}

	public List<CustomFieldDisplay> getCustomFieldDisplaysIn(List fieldNameCollection, Study study, ArkFunction arkFunction, CustomFieldGroup customFieldGroup) {
		return studyDao.getCustomFieldDisplaysIn(fieldNameCollection, study, arkFunction, customFieldGroup);
	}

	public List<CustomFieldDisplay> getCustomFieldDisplaysIn(List fieldNameCollection, Study study, ArkFunction arkFunction) {
		return studyDao.getCustomFieldDisplaysIn(fieldNameCollection, study, arkFunction);
	}

	public List<CustomFieldDisplay> getCustomFieldDisplaysIn(Study study, ArkFunction arkFunction) {
		return studyDao.getCustomFieldDisplaysIn(study, arkFunction);
	}

	public List<SubjectCustomFieldData> getSubjectCustomFieldDataFor(List customFieldDisplaysThatWeNeed, List subjectUIDsToBeIncluded) {
		return studyDao.getSubjectCustomFieldDataFor(customFieldDisplaysThatWeNeed, subjectUIDsToBeIncluded);
	}

	public Payload createPayload(byte[] bytes) {
		return studyDao.createPayload(bytes);
	}

	public Payload getPayloadForUpload(Upload upload) {
		return studyDao.getPayloadForUpload(upload);
	}

	public UploadStatus getUploadStatusForUploaded() {
		return studyDao.getUploadStatusFor(Constants.UPLOAD_STATUS_COMPLETED);
	}

	public UploadStatus getUploadStatusForAwaitingValidation() {
		return studyDao.getUploadStatusFor(Constants.UPLOAD_STATUS_AWAITING_VALIDATION);
	}

	public UploadStatus getUploadStatusFor(String uploadStatusConstant) {
		return studyDao.getUploadStatusFor(uploadStatusConstant);
	}

	public Collection<UploadType> getUploadTypesForSubject(Study study) {
		return studyDao.getUploadTypesForSubject(study);
	}

	public Collection<UploadType> getUploadTypesForLims() {
		return studyDao.getUploadTypesForLims();
	}

	public List<CustomField> matchCustomFieldsFromInputFile(FileUpload fileUpload, Study study, ArkFunction arkFunction) {
		return customFieldDao.matchCustomFieldsFromInputFile(fileUpload, study, arkFunction);
	}

	public String getPreviousLastname(Person person) {
		return studyDao.getPreviousLastname(person);
	}

	public void convertLimsBiocollectionCustomDataValuesToKeysForThisStudy(Study study) {
		customFieldDao.convertLimsBiocollectionCustomDataValuesToKeysForThisStudy(study);
	}

	public void convertLimsBiospecimenCustomDataValuesToKeysForThisStudy(Study study) {
		customFieldDao.convertLimsBiospecimenCustomDataValuesToKeysForThisStudy(study);
	}

	public Collection<EmailStatus> getAllEmailStatuses() {
		return studyDao.getAllEmailStatuses();
	}

	public List<Upload> searchUploadsForBiospecimen(Upload uploadCriteria, List studyListForUser) {
		return studyDao.searchUploadsForBiospecimen(uploadCriteria, studyListForUser);
	}

	public List<Search> getSearchesForThisStudy(Study study) {
		return studyDao.getSearchesForThisStudy(study);
	}

	public boolean create(Search search) throws EntityExistsException {

		return studyDao.create(search);
	}

	public boolean update(Search search) throws EntityExistsException {

		return studyDao.update(search);
	}

	public Collection<ConsentStatusField> getAllConsentStatusFields() {
		return studyDao.getAllConsentStatusFields();
	}

	public Collection<DemographicField> getAllDemographicFields() {

		return studyDao.getAllDemographicFields();
	}

	public Collection<BiospecimenField> getAllBiospecimenFields() {

		return studyDao.getAllBiospecimenFields();
	}

	public Collection<BiocollectionField> getAllBiocollectionFields() {

		return studyDao.getAllBiocollectionFields();
	}

	public boolean create(SearchVO search) throws EntityExistsException {
		return studyDao.create(search);
	}

	public boolean update(SearchVO search) throws EntityExistsException {

		return studyDao.update(search);
	}

	public Collection<ConsentStatusField> getSelectedConsentStatusFieldsForSearch(Search search) {
		return studyDao.getSelectedConsentStatusFieldsForSearch(search);
	}

	public Collection<DemographicField> getSelectedDemographicFieldsForSearch(Search search) {
		return studyDao.getSelectedDemographicFieldsForSearch(search);
	}

	public Collection<BiospecimenField> getSelectedBiospecimenFieldsForSearch(Search search) {
		return studyDao.getSelectedBiospecimenFieldsForSearch(search);
	}

	public Collection<BiocollectionField> getSelectedBiocollectionFieldsForSearch(Search search) {
		return studyDao.getSelectedBiocollectionFieldsForSearch(search);
	}

	/*
	 * public Collection<DemographicField>
	 * getSelectedDemographicFieldsForSearch(Search search, boolean readOnly){
	 * return studyDao.getSelectedDemographicFieldsForSearch(search, readOnly);
	 * }
	 */

	public Collection<CustomFieldDisplay> getSelectedPhenoCustomFieldDisplaysForSearch(Search search) {
		return studyDao.getSelectedPhenoCustomFieldDisplaysForSearch(search);
	}

	public Collection<CustomFieldDisplay> getSelectedSubjectCustomFieldDisplaysForSearch(Search search) {
		return studyDao.getSelectedSubjectCustomFieldDisplaysForSearch(search);

	}

	public Collection<CustomFieldDisplay> getSelectedBiospecimenCustomFieldDisplaysForSearch(Search search) {
		return studyDao.getSelectedBiospecimenCustomFieldDisplaysForSearch(search);

	}

	public Collection<CustomFieldDisplay> getSelectedBiocollectionCustomFieldDisplaysForSearch(Search search) {
		return studyDao.getSelectedBiocollectionCustomFieldDisplaysForSearch(search);

	}

	public void runSearch(Long searchId) {
		// String report = studyDao.runSearch();
		studyDao.runSearch(searchId); // I guess it can even capture issues and
										// reports and doesn't need a return
	}

	public void runSearch(Long searchId, String currentUser) {
		// String report = studyDao.runSearch();
		studyDao.runSearch(searchId, currentUser); // I guess it can even
													// capture issues and
													// reports and doesn't need
													// a return
	}

	public void createQueryFilters(List filterList) throws ArkSystemException {
		studyDao.createQueryFilters(filterList);
	}

	public List<QueryFilterVO> getQueryFilterVOs(Search search) {
		return studyDao.getQueryFilterVOs(search);
	}

	public void deleteQueryFilter(QueryFilter queryFilter) {
		studyDao.deleteQueryFilter(queryFilter);
	}

	public List getParentAndChildStudies(Long id) {
		return studyDao.getParentAndChildStudies(id);
	}

	public SearchPayload getSearchPayloadForSearchResult(SearchResult searchResult) {
		return studyDao.getSearchPayloadForSearchResult(searchResult);
	}

	public List<SearchResult> getSearchResultList(Long searchResultId) {
		return studyDao.getSearchResultList(searchResultId);
	}

	public List<Relationship> getFamilyRelationships() {
		return studyDao.getFamilyRelationships();
	}

	public List<SearchSubject> getSearchSubjects() {
		return studyDao.getSearchSubjects();
	}

	public void createSearchSubjects(Search search, List subjectVos) {
		List<SearchSubject> searchSubjects = new ArrayList<SearchSubject>();

		for (Iterator iterator = subjectVos.iterator(); iterator.hasNext();) {
			SubjectVO subjectVo = (SubjectVO) iterator.next();
			SearchSubject searchSubject = new SearchSubject();
			searchSubject.setSearch(search);
			searchSubject.setLinkSubjectStudy(subjectVo.getLinkSubjectStudy());
			searchSubjects.add(searchSubject);
		}
		studyDao.createSearchSubjects(search, searchSubjects);
	}

	public void delete(Search search) {
		// Create Both CustomField and CustomFieldDisplay
		AuditHistory ah = new AuditHistory();
		// Custom Field History
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted Search " + search.getName());
		ah.setEntityId(search.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_SEARCH);
		this.createAuditHistory(ah, SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal().toString(), search.getStudy());
		studyDao.delete(search);
	}

	public List<OtherID> getOtherIDs(Person person) {
		return studyDao.getOtherIDs(person);
	}

	public Collection<PersonLastnameHistory> getPersonLastNameHistory(Person person) {
		return studyDao.getPersonLastnameHistory(person);
	}

	public ConsentStatus getConsentStatusByName(String name) {
		return studyDao.getConsentStatusByName(name);
	}

	public void createPipeline(Pipeline p) {
		genoDao.createPipeline(p);
	}

	public void deletePipeline(Pipeline p) {
		genoDao.deletePipeline(p);
	}

	public void updatePipeline(Pipeline p) {
		genoDao.updatePipeline(p);
	}

	public GenderType getSubjectGenderType(final String subjectUID, final Long studyId) {
		return studyDao.getSubjectGenderType(subjectUID, studyId);
	}

	public int getPipelineCount(Pipeline p) {
		return genoDao.getPipelineCount(p);
	}

	public List<Pipeline> searchPageablePipelines(Pipeline p, int first, int count) {
		return genoDao.searchPageablePipelines(p, first, count);
	}

	public long getPipelineCount(Study study) {
		return genoDao.getPipelineCount(study);
	}

	public int getProcessCount(Process p) {
		return genoDao.getProcessCount(p);
	}

	public List searchPageableProcesses(Process p, int first, int count) {
		return genoDao.searchPageableProcesses(p, first, count);
	}

	public void createProcess(Process p) {
		genoDao.createProcess(p);
	}

	public void deleteProcess(Process p) {
		genoDao.deleteProcess(p);
	}

	public void updateProcess(Process p) {
		genoDao.updateProcess(p);

	}

	public List<Command> getCommands() {
		return genoDao.getCommands();
	}

	public List getProcessInputsForProcess(Process process) {
		return genoDao.getProcessInputsForProcess(process);
	}

	public List getProcessOutputsForProcess(Process process) {
		return genoDao.getProcessOutputsForProcess(process);
	}

	public void createUserConfigs(List userConfigList) throws ArkSystemException {
		studyDao.createUserConfigs(userConfigList);
	}

	public List<ConfigField> getAllConfigFields() {
		return studyDao.getAllConfigFields();
	}

	public List<UserConfig> getUserConfigs(ArkUser arkUser) {
		return studyDao.getUserConfigs(arkUser);
	}
	
	public UserConfig getUserConfig(ArkUser arkUser, ConfigField configField) {
		return studyDao.getUserConfig(arkUser, configField);
	}
	
	public UserConfig getUserConfig(String configName) {
		String currentUser = SecurityUtils.getSubject().getPrincipal().toString();
		ArkUser arkUser = null;
		try {
			arkUser = getArkUser(currentUser);
			
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ConfigField configField = studyDao.getConfigFieldByName(configName);
		
		UserConfig config = studyDao.getUserConfig(arkUser, configField);
		
		return config;
	}
	
	public void deleteUserConfig(UserConfig uc) {
		studyDao.deleteUserConfig(uc);
	}

	public List<CustomField> getCustomFieldsNotInList(List customFieldsFromData, ArkFunction function, Study study) {
		return customFieldDao.getCustomFieldsNotInList(customFieldsFromData, function, study);
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveArkFileAttachment(final Long studyId, final String subjectUID, final String directoryType, final String fileName, final byte[] payload, final String fileId) throws ArkSystemException {

		String directoryName = getArkFileDirName(studyId, subjectUID, directoryType);
		createArkFileAttachmentDirectoy(directoryName);
		createFile(directoryName, fileId, payload);
	}

	/**
	 * {@inheritDoc}
	 */
	public void createArkFileAttachmentDirectoy(String directoryName) throws ArkSystemException {
		File fileDir = new File(directoryName);

		if (!fileDir.exists()) {
			try {
				fileDir.mkdirs();
			} catch (SecurityException se) {
				log.error("Do not have the sufficient permission to access the file directory " + directoryName, se);
				throw new ArkSystemException(se.getMessage());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String getArkFileDirName(final Long studyId, final String subjectUID, final String directoryType) {
		String directoryName = this.fileAttachmentDir + File.separator + studyId + File.separator + directoryType + File.separator + subjectUID;
		return directoryName;
	}

	/**
	 * {@inheritDoc}
	 */
	public String generateArkFileId(String fileName) {
		return System.currentTimeMillis() + "_" + UUID.randomUUID() + "_" + fileName;
	}

	/**
	 * {@inheritDoc}
	 */
	private void createFile(final String directory, final String fileId, final byte[] payload) throws ArkSystemException {
		try {
			File file = new File(directory + File.separator + fileId);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream fos = new FileOutputStream(file);
			fos.write(payload);
			fos.close();

		} catch (IOException e) {
			log.error("Cannot create the file in " + directory, e);
			throw new ArkSystemException(e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public byte[] retriveArkFileAttachmentByteArray(final Long studyId, final String subjectUID, final String directoryType, final String fileId, String checksum) throws ArkSystemException {
		byte[] data = null;
		String directoryName = getArkFileDirName(studyId, subjectUID, directoryType);
		String fileName = directoryName + File.separator + fileId;

		FileInputStream md5input = null;
		FileInputStream fileInput = null;
		boolean fileInputIsNull = true;
		try {
			md5input = new FileInputStream(new File(fileName));
			if (DigestUtils.md5Hex(md5input).equalsIgnoreCase(checksum)) {
				fileInput = new FileInputStream(new File(fileName));
				// Convert file to byte array
				data = IOUtils.toByteArray(fileInput);
				fileInputIsNull = false;
			} else {
				log.error("MD5 Hashes are not matching");
				throw new ArkSystemException("MD5 Hashes are not matching");
			}
		} catch (Exception e) {
			log.error("Error", e);
			throw new ArkSystemException("exception while getting data" + e.getMessage());
		} finally {
			try {
				md5input.close();
				if (!fileInputIsNull) {
					fileInput.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("Error", e);
				throw new ArkSystemException("exception while closing stream" + e.getMessage());
			}
		}
		return data;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean deleteArkFileAttachment(Long studyId, String subjectUID, String fileId, String attachmentType, String checksum) throws ArkSystemException {
		String directory = getArkFileDirName(studyId, subjectUID, attachmentType);
		String location = directory + File.separator + fileId;
		File file = new File(location);
		boolean delete = false;

		FileInputStream md5input = null;

		try {
			md5input = new FileInputStream(file);
			// Check md5 hashes
			if (DigestUtils.md5Hex(md5input).equalsIgnoreCase(checksum)) {
				if (delete = file.delete()) {
					log.info("File deleted successfully");
				} else {
					log.error("Could not find the file in " + location);
					throw new ArkSystemException("Could not find the attachment");
				}
			} else {
				log.error("Checksum -- " + checksum);
				throw new ArkSystemException("Attachment hash value mismatch");
			}
		} catch (Exception e) {
			log.error("Error", e);
			throw new ArkSystemException(e.getMessage());
		} finally {
			try {
				md5input.close();
			} catch (Exception e) {
				log.error("Error", e);
				throw new ArkSystemException(e.getMessage());
			}
		}
		return delete;
	}

	/**
	 * {@inheritDoc}
	 */
	public void copyArkLargeFileAttachments(String sourceFilePath, String destinationFilePath) throws IOException {
		FileChannel source = null;
		FileChannel destination = null;

		try {
			source = new FileInputStream(new File(sourceFilePath)).getChannel();
			destination = new FileOutputStream(new File(destinationFilePath)).getChannel();

			// This fails with Map Failed exception on large files
			// destination.transferFrom(source, 0, source.size());

			ByteBuffer buf = ByteBuffer.allocateDirect(DEFAULT_BUFFER_SIZE);
			while ((source.read(buf)) != -1) {
				buf.flip();
				destination.write(buf);
				buf.clear();
			}
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String generateArkFileChecksum(File file, String algorithm) throws ArkSystemException {
		try (FileInputStream inputStream = new FileInputStream(file)) {
			MessageDigest digest = MessageDigest.getInstance(algorithm);

			byte[] bytesBuffer = new byte[1024];
			int bytesRead = -1;

			while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
				digest.update(bytesBuffer, 0, bytesRead);
			}

			byte[] hashedBytes = digest.digest();

			return convertByteArrayToHexString(hashedBytes);
		} catch (NoSuchAlgorithmException | IOException ex) {
			log.error("Error", ex);
			throw new ArkSystemException("Could not generate hash from file");
		}
	}

	private String convertByteArrayToHexString(byte[] arrayBytes) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < arrayBytes.length; i++) {
			stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return stringBuffer.toString().toUpperCase();
	}

	@Override
	public long getCustomFieldCategoryCount(CustomFieldCategory customFieldCategoryCriteria) {
		 return customFieldDao.getCustomFieldCategoryCount(customFieldCategoryCriteria);
	}

	@Override
	public void createCustomFieldCategory(CustomFieldCategoryVO CustomFieldCategoryVO)throws ArkSystemException, ArkRunTimeUniqueException,ArkRunTimeException {
		try {
			
			AuditHistory ah = new AuditHistory();

			// Force uppercase and replace erroneous characters
			CustomFieldCategoryVO.getCustomFieldCategory().getName().toUpperCase();
			CustomFieldCategoryVO.getCustomFieldCategory().getName().replaceAll(" ", "_");

			/*// Remove any encoded values if DATE or NUMBER
			if (customFieldVO.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE) || customFieldVO.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
				customFieldVO.getCustomField().setEncodedValues(null);
			}*/

			// Field can not have data yet (since it's new)
			//customFieldVO.getCustomField().setCustomFieldHasData(false);
			customFieldDao.createCustomFieldCategory(CustomFieldCategoryVO.getCustomFieldCategory());

			// Custom Field History
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
			ah.setComment("Created Custom " + CustomFieldCategoryVO.getCustomFieldCategory().getName());
			ah.setEntityId(CustomFieldCategoryVO.getCustomFieldCategory().getId());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CUSTOM_FIELD_CATEGORY);

			createAuditHistory(ah);
			
			// Create CustomFieldDisplay only if allowed
			//if (CustomFieldCategoryVO.isUseCustomFieldCategoryDisplay()) {
				// Set the CustomField this CustomFieldDisplay entity is linked
				// to
				//CustomFieldCategoryVO.getCustomFieldDisplay().setCustomField(CustomFieldCategoryVO.getCustomFieldCategory());
				//customFieldDao.createCustomFieldDisplay(customFieldVO.getCustomFieldDisplay());
				// Put in the sequence based on the ID
				//customFieldVO.getCustomFieldDisplay().setSequence(customFieldVO.getCustomFieldDisplay().getId());
				//customFieldDao.updateCustomFieldDisplay(customFieldVO.getCustomFieldDisplay());

				// Custom Field Display History
				//ah = new AuditHistory();
				//ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
				//ah.setComment("Created Custom Field Display" + customFieldVO.getCustomField().getName());
				//ah.setEntityId(customFieldVO.getCustomField().getId());
				//ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CUSTOM_FIELD_DISPLAY);
				//createAuditHistory(ah);
			//}
		} catch (ConstraintViolationException cvex) {
			log.error("Custom Field Category Constrain violation" + cvex);
			if(cvex.getMessage().contains("Duplicate entry")){
				log.error("Custom Field Category Duplicates" + cvex);
				throw new ArkRunTimeUniqueException("A Custom Field Category duplicate value violation");
			}else if(cvex.getMessage().contains("cannot be null")){
				log.error("Custom Field Category field cannot be null" + cvex);
				throw new ArkRunTimeException("A Custom Field Category null violation");
			}
		} catch (Exception ex) {
			log.error("Problem creating Custom Field Category: " + ex);
			throw new ArkSystemException("Problem creating Custom Field Category: " + ex.getMessage());
		}
		
	}

	@Override
	public void updateCustomFieldCategory(CustomFieldCategoryVO CustomFieldCategoryVO)throws ArkSystemException, ArkUniqueException {
		boolean isUnique = customFieldDao.isCustomFieldCategoryUnqiue(CustomFieldCategoryVO.getCustomFieldCategory().getName(), CustomFieldCategoryVO.getCustomFieldCategory().getStudy(), CustomFieldCategoryVO.getCustomFieldCategory());
		if (!isUnique) {
			log.error("Custom Field Category of this name Already Exists.: ");
			throw new ArkUniqueException("A Custom Field Category of this name already exists.");
		}
		try {
			// Remove any encoded values if DATE or NUMBER
			//if (CustomFieldCategoryVO.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE) || customFieldVO.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
			//	CustomFieldCategoryVO.getCustomField().setEncodedValues(null);
			//}

			customFieldDao.updateCustomFieldCategory(CustomFieldCategoryVO.getCustomFieldCategory());
			// Custom Field History
			AuditHistory ah = new AuditHistory();
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
			ah.setComment("Updated Custom Field Category" + CustomFieldCategoryVO.getCustomFieldCategory().getName());
			ah.setEntityId(CustomFieldCategoryVO.getCustomFieldCategory().getId());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CUSTOM_FIELD_CATEGORY);
			createAuditHistory(ah);

			/*// Only Update CustomFieldDisplay when it is allowed
			if (customFieldVO.isUseCustomFieldDisplay()) {
				customFieldVO.getCustomFieldDisplay().setCustomField(customFieldVO.getCustomField());
				customFieldDao.updateCustomFieldDisplay(customFieldVO.getCustomFieldDisplay());
				// Custom Field Display History
				ah = new AuditHistory();
				ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
				ah.setComment("Updated Custom Field Display " + customFieldVO.getCustomField().getName());
				ah.setEntityId(customFieldVO.getCustomField().getId());
				ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CUSTOM_FIELD_DISPLAY);
				createAuditHistory(ah);
			}*/

		} catch (ConstraintViolationException cvex) {
			log.error("Custom Field Category Already Exists.: " + cvex);
			throw new ArkUniqueException("A Custom Field Category already exits.");
		} catch (Exception ex) {
			log.error("Problem updating Custom Field: " + ex);
			throw new ArkSystemException("Problem updating Custom Field Category: " + ex.getMessage());
		}
		
	}

	@Override
	public void deleteCustomFieldCategory(CustomFieldCategoryVO customFieldCategoryVO)throws EntityCannotBeRemoved, ArkSystemException {
		String fieldName = customFieldCategoryVO.getCustomFieldCategory().getName();
		if(customFieldDao.isThisCustomCategoryWasAParentCategoryOfAnother(customFieldCategoryVO.getCustomFieldCategory())){
					throw new EntityCannotBeRemoved("Can not delete Custom field Category which already assiged as parent category.");
		}else{
					customFieldDao.deleteCustomFieldCategory(customFieldCategoryVO.getCustomFieldCategory());
					// History for Custom Field Category
					AuditHistory ah = new AuditHistory();
					ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
					ah.setComment("Deleted Custom Field Category " + fieldName);
					ah.setEntityId(customFieldCategoryVO.getCustomFieldCategory().getId());
					ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CUSTOM_FIELD_CATEGORY);
					createAuditHistory(ah);
				}
	}

	@Override
	public CustomFieldCategory getCustomFieldCategory(Long id) {
		return customFieldDao.getCustomFieldCategory(id);
	}

	@Override
	public List<CustomFieldCategory> searchPageableCustomFieldCategories(CustomFieldCategory customFieldCategoryCriteria, int first,int count) {
		return customFieldDao.searchPageableCustomFieldCategories(customFieldCategoryCriteria, first, count);
	}

	@Override
	public List<CustomFieldCategory> getParentCategoryListByCustomFieldType(Study study,ArkFunction arkFunction, CustomFieldType customFieldType)throws ArkSystemException {
		return customFieldDao.getParentCategoryListByCustomFieldType(study,arkFunction,customFieldType);
	}

	@Override
	public List getAvailableAllCategoryListByCustomFieldTypeExceptThis(Study study,ArkFunction arkFunction, CustomFieldType customFieldType,CustomFieldCategory thisCustomFieldCategory)throws ArkSystemException {
		return customFieldDao.getAvailableAllCategoryListByCustomFieldTypeExceptThis(study, arkFunction, customFieldType,thisCustomFieldCategory);
	}

	@Override
	public List getAvailableAllCategoryListByCustomFieldType(Study study,ArkFunction arkFunction, CustomFieldType customFieldType)throws ArkSystemException {
		return customFieldDao.getAvailableAllCategoryListByCustomFieldType(study, arkFunction, customFieldType);
	}

	@Override
	public List getCategoriesListInCustomFieldsByCustomFieldType(Study study,ArkFunction arkFunction, CustomFieldType customFieldType)throws ArkSystemException {
		return customFieldDao.getCategoriesListInCustomFieldsByCustomFieldType(study, arkFunction, customFieldType);
	}

	@Override
	public List getAvailableAllCategoryListInStudyByCustomFieldType(Study study, ArkFunction arkFunction,CustomFieldType customFieldType)throws ArkSystemException {
		return customFieldDao.getAvailableAllCategoryListInStudyByCustomFieldType(study,arkFunction, customFieldType);
	}

	@Override
	public CustomFieldType getCustomFieldTypeByName(String name) {
		return customFieldDao.getCustomFieldTypeByName(name);
	}
	
	@Override
	public List getCustomFieldTypes(ArkModule arkModule) {
		return customFieldDao.getCustomFieldTypes(arkModule);
	}
	@Override
	public List<UploadLevel> getAllUploadLevels(){
		return customFieldDao.getAllUploadLevels();
	}

	@Override
	public List getCustomFieldCategoryByCustomFieldTypeAndStudy(Study study,CustomFieldType customFieldType) {
		return customFieldDao.getCustomFieldCategoryByCustomFieldTypeAndStudy(study, customFieldType);
	}

	@Override
	public CustomFieldCategory getCustomFieldCategotyByName(String name) {
		return customFieldDao.getCustomFieldCategotyByName(name);
	}
	@Override
	public UploadLevel getUploadLevelByName(String name){
		return customFieldDao.getUploadLevelByName(name);
	};
	@Override
	public CustomFieldCategory getCustomFieldCategoryByNameStudyAndArkFunction(String name,Study study,ArkFunction arkFunction){
		return customFieldDao.getCustomFieldCategoryByNameStudyAndArkFunction(name, study, arkFunction);
	}

	@Override
	public boolean isCustomFieldCategoryBeingUsed(CustomFieldCategory customFieldCategory) {
			return customFieldDao.isCustomFieldCategoryBeingUsed(customFieldCategory);
	}
	@Override
	public void createCustomFieldCategoryUpload(CustomFieldCategoryUpload cfcUpload){
			studyDao.createCustomFieldCategoryUpload(cfcUpload);
	}

	@Override
	public List<String> getAllFamilyUIDs(Study study) {
		return studyDao.getAllFamilyUIDs(study);
	}

	@Override
	public List getFamilyCustomFieldDataFor(Study study,List customFieldDisplaysThatWeNeed,List familyUIDsToBeIncluded) {
		return studyDao.getfamilyCustomFieldDataFor(study,customFieldDisplaysThatWeNeed, familyUIDsToBeIncluded);
	}
	@Override
	public List<CustomFieldDisplay> getCustomFieldDisplaysInWithCustomFieldType(List fieldNameCollection, Study study, ArkFunction arkFunction,CustomFieldType customFieldType) {
		return studyDao.getCustomFieldDisplaysInWithCustomFieldType(fieldNameCollection, study, arkFunction,customFieldType);
	}

	@Override
	public List getAllChildrenCategoriedBelongToThisParent(Study study,ArkFunction arkFunction, CustomFieldType customFieldType,CustomFieldCategory parentCategory, List allChildrenLst) {
		List<CustomFieldCategory> immediateSubCategories=customFieldDao.getAllSubCategoriesOfThisCategory(study,arkFunction,customFieldType,parentCategory);
		if(!immediateSubCategories.isEmpty()){
			allChildrenLst.addAll(immediateSubCategories);
				for (CustomFieldCategory customFieldCategory : immediateSubCategories) {
					allChildrenLst.addAll(getAllChildrenCategoriedBelongToThisParent(study, arkFunction, customFieldType, customFieldCategory,allChildrenLst));
				}
		}
		return allChildrenLst;
	}

	@Override
	public List getSiblingList(Study study, ArkFunction arkFunction,CustomFieldType customFieldType,CustomFieldCategory customFieldCategory) {
		return customFieldDao.getSiblingList(study, arkFunction, customFieldType, customFieldCategory);
	}

	@Override
	public List<ArkRolePolicyTemplate> getArkRolePolicytemplateList(ArkUserVO arkUserVO) {
		List<ArkRolePolicyTemplate> allArkRolePolicyTemplates=new ArrayList<ArkRolePolicyTemplate>();
		List<ArkUserRole> roleLst=arkUserVO.getArkUserRoleList();
		for (ArkUserRole arkUserRole :roleLst) {
				allArkRolePolicyTemplates.addAll((List<ArkRolePolicyTemplate>)arkAuthorisationDao.getArkRolePolicytemplateList(arkUserRole));
		}
		log.info("RoleSize:"+roleLst.size());
		log.info("Function Size:"+removeDuplicates(allArkRolePolicyTemplates).size());
		return removeDuplicates(allArkRolePolicyTemplates);
		
		 
	}
	@Override
	public List<ArkPermission> getArkPremissionListForRoleAndModule(ArkRolePolicyTemplate arkRolePolicyTemplate){
		return arkAuthorisationDao.getArkPremissionListForRoleAndModule(arkRolePolicyTemplate);
	}
	/**
	 * Remove duplicates from list
	 * @param customFieldLst
	 * @return
	 */
	private  List<ArkRolePolicyTemplate> removeDuplicates(List<ArkRolePolicyTemplate> fieldLst){
		 return new ArrayList<ArkRolePolicyTemplate>(new LinkedHashSet<ArkRolePolicyTemplate>(fieldLst));
		
	}

	@Override
	public UploadType getUploadTypeByModuleAndName(ArkModule arkModule,String name) {
		return studyDao.getUploadTypeByModuleAndName(arkModule, name);
	}

	
	
	
}
