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
package au.org.theark.lims.model.dao;

import java.util.List;

import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.lims.model.vo.LimsVO;

/**
 * 
 * @author cellis
 *
 */
public interface ILimsSubjectDao {

	/**
	 * Get the count of all Subjects in the specified Study list 
	 * @param subjectVO
	 * @param studyList
	 * @return
	 */
	public abstract long getSubjectCount(LimsVO subjectVO, List<Study> studyList);

	/**
	 * Search for a Subject based on a SubjectVO criteria, and a list of Studies (user ha access to)
	 * @param limsVoCriteria
	 * @param studyList
	 * @param first
	 * @param count
	 * @return
	 */
	public abstract List<LinkSubjectStudy> searchPageableSubjects(LimsVO limsVoCriteria, List<Study> studyList, int first, int count);

}