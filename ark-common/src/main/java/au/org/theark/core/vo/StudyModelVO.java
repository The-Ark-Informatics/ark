package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.Study;

@SuppressWarnings("serial")
public class StudyModelVO implements Serializable{

	private Study study;
	private Set<String> lmcAvailableApps;
	private Set<String> lmcSelectedApps;
	private List<Study> studyList;
	private Collection<ModuleVO> modulesAvailable;
	private Collection<ModuleVO> modulesSelected;
	
	private Collection<ArkModule> availableArkModules;
	private Collection<ArkModule> selectedArkModules;
	
	private String studySummaryLabel;
	private String subjectUidExample;
	
	public StudyModelVO(){
		study = new Study();
		lmcAvailableApps = new HashSet<String>();
		lmcSelectedApps = new HashSet<String>();
		studyList = new ArrayList<Study>();
		modulesAvailable = new ArrayList<ModuleVO>();
		modulesSelected = new ArrayList<ModuleVO>();
		availableArkModules = new ArrayList<ArkModule>();
		selectedArkModules = new ArrayList<ArkModule>();
		setSubjectUidExample(new String());
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

	/**
	 * @param studySummaryLabel the studySummaryLabel to set
	 */
	public void setStudySummaryLabel(String studySummaryLabel)
	{
		this.studySummaryLabel = studySummaryLabel;
	}

	/**
	 * @return the studySummaryLabel
	 */
	public String getStudySummaryLabel()
	{
		return study.getName();
	}
	
	/**
	 * @return the subjectUidExample
	 */
	public String getSubjectUidExample()
	{
		return subjectUidExample;
	}
	
	/**
	 * @param subjectUidExample the subjectUidExample to set
	 */
	public void setSubjectUidExample(String subjectUidExample)
	{
		this.subjectUidExample = subjectUidExample;
	}

	public Collection<ArkModule> getAvailableArkModules() {
		return availableArkModules;
	}

	public void setAvailableArkModules(Collection<ArkModule> availableArkModules) {
		this.availableArkModules = availableArkModules;
	}

	public Collection<ArkModule> getSelectedArkModules() {
		return selectedArkModules;
	}

	public void setSelectedArkModules(Collection<ArkModule> selectedArkModules) {
		this.selectedArkModules = selectedArkModules;
	}
}
