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
package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class StudyStatusVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long			studyStatusKey;
	private String			name;
	private String			description;
	private Set<StudyVO>	studies	= new HashSet<StudyVO>(0);

	public Long getStudyStatusKey() {
		return studyStatusKey;
	}

	public void setStudyStatusKey(Long studyStatusKey) {
		this.studyStatusKey = studyStatusKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<StudyVO> getStudies() {
		return studies;
	}

	public void setStudies(Set<StudyVO> studies) {
		this.studies = studies;
	}

}
