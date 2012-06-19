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
package au.org.theark.study.web.component.subject;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.web.component.palette.ArkPalette;

public class ChildStudyPalettePanel<SubjectVO> extends Panel {


	private static final long		serialVersionUID	= 1L;
	protected Label					assignedChildStudiesLabel;
	protected ArkPalette<Study>	assignedChildStudiesPalette;
	protected Label					assignedChildStudiesNote;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedbackPanel
	 * @param studyCrudContainerVO
	 * @param iModel
	 */
	public ChildStudyPalettePanel(String id, IModel<SubjectVO> iModel) {
		super(id, iModel);
		initChildStudyPalette();
		addComponents();
	}

	@SuppressWarnings("unchecked")
	private void initChildStudyPalette() {
		assignedChildStudiesLabel = new Label("assignedChildStudiesLabel", "Assigned Child Studies:");
		PropertyModel<List<Study>> availableChildStudiesPm = new PropertyModel<List<Study>>(getDefaultModelObject(), "availableChildStudies");
		PropertyModel<List<Study>> selectedChildStudiesPm = new PropertyModel<List<Study>>(getDefaultModelObject(), "selectedChildStudies");
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("name", "name");

		assignedChildStudiesPalette = new ArkPalette("assignedChildStudiesPalette", selectedChildStudiesPm, availableChildStudiesPm, renderer, au.org.theark.study.web.Constants.PALETTE_ROWS, false);
		assignedChildStudiesNote = new Label("assignedChildStudiesNote", "");

		setVisible(!availableChildStudiesPm.getObject().isEmpty());
	}

	private void addComponents() {
		add(assignedChildStudiesLabel);
		add(assignedChildStudiesPalette);
		add(assignedChildStudiesNote);
	}
}
