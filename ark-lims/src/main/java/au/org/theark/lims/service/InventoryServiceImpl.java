package au.org.theark.lims.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.model.lims.entity.InvColRowType;
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.model.lims.entity.InvTank;
import au.org.theark.core.model.lims.entity.InvTray;
import au.org.theark.lims.model.dao.IInventoryDao;
import au.org.theark.lims.model.vo.BiospecimenLocationVO;
import au.org.theark.lims.model.vo.LimsVO;

/**
 * @author cellis
 * 
 */
@Transactional
@Service(au.org.theark.lims.web.Constants.LIMS_INVENTORY_SERVICE)
public class InventoryServiceImpl implements IInventoryService {

	private IInventoryDao	iInventoryDao;

	/**
	 * @param iInventoryDao
	 *           the iInventoryDao to set
	 */
	@Autowired
	public void setiInventoryDao(IInventoryDao iInventoryDao) {
		this.iInventoryDao = iInventoryDao;
	}

	public void createInvBox(LimsVO modelObject) {
		// Set up box and cells
		InvBox invBox = modelObject.getInvBox();
		int capacity = invBox.getNoofcol() * invBox.getNoofrow();
		invBox.setCapacity(capacity);
		invBox.setAvailable(capacity);

		iInventoryDao.createInvBox(invBox);

		createCellsForBox(invBox);
	}

	/**
	 * Create cells for the box in question
	 * 
	 * @param invBox
	 */
	private void createCellsForBox(InvBox invBox) {
		// Add cells for box
		for (int row = 1; row <= invBox.getNoofrow(); row++) {
			for (int col = 1; col <= invBox.getNoofcol(); col++) {
				InvCell invCell = new InvCell();
				invCell.setStatus("Empty");
				invCell.setInvBox(invBox);

				invCell.setColno(new Long(col));
				invCell.setRowno(new Long(row));
				createInvCell(invCell);
			}
		}
	}

	public void createInvSite(LimsVO modelObject) {
		iInventoryDao.createInvSite(modelObject.getInvSite());
	}

	public void createInvTank(LimsVO modelObject) {
		iInventoryDao.createInvTank(modelObject.getInvTank());
	}

	public void createInvTray(LimsVO modelObject) {
		iInventoryDao.createInvTray(modelObject.getInvTray());
	}

	public void createInvCell(InvCell invCell) {
		iInventoryDao.createInvCell(invCell);
	}

	public void deleteInvBox(LimsVO modelObject) {
		iInventoryDao.deleteInvBox(modelObject.getInvBox());
	}

	public void deleteInvSite(LimsVO modelObject) {
		iInventoryDao.deleteInvSite(modelObject.getInvSite());
	}

	public void deleteInvTank(LimsVO modelObject) {
		iInventoryDao.deleteInvTank(modelObject.getInvTank());
	}

	public void deleteInvTray(LimsVO modelObject) {
		iInventoryDao.deleteInvTray(modelObject.getInvTray());
	}

	public void deleteInvCell(InvCell invCell) {
		iInventoryDao.deleteInvCell(invCell);
	}

	public InvSite getInvSite(Long id) {
		return iInventoryDao.getInvSite(id);
	}

	public List<InvSite> searchInvSite(InvSite invSite) throws ArkSystemException {
		return iInventoryDao.searchInvSite(invSite);
	}

	public void updateInvBox(LimsVO modelObject) {
		iInventoryDao.updateInvBox(modelObject.getInvBox());
	}

	public void updateInvCell(InvCell invCell) {
		iInventoryDao.updateInvCell(invCell);
	}

	public void updateInvSite(LimsVO modelObject) {
		iInventoryDao.updateInvSite(modelObject.getInvSite());
	}

	public void updateInvTank(LimsVO modelObject) {
		iInventoryDao.updateInvTank(modelObject.getInvTank());
	}

	public void updateInvTray(LimsVO modelObject) {
		iInventoryDao.updateInvTray(modelObject.getInvTray());
	}

	public InvCell getInvCell(InvBox invBox, int rowno, int colno) {
		return iInventoryDao.getInvCell(invBox, rowno, colno);
	}

	public Biospecimen getBiospecimenByInvCell(InvCell invCell) {
		return iInventoryDao.getBiospecimenByInvCell(invCell);
	}

	public InvBox getInvBox(Long id) {
		return iInventoryDao.getInvBox(id);
	}

	public List<InvCell> getCellAndBiospecimenListByBox(InvBox invBox) {
		return iInventoryDao.getCellAndBiospecimenListByBox(invBox);
	}

	public List<InvColRowType> getInvColRowTypes() {
		return iInventoryDao.getInvColRowTypes();
	}

	public InvTank getInvTank(Long id) {
		return iInventoryDao.getInvTank(id);
	}

	public InvTray getInvTray(Long id) {
		return iInventoryDao.getInvTray(id);
	}

	public InvCell getInvCellByBiospecimen(Biospecimen biospecimen) {
		return iInventoryDao.getInvCellByBiospecimen(biospecimen);
	}

	public InvCell getInvCell(Long id) {
		return iInventoryDao.getInvCell(id);
	}

	public List<InvBox> searchInvBox(InvBox invBox) throws ArkSystemException {
		return iInventoryDao.searchInvBox(invBox);
	}

	public List<InvTank> searchInvTank(InvTank invTank) throws ArkSystemException {
		return iInventoryDao.searchInvTank(invTank);
	}

	public List<InvTray> searchInvTray(InvTray invTray) throws ArkSystemException {
		return iInventoryDao.searchInvTray(invTray);
	}

	/**
	 * A wrapper implementation over getInvCellByBiospecimen(Biospecimen)
	 */
	public BiospecimenLocationVO locateBiospecimen(Biospecimen biospecimen) throws ArkSystemException {
		InvCell cell = iInventoryDao.getInvCellByBiospecimen(biospecimen);
		BiospecimenLocationVO biospecimenLocationVO = new BiospecimenLocationVO();
		if (cell != null && cell.getId() != null) {
			InvBox box = cell.getInvBox();
			InvTray tray = box.getInvTray();
			InvTank tank = tray.getInvTank();
			InvSite site = tank.getInvSite();

			biospecimenLocationVO.setIsAllocated(true);
			biospecimenLocationVO.setBoxName(box.getName());
			biospecimenLocationVO.setTrayName(tray.getName());
			biospecimenLocationVO.setTankName(tank.getName());
			biospecimenLocationVO.setSiteName(site.getName());
			biospecimenLocationVO.setColumn(cell.getColno());
			biospecimenLocationVO.setRow(cell.getRowno());
			
			String rowLabel = new String();
			if (box.getRownotype().getName().equalsIgnoreCase("ALPHABET")) {
				char character = (char) (cell.getRowno() + 64);
				rowLabel = new Character(character).toString();
			}
			else {
				rowLabel = new Integer(cell.getRowno().intValue()).toString();
			}
			biospecimenLocationVO.setRowLabel(rowLabel);
			
			String colLabel = new String();
			if (box.getColnotype().getName().equalsIgnoreCase("ALPHABET")) {
				char character = (char) (cell.getColno() + 64);
				colLabel = new Character(character).toString();
			}
			else {
				colLabel = new Integer(cell.getColno().intValue()).toString();
			}
			biospecimenLocationVO.setColLabel(colLabel);
		}

		return biospecimenLocationVO;
	}

	/**
	 * Returns the current path (ie synced to database) to the node in question (box,tray,tank, or site)
	 * @param node
	 * @return List of objects (nodes) in the path order (site : tank : tray : box)
	 */
	public List<Object> getInventoryPathOfNode(Object node){
		List<Object> path = new ArrayList<Object>(0);
		if (node instanceof InvSite) {
			InvSite invSite = (InvSite) node;
			invSite = iInventoryDao.getInvSite(invSite.getId());
			path.add(invSite);
		}
		if (node instanceof InvTank) {
			InvTank invTank = (InvTank) node;
			invTank = iInventoryDao.getInvTank(invTank.getId());
			InvSite invSite = invTank.getInvSite();
			path.add(invSite);
			path.add(invTank);
		}
		if (node instanceof InvTray) {
			InvTray invTray = (InvTray) node;
			invTray = iInventoryDao.getInvTray(invTray.getId());
			InvTank invTank = invTray.getInvTank();
			InvSite invSite = invTank.getInvSite();
			path.add(invSite);
			path.add(invTank);
			path.add(invTray);
		}
		if (node instanceof InvBox) {
			InvBox invBox = (InvBox) node;
			invBox = iInventoryDao.getInvBox(invBox.getId());
			InvTray invTray = invBox.getInvTray();
			InvTank invTank = invTray.getInvTank();
			InvSite invSite = invTank.getInvSite();
			path.add(invSite);
			path.add(invTank);
			path.add(invTray);
			path.add(invBox);
		}
		return path;
	}
}