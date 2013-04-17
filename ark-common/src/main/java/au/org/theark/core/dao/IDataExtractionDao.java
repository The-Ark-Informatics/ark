package au.org.theark.core.dao;

import java.io.File;
import java.util.List;

import au.org.theark.core.model.report.entity.BiospecimenField;
import au.org.theark.core.model.report.entity.DemographicField;
import au.org.theark.core.model.report.entity.FieldCategory;
import au.org.theark.core.model.report.entity.Search;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.vo.DataExtractionVO;

public interface IDataExtractionDao {
	public File createBiocollectionCSV(Search search, DataExtractionVO devo, List<CustomFieldDisplay> cfds, FieldCategory fieldCategory);
	public File createBiospecimenCSV(Search search, DataExtractionVO devo, List<BiospecimenField> bsfs, List<CustomFieldDisplay> cfds, FieldCategory fieldCategory);
	public File createSubjectDemographicCSV(Search search, DataExtractionVO devo, List<DemographicField> allSubjectFields, List<CustomFieldDisplay> cfds, FieldCategory fieldCategory);
	public File createBiospecimenDataCustomCSV(Search search, DataExtractionVO devo, List<CustomFieldDisplay> cfds, FieldCategory fieldCategory);
	public File createPhenotypicCSV(Search search, DataExtractionVO devo, List<CustomFieldDisplay> cfds, FieldCategory fieldCategory);
}
