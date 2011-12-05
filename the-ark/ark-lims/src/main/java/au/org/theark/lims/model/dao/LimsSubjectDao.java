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

import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import au.org.theark.core.Constants;
import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonLastnameHistory;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.lims.model.vo.LimsVO;

@Repository("limsSubjectDao")
public class LimsSubjectDao extends HibernateSessionDao implements ILimsSubjectDao {
	/* (non-Javadoc)
	 * @see au.org.theark.lims.model.dao.ILimsSubjectDao#getSubjectCount(au.org.theark.core.vo.LimsVO, java.util.List)
	 */
	public int getSubjectCount(LimsVO limsVo, List<Study> studyList) {
		if (studyList != null && !studyList.isEmpty()) {
			Criteria criteria = buildGeneralSubjectCriteria(limsVo, studyList);
			criteria.setProjection(Projections.rowCount());
			Integer totalCount = (Integer) criteria.uniqueResult();
			return totalCount;
		}
		else {
			// Fixes to handle for if the studyList is empty (i.e. don't bother querying the database)
			return 0;
		}
	}

	private Criteria buildGeneralSubjectCriteria(LimsVO limsVo, List<Study> studyList) {
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.createAlias("person", "p");
		criteria.add(Restrictions.in("study", studyList));

		if (limsVo.getLinkSubjectStudy().getPerson() != null) {

			if (limsVo.getLinkSubjectStudy().getPerson().getId() != null) {
				criteria.add(Restrictions.eq("p.id", limsVo.getLinkSubjectStudy().getPerson().getId()));
			}

			if (limsVo.getLinkSubjectStudy().getPerson().getFirstName() != null) {
				criteria.add(Restrictions.ilike("p.firstName", limsVo.getLinkSubjectStudy().getPerson().getFirstName(), MatchMode.ANYWHERE));
			}

			if (limsVo.getLinkSubjectStudy().getPerson().getMiddleName() != null) {
				criteria.add(Restrictions.ilike("p.middleName", limsVo.getLinkSubjectStudy().getPerson().getMiddleName(), MatchMode.ANYWHERE));
			}

			if (limsVo.getLinkSubjectStudy().getPerson().getLastName() != null) {
				criteria.add(Restrictions.ilike("p.lastName", limsVo.getLinkSubjectStudy().getPerson().getLastName(), MatchMode.ANYWHERE));
			}

			if (limsVo.getLinkSubjectStudy().getPerson().getDateOfBirth() != null) {
				criteria.add(Restrictions.eq("p.dateOfBirth", limsVo.getLinkSubjectStudy().getPerson().getDateOfBirth()));
			}

			if (limsVo.getLinkSubjectStudy().getPerson().getGenderType() != null) {
				criteria.add(Restrictions.eq("p.genderType.id", limsVo.getLinkSubjectStudy().getPerson().getGenderType().getId()));
			}

			if (limsVo.getLinkSubjectStudy().getPerson().getVitalStatus() != null) {
				criteria.add(Restrictions.eq("p.vitalStatus.id", limsVo.getLinkSubjectStudy().getPerson().getVitalStatus().getId()));
			}

		}

		if (limsVo.getLinkSubjectStudy().getSubjectUID() != null && limsVo.getLinkSubjectStudy().getSubjectUID().length() > 0) {
			criteria.add(Restrictions.ilike("subjectUID", limsVo.getLinkSubjectStudy().getSubjectUID(), MatchMode.ANYWHERE));
		}

		if (limsVo.getLinkSubjectStudy().getSubjectStatus() != null) {
			criteria.add(Restrictions.eq("subjectStatus", limsVo.getLinkSubjectStudy().getSubjectStatus()));
			SubjectStatus subjectStatus = getSubjectStatus("Archive");
			if (subjectStatus != null) {
				criteria.add(Restrictions.ne("subjectStatus", subjectStatus));
			}
		}
		else {
			SubjectStatus subjectStatus = getSubjectStatus("Archive");
			if (subjectStatus != null) {
				criteria.add(Restrictions.ne("subjectStatus", subjectStatus));
			}
		}

		criteria.addOrder(Order.asc("subjectUID"));
		return criteria;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.model.dao.ILimsSubjectDao#searchPageableSubjects(au.org.theark.core.vo.LimsVO, java.util.List, int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<LinkSubjectStudy> searchPageableSubjects(LimsVO limsVoCriteria, List<Study> studyList, int first, int count) {
		Criteria criteria = buildGeneralSubjectCriteria(limsVoCriteria, studyList);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		List<LinkSubjectStudy> subjectList = criteria.list();
		return subjectList;
	}
	
	public SubjectStatus getSubjectStatus(String statusName) {
		SubjectStatus statusToReturn = null;
		SubjectStatus subjectStatus = new SubjectStatus();
		subjectStatus.setName(statusName);
		Example example = Example.create(subjectStatus);

		Criteria criteria = getSession().createCriteria(SubjectStatus.class).add(example);
		if (criteria != null && criteria.list() != null && criteria.list().size() > 0) {
			statusToReturn = (SubjectStatus) criteria.list().get(0);
		}

		return statusToReturn;
	}
	
	public String getPreviousLastname(Person person) {
		Criteria criteria = getSession().createCriteria(PersonLastnameHistory.class);

		if (person.getId() != null) {
			criteria.add(Restrictions.eq(Constants.PERSON_SURNAME_HISTORY_PERSON, person));
		}
		criteria.addOrder(Order.desc("id"));
		PersonLastnameHistory personLastameHistory = new PersonLastnameHistory();
		if (!criteria.list().isEmpty()) {
			if (criteria.list().size() > 1)
				personLastameHistory = (PersonLastnameHistory) criteria.list().get(1);
		}

		return personLastameHistory.getLastName();
	}
}
