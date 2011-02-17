package au.org.theark.core.service;

import java.util.Collection;
import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
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
	
	public List<CountryState>  getStates(Country country);
	
	/**
	 * Looks up all address types
	 * @return
	 */
	public List<AddressType> getAddressTypes();
	
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
	
	
	
	
}
