package au.org.theark.lims.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.model.lims.entity.InvSite;
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
		iInventoryDao.createInvBox(modelObject.getInvBox());
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

	public InvSite getInvSite(Long id) {
		return iInventoryDao.getInvSite(id);
	}
	
	public List<InvSite> searchInvSite(InvSite invSite) throws ArkSystemException {
		return iInventoryDao.searchInvSite(invSite);
	}

	public void updateInvBox(LimsVO modelObject) {
		iInventoryDao.updateInvBox(modelObject.getInvBox());
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
}
