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

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.model.lims.entity.InvColRowType;
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.model.lims.entity.InvTank;
import au.org.theark.core.model.lims.entity.InvTray;

@SuppressWarnings("unchecked")
@Repository("inventoryDao")
public class InventoryDao extends HibernateSessionDao implements IInventoryDao {
	private static final Logger				log						= LoggerFactory.getLogger(InventoryDao.class);

	public void createInvSite(InvSite invSite) {
		getSession().save(invSite);
	}

	public void createInvTank(InvTank invTank) {
		getSession().save(invTank);
	}

	public void createInvTray(InvTray invTray) {
		getSession().save(invTray);
	}
	
	public void createInvBox(InvBox invBox) {
		getSession().save(invBox);
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

	public void updateInvSite(InvSite invSite) {
		getSession().merge(invSite);
	}

	public void updateInvTank(InvTank invTank) {
		getSession().merge(invTank);
	}

	public void updateInvTray(InvTray invTray) {
		getSession().merge(invTray);
	}
	
	public void updateInvBox(InvBox invBox) {
		getSession().merge(invBox);
	}

	public InvSite getInvSite(Long id) {
		InvSite invSite = new InvSite();
		Criteria criteria = getSession().createCriteria(InvSite.class);

		if (id != null) {
			criteria.add(Restrictions.eq("id", id));
		}

		List<InvSite> list = criteria.list();
		if(!list.isEmpty()) {
			invSite = (InvSite) list.get(0);
		}
		return invSite;
	}

	public InvCell getInvCell(InvBox invBox, int rowno, int colno) {
		InvCell invCell = new InvCell();
		Criteria criteria = getSession().createCriteria(InvSite.class);

		if (invBox != null) {
			criteria.add(Restrictions.eq("invBox", invBox));
		}
		
		criteria.add(Restrictions.eq("rowno", rowno));
		criteria.add(Restrictions.eq("colno", colno));
		
		List<InvCell> list = criteria.list();
		if(!list.isEmpty()) {
			invCell = (InvCell) list.get(0);
		}
		return invCell;
	}

	public Biospecimen getBiospecimenByInvCell(InvCell invCell) {
		Biospecimen biospecimen = null;
		Criteria criteria = getSession().createCriteria(InvCell.class);
		criteria.add(Restrictions.eq("id", invCell.getId()));
		List<InvCell> list = criteria.list();
		if(!list.isEmpty()){ 
			biospecimen = (Biospecimen) list.get(0).getBiospecimen();	
		}
		return biospecimen;
	}

	public InvBox getInvBox(Long id) {
		InvBox invBox = new InvBox();
		Criteria criteria = getSession().createCriteria(InvBox.class);

		if (id != null) {
			criteria.add(Restrictions.eq("id", id));
		}

		List<InvBox> list = criteria.list();
		if(!list.isEmpty()){ 
			invBox = (InvBox) list.get(0);
		}
		
		if(invBox == null) {
			log.error("InvBox with ID " + id + "is no longer in the database");
		}
		return invBox;
	}
	
	public List<InvCell> getCellAndBiospecimenListByBox(InvBox invBox){
		
		List<InvCell> invCellList = new ArrayList<InvCell>();
	
		StringBuffer sb = new StringBuffer();
		sb.append(  " FROM  InvCell AS cell ");
		sb.append("LEFT JOIN cell.biospecimen as biospecimenList ");
		sb.append( "  WHERE cell.invBox.id = :invBoxId" );
		sb.append(" ORDER BY cell.rowno, cell.colno");
		
		Query query = getSession().createQuery(sb.toString());
		query.setParameter("invBoxId", invBox.getId());
		
		List<Object[]> listOfObjects = query.list();
		for (Object[] objects : listOfObjects) {
			InvCell invCell = new InvCell();
			Biospecimen biospecimen = new Biospecimen();
			
			if(objects.length > 0 && objects.length >= 1){
				invCell = (InvCell) objects[0];
				if(objects[1] != null){
					biospecimen = (Biospecimen) objects[1];
					invCell.setBiospecimen(biospecimen);
				}
				invCellList.add(invCell);
			}
		}
		
		return invCellList;
	}

	public List<InvColRowType> getInvColRowTypes() {
		Criteria criteria = getSession().createCriteria(InvColRowType.class);
		List<InvColRowType> list = criteria.list();
		return list;
	}

	public void createInvCell(InvCell invCell) {
		getSession().save(invCell);
	}
	
	public void updateInvCell(InvCell invCell) {
		getSession().update(invCell);
	}
	
	public void deleteInvCell(InvCell invCell) {
		getSession().delete(invCell);
	}

	public InvTank getInvTank(Long id) {
		InvTank invTank = new InvTank();
		Criteria criteria = getSession().createCriteria(InvTank.class);

		if (id != null) {
			criteria.add(Restrictions.eq("id", id));
		}

		List<InvTank> list = criteria.list();
		if(!list.isEmpty()){ 
			invTank = (InvTank) list.get(0);
		}
		
		if(invTank == null) {
			log.error("InvTank with ID " + id + "is no longer in the database");
		}
		return invTank;
	}

	public InvTray getInvTray(Long id) {
		InvTray invTray = new InvTray();
		Criteria criteria = getSession().createCriteria(InvTray.class);

		if (id != null) {
			criteria.add(Restrictions.eq("id", id));
		}

		List<InvTray> list = criteria.list();
		if(!list.isEmpty()){ 
			invTray = (InvTray) list.get(0);
		}
		
		if(invTray == null) {
			log.error("InvTray with ID " + id + "no longer in the database");
		}
		return invTray;
	}

	public InvCell getInvCellByBiospecimen(Biospecimen biospecimen) {
		InvCell invCell = new InvCell();
		Criteria criteria = getSession().createCriteria(InvCell.class);

		if (biospecimen != null) {
			criteria.add(Restrictions.eq("biospecimen", biospecimen));
		}

		List<InvCell> list = criteria.list();
		if(!list.isEmpty()){ 
			invCell = (InvCell) list.get(0);
		}
		
		if(invCell == null) {
			log.error("InvCell with biospecimen " + biospecimen.getId() + "no longer in the database");
		}
		return invCell;
	}

	public InvCell getInvCell(Long id) {
		InvCell invCell = new InvCell();
		Criteria criteria = getSession().createCriteria(InvCell.class);

		if (id != null) {
			criteria.add(Restrictions.eq("id", id));
		}

		List<InvCell> list = criteria.list();
		if(!list.isEmpty()){ 
			invCell = (InvCell) list.get(0);
		}
		
		if(invCell == null) {
			log.error("InvTray with ID " + id + "no longer in the database");
		}
		return invCell;
	}

	public List<InvBox> searchInvBox(InvBox invBox) throws ArkSystemException {
		Criteria criteria = getSession().createCriteria(InvBox.class);

		if (invBox.getId() != null) {
			criteria.add(Restrictions.eq("id", invBox.getId()));
		}

		if (invBox.getName() != null) {
			criteria.add(Restrictions.eq("name", invBox.getName()));
		}

		List<InvBox> list = criteria.list();
		return list;
	}

	public List<InvTank> searchInvTank(InvTank invTank) throws ArkSystemException {
		Criteria criteria = getSession().createCriteria(InvTank.class);

		if (invTank.getId() != null) {
			criteria.add(Restrictions.eq("id", invTank.getId()));
		}

		if (invTank.getName() != null) {
			criteria.add(Restrictions.eq("name", invTank.getName()));
		}

		List<InvTank> list = criteria.list();
		return list;
	}

	public List<InvTray> searchInvTray(InvTray invTray) throws ArkSystemException {
		Criteria criteria = getSession().createCriteria(InvTray.class);

		if (invTray.getId() != null) {
			criteria.add(Restrictions.eq("id", invTray.getId()));
		}

		if (invTray.getName() != null) {
			criteria.add(Restrictions.eq("name", invTray.getName()));
		}

		List<InvTray> list = criteria.list();
		return list;
	}
}
