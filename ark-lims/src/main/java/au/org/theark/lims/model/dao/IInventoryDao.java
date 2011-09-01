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
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.model.lims.entity.InvTank;
import au.org.theark.core.model.lims.entity.InvTray;

public interface IInventoryDao {
	/**
	 * Search on a particular site
	 * @param invSite
	 * @return a List of InvSite(s)
	 * @throws ArkSystemException
	 */
	public List<InvSite> searchInvSite(InvSite invSite) throws ArkSystemException;
	
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
	 * Create a Tank based on the supplied LimsVO
	 * 
	 * @param invTank
	 *           the InvTank object
	 */
	public void createInvTank(InvTank invTank);

	/**
	 * Update a Tank based on the supplied LimsVO
	 * 
	 * @param invTank
	 *           the InvTank object
	 */
	public void updateInvTank(InvTank invTank);

	/**
	 * Delete a Tank based on the supplied LimsVO
	 * 
	 * @param invTank
	 *           the InvTank object
	 */
	public void deleteInvTank(InvTank invTank);
	
	/**
	 * Create a Tray based on the supplied InvTray
	 * 
	 * @param invTray
	 *           the invTray object
	 */
	public void createInvTray(InvTray invTray);

	/**
	 * Update a Tray based on the supplied InvTray
	 * 
	 * @param invTray
	 *           the invTray object
	 */
	public void updateInvTray(InvTray invTray);

	/**
	 * Delete a Tray based on the supplied InvTray
	 * 
	 * @param invTray
	 *           the invTray object
	 */
	public void deleteInvTray(InvTray invTray);
	
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
	 * Get an InvTank based on the specified id
	 * @param id
	 * @return
	 */
	public InvTank getInvTank(Long id);
	
	/**
	 * Get an InvTray based on the specified id
	 * @param id
	 * @return
	 */
	public InvTray getInvTray(Long id);

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
}
