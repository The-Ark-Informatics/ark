package au.org.theark.core.dao;

import java.util.ArrayList;
import java.util.List;

import javax.print.DocFlavor.READER;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.model.disease.entity.Disease;
import au.org.theark.core.model.disease.entity.Gene;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.vo.DiseaseVO;
import au.org.theark.core.vo.GeneVO;

@Repository("diseaseDao")
public class DiseaseDao extends HibernateSessionDao implements IDiseaseDao {

	private static final Logger log = LoggerFactory.getLogger(DiseaseDao.class);

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

		for(Long l : diseaseIDs) {
			Disease disease = getDiseaseById(l);
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

		if(disease.getName() != null && !disease.getName().isEmpty()) {
			criteria.add(Restrictions.like("name", disease.getName(), MatchMode.ANYWHERE));
		}
		if(disease.getStudy() != null) {
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

		if(gene.getName() != null && !gene.getName().isEmpty()) {
			criteria.add(Restrictions.like("name", gene.getName(), MatchMode.ANYWHERE));
		}
		if(gene.getStudy() != null) {
			criteria.add(Restrictions.eq("study", gene.getStudy()));
		}

		return criteria;
	}

	private List<GeneVO> getDistinctGenes(Criteria criteria) {
		List<GeneVO> geneVOs = new ArrayList<GeneVO>();

		criteria.setProjection(Projections.distinct(Projections.id()));
		List<Long> geneIDs = criteria.list();

		for(Long l : geneIDs) {
			Gene gene = getGeneById(l);
			log.info("gene: " + gene.toString());
			geneVOs.add(new GeneVO(gene));
		}
		return geneVOs;
	}

	private Gene getGeneById(Long id) {
		Criteria criteria = getSession().createCriteria(Gene.class);
		criteria.add(Restrictions.idEq(id));
		Gene gene = (Gene) criteria.uniqueResult();
		return gene;
	}

	public void save(Object object) {
		getSession().saveOrUpdate(object);
	}

	public void update(Object object) {
		getSession().update(object);
	}

	public void delete(Object object) {
		getSession().delete(object);
	}

	public List<Gene> getAvailableGenesForStudy(Study study) {
		return getSession().createCriteria(Gene.class).add(Restrictions.eq("study", study)).list();
	}	
	
	public List<Disease> getAvailableDiseasesForStudy(Study study) {
		return getSession().createCriteria(Disease.class).add(Restrictions.eq("study", study)).list();
	}
}
