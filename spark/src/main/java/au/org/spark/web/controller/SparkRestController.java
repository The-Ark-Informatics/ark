package au.org.spark.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import au.org.spark.service.CassandraService;
import au.org.spark.service.OpenStackService;
import au.org.spark.service.SshService;
import au.org.spark.util.Constants;
import au.org.spark.web.view.DataCenterVo;
import au.org.spark.web.view.DataSourceVo;
import au.org.spark.web.view.JavaBean;

import static au.org.spark.util.Constants.DATA_CENTERS;

;

@RestController
public class SparkRestController {

	@Autowired
	OpenStackService openStackService;

	@Autowired
	SshService sshService;

	@Autowired
	CassandraService CassandraService;

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
		return CassandraService.getRingMembers();
	}

	@RequestMapping(value = "/insertCassandra", method = RequestMethod.GET)
	public @ResponseBody String insertCassandra() {
		CassandraService.insert();
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

	@RequestMapping(value = "/datasources", method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody List<DataSourceVo> listDataSourcess(@RequestBody DataCenterVo datacenter) {
		
		List<DataSourceVo> list = null;
		if(DATA_CENTERS.SSH_TEST.toString().equals(datacenter.getName())){
		try{
			list=sshService.listFilesAndDirectories(datacenter.getDirectory(), datacenter.getFileName());
		}catch(Exception e){
			list=new ArrayList<DataSourceVo>();
		}
		}else if(DATA_CENTERS.SSH_LOCAL.toString().equals(datacenter.getName())){
			list=new ArrayList<DataSourceVo>();
		}else if(DATA_CENTERS.FTP_TEST.toString().equals(datacenter.getName())){
			list=new ArrayList<DataSourceVo>();
		}
		return list;
	}
}
