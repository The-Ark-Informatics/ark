package au.org.theark.geno.service;

import java.util.Collection;
import java.util.List;

import au.org.theark.core.model.geno.entity.Beam;
import au.org.theark.core.model.geno.entity.Data;
import au.org.theark.core.model.geno.entity.Row;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;

public interface IArkGenoService {
	public Collection<Row> getGenoTableRows(Study study);

	public Collection<Beam> getGenoTableBeam(Study study);

	public Collection<Data> getGenoTableData(Study study, Person person);

	public Data getDataGivenRowandColumn(Person person, Row row, Beam beam);

	public void saveOrUpdate(Data data);

	public void createOrUpdateRows(List<Row> rowList);

	public void createOrUpdateBeams(List<Beam> beamList);

	public void delete(Object object);
	
}
