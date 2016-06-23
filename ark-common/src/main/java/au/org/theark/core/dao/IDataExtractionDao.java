package au.org.theark.core.dao;

import java.io.File;
import java.util.List;
import java.util.Map;

import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.core.model.report.entity.BiospecimenField;
import au.org.theark.core.model.report.entity.ConsentStatusField;
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
	public File createPhenotypicCSV(Search search, DataExtractionVO devo, List<PhenoDataSetFieldDisplay> cfds, FieldCategory fieldCategory);
	public File createGenoCSV(Search search, DataExtractionVO allTheData, FieldCategory geno, Long maxProcessesPerPipeline, Map<Long, Long> maxInputList, Map<Long, Long> maxOutputList);
	public File createConsentStatusCSV(Search search, DataExtractionVO devo, List<ConsentStatusField> consentStatusFields, FieldCategory fieldCategory);
	public File createMegaCSV(Search search, DataExtractionVO allTheData, List<DemographicField> allSubjectFields, List<CustomFieldDisplay> biocollectionCustomFieldDisplay, List<CustomFieldDisplay> biospecimenCustomFieldDisplay, List<PhenoDataSetFieldDisplay> phenotypicCustomFieldDisplay, List<ConsentStatusField> consentStatusFields);
}
