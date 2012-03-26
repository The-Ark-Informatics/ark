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

import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;

/**
 * @author nivedann
 * 
 */
public class CustomFieldVO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CustomField customField;
	private CustomFieldDisplay customFieldDisplay;
	
	protected boolean useCustomFieldDisplay;	// Flags whether or not CustomFieldDisplay should be saved, etc
	
	public CustomFieldVO() {
		super();
		customField = new CustomField();
		customFieldDisplay = new CustomFieldDisplay();
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

}
