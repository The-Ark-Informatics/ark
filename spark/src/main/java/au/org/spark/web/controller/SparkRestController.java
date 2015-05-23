package au.org.spark.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import au.org.spark.jobs.JobService;
import au.org.spark.service.CassandraService;
import au.org.spark.service.OpenStackService;
import au.org.spark.service.SshService;
import au.org.spark.util.Constants;
import au.org.spark.util.Constants.DATA_CENTERS;
import au.org.spark.web.view.DataCenterVo;
import au.org.spark.web.view.DataSourceVo;
import au.org.spark.web.view.JavaBean;
import au.org.spark.web.view.Report;

@RestController
public class SparkRestController {

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

}
