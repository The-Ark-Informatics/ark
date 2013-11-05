/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.dao;

import org.springframework.stereotype.Repository;

import au.org.theark.core.model.geno.entity.Pipeline;
import au.org.theark.core.model.study.entity.Study;

/**
 * @author nivedann
 * 
 */
@Repository("genoDao")
public class GenoDao extends HibernateSessionDao implements IGenoDao {
	public void createPipeline(Pipeline p){
		getSession().save(p);
	}

	public void updatePipeline(Pipeline p){
		getSession().update(p);
	}

	public void deletePipeline(Pipeline p) {
		getSession().delete(p);
	}

	public long getPipelineCount(Study study){
		return study.getPipelines().size();//TODO  woefully inappropriate speed/efficiency FIX
	}
	
}
