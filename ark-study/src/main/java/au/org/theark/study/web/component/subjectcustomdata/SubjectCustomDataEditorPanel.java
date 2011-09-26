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
package au.org.theark.study.web.component.subjectcustomdata;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.component.customfield.dataentry.AbstractCustomDataEditorForm;
import au.org.theark.study.model.vo.SubjectCustomDataVO;
import au.org.theark.study.web.component.subjectcustomdata.form.CustomDataEditorForm;

/**
 * @author elam
 * 
 */
@SuppressWarnings({ "serial" })
public class SubjectCustomDataEditorPanel extends Panel {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= -1L;

	private CompoundPropertyModel<SubjectCustomDataVO>			cpModel;

	protected FeedbackPanel				feedbackPanel;
	protected AbstractCustomDataEditorForm<SubjectCustomDataVO>	customDataEditorForm;
	protected SubjectCustomDataDataViewPanel dataViewPanel;

	public SubjectCustomDataEditorPanel(String id, CompoundPropertyModel<SubjectCustomDataVO> cpModel, FeedbackPanel feedBackPanel) {
		super(id);

		this.cpModel = cpModel;
		this.feedbackPanel = feedBackPanel;
	}
	
	public SubjectCustomDataEditorPanel initialisePanel() {
		
		dataViewPanel = new SubjectCustomDataDataViewPanel("dataViewPanel", cpModel).initialisePanel(null);

		customDataEditorForm = new CustomDataEditorForm("customDataEditorForm", cpModel, feedbackPanel).initialiseForm();
		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", dataViewPanel.getDataView()) {
			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(customDataEditorForm.getDataViewWMC());
				target.add(this);
			}
		};
		pageNavigator.setVisible(false);
		customDataEditorForm.getDataViewWMC().add(dataViewPanel);
		this.add(customDataEditorForm);
		this.add(pageNavigator);
		
		return this;
	}
	
}
