/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.dao;

import java.util.List;

import au.org.theark.core.model.geno.entity.Command;
import au.org.theark.core.model.geno.entity.Pipeline;
import au.org.theark.core.model.geno.entity.Process;
import au.org.theark.core.model.study.entity.Study;

/**
 * @author nivedann
 * 
 */
public interface IGenoDao {
	public void createPipeline(Pipeline p);

	public void updatePipeline(Pipeline p);

	public void deletePipeline(Pipeline p);

	public int getPipelineCount(Pipeline p);

	public List<Pipeline> searchPageablePipelines(Pipeline p, int first,
			int count);

	public long getPipelineCount(Study study);

	public int getProcessCount(Process p);

	public List searchPageableProcesses(Process p, int first, int count);

	public void createProcess(Process p);

	public void deleteProcess(Process p);

	public void updateProcess(Process p);

	public List<Command> getCommands();

}

