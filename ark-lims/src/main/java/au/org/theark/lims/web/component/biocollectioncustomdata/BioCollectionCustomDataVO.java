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
package au.org.theark.lims.web.component.biocollectioncustomdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioCollectionCustomFieldData;
import au.org.theark.core.model.study.entity.ArkFunction;

public class BioCollectionCustomDataVO implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	protected BioCollection bioCollection;
	protected ArkFunction arkFunction;
	protected List<BioCollectionCustomFieldData> bioCollectionCustomFieldDataList;
	
	BioCollectionCustomDataVO() {
		bioCollection = new BioCollection();
		bioCollectionCustomFieldDataList = new ArrayList<BioCollectionCustomFieldData>();
	}

	public BioCollection getBioCollection() {
		return bioCollection;
	}

	public void setBioCollection(BioCollection bioCollection) {
		this.bioCollection = bioCollection;
	}

	public ArkFunction getArkFunction() {
		return arkFunction;
	}

	public void setArkFunction(ArkFunction arkFunction) {
		this.arkFunction = arkFunction;
	}

	public List<BioCollectionCustomFieldData> getBioCollectionCustomFieldDataList() {
		return bioCollectionCustomFieldDataList;
	}

	public void setBioCollectionCustomFieldDataList(List<BioCollectionCustomFieldData> bioCollectionCustomFieldDataList) {
		this.bioCollectionCustomFieldDataList = bioCollectionCustomFieldDataList;
	}

}
