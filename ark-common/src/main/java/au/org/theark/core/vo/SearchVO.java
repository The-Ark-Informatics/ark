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


	public Collection<DemographicField> getSelectedDemographicFields() {
		return selectedDemographicFields;
	}


	public void setSelectedDemographicFields(
			Collection<DemographicField> selectedDemographicFields) {
		this.selectedDemographicFields = selectedDemographicFields;
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

	
}
