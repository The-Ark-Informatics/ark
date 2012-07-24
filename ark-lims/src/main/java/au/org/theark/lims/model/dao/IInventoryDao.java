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
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.model.lims.entity.InvColRowType;
import au.org.theark.core.model.lims.entity.InvFreezer;
import au.org.theark.core.model.lims.entity.InvRack;
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.lims.model.vo.BiospecimenLocationVO;

public interface IInventoryDao {
	/**
	 * Search on a particular site
	 * @param invSite Search criteria of site
	 * @return a List of InvSite(s)
	 * @throws ArkSystemException
	 */
	public List<InvSite> searchInvSite(InvSite invSite) throws ArkSystemException;
	
	/**
	 * Search on a particular site, for a list of studies
	 * @param invSite Search criteria of site
	 * @param studyList A list of Studies
	 * @return a List of InvSite(s)
	 * @throws ArkSystemException
	 */
	public List<InvSite> searchInvSite(InvSite invSite, List<Study> studyList) throws ArkSystemException;
	
	/**
	 * Create a Site based on the supplied InvSite
	 * 
	 * @param invSite
	 *           the InvSite object
	 */
	public void createInvSite(InvSite invSite);

	/**
	 * Update a Site based on the supplied InvSite
	 * 
	 * @param invSite
	 *           the InvSite object
	 */
	public void updateInvSite(InvSite invSite);

	/**
	 * Delete a Site based on the supplied InvSite
	 * 
	 * @param invSite
	 *           the InvSite object
	 */
	public void deleteInvSite(InvSite invSite);
	
	/**
	 * Create a Freezer based on the supplied LimsVO
	 * 
	 * @param invFreezer
	 *           the InvFreezer object
	 */
	public void createInvFreezer(InvFreezer invFreezer);

	/**
	 * Update a Freezer based on the supplied LimsVO
	 * 
	 * @param invFreezer
	 *           the InvFreezer object
	 */
	public void updateInvFreezer(InvFreezer invFreezer);

	/**
	 * Delete a Freezer based on the supplied LimsVO
	 * 
	 * @param invFreezer
	 *           the InvFreezer object
	 */
	public void deleteInvFreezer(InvFreezer invFreezer);
	
	/**
	 * Create a Rack based on the supplied InvRack
	 * 
	 * @param invRack
	 *           the invRack object
	 */
	public void createInvRack(InvRack invRack);

	/**
	 * Update a Rack based on the supplied InvRack
	 * 
	 * @param invRack
	 *           the invRack object
	 */
	public void updateInvRack(InvRack invRack);

	/**
	 * Delete a Rack based on the supplied InvRack
	 * 
	 * @param invRack
	 *           the invRack object
	 */
	public void deleteInvRack(InvRack invRack);
	
	/**
	 * Create a Box based on the supplied InvBox
	 * 
	 * @param invBox
	 *           the LimsVO object
	 */
	public void createInvBox(InvBox invBox);

	/**
	 * Update a Box based on the supplied InvBox
	 * 
	 * @param invBox
	 *           the LimsVO object
	 */
	public void updateInvBox(InvBox invBox);

	/**
	 * Delete a Box based on the supplied InvBox
	 * 
	 * @param invBox
	 *           the InvBox object
	 */
	public void deleteInvBox(InvBox invBox);

	/**
	 * Get an InvSite based on the specified id
	 * @param id
	 * @return
	 */
	public InvSite getInvSite(Long id);

	/**
	 * Get an InvCell based on an InvBox, rowno and colno
	 * @param invBox
	 * @param rowno
	 * @param colno
	 * @return
	 */
	public InvCell getInvCell(InvBox invBox, int rowno, int colno);
	
	/**
	 * Get a Biospecimen based on an InvCell
	 * @param invCell
	 * @return
	 */
	public Biospecimen getBiospecimenByInvCell(InvCell invCell);

	/**
	 * Get an invBox based on an id
	 * @param id
	 * @return
	 */
	public InvBox getInvBox(Long id);
	
	/**
	 * Get a list of cells and biospecimens for a specifed InvBox
	 * @param invBox
	 * @return
	 */
	public List<InvCell> getCellAndBiospecimenListByBox(InvBox invBox);

	/**
	 * Gets list of col/row types (Numberic or Alphabet)
	 * @return
	 */
	public List<InvColRowType> getInvColRowTypes();

	/**
	 * Create a new InvCell entity
	 * @param invCell
	 */
	public void createInvCell(InvCell invCell);
	
	/**
	 * Update an InvCell entity
	 * @param invCell
	 */
	public void updateInvCell(InvCell invCell);

	/**
	 * Delete an InvCell entity
	 * @param invCell
	 */
	public void deleteInvCell(InvCell invCell);

	/**
	 * Get an InvFreezer based on the specified id
	 * @param id
	 * @return
	 */
	public InvFreezer getInvFreezer(Long id);
	
	/**
	 * Get an InvRack based on the specified id
	 * @param id
	 * @return
	 */
	public InvRack getInvRack(Long id);

	/**
	 * Get an InvCell based on an Biospecimen
	 * @param biospecimen
	 * @return
	 */
	public InvCell getInvCellByBiospecimen(Biospecimen biospecimen);

	/**
	 * Get an InvCell based on an id
	 * @param id
	 * @return
	 */
	public InvCell getInvCell(Long id);

	/**
	 * Search on a particular freezer
	 * @param invFreezer
	 * @param studyListForUser 
	 * @return a List of InvFreezer(s)
	 * @throws ArkSystemException
	 */
	public List<InvFreezer> searchInvFreezer(InvFreezer invFreezer, List<Study> studyListForUser) throws ArkSystemException;
	
	/**
	 * Search on a particular rack
	 * @param invRack
	 * @param studyListForUser 
	 * @return a List of InvRack(s)
	 * @throws ArkSystemException
	 */
	public List<InvRack> searchInvRack(InvRack invRack, List<Study> studyListForUser) throws ArkSystemException;
	
	/**
	 * Search on a particular box
	 * @param invBox
	 * @return a List of InvBox(s)
	 * @throws ArkSystemException
	 */
	public List<InvBox> searchInvBox(InvBox invBox) throws ArkSystemException;
	
	/**
	 * Returns a light weight VO that represents the location of the given biospecimen 
	 * @param biospecimen
	 * @return
	 * @throws ArkSystemException
	 */
	public BiospecimenLocationVO getBiospecimenLocation(Biospecimen biospecimen) throws ArkSystemException;
	
	/**
	 * Returns a light weight VO that represents the location of the given invCell 
	 * @param invCell
	 * @return
	 * @throws ArkSystemException
	 */
	public BiospecimenLocationVO getInvCellLocation(InvCell invCell) throws ArkSystemException;

	public boolean boxesExist();

	public boolean hasAllocatedCells(InvBox invBox);
	
	public InvCell getInvCellByLocationNames(String siteName, String freezerName, String rackName, String boxName, String row, String column) throws ArkSystemException;

	public void batchUpdateInvCells(List<InvCell> updateInvCells);

	public InvCell getNextAvailableInvCell(InvBox invBox);

	public Integer countAvailableCellsForBox(InvBox invBox);
}