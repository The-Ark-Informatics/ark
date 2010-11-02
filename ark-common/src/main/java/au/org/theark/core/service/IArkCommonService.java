package au.org.theark.core.service;

import java.util.Collection;
import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.Study;
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
	
}
