package au.org.theark.lims.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.model.lims.entity.BarcodeLabel;
import au.org.theark.core.model.lims.entity.BarcodeLabelData;
import au.org.theark.core.model.lims.entity.BarcodePrinter;

@Repository("barcodeDao")
public class BarcodeDao extends HibernateSessionDao implements IBarcodeDao {
	private static final Logger	log	= LoggerFactory.getLogger(BarcodeDao.class);

	public void createBarcodeLabel(BarcodeLabel barcodeLabel) {
		getSession().save(barcodeLabel);
	}
	
	public void createBarcodeLabelData(BarcodeLabelData barcodeLabelData) {
		getSession().save(barcodeLabelData);
	}

	public void createBarcodePrinter(BarcodePrinter barcodePrinter) {
		getSession().save(barcodePrinter);
	}

	public void deleteBarcodeLabel(BarcodeLabel barcodeLabel) {
		getSession().delete(barcodeLabel);
	}

	public void deleteBarcodeLabelData(BarcodeLabelData barcodeLabelData) {
		getSession().delete(barcodeLabelData);
	}

	public void deleteBarcodePrinter(BarcodePrinter barcodePrinter) {
		getSession().delete(barcodePrinter);
	}

	public void updateBarcodeLabel(BarcodeLabel barcodeLabel) {
		getSession().update(barcodeLabel);
	}

	public void updateBarcodeLabelData(BarcodeLabelData barcodeLabelData) {
		getSession().update(barcodeLabelData);
	}

	public void updateBarcodePrinter(BarcodePrinter barcodePrinter) {
		getSession().update(barcodePrinter);
	}

	@SuppressWarnings("unchecked")
	public BarcodeLabel searchBarcodeLabel(BarcodeLabel barcodeLabel) {
		Criteria criteria = getSession().createCriteria(BarcodeLabel.class);
		if (barcodeLabel.getId() != null) {
			criteria.add(Restrictions.eq("id", barcodeLabel.getId()));
		}
		
		if (barcodeLabel.getStudy() != null) {
			criteria.add(Restrictions.eq("study", barcodeLabel.getStudy()));
		}
		
		if (barcodeLabel.getBarcodePrinter() != null) {
			criteria.add(Restrictions.eq("barcodePrinter", barcodeLabel.getBarcodePrinter()));
		}
		
		List<BarcodeLabel> list = criteria.list();
		if (list != null && list.size() > 0) {
			barcodeLabel = list.get(0);
		}
		else {
			log.error("The entity with id" + barcodeLabel.getId().toString() + " cannot be found.");
		}

		return barcodeLabel;
	}

	@SuppressWarnings("unchecked")
	public BarcodeLabelData searchBarcodeLabelData(BarcodeLabelData barcodeLabelData) {
		Criteria criteria = getSession().createCriteria(BarcodeLabelData.class);
		if (barcodeLabelData.getId() != null) {
			criteria.add(Restrictions.eq("id", barcodeLabelData.getId()));
		}
		
		List<BarcodeLabelData> list = criteria.list();
		if (list != null && list.size() > 0) {
			barcodeLabelData = list.get(0);
		}
		else {
			log.error("The entity with id" + barcodeLabelData.getId().toString() + " cannot be found.");
		}

		return barcodeLabelData;
	}

	@SuppressWarnings("unchecked")
	public BarcodePrinter searchBarcodePrinter(BarcodePrinter barcodePrinter) {
		Criteria criteria = getSession().createCriteria(BarcodePrinter.class);
		
		if (barcodePrinter.getId() != null) {
			criteria.add(Restrictions.eq("id", barcodePrinter.getId()));
		}
		
		if (barcodePrinter.getStudy() != null) {
			criteria.add(Restrictions.eq("study", barcodePrinter.getStudy()));
		}
		
		if(barcodePrinter.getName() != null) {
			criteria.add(Restrictions.eq("name", barcodePrinter.getName()));
		}
		
		if (barcodePrinter.getDescription() != null) {
			criteria.add(Restrictions.ilike("description", barcodePrinter.getDescription(), MatchMode.ANYWHERE));
		}
		
		if(barcodePrinter.getLocation() != null) {
			criteria.add(Restrictions.eq("location", barcodePrinter.getLocation()));
		}
		
		if(barcodePrinter.getHost() != null) {
			criteria.add(Restrictions.eq("host", barcodePrinter.getHost()));
		}

		List<BarcodePrinter> list = criteria.list();
		if (list != null && list.size() > 0) {
			barcodePrinter = list.get(0);
		}
		else {
			log.error("The barcodePrinter " + barcodePrinter.getName() + " cannot be found.");
		}

		return barcodePrinter;
	}
}