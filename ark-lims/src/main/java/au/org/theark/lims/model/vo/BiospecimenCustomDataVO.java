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
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioCollectionCustomFieldData;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.BiospecimenCustomFieldData;
import au.org.theark.core.model.study.entity.ArkFunction;

public class BiospecimenCustomDataVO implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	protected Biospecimen biospecimen;
	protected ArkFunction arkFunction;
	protected List<BiospecimenCustomFieldData> biospecimenCustomFieldDataList;
	
	public BiospecimenCustomDataVO() {
		biospecimen = new Biospecimen();
		biospecimenCustomFieldDataList = new ArrayList<BiospecimenCustomFieldData>();
	}

	public Biospecimen getBiospecimen() {
		return biospecimen;
	}

	public void setBiospecimen(Biospecimen biospecimen) {
		this.biospecimen = biospecimen;
	}

	public ArkFunction getArkFunction() {
		return arkFunction;
	}

	public void setArkFunction(ArkFunction arkFunction) {
		this.arkFunction = arkFunction;
	}

	public List<BiospecimenCustomFieldData> getBiospecimenCustomFieldDataList() {
		return biospecimenCustomFieldDataList;
	}

	public void setBiospecimenCustomFieldDataList(List<BiospecimenCustomFieldData> biospecimenCustomFieldDataList) {
		this.biospecimenCustomFieldDataList = biospecimenCustomFieldDataList;
	}

}
