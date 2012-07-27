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

import au.org.theark.core.model.study.entity.Correspondences;
import au.org.theark.core.model.worktracking.entity.BillableItemType;
import au.org.theark.core.model.worktracking.entity.WorkRequest;

public class CorrespondenceVO implements Serializable {


	private static final long serialVersionUID = 1L;
	private Correspondences								correspondence;
	private Collection<Correspondences>				correspondenceList;
	
	//Required to create automated billable items
	private WorkRequest			workRequest;
	private BillableItemType 	billableItemType;

	public CorrespondenceVO() {
		correspondence = new Correspondences();
		correspondenceList = new ArrayList<Correspondences>();
	}

	public Correspondences getCorrespondence() {
		return correspondence;
	}

	public void setCorrespondence(Correspondences correspondence) {
		this.correspondence = correspondence;
	}

	public Collection<Correspondences> getCorrespondenceList() {
		return correspondenceList;
	}

	public void setCorrespondenceList(Collection<Correspondences> correspondenceList) {
		this.correspondenceList = correspondenceList;
	}

	public WorkRequest getWorkRequest() {
		return workRequest;
	}

	public void setWorkRequest(WorkRequest workRequest) {
		this.workRequest = workRequest;
	}

	public BillableItemType getBillableItemType() {
		return billableItemType;
	}

	public void setBillableItemType(BillableItemType billableItemType) {
		this.billableItemType = billableItemType;
	}
	
}
