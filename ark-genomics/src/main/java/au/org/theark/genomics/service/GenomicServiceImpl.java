package au.org.theark.genomics.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.model.spark.entity.MicroService;
import au.org.theark.genomics.model.dao.IGenomicsDao;
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

			// BufferedReader br = new BufferedReader(new InputStreamReader(
			// (conn.getInputStream())));
			//
			// String output;
			// while ((output = br.readLine()) != null) {
			//
			// }
			conn.disconnect();
		} catch (MalformedURLException e) {
			log.error("Invalid URL ", e);

		} catch (IOException e) {
			log.error("IO eroor", e);

		}

		return status;
	}

}
