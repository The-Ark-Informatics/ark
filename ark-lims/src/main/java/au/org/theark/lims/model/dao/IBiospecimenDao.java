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
import au.org.theark.core.model.lims.entity.Biospecimen;

public interface IBiospecimenDao {
	/**
	 * Look up a LIMS biospecimen based on the supplied Long id that represents the primary key
	 * 
	 * @param id
	 * @return Biospecimen
	 * @throws EntityNotFoundException
	 */
	public Biospecimen getBiospecimen(Long id) throws EntityNotFoundException;

	/**
	 * Look up a List of LIMS Biospecimen(s) based on the supplied biospecimen object
	 * 
	 * @param biospecimen
	 * @return List<au.org.theark.core.model.lims.entity.Biospecimen>
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<au.org.theark.core.model.lims.entity.Biospecimen> searchBiospecimen(au.org.theark.core.model.lims.entity.Biospecimen biospecimen) throws ArkSystemException;

	/**
	 * Create a LIMS Biospecimen based on the supplied biospecimen
	 * 
	 * @param biospecimen
	 */
	public void createBiospecimen(au.org.theark.core.model.lims.entity.Biospecimen biospecimen);

	/**
	 * Update a LIMS Biospecimen based on the supplied biospecimen
	 * 
	 * @param biospecimen
	 */
	public void updateBiospecimen(au.org.theark.core.model.lims.entity.Biospecimen biospecimen);

	/**
	 * Delete a LIMS Biospecimen based on the supplied biospecimen
	 * 
	 * @param biospecimen
	 */
	public void deleteBiospecimen(au.org.theark.core.model.lims.entity.Biospecimen biospecimen);

	/**
	 * Get count of the Biospecimens given the criteria
	 * 
	 * @param Biospecimens
	 *           criteria
	 * @return counts
	 */
	public int getBiospecimenCount(Biospecimen biospecimenCriteria);

	/**
	 * A generic interface that will return a list Biospecimens specified by a particular criteria, and a paginated reference point
	 * 
	 * @param Biospecimens
	 *           criteria
	 * @return Collection of Biospecimen
	 */
	public List<Biospecimen> searchPageableBiospecimens(Biospecimen biospecimenCriteria, int first, int count);

}
