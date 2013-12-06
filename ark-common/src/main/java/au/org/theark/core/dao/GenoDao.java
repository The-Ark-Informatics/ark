/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import au.org.theark.core.model.geno.entity.Command;
import au.org.theark.core.model.geno.entity.Pipeline;
import au.org.theark.core.model.geno.entity.Process;
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

	public int getPipelineCount(Pipeline p) {
		// Handle for study not in context
		if (p.getStudy() == null) {
			return 0;
		}

		Criteria criteria = buildGeneralPipelineCriteria(p);
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		return totalCount.intValue();
	}

	public List<Pipeline> searchPageablePipelines(Pipeline p, int first,
			int count) {
		Criteria criteria = buildGeneralPipelineCriteria(p);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		List<Pipeline> list = criteria.list();
		return list;
	}

	private Criteria buildGeneralPipelineCriteria(Pipeline p) {
		Criteria criteria = getSession().createCriteria(Pipeline.class);
		if (p.getId() != null){ 
			criteria.add(Restrictions.eq("id", p.getId()));
		}
		
		if(p.getName() != null){
			criteria.add(Restrictions.eq("name", p.getName()));
		}
	
		if(p.getDescription()!=null) {
			criteria.add(Restrictions.eq("description", p.getDescription()));
		}
		
		if(p.getStudy() != null) {
			criteria.add(Restrictions.eq("study", p.getStudy()));
		}
		
		return criteria;
	}

	public long getPipelineCount(Study study) {
		return 0;
	}
	
	private Criteria buildGeneralProcessCriteria(Process p) {
		Criteria criteria = getSession().createCriteria(Process.class);
		if (p.getId() != null){ 
			criteria.add(Restrictions.eq("id", p.getId()));
		}
		
		if(p.getName() != null){
			criteria.add(Restrictions.eq("name", p.getName()));
		}
	
		if(p.getDescription()!=null) {
			criteria.add(Restrictions.eq("description", p.getDescription()));
		}
		
		if(p.getPipeline() != null) {
			criteria.add(Restrictions.eq("pipeline", p.getPipeline()));
		}
		
		return criteria;
	}

	public int getProcessCount(Process p) {
		Criteria criteria = buildGeneralProcessCriteria(p);
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		return totalCount.intValue();
	}

	public List searchPageableProcesses(Process p, int first, int count) {
		Criteria criteria = buildGeneralProcessCriteria(p);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		List<Pipeline> list = criteria.list();
		return list;
	}

	public void createProcess(Process p) {
		getSession().save(p);
	}

	public void deleteProcess(Process p) {
		getSession().delete(p);
	}
	
	public void updateProcess(Process p) {
		getSession().update(p);
	}

	public List<Command> getCommands() {
		Criteria criteria = getSession().createCriteria(Command.class);
		return criteria.list();
	}
}