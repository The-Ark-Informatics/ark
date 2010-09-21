package au.org.theark.study.web.component.study;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import au.org.theark.study.model.entity.Study;
import au.org.theark.study.web.form.ModuleVo;

@SuppressWarnings("serial")
public class StudyModel implements Serializable{

	private Study study;
	private Set<String> lmcAvailableApps;
	private Set<String> lmcSelectedApps;
	private List<Study> studyList;
	private Collection<ModuleVo> modulesAvailable;
	private Collection<ModuleVo> modulesSelected;
	private String studySummaryLabel;
	
	public StudyModel(){
		study = new Study();
		lmcAvailableApps = new HashSet<String>();
		lmcSelectedApps = new HashSet<String>();
		studyList = new ArrayList<Study>();
		modulesAvailable = new ArrayList<ModuleVo>();
		modulesSelected = new ArrayList<ModuleVo>();
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

	public Collection<ModuleVo> getModulesAvailable() {
		return modulesAvailable;
	}

	public void setModulesAvailable(Collection<ModuleVo> modulesAvailable) {
		this.modulesAvailable = modulesAvailable;
	}

	public Collection<ModuleVo> getModulesSelected() {
		return modulesSelected;
	}

	public void setModulesSelected(Collection<ModuleVo> modulesSelected) {
		this.modulesSelected = modulesSelected;
	}

	public String getStudySummaryLabel() {
		return study.getName();
	}

	public void setStudySummaryLabel(String studySummaryLabel) {
		this.studySummaryLabel = studySummaryLabel;
	}
	
}
