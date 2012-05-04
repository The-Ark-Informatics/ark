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
import au.org.theark.core.model.lims.entity.InvFreezer;
import au.org.theark.core.model.lims.entity.InvRack;
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.model.study.entity.AuditHistory;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.lims.model.dao.IInventoryDao;
import au.org.theark.lims.model.vo.BiospecimenLocationVO;
import au.org.theark.lims.model.vo.LimsVO;

/**
 * @author cellis
 * 
 */
@SuppressWarnings("unchecked")
@Transactional
@Service(au.org.theark.lims.web.Constants.LIMS_INVENTORY_SERVICE)
public class InventoryServiceImpl implements IInventoryService {

	private IArkCommonService	arkCommonService;
	private IInventoryDao		iInventoryDao;

	/**
	 * @param iInventoryDao
	 *           the iInventoryDao to set
	 */
	@Autowired
	public void setiInventoryDao(IInventoryDao iInventoryDao) {
		this.iInventoryDao = iInventoryDao;
	}

	@Autowired
	public void setArkCommonService(IArkCommonService arkCommonService) {
		this.arkCommonService = arkCommonService;
	}

	public void createInvBox(LimsVO modelObject) {
		// Set up box and cells
		InvBox invBox = modelObject.getInvBox();
		int capacity = invBox.getNoofcol() * invBox.getNoofrow();
		invBox.setCapacity(capacity);
		invBox.setAvailable(capacity);

		iInventoryDao.createInvBox(invBox);

		createCellsForBox(invBox);

		// update available of parent
		invBox.getInvRack().setAvailable(invBox.getInvRack().getAvailable() - 1);
		iInventoryDao.updateInvRack(invBox.getInvRack());

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created InvBox " + invBox.getName());
		ah.setEntityId(invBox.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_INV_BOX);
		arkCommonService.createAuditHistory(ah);
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
		InvSite invSite = modelObject.getInvSite();
		iInventoryDao.createInvSite(invSite);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created InvSite " + invSite.getName());
		ah.setEntityId(invSite.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_INV_SITE);
		arkCommonService.createAuditHistory(ah);
	}

	public void createInvFreezer(LimsVO modelObject) {
		InvFreezer invFreezer = modelObject.getInvFreezer();
		int capacity = invFreezer.getCapacity();
		invFreezer.setAvailable(capacity);

		iInventoryDao.createInvFreezer(invFreezer);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created InvFreezer " + invFreezer.getName());
		ah.setEntityId(invFreezer.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_INV_FREEZER);
		arkCommonService.createAuditHistory(ah);
	}

	public void createInvRack(LimsVO modelObject) {
		InvRack invRack = modelObject.getInvRack();
		int capacity = invRack.getCapacity();
		invRack.setAvailable(capacity);
		iInventoryDao.createInvRack(invRack);

		// update available of parent
		invRack.getInvFreezer().setAvailable(invRack.getInvFreezer().getAvailable() - 1);
		iInventoryDao.updateInvFreezer(invRack.getInvFreezer());

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created InvRack " + invRack.getName());
		ah.setEntityId(invRack.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_INV_RACK);
		arkCommonService.createAuditHistory(ah);
	}

	public void createInvCell(InvCell invCell) {
		iInventoryDao.createInvCell(invCell);
	}

	public void deleteInvSite(LimsVO modelObject) {
		InvSite invSite = modelObject.getInvSite();
		iInventoryDao.deleteInvSite(invSite);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted InvSite " + invSite.getName());
		ah.setEntityId(invSite.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_INV_SITE);
		arkCommonService.createAuditHistory(ah);
	}

	public void deleteInvFreezer(LimsVO modelObject) {
		InvFreezer invFreezer = modelObject.getInvFreezer();
		iInventoryDao.deleteInvFreezer(invFreezer);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted InvFreezer " + invFreezer.getName());
		ah.setEntityId(invFreezer.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_INV_FREEZER);
		arkCommonService.createAuditHistory(ah);
	}

	public void deleteInvRack(LimsVO modelObject) {
		InvRack invRack = modelObject.getInvRack();
		// update available of parent
		invRack.getInvFreezer().setAvailable(invRack.getInvFreezer().getAvailable() + 1);
		iInventoryDao.updateInvFreezer(invRack.getInvFreezer());

		iInventoryDao.deleteInvRack(invRack);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted InvRack " + invRack.getName());
		ah.setEntityId(invRack.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_INV_RACK);
		arkCommonService.createAuditHistory(ah);
	}
	
	public void deleteInvBox(LimsVO modelObject) {
		InvBox invBox = modelObject.getInvBox();
		// update available of parent
		invBox.getInvRack().setAvailable(invBox.getInvRack().getAvailable() + 1);
		iInventoryDao.updateInvRack(invBox.getInvRack());
		
		// Delete box
		iInventoryDao.deleteInvBox(invBox);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted InvBox " + invBox.getName());
		ah.setEntityId(invBox.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_INV_BOX);
		arkCommonService.createAuditHistory(ah);
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
		InvBox invBox = modelObject.getInvBox();
		iInventoryDao.updateInvBox(invBox);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated InvBox " + invBox.getName());
		ah.setEntityId(invBox.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_INV_BOX);
		arkCommonService.createAuditHistory(ah);
	}

	public void updateInvCell(InvCell invCell) {
		// Update available cells in box
		InvBox invBox = invCell.getInvBox();
		invBox.setAvailable(countAvailableCellsForBox(invBox));
		iInventoryDao.updateInvBox(invBox);
		
		iInventoryDao.updateInvCell(invCell);
	}

	private Integer countAvailableCellsForBox(InvBox invBox) {
		return iInventoryDao.countAvailableCellsForBox(invBox);
	}

	public void updateInvSite(LimsVO modelObject) {
		InvSite invSite = modelObject.getInvSite();
		iInventoryDao.updateInvSite(invSite);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated InvSite " + invSite.getName());
		ah.setEntityId(invSite.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_INV_SITE);
		arkCommonService.createAuditHistory(ah);
	}

	public void updateInvFreezer(LimsVO modelObject) {
		InvFreezer invFreezer = modelObject.getInvFreezer();
		iInventoryDao.updateInvFreezer(invFreezer);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated InvFreezer " + invFreezer.getName());
		ah.setEntityId(invFreezer.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_INV_FREEZER);
		arkCommonService.createAuditHistory(ah);
	}

	public void updateInvRack(LimsVO modelObject) {
		InvRack invRack = modelObject.getInvRack();
		iInventoryDao.updateInvRack(invRack);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated InvRack " + invRack.getName());
		ah.setEntityId(invRack.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_INV_RACK);
		arkCommonService.createAuditHistory(ah);
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

	public InvFreezer getInvFreezer(Long id) {
		return iInventoryDao.getInvFreezer(id);
	}

	public InvRack getInvRack(Long id) {
		return iInventoryDao.getInvRack(id);
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

	public List<InvFreezer> searchInvFreezer(InvFreezer invTank) throws ArkSystemException {
		return iInventoryDao.searchInvFreezer(invTank);
	}

	public List<InvRack> searchInvRack(InvRack invRack) throws ArkSystemException {
		return iInventoryDao.searchInvRack(invRack);
	}

	public BiospecimenLocationVO getBiospecimenLocation(Biospecimen biospecimen) throws ArkSystemException {
		BiospecimenLocationVO biospecimenLocationVO = new BiospecimenLocationVO();
		biospecimenLocationVO = iInventoryDao.getBiospecimenLocation(biospecimen);
		return biospecimenLocationVO;
	}

	/**
	 * Returns the current path (ie synced to database) to the node in question (box,tray,tank, or site)
	 * 
	 * @param node
	 * @return List of objects (nodes) in the path order (site : freezer : rack : box)
	 */
	public List<Object> getInventoryPathOfNode(Object node) {
		List<Object> path = new ArrayList<Object>(0);
		if (node instanceof InvSite) {
			InvSite invSite = (InvSite) node;
			invSite = iInventoryDao.getInvSite(invSite.getId());
			path.add(invSite);
		}
		if (node instanceof InvFreezer) {
			InvFreezer invFreezer = (InvFreezer) node;
			invFreezer = iInventoryDao.getInvFreezer(invFreezer.getId());
			InvSite invSite = invFreezer.getInvSite();
			path.add(invSite);
			path.add(invFreezer);
		}
		if (node instanceof InvRack) {
			InvRack invRack = (InvRack) node;
			invRack = iInventoryDao.getInvRack(invRack.getId());
			InvFreezer invTank = invRack.getInvFreezer();
			InvSite invSite = invTank.getInvSite();
			path.add(invSite);
			path.add(invTank);
			path.add(invRack);
		}
		if (node instanceof InvBox) {
			InvBox invBox = (InvBox) node;
			invBox = iInventoryDao.getInvBox(invBox.getId());
			InvRack invRack = invBox.getInvRack();
			InvFreezer invFreezer = invRack.getInvFreezer();
			InvSite invSite = invFreezer.getInvSite();
			path.add(invSite);
			path.add(invFreezer);
			path.add(invRack);
			path.add(invBox);
		}
		return path;
	}

	public BiospecimenLocationVO getInvCellLocation(InvCell invCell) throws ArkSystemException {
		return iInventoryDao.getInvCellLocation(invCell);
	}

	public boolean boxesExist() {
		return iInventoryDao.boxesExist();
	}

	public boolean hasAllocatedCells(InvBox invBox) {
		return iInventoryDao.hasAllocatedCells(invBox);
	}

	public InvCell getInvCellByLocationNames(String siteName, String freezerName, String rackName, String boxName, String row, String column) throws ArkSystemException {
		return iInventoryDao.getInvCellByLocationNames(siteName, freezerName, rackName, boxName, row, column);
	}

	public InvCell getNextAvailableInvCell(InvBox invBox) {
		return iInventoryDao.getNextAvailableInvCell(invBox);
	}

	public List<InvSite> searchInvSite(InvSite invSite, List<Study> studyList) throws ArkSystemException {
		return iInventoryDao.searchInvSite(invSite, studyList);
	}
}