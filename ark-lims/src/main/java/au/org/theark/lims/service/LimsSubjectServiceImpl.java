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
package au.org.theark.lims.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.lims.model.dao.ILimsSubjectDao;
import au.org.theark.lims.model.vo.LimsVO;

/**
 * @author cellis
 * 
 */
@Transactional
@Service(au.org.theark.lims.web.Constants.LIMS_SUBJECT_SERVICE)
public class LimsSubjectServiceImpl implements ILimsSubjectService  {
	private ILimsSubjectDao iLimsSubjectDao;
	
	/**
	 * @param iLimsSubjectDao
	 *           the iLimsSubjectDao to set
	 */
	@Autowired
	public void setiLimsSubjectDao(ILimsSubjectDao iLimsSubjectDao) {
		this.iLimsSubjectDao = iLimsSubjectDao;
	}
	
	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsSubjectService#getSubjectCount(au.org.theark.core.vo.SubjectVO, java.util.List)
	 */
	public int getSubjectCount(LimsVO subjectVO, List<Study> studyList) {
		return (int)iLimsSubjectDao.getSubjectCount(subjectVO, studyList);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsSubjectService#searchPageableSubjects(au.org.theark.core.vo.SubjectVO, java.util.List, int, int)
	 */
	public List<LinkSubjectStudy> searchPageableSubjects(LimsVO limsVoCriteria, List<Study> studyList, int first, int count) {
		return iLimsSubjectDao.searchPageableSubjects(limsVoCriteria, studyList, first, count);
	}
}
