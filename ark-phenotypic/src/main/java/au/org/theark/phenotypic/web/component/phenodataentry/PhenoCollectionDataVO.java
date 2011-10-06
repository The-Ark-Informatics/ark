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
package au.org.theark.phenotypic.web.component.phenodataentry;

import java.io.Serializable;

import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.pheno.entity.PhenoData;
import au.org.theark.core.model.pheno.entity.PhenotypicCollection;
import au.org.theark.core.web.component.customfield.dataentry.CustomDataVO;

public class PhenoCollectionDataVO extends CustomDataVO<PhenoData> implements Serializable {

	private static final long	serialVersionUID	= 1L;
	
	protected PhenotypicCollection phenoCollection;

	public PhenoCollectionDataVO() {
		super();
		phenoCollection = new PhenotypicCollection();
	}

	public PhenotypicCollection getPhenotypicCollection() {
		return phenoCollection;
	}

	public void setPhenotypicCollection(PhenotypicCollection phenotypicCollection) {
		this.phenoCollection = phenotypicCollection;
	}

}
