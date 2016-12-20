package au.org.spark.web.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import au.org.spark.jobs.JobService;
import au.org.spark.service.CassandraService;
import au.org.spark.service.OpenStackService;
import au.org.spark.service.SshService;
import au.org.spark.util.Constants;
import au.org.spark.util.Constants.DATA_CENTERS;
import au.org.spark.web.view.AnalysisJobVo;
import au.org.spark.web.view.AnalysisVo;
import au.org.spark.web.view.ComputationVo;
import au.org.spark.web.view.DataCenterVo;
import au.org.spark.web.view.DataSourceVo;
import au.org.spark.web.view.JavaBean;
import au.org.spark.web.view.Report;

@RestController
public class SparkRestController {

	@Value("${spark.computation.dir}")
	private String computationDir;

	@Value("${spark.analysis.dir}")
	private String analysisDir;

	@Autowired
	OpenStackService openStackService;

	@Autowired
	SshService sshService;

	@Autowired
	CassandraService cassandraService;

	@Autowired
	JobService jobService;

	private HashMap<String, Future<Report>> reportMap = new HashMap<String, Future<Report>>();

	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public @ResponseBody String getStatus() {
		return "Available";
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public @ResponseBody JavaBean getJSONJavaBean() {
		openStackService.listContainers();
		return new JavaBean();
	}

	@RequestMapping(value = "/testp", method = RequestMethod.POST)
	public @ResponseBody String getPostValue() {
		// openStackService.listContainers();
		return "Available";
	}

	@RequestMapping(value = "/listContainers", method = RequestMethod.GET)
	public @ResponseBody List<String> listContainers() {
		return openStackService.listContainers();
	}

	@RequestMapping(value = "/listFiles", method = RequestMethod.GET)
	public @ResponseBody List<String> listFiles(@RequestParam("dirName") String dirName) {
		List<String> list = null;
		try {
			list = sshService.listFiles(dirName);
		} catch (Exception e) {
			list = new ArrayList<String>();
			list.add("No dir found");
		}
		return list;
	}

	@RequestMapping(value = "/listCassandra", method = RequestMethod.GET)
	public @ResponseBody String listCassandra() {
		return cassandraService.getRingMembers();
	}

	@RequestMapping(value = "/insertCassandra", method = RequestMethod.GET)
	public @ResponseBody String insertCassandra() {
		cassandraService.insert();
		return "SUCCESS";
	}

	@RequestMapping(value = "/mapping/producesjson", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody JavaBean byProducesJson() {
		return new JavaBean();
	}

	@RequestMapping(value = "/mapping/producesxml", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody JavaBean byProducesXml() {
		return new JavaBean();
	}

	@RequestMapping(value = "/datacenters", method = RequestMethod.GET)
	public @ResponseBody List<String> listDataCenters() {
		return Constants.listDataCenters();
	}

	@RequestMapping(value = "/datasources", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<DataSourceVo> listDataSourcess(@RequestBody DataCenterVo datacenter) {

		List<DataSourceVo> list = null;
		if (DATA_CENTERS.SSH_HPC.toString().equals(datacenter.getName())) {
			try {
				list = sshService.listFilesAndDirectories(datacenter.getDirectory(), datacenter.getFileName());
			} catch (Exception e) {
				list = new ArrayList<DataSourceVo>();
			}
		} 
		//else if (DATA_CENTERS.SSH_LOCAL.toString().equals(datacenter.getName())) {
//			list = new ArrayList<DataSourceVo>();
//		} else if (DATA_CENTERS.FTP_TEST.toString().equals(datacenter.getName())) {
//			list = new ArrayList<DataSourceVo>();
//		}
		return list;
	}

	@RequestMapping(value = "/executeProcess", method = RequestMethod.POST)
	public @ResponseBody String executeProcess(@RequestBody DataSourceVo dataSource) {

		Future<Report> report = null;

		if ("uploading".equalsIgnoreCase(dataSource.getStatus())) {
			report = jobService.uploadDataSource(dataSource);
		} else {
			report = jobService.deleteDataSource(dataSource);
		}

		String UID = System.currentTimeMillis() + "_" + UUID.randomUUID();

		System.out.println("Generate from SPARK -- " + UID);
		reportMap.put(UID, report);

		return UID;
	}

	@RequestMapping(value = "/executePlinkProcess", method = RequestMethod.POST)
	public @ResponseBody String executePlinkProcess(@RequestBody DataCenterVo dataCenter) {

		Future<Report> report = null;
		try {
			if (Constants.STATUS_UNPROCESSED.equalsIgnoreCase(dataCenter.getStatus())) {
				report = jobService.uploadPlinkDataSource(dataCenter);
			} else {
				report = jobService.deletePlinkDataSource(dataCenter);
			}
		} catch (Exception e) {
			report = new Future<Report>() {

				@Override
				public boolean cancel(boolean mayInterruptIfRunning) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean isCancelled() {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean isDone() {

					return false;
				}

				@Override
				public Report get() throws InterruptedException, ExecutionException {
					throw new InterruptedException();
					// return null;
				}

				@Override
				public Report get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
					// TODO Auto-generated method stub
					return null;
				}

			};
		}

		String UID = System.currentTimeMillis() + "_" + UUID.randomUUID();

		System.out.println("Generate from SPARK -- " + UID);
		reportMap.put(UID, report);

		return UID;
	}

	@RequestMapping(value = "/processStatus", method = RequestMethod.POST)
	public @ResponseBody String reportStatus(@RequestBody String uid) {

		String status = "Failed";

		System.out.println("Process UID -- " + uid);

		Future<Report> report = reportMap.get(uid);

		try {
			if (report.get() != null && report.isDone()) {
				System.out.println("Report Generation Done");
				status = Constants.STATUS_PROCESSED;
			} else {
				System.out.println("Still Working on Report");
				status = Constants.STATUS_PROCESSING;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = "Error";
		}
		return status;
	}
	
	@RequestMapping(value = "/queueStatus", method = RequestMethod.POST)
	public @ResponseBody String queueStatus(@RequestBody String jobId) {



		System.out.println("Job UID -- " + jobId);
		
		String status = sshService.getJobStatus(jobId);

		System.out.println(status);	
		
		return status;
	}


	@RequestMapping(value = "/createTable", method = RequestMethod.GET)
	public @ResponseBody String createTable() {
		cassandraService.createDataSourceTable(new DataSourceVo(), DATA_CENTERS.SSH_HPC);
		return "SUCCESS";
	}

	@RequestMapping(value = "/dropTable", method = RequestMethod.GET)
	public @ResponseBody String dropTable() {
		cassandraService.dropDataSourceTable(new DataSourceVo(), DATA_CENTERS.SSH_HPC);
		return "SUCCESS";
	}

	@RequestMapping(value = "/readFile", method = RequestMethod.GET)
	public @ResponseBody String readFile() {
		try {
			sshService.readFile("plink", "10SAMPLE.ped");
		} catch (Exception e) {
			e.printStackTrace();
			return "FAILED";
		}
		return "SUCCESS";
	}

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public @ResponseBody String handleFileUpload(@RequestBody ComputationVo computation) {

		System.out.println(computation.getProgram());

		return computation.getProgramId();
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody String handleFileUpload(@RequestParam("name") String fileId, @RequestParam("file") MultipartFile file) {

		byte[] buffer = new byte[1024];
		if (!file.isEmpty()) {
			try {

				String array[] = fileId.split("[.]");
				String dirName = computationDir + File.separator + array[0];

				File outputFolder = new File(dirName);
				if (!outputFolder.exists()) {
					outputFolder.mkdir();
				}

				ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(file.getBytes()));
				// get the zipped file list entry
				ZipEntry ze = zis.getNextEntry();

				boolean firstEntry = false;

				while (ze != null) {

					String fileName = ze.getName();

					if (!firstEntry) {
						System.out.println("ROOT_DIR is --- " + fileName);
						firstEntry = true;
					}

					File newFile = new File(outputFolder + File.separator + fileName);

					System.out.println("file unzip : " + newFile.getAbsoluteFile());

					if (ze.isDirectory()) {
						newFile.mkdirs();
					} else {
						FileOutputStream fos = new FileOutputStream(newFile);
						int len;
						while ((len = zis.read(buffer)) > 0) {
							fos.write(buffer, 0, len);
						}
						fos.close();
						if (newFile.getName().equals("spark.info")) {

							BufferedReader br = new BufferedReader(new FileReader(newFile));
							String sCurrentLine;
							while ((sCurrentLine = br.readLine()) != null) {
								System.out.println(sCurrentLine);
							}
							br.close();
						}
					}
					ze = zis.getNextEntry();
				}

				zis.closeEntry();
				zis.close();

				// byte[] bytes = file.getBytes();
				// BufferedOutputStream stream =
				// new BufferedOutputStream(new FileOutputStream(new
				// File(name)));
				// stream.write(bytes);
				// stream.close();

				/**
				 * Start reading the extract file
				 */

				sshService.uploadProgram(computationDir, array[0]);
				return "Uploaded:" + fileId + "!";
			} catch (Exception e) {
				e.printStackTrace();
				return "You failed to upload " + fileId + " => " + e.getMessage();
			}
		} else {
			return "You failed to upload" + fileId + " because the file was empty.";
		}
	}

	@RequestMapping(value = "/compile", method = RequestMethod.POST)
	public @ResponseBody String compileProgram(@RequestBody ComputationVo computationVo) {
		Future<Report> report = null;
		try {
			report = jobService.compileProgram(computationVo);
		} catch (Exception e) {
			report = new Future<Report>() {

				@Override
				public boolean cancel(boolean mayInterruptIfRunning) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean isCancelled() {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean isDone() {

					return false;
				}

				@Override
				public Report get() throws InterruptedException, ExecutionException {
					throw new InterruptedException();
					// return null;
				}

				@Override
				public Report get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
					// TODO Auto-generated method stub
					return null;
				}

			};
		}

		String UID = System.currentTimeMillis() + "_" + UUID.randomUUID();

		System.out.println("Generate from SPARK -- " + UID);
		reportMap.put(UID, report);

		return UID;
	}

	@RequestMapping(value = "/executeAnalysis", method = RequestMethod.POST)
	public @ResponseBody String executeAnalysis(@RequestBody AnalysisVo analysis) {

		Future<Report> report = null;
		try {
			report = jobService.executeAnalysis(analysis);
		} catch (Exception e) {
			report = new Future<Report>() {

				@Override
				public boolean cancel(boolean mayInterruptIfRunning) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean isCancelled() {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean isDone() {

					return false;
				}

				@Override
				public Report get() throws InterruptedException, ExecutionException {
					throw new InterruptedException();
					// return null;
				}

				@Override
				public Report get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
					// TODO Auto-generated method stub
					return null;
				}

			};
		}

		String UID = System.currentTimeMillis() + "_" + UUID.randomUUID();

		System.out.println("Generate from SPARK -- " + UID);
		reportMap.put(UID, report);

		return UID;
	}

	@RequestMapping(value = "/getAnalysisResult", method = RequestMethod.POST)
	public @ResponseBody String getAnalysisResult(@RequestBody AnalysisVo analysis) {
		String result = "";
		System.out.println("------------Job Id------------   "+analysis.getJobId());
		result = sshService.getAnalysisResults(analysis);
		return result;
	}

	private static void traverse(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; children != null && i < children.length; i++) {
				traverse(new File(dir, children[i]));
			}
		}
		if (dir.isFile()) {
			if (dir.getName().endsWith(".zip") || dir.getName().endsWith(".xml")) {
				System.out.println(dir.getAbsolutePath());// change it if needed
			}
		}
	}

	@RequestMapping(value = "/jobSubmission", method = RequestMethod.POST)
	public @ResponseBody String jobSubmission(@RequestParam("analysis") String analysis, @RequestParam("file") MultipartFile file) {
		String status = ""; 

		System.out.println("-------------------------------------------------------");
		System.out.println(analysis);
		System.out.println("-------------------------------------------------------");

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(AnalysisJobVo.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			StringReader reader = new StringReader(analysis);
			AnalysisJobVo analysisJobVo = (AnalysisJobVo) unmarshaller.unmarshal(reader);

			if (!file.isEmpty()) {
				System.out.println("---------------------------------------------------");
				System.out.println(file.getName());
				System.out.println("---------------------------------------------------");
				String dirName = analysisDir + File.separator + analysisJobVo.getAnalysisId();

				File outputFolder = new File(dirName);
				if (!outputFolder.exists()) {
					System.out.println(dirName);
					outputFolder.mkdir();
				}
				String src = dirName+File.separator+analysisJobVo.getScriptName();
				file.transferTo(new File(src));
				sshService.uploadJobScript(analysisJobVo, src);
				status=Constants.STATUS_JOB_SUBMIT;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status=Constants.STATUS_JOB_SUBMIT_FAILED;
		}
		return status;
	}
	
	@RequestMapping(value = "/jobQueue", method = RequestMethod.POST)
	public @ResponseBody String jobQueue(@RequestBody AnalysisVo analysis) {
		String jobId = ""; 
		try {
			
			
			System.out.println("-------------------"+analysis.getScriptName());
			
			jobId=sshService.jobSubmission(analysis);
				
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return jobId;
	}
	
	@RequestMapping(value = "/queryResult", method = RequestMethod.POST)
	public @ResponseBody String queryResult(@RequestBody DataCenterVo dataCenter) {
		String result = "No Result Found"; 
		try {
			
			System.out.println("--------Query Result Execution-----------"+dataCenter.getIndividualId());
			
			result=sshService.queryResult(dataCenter);
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("-------------Result----------------"+ result);
		return result;
	}
	
	@RequestMapping(value = "/queryOutput", method = RequestMethod.POST)
	public @ResponseBody String queryOutput(@RequestBody DataCenterVo dataCenter) {
		String result = "No Result Found"; 
		try {
			
			System.out.println("--------Query Result Execution-----------"+dataCenter.getIndividualId());
			
			result=sshService.getQueryResults(dataCenter);
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("-------------Result----------------"+ result);
		return result;
	}

}
