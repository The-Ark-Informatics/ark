package au.org.theark.genomics.service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.HttpMethod;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.WordUtils;
import org.apache.shiro.SecurityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.ArkFileNotFoundException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.spark.entity.Analysis;
import au.org.theark.core.model.spark.entity.Computation;
import au.org.theark.core.model.spark.entity.DataSource;
import au.org.theark.core.model.spark.entity.DataSourceType;
import au.org.theark.core.model.spark.entity.MicroService;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.webservice.ArkHTTPService;
import au.org.theark.core.webservice.ArkMultipartService;
import au.org.theark.genomics.model.dao.IGenomicsDao;
import au.org.theark.genomics.model.vo.AnalysisJobVo;
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

	//@Value("${service.auth.header}")
	private String authHeader;

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
	public void saveOrUpdate(Analysis analysis) {
		genomicsDao.saveOrUpdate(analysis);
	}

	@Override
	public void save(Analysis analysis, byte[] attachement) throws ArkSystemException {
		Long computationId = genomicsDao.saveOrUpdate(analysis);

		if (attachement != null) {
			Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			// String subjectUID = correspondence.getLss().getSubjectUID();
			String fileName = analysis.getScriptName();

			// Generate unique file id for given file name
			String fileId = iArkCommonService.generateArkFileId(fileName);

			// Set unique subject file id
			analysis.setScriptId(fileId);

			// Save the attachment to directory configured in
			// application.properties {@code fileAttachmentDir}
			iArkCommonService.saveArkFileAttachment(studyId, computationId.toString(), Constants.ARK_GENOMICS_ANALYSIS_DIR, fileName, attachement, fileId);

			// Remove the attachment
			genomicsDao.saveOrUpdate(analysis);
		}

	}

	@Override
	public void update(Analysis analysis, byte[] attachement, String checksum) throws ArkSystemException, ArkFileNotFoundException {
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		// String subjectUID = correspondence.getLss().getSubjectUID();
		String fileName = analysis.getScriptName();
		String prevChecksum = analysis.getChecksum();

		String fileId = null;
		if (attachement != null) {

			if (analysis.getScriptId() != null) {

				// Get existing file Id
				fileId = analysis.getScriptId();

				// Delete existing attachment
				iArkCommonService.deleteArkFileAttachment(studyId, analysis.getId().toString(), fileId, Constants.ARK_GENOMICS_ANALYSIS_DIR, prevChecksum);

				// Generate unique file id for given file name
				fileId = iArkCommonService.generateArkFileId(fileName);

				// Set unique subject file id
				analysis.setScriptId(fileId);

				// Save the attachment to directory configured in
				// application.properties {@code fileAttachmentDir}
				iArkCommonService.saveArkFileAttachment(studyId, analysis.getId().toString(), Constants.ARK_GENOMICS_ANALYSIS_DIR, fileName, attachement, fileId);
			} else {
				// Generate unique file id for given file name
				fileId = iArkCommonService.generateArkFileId(fileName);

				// Set unique subject file id
				analysis.setScriptId(fileId);

				// Save the attachment to directory configured in
				// application.properties {@code fileAttachmentDir}
				iArkCommonService.saveArkFileAttachment(studyId, analysis.getId().toString(), Constants.ARK_GENOMICS_ANALYSIS_DIR, fileName, attachement, fileId);
			}
			// Set new file checksum
			analysis.setChecksum(checksum);
		} else {
			if (analysis.getScriptId() != null) {
				// Get existing file Id
				fileId = analysis.getScriptId();

				// Delete existing attachment
				iArkCommonService.deleteArkFileAttachment(studyId, analysis.getId().toString(), fileId, Constants.ARK_GENOMICS_ANALYSIS_DIR, prevChecksum);

				// remove existing attachment file id and checksum
				analysis.setScriptId(null);
				analysis.setChecksum(null);
			}
		}
		// Remove the attachment
		genomicsDao.saveOrUpdate(analysis);

	}

	public void saveOrUpdate(Computation computation) {
		genomicsDao.saveOrUpdate(computation);
	}

	@Override
	public void save(Computation computation, byte[] attachement) throws ArkSystemException {
		Long computationId = genomicsDao.saveOrUpdate(computation);

		if (attachement != null) {
			Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			// String subjectUID = correspondence.getLss().getSubjectUID();
			String fileName = computation.getProgramName();
			// byte[] payload = correspondence.getAttachmentPayload();

			// Generate unique file id for given file name
			String fileId = iArkCommonService.generateArkFileId(fileName);

			// Set unique subject file id
			computation.setProgramId(fileId);

			// Save the attachment to directory configured in
			// application.properties {@code fileAttachmentDir}
			iArkCommonService.saveArkFileAttachment(studyId, computationId.toString(), Constants.ARK_GENOMICS_COMPUTATION_DIR, fileName, attachement, fileId);

			// Remove the attachment
			genomicsDao.saveOrUpdate(computation);
		}

	}

	@Override
	public void update(Computation computation, byte[] attachement, String checksum) throws ArkSystemException, ArkFileNotFoundException {
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		// String subjectUID = correspondence.getLss().getSubjectUID();
		String fileName = computation.getProgramName();
		String prevChecksum = computation.getChecksum();

		String fileId = null;
		if (attachement != null) {
			
			System.out.println("attachment is not null");

			if (computation.getProgramId() != null) {
				
				System.out.println("attachment is not null program id is not null");

				// Get existing file Id
				fileId = computation.getProgramId();

				// Delete existing attachment
				iArkCommonService.deleteArkFileAttachment(studyId, computation.getId().toString(), fileId, Constants.ARK_GENOMICS_COMPUTATION_DIR, prevChecksum);

				// Generate unique file id for given file name
				fileId = iArkCommonService.generateArkFileId(fileName);

				// Set unique subject file id
				computation.setProgramId(fileId);

				// Save the attachment to directory configured in
				// application.properties {@code fileAttachmentDir}
				iArkCommonService.saveArkFileAttachment(studyId, computation.getId().toString(), Constants.ARK_GENOMICS_COMPUTATION_DIR, fileName, attachement, fileId);
			} else {
				
				System.out.println("attachment is not null program id is null");
				
				// Generate unique file id for given file name
				fileId = iArkCommonService.generateArkFileId(fileName);

				// Set unique subject file id
				computation.setProgramId(fileId);

				// Save the attachment to directory configured in
				// application.properties {@code fileAttachmentDir}
				iArkCommonService.saveArkFileAttachment(studyId, computation.getId().toString(), Constants.ARK_GENOMICS_COMPUTATION_DIR, fileName, attachement, fileId);
			}
			// Set new file checksum
			computation.setChecksum(checksum);
		} else {
			System.out.println("attachment is null");
			if (computation.getProgramId() != null && computation.getProgramName() == null) {
				
				System.out.println("attachment is null && program id is not null");
				
				// Get existing file Id
				fileId = computation.getProgramId();

				// Delete existing attachment
				iArkCommonService.deleteArkFileAttachment(studyId, computation.getId().toString(), fileId, Constants.ARK_GENOMICS_COMPUTATION_DIR, prevChecksum);

				// remove existing attachment file id and checksum
				computation.setProgramId(null);
				computation.setChecksum(null);
			}
		}
		// Remove the attachment
		genomicsDao.saveOrUpdate(computation);

	}

	@Override
	public void delete(Computation computation) throws ArkSystemException, ArkFileNotFoundException {

		Long studyId = computation.getMicroService().getStudyId();
		String computationId = computation.getId().toString();
		String fileId = computation.getProgramId();
		String checksum = computation.getChecksum();

		if (fileId != null && iArkCommonService.deleteArkFileAttachment(studyId, computationId, fileId, "computation", checksum)) {
			genomicsDao.delete(computation);
		} else {
			genomicsDao.delete(computation);
		}

	}

	public void delete(Analysis analysis) {
		genomicsDao.delete(analysis);
	}

	public List<MicroService> searchMicroService(MicroService microService) {
		List<MicroService> serviceList = genomicsDao.searchMicroService(microService);

		serviceList.forEach(service -> service.setStatus(checkServiceStatus(service)));

		if (microService.getStatus() != null) {
			serviceList.removeIf(service -> !(microService.getStatus().equalsIgnoreCase(service.getStatus().split("[(]")[0].trim())));
		}

		return serviceList;
	}

	public String checkServiceStatus(final MicroService microService) {
		String status = Constants.STATUS_OFFLINE;
		try {

			String URL = microService.getServiceUrl() + "/status";
			ArkHTTPService httpService = new ArkHTTPService(URL, this.authHeader);
			int responseCode = httpService.getResponseCode();

			if (responseCode != HttpURLConnection.HTTP_OK) {
				status = status + " (" + responseCode + ")";
			} else {
				status = Constants.STATUS_ONLINE;
			}

		} catch (MalformedURLException e) {
			status = Constants.STATUS_OFFLINE + " (404)";
			log.error("Invalid URL " + microService.getName() + " has generated " + e.getMessage());
		} catch (IOException e) {
			status = Constants.STATUS_OFFLINE + " (404)";
			log.error("Invalid URL " + microService.getName() + " has generated " + e.getMessage());
		} catch (Exception e) {
			status = Constants.STATUS_OFFLINE + " (404)";
			log.error("Invalid URL " + microService.getName() + " has generated " + e.getMessage());
		}
		return status;
	}

	public List<String> searchDataCenters(final MicroService microService) {
		ArrayList<String> list = new ArrayList<String>();

		String URL = microService.getServiceUrl() + "/datacenters";

		StringBuffer sb = new StringBuffer();

		try {

			ArkHTTPService httpService = new ArkHTTPService(URL, this.authHeader);

			List<String> data = httpService.finish();

			data.forEach(s -> sb.append(s));

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
		} catch (Exception e) {
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

			ArkHTTPService httpService = new ArkHTTPService(URL, "UTF-8", this.authHeader, HttpMethod.POST);

			JSONObject obj = new JSONObject();
			obj.put("name", datacenter.getName());
			obj.put("directory", datacenter.getDirectory());
			obj.put("fileName", datacenter.getFileName());
			obj.put("microserviceId", datacenter.getMicroService().getId().longValue());

			httpService.addPostParameters(obj);

			StringBuffer sb = new StringBuffer();
			List<String> data = httpService.finish();

			data.forEach(s -> sb.append(s));

			JSONParser parser = new JSONParser();

			Object outobj = parser.parse(sb.toString());
			JSONArray array = (JSONArray) outobj;

			for (int i = 0; i < array.size(); ++i) {
				JSONObject obj2 = (JSONObject) array.get(i);

//				DataSourceVo ds = new DataSourceVo();
//				String fileName = obj2.get("fileName").toString();
				String fileName = obj2.get("fileName").toString();
				if(fileName.startsWith(".")){
					continue;
				}
				DataSourceVo ds = new DataSourceVo();
				ds.setFileName(fileName);
				ds.setDirectory(obj2.get("directory").toString());
				if (datacenter.getDirectory() == null || datacenter.getDirectory().trim().length() == 0) {
					ds.setPath("/" + fileName);
				} else {
					String dirPath = datacenter.getDirectory().trim();

					// System.out.println("--------------------------="+dirPath);
					// System.out.println("--------------------------="+dirPath.charAt(0));
					// System.out.println("--------------------------="+(dirPath.length()
					// - 1));

					if ("/".equals(dirPath.charAt(0))) {
						dirPath = "/" + dirPath;
					}

					if (!"/".equals(dirPath.charAt(dirPath.length() - 1))) {
						dirPath = dirPath + "/";
					}

					ds.setPath(dirPath + fileName);
				}
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

	public List<DataSource> searchDataSources(MicroService microService) {
		return genomicsDao.searchDataSources(microService);
	}

	public List<Computation> searchComputation(MicroService microService) {
		return genomicsDao.searchComputation(microService);
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
		List<DataSource> list = genomicsDao.getDataSources(dataSourceVo);
		return list.size() > 0 ? list.get(0) : null;
	}
	
	public List<DataSource> searchDataSources(DataSourceVo dataSourceVo){
		return genomicsDao.getDataSources(dataSourceVo);
	}

	public String executeDataSourceUpload(DataSourceVo dataSource) {

		MicroService microService = dataSource.getMicroService();

		String URL = microService.getServiceUrl() + "/executeProcess";

		String processUID = null;
		try {

			ArkHTTPService httpService = new ArkHTTPService(URL, "UTF-8", this.authHeader, HttpMethod.POST);

			JSONObject obj = new JSONObject();
			obj.put("directoryName", dataSource.getDirectoryName());
			obj.put("dataCenterName", dataSource.getDataCenter());
			obj.put("path", dataSource.getPath());
			obj.put("fileName", dataSource.getFileName());
			obj.put("status", dataSource.getStatus());

			httpService.addPostParameters(obj);
			List<String> data = httpService.finish();
			processUID = data.stream().findFirst().get();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return processUID;
	}

	public String executeDataSourceUpload(DataCenterVo dataCenter, String initStatus) {

		MicroService microService = dataCenter.getMicroService();

		String URL = microService.getServiceUrl() + "/executePlinkProcess";

		String processUID = null;
		try {
			ArkHTTPService httpService = new ArkHTTPService(URL, "UTF-8", this.authHeader, HttpMethod.POST);

			JSONObject obj = new JSONObject();
			obj.put("directory", dataCenter.getDirectory());
			obj.put("name", dataCenter.getName());
			obj.put("status", initStatus);
			obj.put("microserviceId", dataCenter.getMicroService().getId().longValue());
			httpService.addPostParameters(obj);
			List<String> data = httpService.finish();
			processUID = data.stream().findFirst().get();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return processUID;
	}

	public String executeAnalysis(Analysis analysis) {

		MicroService microService = analysis.getMicroService();

		String URL = microService.getServiceUrl() + "/executeAnalysis";

		String processUID = null;
		try {
			ArkHTTPService httpService = new ArkHTTPService(URL, "UTF-8", this.authHeader, HttpMethod.POST);

			JSONObject obj = new JSONObject();
			obj.put("programId", analysis.getComputation().getProgramId().split("[.]")[0]);
			obj.put("programName", analysis.getComputation().getProgramName().split("[.]")[0]);
			obj.put("analysisId", analysis.getId());
			obj.put("sourcePath", analysis.getDataSource().getPath());
			obj.put("sourceDataCenter", analysis.getDataSource().getDataCenter());
			obj.put("sourceDir", analysis.getDataSource().getDirectory());
			obj.put("parameters", analysis.getParameters());
			obj.put("result", analysis.getResult());

			httpService.addPostParameters(obj);
			List<String> data = httpService.finish();
			processUID = data.stream().findFirst().get();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return processUID;
	}

	public byte[] getAnalysisResult(Analysis analysis) throws Exception {
		byte[] result = null;
		MicroService microService = analysis.getMicroService();
		String URL = microService.getServiceUrl() + "/getAnalysisResult";
		StringBuffer sb = new StringBuffer();

		ArkHTTPService httpService = new ArkHTTPService(URL, "UTF-8", this.authHeader, HttpMethod.POST);

		JSONObject obj = new JSONObject();
		obj.put("programId", analysis.getComputation().getProgramId().split("[.]")[0]);
		obj.put("programName", analysis.getComputation().getProgramName().split("[.]")[0]);
		obj.put("analysisId", analysis.getId());
		obj.put("parameters", analysis.getParameters());
		obj.put("result", analysis.getResult());
		obj.put("jobId", analysis.getJobId());

		httpService.addPostParameters(obj);
		List<String> data = httpService.finish();
		for (String s : data) {
			if (s.contains("\n")) {
				sb.append(s);
			} else {
				sb.append(s + "\n");
			}
		}
		result = sb.toString().getBytes();
		return result;
	}

	public void updateDataSourceStatus(final String processUID, DataSource dataSource, List<DataSource> dataSourceList, String initStatus) {

		MicroService microService = dataSource.getMicroService();

		String URL = microService.getServiceUrl() + "/processStatus";

		String result = Constants.STATUS_PROCESSING;

		while (!(Constants.STATUS_PROCESSED.equalsIgnoreCase(result) || "Error".equalsIgnoreCase(result))) {

			try {
				ArkHTTPService httpService = new ArkHTTPService(URL, "UTF-8", this.authHeader, HttpMethod.POST);

				httpService.addPostParameters(processUID);

				List<String> data = httpService.finish();
				result = data.stream().findFirst().get();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			log.info("Process Status -- " + result);

			log.info("DataSoure Status -- " + dataSource.getStatus());

			if (Constants.STATUS_PROCESSED.equalsIgnoreCase(initStatus) && Constants.STATUS_PROCESSED.equalsIgnoreCase(result)) {
				dataSource.setStatus(Constants.STATUS_UNPROCESSED);
			} else {
				dataSource.setStatus(result);
			}

			saveOrUpdate(dataSource);

			if (Constants.STATUS_UNPROCESSED.equalsIgnoreCase(initStatus) && Constants.STATUS_PROCESSED.equalsIgnoreCase(dataSource.getStatus())) {
				for (DataSource ds : dataSourceList) {
					updateDataSourceStatus(ds, Constants.STATUS_READY);
				}
			} else {
				for (DataSource ds : dataSourceList) {
					updateDataSourceStatus(ds, Constants.STATUS_NOT_READY);
				}
			}

			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void updateDataSourceStatus(DataSource dataSource, String status) {
		dataSource.setStatus(status);
		saveOrUpdate(dataSource);
	}

	public void updateAnalysisStatus(final String processUID, Analysis analysis) {
		MicroService microService = analysis.getMicroService();

		String URL = microService.getServiceUrl() + "/processStatus";

		String result = "Running";

		String initStatus = analysis.getStatus();

		while (!(Constants.STATUS_PROCESSED.equalsIgnoreCase(result) || "Error".equalsIgnoreCase(result))) {

			try {
				ArkHTTPService httpService = new ArkHTTPService(URL, "UTF-8", this.authHeader, HttpMethod.POST);
				httpService.addPostParameters(processUID);
				List<String> data = httpService.finish();
				result = data.stream().findFirst().get();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			log.info("Process Status -- " + result);

			log.info("Analysis Status -- " + analysis.getStatus());

			analysis.setStatus(result);

			saveOrUpdate(analysis);

			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateQueueStatus(Analysis analysis) {
		MicroService microService = analysis.getMicroService();

		String URL = microService.getServiceUrl() + "/queueStatus";

		String result = "Running";
		try {

			while (!(Constants.STATUS_COMPLETED.equalsIgnoreCase(result) || Constants.STATUS_FAILED.equalsIgnoreCase(result))) {

				ArkHTTPService httpService = new ArkHTTPService(URL, "UTF-8", this.authHeader, HttpMethod.POST);
				httpService.addPostParameters(analysis.getJobId());
				List<String> data = httpService.finish();
				result = data.stream().findFirst().get();

				// } catch (MalformedURLException e) {
				// e.printStackTrace();
				// } catch (IOException e) {
				// e.printStackTrace();
				// }

				log.info("Process Status -- " + result);

				log.info("DataSoure Status -- " + analysis.getStatus());

				analysis.setStatus(WordUtils.capitalize(result.toLowerCase()));

				saveOrUpdate(analysis);

				// try {
				Thread.sleep(30000);

			}
		} catch (Exception e) {
			e.printStackTrace();
			result = Constants.STATUS_FAILED;
			analysis.setStatus(result);
			saveOrUpdate(analysis);

		}
	}

	public void uploadComputation(Computation computation) throws Exception {
		MicroService microService = computation.getMicroService();
		String URL = microService.getServiceUrl() + "/upload";

		ArkMultipartService multipartService = new ArkMultipartService(URL, "UTF-8", this.authHeader, HttpMethod.POST);

		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		String directory = iArkCommonService.getArkFileDirName(studyId, computation.getId().toString(), Constants.ARK_GENOMICS_COMPUTATION_DIR);
		String location = directory + File.separator + computation.getProgramId();
		File file = new File(location);

		multipartService.addFormField("name", computation.getProgramId() + "");
		multipartService.addFilePart("file", file);

		List<String> outputList = multipartService.finish();
		if (outputList.size() > 0) {
			if (outputList.get(0).contains(Constants.STATUS_UPLOADED)) {
				computation.setStatus(Constants.STATUS_UPLOADED);
			} else {
				computation.setStatus(Constants.STATUS_UPLOAD_FAILED);
			}
		}
		genomicsDao.saveOrUpdate(computation);

	}

	public String compileComputation(Computation computation) throws Exception {
		MicroService microService = computation.getMicroService();

		String URL = microService.getServiceUrl() + "/compile";

		String status = null;

		String processUID = null;

		ArkHTTPService httpService = new ArkHTTPService(URL, "UTF-8", this.authHeader, HttpMethod.POST);

		JSONObject obj = new JSONObject();

		String programId[] = computation.getProgramId().split("[.]");
		String programName[] = computation.getProgramName().split("[.]");
		obj.put("programId", programId[0]);
		obj.put("name", computation.getName());
		obj.put("program", programName[0]);

		httpService.addPostParameters(obj);
		List<String> data = httpService.finish();
		processUID = data.stream().findFirst().get();

		// if (Constants.STATUS_COMPILED.equalsIgnoreCase(status)) {
		// computation.setStatus(Constants.STATUS_COMPILED);
		// } else {
		// computation.setStatus(Constants.STATUS_CPMPILE_FAILED);
		// }

		computation.setStatus(Constants.STATUS_SUBMITTED);

		genomicsDao.saveOrUpdate(computation);

		return processUID;
	}

	public void updateCompilationStatus(String processUID, Computation computation) {
		MicroService microService = computation.getMicroService();

		String URL = microService.getServiceUrl() + "/processStatus";

		String result = "Running";

		String initStatus = computation.getStatus();

		while (!(Constants.STATUS_PROCESSED.equalsIgnoreCase(result) || "Error".equalsIgnoreCase(result))) {

			try {
				ArkHTTPService httpService = new ArkHTTPService(URL, "UTF-8", this.authHeader, HttpMethod.POST);
				httpService.addPostParameters(processUID);
				List<String> data = httpService.finish();
				result = data.stream().findFirst().get();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			log.info("Process Status -- " + result);

			log.info("DataSoure Status -- " + computation.getStatus());

			computation.setStatus(result);

			saveOrUpdate(computation);

			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<Analysis> searchAnalysis(Analysis analysis, Long studyId) {
		// TODO Auto-generated method stub
		return genomicsDao.searchAnalysis(analysis, studyId);
	}

	public String submitJob(Analysis analysis) {

		MicroService microService = analysis.getMicroService();

		String URL = microService.getServiceUrl() + "/jobSubmission";

		String status = null;
		try {

			AnalysisJobVo jobVo = new AnalysisJobVo();
			jobVo.setProgramId(analysis.getComputation().getProgramId().split("[.]")[0]);
			jobVo.setProgramName(analysis.getComputation().getProgramName().split("[.]")[0]);
			jobVo.setAnalysisId(analysis.getId().toString());
			if (analysis.getDataSource() != null) {
				jobVo.setSourcePath(analysis.getDataSource().getPath());
				jobVo.setSourceDataCenter(analysis.getDataSource().getDataCenter());
				jobVo.setSourceDir(analysis.getDataSource().getDirectory().toString());
			} else {
				jobVo.setSourcePath("");
				jobVo.setSourceDataCenter("");
				jobVo.setSourceDir("");
			}
			jobVo.setParameters(analysis.getParameters());
			jobVo.setResult(analysis.getResult());
			jobVo.setScriptName(analysis.getScriptName());

			StringWriter sw = new StringWriter();
			JAXBContext jaxbContext = JAXBContext.newInstance(AnalysisJobVo.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.marshal(jobVo, sw);
			String xmlString = sw.toString();

			ArkMultipartService multipartService = new ArkMultipartService(URL, "UTF-8", this.authHeader, HttpMethod.POST);

			Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			String directory = iArkCommonService.getArkFileDirName(studyId, analysis.getId().toString(), Constants.ARK_GENOMICS_ANALYSIS_DIR);
			String location = directory + File.separator + analysis.getScriptId();
			File file = new File(location);

			multipartService.addFormField("analysis", xmlString);
			multipartService.addFilePart("file", file);

			List<String> outputList = multipartService.finish();
			if (outputList.size() > 0) {
				status = outputList.get(0);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (ArkSystemException e) {
			e.printStackTrace();
		}

		return status;
	}

	public String submitToQueue(Analysis analysis) throws Exception {
		MicroService microService = analysis.getMicroService();

		String URL = microService.getServiceUrl() + "/jobQueue";

		String jobId = null;

		ArkHTTPService httpService = new ArkHTTPService(URL, "UTF-8", this.authHeader, HttpMethod.POST);

		JSONObject obj = new JSONObject();
		obj.put("programId", analysis.getComputation().getProgramId().split("[.]")[0]);
		obj.put("programName", analysis.getComputation().getProgramName().split("[.]")[0]);
		obj.put("analysisId", analysis.getId());

		if (analysis.getDataSource() != null) {
			obj.put("sourcePath", analysis.getDataSource().getPath());
			obj.put("sourceDataCenter", analysis.getDataSource().getDataCenter());
			obj.put("sourceDir", analysis.getDataSource().getDirectory());
		} else {
			obj.put("sourcePath", "");
			obj.put("sourceDataCenter", "");
			obj.put("sourceDir", "");
		}
		obj.put("parameters", analysis.getParameters());
		obj.put("result", analysis.getResult());
		obj.put("scriptName", analysis.getScriptName());

		System.out.println("----------------------------------" + analysis.getScriptName());

		httpService.addPostParameters(obj);
		List<String> data = httpService.finish();
		jobId = data.stream().findFirst().get();

		analysis.setJobId(jobId);
		analysis.setStatus(Constants.STATUS_SUBMITTED);
		genomicsDao.saveOrUpdate(analysis);

		return jobId;
	}

	public String executeQueryAnalysis(DataCenterVo dataCenter) {
		MicroService microService = dataCenter.getMicroService();

		String URL = microService.getServiceUrl() + "/queryResult";

		System.out.println(" -------------------- " + URL + " ----------------------- ");

		String result = null;
		try {
			ArkHTTPService httpService = new ArkHTTPService(URL, "UTF-8", this.authHeader, HttpMethod.POST);

			JSONObject obj = new JSONObject();
			obj.put("directory", dataCenter.getDirectory());
			obj.put("name", dataCenter.getName());
			obj.put("individualId", dataCenter.getIndividualId());
			obj.put("microserviceId", dataCenter.getMicroService().getId().longValue());
			httpService.addPostParameters(obj);
			List<String> data = httpService.finish();
			result = data.stream().findFirst().get();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	public byte[] getQueryResult(DataCenterVo dataCenter) {
		MicroService microService = dataCenter.getMicroService();

		String URL = microService.getServiceUrl() + "/queryOutput";

		System.out.println(" -------------------- " + URL + " ----------------------- ");

		byte[] result = null;
		try {
			ArkHTTPService httpService = new ArkHTTPService(URL, "UTF-8", this.authHeader, HttpMethod.POST);

			JSONObject obj = new JSONObject();
			obj.put("directory", dataCenter.getDirectory());
			obj.put("name", dataCenter.getName());
			obj.put("individualId", dataCenter.getIndividualId());
			obj.put("microserviceId", dataCenter.getMicroService().getId().longValue());
			httpService.addPostParameters(obj);
			List<String> data = httpService.finish();
			result = data.stream().findFirst().get().getBytes();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	public int getAnalysisCount(long computationId) {
		return genomicsDao.getAnalysisCount(computationId);
	}
	
	public int getDataSourceCount(long dataSourceId){
		return genomicsDao.getDataSourceCount(dataSourceId);
	}
	
	public void refreshDataSource(DataSource dataSource){
		genomicsDao.refreshDataSource(dataSource);
	}
		
}
