/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.registry.service;

import au.org.theark.core.model.geno.entity.Pipeline;

/**
 * @author nivedann
 * 
 */
public interface IGenoService {
	public void createPipeline(Pipeline p);

	public void updatePipeline(Pipeline p);
	
	public void deletePipeline(Pipeline p);
}
