package au.org.theark.geno.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.dao.IGenoDao;
import au.org.theark.core.model.geno.entity.Beam;
import au.org.theark.core.model.geno.entity.Data;
import au.org.theark.core.model.geno.entity.Row;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;

@Transactional
@Service("arkGenoService")
public class ArkGenoServiceImpl implements IArkGenoService {

	private IGenoDao genoDao;
	
	public IGenoDao getGenoDao() {
		return genoDao;
	}

	@Autowired
	public void setGenoDao(IGenoDao genoDao) {
		this.genoDao = genoDao;
	}
	
	public Collection<Row> getGenoTableRows(Study study) {
		return genoDao.getGenoTableRow(study);
	}
	
	public Collection<Beam> getGenoTableBeam(Study study) {
		return genoDao.getGenoTableBeam(study);
	}

	public Collection<Data> getGenoTableData(Study study, Person person) {
		return genoDao.getGenoTableData(study, person);
	}

	public Data getDataGivenRowandColumn(Person person, Row row, Beam beam) {
		return genoDao.getDataGivenRowandColumn(person, row, beam);
	}

	public void saveOrUpdate(Data data) {
		genoDao.saveOrUpdate(data);
	}

	public void createOrUpdateRows(List<Row> rowList) {
		genoDao.createOrUpdateRows(rowList);
	}

	public void createOrUpdateBeams(List<Beam> beamList) {
		genoDao.createOrUpdateBeams(beamList);
	}
	
	public void delete(Object object) {
		genoDao.delete(object);
	}
}
