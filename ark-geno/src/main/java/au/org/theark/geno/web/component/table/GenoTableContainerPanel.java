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
package au.org.theark.geno.web.component.table;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.geno.entity.Row;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.geno.model.vo.RowListVO;
import au.org.theark.geno.model.vo.RowVO;
import au.org.theark.geno.service.IArkGenoService;
import au.org.theark.geno.web.component.table.form.ContainerForm;
import au.org.theark.study.service.IStudyService;


/**
 * @author gecgooden
 * 
 */
public class GenoTableContainerPanel extends AbstractContainerPanel<RowListVO> {

	private static final long										serialVersionUID	= 2166285054533611912L;
	private static final Logger									log					= LoggerFactory.getLogger(GenoTableContainerPanel.class);
	private DetailPanel												detailPanel;
	private ContainerForm											containerForm;

	private WebMarkupContainer										arkContextMarkup;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService										iArkCommonService;

	@SpringBean(name = "arkGenoService")
	private IArkGenoService											iArkGenoService;
	
	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService											studyService;
	
	private Long														sessionStudyId;
	private Long 														sessionPersonId;
	protected WebMarkupContainer 								studyNameMarkup;
	protected WebMarkupContainer 								studyLogoMarkup;
	protected WebMarkupContainer resultsWmc = new WebMarkupContainer("resultsWmc");

	/**
	 * @param id
	 * @param studyLogoMarkup 
	 * @param studyNameMarkup 
	 */
	public GenoTableContainerPanel(String id, WebMarkupContainer arkContextMarkup, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		this.setOutputMarkupId(true);
	
		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<RowListVO>(new RowListVO());

		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		
		prerenderContextCheck();
		
		initialiseModelObject();
		containerForm.add(initialiseDetailPanel());

		add(containerForm);
	}
	
	protected void initialiseModelObject() {
		if(sessionPersonId != null && sessionStudyId != null) {
			Person person = null;
			try {
				person = studyService.getPerson(sessionPersonId);
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
			} catch (ArkSystemException e) {
				e.printStackTrace();
			}
			Study study = iArkCommonService.getStudy(sessionStudyId);
			if(person != null && study != null) {
				Collection<RowVO> rowVOs = new ArrayList<RowVO>();
				for(Row r : iArkGenoService.getGenoTableRows(study)) {
					rowVOs.add(new RowVO(r));
				}
				RowListVO rowListVO = new RowListVO(rowVOs);
				log.info(rowListVO.toString());
				containerForm.setModelObject(rowListVO);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void prerenderContextCheck() {
		sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

		if ((sessionStudyId != null) && (sessionPersonId != null)) {
			String sessionPersonType = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_TYPE);			
			
			if (sessionPersonType.equals(au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT)) {
				Person person;
				boolean contextLoaded = false;
				try {
					person = studyService.getPerson(sessionPersonId);
					Study study = iArkCommonService.getStudy(sessionStudyId);
					contextLoaded = true;
				}
				catch (EntityNotFoundException e) {
					log.error(e.getMessage());
				}
				catch (ArkSystemException e) {
					log.error(e.getMessage());
				}
				if(contextLoaded) {
					arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
				} 
			}
		}
	}


	protected WebMarkupContainer initialiseDetailPanel() {
		detailPanel = new DetailPanel("detailPanel", feedBackPanel, arkContextMarkup, containerForm, arkCrudContainerVO);
		add(detailPanel);
		arkCrudContainerVO.getDetailPanelContainer().add(detailPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		return null;
	}

	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		return null;
	}	
}
