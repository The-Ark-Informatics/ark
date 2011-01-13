package au.org.theark.core.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.Constants;
import au.org.theark.core.dao.ILdapPersonDao;
import au.org.theark.core.dao.IStudyDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CountryState;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.SubjectVO;
/**
 * The implementation of IArkCommonService. We want to auto-wire and hence use the @Service annotation.
 * 
 * @author nivedann
 *
 */

@Transactional
@Service(Constants.ARK_COMMON_SERVICE)
public class ArkCommonServiceImpl implements IArkCommonService{

	private ILdapPersonDao ldapInterface;
	private IStudyDao studyDao;
	
	
	public IStudyDao getStudyDao() {
		return studyDao;
	}

	@Autowired
	public void setStudyDao(IStudyDao studyDao) {
		this.studyDao = studyDao;
	}
	
	@Autowired
	public void setLdapInterface(ILdapPersonDao ldapInterface) {
		this.ldapInterface = ldapInterface;
	}


	public ArkUserVO getUser(String name) throws ArkSystemException {
		return ldapInterface.getUser(name);
	}
	
	public List<String> getUserRole(String userName) throws ArkSystemException{
		return ldapInterface.getUserRole(userName);
	}


	/* (non-Javadoc)
	 * @see au.org.theark.core.service.IArkCommonService#getListOfStudyStatus()
	 */
	public List<StudyStatus> getListOfStudyStatus() {
		return studyDao.getListOfStudyStatus();
	}


	/* (non-Javadoc)
	 * @see au.org.theark.core.service.IArkCommonService#getStudy(au.org.theark.core.model.study.entity.Study)
	 */
	public List<Study> getStudy(Study study) {
		
		return studyDao.getStudy(study);
	}


	/* (non-Javadoc)
	 * @see au.org.theark.core.service.IArkCommonService#getStudy(java.lang.Long)
	 */
	public Study getStudy(Long id) {

		return studyDao.getStudy(id);
	}


	/* (non-Javadoc)
	 * @see au.org.theark.core.service.IArkCommonService#getSubject(au.org.theark.core.vo.SubjectVO)
	 */
	public Collection<SubjectVO> getSubject(SubjectVO subjectVO) {
		
		return studyDao.getSubject(subjectVO);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.service.IArkCommonService#getGenderType()
	 */
	public Collection<GenderType> getGenderType() {
		
		return studyDao.getGenderType();
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.service.IArkCommonService#getListOfPhoneType()
	 */
	public List<PhoneType> getListOfPhoneType() {
		
		return studyDao.getListOfPhoneType();
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.service.IArkCommonService#getSubjectStatus()
	 */
	public Collection<SubjectStatus> getSubjectStatus() {
		
		return studyDao.getSubjectStatus();
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.service.IArkCommonService#getTitleType()
	 */
	public Collection<TitleType> getTitleType() {
		
		return studyDao.getTitleType();
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.service.IArkCommonService#getVitalStatus()
	 */
	public Collection<VitalStatus> getVitalStatus() {
		
		return studyDao.getVitalStatus();
	}
	
	public LinkSubjectStudy getSubjectByUID(String subjectUID) throws EntityNotFoundException{
	
		return studyDao.getSubjectByUID(subjectUID);
	}
	
	public Collection<MaritalStatus> getMaritalStatus(){
		return studyDao.getMaritalStatus();
	}
	
	public List<Country> getCountries(){
		return studyDao.getCountries();
	}
	
	public List<CountryState>  getStates(Country country){
		 return studyDao.getStates(country);
	}
	
	public List<AddressType> getAddressTypes(){
		return studyDao.getAddressTypes();
	}

}
