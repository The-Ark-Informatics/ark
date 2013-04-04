package au.org.theark.core.dao;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import au.org.theark.core.model.report.entity.FieldCategory;
import au.org.theark.core.model.report.entity.Search;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.vo.DataExtractionVO;
import au.org.theark.core.vo.ExtractionVO;

public interface IDataExtractionDao {
	public File createBiocollectionCSV(Search search, DataExtractionVO devo, Collection<CustomFieldDisplay> cfds, FieldCategory fieldCategory);
	public File createBiospecimenCSV(Search search, DataExtractionVO devo, Collection<CustomFieldDisplay> cfds, FieldCategory fieldCategory);
	public File createSubjectDemographicCSV(Search search, DataExtractionVO devo, Collection<CustomFieldDisplay> cfds, FieldCategory fieldCategory);
	public File createBiospecimenDataCustomCSV(Search search, DataExtractionVO devo, Collection<CustomFieldDisplay> cfds, FieldCategory fieldCategory);
}
