package au.org.theark.genomics.util;

public final class Constants {

	public static final String MICRO_SERVICE = "MICRO_SERVICE";

	public static final String DATA_CENTER = "DATA_CENTER";
	
	public static final String COMPUTATION = "COMPUTATION";

	public static final String ANALYSIS = "ANALYSIS";


	public static final String GENO_SUBMENU = "genoSubMenus";

	public static final String GENOMIC_SERVICE = "genomicService";

	public static final String GENOMICS_DAO = "genomicsDao";

	public static final String SEARCH_FORM = "searchForm";
	public static final String DATA_SOURCE_FORM = "dataSourceForm";
	public static final String QUERY_FORM = "queryForm";

	public static final String MICRO_SERVICE_ID = "microService.id";
	public static final String MICRO_SERVICE_NAME = "microService.name";
	public static final String MICRO_SERVICE_DESCRIPTION = "microService.description";
	public static final String MICRO_SERVICE_URL = "microService.serviceUrl";
	public static final String MICRO_SERVICE_STATUS = "microService.status";

	public static final String DATA_CENTER_MICRO_SERVICE="microService";
	public static final String DATA_CENTER_NAME="name";
	public static final String DATA_CENTER_FILE_NAME="fileName";
	public static final String DATA_CENTER_DIRECTORY="directory";
	
	public static final String DATA_SOURCE_VO_FILE_NAME = "dataSourceVo.fileName";
	public static final String DATA_SOURCE_VO_DIRECTORY = "dataSourceVo.directory";
	public static final String DATA_SOURCE_VO_PATH = "dataSourceVo.path";
	public static final String DATA_SOURCE_VO_STATUS = "dataSourceVo.status";
	public static final String DATA_SOURCE_VO_SOURCE = "dataSourceVo.source";
	
	public static final String DATA_SOURCE_ID = "dataSource.id";
	public static final String DATA_SOURCE_MICRO_SERVICE_NAME = "dataSource.microService.name";
	public static final String DATA_SOURCE_NAME = "dataSource.name";
	public static final String DATA_SOURCE_DESCRIPTION = "dataSource.description";
	public static final String DATA_SOURCE_PATH = "dataSource.path";
	public static final String DATA_SOURCE_DATA_CENTER = "dataSource.dataCenter";
	public static final String DATA_SOURCE_TYPE = "dataSource.type";
	public static final String DATA_SOURCE_STATUS = "dataSource.status";
	
	public static final String COMPUTATION_ID = "computation.id";
	public static final String COMPUTATION_NAME = "computation.name";
	public static final String COMPUTATION_DESCRIPTION = "computation.description";
	public static final String COMPUTATION_MICROSERVICE = "computation.microService";
	public static final String COMPUTATION_STATUS = "computation.status";
	public static final String COMPUTATION_AVAILABLE = "computation.available";
	
	public static final String ANALYIS_ID = "analysis.id";
	public static final String ANALYIS_NAME = "analysis.name";
	public static final String ANALYIS_DESCRIPTION = "analysis.description";
	public static final String ANALYIS_STATUS = "analysis.status";
	public static final String ANALYIS_MICRO_SERVICE = "analysis.microService";
	public static final String ANALYIS_DATA_SOURCE = "analysis.dataSource";
	public static final String ANALYIS_COMPUTAION = "analysis.computation";
	public static final String ANALYIS_PARAMETERS = "analysis.parameters";
	public static final String ANALYIS_RESULT = "analysis.result";
	public static final String ANALYIS_JOB_ID = "analysis.jobId";
	public static final String ANALYIS_SCRIPT_NAME = "analysis.scriptName";
	
	public static final int MODE_NEW = 1;
	public static final int MODE_EDIT = 2;
	public static final int MODE_READ = 3;

	public static final String STUDY_ID = "studyId";
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String PATH = "path";
	public static final String SERVICE_URL = "serviceUrl";
	public static final String DATACENTER = "dataCenter";
	public static final String MICROSERVICE = "microService";
	public static final String AVAILABLE = "available";

	public static final String ERROR_MICRO_SERVICE_NAME_TAG = "Micro Service Name";
	public static final String ERROR_MICRO_SERVICE_NAME_REQUIRED = "error.microservice.name.required";

	public static final String ERROR_MICRO_SERVICE_URL_TAG = "Micro Service URL";
	public static final String ERROR_MICRO_SERVICE_URL_REQUIRED = "error.microservice.url.required";

	public static final String STATUS_ONLINE = "Online";

	public static final String STATUS_OFFLINE = "Offline";
	
	public static final String ARK_GENOMICS_COMPUTATION_DIR="computation";

	public static final String ARK_GENOMICS_ANALYSIS_DIR="analysis";
	
	public static final String STATUS_READY = "Ready";

	public static final String STATUS_NOT_READY = "Not Ready";

	public static final String STATUS_NOT_REQUIRED = "Not Required";
	
	public static final String STATUS_PROCESSED="Processed";

	public static final String STATUS_UNPROCESSED="Unprocessed";
	
	public static final String STATUS_PROCESSING="Processing";

	public static final String STATUS_UPLOADED="Uploaded";

	public static final String STATUS_UPLOADING="Uploading";
	
	public static final String STATUS_UPLOAD_FAILED="Upload Failed";
	
	public static final String STATUS_COMPILED="Compiled";
	
	public static final String STATUS_CPMPILE_FAILED="Compile Failed";
	
	public static final String STATUS_UNDEFINED="Undefined";
	
	public static final String STATUS_COMPLETED="Completed";

	public static final String STATUS_RUNNING="Running";

	public static final String STATUS_FAILED="Failed";
	
	public static final String STATUS_SUBMITTED="Submitted";
	
	public static final String ERROR_COMPUTATION_NAME_TAG = "Computation Name";
	public static final String ERROR_COMPUTATION_NAME_REQUIRED = "error.computation.name.required";
	
	public static final String ERROR_COMPUTATION_MICROSERVICE_TAG = "Micro Service";
	public static final String ERROR_COMPUTATION_MICROSERVICE_REQUIRED = "error.computation.microservice.required";
	
	public static final String ERROR_ANALYSIS_NAME_TAG = "Analysis Name";
	public static final String ERROR_ANALYSIS_NAME_REQUIRED = "error.analysis.name.required";
	
	public static final String ERROR_ANALYSIS_MICROSERVICE_TAG = "Micro Service";
	public static final String ERROR_ANALYSIS_MICROSERVICE_REQUIRED = "error.analysis.microservice.required";
	
	public static final String ERROR_ANALYSIS_COMPUTATION_TAG = "Alogorithm";
	public static final String ERROR_ANALYSIS_COMPUTATION_REQUIRED = "error.analysis.computation.required";
	
	
	
}