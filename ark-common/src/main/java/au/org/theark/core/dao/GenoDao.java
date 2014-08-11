/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import au.org.theark.core.model.geno.entity.Beam;
import au.org.theark.core.model.geno.entity.Command;
import au.org.theark.core.model.geno.entity.Data;
import au.org.theark.core.model.geno.entity.Pipeline;
import au.org.theark.core.model.geno.entity.Process;
import au.org.theark.core.model.geno.entity.ProcessInput;
import au.org.theark.core.model.geno.entity.ProcessOutput;
import au.org.theark.core.model.geno.entity.Row;
import au.org.theark.core.model.study.entity.Person;
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
		
		if(p.getPipeline() != null && p.getPipeline().getId() != null) {
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
		List<Pipeline> list = new ArrayList();
		if(p !=null && p.getPipeline().getId() == null) {
			return list;
		}
		Criteria criteria = buildGeneralProcessCriteria(p);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		list = criteria.list();
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
	
	public List<ProcessInput> getProcessInputsForProcess(Process p) {
		List<ProcessInput> list = new ArrayList();
		Criteria criteria = buildGeneralProcessInputCriteria(p);
		list = criteria.list();
		return list; 
	}

	private Criteria buildGeneralProcessInputCriteria(Process p) {
		Criteria criteria = getSession().createCriteria(ProcessInput.class);
		
		if(p.getId() != null){
			criteria.add(Restrictions.eq("process", p));
		}
		
		return criteria;
	}

	public List getProcessOutputsForProcess(Process process) {
		List<ProcessInput> list = new ArrayList();
		Criteria criteria = buildGeneralProcessOutputCriteria(process);
		list = criteria.list();
		return list; 
	}

	private Criteria buildGeneralProcessOutputCriteria(Process process) {
		Criteria criteria = getSession().createCriteria(ProcessOutput.class);
		
		if(process.getId() != null){
			criteria.add(Restrictions.eq("process", process));
		}
		
		return criteria;
	}
	
	public Collection<Row> getGenoTableRow(Study study) {
		Criteria criteria = getSession().createCriteria(Row.class);
		criteria.add(Restrictions.eq("study", study));
		
		Collection<Row> results = criteria.list();
		
		return results;
	}
	
	public Collection<Beam> getGenoTableBeam(Study study) {
		Criteria criteria = getSession().createCriteria(Beam.class);
		criteria.add(Restrictions.eq("study", study));
		
		Collection<Beam> results = criteria.list();
		
		return results;
	}

	public Collection<Data> getGenoTableData(Study study, Person person) {
		return getSession().createCriteria(Data.class).add(Restrictions.eq("study", study)).add(Restrictions.eq("person", person)).list();
	}
	
	public Data getDataGivenRowandColumn(Person person, Row row, Beam beam) {
		return (Data) getSession().createCriteria(Data.class).add(Restrictions.eq("row", row)).add(Restrictions.eq("beam", beam)).add(Restrictions.eq("person", person)).uniqueResult();
	}

	public void saveOrUpdate(Data data) {
		getSession().saveOrUpdate(data);
	}

	public void createOrUpdateRows(List<Row> rowList) {
		for(Row row : rowList) {
			getSession().saveOrUpdate(row);
		}
	}

	public void createOrUpdateBeams(List<Beam> beamList) {
		for(Beam beam : beamList) {
			getSession().saveOrUpdate(beam);
		}
	}

	public void delete(Object object) {
		getSession().delete(object);
	}
	
}