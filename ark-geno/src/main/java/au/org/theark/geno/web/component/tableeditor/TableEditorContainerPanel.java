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
package au.org.theark.geno.web.component.tableeditor;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.geno.entity.Beam;
import au.org.theark.core.model.geno.entity.Row;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.geno.model.vo.BeamListVO;
import au.org.theark.geno.model.vo.BeamVO;
import au.org.theark.geno.model.vo.RowListVO;
import au.org.theark.geno.model.vo.RowVO;
import au.org.theark.geno.service.IArkGenoService;
import au.org.theark.geno.web.component.tableeditor.form.BeamContainerForm;
import au.org.theark.geno.web.component.tableeditor.form.RowContainerForm;
import au.org.theark.study.service.IStudyService;


/**
 * @author gecgooden
 * 
 */
public class TableEditorContainerPanel extends AbstractContainerPanel<RowListVO> {

	private static final long										serialVersionUID	= 2166285054533611912L;
	private static final Logger									log					= LoggerFactory.getLogger(TableEditorContainerPanel.class);
	private RowPanel												rowPanel;
	private RowContainerForm											rowContainerForm;
	private FeedbackPanel											rowFeedbackPanel;
	
	private BeamPanel												beamPanel;
	private BeamContainerForm										beamContainerForm;
	private FeedbackPanel											beamFeedbackPanel;
	
	protected CompoundPropertyModel<BeamListVO> beamCPmodel;
	
	private WebMarkupContainer										arkContextMarkup;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService										iArkCommonService;

	@SpringBean(name = "arkGenoService")
	private IArkGenoService											iArkGenoService;
	
	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService											studyService;

	
	private Long														sessionStudyId;
	private Long 														sessionPersonId;
//	private Study														study = new Study();
	protected WebMarkupContainer 								studyNameMarkup;
	protected WebMarkupContainer 								studyLogoMarkup;
	protected WebMarkupContainer resultsWmc = new WebMarkupContainer("resultsWmc");

	protected AbstractDetailModalWindow modalWindow;

	/**
	 * @param id
	 * @param studyLogoMarkup 
	 * @param studyNameMarkup 
	 */
	public TableEditorContainerPanel(String id, WebMarkupContainer arkContextMarkup, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup, AbstractDetailModalWindow modalWindow) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		this.modalWindow = modalWindow;
		

		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<RowListVO>(new RowListVO());
		beamCPmodel = new CompoundPropertyModel<BeamListVO>(new BeamListVO());
		
		rowContainerForm = new RowContainerForm("rowContainerForm", cpModel);
		rowContainerForm.add(initialiseRowFeedBackPanel());
		
		beamContainerForm = new BeamContainerForm("beamContainerForm", beamCPmodel);
		beamContainerForm.add(initialiseBeamFeedBackPanel());
		
		prerenderContextCheck();
		
		initialiseModelObject();
		rowContainerForm.add(initialiseRowDetailPanel());
		beamContainerForm.add(initialiseBeamDetailPanel());

		add(rowContainerForm);
		add(beamContainerForm);
	}

	protected WebMarkupContainer initialiseRowFeedBackPanel() {
		/* Feedback Panel */
		rowFeedbackPanel = new FeedbackPanel("rowFeedbackMessage");
		rowFeedbackPanel.setOutputMarkupId(true);
		return rowFeedbackPanel;
	}

	protected WebMarkupContainer initialiseBeamFeedBackPanel() {
		/* Feedback Panel */
		beamFeedbackPanel = new FeedbackPanel("beamFeedbackMessage");
		beamFeedbackPanel.setOutputMarkupId(true);
		return beamFeedbackPanel;
	}

	protected void initialiseModelObject() {
		if(sessionStudyId != null) {
			Study study = iArkCommonService.getStudy(sessionStudyId);
			if(study != null) {
				RowListVO rowVO = new RowListVO();
				List<RowVO> rows = new ArrayList<RowVO>();
				for(Row row : iArkGenoService.getGenoTableRows(study)) {
					rows.add(new RowVO(row));
				}
				rowVO.setRows(rows);
				rowContainerForm.setModelObject(rowVO);
				
				BeamListVO beamVO = new BeamListVO();
				List<BeamVO> beams = new ArrayList<BeamVO>();
				for(Beam beam : iArkGenoService.getGenoTableBeam(study)) {
					beams.add(new BeamVO(beam));
				}
				beamVO.setBeams(beams);
				log.info("beams: " + beamVO);
				beamContainerForm.setModelObject(beamVO);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void prerenderContextCheck() {
		// Get the Person in Context and determine the Person Type
		sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		log.info("sessionStudyID: " + sessionStudyId);
		if (sessionStudyId != null) {
			Study study = iArkCommonService.getStudy(sessionStudyId);
			arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
		}
	}


	protected WebMarkupContainer initialiseDetailPanel() {
		return null;
	}
	
	protected WebMarkupContainer initialiseRowDetailPanel() {
		rowPanel = new RowPanel("rowDetailPanel", rowFeedbackPanel, arkContextMarkup, rowContainerForm, arkCrudContainerVO, modalWindow);
		return rowPanel;
	}
	
	protected WebMarkupContainer initialiseBeamDetailPanel() {
		beamPanel = new BeamPanel("beamDetailPanel", beamFeedbackPanel, arkContextMarkup, beamContainerForm, arkCrudContainerVO, modalWindow);
		return beamPanel;
	}

	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		// TODO Auto-generated method stub
		return null;
	}	
}
