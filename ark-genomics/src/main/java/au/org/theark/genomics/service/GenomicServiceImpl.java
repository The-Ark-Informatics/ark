package au.org.theark.genomics.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.model.spark.entity.DataSource;
import au.org.theark.core.model.spark.entity.DataSourceType;
import au.org.theark.core.model.spark.entity.MicroService;
import au.org.theark.genomics.model.dao.IGenomicsDao;
import au.org.theark.genomics.model.vo.DataCenterVo;
import au.org.theark.genomics.model.vo.DataSourceVo;
import au.org.theark.genomics.util.Constants;

@Transactional
@Service(Constants.GENOMIC_SERVICE)
public class GenomicServiceImpl implements IGenomicService {

	@Autowired
	IGenomicsDao genomicsDao;

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
		// TODO Auto-generated method stub
		ArrayList<String> list = new ArrayList<String>();

		String URL = microService.getServiceUrl() + "/datacenters";

		// ArrayList<String> list = new ArrayList<String>();

		// JSONObject jsonObject= new JSONObject();

		// JSONArray jsonArray = new JSONArray();
		// jsonArray.add("");
		//
		// if (jsonArray != null) {
		// int len = jsonArray.size();
		// for (int i = 0; i < len; i++) {
		// list.add(jsonArray.get(i).toString());
		// }
		// }
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

		// if ("SPARK-SERVICE".equalsIgnoreCase(microService.getName())) {
		// list.add("A");
		// list.add("B");
		// list.add("C");
		// } else if ("WP-service".equalsIgnoreCase(microService.getName())) {
		// list.add("D");
		// list.add("E");
		// list.add("F");
		// } else {
		// list.add("G");
		// list.add("H");
		// list.add("I");
		// }
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
			//
			// if (conn.getResponseCode() != 200) {
			// throw new RuntimeException("Failed : HTTP error code : " +
			// conn.getResponseCode());
			// }

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
				ds.setStatus(obj2.get("status").toString());
				ds.setMicroService(datacenter.getMicroService());
				ds.setDataCenter(datacenter.getName());
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

	// public List<DataSourceVo> searchDataSources1(DataCenterVo datacenter) {
	// ArrayList<DataSourceVo> list = new ArrayList<DataSourceVo>();
	//
	// String URL = datacenter.getMicroService().getServiceUrl() +
	// "/datasources";
	//
	// try {
	//
	// URL url = new URL(URL);
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// conn.setRequestMethod("POST");
	// conn.setRequestProperty("Accept", "application/json");
	// conn.setDoOutput(true);
	// conn.setDoInput(true);
	//
	// String line;
	// BufferedReader reader = new BufferedReader(new
	// InputStreamReader(conn.getInputStream()));
	//
	// StringBuffer sb = new StringBuffer();
	// while ((line = reader.readLine()) != null) {
	// sb.append(line);
	// }
	// // writer.close();
	// reader.close();
	//
	// JSONParser parser = new JSONParser();
	//
	// Object outobj = parser.parse(sb.toString());
	// JSONArray array = (JSONArray) outobj;
	//
	// for (int i = 0; i < array.size(); ++i) {
	// JSONObject obj2 = (JSONObject) array.get(i);
	//
	// DataSourceVo ds = new DataSourceVo();
	// ds.setFileName(obj2.get("fileName").toString());
	// ds.setDirectory(obj2.get("directory").toString());
	// ds.setPath(obj2.get("path").toString());
	// ds.setStatus(obj2.get("status").toString());
	//
	// list.add(ds);
	// }
	//
	// for (DataSourceVo data : list) {
	// System.out.println(data.getFileName());
	// }
	//
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	//
	// return list;
	// }

	public List<DataSourceType> listDataSourceTypes() {
		return genomicsDao.listDataSourceTypes();
	}
	
	public DataSource getDataSource(DataSourceVo dataSourceVo) {
		// TODO Auto-generated method stub
		return genomicsDao.getDataSource(dataSourceVo);
	}

}
