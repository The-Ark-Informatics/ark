package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;



public class StudyStatusVo implements Serializable{
	
	private Long studyStatusKey;
	private String name;
	private String description;
	private Set<StudyVO> studies = new HashSet<StudyVO>(0);
	public Long getStudyStatusKey() {
		return studyStatusKey;
	}
	public void setStudyStatusKey(Long studyStatusKey) {
		this.studyStatusKey = studyStatusKey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Set<StudyVO> getStudies() {
		return studies;
	}
	public void setStudies(Set<StudyVO> studies) {
		this.studies = studies;
	}


}
