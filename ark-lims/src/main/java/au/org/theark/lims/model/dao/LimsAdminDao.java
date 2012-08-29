package au.org.theark.lims.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
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
	//private static final Logger	log	= LoggerFactory.getLogger(LimsAdminDao.class);

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

	public BarcodeLabel searchBarcodeLabel(BarcodeLabel barcodeLabel) {
		Criteria criteria = getSession().createCriteria(BarcodeLabel.class);
		if (barcodeLabel.getId() != null) {
			criteria.add(Restrictions.eq("id", barcodeLabel.getId()));
		}
		else {
			if (barcodeLabel.getStudy() != null) {
				if(barcodeLabel.getStudy().getParentStudy() != null && barcodeLabel.getStudy().getParentStudy().getId() != null) {
					// Use parent study
					criteria.add(Restrictions.eq("study", barcodeLabel.getStudy().getParentStudy()));
				}
				else {	
					criteria.add(Restrictions.eq("study", barcodeLabel.getStudy()));
				}
			}
			
			if (barcodeLabel.getBarcodePrinter() != null && barcodeLabel.getBarcodePrinter().getId() != null) {
				criteria.add(Restrictions.eq("barcodePrinter", barcodeLabel.getBarcodePrinter()));
			}
			
			if (barcodeLabel.getName() != null) {
				criteria.add(Restrictions.eq("name", barcodeLabel.getName()));
			}
			
			// Restrict to latest version of label
			DetachedCriteria versionCriteria = DetachedCriteria.forClass(BarcodeLabel.class);
			versionCriteria.add(Restrictions.eq("name", barcodeLabel.getName()));

			if (barcodeLabel.getStudy() != null) {
				if(barcodeLabel.getStudy().getParentStudy() != null && barcodeLabel.getStudy().getParentStudy().getId() != null) {
					// Use parent study
					versionCriteria.add(Restrictions.eq("study", barcodeLabel.getStudy().getParentStudy()));
				}
				else {	
					versionCriteria.add(Restrictions.eq("study", barcodeLabel.getStudy()));
				}
			}
			versionCriteria.setProjection(Projections.max("version"));
			
			criteria.add(Property.forName("version").eq(versionCriteria));
		}
		
		BarcodeLabel result = (BarcodeLabel) criteria.uniqueResult();
		return result;
	}

	public BarcodeLabelData searchBarcodeLabelData(BarcodeLabelData barcodeLabelData) {
		Criteria criteria = getSession().createCriteria(BarcodeLabelData.class);
		if (barcodeLabelData.getId() != null) {
			criteria.add(Restrictions.eq("id", barcodeLabelData.getId()));
		}
		
		BarcodeLabelData result = (BarcodeLabelData) criteria.uniqueResult();
		return result;
	}

	public BarcodePrinter searchBarcodePrinter(BarcodePrinter barcodePrinter) {
		Criteria criteria = getSession().createCriteria(BarcodePrinter.class);
		
		if (barcodePrinter.getId() != null) {
			criteria.add(Restrictions.eq("id", barcodePrinter.getId()));
		}
		
		if (barcodePrinter.getStudy() != null) {
			if(barcodePrinter.getStudy().getParentStudy() != null && barcodePrinter.getStudy().getParentStudy().getId() != null) {
				// Use parent study
				criteria.add(Restrictions.eq("study", barcodePrinter.getStudy().getParentStudy()));
			}
			else {	
				criteria.add(Restrictions.eq("study", barcodePrinter.getStudy()));
			}
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

		BarcodePrinter result = (BarcodePrinter) criteria.uniqueResult();
		return result;
	}

	public long getBarcodeLabelCount(BarcodeLabel object) {
		Criteria criteria = buildBarcodeLabelCriteria(object);
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		return totalCount;
	}

	public long getBarcodePrinterCount(BarcodePrinter object) {
		Criteria criteria = buildBarcodePrinterCriteria(object);
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		return totalCount;
	}

	public List<BarcodeLabel> searchPageableBarcodeLabels(BarcodeLabel object, int first, int count) {
		Criteria criteria = buildBarcodeLabelCriteria(object);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		criteria.add(Restrictions.ne("study", new Study(new Long(0))));
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
	
	protected Criteria buildBarcodeLabelCriteria(BarcodeLabel barcodeLabel) {
		Criteria criteria = getSession().createCriteria(BarcodeLabel.class);
		
		if (barcodeLabel.getId() != null) {
			criteria.add(Restrictions.eq("id", barcodeLabel.getId()));
		}
		
		if(barcodeLabel.getStudy() != null) {
			criteria.add(Restrictions.eq("study", barcodeLabel.getStudy()));
		}
		
		if(barcodeLabel.getBarcodePrinter() != null) {
			criteria.add(Restrictions.eq("barcodePrinter", barcodeLabel.getBarcodePrinter()));
		}

		if (barcodeLabel.getName() != null) {
			criteria.add(Restrictions.eq("name", barcodeLabel.getName()));
		}

		if (barcodeLabel.getDescription() != null) {
			criteria.add(Restrictions.ilike("description", barcodeLabel.getDescription(), MatchMode.ANYWHERE));
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
		biospecimenUidTemplate = (BiospecimenUidTemplate) criteria.uniqueResult();
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

	public long getBiospecimenUidTemplateCount(BiospecimenUidTemplate modelObject) {
		Criteria criteria = buildBiospecimenUidTemplateCriteria(modelObject);
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
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
		biospecimenUidTemplateResult = (BiospecimenUidTemplate) criteria.uniqueResult();
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

	public List<BarcodePrinter> getBarcodePrintersByStudy(Study study) {
		List<BarcodePrinter> list = new ArrayList<BarcodePrinter>(0);
		
		if(study != null && study.getId() != null) {
			Criteria criteria = getSession().createCriteria(BarcodePrinter.class);
			criteria.add(Restrictions.eq("study", study));
			list = criteria.list();
		}
		return list;
	}

	public List<Study> getStudyListAssignedToBarcodePrinter() {
		Criteria criteria = getSession().createCriteria(BarcodePrinter.class);
		criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("study")));
		return criteria.list();
	}
	
	public List<Study> getStudyListAssignedToBarcodeLabel() {
		Criteria criteria = getSession().createCriteria(BarcodeLabel.class);
		criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("study")));
		return criteria.list();
	}

	public List<Study> getStudyListAssignedToBiospecimenUidTemplate() {
		Criteria criteria = getSession().createCriteria(BiospecimenUidTemplate.class);
		criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("study")));
		return criteria.list();
	}

	public List<BarcodeLabelData> getBarcodeLabelDataByBarcodeLabel(BarcodeLabel barcodeLabel) {
		List<BarcodeLabelData> list = new ArrayList<BarcodeLabelData>(0);
		
		if(barcodeLabel != null && barcodeLabel.getId() != null) {
			Criteria criteria = getSession().createCriteria(BarcodeLabelData.class);
			criteria.add(Restrictions.eq("barcodeLabel", barcodeLabel));
			list = criteria.list();
		}
		return list;
	}

	public List<BarcodeLabel> getBarcodeLabelsByStudy(Study study) {
		List<BarcodeLabel> list = new ArrayList<BarcodeLabel>(0);
		
		if(study != null && study.getId() != null) {
			Criteria criteria = getSession().createCriteria(BarcodeLabel.class);
			criteria.add(Restrictions.eq("study", study));
			list = criteria.list();
		}
		return list;
	}
	
	public List<BarcodeLabel> getBarcodeLabelTemplates() {
		List<BarcodeLabel> list = new ArrayList<BarcodeLabel>(0);
		
		Criteria criteria = getSession().createCriteria(BarcodeLabel.class);
		criteria.add(Restrictions.isNull("study"));
		criteria.addOrder(Order.asc("name"));
		criteria.addOrder(Order.asc("version"));
		list = criteria.list();
		
		return list;
	}

	public List<BarcodePrinter> getBarcodePrintersByStudyList(List<Study> studyListForUser) {
		List<BarcodePrinter> list = new ArrayList<BarcodePrinter>(0);
		
		Criteria criteria = getSession().createCriteria(BarcodePrinter.class);
		criteria.add(Restrictions.in("study", studyListForUser));
		list = criteria.list();
		
		return list;
	}
	
	public Long getMaxBarcodeVersion(BarcodeLabel barcodeLabel) {
		Long maxVersion = new Long(1);
		Criteria criteria = buildBarcodeLabelCriteria(barcodeLabel);
		criteria.setProjection(Projections.max("version"));
		maxVersion = (Long) criteria.uniqueResult();
		return maxVersion;
	}

	public List<BarcodePrinter> searchPageableBarcodePrinters(BarcodePrinter object, int first, int count, List<Study> studyListForUser) {
		Criteria criteria = buildBarcodePrinterCriteria(object);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		criteria.add(Restrictions.in("study", studyListForUser));
		List<BarcodePrinter> list = criteria.list();
		return list;
	}

	public Long getBarcodePrinterCount(BarcodePrinter object, List<Study> studyListForUser) {
		Criteria criteria = buildBarcodePrinterCriteria(object);
		criteria.add(Restrictions.in("study", studyListForUser));
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		return totalCount;
	}

	public Long getBarcodeLabelCount(BarcodeLabel object, List<Study> studyListForUser) {
		Criteria criteria = buildBarcodeLabelCriteria(object);
		criteria.add(Restrictions.in("study", studyListForUser));
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		return totalCount;
	}

	public List<BarcodeLabel> searchPageableBarcodeLabels(BarcodeLabel object, int first, int count, List<Study> studyListForUser) {
		Criteria criteria = buildBarcodeLabelCriteria(object);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		criteria.add(Restrictions.in("study", studyListForUser));
		List<BarcodeLabel> list = criteria.list();
		return list;
	}
}