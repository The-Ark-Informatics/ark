package au.org.theark.study.web.component.study;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import au.org.theark.study.model.entity.Study;

@SuppressWarnings("serial")
public class StudyModel implements Serializable{

	private Study study;
	private Set<String> lmcAvailableApps;
	private Set<String> lmcSelectedApps;
	private List<Study> studyList;
	
	
	public StudyModel(){
		study = new Study();
		lmcAvailableApps = new HashSet<String>();
		lmcSelectedApps = new HashSet<String>();
		studyList = new ArrayList<Study>();
	}
	
	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public Set<String> getLmcAvailableApps() {
		return lmcAvailableApps;
	}
	public void setLmcAvailableApps(Set<String> lmcAvailableApps) {
		this.lmcAvailableApps = lmcAvailableApps;
	}
	public Set<String> getLmcSelectedApps() {
		return lmcSelectedApps;
	}
	public void setLmcSelectedApps(Set<String> lmcSelectedApps) {
		this.lmcSelectedApps = lmcSelectedApps;
	}

	public List<Study> getStudyList() {
		return studyList;
	}

	public void setStudyList(List<Study> studyList) {
		this.studyList = studyList;
	}
	
}
