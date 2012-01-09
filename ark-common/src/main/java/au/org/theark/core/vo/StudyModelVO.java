/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import au.org.theark.core.model.lims.entity.BiospecimenUidTemplate;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.Study;

@SuppressWarnings("serial")
public class StudyModelVO implements Serializable {

	private Study						study;
	private Set<String>				lmcAvailableApps;
	private Set<String>				lmcSelectedApps;
	private List<Study>				studyList;
	private Collection<ModuleVO>	modulesAvailable;
	private Collection<ModuleVO>	modulesSelected;

	private Collection<ArkModule>	availableArkModules;
	private Collection<ArkModule>	selectedArkModules;

	private String						studySummaryLabel;
	private String						subjectUidExample;
	
	private BiospecimenUidTemplate biospecimentUidTemplate;

	public StudyModelVO() {
		study = new Study();
		biospecimentUidTemplate = new BiospecimenUidTemplate();
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
	 * @param studySummaryLabel
	 *           the studySummaryLabel to set
	 */
	public void setStudySummaryLabel(String studySummaryLabel) {
		this.studySummaryLabel = studySummaryLabel;
	}

	/**
	 * @return the studySummaryLabel
	 */
	public String getStudySummaryLabel() {
		return study.getName();
	}

	/**
	 * @return the subjectUidExample
	 */
	public String getSubjectUidExample() {
		return subjectUidExample;
	}

	/**
	 * @param subjectUidExample
	 *           the subjectUidExample to set
	 */
	public void setSubjectUidExample(String subjectUidExample) {
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

	public BiospecimenUidTemplate getBiospecimentUidTemplate() {
		return biospecimentUidTemplate;
	}

	public void setBiospecimentUidTemplate(
			BiospecimenUidTemplate biospecimentUidTemplate) {
		this.biospecimentUidTemplate = biospecimentUidTemplate;
	}
}
