package au.org.theark.report.model.dao;

import java.util.Collection;
import java.util.List;

import au.org.theark.core.exception.ArkSubjectInsertException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.CorrespondenceDirectionType;
import au.org.theark.core.model.study.entity.CorrespondenceModeType;
import au.org.theark.core.model.study.entity.CorrespondenceOutcomeType;
import au.org.theark.core.model.study.entity.CorrespondenceStatusType;
import au.org.theark.core.model.study.entity.Correspondences;
import au.org.theark.core.model.study.entity.ConsentFile;
import au.org.theark.core.model.study.entity.DelimiterType;
import au.org.theark.core.model.study.entity.FileFormat;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonLastnameHistory;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.StudyUpload;
import au.org.theark.core.model.study.entity.SubjectCustmFld;
import au.org.theark.core.model.study.entity.SubjectFile;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.report.model.entity.ReportSecurity;


public interface IReportDao {

	public Study getStudy(Long id);
	public Collection<ReportSecurity> getReportSecurity(Study study, ArkUser arkUser);
	
}
