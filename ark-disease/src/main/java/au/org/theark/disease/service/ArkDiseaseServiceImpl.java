package au.org.theark.disease.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.Constants;
import au.org.theark.core.model.disease.entity.Affection;
import au.org.theark.core.model.disease.entity.AffectionCustomFieldData;
import au.org.theark.core.model.disease.entity.AffectionStatus;
import au.org.theark.core.model.disease.entity.Disease;
import au.org.theark.core.model.disease.entity.Gene;
import au.org.theark.core.model.disease.entity.Position;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.disease.dao.IDiseaseDao;
import au.org.theark.disease.vo.AffectionListVO;
import au.org.theark.disease.vo.AffectionVO;
import au.org.theark.disease.vo.DiseaseVO;
import au.org.theark.disease.vo.GeneVO;

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

	public int getAffectionCount(AffectionVO affectionVO) {
		return diseaseDao.getAffectionCount(affectionVO);
	}

	public List<AffectionVO> searchPageableAffections(AffectionVO affectionVO, int first, int count) {
		return diseaseDao.searchPageableAffections(affectionVO, first, count);
	}

	public List<AffectionStatus> getAffectionStatus() {
		return diseaseDao.getAffectionStatus();
	}

	public List<Affection> getPersonsAffections(Affection affection) {
		return diseaseDao.getPersonsAffections(affection);
	}

	public List<AffectionListVO> searchPageableAffectionListVOs(AffectionListVO affectionListVO, int first, int count) {
		return diseaseDao.searchPageableAffectionListVOs(affectionListVO, first, count);
	}

	public int getAffectionCount(LinkSubjectStudy linkSubjectStudy) {
		return diseaseDao.getAffectionCount(linkSubjectStudy);
	}

	public List<AffectionCustomFieldData> getAffectionCustomFieldData(Affection affection) {
		return diseaseDao.getAffectionCustomFieldData(affection);
	}

	public List<Position> getPositions(Affection affection) {
		return diseaseDao.getPositions(affection);
	}

	public Gene getGeneByID(Long id) {
		return diseaseDao.getGeneById(id);
	}

	public Affection getAffectionByID(Long id) {
		return diseaseDao.getAffectionByID(id);
	}
}
