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
package au.org.theark.lims.web.component.biospecimen;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.component.biospecimen.form.BiospecimenListForm;

public class BiospecimenListPanel extends Panel {

	private static final long						serialVersionUID	= 7224168117680252835L;

	protected CompoundPropertyModel<LimsVO>	cpModel;

	protected FeedbackPanel							feedbackPanel;
	private BiospecimenListForm					listDetailForm;
	private WebMarkupContainer arkContextMarkup;
	private WebMarkupContainer		studyNameMarkup;
	private WebMarkupContainer		studyLogoMarkup;

	public BiospecimenListPanel(String id, FeedbackPanel feedbackPanel, CompoundPropertyModel<LimsVO> cpModel, WebMarkupContainer arkContextMarkup, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup) {
		super(id);
		this.feedbackPanel = feedbackPanel;
		this.cpModel = cpModel;
		this.arkContextMarkup = arkContextMarkup;
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		initialisePanel();
		setOutputMarkupPlaceholderTag(true);
	}

	public void initialisePanel() {
		final BiospecimenListPanel panelToRepaint = this;
		AbstractDetailModalWindow modalWindow = new AbstractDetailModalWindow("detailModalWindow") {


			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {
				target.add(panelToRepaint);
			}

		};
		listDetailForm = new BiospecimenListForm("biospecimenListForm", feedbackPanel, modalWindow, cpModel, arkContextMarkup, studyNameMarkup, studyLogoMarkup);
		listDetailForm.initialiseForm();
		add(listDetailForm);
	}

	/**
	 * @return the listDetailForm
	 */
	public BiospecimenListForm getListDetailForm() {
		return listDetailForm;
	}

	/**
	 * @param listDetailForm the listDetailForm to set
	 */
	public void setListDetailForm(BiospecimenListForm listDetailForm) {
		this.listDetailForm = listDetailForm;
	}
}
