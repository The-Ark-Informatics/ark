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
package au.org.theark.phenotypic.web.component.phenodataentry;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.pheno.entity.PhenotypicCollection;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.security.PermissionConstants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
/**
 * @author elam
 *
 */
public class PhenoDataEntryContainerPanel extends Panel {
	private static final long	serialVersionUID	= 1L;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService<Void>					iArkCommonService;
	
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService			iPhenotypicService;
	
	protected CompoundPropertyModel<PhenotypicCollectionDataVO> cpModel;

	protected FeedbackPanel feedbackPanel;
	protected WebMarkupContainer customDataEditorWMC;

	/**
	 * Constructor requires the phenoCollection to be passed in
	 * @param id
	 * @param phenoCollection
	 */
	public PhenoDataEntryContainerPanel(String id, CompoundPropertyModel<PhenotypicCollectionDataVO> phenoCollectionModel) {
		super(id, phenoCollectionModel);
		this.cpModel = phenoCollectionModel;
	}

	public PhenoDataEntryContainerPanel initialisePanel() {
		add(initialiseFeedbackPanel());
		add(initialiseCustomDataEditorWMC());
		if (!ArkPermissionHelper.isModuleFunctionAccessPermitted()) {
			this.error(au.org.theark.core.Constants.MODULE_NOT_ACCESSIBLE_MESSAGE);
			customDataEditorWMC.setVisible(false);
		}
		return this;
	}
	
	protected WebMarkupContainer initialiseCustomDataEditorWMC() {
		customDataEditorWMC = new WebMarkupContainer("customDataEditorWMC");
		Panel dataEditorPanel;
		boolean contextLoaded = prerenderContextCheck();
		if (contextLoaded && isActionPermitted()) {
			dataEditorPanel = new PhenoDataEditorPanel("customDataEditorPanel", cpModel, feedbackPanel).initialisePanel();;
		}
		else if (!contextLoaded) {
			dataEditorPanel = new EmptyPanel("customDataEditorPanel");
			this.error("A study and subject in context are required to proceed.");
		}
		else {
			dataEditorPanel = new EmptyPanel("customDataEditorPanel");
			this.error("You do not have sufficient permissions to access this function");
		}
		customDataEditorWMC.add(dataEditorPanel);
		return customDataEditorWMC;
	}
	
	protected WebMarkupContainer initialiseFeedbackPanel() {
		/* Feedback Panel doesn't have to sit within a form */
		feedbackPanel = new FeedbackPanel("feedbackMessage");
		feedbackPanel.setOutputMarkupId(true);
		return feedbackPanel;
	}
	
	protected boolean isActionPermitted() {
		boolean flag = false;
		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.READ)) {
			flag = true;
		}
		else {
			flag = false;
		}
		return flag;
	}
	
	protected boolean prerenderContextCheck() {
		
		//TODO: Remove this test code later
		PhenotypicCollection aPhenoCollection = iPhenotypicService.getPhenotypicCollection(new Long(1));
		
		boolean contextLoaded = false;
		if (aPhenoCollection != null) {
			cpModel.getObject().setPhenotypicCollection(aPhenoCollection);
			contextLoaded = true;
		}
		return contextLoaded;
	}

}
