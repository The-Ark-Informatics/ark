package au.org.spark.web.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
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
import au.org.spark.web.view.ComputationVo;
import au.org.spark.web.view.DataCenterVo;
import au.org.spark.web.view.DataSourceVo;
import au.org.spark.web.view.JavaBean;
import au.org.spark.web.view.Report;

@RestController
public class SparkRestController {

	@Value("${spark.computation.dir}")
	private String computationDir;

	@Autowired
	OpenStackService openStackService;

	@Autowired
	SshService sshService;

	@Autowired
	CassandraService cassandraService;

	@Autowired
	JobService reportService;

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
		if (DATA_CENTERS.SSH_TEST.toString().equals(datacenter.getName())) {
			try {
				list = sshService.listFilesAndDirectories(datacenter.getDirectory(), datacenter.getFileName());
			} catch (Exception e) {
				list = new ArrayList<DataSourceVo>();
			}
		} else if (DATA_CENTERS.SSH_LOCAL.toString().equals(datacenter.getName())) {
			list = new ArrayList<DataSourceVo>();
		} else if (DATA_CENTERS.FTP_TEST.toString().equals(datacenter.getName())) {
			list = new ArrayList<DataSourceVo>();
		}
		return list;
	}

	@RequestMapping(value = "/executeProcess", method = RequestMethod.POST)
	public @ResponseBody String executeProcess(@RequestBody DataSourceVo dataSource) {

		Future<Report> report = null;

		if ("uploading".equalsIgnoreCase(dataSource.getStatus())) {
			report = reportService.uploadDataSource(dataSource);
		} else {
			report = reportService.deleteDataSource(dataSource);
		}

		// Future<Report> report = reportService.generateReport();

		String UID = System.currentTimeMillis() + "_" + UUID.randomUUID();
		// session.setAttribute(UID, report);

		System.out.println("Generate from SPARK -- " + UID);
		reportMap.put(UID, report);

		return UID;
	}

	@RequestMapping(value = "/executePlinkProcess", method = RequestMethod.POST)
	public @ResponseBody String executePlinkProcess(@RequestBody DataCenterVo dataCenter) {

		Future<Report> report = null;
		try {
			if ("uploading".equalsIgnoreCase(dataCenter.getStatus())) {
				report = reportService.uploadPlinkDataSource(dataCenter);
			} else {
				report = reportService.deletePlinkDataSource(dataCenter);
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

		Future<Report> report = reportMap.get(uid);

		try {
			if (report.get() != null && report.isDone()) {
				System.out.println("Report Generation Done");
				status = "Completed";
			} else {
				System.out.println("Still Working on Report");
				status = "Running";
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = "Error";
		}
		return status;
	}

	@RequestMapping(value = "/createTable", method = RequestMethod.GET)
	public @ResponseBody String createTable() {
		cassandraService.createDataSourceTable(new DataSourceVo(), DATA_CENTERS.SSH_TEST);
		return "SUCCESS";
	}

	@RequestMapping(value = "/dropTable", method = RequestMethod.GET)
	public @ResponseBody String dropTable() {
		cassandraService.dropDataSourceTable(new DataSourceVo(), DATA_CENTERS.SSH_TEST);
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

	// @RequestMapping(value="/uploadFile", method=RequestMethod.POST)
	// public @ResponseBody String handleFileUpload(@RequestParam("programId")
	// String programId, @RequestParam("programName") String programName
	// ,@RequestParam("file") MultipartFile file){
	// if (!file.isEmpty()) {
	// try {
	// byte[] bytes = file.getBytes();
	// BufferedOutputStream stream = new BufferedOutputStream(new
	// FileOutputStream(new File(programId + "-uploaded")));
	// stream.write(bytes);
	// stream.close();
	// return "You successfully uploaded " + programName + " into " + programId
	// + "-uploaded !";
	// } catch (Exception e) {
	// return "You failed to upload " + programName + " => " + e.getMessage();
	// }
	// } else {
	// return "You failed to upload " + programName +
	// " because the file was empty.";
	// }
	// }

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public @ResponseBody String handleFileUpload(@RequestBody ComputationVo computation) {

		// Future<Report> report = null;

		// if ("uploading".equalsIgnoreCase(dataSource.getStatus())) {
		// report = reportService.uploadDataSource(dataSource);
		// } else {
		// report = reportService.deleteDataSource(dataSource);
		// }
		//
		// // Future<Report> report = reportService.generateReport();
		//
		// String UID = System.currentTimeMillis() + "_" + UUID.randomUUID();
		// // session.setAttribute(UID, report);
		//
		// System.out.println("Generate from SPARK -- " + UID);
		// reportMap.put(UID, report);

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
				return "You successfully uploaded " + fileId + "!";
			} catch (Exception e) {
				e.printStackTrace();
				return "You failed to upload " + fileId + " => " + e.getMessage();
			}
		} else {
			return "You failed to upload " + fileId + " because the file was empty.";
		}
	}
	
	@RequestMapping(value = "/compile", method = RequestMethod.POST)
	public @ResponseBody String compileProgram(@RequestParam("name") String fileId) {
		String array[] = fileId.split("[.]");
		System.out.println("Call compile");
		sshService.compileProgram(array[0]);
		
		return "Compiled";
	}

	// public static void main(String[] args) {
	// String
	// s="1434431560561_585dcc87-fb41-4929-8519-8b7f61b50367_spark_program.zip";
	// String array[] = s.split("[.]");
	// String dirName = "ABC" +File.separator +array[0];
	//
	// }

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

}
