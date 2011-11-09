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

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.managestudy.form.Container;
import au.org.theark.study.web.component.managestudy.form.SearchForm;

public class SearchPanel extends Panel {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= -4770548021154298531L;
	private Container					containerForm;
	private FeedbackPanel			feedBackPanel;
	private StudyCrudContainerVO	studyCrudContainerVO;

	/**
	 * Constructor That uses the VO
	 * 
	 * @param id
	 * @param studyCrudContainerVO
	 */
	public SearchPanel(String id, StudyCrudContainerVO studyCrudContainerVO, FeedbackPanel feedbackPanel, Container containerForm) {

		super(id);
		this.studyCrudContainerVO = studyCrudContainerVO;
		this.containerForm = containerForm;
		this.feedBackPanel = feedbackPanel;
	}

	public void initialisePanel(CompoundPropertyModel<StudyModelVO> studyModelVOCpm) {

		SearchForm searchForm = new SearchForm(Constants.SEARCH_FORM, studyModelVOCpm, studyCrudContainerVO, feedBackPanel, containerForm);
		add(searchForm);
	}

}
