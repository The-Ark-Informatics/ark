package au.org.theark.core.dao;

import java.util.Collection;
import java.util.List;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.model.study.entity.AddressStatus;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.ArkRole;
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
import au.org.theark.core.model.study.entity.PersonLastnameHistory;
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
import au.org.theark.core.vo.SubjectVO;

/**
 * @author nivedann
 *
 */
public interface IStudyDao {
	
	/**
	 * Looks up a Study based on the filter supplied as part of the Study. 
	 * @param study
	 * @return
	 */
	public List<Study> getStudy(Study study);
	
	/**
	 * Interface to get a list of Study Status reference data from the backend.
	 * These study status' are no associated with a study as such but can be used for
	 * displaying a list of options for a particular study.
	 * @return
	 */
	public List<StudyStatus> getListOfStudyStatus();
	
	/**
	 * Fetch a Study entity by providing the primary key of the study.
	 * @param id
	 * @return
	 */
	public Study getStudy(Long id);
	
	/**
	 * An interace to return a StudyStatus entity by providing the status name.
	 * @param statusName
	 * @return
	 * @throws StatusNotAvailableException
	 */
	
	public StudyStatus getStudyStatus(String statusName) throws StatusNotAvailableException;
	
	/**
	 * A look up that returns a list of All Phone Types. Mobile, Land etc
	 * In the event that there is a database/runtime error it is wrapped into a ArkSystemException and returned
	 * @return List<PhoneType>
	 */
	public List<PhoneType> getListOfPhoneType();
	
	/**
	 * An interface fetch a list of Title types. 
	 * @return
	 */
	public Collection<TitleType> getTitleType();
	
	/**
	 * An interace to fetch a list of Vital Status reference types.
	 * @return
	 */
	public Collection<VitalStatus> getVitalStatus();
	
	/**
	 * An interface to fetch a list of GenderType references.
	 * @return
	 */
	public Collection<GenderType> getGenderType();
	
	/**
	 * An interface to return a list of Subject Status types
	 * @return
	 */
	public Collection<SubjectStatus> getSubjectStatus();
	
	/**
	 * A look-up method that searches based on the Filter provided in the incoming 
	 * SubjectVO value object.
	 * @param subjectVO
	 * @return
	 */
	public Collection<SubjectVO> getSubject(SubjectVO subjectVO);
	
	
	/**
	 * Returns a LinkSubjectStudy entity by comparing the Long personId  with LinkSubjectStudy entity.
	 * The Long id passed in represents the Person primary key or personId.
	 * @return LinkSubjectStudy
	 * @throws EntityNotFoundException
	 */
	public LinkSubjectStudy getSubject(Long personId) throws EntityNotFoundException;
	
	/**
	 * Look up a list of subjects linked to the current study by passing in a primary key of the
	 * LinkSubjectStudy. 
	 * @param id
	 * @return LinkSubjectStudy
	 * @throws EntityNotFoundException
	 */
	public LinkSubjectStudy getLinkSubjectStudy(Long linkSubjectStudyId) throws EntityNotFoundException;
	
	/**
	 * Return a Subject based on a Unique Identifier. In the event it does not locate the specified subject by the Id
	 * it will throw a  EntityNotFoundException
	 * @param subjectUID
	 * @return LinkSubjectStudy
	 * @throws EntityNotFoundException
	 */
	public LinkSubjectStudy getSubjectByUID(String subjectUID) throws EntityNotFoundException;
	
	/**
	 * Get a list of Marital Status
	 * @return
	 */
	public Collection<MaritalStatus> getMaritalStatus();
	
	/**
	 * Returns a list of all countries.
	 * @return
	 */
	public List<Country> getCountries();
	
	
	public Country getCountry(String countryCode);
	
	/**
	 * Given a country retrieves a list of States linked to it
	 * @param country
	 * @return
	 */
	public List<CountryState>  getStates(Country country);
	
	/**
	 * Returns a list of Address Types
	 * @return
	 */
	public List<AddressType> getAddressTypes();
	
	/**
	 * Returns a list of Address Statuses
	 * @return
	 */
	public List<AddressStatus> getAddressStatuses();
	
	/**
	 * Returns a Collection of Consent Status
	 * @return
	 */
	public List<ConsentStatus> getConsentStatus();
	
	/**
	 * 
	 * @return
	 */
	public List<StudyCompStatus> getStudyComponentStatus();

	/**
	 * 
	 * @return
	 */
	public List<StudyComp> getStudyComponent();
	
	/**
	 * Returns a list of Consent types hardcopy, electronic document etc.
	 * @return
	 */
	public List<ConsentType> getConsentType();
	
	
	public List<ConsentAnswer> getConsentAnswer();
	
	public List<YesNo> getYesNoList();
	
	public void createAuditHistory(AuditHistory auditHistory);

	public List<PersonContactMethod> getPersonContactMethodList();
	
	public boolean  isSubjectConsentedToComponent(StudyComp studyComponent, Person subject, Study study);

	public void createPersonLastnameHistory(Person person);
	
	public void updatePersonLastnameHistory(Person person);
	
	/**
	 * Returns previousSurnameHistory
	 * @return
	 */
	public PersonLastnameHistory getPreviousSurnameHistory(PersonLastnameHistory personSurnameHistory);
	
	/**
	 * Returns previous surname
	 * @return
	 */
	public String getPreviousLastname(Person person);
	
	/**
	 * Returns current surname
	 * @return
	 */
	public String getCurrentLastname(Person person);
	
	/**
	 * Returns a list of PersonSurnameHistory
	 * @return
	 */
	public List<PersonLastnameHistory> getLastnameHistory(Person person);

	public List<SubjectUidPadChar> getListOfSubjectUidPadChar();
	
	public String getSubjectUidExample(Study study);
	
	public Long getSubjectCount(Study study);

	public List<SubjectUidToken> getListOfSubjectUidToken();
	
	/**
	 * This interface checks if the user is a Super Administrator 
	 */
	public boolean isSuperAdministrator(String userName) throws EntityNotFoundException;
	
	/**
	 * This interface checks if the user is a Administrator in the Ark System.
	 * @param userName
	 * @return
	 */
	public boolean isAdministator(String userName) throws EntityNotFoundException;
	
	/**
	 * The interface that returns a list of ArkRole objects from the Database
	 * @return
	 */
	public List<ArkRole> getArkRoles();
	
	
	public GenderType getGenderType(String name);

	public VitalStatus getVitalStatus(String name);

	public TitleType getTitleType(String name);
	
	public MaritalStatus getMaritalStatus(String name);
	
	public PersonContactMethod getPersonContactMethod(String name);

	public SubjectStatus getSubjectStatus(String name);
	
	
}