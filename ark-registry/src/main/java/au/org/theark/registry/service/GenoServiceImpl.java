/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.registry.service;

import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	public void createPipeline(Pipeline p){
		;
	}

	@Autowired
	public void updatePipeline(Pipeline p){
		;
	}

	@Autowired
	public void deletePipeline(Pipeline p) {
		// TODO Auto-generated method stub
		
	}
}
