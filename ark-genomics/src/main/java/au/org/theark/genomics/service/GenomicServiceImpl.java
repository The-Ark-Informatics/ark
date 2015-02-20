package au.org.theark.genomics.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.model.spark.entity.MicroService;
import au.org.theark.genomics.model.dao.IGenomicsDao;
import au.org.theark.genomics.util.Constants;

@Transactional
@Service(Constants.GENOMIC_SERVICE)
public class GenomicServiceImpl implements IGenomicService {

	@Autowired
	IGenomicsDao genomicsDao;
	
	public void saveOrUpdate(MicroService microService) {
		genomicsDao.saveOrUpdate(microService);
		
	}

	public void delete(MicroService microService) {
		genomicsDao.delete(microService);
		
	}

	public List<MicroService> searchMicroService(MicroService microService) {
		return genomicsDao.searchMicroService(microService);
	}
	
}
