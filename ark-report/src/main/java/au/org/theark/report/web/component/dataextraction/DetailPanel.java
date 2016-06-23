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
package au.org.theark.report.web.component.dataextraction;

import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.report.entity.ConsentStatusField;
import au.org.theark.core.model.report.entity.DemographicField;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.report.web.component.dataextraction.form.ContainerForm;
import au.org.theark.report.web.component.dataextraction.form.DetailForm;

public class DetailPanel extends Panel {

	private static Logger	log	= LoggerFactory.getLogger(DetailPanel.class);
	
	private static final long	serialVersionUID	= 1L;

	private DetailForm			detailForm;
	private FeedbackPanel		feedBackPanel;
	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;

//	protected FeedbackPanel									feedbackPanel;
//	private Panel												modalContentPanel;
//	protected AbstractDetailModalWindow					modalWindow;
	
	public DetailPanel(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
		this.feedBackPanel = feedBackPanel;

	}

	public void initialisePanel() {

		AbstractDetailModalWindow modalWindow = new AbstractDetailModalWindow("detailModalWindow") {

			private static final long	serialVersionUID	= 1L;
/*   this sort of this is what i wil have to do if i want to repaint the original call which may be what happens if 
 * 	the button is an edit old filter/add new filter type thing...rightnow just adds new filters.
			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {
				target.add(panelToRepaint);
			}
*/
			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {
				// TODO Auto-generated method stub				
			}
		};
		

		log.info("initpanel getselectedconsentstatusfields");
		for(ConsentStatusField field : containerForm.getModelObject().getSelectedConsentStatusFields()) {
			log.info(field.getPublicFieldName());
		}
		for(DemographicField field : containerForm.getModelObject().getSelectedDemographicFields()) {
			log.info(field.getPublicFieldName());
		}


		detailForm = new DetailForm("detailsForm", feedBackPanel, arkCrudContainerVO, containerForm, modalWindow);
		detailForm.initialiseDetailForm();
		add(detailForm);
	}

	public DetailForm getDetailForm() {
		return detailForm;
	}

	public void setDetailForm(DetailForm detailForm) {
		this.detailForm = detailForm;
	}

}
