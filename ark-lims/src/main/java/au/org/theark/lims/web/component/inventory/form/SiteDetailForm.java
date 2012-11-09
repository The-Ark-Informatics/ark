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
package au.org.theark.lims.web.component.inventory.form;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.extensions.markup.html.form.palette.component.Recorder;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.palette.ArkPalette;
import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.inventory.tree.InventoryLinkTree;
import au.org.theark.lims.web.component.inventory.tree.MutableTreeNode;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "serial", "unused" })
public class SiteDetailForm extends AbstractInventoryDetailForm<LimsVO> {
	private static Logger				log	= LoggerFactory.getLogger(SiteDetailForm.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	@SpringBean(name = Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService			iInventoryService;

	private ContainerForm				fieldContainerForm;

	private int								mode;

	private TextField<String>			idTxtFld;
	private TextField<String>			nameTxtFld;
	private TextField<String>			collectionIdTxtFld;
	private TextField<String>			contactTxtFld;
	private TextArea<String>			addressTxtAreaFld;
	private TextField<String>			phoneTxtFld;
	private Palette<Study>				studyPalette;	

	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param detailContainer
	 * @param detailPanel
	 * @param containerForm
	 * @param tree
	 * @param node 
	 */
	public SiteDetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer detailContainer, AbstractContainerForm<LimsVO> containerForm, InventoryLinkTree tree, DefaultMutableTreeNode node) {
		super(id, feedBackPanel, detailContainer, containerForm, tree, node);
	}

	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("invSite.id");
		initStudyPalette();
		nameTxtFld = new TextField<String>("invSite.name");
		contactTxtFld = new TextField<String>("invSite.contact");
		addressTxtAreaFld = new TextArea<String>("invSite.address");
		phoneTxtFld = new TextField<String>("invSite.phone");

		attachValidators();
		addComponents();

		// Focus on Name
		nameTxtFld.add(new ArkDefaultFormFocusBehavior());
	}
	
	@SuppressWarnings("unchecked")
	private void initStudyPalette() {
		CompoundPropertyModel<LimsVO> cpm = (CompoundPropertyModel<LimsVO>) containerForm.getModel();
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
			cpm.getObject().setStudyList(studyListForUser);
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());
		}
		
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("name", "name");
		PropertyModel<List<Study>> selectedStudies = new PropertyModel<List<Study>>(cpm, "selectedStudies");
		PropertyModel<List<Study>> availableStudies = new PropertyModel<List<Study>>(cpm, "studyList");
		studyPalette = new ArkPalette("selectedStudies", selectedStudies, availableStudies, renderer, au.org.theark.core.Constants.PALETTE_ROWS, false){
			 @Override
          protected Recorder newRecorderComponent() {
              Recorder rec = super.newRecorderComponent();
              rec.setRequired(true).setLabel(new StringResourceModel("error.invSite.studies.required", this, new Model<String>("Studies")));
              return rec;
          }
			 
			@Override
			public boolean isVisible() {
				SecurityManager securityManager = ThreadContext.getSecurityManager();
				Subject currentUser = SecurityUtils.getSubject();
				return securityManager.hasRole(currentUser.getPrincipals(), au.org.theark.core.security.RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR);
			}
		};
	}

	protected void attachValidators() {
		nameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.invSite.name.required", this, new Model<String>("Name")));
	}

	private void addComponents() {
		detailFormContainer.add(idTxtFld.setEnabled(false));
		detailFormContainer.add(studyPalette);
		detailFormContainer.add(nameTxtFld);
		detailFormContainer.add(contactTxtFld);
		detailFormContainer.add(addressTxtAreaFld);
		detailFormContainer.add(phoneTxtFld);
		add(detailFormContainer);
	}

	@Override
	protected void onSave(Form<LimsVO> containerForm, AjaxRequestTarget target) {
		if (containerForm.getModelObject().getInvSite().getId() == null) {
			// Save
			iInventoryService.createInvSite(containerForm.getModelObject());
			this.info("Site " + containerForm.getModelObject().getInvSite().getName() + " was created successfully");
			processErrors(target);
			node = (new DefaultMutableTreeNode(containerForm.getModelObject().getInvSite()));
			DefaultTreeModel model = (DefaultTreeModel)tree.getModelObject();
			((MutableTreeNode)tree.getModelObject().getRoot()).add(node);
			model.reload();			
		}
		else {
			// Update
			iInventoryService.updateInvSite(containerForm.getModelObject());
			this.info("Site " + containerForm.getModelObject().getInvSite().getName() + " was updated successfully");
			processErrors(target);
		}

		onSavePostProcess(target);
	}

	protected void onCancel(AjaxRequestTarget target) {
		LimsVO limsVo = new LimsVO();
		containerForm.setModelObject(limsVo);

		java.util.List<au.org.theark.core.model.lims.entity.InvSite> invSiteList = new ArrayList<InvSite>(0);
		try {
			invSiteList = iInventoryService.searchInvSite(limsVo.getInvSite());
		}
		catch (ArkSystemException e) {
			log.error(e.getMessage());
		}
		containerForm.getModelObject().setInvSiteList(invSiteList);
		target.add(feedbackPanel);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedbackPanel);
	}

	public AjaxButton getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(AjaxButton deleteButton) {
		this.deleteButton = deleteButton;
	}

	protected void onDeleteConfirmed(AjaxRequestTarget target) {
		iInventoryService.deleteInvSite(containerForm.getModelObject());
		this.info("Site " + containerForm.getModelObject().getInvSite().getName() + " was deleted successfully");

		// Display delete confirmation message
		target.add(feedbackPanel);

		// Move focus back to Search form
		LimsVO limsVo = new LimsVO();
		containerForm.setModelObject(limsVo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getInvSite().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	protected boolean canDelete() {
		return containerForm.getModelObject().getInvSite().getChildren().isEmpty();
	}
}