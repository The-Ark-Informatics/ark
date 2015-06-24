package au.org.theark.genomics.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.spark.entity.Computation;
import au.org.theark.core.model.spark.entity.DataSource;
import au.org.theark.core.model.spark.entity.DataSourceType;
import au.org.theark.core.model.spark.entity.MicroService;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.genomics.model.dao.IGenomicsDao;
import au.org.theark.genomics.model.vo.DataCenterVo;
import au.org.theark.genomics.model.vo.DataSourceVo;
import au.org.theark.genomics.util.Constants;

@Transactional
@Service(Constants.GENOMIC_SERVICE)
public class GenomicServiceImpl implements IGenomicService {

	@Autowired
	IGenomicsDao genomicsDao;
	
	@Autowired
	IArkCommonService iArkCommonService;

	Logger log = LoggerFactory.getLogger(GenomicServiceImpl.class);

	public void saveOrUpdate(MicroService microService) {
		genomicsDao.saveOrUpdate(microService);
	}

	public void delete(MicroService microService) {
		genomicsDao.delete(microService);
	}

	public void saveOrUpdate(DataSource dataSource) {
		genomicsDao.saveOrUpdate(dataSource);
	}

	public void delete(DataSource dataSource) {
		genomicsDao.delete(dataSource);
	}
	
	@Override
	public void save(Computation computation, byte[] attachement) throws ArkSystemException {
		Long computationId= genomicsDao.saveOrUpdate(computation);
		
		if (attachement != null) {
			Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
//			String subjectUID = correspondence.getLss().getSubjectUID();
			String fileName = computation.getProgramName();
//			byte[] payload = correspondence.getAttachmentPayload();

			// Generate unique file id for given file name
			String fileId = iArkCommonService.generateArkFileId(fileName);

			// Set unique subject file id
			computation.setProgramId(fileId);

			// Save the attachment to directory configured in application.properties {@code fileAttachmentDir}
			iArkCommonService.saveArkFileAttachment(studyId, computationId.toString(), Constants.ARK_GENOMICS_COMPUTATION_DIR, fileName, attachement, fileId);

			// Remove the attachment
			genomicsDao.saveOrUpdate(computation);
		}
		
	}
	
	@Override
	public void update(Computation computation,byte[] attachement, String checksum) throws ArkSystemException {
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
//		String subjectUID = correspondence.getLss().getSubjectUID();
		String fileName = computation.getProgramName();
		String prevChecksum = computation.getChecksum();

		String fileId = null;
		if (attachement != null) {

			if (computation.getProgramId() != null) {

				// Get existing file Id
				fileId = computation.getProgramId();

				// Delete existing attachment
				iArkCommonService.deleteArkFileAttachment(studyId, computation.getId().toString(), fileId, Constants.ARK_GENOMICS_COMPUTATION_DIR,prevChecksum);

				// Generate unique file id for given file name
				fileId = iArkCommonService.generateArkFileId(fileName);

				// Set unique subject file id
				computation.setProgramId(fileId);

				// Save the attachment to directory configured in application.properties {@code fileAttachmentDir}
				iArkCommonService.saveArkFileAttachment(studyId, computation.getId().toString(), Constants.ARK_GENOMICS_COMPUTATION_DIR, fileName, attachement, fileId);
			}
			else {
				// Generate unique file id for given file name
				fileId = iArkCommonService.generateArkFileId(fileName);

				// Set unique subject file id
				computation.setProgramId(fileId);

				// Save the attachment to directory configured in application.properties {@code fileAttachmentDir}
				iArkCommonService.saveArkFileAttachment(studyId, computation.getId().toString(), Constants.ARK_GENOMICS_COMPUTATION_DIR, fileName, attachement, fileId);
			}
			//Set new file checksum
			computation.setChecksum(checksum);
		}
		else {
			if (computation.getProgramId() != null) {
				// Get existing file Id
				fileId = computation.getProgramId();

				// Delete existing attachment
				iArkCommonService.deleteArkFileAttachment(studyId, computation.getId().toString(), fileId, Constants.ARK_GENOMICS_COMPUTATION_DIR,prevChecksum);
				
				//remove existing attachment file id and checksum
				computation.setProgramId(null);
				computation.setChecksum(null);
			}
		}
		// Remove the attachment
		genomicsDao.saveOrUpdate(computation);
		
	}
	
	@Override
	public void delete(Computation computation) {
		// TODO Auto-generated method stub		
	}

	public List<MicroService> searchMicroService(MicroService microService) {
		List<MicroService> serviceList = genomicsDao.searchMicroService(microService);
		for (MicroService service : serviceList) {
			service.setStatus(checkServiceStatus(service));
		}
		return serviceList;
	}

	private String checkServiceStatus(final MicroService microService) {
		String status = Constants.STATUS_NOT_AVAILABLE;

		try {

			URL url = new URL(microService.getServiceUrl() + "/status");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				status = status + " - " + conn.getResponseCode();
			} else {
				status = Constants.STATUS_AVAILABLE;
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			log.error("Invalid URL ", e);
			status = status + " - 404";

		} catch (IOException e) {
			log.error("IO eroor", e);
			status = status + " - 404";
		}

		return status;
	}

	public List<String> searchDataCenters(final MicroService microService) {
		ArrayList<String> list = new ArrayList<String>();

		String URL = microService.getServiceUrl() + "/datacenters";

		StringBuffer sb = new StringBuffer();

		try {

			URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			// System.out.println("Output from Server .... \n");
			String output = null;
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}

			conn.disconnect();

			JSONParser parser = new JSONParser();

			Object obj = parser.parse(sb.toString());
			JSONArray array = (JSONArray) obj;

			for (int i = 0; i < array.size(); ++i) {
				list.add(array.get(i).toString());
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<DataSourceVo> searchDataSources(DataCenterVo datacenter) {
		ArrayList<DataSourceVo> list = new ArrayList<DataSourceVo>();

		MicroService microService = datacenter.getMicroService();
		String URL = null;

		if (microService != null) {
			URL = microService.getServiceUrl() + "/datasources";
		}

		try {

			URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			JSONObject obj = new JSONObject();
			obj.put("name", datacenter.getName());
			obj.put("directory", datacenter.getDirectory());
			obj.put("fileName", datacenter.getFileName());

			StringWriter out = new StringWriter();
			obj.writeJSONString(out);
			String data = out.toString();

			System.out.println(data);

			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

			writer.write(data);
			writer.flush();
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			writer.close();
			reader.close();

			JSONParser parser = new JSONParser();

			Object outobj = parser.parse(sb.toString());
			JSONArray array = (JSONArray) outobj;

			for (int i = 0; i < array.size(); ++i) {
				JSONObject obj2 = (JSONObject) array.get(i);

				DataSourceVo ds = new DataSourceVo();
				String fileName = obj2.get("fileName").toString();
				ds.setFileName(fileName);
				ds.setDirectory(obj2.get("directory").toString());
				if (datacenter.getDirectory() == null || datacenter.getDirectory().trim().length() == 0) {
					ds.setPath("/" + fileName);
				} else {
					String dirPath = datacenter.getDirectory().trim();
					if (!"/".equals(dirPath.charAt(0))) {
						dirPath = "/" + dirPath;
					}

					if (!"/".equals(dirPath.charAt(dirPath.length() - 1))) {
						dirPath = dirPath + "/";
					}

					ds.setPath(dirPath + fileName);
				}
				// ds.setStatus(obj2.get("status").toString());
				ds.setMicroService(datacenter.getMicroService());
				ds.setDataCenter(datacenter.getName());
				ds.setDirectoryName(datacenter.getDirectory());
				DataSource dataSource = getDataSource(ds);

				if (dataSource != null) {
					ds.setDataSource(dataSource);
					ds.setMode(Constants.MODE_EDIT);

				} else {
					ds.pupulateDataSource();
				}

				list.add(ds);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return list;
	}
	
	@Override
	public List<Computation> searchComputations(Computation computation) {
		// TODO Auto-generated method stub
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		return genomicsDao.searchComputations(computation, studyId);
	}

	public List<DataSourceType> listDataSourceTypes() {
		return genomicsDao.listDataSourceTypes();
	}

	public DataSource getDataSource(DataSourceVo dataSourceVo) {
		// TODO Auto-generated method stub
		return genomicsDao.getDataSource(dataSourceVo);
	}

//	public String executeDataSourceUpload(DataSource dataSource) {
//
//		MicroService microService = dataSource.getMicroService();
//
//		String URL = microService.getServiceUrl() + "/executeProcess";
//
//		String processUID = null;
//		try {
//			URL url = new URL(URL);
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestMethod("POST");
//			conn.setRequestProperty("Accept", "application/json");
//			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//			conn.setDoOutput(true);
//			conn.setDoInput(true);
//			
//						
//			if (conn.getResponseCode() != 200) {
//				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
//			}
//			
//			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
//
//			String output = null;
//			while ((output = br.readLine()) != null) {
//				log.info("Process UID -- " + output);
//				processUID = output;
//			}
//			conn.disconnect();
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		return processUID;
//	}
	
	public String executeDataSourceUpload(DataSourceVo dataSource) {

		MicroService microService = dataSource.getMicroService();

		String URL = microService.getServiceUrl() + "/executeProcess";

		String processUID = null;
		try {
			URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
						
			JSONObject obj = new JSONObject();
			obj.put("directoryName", dataSource.getDirectoryName());
			obj.put("dataCenterName", dataSource.getDataCenter());
			obj.put("path", dataSource.getPath());
			obj.put("fileName", dataSource.getFileName());
			obj.put("status", dataSource.getStatus());

			StringWriter out = new StringWriter();
			obj.writeJSONString(out);
			String data = out.toString();

			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(data);
			writer.flush();
			
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = null;
			while ((output = br.readLine()) != null) {
				log.info("Process UID -- " + output);
				processUID = output;
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return processUID;
	}
	
	
	public String executeDataSourceUpload(DataCenterVo dataCenter) {

		MicroService microService = dataCenter.getMicroService();

		String URL = microService.getServiceUrl() + "/executePlinkProcess";

		String processUID = null;
		try {
			URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
						
			JSONObject obj = new JSONObject();
			obj.put("directory", dataCenter.getDirectory());
			obj.put("name", dataCenter.getName());
			obj.put("status", dataCenter.getStatus());

			StringWriter out = new StringWriter();
			obj.writeJSONString(out);
			String data = out.toString();

			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(data);
			writer.flush();
			
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = null;
			while ((output = br.readLine()) != null) {
				log.info("Process UID -- " + output);
				processUID = output;
			}
			conn.disconnect();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return processUID;
	}



	public void updateDataSourceStatus(final String processUID, DataSource dataSource) {

		MicroService microService = dataSource.getMicroService();

		String URL = microService.getServiceUrl() + "/processStatus";

		String result = "Running";
		
		String initStatus = dataSource.getStatus();

		while (!("Completed".equalsIgnoreCase(result) || "Error".equalsIgnoreCase(result))) {

			try {
				URL url = new URL(URL);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Accept", "application/json");
				conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
				conn.setDoOutput(true);
				conn.setDoInput(true);
				
				OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

				writer.write(processUID);
				writer.flush();

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

				String output = null;
				while ((output = br.readLine()) != null) {
					result = output;
				}
				conn.disconnect();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			log.info("Process Status -- " + result);
			
			log.info("DataSoure Status -- " + dataSource.getStatus());
			
			if("Deleting".equalsIgnoreCase(initStatus) && "completed".equalsIgnoreCase(result)){
				dataSource.setStatus("Deleted");
			}else{
				dataSource.setStatus(result);				
			}
			
			saveOrUpdate(dataSource);

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void uploadComputaion(Computation computation){
		MicroService microService = computation.getMicroService();

		String URL = microService.getServiceUrl() + "/uploadFile";

		String processUID = null;
		try {
			URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//			conn.setRequestProperty("Content-Transfer-Encoding", "base64");
			conn.setDoOutput(true);
			conn.setDoInput(true);
						
			JSONObject obj = new JSONObject();
			obj.put("programId", computation.getProgramId());
			obj.put("name", computation.getName());
			
			Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			
			byte[] pdata = iArkCommonService.retriveArkFileAttachmentByteArray(studyId,computation.getId().toString(),Constants.ARK_GENOMICS_COMPUTATION_DIR,computation.getProgramId(),computation.getChecksum());
			
			System.out.println(Base64.getEncoder().encode(pdata));
			
			obj.put("program", "program");
			

			StringWriter out = new StringWriter();
			obj.writeJSONString(out);
			String data = out.toString();

			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(data);
			writer.flush();
			
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = null;
			while ((output = br.readLine()) != null) {
				log.info("message -- " + output);
//				processUID = output;
			}
			conn.disconnect();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch(ArkSystemException e){
			e.printStackTrace();
		}
	}
	
	
	public void uploadComputation(Computation computation){
		MicroService microService = computation.getMicroService();

		String URL = microService.getServiceUrl() + "/upload";
		
		try{
			HttpClient httpclient = new DefaultHttpClient();
		    httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		    
		    HttpPost httppost = new HttpPost(URL);
		    
		    Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			
			//byte[] pdata = iArkCommonService.retriveArkFileAttachmentByteArray(studyId,computation.getId().toString(),Constants.ARK_GENOMICS_COMPUTATION_DIR,computation.getProgramId(),computation.getChecksum());
		    
		    String directory = iArkCommonService.getArkFileDirName(studyId, computation.getId().toString(), Constants.ARK_GENOMICS_COMPUTATION_DIR);
			String location = directory + File.separator + computation.getProgramId();
			File file = new File(location);
		    

		    MultipartEntity mpEntity = new MultipartEntity();
		    ContentBody cbFile = new FileBody(file, "multipart/form-data");
		    StringBody contentString = new StringBody(computation.getProgramId() + "");
		    
		    mpEntity.addPart("file", cbFile);
		    mpEntity.addPart("name", contentString);

		    
//		    StringEntity nameEntity = new StringEntity("name","test");

//		    httppost.setEntity(nameEntity);
		    httppost.setEntity(mpEntity);
		    System.out.println("executing request " + httppost.getRequestLine());
		    HttpResponse response = httpclient.execute(httppost);
		    HttpEntity resEntity = response.getEntity();

		    System.out.println(response.getStatusLine());
		    if (resEntity != null) {
		      System.out.println(EntityUtils.toString(resEntity));
		    }
		    if (resEntity != null) {
		      resEntity.consumeContent();
		    }

		    httpclient.getConnectionManager().shutdown();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void compileComputation(Computation computation){
		MicroService microService = computation.getMicroService();

		String URL = microService.getServiceUrl() + "/compile";
		
		try{
			HttpClient httpclient = new DefaultHttpClient();
		    httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		    
		    HttpPost httppost = new HttpPost(URL);
		    

		    

		    MultipartEntity mpEntity = new MultipartEntity();
//		    ContentBody cbFile = new FileBody(file, "multipart/form-data");
		    StringBody contentString = new StringBody(computation.getProgramId() + "");
		    
//		    mpEntity.addPart("file", cbFile);
		    mpEntity.addPart("name", contentString);

		    
//		    StringEntity nameEntity = new StringEntity("name","test");

//		    httppost.setEntity(nameEntity);
		    httppost.setEntity(mpEntity);
		    System.out.println("executing request " + httppost.getRequestLine());
		    HttpResponse response = httpclient.execute(httppost);
		    HttpEntity resEntity = response.getEntity();

		    System.out.println(response.getStatusLine());
		    if (resEntity != null) {
		      System.out.println(EntityUtils.toString(resEntity));
		    }
		    if (resEntity != null) {
		      resEntity.consumeContent();
		    }

		    httpclient.getConnectionManager().shutdown();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
//	 private String convertFileToString(File file) throws IOException{
//	        byte[] bytes = Files.readAllBytes(file.toPath());   
//	        return new String(Base64.getEncoder().encode(bytes));
//	    }
//	 
//	 public static void main(String[] args){
//		String a ="ABCDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD";
//		System.out.println(Base64.getEncoder().encode(a.getBytes()));
//	}

}
