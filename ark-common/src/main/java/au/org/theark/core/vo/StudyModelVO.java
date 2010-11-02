package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import au.org.theark.core.model.study.entity.Study;

@SuppressWarnings("serial")
public class StudyModelVO implements Serializable{

	private Study study;
	private Set<String> lmcAvailableApps;
	private Set<String> lmcSelectedApps;
	private List<Study> studyList;
	private Collection<ModuleVO> modulesAvailable;
	private Collection<ModuleVO> modulesSelected;
	private String studySummaryLabel;
	
	public StudyModelVO(){
		study = new Study();
		lmcAvailableApps = new HashSet<String>();
		lmcSelectedApps = new HashSet<String>();
		studyList = new ArrayList<Study>();
		modulesAvailable = new ArrayList<ModuleVO>();
		modulesSelected = new ArrayList<ModuleVO>();
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

	public Collection<ModuleVO> getModulesAvailable() {
		return modulesAvailable;
	}

	public void setModulesAvailable(Collection<ModuleVO> modulesAvailable) {
		this.modulesAvailable = modulesAvailable;
	}

	public Collection<ModuleVO> getModulesSelected() {
		return modulesSelected;
	}

	public void setModulesSelected(Collection<ModuleVO> modulesSelected) {
		this.modulesSelected = modulesSelected;
	}

	public String getStudySummaryLabel() {
		return study.getName();
	}

	public void setStudySummaryLabel(String studySummaryLabel) {
		this.studySummaryLabel = studySummaryLabel;
	}
	
}
