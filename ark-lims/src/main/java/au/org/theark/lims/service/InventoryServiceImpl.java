package au.org.theark.lims.service;

import java.util.Iterator;
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
		
		// Add cells for box
		for (int row = 1; row < invBox.getNoofrow(); row++) {
			for (int col = 1; col < invBox.getNoofcol(); col++) {
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
		// Update box and cells
		InvBox invBox = modelObject.getInvBox();
		int capacity = invBox.getNoofcol() * invBox.getNoofrow();
		invBox.setCapacity(capacity);
		invBox.setAvailable(capacity);
		
		iInventoryDao.updateInvBox(modelObject.getInvBox());
		
		// Remove previous cells
		List<InvCell> invCellList = invBox.getInvCells();
		for (Iterator<InvCell> iterator = invCellList.iterator(); iterator.hasNext();) {
			InvCell invCell = (InvCell) iterator.next();
			deleteInvCell(invCell);
		}
		
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
}
