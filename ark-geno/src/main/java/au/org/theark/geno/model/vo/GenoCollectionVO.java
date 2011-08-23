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
package au.org.theark.geno.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import au.org.theark.core.model.geno.entity.GenoCollection;

/**
 * @author elam
 *
 */
public class GenoCollectionVO implements Serializable {

	private GenoCollection genoCollection;
	private java.util.Collection<GenoCollection> genoCollectionList;
	
	
	public GenoCollectionVO() {
		this.genoCollection = new GenoCollection();
		this.genoCollectionList = new ArrayList<GenoCollection>();
	}

	public GenoCollection getGenoCollection() {
		return genoCollection;
	}
	
	public void setGenoCollection(GenoCollection genoCollection) {
		this.genoCollection = genoCollection;
	}
	
	public java.util.Collection<GenoCollection> getGenoCollectionCollection() {
		return genoCollectionList;
	}
	
	public void setGenoCollectionCollection(
			java.util.Collection<GenoCollection> genoCollectionCollection) {
		this.genoCollectionList = genoCollectionCollection;
	}


}
