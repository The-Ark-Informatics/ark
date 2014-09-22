package au.org.theark.disease.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import mx4j.tools.config.DefaultConfigurationBuilder.Create;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.dao.ICustomFieldDao;
import au.org.theark.core.model.disease.entity.Affection;
import au.org.theark.core.model.disease.entity.AffectionCustomFieldData;
import au.org.theark.core.model.disease.entity.AffectionStatus;
import au.org.theark.core.model.disease.entity.Disease;
import au.org.theark.core.model.disease.entity.Gene;
import au.org.theark.core.model.disease.entity.Position;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.disease.vo.AffectionListVO;
import au.org.theark.disease.vo.AffectionVO;
import au.org.theark.disease.vo.DiseaseVO;
import au.org.theark.disease.vo.GeneVO;

@Repository("diseaseDao")
public class DiseaseDao extends HibernateSessionDao implements IDiseaseDao {

	private static final Logger	log	= LoggerFactory.getLogger(DiseaseDao.class);

	private ICustomFieldDao			customFieldDao;

	public ICustomFieldDao getCustomFieldDao() {
		return customFieldDao;
	}

	@Autowired
	public void setCustomFieldDao(ICustomFieldDao customFieldDao) {
		this.customFieldDao = customFieldDao;
	}

	public int getDiseaseCount(DiseaseVO diseaseVO) {
		Criteria criteria = buildSearchDiseaseSearchCriteria(diseaseVO);
		int result = getDistinctDiseases(criteria).size();
		log.info("result of disease count: " + result);
		return result;
	}

	public List<DiseaseVO> searchPageableDiseases(DiseaseVO diseaseVO, int first, int count) {
		Criteria criteria = buildSearchDiseaseSearchCriteria(diseaseVO);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		return getDistinctDiseases(criteria);
	}

	private List<DiseaseVO> getDistinctDiseases(Criteria criteria) {
		List<DiseaseVO> diseaseVOs = new ArrayList<DiseaseVO>();

		criteria.setProjection(Projections.distinct(Projections.id()));
		List<Long> diseaseIDs = criteria.list();

		for (Long l : diseaseIDs) {
			Disease disease = getDiseaseById(l);
			for (CustomField cf : disease.getCustomFields()) {
				log.info(cf.toString() + " " + cf.getFieldType().toString());
			}
			log.info("disease: " + disease.toString());
			diseaseVOs.add(new DiseaseVO(disease));
		}
		return diseaseVOs;
	}

	private Disease getDiseaseById(Long id) {
		Criteria criteria = getSession().createCriteria(Disease.class);
		criteria.add(Restrictions.idEq(id));
		Disease disease = (Disease) criteria.uniqueResult();

		return disease;
	}

	private Criteria buildSearchDiseaseSearchCriteria(DiseaseVO diseaseVO) {
		Criteria criteria = getSession().createCriteria(Disease.class);

		Disease disease = diseaseVO.getDisease();

		if (disease.getName() != null && !disease.getName().isEmpty()) {
			criteria.add(Restrictions.like("name", disease.getName(), MatchMode.ANYWHERE));
		}
		if (disease.getStudy() != null) {
			criteria.add(Restrictions.eq("study", disease.getStudy()));
		}

		return criteria;
	}

	public int getGeneCount(GeneVO geneVO) {
		Criteria criteria = buildSearchGeneSearchCriteria(geneVO);
		int result = getDistinctGenes(criteria).size();
		log.info("Gene count: " + result);
		return result;
	}

	public List<GeneVO> searchPageableGenes(GeneVO geneVO, int first, int count) {
		Criteria criteria = buildSearchGeneSearchCriteria(geneVO);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		return getDistinctGenes(criteria);
	}

	private Criteria buildSearchGeneSearchCriteria(GeneVO geneVO) {
		Criteria criteria = getSession().createCriteria(Gene.class);

		Gene gene = geneVO.getGene();

		if (gene.getName() != null && !gene.getName().isEmpty()) {
			criteria.add(Restrictions.like("name", gene.getName(), MatchMode.ANYWHERE));
		}
		if (gene.getStudy() != null) {
			criteria.add(Restrictions.eq("study", gene.getStudy()));
		}

		return criteria;
	}

	private List<GeneVO> getDistinctGenes(Criteria criteria) {
		List<GeneVO> geneVOs = new ArrayList<GeneVO>();

		criteria.setProjection(Projections.distinct(Projections.id()));
		List<Long> geneIDs = criteria.list();

		for (Long l : geneIDs) {
			Gene gene = getGeneById(l);
			log.info("gene: " + gene.toString());
			geneVOs.add(new GeneVO(gene));
		}
		return geneVOs;
	}

	public Gene getGeneById(Long id) {
		Criteria criteria = getSession().createCriteria(Gene.class);
		criteria.add(Restrictions.idEq(id));
		Gene gene = (Gene) criteria.uniqueResult();
		log.info("Gene.positions: " + gene.getPositions());
		return gene;
	}

	public void save(Object object) {
		try {
			getSession().saveOrUpdate(object);
		}
		catch (NonUniqueObjectException nuoe) {
			nuoe.printStackTrace();
			getSession().merge(object);

		}
	}

	public void update(Object object) {
		try {
			getSession().update(object);
		}
		catch (NonUniqueObjectException nuoe) {
			nuoe.printStackTrace();
			getSession().merge(object);
		}
	}

	public void delete(Object object) {
		getSession().delete(object);
	}

	public List<Gene> getAvailableGenesForStudy(Study study) {
		return getSession().createCriteria(Gene.class).add(Restrictions.eq("study", study)).list();
	}

	public List<Disease> getAvailableDiseasesForStudy(Study study) {
		List<Disease> diseases = new ArrayList<Disease>();
		Criteria criteria = getSession().createCriteria(Disease.class).add(Restrictions.eq("study", study)).setProjection(Projections.distinct(Projections.id()));
		for (Long l : (List<Long>) criteria.list()) {
			diseases.add(getDiseaseById(l));
		}

		return diseases;
	}

	public int getAffectionCount(AffectionVO affectionVO) {
		return buildSearchAffectionCriteria(affectionVO).list().size();
	}

	public List<AffectionVO> searchPageableAffections(AffectionVO affectionVO, int first, int count) {
		log.info("Affection first, count: " + first + ", " + count);
		Criteria criteria = buildSearchAffectionCriteria(affectionVO);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);

		List<AffectionVO> affectionVOs = new ArrayList<AffectionVO>();
		for (Affection affection : (List<Affection>) criteria.list()) {
			// for(Long l : (List<Long>)criteria.list()) {
			// Affection affection = getAffectionByID(l);
			log.info("affection found: " + affection.toString());
			Hibernate.initialize(affection.getDisease());
			affectionVOs.add(new AffectionVO(affection));
		}
		return affectionVOs;
	}

	private Affection getAffectionByID(Long id) {
		Criteria criteria = getSession().createCriteria(Affection.class).add(Restrictions.idEq(id));
		return (Affection) criteria.uniqueResult();
	}

	private Criteria buildSearchAffectionCriteria(AffectionVO affectionVO) {
		Criteria criteria = getSession().createCriteria(Affection.class);

		Affection affection = affectionVO.getAffection();

		if (affection != null) {
			// log.info("build affection criteria: " + affection + " " + affection.getLinkSubjectStudy().getSubjectUID() + " " +
			// affection.getStudy().getName());
			if (affection.getStudy() != null) {
				log.info("Study: " + affection.getStudy().getName());
				criteria.add(Restrictions.eq("study", affection.getStudy()));
			}
			if (affection.getDisease() != null && affection.getDisease().getName() != null && !affection.getDisease().getName().isEmpty()) {
				log.info("Disease: " + affection.getDisease().getName());
				criteria.createAlias("disease", "d");
				criteria.add(Restrictions.eq("d.name", affection.getDisease().getName()));
			}
			if (affection.getLinkSubjectStudy() != null && affection.getLinkSubjectStudy().getSubjectUID() != null && !affection.getLinkSubjectStudy().getSubjectUID().isEmpty()) {
				log.info("LSS: " + affection.getLinkSubjectStudy().getSubjectUID());
				criteria.createAlias("linkSubjectStudy", "lss");
				criteria.add(Restrictions.eq("lss.subjectUID", affection.getLinkSubjectStudy().getSubjectUID()));
			}
			else {
				criteria.createAlias("linkSubjectStudy", "lss");
				criteria.add(Restrictions.isNull("lss.subjectUID"));
			}
			if (affection.getRecordDate() != null) {
				log.info("RecordDate: " + affection.getRecordDate());
				criteria.add(Restrictions.eq("recordDate", affection.getRecordDate()));
			}
			if (affection.getAffectionStatus() != null) {
				log.info("AffectionStatus: " + affection.getAffectionStatus().getName());
				criteria.add(Restrictions.eq("affectionStatus", affection.getAffectionStatus()));
			}
		}
		// criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		return criteria;
	}

	public List<AffectionStatus> getAffectionStatus() {
		Example example = Example.create(new AffectionStatus());
		Criteria criteria = getSession().createCriteria(AffectionStatus.class).add(example);
		return criteria.list();
	}

	public List<Affection> getPersonsAffections(Affection affection) {
		Criteria criteria = buildSearchAffectionCriteria(new AffectionVO(affection));
		return criteria.list();
	}

	private Criteria buildSearchAffectionListCriteria(AffectionListVO affectionListVO) {
		Criteria criteria = getSession().createCriteria(Affection.class);

		if (affectionListVO.getDisease() != null && affectionListVO.getDisease().getName() != null && !affectionListVO.getDisease().getName().isEmpty()) {
			log.info("Disease: " + affectionListVO.getDisease());
			criteria.createAlias("disease", "d");
			criteria.add(Restrictions.like("d.name", affectionListVO.getDisease().getName(), MatchMode.ANYWHERE));
		}
		if (affectionListVO.getLinkSubjectStudy() != null) {
			log.info("LSS: " + affectionListVO.getLinkSubjectStudy());
			criteria.createAlias("linkSubjectStudy", "lss");
			criteria.add(Restrictions.eq("lss.id", affectionListVO.getLinkSubjectStudy().getId()));
		}

		return criteria;
	}

	public List<AffectionCustomFieldData> getAffectionCustomFieldData(Affection affection) {
		List<AffectionCustomFieldData> data = new ArrayList<AffectionCustomFieldData>(); // default action if is new affection
		log.info("affection: " + affection);
		log.info("affection.disease: " + affection.getDisease());
		if (affection.getId() != null) { // i.e is not new affection
			Criteria criteria = getSession().createCriteria(AffectionCustomFieldData.class);
			criteria.add(Restrictions.eq("affection", affection));
			data = criteria.list();
		}
		log.info("data: " + data);
		if (affection != null && affection.getDisease() != null) {
			Set<CustomField> customFields = affection.getDisease().getCustomFields();
			for (AffectionCustomFieldData afcd : data) {
				if (customFields.contains(afcd.getCustomFieldDisplay().getCustomField())) {
					customFields.remove(afcd.getCustomFieldDisplay().getCustomField());
				}
				log.info(afcd.getId() + " " + afcd.getTextDataValue());
			}
			for (CustomField cf : customFields) {
				AffectionCustomFieldData acfd = new AffectionCustomFieldData();
				acfd.setAffection(affection);
				acfd.setCustomFieldDisplay(customFieldDao.getCustomFieldDisplayByCustomField(cf));
				data.add(acfd);
			}
			Collections.sort(data, new Comparator<AffectionCustomFieldData>() {
				public int compare(AffectionCustomFieldData o1, AffectionCustomFieldData o2) {
					return o1.getCustomFieldDisplay().getCustomField().getId().compareTo(o2.getCustomFieldDisplay().getCustomField().getId());
				}
			});
		}
		return data;

	}

	public List<AffectionListVO> searchPageableAffectionListVOs(AffectionListVO affectionListVO, int first, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getAffectionCount(LinkSubjectStudy linkSubjectStudy) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<Position> getPositions(Affection affection) {
		if (affection == null)
			return new ArrayList<Position>();
		Affection aff = getAffectionByID(affection.getId());
		if (aff == null)
			return new ArrayList<Position>();
		List<Position> positions = new ArrayList<Position>(aff.getPositions());
		Collections.sort(positions, new Comparator<Position>() {
			public int compare(Position o1, Position o2) {
//				return o1.getId().compareTo(o2.getId());
				int geneSort = o1.getGene().getName().compareTo(o2.getGene().getName());
				if(geneSort == 0) {
					return o1.getId().compareTo(o2.getId());
				} else {
					return geneSort;
				}
			}
		});
		return positions;
	}
}
