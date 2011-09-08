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
package au.org.theark.study.web.component.managestudy;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.StudyModelVO;

public class Summary extends Panel {

	/* A reference of the Model from the Container in this case Search Panel */
	private CompoundPropertyModel<StudyModelVO>	cpm;

	public CompoundPropertyModel<StudyModelVO> getCpm() {
		return cpm;
	}

	public void setCpm(CompoundPropertyModel<StudyModelVO> cpm) {
		this.cpm = cpm;
	}

	public Summary(String id, CompoundPropertyModel<StudyModelVO> cpm) {
		super(id);
		Label studySummaryLabel = new Label("studySummaryLabel", "You have selected the Study " + cpm.getObject().getStudy().getName() + ".Summary of the study follows:");
		add(studySummaryLabel);
	}

}
