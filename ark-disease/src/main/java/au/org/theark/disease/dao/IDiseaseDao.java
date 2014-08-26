package au.org.theark.disease.dao;

import java.util.List;

import au.org.theark.core.model.disease.entity.Disease;
import au.org.theark.core.model.disease.entity.Gene;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.vo.DiseaseVO;
import au.org.theark.core.vo.GeneVO;

public interface IDiseaseDao {
	
	public int getDiseaseCount(DiseaseVO diseaseVO);
	
	public List<DiseaseVO> searchPageableDiseases(DiseaseVO object, int first, int count);
	
	public int getGeneCount(GeneVO geneVO);

	public List<GeneVO> searchPageableGenes(GeneVO object, int first, int count);
	
	public void save(Object object);
	
	public void update(Object object);
	
	public void delete(Object object);
	
	public List<Gene> getAvailableGenesForStudy(Study study);
	
	public List<Disease> getAvailableDiseasesForStudy(Study study);
	
}
