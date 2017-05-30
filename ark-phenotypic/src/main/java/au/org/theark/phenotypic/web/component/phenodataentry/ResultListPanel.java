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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.PhenoDataCollectionVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.phenotypic.web.component.phenodataentry.form.PhenoCollectionListForm;

public class ResultListPanel extends Panel {

	private static final long						serialVersionUID	= 7224168117680252835L;

	protected CompoundPropertyModel<PhenoDataCollectionVO>	cpModel;

	protected FeedbackPanel							feedbackPanel;
	private PhenoCollectionListForm		listDetailForm;
	private AbstractDetailModalWindow detailModalWindow;
	private AbstractDetailModalWindow datasetValueModalWindow;
	

	public ResultListPanel(String id, FeedbackPanel feedbackPanel, CompoundPropertyModel<PhenoDataCollectionVO> cpModel) {
		super(id);
		this.feedbackPanel = feedbackPanel;
		this.cpModel = cpModel;
		setOutputMarkupPlaceholderTag(true);
		initModalwindowDatasetDetails();
		initModalwindowClinicalDatasetValues();
	}

	public ResultListPanel initialisePanel() {
		listDetailForm = new PhenoCollectionListForm("PhenoDataSetCollectionListForm", feedbackPanel, detailModalWindow, datasetValueModalWindow,cpModel);
		listDetailForm.initialiseForm();
		add(listDetailForm);
		return this;
	}

	/**
	 * @return the feedbackPanel
	 */
	public FeedbackPanel getFeedbackPanel() {
		return feedbackPanel;
	}

	/**
	 * @param feedbackPanel the feedbackPanel to set
	 */
	public void setFeedbackPanel(FeedbackPanel feedbackPanel) {
		this.feedbackPanel = feedbackPanel;
	}

	/**
	 * @return the listDetailForm
	 */
	public PhenoCollectionListForm getListDetailForm() {
		return listDetailForm;
	}

	/**
	 * @param listDetailForm the listDetailForm to set
	 */
	public void setListDetailForm(PhenoCollectionListForm listDetailForm) {
		this.listDetailForm = listDetailForm;
	}
	
	private void initModalwindowDatasetDetails(){
		final ResultListPanel panelToRepaint = this;
		detailModalWindow = new AbstractDetailModalWindow("detailModalWindow") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {
				target.add(panelToRepaint);
			}

		};
	}
	private void initModalwindowClinicalDatasetValues(){
		final ResultListPanel panelToRepaint = this;
		datasetValueModalWindow = new AbstractDetailModalWindow("datasetValueModalWindow") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {
				target.add(panelToRepaint);
			}

		};
	}
}
