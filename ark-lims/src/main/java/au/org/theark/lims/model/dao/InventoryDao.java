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
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.model.lims.entity.InvTank;
import au.org.theark.core.model.lims.entity.InvTray;

@Repository("inventoryDao")
public class InventoryDao extends HibernateSessionDao implements IInventoryDao {

	public void createInvBox(InvBox invBox) {
		getSession().save(invBox);
	}

	public void createInvSite(InvSite invSite) {
		getSession().save(invSite);
	}

	public void createInvTank(InvTank invTank) {
		getSession().save(invTank);
	}

	public void createInvTray(InvTray invTray) {
		getSession().save(invTray);
	}

	public void deleteInvBox(InvBox invBox) {
		getSession().delete(invBox);
	}

	public void deleteInvSite(InvSite invSite) {
		getSession().delete(invSite);
	}

	public void deleteInvTank(InvTank invTank) {
		getSession().delete(invTank);
	}

	public void deleteInvTray(InvTray invTray) {
		getSession().delete(invTray);
	}

	@SuppressWarnings("unchecked")
	public List<InvSite> searchInvSite(InvSite invSite) throws ArkSystemException {
		Criteria criteria = getSession().createCriteria(InvSite.class);

		if (invSite.getId() != null) {
			criteria.add(Restrictions.eq("id", invSite.getId()));
		}

		if (invSite.getName() != null) {
			criteria.add(Restrictions.eq("name", invSite.getName()));
		}

		if (invSite.getContact() != null) {
			criteria.add(Restrictions.eq("contact", invSite.getContact()));
		}

		if (invSite.getAddress() != null) {
			criteria.add(Restrictions.eq("address", invSite.getAddress()));
		}

		if (invSite.getPhone() != null) {
			criteria.add(Restrictions.eq("phone", invSite.getPhone()));
		}
		
		if (invSite.getStudy() != null) {
			criteria.add(Restrictions.eq("study", invSite.getStudy()));
		}

		List<InvSite> list = criteria.list();
		return list;
	}

	public void updateInvBox(InvBox invBox) {
		getSession().update(invBox);
	}

	public void updateInvSite(InvSite invSite) {
		getSession().update(invSite);
	}

	public void updateInvTank(InvTank invTank) {
		getSession().update(invTank);
	}

	public void updateInvTray(InvTray invTray) {
		getSession().update(invTray);
	}

	@SuppressWarnings("unchecked")
	public InvSite getInvSite(Long id) {
		Criteria criteria = getSession().createCriteria(InvSite.class);

		if (id != null) {
			criteria.add(Restrictions.eq("id", id));
		}

		List<InvSite> list = criteria.list();
		InvSite invSite = (InvSite) list.get(0);
		return invSite;
	}

	@SuppressWarnings("unchecked")
	public InvCell getInvCell(InvBox invBox, int rowno, int colno) {
		Criteria criteria = getSession().createCriteria(InvSite.class);

		if (invBox != null) {
			criteria.add(Restrictions.eq("invBox", invBox));
		}
		
		criteria.add(Restrictions.eq("rowno", rowno));
		criteria.add(Restrictions.eq("colno", colno));
		
		List<InvCell> list = criteria.list();
		InvCell invCell = (InvCell) list.get(0);
		return invCell;
	}

	@SuppressWarnings("unchecked")
	public Biospecimen getBiospecimenByInvCell(InvCell invCell) {
		Criteria criteria = getSession().createCriteria(Biospecimen.class);

		if (invCell != null) {
			criteria.add(Restrictions.eq("invCell", invCell));
		}
		
		List<Biospecimen> list = criteria.list();
		Biospecimen biospecimen = null;
		if(!list.isEmpty()){ 
			biospecimen = (Biospecimen) list.get(0);
		}
		return biospecimen;
	}

	public InvBox getInvBox(Long id) {
		Criteria criteria = getSession().createCriteria(InvBox.class);

		if (id != null) {
			criteria.add(Restrictions.eq("id", id));
		}

		List<InvBox> list = criteria.list();
		InvBox invBox = (InvBox) list.get(0);
		return invBox;
	}
}
