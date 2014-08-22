package au.org.theark.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
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
		log.info(criteria.toString());
		log.info(getSession().toString());
		int result = criteria.list().size();
		log.info("result of disease count: " + result);
		return result;
	}
	
	public List<DiseaseVO> searchPageableDiseases(DiseaseVO diseaseVO, int first, int count) {
		log.info("Disease: " + diseaseVO.getDisease().toString());
		Criteria criteria = buildSearchDiseaseSearchCriteria(diseaseVO);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		
		List<Disease> diseases = criteria.list();
		
		List<DiseaseVO> diseaseVOs = new ArrayList<DiseaseVO>();
		
		for(Disease disease : diseases) {
			diseaseVOs.add(new DiseaseVO(disease));
		}
		return diseaseVOs;
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
		return buildSearchGeneSearchCriteria(geneVO).list().size();
	}

	public List<GeneVO> searchPageableGenes(GeneVO geneVO, int first, int count) {
		Criteria criteria = buildSearchGeneSearchCriteria(geneVO);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		
		List<Gene> genes = criteria.list();
		List<GeneVO> geneVOs = new ArrayList<GeneVO>();
		
		for(Gene gene : genes) {
			geneVOs.add(new GeneVO(gene));
		}
		return geneVOs;
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

	
}
