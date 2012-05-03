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
package au.org.theark.lims.model.dao;

import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioTransaction;
import au.org.theark.core.model.lims.entity.BioTransactionStatus;
import au.org.theark.core.model.lims.entity.TreatmentType;

public interface IBioTransactionDao {
	/**
	 * Look up a LIMS bioTransaction based on the supplied Long id that represents the primary key
	 * 
	 * @param id
	 * @return BioTransaction
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public BioTransaction getBioTransaction(Long id) throws EntityNotFoundException, ArkSystemException;

	/**
	 * Count the LIMS BioTransaction(s) based on the supplied bioTransaction
	 * 
	 * @param bioTransaction
	 * @return total count of transactions given the criteria 
	 */
	public long getBioTransactionCount(BioTransaction bioTransaction);

	/**
	 * Look up a List of LIMS BioTransaction(s) based on the supplied bioTransactionCriteria
	 * that supports results pagination
	 * 
	 * @param bioTransaction
	 * @param first
	 * @param count
	 * @return List<au.org.theark.core.model.lims.entity.BioTransaction> given the criteria
	 */
	public List<BioTransaction> searchPageableBioTransactions(BioTransaction bioTransaction, int first, int count);

	/**
	 * Create a LIMS BioTransaction based on the supplied bioTransaction
	 * 
	 * @param bioTransaction
	 */
	public void createBioTransaction(BioTransaction bioTransaction);

	/**
	 * Update a LIMS BioTransaction based on the supplied bioTransaction
	 * 
	 * @param bioTransaction
	 */
	public void updateBioTransaction(BioTransaction bioTransaction);

	/**
	 * Delete a LIMS BioTransaction based on the supplied bioTransaction
	 * 
	 * @param bioTransaction
	 */
	public void deleteBioTransaction(BioTransaction bioTransaction);

	/**
	 * Get a list of treatment types
	 * @return
	 */
	public List<TreatmentType> getTreatmentTypes();

	/**
	 * Get a List of BioTransaction Status that can be used for a dropdown choice (i.e. no Initial status)
	 * @return
	 */
	public List<BioTransactionStatus> getBioTransactionStatusChoices();

	/**
	 * Get a specific BioTransaction Status by name
	 * @return
	 */
	public BioTransactionStatus getBioTransactionStatusByName(String statusName);

}
