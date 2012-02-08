/**
 * 
 */
package au.org.theark.lims.service;

import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.model.lims.entity.InvColRowType;
import au.org.theark.core.model.lims.entity.InvFreezer;
import au.org.theark.core.model.lims.entity.InvRack;
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.lims.model.vo.BiospecimenLocationVO;
import au.org.theark.lims.model.vo.LimsVO;

/**
 * Service layer class for all LIMS inventory create/update/delete methods
 * @author cellis
 *
 */
public interface IInventoryService {
	
	/**
	 * Get an InvSite based on the specified id
	 * @param id
	 * @return
	 */
	public InvSite getInvSite(Long id);
	
	/**
	 * Search on a particular site
	 * @param invSite
	 * @return a List of InvSite(s)
	 * @throws ArkSystemException
	 */
	public List<InvSite> searchInvSite(InvSite invSite) throws ArkSystemException;
	
	/**
	 * Create a Site based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void createInvSite(LimsVO modelObject);

	/**
	 * Update a Site based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void updateInvSite(LimsVO modelObject);

	/**
	 * Delete a Site based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void deleteInvSite(LimsVO modelObject);
	
	/**
	 * Create a Freezer based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void createInvFreezer(LimsVO modelObject);

	/**
	 * Update a Freezer based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void updateInvFreezer(LimsVO modelObject);

	/**
	 * Delete a Freezer based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void deleteInvFreezer(LimsVO modelObject);
	
	/**
	 * Create a Rack based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void createInvRack(LimsVO modelObject);

	/**
	 * Update a Rack based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void updateInvRack(LimsVO modelObject);

	/**
	 * Delete a Rack based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void deleteInvRack(LimsVO modelObject);
	
	/**
	 * Create a Box based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void createInvBox(LimsVO modelObject);

	/**
	 * Update a Box based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void updateInvBox(LimsVO modelObject);

	/**
	 * Delete a Box based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void deleteInvBox(LimsVO modelObject);

	/**
	 * Get a particular InvCell, based on the InvBox, rowno and colno
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
	 * Get invTank by id
	 * @param id
	 * @return
	 */
	public InvFreezer getInvFreezer(Long id);
	
	/**
	 * Get invRack by id
	 * @param id
	 * @return
	 */
	public InvRack getInvRack(Long id);
	
	/**
	 * Get an InvBox based on the id
	 * @param invBox
	 * @return
	 */
	public InvBox getInvBox(Long id);
	
	/**
	 * Get an InvCell based on an Biospecimen
	 * @param biospecimen
	 * @return
	 */
	public InvCell getInvCellByBiospecimen(Biospecimen biospecimen);
	
	/**
	 * Update an InvCell
	 * @param invCell to update
	 */
	public void updateInvCell(InvCell invCell);

	/**
	 * Get an InvCell based on an id
	 * @param id
	 * @return
	 */
	public InvCell getInvCell(Long id);

	/**
	 * Search on a particular freezer
	 * @param invFreezer
	 * @return a List of InvTank(s)
	 * @throws ArkSystemException
	 */
	public List<InvFreezer> searchInvFreezer(InvFreezer invFreezer) throws ArkSystemException;
	
	/**
	 * Search on a particular Rack
	 * @param invSite
	 * @return a List of InvSite(s)
	 * @throws ArkSystemException
	 */
	public List<InvRack> searchInvRack(InvRack invRack) throws ArkSystemException;
	
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
	 * Returns the current path (ie synced to database) to the node in question (box,Rack,tank, or site)
	 * @param node
	 * @return List of objects (nodes) in the path order (site : freezer : rack : box)
	 */
	public List<Object> getInventoryPathOfNode(Object node);
	
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
}