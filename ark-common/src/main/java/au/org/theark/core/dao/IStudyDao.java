package au.org.theark.core.dao;

import java.util.Collection;
import java.util.List;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.ConsentAnswer;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CountryState;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyCompStatus;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
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
	
	
	

}
