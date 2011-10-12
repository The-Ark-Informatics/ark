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
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.model.lims.entity.InvTank;
import au.org.theark.core.model.lims.entity.InvTray;
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
	 * Create a Tank based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void createInvTank(LimsVO modelObject);

	/**
	 * Update a Tank based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void updateInvTank(LimsVO modelObject);

	/**
	 * Delete a Tank based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void deleteInvTank(LimsVO modelObject);
	
	/**
	 * Create a Tray based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void createInvTray(LimsVO modelObject);

	/**
	 * Update a Tray based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void updateInvTray(LimsVO modelObject);

	/**
	 * Delete a Tray based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void deleteInvTray(LimsVO modelObject);
	
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
	public InvTank getInvTank(Long id);
	
	/**
	 * Get invTray by id
	 * @param id
	 * @return
	 */
	public InvTray getInvTray(Long id);
	
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
	 * Search on a particular tank
	 * @param invTank
	 * @return a List of InvTank(s)
	 * @throws ArkSystemException
	 */
	public List<InvTank> searchInvTank(InvTank invTank) throws ArkSystemException;
	
	/**
	 * Search on a particular tray
	 * @param invSite
	 * @return a List of InvSite(s)
	 * @throws ArkSystemException
	 */
	public List<InvTray> searchInvTray(InvTray invTray) throws ArkSystemException;
	
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
	
	public BiospecimenLocationVO locateBiospecimen(Biospecimen biospecimen) throws ArkSystemException;
	
	/**
	 * Returns the current path (ie synced to database) to the node in question (box,tray,tank, or site)
	 * @param node
	 * @return List of objects (nodes) in the path order (site : tank : tray : box)
	 */
	public List<Object> getInventoryPathOfNode(Object node);
}