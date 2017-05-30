package au.org.theark.study.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.collections.ListUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.XLStoCSV;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.component.worksheet.ArkGridCell;

import com.csvreader.CsvReader;

public class SubjectAttachmentValidator {

	Logger								log							= LoggerFactory.getLogger(SubjectAttachmentValidator.class);

	@SuppressWarnings("unchecked")
	private IArkCommonService		iArkCommonService;
	private Long						studyId;
	private Study						study;
	java.util.Collection<String>	fileValidationMessages	= new java.util.ArrayList<String>();
	java.util.Collection<String>	dataValidationMessages	= new java.util.ArrayList<String>();
	private HashSet<Integer>		existantSubjectUIDRows;
	private HashSet<Integer>		nonExistantUIDs;
	private HashSet<ArkGridCell>	errorCells;
	private SimpleDateFormat		simpleDateFormat			= new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
	private char						delimiterCharacter		= au.org.theark.core.Constants.DEFAULT_DELIMITER_CHARACTER;
	private String						fileFormat					= au.org.theark.core.Constants.DEFAULT_FILE_FORMAT;
	private int							row							= 1;

	public SubjectAttachmentValidator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SubjectAttachmentValidator(IArkCommonService iArkCommonService) {
		super();
		this.iArkCommonService = iArkCommonService;
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		this.study = iArkCommonService.getStudy(studyId);
		this.existantSubjectUIDRows = new HashSet<Integer>();
		this.nonExistantUIDs = new HashSet<Integer>();
		this.errorCells = new HashSet<ArkGridCell>();
		simpleDateFormat.setLenient(false);
	}

	/**
	 * Validates the file in the default "matrix" file format assumed: SUBJECTUID,STUDY_COMPONENT,STUDY_COMPONENT_STATUS...
	 * 
	 * @param uploadVo
	 *           is the UploadVO of the file
	 * @return a collection of validation messages
	 */
	public Collection<String> validateSubjectAttachmentFileFormat(UploadVO uploadVo) {
		java.util.Collection<String> validationMessages = null;
		try {
			InputStream inputStream = uploadVo.getFileUpload().getInputStream();
			String filename = uploadVo.getFileUpload().getClientFileName();
			fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
			delimiterCharacter = uploadVo.getUpload().getDelimiterType().getDelimiterCharacter();
			validationMessages = validateSubjectAttachmentFileFormat(inputStream, fileFormat, delimiterCharacter);
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		return validationMessages;
	}

	/**
	 * Validates the file in the default "matrix" file data assumed: SUBJECTUID,STUDY_COMPONENT,STUDY_COMPONENT_STATUS...
	 * 
	 * @param uploadVo
	 *           is the UploadVO of the file
	 * @return a collection of validation messages
	 */
	public Collection<String> validateSubjectAttachmentFileData(UploadVO uploadVo, List<String> uidsToUpdateReference) {
		java.util.Collection<String> validationMessages = null;
		try {
			InputStream inputStream = uploadVo.getFileUpload().getInputStream();
			String filename = uploadVo.getFileUpload().getClientFileName();
			fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
			delimiterCharacter = uploadVo.getUpload().getDelimiterType().getDelimiterCharacter();

			// If Excel, convert to CSV for validation
			if (fileFormat.equalsIgnoreCase("XLS")) {
				Workbook w;
				try {
					w = Workbook.getWorkbook(inputStream);
					delimiterCharacter = ',';
					XLStoCSV xlsToCsv = new XLStoCSV(delimiterCharacter);
					inputStream = xlsToCsv.convertXlsToCsv(w);
					inputStream.reset();
				}
				catch (BiffException e) {
					log.error(e.getMessage());
				}
			}

			validationMessages = validateSubjectAttachmentFileData(inputStream, fileFormat, delimiterCharacter, uidsToUpdateReference);
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		return validationMessages;
	}

	public Collection<String> validateSubjectAttachmentFileData(InputStream inputStream, String fileFormat, char delimChar, List<String> uidsToUpdateReference) {
		java.util.Collection<String> validationMessages = null;

		try {
			validationMessages = validateMatrixAttachmentFileData(inputStream, inputStream.toString().length(), fileFormat, delimChar, Long.MAX_VALUE, uidsToUpdateReference);
		}
		catch (FileFormatException ffe) {
			log.error(au.org.theark.study.web.Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(au.org.theark.study.web.Constants.ARK_BASE_EXCEPTION + abe);
		}
		return validationMessages;
	}

	/**
	 * Validates the file in the default "matrix" file format assumed: SUBJECTUID, FILE_NAME_WITH_FULL_PATH, STUDY_COMPONENT....
	 * 
	 * @param inputStream
	 *           is the input stream of the file
	 * @param fileFormat
	 *           is the file format (eg txt)
	 * @param delimChar
	 *           is the delimiter character of the file (eg comma)
	 * @return a collection of validation messages
	 */
	public Collection<String> validateSubjectAttachmentFileFormat(InputStream inputStream, String fileFormat, char delimChar) {
		java.util.Collection<String> validationMessages = null;

		try {
			// If Excel, convert to CSV for validation
			if (fileFormat.equalsIgnoreCase("XLS")) {
				Workbook w;
				try {
					w = Workbook.getWorkbook(inputStream);
					delimiterCharacter = ',';
					XLStoCSV xlsToCsv = new XLStoCSV(delimiterCharacter);
					inputStream = xlsToCsv.convertXlsToCsv(w);
					inputStream.reset();
				}
				catch (BiffException e) {
					log.error(e.getMessage());
				}
				catch (IOException e) {
					log.error(e.getMessage());
				}
			}
			validationMessages = validateSubjectAttachmentMatrixFileFormat(inputStream, inputStream.toString().length(), fileFormat, delimChar);
		}
		catch (FileFormatException ffe) {
			log.error(au.org.theark.study.web.Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(au.org.theark.study.web.Constants.ARK_BASE_EXCEPTION + abe);
		}
		return validationMessages;
	}

	/**
	 * Validates the file in the default "matrix" file format assumed: SUBJECTUID,STUDY_COMPONENT,STUDY_COMPONENT_STATUS ...
	 * 
	 * TODO: remove globals unless their is a legit reason
	 * 
	 * Where N is any number of columns
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @param inLength
	 *           is the length of a file
	 * @param rowsToValidate
	 *           validate the number of rows specified (or as many as exist, if that number is greater).
	 * @throws FileFormatException
	 *            file format Exception
	 * @throws ArkBaseException
	 *            general ARK Exception
	 * @return a collection of data validation messages
	 */
	@SuppressWarnings("unchecked")
	public java.util.Collection<String> validateMatrixAttachmentFileData(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr, long rowsToValidate,
			List<String> uidsToUpdateReference) throws FileFormatException, ArkSystemException {
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;
		row = 1;
		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;

		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			String[] stringLineArray;
			if (inLength <= 0) {
				throw new FileFormatException("The input files' size was not greater than 0.  Actual length reported: " + inLength);
			}

			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			csvReader.readHeaders();

			List<String> subjectUIDsAlreadyExisting = iArkCommonService.getAllSubjectUIDs(study);
			List<StudyComp> studyComList = iArkCommonService.getStudyComponentByStudy(study);
			List<String> studyCompNames = new ArrayList<String>();

			for (StudyComp comp : studyComList) {
				studyCompNames.add(comp.getName());
			}

			while (csvReader.readRecord()) {
				stringLineArray = csvReader.getValues();// i might still need this or might not now that i am evaluating by name ... TODO evaluate
				String subjectUID = stringLineArray[0]; // First/0th column should be the SubjectUID
				if (!subjectUIDsAlreadyExisting.contains(subjectUID)) {
					nonExistantUIDs.add(row);// TODO test and compare array.
					errorCells.add(new ArkGridCell(0, row));
				}
				else {
					this.validateFilePath(csvReader.getIndex("FILE_NAME_WITH_FULL_PATH"), csvReader.get("FILE_NAME_WITH_FULL_PATH"), "FILE_NAME_WITH_FULL_PATH");
					this.validateSelectedStudyComponent(studyCompNames, csvReader.getIndex("STUDY_COMPONENT"), csvReader.get("STUDY_COMPONENT"));
				}
				row++;
			}
		}
		catch (IOException ioe) {
			log.error("processMatrixSubjectFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the subject data file");
		}
		catch (Exception ex) {
			log.error("processMatrixSubjectFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process subject data file");
		}
		finally {
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
		}

		// TODO: test hashset this i.intvalue or left hashset value??
		for (Iterator<Integer> iterator = nonExistantUIDs.iterator(); iterator.hasNext();) {
			Integer i = (Integer) iterator.next();
			dataValidationMessages.add("Subject on row " + i.intValue() + " does not exist in the database.  Please remove this row and retry or run upload/create this subject first.");
		}
		return dataValidationMessages;
	}

	private void validateSelectedStudyComponent(final List<String> compNameList, final int col, final String cellValue) {
		if (cellValue != null && cellValue.trim().length() > 0) {
			if (!compNameList.contains(cellValue)) {
				errorCells.add(new ArkGridCell(col, row));
				dataValidationMessages.add(cellValue + " study component does not exist in the study selected");
			}
		}
	}

	private void validateFilePath(final int col, final String filePath, final String field) {
		if (filePath != null && filePath.trim().length() > 0) {
			String os = System.getProperty("os.name");
			if (!filePath.contains(File.separator)) {
				errorCells.add(new ArkGridCell(col, row));
				dataValidationMessages.add(field + " is not compatible with the " + os + " operating system");
			}

			FileSystem defaultFS = FileSystems.getDefault();
			Path path = defaultFS.getPath(filePath);

			if (Files.notExists(path) || Files.isDirectory(path)) {
				errorCells.add(new ArkGridCell(col, row));
				dataValidationMessages.add("Attachment file is not exists in the given " + field);
			}
			else {

				if (!Files.isReadable(path)) {
					errorCells.add(new ArkGridCell(col, row));
					dataValidationMessages.add("Attachment file is unable to read by the system");
				}

				if (!Files.isWritable(path)) {
					errorCells.add(new ArkGridCell(col, row));
					dataValidationMessages.add("Attachment file is unable to modify by the sytem");
				}
			}

		}
		else {
			errorCells.add(new ArkGridCell(col, row));
			dataValidationMessages.add(field + " is required to upload subject attachment");
		}
	}

	/**
	 * Validates the file in the custom field list.
	 * 
	 * Requires. SubjectUID specified in row one. And all Fields must be valid for its type
	 * 
	 * Where N is any number of columns
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @param inLength
	 *           is the length of a file
	 * @throws FileFormatException
	 *            file format Exception
	 * @throws ArkBaseException
	 *            general ARK Exception
	 * @return a collection of file format validation messages
	 */
	public java.util.Collection<String> validateSubjectAttachmentMatrixFileFormat(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException,
			ArkBaseException {
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;
		row = 0;
		long srcLength = -1;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			srcLength = inLength;
			if (srcLength <= 0) {
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}

			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			// Set field list (note 2th column to Nth column) // SUBJECTUID DATE_COLLECTED F1 F2 FN // 0 1 2 3 N
			csvReader.readHeaders();
			srcLength = inLength - csvReader.getHeaders().toString().length();

			List<String> actualheaderColumnList = Arrays.asList(csvReader.getHeaders());

			List<String> requiredheaderColumnList = Arrays.asList(au.org.theark.study.web.Constants.SUBJECT_ATTACHMENT_TEMPLATE_HEADER);

			List<String> diffColumnList = new ArrayList<String>();
			diffColumnList.addAll(ListUtils.subtract(requiredheaderColumnList, actualheaderColumnList));
			diffColumnList.addAll(ListUtils.subtract(actualheaderColumnList, requiredheaderColumnList));

			if (diffColumnList.size() > 0) {
				StringBuffer sb = new StringBuffer();
				sb.append("Error: The specified file does not appear to conform to the expected file format.\n");
				sb.append("Please refer to the template, as seen on step one, for the correct format. \n");
				fileValidationMessages.add(sb.toString());
				for (String diffColumn : diffColumnList) {
					if (requiredheaderColumnList.contains(diffColumn)) {
						fileValidationMessages.add("Error: the column name " + diffColumn + " is not specified.");
					}
					else {
						fileValidationMessages.add("Error: the column name " + diffColumn + " is not a valid column name.");
					}
				}
			}
			row = 1;
		}
		catch (IOException ioe) {
			log.error("processMatrixSubjectFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the subject data file");
		}
		catch (Exception ex) {
			log.error("processMatrixSubjectFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process subject data file");
		}
		finally {

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
					// TODO ASAP : re-evaluate below
					inputStreamReader.close();
				}
				catch (Exception ex) {
					log.error("Cleanup operation failed: isr.close()", ex);
				}
			}
		}

		return fileValidationMessages;
	}

	public HashSet<Integer> getInsertRows() {
		return existantSubjectUIDRows;
	}

	public void setInsertRows(HashSet<Integer> insertRows) {
		this.existantSubjectUIDRows = insertRows;
	}

	public HashSet<Integer> getUpdateRows() {
		return nonExistantUIDs;
	}

	public void setUpdateRows(HashSet<Integer> updateRows) {
		this.nonExistantUIDs = updateRows;
	}

	public HashSet<ArkGridCell> getErrorCells() {
		return errorCells;
	}

	public void setErrorCells(HashSet<ArkGridCell> errorCells) {
		this.errorCells = errorCells;
	}

}
