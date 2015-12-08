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
import java.util.Arrays;
import java.util.List;

import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;

/**
 * @author nivedann
 * 
 */
public class CustomFieldVO implements Serializable {

	

	private static final long serialVersionUID = 1L;
	private CustomField customField;
	private CustomFieldDisplay customFieldDisplay;
	//please remove not needed unless used for phenoDataSetCategories.
	protected List<CustomFieldCategory>			customFieldCategoryLst;
	protected boolean useCustomFieldDisplay;	// Flags whether or not CustomFieldDisplay should be saved, etc
	//Below fields used for testing purpose only.
	private List<CustomFieldCategory>			selectedCategories;
	private List<CustomFieldCategory>  			firstLevelAvailableCategories;
	private List<CustomFieldCategory>  			firstLevelSelectedCategories;
	private List<CustomFieldCategory>  			lastLevelAvailableCategories;
	private List<CustomFieldCategory>  			lastLevelSelectedCategories;
	

	public CustomFieldVO() {
		super();
		customField = new CustomField();
		customFieldDisplay = new CustomFieldDisplay();
		customFieldCategoryLst=new ArrayList<CustomFieldCategory>(0);
		selectedCategories=new ArrayList<CustomFieldCategory>(0);
		firstLevelAvailableCategories=new ArrayList<CustomFieldCategory>(0);
		lastLevelAvailableCategories=new ArrayList<CustomFieldCategory>(0);
		useCustomFieldDisplay = false;
	}

	public CustomField getCustomField() {
		return customField;
	}

	public void setCustomField(CustomField customField) {
		this.customField = customField;
	}

	public CustomFieldDisplay getCustomFieldDisplay() {
		return customFieldDisplay;
	}

	public void setCustomFieldDisplay(CustomFieldDisplay customFieldDisplay) {
		this.customFieldDisplay = customFieldDisplay;
	}

	public boolean isUseCustomFieldDisplay() {
		return useCustomFieldDisplay;
	}

	public void setUseCustomFieldDisplay(boolean useCustomFieldDisplay) {
		this.useCustomFieldDisplay = useCustomFieldDisplay;
	}

	public List<CustomFieldCategory> getCustomFieldCategoryLst() {
		return customFieldCategoryLst;
	}

	public void setCustomFieldCategoryLst(
			List<CustomFieldCategory> customFieldCategoryLst) {
		this.customFieldCategoryLst = customFieldCategoryLst;
	}

	public List<CustomFieldCategory> getSelectedCategories() {
		return selectedCategories;
	}

	public void setSelectedCategories(List<CustomFieldCategory> selectedCategories) {
		this.selectedCategories = selectedCategories;
	}
	public List<CustomFieldCategory> getFirstLevelAvailableCategories() {
		return firstLevelAvailableCategories;
	}

	public void setFirstLevelAvailableCategories(List<CustomFieldCategory> firstLevelAvailableCategories) {
		this.firstLevelAvailableCategories = firstLevelAvailableCategories;
	}

	public List<CustomFieldCategory> getFirstLevelSelectedCategories() {
		return firstLevelSelectedCategories;
	}

	public void setFirstLevelSelectedCategories(
			List<CustomFieldCategory> firstLevelSelectedCategories) {
		this.firstLevelSelectedCategories = firstLevelSelectedCategories;
	}

	public List<CustomFieldCategory> getLastLevelAvailableCategories() {
		return lastLevelAvailableCategories;
	}

	public void setLastLevelAvailableCategories(
			List<CustomFieldCategory> lastLevelAvailableCategories) {
		this.lastLevelAvailableCategories = lastLevelAvailableCategories;
	}

	public List<CustomFieldCategory> getLastLevelSelectedCategories() {
		return lastLevelSelectedCategories;
	}

	public void setLastLevelSelectedCategories(
			List<CustomFieldCategory> lastLevelSelectedCategories) {
		this.lastLevelSelectedCategories = lastLevelSelectedCategories;
	}
	

	

}
