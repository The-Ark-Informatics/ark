/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.dao;

import au.org.theark.core.model.geno.entity.Pipeline;
import au.org.theark.core.model.study.entity.Study;

/**
 * @author nivedann
 * 
 */
public interface IGenoDao {
	public void createPipeline(Pipeline p);

	public void updatePipeline(Pipeline p);

	public void deletePipeline(Pipeline p);

	public long getPipelineCount(Study study);

}
