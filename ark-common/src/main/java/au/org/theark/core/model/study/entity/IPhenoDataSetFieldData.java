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
package au.org.theark.core.model.study.entity;

import java.util.Date;

import au.org.theark.core.model.pheno.entity.PhenoDataSetData;
import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;

public interface IPhenoDataSetFieldData {
	
	public abstract Long getId();
	
	public abstract void setId(Long Id);

	public abstract PhenoDataSetFieldDisplay getPhenoDataSetFieldDisplay();

	public abstract void setPhenoDataSetFieldDisplay(PhenoDataSetFieldDisplay phenoDataSetFieldDisplay);

	public abstract Date getDateDataValue();

	public abstract void setDateDataValue(Date dateDataValue);

	public abstract String getErrorDataValue();

	public abstract void setErrorDataValue(String errorDataValue);

	public abstract Double getNumberDataValue();

	public abstract void setNumberDataValue(Double numberDataValue);

	public abstract String getTextDataValue();

	public abstract void setTextDataValue(String textDataValue);

    public abstract PhenoDataSetData deepCopy();
}