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
import java.util.List;

import au.org.theark.core.model.report.entity.BiocollectionField;
import au.org.theark.core.model.report.entity.BiospecimenField;
import au.org.theark.core.model.report.entity.DemographicField;
import au.org.theark.core.model.report.entity.Search;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;

/**
 * @author cellis and travis
 * 
 */
public class SearchVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Search search;
	private List<Search> listOfSearchesForResultList = new ArrayList<Search>();
	


	// would be better if pallette could point to search.getDemographicFieldsToReturn 
	private Collection<DemographicField>	availableDemographicFields = new ArrayList<DemographicField>();
	private Collection<DemographicField>	selectedDemographicFields = new ArrayList<DemographicField>();
/*  same concept but for standard biospec and biocoll fields */
	private Collection<BiospecimenField>	availableBiospecimenFields = new ArrayList<BiospecimenField>();
	private Collection<BiospecimenField>	selectedBiospecimenFields = new ArrayList<BiospecimenField>();

	private Collection<BiocollectionField>	availableBiocollectionFields = new ArrayList<BiocollectionField>();
	private Collection<BiocollectionField>	selectedBiocollectionFields = new ArrayList<BiocollectionField>();

	private Collection<CustomFieldDisplay>	availableBiospecimenCustomFieldDisplays = new ArrayList<CustomFieldDisplay>();
	private Collection<CustomFieldDisplay>	selectedBiospecimenCustomFieldDisplays = new ArrayList<CustomFieldDisplay>();

	private Collection<CustomFieldDisplay>	availableSubjectCustomFieldDisplays = new ArrayList<CustomFieldDisplay>();
	private Collection<CustomFieldDisplay>	selectedSubjectCustomFieldDisplays = new ArrayList<CustomFieldDisplay>();

	private Collection<CustomFieldDisplay>	availableBiocollectionCustomFieldDisplays = new ArrayList<CustomFieldDisplay>();
	private Collection<CustomFieldDisplay>	selectedBiocollectionCustomFieldDisplays = new ArrayList<CustomFieldDisplay>();

	private Collection<CustomFieldDisplay>	availablePhenoCustomFieldDisplays = new ArrayList<CustomFieldDisplay>();
	private Collection<CustomFieldDisplay>	selectedPhenoCustomFieldDisplays = new ArrayList<CustomFieldDisplay>();
	
	
	public SearchVO() {
		search = new Search();
	}


	public Search getSearch() {
		return search;
	}


	public void setSearch(Search search) {
		this.search = search;
	}


	public List<Search> getListOfSearchesForResultList() {
		return listOfSearchesForResultList;
	}


	public void setListOfSearchesForResultList(
			List<Search> listOfSearchesForResultList) {
		this.listOfSearchesForResultList = listOfSearchesForResultList;
	}


	public Collection<DemographicField> getAvailableDemographicFields() {
		return availableDemographicFields;
	}


	public void setAvailableDemographicFields(
			Collection<DemographicField> availableDemographicFields) {
		this.availableDemographicFields = availableDemographicFields;
	}

	public Collection<BiospecimenField> getAvailableBiospecimenFields() {
		return availableBiospecimenFields;
	}


	public void setAvailableBiospecimenFields(
			Collection<BiospecimenField> availableBiospecimenFields) {
		this.availableBiospecimenFields = availableBiospecimenFields;
	}

	public Collection<BiocollectionField> getAvailableBiocollectionFields() {
		return availableBiocollectionFields;
	}


	public void setAvailableBiocollectionFields(
			Collection<BiocollectionField> availableBiocollectionFields) {
		this.availableBiocollectionFields = availableBiocollectionFields;
	}


	public Collection<DemographicField> getSelectedDemographicFields() {
		return selectedDemographicFields;
	}


	public void setSelectedDemographicFields(
			Collection<DemographicField> selectedDemographicFields) {
		this.selectedDemographicFields = selectedDemographicFields;
	}

	public Collection<BiocollectionField> getSelectedBiocollectionFields() {
		return selectedBiocollectionFields;
	}


	public void setSelectedBiocollectionFields(
			Collection<BiocollectionField> selectedBiocollectionFields) {
		this.selectedBiocollectionFields = selectedBiocollectionFields;
	}


	public Collection<BiospecimenField> getSelectedBiospecimenFields() {
		return selectedBiospecimenFields;
	}


	public void setSelectedBiospecimenFields(
			Collection<BiospecimenField> selectedBiospecimenFields) {
		this.selectedBiospecimenFields = selectedBiospecimenFields;
	}



	public Collection<CustomFieldDisplay> getAvailablePhenoCustomFieldDisplays() {
		return availablePhenoCustomFieldDisplays;
	}


	public void setAvailablePhenoCustomFieldDisplays(
			Collection<CustomFieldDisplay> availablePhenoCustomFieldDisplays) {
		this.availablePhenoCustomFieldDisplays = availablePhenoCustomFieldDisplays;
	}


	public Collection<CustomFieldDisplay> getSelectedPhenoCustomFieldDisplays() {
		return selectedPhenoCustomFieldDisplays;
	}


	public void setSelectedPhenoCustomFieldDisplays(
			Collection<CustomFieldDisplay> selectedPhenoCustomFieldDisplays) {
		this.selectedPhenoCustomFieldDisplays = selectedPhenoCustomFieldDisplays;
	}


	public Collection<CustomFieldDisplay> getAvailableBiospecimenCustomFieldDisplays() {
		return availableBiospecimenCustomFieldDisplays;
	}


	public void setAvailableBiospecimenCustomFieldDisplays(
			Collection<CustomFieldDisplay> availableBiospecimenCustomFieldDisplays) {
		this.availableBiospecimenCustomFieldDisplays = availableBiospecimenCustomFieldDisplays;
	}


	public Collection<CustomFieldDisplay> getSelectedBiospecimenCustomFieldDisplays() {
		return selectedBiospecimenCustomFieldDisplays;
	}


	public void setSelectedBiospecimenCustomFieldDisplays(
			Collection<CustomFieldDisplay> selectedBiospecimenCustomFieldDisplays) {
		this.selectedBiospecimenCustomFieldDisplays = selectedBiospecimenCustomFieldDisplays;
	}


	public Collection<CustomFieldDisplay> getAvailableSubjectCustomFieldDisplays() {
		return availableSubjectCustomFieldDisplays;
	}


	public void setAvailableSubjectCustomFieldDisplays(
			Collection<CustomFieldDisplay> availableSubjectCustomFieldDisplays) {
		this.availableSubjectCustomFieldDisplays = availableSubjectCustomFieldDisplays;
	}


	public Collection<CustomFieldDisplay> getSelectedSubjectCustomFieldDisplays() {
		return selectedSubjectCustomFieldDisplays;
	}


	public void setSelectedSubjectCustomFieldDisplays(
			Collection<CustomFieldDisplay> selectedSubjectCustomFieldDisplays) {
		this.selectedSubjectCustomFieldDisplays = selectedSubjectCustomFieldDisplays;
	}


	public Collection<CustomFieldDisplay> getAvailableBiocollectionCustomFieldDisplays() {
		return availableBiocollectionCustomFieldDisplays;
	}


	public void setAvailableBiocollectionCustomFieldDisplays(
			Collection<CustomFieldDisplay> availableBiocollectionCustomFieldDisplays) {
		this.availableBiocollectionCustomFieldDisplays = availableBiocollectionCustomFieldDisplays;
	}


	public Collection<CustomFieldDisplay> getSelectedBiocollectionCustomFieldDisplays() {
		return selectedBiocollectionCustomFieldDisplays;
	}


	public void setSelectedBiocollectionCustomFieldDisplays(
			Collection<CustomFieldDisplay> selectedBiocollectionCustomFieldDisplays) {
		this.selectedBiocollectionCustomFieldDisplays = selectedBiocollectionCustomFieldDisplays;
	}

	
}
