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
package au.org.theark.lims.model.vo;

import java.io.Serializable;

import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioCollectionCustomFieldData;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.web.component.customfield.dataentry.CustomDataVO;

public class BioCollectionCustomDataVO extends CustomDataVO<BioCollectionCustomFieldData> implements Serializable {


	private static final long	serialVersionUID	= 1L;
	
	protected BioCollection bioCollection;
	
	private BioCollectionCustomFieldData bioCollectionCustomFieldData;
	
	private CustomFieldCategory customFieldCategory;
		
	public BioCollectionCustomDataVO() {
		super();
		bioCollection = new BioCollection();
		bioCollectionCustomFieldData=new BioCollectionCustomFieldData();
		
	}

	public BioCollection getBioCollection() {
		return bioCollection;
	}

	public void setBioCollection(BioCollection bioCollection) {
		this.bioCollection = bioCollection;
	}
	
	public CustomFieldCategory getCustomFieldCategory() {
		return customFieldCategory;
	}
	public void setCustomFieldCategory(CustomFieldCategory customFieldCategory) {
		this.customFieldCategory = customFieldCategory;
	}

	public BioCollectionCustomFieldData getBioCollectionCustomFieldData() {
		return bioCollectionCustomFieldData;
	}

	public void setBioCollectionCustomFieldData(BioCollectionCustomFieldData bioCollectionCustomFieldData) {
		this.bioCollectionCustomFieldData = bioCollectionCustomFieldData;
	}

}
