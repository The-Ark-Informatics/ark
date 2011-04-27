package au.org.theark.core.service;

import java.util.Collection;
import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.AddressStatus;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUsecase;
import au.org.theark.core.model.study.entity.AuditHistory;
import au.org.theark.core.model.study.entity.ConsentAnswer;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CountryState;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonContactMethod;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyCompStatus;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.SubjectUidPadChar;
import au.org.theark.core.model.study.entity.SubjectUidToken;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.SubjectVO;

public interface IArkCommonService {
	
	//Place here any common services that must be visible to sub-applications
	//Get reference data etc.get study maybe required but sub-applications access a study via ETA Study module
	public ArkUserVO getUser(String name) throws ArkSystemException ;
	
	public List<String> getUserRole(String userName) throws ArkSystemException;
	
	
	public List<Study> getStudy(Study study);
	
	public List<StudyStatus> getListOfStudyStatus();
	
	public Study getStudy(Long id);
	
	public Collection<SubjectVO> getSubject(SubjectVO subjectVO);

	public Collection<SubjectStatus> getSubjectStatus();
	
	public Collection<TitleType> getTitleType();
	
	public Collection<VitalStatus> getVitalStatus();
	
	public Collection<GenderType> getGenderType();
	
	public List<PhoneType> getListOfPhoneType();
	
	/**
	 * <p>
	 * An interface that will return a LinkSubjectStudy instance which represents a Subject when provided with
	 * a Subject Unique Identifier.In the event that the system does not find a subject with the given identifier
	 * the interface will throw an EntityNotFoundException.</p>
	 * @param subjectUID
	 * @return LinkSubjectStudy
	 * @throws EntityNotFoundException
	 */
	public LinkSubjectStudy getSubjectByUID(String subjectUID) throws EntityNotFoundException;
	
	public Collection<MaritalStatus>  getMaritalStatus();
	
	public List<Country> getCountries();
	
	public Country getCountry(String countryCode);
	
	public List<CountryState>  getStates(Country country);
	
	/**
	 * Looks up all address types
	 * @return
	 */
	public List<AddressType> getAddressTypes();
	
	/**
	 * Looks up all address statuses
	 * @return
	 */
	public List<AddressStatus> getAddressStatuses();

	/**
	 * 
	 * @return a list of Consent Status
	 */
	public List<ConsentStatus> getConsentStatus();
	
	/**	
	 * @return a list of Study Components
	 */
	public List<StudyComp> getStudyComponent();	
	
	public List<ConsentType> getConsentType();
	
	public List<StudyCompStatus> getStudyComponentStatus();
	
	public List<ConsentAnswer> getConsentAnswer();
	
	public List<YesNo> getYesNoList();
	
	public void createAuditHistory(AuditHistory auditHistory);

	public List<PersonContactMethod> getPersonContactMethodList();
	
	public boolean  isSubjectConsentedToComponent(StudyComp studyComponent, Person subject, Study study);
	
	/**
	 * Returns a LinkSubjectStudy via the personId represented by the Long
	 * @param personId
	 * @return
	 * @throws EntityNotFoundException
	 */
	public LinkSubjectStudy getSubject(Long personId) throws EntityNotFoundException;

	public List<SubjectUidPadChar> getListOfSubjectUidPadChar();
	
	public String getSubjectUidExample(Study study);

	public Long getSubjectCount(Study study);

	public List<SubjectUidToken> getListOfSubjectUidToken();
	
	/*Access User Roles from Backend */
	
	/**
	 * This interface checks if the user is a Super Administrator 
	 */
	public boolean isSuperAdministrator(String userName)  throws EntityNotFoundException ;
	
	/**
	 * This interface checks if the user is a Administrator in the Ark System.
	 * @param userName
	 * @return
	 */
	public boolean isAdministator(String userName)  throws EntityNotFoundException ;
	
	/**
	 * Returns a Collection User Admin Roles. Super Administrator or Administator
	 * @param ldapUserName
	 * @return
	 * @throws EntityNotFoundException
	 */
	public Collection<String> getUserAdminRoles(String ldapUserName) throws EntityNotFoundException;
	
	/**
	 * For a Given study and Ldap User name get the Role he has been assigned. 
	 * @param ldapUserName
	 * @param study
	 * @return String that represents the Role for this study
	 * @throws EntityNotFoundException
	 */
	public String getUserRoleForStudy(String ldapUserName,Study study) throws EntityNotFoundException;


	/**
	 * Returns an instance of GenderType for a given String that represents a gender type name. The name should match
	 * the name in the database Gender Type.
	 * @param name
	 * @return
	 */
	public GenderType getGenderType(String name);

	/**
	 * Returns an instance of VitalStatus for a given String that represents a Vital Status. The name should match
	 * the name in the database table vital_status.
	 * @param name
	 * @return
	 */
	public VitalStatus getVitalStatus(String name);

	/**
	 * Returns an instance of Title Type for a given String that represents a gender type name. The name should match
	 * the name in the database title_type
	 * @param name
	 * @return
	 */
	public TitleType getTitleType(String name);
	
	/**
	 * Returns an instance of Marital Status for a given String that represents a gender type name. The name should match
	 * the name in the database marital_status
	 * @param name
	 * @return
	 */

	public MaritalStatus getMaritalStatus(String name);
	
	/**
	 * Returns an instance of PersonContactMethod for a given String that represents a Contact method. The name should match
	 * the name in the database person_contact_method
	 * @param name
	 * @return
	 */
	public PersonContactMethod getPersonContactMethod(String name);

	/**
	 * Returns an instance of Subject Status for a given String that represents a gender type name. The name should match
	 * the name in the database subject_status.
	 * @param name
	 * @return
	 */

	public SubjectStatus getSubjectStatus(String name);
	
	/**
	 *Returns a ArkUsecase instance for a given String that represents a usecase name.The name should match
	 *the name in the database table ark_usecase.
	 *@return ArkUsecase
	 */
	public ArkUsecase getArkUsecaseByName(String usecaseName);
	
	/**
	 * Returns a ArkModule instance for a given String that represents a module name.The name should match
	 * the name in the database table ark_module
	 * @return
	 */
	public ArkModule getArkModuleByName(String moduleName);
	
	/**
	 * Returns a String that represents a Role for a given Ldap User Name, ArkUsecase, ArkModule and Study.
	 * The LdapUserName,ArkUsecase are mandatory for a successful retrieval of a role.
	 * @param ldapUserName
	 * @param arkUseCase
	 * @param arkModule
	 * @param study
	 * @return String
	 * @throws EntityNotFoundException
	 */
	public String getUserRole(String ldapUserName,ArkUsecase arkUseCase, ArkModule arkModule,Study study) throws EntityNotFoundException;
	
	/**
	 * Returns a ArkUsecase instance when provided a Long id that represents a valid use case id.
	 * @param usecaseId
	 * @return
	 */
	public ArkUsecase getArkUsecaseById(Long usecaseId);
	
	/**
	 * Returns a ArkModule instance when provided a Long id that represents a valid module  id.
	 * @param usecaseId
	 * @return
	 */
	public ArkModule getArkModuleById(Long moduleId);
	
	/**
	 * 
	 * @param ldapUserName
	 * @param arkUseCase
	 * @param userRole
	 * @param arkModule
	 * @param study
	 * @throws EntityNotFoundException
	 */
	public Collection<String> getArkUserRolePermission(String ldapUserName,ArkUsecase arkUseCase,String userRole, ArkModule arkModule,Study study) throws EntityNotFoundException;
	
	/**
	 * Returns All Permissions as collection of Strings
	 * @return  Collection<String> that represents ArkPermission
	 */
	public Collection<String> getArkPermission();
}
