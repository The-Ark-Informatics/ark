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
package au.org.theark.lims.web.component.inventory.tree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.apache.wicket.markup.html.tree.WicketTreeModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
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
import au.org.theark.core.session.ArkSession;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.lims.model.TreeNodeModel;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.inventory.form.ContainerForm;

public class InventoryLinkTree extends LinkTree {

	private static final long					serialVersionUID	= -3736908668279170191L;
	private static final Logger	log					= LoggerFactory.getLogger(InventoryLinkTree.class);
	
	@SpringBean(name = Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService					iInventoryService;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;
	
	private List<InvSite>			invSites				= new ArrayList<InvSite>(0);
	
	private FeedbackPanel feedbackPanel;
	private WebMarkupContainer detailContainer;
	private ContainerForm containerForm;

	public InventoryLinkTree(String id, FeedbackPanel feedbackPanel, WebMarkupContainer detailContainer, ContainerForm containerForm, DefaultTreeModel treeModel) {
		super(id, new WicketTreeModel());
		this.feedbackPanel = feedbackPanel;
		this.detailContainer = detailContainer;
		this.containerForm = containerForm;
		setModelObject(treeModel);
	}
	
	public InventoryLinkTree(String id, FeedbackPanel feedbackPanel, WebMarkupContainer detailContainer, ContainerForm containerForm) {
		super(id, new WicketTreeModel());
		this.feedbackPanel = feedbackPanel;
		this.detailContainer = detailContainer;
		this.containerForm = containerForm;
		setModelObject(createTreeModel());
	}
	
	/**
	 * Creates the model that feeds the tree.
	 * 
	 * @return New instance of tree model.
	 */
	protected DefaultTreeModel createTreeModel() {
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
	
	@Override
	protected void onJunctionLinkClicked(AjaxRequestTarget target, Object node) {
		
		final DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node;
		parentNode.removeAllChildren();
		if (parentNode.getUserObject() instanceof InvSite) {
			InvSite invSite = (InvSite) parentNode.getUserObject();
			invSite = iInventoryService.getInvSite(invSite.getId());
			addFreezers(parentNode, invSite.getInvFreezers());
		}
		if (parentNode.getUserObject() instanceof InvFreezer) {
			InvFreezer invFreezer = (InvFreezer) parentNode.getUserObject();
			invFreezer = iInventoryService.getInvFreezer(invFreezer.getId());
			addRacks(parentNode, invFreezer.getInvRacks());
		}
		if (parentNode.getUserObject() instanceof InvRack) {
			InvRack invRack = (InvRack) parentNode.getUserObject();
			invRack = iInventoryService.getInvRack(invRack.getId());
			addBoxes(parentNode, invRack.getInvBoxes());
		}
		
		//Set the junction tree node to session
		ArkSession.get().setNodeObject(node);
		
		this.updateTree(target);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Component newNodeComponent(String id, IModel model) {
		InventoryNodePanel panel = new InventoryNodePanel(id, model, InventoryLinkTree.this, feedbackPanel, detailContainer, containerForm);
		return panel;
	}	
}