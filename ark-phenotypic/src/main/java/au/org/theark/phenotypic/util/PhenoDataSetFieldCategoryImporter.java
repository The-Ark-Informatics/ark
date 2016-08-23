package au.org.theark.phenotypic.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PhenoDataSetFieldCategoryUpload;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.PhenoDataSetCategoryVO;
import au.org.theark.phenotypic.service.IPhenotypicService;

import com.csvreader.CsvReader;

public class PhenoDataSetFieldCategoryImporter implements IPhenoImporter,Serializable {
	
	private static final long serialVersionUID = 1L;
	private String fieldName;
	private long subjectCount;
	private long fieldCount;
	private long insertCount;
	private long updateCount;
	private double speed;
	private long curPos;
	private long srcLength = -1; // -1 means nothing being processed
	private char phenotypicDelimChr = Constants.IMPORT_DELIM_CHAR_COMMA; 
	private String fileFormat;
	private Person person;
	private List<PhenoDataSetField> fieldList;
	private Study study;
	static Logger log = LoggerFactory.getLogger(PhenoDataSetFieldCategoryImporter.class);
	java.util.Collection<String> fileValidationMessages = new ArrayList<String>();
	java.util.Collection<String> dataValidationMessages = new ArrayList<String>();
	private IArkCommonService<Void> iArkCommonService = null;
	private StringBuffer uploadReport = null;
	private List<PhenoDataSetFieldCategoryUpload> fieldUploadList = new ArrayList<PhenoDataSetFieldCategoryUpload>();
	private Long phenoCollectionId = null;
	private ArkFunction arkFunction;
	private Date completionTime = null;
	private ArkModule arkModule;
	private IPhenotypicService		iPhenotypicService;
	
	
	public PhenoDataSetFieldCategoryImporter(Study study, ArkFunction arkFunction,IArkCommonService<Void> iArkCommonService,IPhenotypicService iPhenotypicService, String fileFormat,char delimiterChar) {
		this.study = study;
		this.iArkCommonService = iArkCommonService;
		this.iPhenotypicService=iPhenotypicService;
		this.fileFormat = fileFormat;
		this.phenotypicDelimChr = delimiterChar;
		this.arkFunction = arkFunction;
		Long sessionModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
		this.arkModule = iArkCommonService.getArkModuleById(sessionModuleId);
	}

	@Override
	public StringBuffer uploadAndReportMatrixDataDictionaryFile(InputStream fileInputStream, long inLength)throws FileFormatException, ArkSystemException {
		uploadReport = new StringBuffer();
		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		Date dateCollected = new Date();
		PhenoDataSetCategory category= null;

		completionTime = null;
		
		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, phenotypicDelimChr);
			String[] stringLineArray;

			srcLength = inLength;
			if (srcLength <= 0) {
				uploadReport.append("The input size was not greater than 0.  Actual length reported: ");
				uploadReport.append(srcLength);
				uploadReport.append("\n");
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}

			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());
		
			ArkFunction arkFunctionToBeUsed = arkFunction;
				
			log.info("ark function = " + arkFunction.getName());
			
				arkFunctionToBeUsed = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_DATA_CATEGORY);
			
			// Loop through all rows in file
			while (csvReader.readRecord()) {
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();
				String categoryName = stringLineArray[0];
				
				//CustomFieldCategory categoryOld = iArkCommonService.getCustomFieldCategoryByNameStudyAndArkFunction(csvReader.get("CATEGORY_NAME"), study, arkFunctionToBeUsed);
				PhenoDataSetCategory categoryOld=iPhenotypicService.getPhenoDataFieldCategoryByNameStudyAndArkFunction(csvReader.get("CATEGORY_NAME"), study, arkFunctionToBeUsed);
				if (categoryOld != null) {
					uploadReport.append("Updating category for: ");
					uploadReport.append("\tATEGORY: ");
					categoryName = csvReader.get("CATEGORY_NAME");
					uploadReport.append(csvReader.get("CATEGORY_NAME"));
					uploadReport.append("\n");

					categoryOld.setName(categoryName);
					categoryOld.setDescription(csvReader.get("DESCRIPTION"));
					
					PhenoDataSetCategoryVO updatePFCVo = new PhenoDataSetCategoryVO();
					updatePFCVo.setPhenoDataSetCategory(categoryOld);
					
					iPhenotypicService.updatePhenoDataSetCategory(updatePFCVo);
					updateCount++;

					PhenoDataSetFieldCategoryUpload categoryUpload = new PhenoDataSetFieldCategoryUpload();
					categoryUpload.setPhenoDataSetCategory(categoryOld);
					fieldUploadList.add(categoryUpload);
				}
				else {
					PhenoDataSetCategory categoryNew= new PhenoDataSetCategory();
					categoryNew.setStudy(study);
					categoryNew.setName(csvReader.get("CATEGORY_NAME"));
					categoryNew.setArkFunction(arkFunctionToBeUsed);
							categoryNew.setDescription(csvReader.get("DESCRIPTION"));
					uploadReport.append("Creating new category: ");
					uploadReport.append("\tCATEGORY: ");
					uploadReport.append((stringLineArray[csvReader.getIndex("CATEGORY_NAME")]));
					uploadReport.append("\n");

					// Try to create the field
					PhenoDataSetCategoryVO saveCFCVo = new PhenoDataSetCategoryVO();
					saveCFCVo.setPhenoDataSetCategory(categoryNew);
					
					iPhenotypicService.createPhenoDataSetCategory(saveCFCVo);
					insertCount++;

					PhenoDataSetFieldCategoryUpload categoryUpload = new PhenoDataSetFieldCategoryUpload();
					categoryUpload.setPhenoDataSetCategory(categoryNew);
					fieldUploadList.add(categoryUpload);
				}

				fieldCount++;
			}
			completionTime = new Date(System.currentTimeMillis());
		}
		catch (IOException ioe) {
			uploadReport.append("Unexpected I/O exception whilst reading the categoty data file\n");
			log.error("uploadAndReportMatrixDataDictionaryFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the category data file");
		}
		catch (Exception ex) {
			uploadReport.append("Unexpected exception whilst reading the category data file\n");
			log.error("uploadAndReportMatrixDataDictionaryFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process category data file");
		}
		finally {
			uploadReport.append("Total file size: ");
			uploadReport.append(inLength);
			uploadReport.append(" B or ");
			uploadReport.append(decimalFormat.format(inLength / 1024.0 / 1024.0));
			uploadReport.append(" MB");
			uploadReport.append("\n");

			if (csvReader != null) {
				try {
					csvReader.close();
				}
				catch (Exception ex) {
					log.error("Cleanup operation failed: csvRdr.close()", ex);
				}
			}
			if (inputStreamReader != null) {
				try {
					inputStreamReader.close();
				}
				catch (Exception ex) {
					log.error("Cleanup operation failed: isr.close()", ex);
				}
			}
			// Restore the state of variables
			srcLength = -1;
		}

		uploadReport.append("Inserted ");
		uploadReport.append(insertCount);
		uploadReport.append(" rows of data");
		uploadReport.append("\n");

		uploadReport.append("Updated ");
		uploadReport.append(updateCount);
		uploadReport.append(" rows of data");
		uploadReport.append("\n");

		return uploadReport;

		
	}

	@Override
	public InputStream convertXlsToCsv(Workbook w) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			OutputStreamWriter osw = new OutputStreamWriter(out);

			// Gets first sheet from workbook
			Sheet s = w.getSheet(0);

			Cell[] row = null;

			// Gets the cells from sheet
			for (int i = 0; i < s.getRows(); i++) {
				row = s.getRow(i);

				if (row.length > 0) {
					osw.write(row[0].getContents());
					for (int j = 1; j < row.length; j++) {
						osw.write(phenotypicDelimChr);
						osw.write(row[j].getContents());
					}
				}
				osw.write("\n");
			}

			osw.flush();
			osw.close();
		}
		catch (UnsupportedEncodingException e) {
			System.err.println(e.toString());
		}
		catch (IOException e) {
			System.err.println(e.toString());
		}
		catch (Exception e) {
			System.err.println(e.toString());
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

	public List<PhenoDataSetFieldCategoryUpload> getFieldUploadList() {
		return fieldUploadList;
	}

	public void setFieldUploadList(
			List<PhenoDataSetFieldCategoryUpload> fieldUploadList) {
		this.fieldUploadList = fieldUploadList;
	}
	
	
	

}
