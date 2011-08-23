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
package au.org.theark.study.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.StudyComp;

;
/**
 * A container for Study Component related function.
 * 
 * @author nivedann
 * 
 */
public class StudyCompVo implements Serializable {

	private StudyComp			studyComponent;
	private List<StudyComp>	studyCompList;
	private int					mode;

	public StudyCompVo() {
		studyComponent = new StudyComp();
		studyCompList = new ArrayList<StudyComp>();
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public List<StudyComp> getStudyCompList() {
		return studyCompList;
	}

	public void setStudyCompList(List<StudyComp> studyCompList) {
		this.studyCompList = studyCompList;
	}

	public StudyComp getStudyComponent() {
		return studyComponent;
	}

	public void setStudyComponent(StudyComp studyComponent) {
		this.studyComponent = studyComponent;
	}

}
