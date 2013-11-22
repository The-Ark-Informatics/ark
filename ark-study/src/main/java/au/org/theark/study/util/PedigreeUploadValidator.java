package au.org.theark.study.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.util.collections.MultiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.graph.Cycle;
import au.org.theark.core.graph.Graph;
import au.org.theark.core.graph.SymbolGraph;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.component.worksheet.ArkGridCell;
import au.org.theark.study.service.IStudyService;

import com.csvreader.CsvReader;

/**
 * The PED file is a TAB delimited file.The first four columns are mandatory.
 * <p>
 * Validator checks the input file has satisfied the required file format
 * 
 * @author thilina
 * 
 */
public class PedigreeUploadValidator {
	private static Logger			log							= LoggerFactory.getLogger(PedigreeUploadValidator.class);

	@SuppressWarnings("unchecked")
	private IArkCommonService		iArkCommonService;

	@SuppressWarnings("unchecked")
	private IStudyService			iStudyService;

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

	public PedigreeUploadValidator() {

	}

	public PedigreeUploadValidator(IArkCommonService iArkCommonService, IStudyService iStudyService) {
		this.iArkCommonService = iArkCommonService;
		this.iStudyService = iStudyService;
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		this.study = iArkCommonService.getStudy(studyId);
		this.existantSubjectUIDRows = new HashSet<Integer>();
		this.nonExistantUIDs = new HashSet<Integer>();
		this.errorCells = new HashSet<ArkGridCell>();
		simpleDateFormat.setLenient(false);
	}

	/**
	 * Validates the file in the default PED file format
	 * 
	 * @param uploadVo
	 *           is the UploadVO of the file
	 * @return a collection of validation messages
	 */
	public Collection<String> validatePedigreeFileFormat(UploadVO uploadVo) {
		java.util.Collection<String> validationMessages = null;
		try {
			InputStream inputStream = uploadVo.getFileUpload().getInputStream();
			String filename = uploadVo.getFileUpload().getClientFileName();
			fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
			delimiterCharacter = uploadVo.getUpload().getDelimiterType().getDelimiterCharacter();
			validationMessages = validatePedigreeFileFormat(inputStream, fileFormat, delimiterCharacter);
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		return validationMessages;
	}

	/**
	 * Validates the file in the default PED file data format
	 * 
	 * @param uploadVo
	 *           is the UploadVO of the file
	 * @return a collection of validation messages
	 */
	public Collection<String> validatePedigreeFileData(UploadVO uploadVo, List<String> uidsToUpdateReference) {
		java.util.Collection<String> validationMessages = null;
		try {
			delimiterCharacter = uploadVo.getUpload().getDelimiterType().getDelimiterCharacter();
			InputStream inputStream = uploadVo.getFileUpload().getInputStream();
			validationMessages = validatePedigreeFileData(inputStream, fileFormat, delimiterCharacter, uidsToUpdateReference);
		}
		catch (Exception e) {
			log.error(e.toString());
		}
		return validationMessages;
	}

	public Collection<String> validatePedigreeFileData(InputStream inputStream, String fileFormat, char delimChar, List<String> uidsToUpdateReference) {
		java.util.Collection<String> validationMessages = null;

		try {
			validationMessages = validateMatrixPedigreeFileData(inputStream, inputStream.toString().length(), fileFormat, delimChar, Long.MAX_VALUE, uidsToUpdateReference);
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
	 * Validates the file in the default PED file format.
	 * 
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
	public java.util.Collection<String> validateMatrixPedigreeFileData(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr, long rowsToValidate,
			List<String> uidsToUpdateReference) throws FileFormatException, ArkSystemException {
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;
		row = 0;
		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		HashMap<Integer, String> noUidMap = new HashMap<Integer, String>();
		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			String[] stringLineArray;
			if (inLength == 0) {
				throw new FileFormatException("The pedigree uploader expects a file comprising 5 tab-delimited columns. Please refer to the template example..  Actual length reported: " + inLength);
			}

			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);

			List<String> subjectUIDsAlreadyExisting = iArkCommonService.getAllSubjectUIDs(study);

			class TmpPedRecord {
				String	individualId;
				String	parentId;
				String	twinId;
				int		row;

				public TmpPedRecord(String individualId, String parentId, String twinId, int row) {
					super();
					this.individualId = individualId;
					this.parentId = parentId;
					this.twinId = twinId;
					this.row = row;
				}

				public String getIndividualId() {
					return individualId;
				}

				public void setIndividualId(String individualId) {
					this.individualId = individualId;
				}

				public String getParentId() {
					return parentId;
				}

				public void setParentId(String parentId) {
					this.parentId = parentId;
				}

				public String getTwinId() {
					return twinId;
				}

				public void setTwinId(String twinId) {
					this.twinId = twinId;
				}

				public int getRow() {
					return row;
				}

				public void setRow(int row) {
					this.row = row;
				}
			}

			List<TmpPedRecord> twinRecords = new ArrayList<TmpPedRecord>();
			List<TmpPedRecord> individualRecords = new ArrayList<TmpPedRecord>();

			StringBuilder pedigree = new StringBuilder();
			ArrayList<String> dummyParents = new ArrayList<String>();
			boolean firstLine = true;

			while (csvReader.readRecord()) {
				stringLineArray = csvReader.getValues();
				int index = 0;
				String subjectUID = stringLineArray[index++];
				String fatherUID = stringLineArray[index++];
				String motherUID = stringLineArray[index++];
				String twinStatus = stringLineArray[index++];
				String twinUID = stringLineArray[index++];

				String dummyParent = "D-";

				if (!subjectUIDsAlreadyExisting.contains(subjectUID)) {
					nonExistantUIDs.add(row);// TODO test and compare array.
					noUidMap.put(row, subjectUID);
					errorCells.add(new ArkGridCell(0, row));
				}

				if (!"-".equalsIgnoreCase(fatherUID) && !subjectUIDsAlreadyExisting.contains(fatherUID)) {
					errorCells.add(new ArkGridCell(1, row));
					dataValidationMessages.add("Invalid father subject UID is specified on row " + row + ".");
				}
				else if (!"-".equalsIgnoreCase(fatherUID)) {
					GenderType gender = iArkCommonService.getSubjectGenderType(fatherUID, study.getId());
					if (gender == null || gender.getName().startsWith("F")) {
						errorCells.add(new ArkGridCell(1, row));
						dataValidationMessages.add("Father on row " + row + " with UID " + fatherUID + " has sex: female.");
					}
					dummyParent = dummyParent + fatherUID;
				}

				if (!"-".equalsIgnoreCase(motherUID) && !subjectUIDsAlreadyExisting.contains(motherUID)) {
					errorCells.add(new ArkGridCell(2, row));
					dataValidationMessages.add("Invalid mother subject UID is specified on row " + row + ".");
				}
				else if (!"-".equalsIgnoreCase(motherUID)) {
					GenderType gender = iArkCommonService.getSubjectGenderType(motherUID, study.getId());
					if (gender == null || gender.getName().startsWith("M")) {
						errorCells.add(new ArkGridCell(2, row));
						dataValidationMessages.add("Mother on row " + row + " with UID " + motherUID + " has sex: male.");
					}
					dummyParent = dummyParent + motherUID;
				}

				if ((twinStatus == null) || (!twinStatus.matches("[-]|[M]|[D]"))) {
					errorCells.add(new ArkGridCell(3, row));
					dataValidationMessages.add("Incorrect encodings found in the TwinStatus field. Please refer to the pedigree template.");
				}
				else {
					if (twinStatus.matches("[M]|[D]") && ("-".equals(fatherUID) || "-".equals(motherUID))) {
						errorCells.add(new ArkGridCell(1, row));
						errorCells.add(new ArkGridCell(2, row));
						dataValidationMessages.add("Both parents must be specified for the subject on row " + row + " who is a twin.");
					}
				}

				if ("-".equalsIgnoreCase(twinStatus) && !"-".equalsIgnoreCase(twinUID)) {
					errorCells.add(new ArkGridCell(4, row));
					dataValidationMessages.add("TwinUID specified on row " + row + " for a subject who is not a twin.");
				}
				else if (!"-".equalsIgnoreCase(twinStatus) && !subjectUIDsAlreadyExisting.contains(twinUID)) {
					errorCells.add(new ArkGridCell(4, row));
					dataValidationMessages.add("Invalid twin UID " + twinUID + " specified on row " + row + ".");
				}
				else {
					if (!"-".equalsIgnoreCase(twinStatus)) {
						twinRecords.add(new TmpPedRecord(subjectUID, fatherUID + "-" + motherUID, twinUID, row));
					}
				}

				long existingRelationships = iStudyService.getRelationshipCount(subjectUID, studyId);

				if (existingRelationships > 0) {
					errorCells.add(new ArkGridCell(0, row));
					dataValidationMessages.add("Subject on row " + row + " with UID " + subjectUID + " is already member of a pedigree.");
				}

				individualRecords.add(new TmpPedRecord(subjectUID, fatherUID + "-" + motherUID, null, row));

				if (!"D-".equals(dummyParent) && !dummyParents.contains(dummyParent)) {
					dummyParents.add(dummyParent);
					if (!"-".equals(fatherUID)) {
						if (firstLine) {
							pedigree.append(fatherUID + " " + dummyParent);
							firstLine = false;
						}
						else {
							pedigree.append("\n" + fatherUID + " " + dummyParent);
						}
					}
					if (!"-".equals(motherUID)) {
						if (firstLine) {
							pedigree.append(motherUID + " " + dummyParent);
							firstLine = false;
						}
						else {
							pedigree.append("\n" + motherUID + " " + dummyParent);
						}
					}
					pedigree.append("\n" + dummyParent + " " + subjectUID);
				}
				else if (!"D-".equals(dummyParent)) {
					if (firstLine) {
						pedigree.append(dummyParent + " " + subjectUID);
						firstLine = false;
					}
					else {
						pedigree.append("\n" + dummyParent + " " + subjectUID);
					}
				}

				row++;
			}

			loop1: for (TmpPedRecord tmp1 : twinRecords) {
				loop2: for (TmpPedRecord tmp2 : individualRecords) {
					if (tmp1.getTwinId() != null && tmp1.getTwinId().equals(tmp2.getIndividualId())) {
						if (!tmp1.getParentId().equalsIgnoreCase(tmp2.getParentId())) {
							errorCells.add(new ArkGridCell(4, tmp1.getRow()));
							errorCells.add(new ArkGridCell(1, tmp2.getRow()));
							dataValidationMessages.add("Twins were specified with miss-matched parent UIDs on rows " + tmp1.getRow() + " and " + tmp2.getRow() + ".");
							break loop2;
						}
					}
				}
			}

			Set<String> circularUIDs = getCircularUIDs(pedigree);
			if (circularUIDs.size() > 0) {
				dataValidationMessages.add("Circular relationship encountered within the pedigree file.");
				StringBuffer sb = new StringBuffer("The proposed action will cause a pedigree cycle involving subjects with UID:");
				boolean first = true;
				for (String uid : circularUIDs) {
					if (first) {
						sb.append(uid);
						first = false;
					}
					else {
						sb.append(", " + uid);
					}
				}
				sb.append(".");
				dataValidationMessages.add(sb.toString());
			}

		}
		catch (IOException ioe) {
			log.error("processMatrixSubjectFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the subject data file.");
		}
		catch (Exception ex) {
			log.error("processMatrixSubjectFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process subject data file.");
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

		for (Iterator<Integer> iterator = nonExistantUIDs.iterator(); iterator.hasNext();) {
			Integer i = (Integer) iterator.next();
			dataValidationMessages.add("Subject on row " + i.intValue() + " with UID " + noUidMap.get(i) + " is not present in the current study.");
		}
		return dataValidationMessages;
	}

	public static Set<String> getCircularUIDs(StringBuilder sb) {
		Set<String> circularUIDs = new HashSet<String>();
		String delimiter = "\\s+";

		char[] characters = new char[sb.length()];
		sb.getChars(0, sb.length(), characters, 0);

		log.info(new String(characters));
		SymbolGraph sg = new SymbolGraph(characters, delimiter);

		Graph G = sg.G();
		Cycle cycle = new Cycle(G);

		if (cycle.hasCycle()) {
			log.info("Detect cycle");
			for (int v : cycle.cycle()) {
				String uid = sg.name(v);
				if (uid != null && !uid.startsWith("D-")) {
					circularUIDs.add(uid);
				}
			}
		}
		else {
			log.info("No cycle detected");
		}
		return circularUIDs;
	}

	/**
	 * Check which parent UID creates a circular relationship
	 * 
	 * @param pedigree
	 *           Relationship graph
	 * @return Problematic UID
	 */
	public static String getCircularUID(MultiMap<String, String> pedigree) {

		for (String uid : pedigree.keySet()) {
			if (checkCircularRelationshipsByUID(pedigree, uid)) {
				return uid;
			}
		}
		return null;
	}

	/**
	 * Check for selected parent UID creates a circular relationship
	 * 
	 * @param pedigree
	 *           Relationship graph
	 * @param uid
	 *           Parent UID
	 * @return Circular relationship status
	 */
	private static boolean checkCircularRelationshipsByUID(MultiMap<String, String> pedigree, String uid) {
		Set<String> allChildren = new HashSet<String>();

		List<String> children = pedigree.get(uid);

		allChildren.addAll(children);

		Set<String> parents = pedigree.keySet();

		// Check invalid grand-child relationships
		List<String> grandChildren = new ArrayList<String>();
		for (String child : children) {
			if (parents.contains(child)) {
				List<String> selectedChildren = pedigree.get(child);
				for (String selectedChild : selectedChildren) {
					if (grandChildren.contains(selectedChild)) {
						return true;
					}
					else {
						grandChildren.add(selectedChild);
					}
				}
			}
		}

		// Check invalid parent-child relationships
		for (String parent : parents) {
			if (!parent.equalsIgnoreCase(uid) && allChildren.contains(parent)) {
				allChildren.addAll(pedigree.get(parent));
			}
		}

		for (String subjectChild : allChildren) {
			if (parents.contains(subjectChild)) {
				List<String> selectedChildren = pedigree.get(subjectChild);

				for (String child : children) {
					if (selectedChildren.contains(child)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Validates the pedigree file type
	 * 
	 * @param inputStream
	 *           is the input stream of the file
	 * @param fileFormat
	 *           is the file format (PED)
	 * @param delimChar
	 *           is the delimiter character of the file (TAB)
	 * @return a collection of validation messages
	 */
	public Collection<String> validatePedigreeFileFormat(InputStream inputStream, String fileFormat, char delimChar) {
		java.util.Collection<String> validationMessages = null;
		if (!fileFormat.equalsIgnoreCase("PED")) {
			fileValidationMessages.add("The input file has to be a PED file");
			validationMessages = fileValidationMessages;
		}
		else {
			try {
				validationMessages = validatePedigreeMatrixFileFormat(inputStream, inputStream.toString().length(), fileFormat, delimiterCharacter);
			}
			catch (ArkBaseException abe) {
				log.error(abe.toString());
			}
			catch (Exception ex) {
				log.error(ex.toString());
			}
		}
		return validationMessages;
	}

	/**
	 * Validates mandatory file columns.
	 * 
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
	public java.util.Collection<String> validatePedigreeMatrixFileFormat(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException, ArkBaseException {
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;
		row = 0;
		String[] stringLineArray;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			if (inLength <= 0) {
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + inLength);
			}

			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			while (csvReader.readRecord()) {
				stringLineArray = csvReader.getValues();
				if (stringLineArray.length != 5) {
					fileValidationMessages.add("The pedigree uploader expects a file comprising 5 tab-delimited columns. Please refer to the template example.");
					break;
				}
			}
			row = 1;
		}
		catch (IOException ioe) {
			log.error("processMatrixSubjectFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the pedigree data file");
		}
		catch (Exception ex) {
			log.error("processMatrixSubjectFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process pedigree data file");
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
