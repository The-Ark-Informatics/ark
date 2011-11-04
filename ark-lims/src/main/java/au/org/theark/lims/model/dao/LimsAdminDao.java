package au.org.theark.lims.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.model.lims.entity.BarcodeLabel;
import au.org.theark.core.model.lims.entity.BarcodeLabelData;
import au.org.theark.core.model.lims.entity.BarcodePrinter;
import au.org.theark.core.model.lims.entity.BiospecimenUidPadChar;
import au.org.theark.core.model.lims.entity.BiospecimenUidTemplate;
import au.org.theark.core.model.lims.entity.BiospecimenUidToken;
import au.org.theark.core.model.study.entity.Study;

@Repository("limsAdminDao")
public class LimsAdminDao extends HibernateSessionDao implements ILimsAdminDao {
	//private static final Logger	log	= LoggerFactory.getLogger(BarcodeDao.class);

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
		
		if (barcodeLabel.getName() != null) {
			criteria.add(Restrictions.eq("name", barcodeLabel.getName()));
		}
		
		List<BarcodeLabel> list = criteria.list();
		if (list != null && list.size() > 0) {
			barcodeLabel = list.get(0);
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

		return barcodePrinter;
	}

	public int getBarcodeLabelCount(BarcodeLabel object) {
		Criteria criteria = buildBarcodeLabelCriteria(object);
		criteria.setProjection(Projections.rowCount());
		Integer totalCount = (Integer) criteria.uniqueResult();
		return totalCount;
	}

	public int getBarcodePrinterCount(BarcodePrinter object) {
		Criteria criteria = buildBarcodePrinterCriteria(object);
		criteria.setProjection(Projections.rowCount());
		Integer totalCount = (Integer) criteria.uniqueResult();
		return totalCount;
	}

	public List<BarcodeLabel> searchPageableBarcodeLabels(BarcodeLabel object, int first, int count) {
		Criteria criteria = buildBarcodeLabelCriteria(object);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		List<BarcodeLabel> list = criteria.list();
		return list;
	}

	public List<BarcodePrinter> searchPageableBarcodePrinters(BarcodePrinter object, int first, int count) {
		Criteria criteria = buildBarcodePrinterCriteria(object);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		List<BarcodePrinter> list = criteria.list();
		return list;
	}
	
	protected Criteria buildBarcodePrinterCriteria(BarcodePrinter object) {
		Criteria criteria = getSession().createCriteria(BarcodePrinter.class);

		if (object.getId() != null) {
			criteria.add(Restrictions.eq("id", object.getId()));
		}
		
		if(object.getStudy() != null) {
			criteria.add(Restrictions.eq("study", object.getStudy()));
		}

		if (object.getName() != null) {
			criteria.add(Restrictions.eq("name", object.getName()));
		}

		if (object.getDescription() != null) {
			criteria.add(Restrictions.ilike("description", object.getDescription(), MatchMode.ANYWHERE));
		}
		
		if (object.getLocation() != null) {
			criteria.add(Restrictions.eq("location", object.getLocation()));
		}
		
		if (object.getHost() != null) {
			criteria.add(Restrictions.eq("host", object.getHost()));
		}
		
		if (object.getPort() != null) {
			criteria.add(Restrictions.eq("port", object.getPort()));
		}

		return criteria;
	}
	
	protected Criteria buildBarcodeLabelCriteria(BarcodeLabel object) {
		Criteria criteria = getSession().createCriteria(BarcodeLabel.class);
		
		if (object.getId() != null) {
			criteria.add(Restrictions.eq("id", object.getId()));
		}
		
		if(object.getStudy() != null) {
			criteria.add(Restrictions.eq("study", object.getStudy()));
		}
		
		if(object.getBarcodePrinter() != null) {
			criteria.add(Restrictions.eq("barcodePrinter", object.getBarcodePrinter()));
		}

		if (object.getName() != null) {
			criteria.add(Restrictions.eq("name", object.getName()));
		}

		if (object.getDescription() != null) {
			criteria.add(Restrictions.ilike("description", object.getDescription(), MatchMode.ANYWHERE));
		}
		
		return criteria;
	}

	public List<BarcodePrinter> getBarcodePrinters(List<Study> studyListForUser) {
		Criteria criteria = getSession().createCriteria(BarcodePrinter.class);
		if(!studyListForUser.isEmpty()) {
			criteria.add(Restrictions.in("study", studyListForUser));
		}
		return criteria.list();
	}
	
	public BiospecimenUidTemplate getBiospecimenUidTemplate(Study study) {
		BiospecimenUidTemplate biospecimenUidTemplate = null;
		Criteria criteria = getSession().createCriteria(BiospecimenUidTemplate.class);
		criteria.add(Restrictions.eq("study", study));
		List<BiospecimenUidTemplate> list = criteria.list();
		if(!list.isEmpty()) {
			biospecimenUidTemplate = list.get(0);
		}
		return biospecimenUidTemplate;
	}

	public void createBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate) {
		getSession().save(biospecimenUidTemplate);
	}

	public void deleteBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate) {
		getSession().delete(biospecimenUidTemplate);
	}

	public void updateBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate) {
		getSession().update(biospecimenUidTemplate);
	}

	public List<BiospecimenUidPadChar> getBiospecimenUidPadChars() {
		Criteria criteria = getSession().createCriteria(BiospecimenUidPadChar.class);
		return criteria.list();
	}

	public int getBiospecimenUidTemplateCount(BiospecimenUidTemplate modelObject) {
		Criteria criteria = buildBiospecimenUidTemplateCriteria(modelObject);
		criteria.setProjection(Projections.rowCount());
		Integer totalCount = (Integer) criteria.uniqueResult();
		return totalCount;
	}

	public List<BiospecimenUidToken> getBiospecimenUidTokens() {
		Criteria criteria = getSession().createCriteria(BiospecimenUidToken.class);
		return criteria.list();
	}

	public BiospecimenUidTemplate searchBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate) {
		Criteria criteria = getSession().createCriteria(BiospecimenUidTemplate.class);
		
		if (biospecimenUidTemplate.getId() != null) {
			criteria.add(Restrictions.eq("id", biospecimenUidTemplate.getId()));
		}
		
		if(biospecimenUidTemplate.getStudy() != null) {
			criteria.add(Restrictions.eq("study", biospecimenUidTemplate.getStudy()));
		}
		
		if(biospecimenUidTemplate.getBiospecimenUidPrefix() != null) {
			criteria.add(Restrictions.eq("biospecimenUidPrefix", biospecimenUidTemplate.getBiospecimenUidPrefix()));
		}

		if(biospecimenUidTemplate.getBiospecimenUidToken() != null) {
			criteria.add(Restrictions.eq("biospecimenUidToken", biospecimenUidTemplate.getBiospecimenUidToken()));
		}
		
		if(biospecimenUidTemplate.getBiospecimenUidPadChar() != null) {
			criteria.add(Restrictions.eq("biospecimenUidPadChar", biospecimenUidTemplate.getBiospecimenUidPadChar()));
		}
		
		BiospecimenUidTemplate biospecimenUidTemplateResult = new BiospecimenUidTemplate();
		if(!criteria.list().isEmpty()) {
			biospecimenUidTemplateResult = (BiospecimenUidTemplate) criteria.list().get(0);
		}
		return biospecimenUidTemplateResult;
	}

	public List<BiospecimenUidTemplate> searchPageableBiospecimenUidTemplates(BiospecimenUidTemplate object, int first, int count) {
		Criteria criteria = buildBiospecimenUidTemplateCriteria(object);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		List<BiospecimenUidTemplate> list = criteria.list();
		return list;
	}
	
	protected Criteria buildBiospecimenUidTemplateCriteria(BiospecimenUidTemplate object) {
		Criteria criteria = getSession().createCriteria(BiospecimenUidTemplate.class);
		
		if (object.getId() != null) {
			criteria.add(Restrictions.eq("id", object.getId()));
		}
		
		if(object.getStudy() != null) {
			criteria.add(Restrictions.eq("study", object.getStudy()));
		}
		
		if(object.getBiospecimenUidPrefix() != null) {
			criteria.add(Restrictions.eq("biospecimenUidPrefix", object.getBiospecimenUidPrefix()));
		}

		if(object.getBiospecimenUidToken() != null) {
			criteria.add(Restrictions.eq("biospecimenUidToken", object.getBiospecimenUidToken()));
		}
		
		if(object.getBiospecimenUidPadChar() != null) {
			criteria.add(Restrictions.eq("biospecimenUidPadChar", object.getBiospecimenUidPadChar()));
		}
		
		return criteria;
	}
}