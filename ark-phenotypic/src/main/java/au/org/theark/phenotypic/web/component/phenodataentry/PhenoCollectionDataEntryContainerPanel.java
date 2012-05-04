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

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.security.PermissionConstants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.PhenoDataCollectionVO;

/**
 * @author elam
 * 
 */
public class PhenoCollectionDataEntryContainerPanel extends Panel {


	private static final long	serialVersionUID	= 1L;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService<Void>					iArkCommonService;
	
	protected CompoundPropertyModel<PhenoDataCollectionVO> cpModel;

	protected FeedbackPanel feedbackPanel;
	protected WebMarkupContainer resultListWMC;
	protected WebMarkupContainer detailWMC;

	/**
	 * @param id
	 */
	public PhenoCollectionDataEntryContainerPanel(String id) {
		super(id);
		cpModel = new CompoundPropertyModel<PhenoDataCollectionVO>(new PhenoDataCollectionVO());
	}
	
	public PhenoCollectionDataEntryContainerPanel initialisePanel() {
		add(initialiseFeedbackPanel());
		add(initialiseResultListWMC());
		add(initialiseDetailWMC());
		if (!ArkPermissionHelper.isModuleFunctionAccessPermitted()) {
			this.error(au.org.theark.core.Constants.MODULE_NOT_ACCESSIBLE_MESSAGE);
			resultListWMC.setVisible(false);
		}
		return this;
	}

	protected WebMarkupContainer initialiseResultListWMC() {
		resultListWMC = new WebMarkupContainer("resultListContainer");
		Panel dataEditorPanel;
		boolean contextLoaded = prerenderContextCheck();
		if (contextLoaded && isActionPermitted()) {
			dataEditorPanel = new ResultListPanel("resultList", feedbackPanel, cpModel).initialisePanel();;
		}
		else if (!contextLoaded) {
			dataEditorPanel = new EmptyPanel("resultList");
			this.error("A study and subject in context are required to proceed.");
		}
		else {
			dataEditorPanel = new EmptyPanel("resultList");
			this.error("You do not have sufficient permissions to access this function");
		}
		resultListWMC.add(dataEditorPanel);
		return resultListWMC;
	}

	protected WebMarkupContainer initialiseDetailWMC() {
		detailWMC = new WebMarkupContainer("detailContainer");
		Panel detailPanel = new EmptyPanel("detailsPanel");
		detailWMC.add(detailPanel);
		return detailWMC;
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
		// Get the Study, SubjectUID and ArkModule from Context
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		String sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
		Long sessionArkModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);

		boolean contextLoaded = false;
		if ((sessionStudyId != null) && (sessionSubjectUID != null)) {
			LinkSubjectStudy linkSubjectStudy = null;
			ArkModule arkModule = null;
			Study study = null;
			try {
				study = iArkCommonService.getStudy(sessionStudyId);
				linkSubjectStudy = iArkCommonService.getSubjectByUID(sessionSubjectUID, study);
				cpModel.getObject().getPhenotypicCollection().setLinkSubjectStudy(linkSubjectStudy);
				arkModule = iArkCommonService.getArkModuleById(sessionArkModuleId);
				if (study != null && linkSubjectStudy != null && arkModule != null) {
					contextLoaded = true;
					cpModel.getObject().setArkFunction(iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY));
				}
			}
			catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return contextLoaded;
	}

}
