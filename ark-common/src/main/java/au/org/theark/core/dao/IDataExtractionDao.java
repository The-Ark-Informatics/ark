package au.org.theark.core.dao;

import java.io.File;
import java.util.HashMap;

import au.org.theark.core.model.report.entity.FieldCategory;
import au.org.theark.core.model.report.entity.Search;
import au.org.theark.core.vo.ExtractionVO;

public interface IDataExtractionDao {
	public File createBiocollectionCSV(Search search, HashMap<String, ExtractionVO> map, FieldCategory fieldCategory);
	public File createBiospecimenCSV(Search search, HashMap<String, ExtractionVO> map, FieldCategory fieldCategory);
	public File createSubjectDemographicCSV(Search search, HashMap<String, ExtractionVO> map, FieldCategory fieldCategory);
	public File createBiospecimenDataCustomCSV(Search search, HashMap<String, ExtractionVO> hashOfBiospecimenCustomData, FieldCategory fieldCategory);
}
