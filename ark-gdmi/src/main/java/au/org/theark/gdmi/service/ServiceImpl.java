package au.org.theark.gdmi.service;

import au.org.theark.gdmi.model.dao.IGwasDao;
import au.org.theark.gdmi.model.entity.EncodedData;
import au.org.theark.gdmi.model.entity.MetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("gwasService")
public class ServiceImpl implements ServiceInterface{

	private IGwasDao gwasDaoInt;
	

	public IGwasDao getGwasDaoInt() {
		return gwasDaoInt;
	}

	@Autowired
	public void setGwasDaoInt(IGwasDao gwasDaoInt) {
		this.gwasDaoInt = gwasDaoInt;
	}

	public void create(MetaData metaData) {
		gwasDaoInt.create(metaData);
	}

	public void createEncodedData(EncodedData ed) {
		gwasDaoInt.createEncodedData(ed);
		
	}

}
