package au.org.theark.lims.web.component.inventory.tree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.model.lims.entity.InvFreezer;
import au.org.theark.core.model.lims.entity.InvRack;
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.lims.model.TreeNodeModel;
import au.org.theark.lims.service.IInventoryService;

public class TreeModel {
	private static final long					serialVersionUID	= 1L;
	private static final Logger	log					= LoggerFactory.getLogger(InventoryLinkTree.class);
	
	private IInventoryService					iInventoryService;
	private IArkCommonService<Void>			iArkCommonService;
	
	private List<InvSite>			invSites				= new ArrayList<InvSite>(0);
	
	public TreeModel(IArkCommonService<Void> iArkCommonService, IInventoryService iInventoryService) {
		this.iArkCommonService = iArkCommonService;
		this.iInventoryService = iInventoryService;
	}
	
	/**
	 * Creates the model that feeds the tree.
	 * 
	 * @return New instance of tree model.
	 */
	public DefaultTreeModel createTreeModel() {
		InvSite invSite = new InvSite();
		
		List<Study> studyListForUser = new ArrayList<Study>(0);
		try {
			Subject currentUser = SecurityUtils.getSubject();
			ArkUser arkUser = iArkCommonService.getArkUser(currentUser.getPrincipal().toString());
			ArkUserVO arkUserVo = new ArkUserVO();
			arkUserVo.setArkUserEntity(arkUser);
			
			Long sessionArkModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
			ArkModule arkModule = null;
			arkModule = iArkCommonService.getArkModuleById(sessionArkModuleId);
			studyListForUser = iArkCommonService.getStudyListForUserAndModule(arkUserVo, arkModule);
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());
		}
		
		try {
			invSites = iInventoryService.searchInvSite(invSite, studyListForUser);
		}
		catch (ArkSystemException e) {
			log.error(e.getMessage());
		}
		return convertToTreeModel();
	}

	private DefaultTreeModel convertToTreeModel() {
		DefaultTreeModel model = null;
		// Default root node (set to not show)
		MutableTreeNode rootNode = new MutableTreeNode(new TreeNodeModel("ROOT"));
		addSites(rootNode, invSites);
		model = new DefaultTreeModel(rootNode);
		return model;
	}

	void addSites(DefaultMutableTreeNode parentNode, List<InvSite> sites) {
		for (InvSite site : sites){
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(site);
			parentNode.add(childNode);
			addFreezers(childNode, site.getInvFreezers());
		}
	}
	
	void addFreezers(DefaultMutableTreeNode parentNode, List<InvFreezer> freezers){
		for (InvFreezer freezer : freezers){
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(freezer);
			parentNode.add(childNode);
			addRacks(childNode, freezer.getInvRacks());
		}
	}
	
	void addRacks(DefaultMutableTreeNode parentNode, List<InvRack> racks){
		for (InvRack rack : racks){
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(rack);
			parentNode.add(childNode);
			addBoxes(childNode, rack.getInvBoxes());
		}
	}

	void addBoxes(DefaultMutableTreeNode parentNode, List<InvBox> boxes){
		for (InvBox box : boxes){
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(box);
			parentNode.add(childNode);
		}
	}
}