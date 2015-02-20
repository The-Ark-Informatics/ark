package au.org.theark.genomics.service;

import java.util.List;

import au.org.theark.core.model.spark.entity.MicroService;

public interface IGenomicService {

	public void saveOrUpdate(MicroService microService);

	public void delete(MicroService microService);

	public List<MicroService> searchMicroService(final MicroService microService);

}
