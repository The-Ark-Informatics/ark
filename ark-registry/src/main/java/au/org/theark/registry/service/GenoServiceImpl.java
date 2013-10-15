/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.registry.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.model.geno.entity.Pipeline;

/**
 * @author nivedann
 * 
 */

@Transactional
@Service("genoService")
public class GenoServiceImpl implements IGenoService {
	public void createPipeline(Pipeline p){
		;
	}

	public void updatePipeline(Pipeline p){
		;
	}

	public void deletePipeline(Pipeline p) {
		// TODO Auto-generated method stub
		
	}
}
