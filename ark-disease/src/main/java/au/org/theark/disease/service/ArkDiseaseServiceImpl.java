package au.org.theark.disease.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.Constants;
import au.org.theark.core.model.disease.entity.Disease;
import au.org.theark.core.model.disease.entity.Gene;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.vo.DiseaseVO;
import au.org.theark.core.vo.GeneVO;
import au.org.theark.disease.dao.IDiseaseDao;

@Transactional
@Service(Constants.ARK_DISEASE_SERVICE)
public class ArkDiseaseServiceImpl implements IArkDiseaseService {

	private IDiseaseDao diseaseDao;

	public IDiseaseDao getDiseaseDao() {
		return diseaseDao;
	}
	
	@Autowired
	public void setDiseaseDao(IDiseaseDao diseaseDao) {
		this.diseaseDao = diseaseDao;
	}
	
	public int getDiseaseCount(DiseaseVO diseaseVO) {
		return diseaseDao.getDiseaseCount(diseaseVO);
	}

	public List searchPageableDiseases(DiseaseVO diseaseVO, int first, int count) {
		return diseaseDao.searchPageableDiseases(diseaseVO, first, count);
	}

	public void save(Object object) {
		diseaseDao.save(object);
	}

	public void update(Object object) {
		diseaseDao.update(object);
	}
	
	public void delete(Object object) {
		diseaseDao.delete(object);
	}

	public int getGeneCount(GeneVO geneVO) {
		return diseaseDao.getGeneCount(geneVO);
	}

	public List<GeneVO> searchPageableGenes(GeneVO object, int first, int count) {
		return diseaseDao.searchPageableGenes(object, first, count);
	}

	public List<Gene> getAvailableGenesForStudy(Study study) {
		return diseaseDao.getAvailableGenesForStudy(study);
	}

	public List<Disease> getAvailableDiseasesForStudy(Study study) {
		return diseaseDao.getAvailableDiseasesForStudy(study);
	}
	
}
