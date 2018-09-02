package au.org.spark.service;

import java.util.List;

import au.org.spark.web.view.AnalysisJobVo;
import au.org.spark.web.view.AnalysisVo;
import au.org.spark.web.view.ComputationVo;
import au.org.spark.web.view.DataCenterVo;
import au.org.spark.web.view.DataSourceVo;
import au.org.spark.web.view.QueryVo;

public interface SshService {
	
	public List<String> listFiles(String directory) throws Exception;
	
	public List<DataSourceVo> listFilesAndDirectories(String directory, String fileName) throws Exception;
	
	public void readFile(String directory, String fileName) throws Exception;
	
	public void processMapFile(DataSourceVo dataSource) throws Exception;
	
	public void processPlinkDataSource(DataCenterVo dataSource) throws Exception;
	
	public void onlinePlinkDataSource(DataCenterVo dataCenter) throws Exception;
	
	public void uploadProgram(String destinationDir, String name);
	
	public String compileProgram(ComputationVo computationVo);
	
	public void executeAnalysis(AnalysisVo analysisVo);
	
	public String getAnalysisResults(AnalysisVo analysisVo);
	
	public String jobSubmission(final AnalysisVo analysisVo);
	
	public void uploadJobScript(AnalysisJobVo job, String srcPath);
	
	public String getJobStatus(String jobId);
	
	public String queryResult(DataCenterVo dataCenterVo);
	
	public String getQueryResults(DataCenterVo dataCenterVo);
	
	public void executeQuery(QueryVo queryVo) throws Exception;
	
}
