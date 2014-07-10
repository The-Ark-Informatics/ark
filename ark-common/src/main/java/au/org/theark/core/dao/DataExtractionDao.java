/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.core.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.Constants;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.report.entity.BiocollectionFieldSearch;
import au.org.theark.core.model.report.entity.BiospecimenField;
import au.org.theark.core.model.report.entity.ConsentStatusField;
import au.org.theark.core.model.report.entity.DemographicField;
import au.org.theark.core.model.report.entity.FieldCategory;
import au.org.theark.core.model.report.entity.Search;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.util.CsvWriter;
import au.org.theark.core.vo.DataExtractionVO;
import au.org.theark.core.vo.ExtractionVO;
import au.org.theark.core.vo.LinkedExtractionVO;

/**
 * @author cellis
 * @param <T>
 * 
 */
@Repository("dataExtractionDao")
public class DataExtractionDao<T> extends HibernateSessionDao implements IDataExtractionDao {
	private static Logger	log	= LoggerFactory.getLogger(DataExtractionDao.class);

	/**
	 * Simple export to CSV as first cut
	 */
	public File createSubjectDemographicCSV(Search search, DataExtractionVO devo, List<DemographicField> allSubjectFields, List<CustomFieldDisplay> cfds, FieldCategory fieldCategory) {
		final String tempDir = System.getProperty("java.io.tmpdir");
		String filename = new String("SUBJECTDEMOGRAPHICS.csv");
		final java.io.File file = new File(tempDir, filename);
		if (filename == null || filename.isEmpty()) {
			filename = "exportcsv.csv";
		}

		HashMap<String, ExtractionVO> hashOfSubjectsWithData = devo.getDemographicData();
		HashMap<String, ExtractionVO> hashOfSubjectCustomData = devo.getSubjectCustomData();

		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file);
			CsvWriter csv = new CsvWriter(outputStream);

			// Header
			csv.write("SUBJECTUID");
			
			Set<String> demofields = new HashSet<String>();
			for (String subjectUID : hashOfSubjectsWithData.keySet()) {
				HashMap<String, String> keyValues = hashOfSubjectsWithData.get(subjectUID).getKeyValues();
				demofields.addAll((keyValues.keySet()));
			}
			for(String demoField : demofields) {
				csv.write(demoField);
			}
			for (CustomFieldDisplay cfd : cfds) {
				csv.write(cfd.getCustomField().getName());
			}
			csv.endLine();

			for (String subjectUID : hashOfSubjectsWithData.keySet()) {
				csv.write(subjectUID);

				for (String demoField : demofields) {
					HashMap<String, String> keyValues = hashOfSubjectsWithData.get(subjectUID).getKeyValues();
					csv.write(keyValues.get(demoField));
					
				}

				/**
				 * for (String subjectUID : hashOfSubjectsWithData.keySet()) { HashMap<String, String> keyValues =
				 * hashOfSubjectsWithData.get(subjectUID).getKeyValues(); log.info(subjectUID + " has " + keyValues.size() + "demo fields"); //
				 * remove(subjectUID).getKeyValues().size() + "demo fields"); for (String key : keyValues.keySet()) { log.info("     key=" + key +
				 * "\t   value=" + keyValues.get(key)); } }
				 */
				ExtractionVO evo = hashOfSubjectCustomData.get(subjectUID);
				if (evo != null) {
					HashMap<String, String> keyValues = evo.getKeyValues();
					for (CustomFieldDisplay cfd : cfds) {

						String valueResult = keyValues.get(cfd.getCustomField().getName());
						if (cfd.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE) && valueResult != null) {
							try {
								DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
								String[] dateFormats = { au.org.theark.core.Constants.DD_MM_YYYY, au.org.theark.core.Constants.yyyy_MM_dd_hh_mm_ss_S };
								Date date = DateUtils.parseDate(valueResult, dateFormats);
								csv.write(dateFormat.format(date));
							}
							catch (ParseException e) {
								csv.write(valueResult);
							}
						}
						else {
							csv.write(valueResult);
						}
					}
				}
				else {
					// Write out a line with no values (no data existed for subject in question
					for (CustomFieldDisplay cfd : cfds) {
						csv.write("");
					}
				}

				csv.endLine();
			}
			csv.close();
		}
		catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}

		return file;
	}

	/**
	 * 
	 * Simple export to CSV as Biocollection Data
	 * 
	 */
	public File createBiocollectionCSV(Search search, DataExtractionVO devo, List<CustomFieldDisplay> cfds, FieldCategory fieldCategory) {
		final String tempDir = System.getProperty("java.io.tmpdir");
		String filename = new String("BIOCOLLECTION.csv");
		final java.io.File file = new File(tempDir, filename);
		if (filename == null || filename.isEmpty()) {
			filename = "exportBiocollectioncsv.csv";
		}
		HashMap<String, ExtractionVO> hashOfBiocollectionsWithData = devo.getBiocollectionData();
		HashMap<String, ExtractionVO> hashOfBiocollectionCustomData = devo.getBiocollectionCustomData();

		Set<String> bioCollectionUids = new HashSet<String>();
		bioCollectionUids.addAll(hashOfBiocollectionsWithData.keySet());
		bioCollectionUids.addAll(hashOfBiocollectionCustomData.keySet());

		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file);
			CsvWriter csv = new CsvWriter(outputStream);

			// Header
			csv.write("SUBJECTID");
			csv.write("BIOCOLLECTIONUID");
			
			for (BiocollectionFieldSearch bcfs : search.getBiocollectionFieldsToReturn()) {
				csv.write(bcfs.getBiocollectionField().getPublicFieldName());
			}
			for (CustomFieldDisplay cfd : cfds) {
				csv.write(cfd.getCustomField().getName());
			}

			csv.endLine();

			for (String biocollectionUID : bioCollectionUids) {
				ExtractionVO hash = hashOfBiocollectionsWithData.get(biocollectionUID);
				csv.write(hash.getSubjectUid());
				csv.write(biocollectionUID);
				
				for (BiocollectionFieldSearch bcfs : search.getBiocollectionFieldsToReturn()) {
					
					if(hash!=null){
						HashMap<String, String> keyValues = hash.getKeyValues();
						//csv.write(keyValues.get(bcfs.getBiocollectionField().getPublicFieldName()));
						
						String valueResult = keyValues.get(bcfs.getBiocollectionField().getPublicFieldName());
						if (bcfs.getBiocollectionField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE) && valueResult != null) {
							try {
								DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
								String[] dateFormats = { au.org.theark.core.Constants.DD_MM_YYYY, au.org.theark.core.Constants.yyyy_MM_dd_hh_mm_ss_S };
								Date date = DateUtils.parseDate(valueResult, dateFormats);
								csv.write(dateFormat.format(date));
							}
							catch (ParseException e) {
								csv.write(valueResult);
							}
						}
						else {
							csv.write(valueResult);
						}
						
					}
				}
				/*
				 * for(CustomFieldDisplay cfd : cfds) { HashMap<String, String> keyValues =
				 * hashOfBiocollectionCustomData.get(biocollectionUID).getKeyValues(); String valueResult = keyValues.get(cfd.getCustomField().getName());
				 * if (cfd.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE) && valueResult != null) { DateFormat
				 * dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY); dateFormat.setLenient(false); try { String date =
				 * dateFormat.format(dateFormat.parse(valueResult)); csv.write(date); } catch (ParseException e) { csv.write(valueResult); }
				 * csv.write(valueResult); } else { csv.write(valueResult); } } csv.endLine();
				 */

				ExtractionVO evo = hashOfBiocollectionCustomData.get(biocollectionUID);
				if (evo != null) {
					HashMap<String, String> keyValues = evo.getKeyValues();
					for (CustomFieldDisplay cfd : cfds) {

						String valueResult = keyValues.get(cfd.getCustomField().getName());
						if (cfd.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE) && valueResult != null) {
							try {
								DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
								String[] dateFormats = { au.org.theark.core.Constants.DD_MM_YYYY, au.org.theark.core.Constants.yyyy_MM_dd_hh_mm_ss_S };
								Date date = DateUtils.parseDate(valueResult, dateFormats);
								csv.write(dateFormat.format(date));
							}
							catch (ParseException e) {
								csv.write(valueResult);
							}
						}
						else {
							csv.write(valueResult);
						}
					}
				}
				else {
					// Write out a line with no values (no data existed for subject in question
					for (CustomFieldDisplay cfd : cfds) {
						csv.write("");
					}
				}
				csv.endLine();
			}
			csv.close();
		}
		catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}

		return file;
	}

	/**
	 * 
	 * Simple export to CSV as Biospecimen Data
	 * 
	 */
	public File createBiospecimenCSV(Search search, DataExtractionVO devo, List<BiospecimenField> bsfs, List<CustomFieldDisplay> cfds, FieldCategory fieldCategory) {
		final String tempDir = System.getProperty("java.io.tmpdir");
		String filename = new String("BIOSPECIMEN.csv");
		final java.io.File file = new File(tempDir, filename);
		if (filename == null || filename.isEmpty()) {
			filename = "exportBiospecimencsv.csv";
		}
		OutputStream outputStream;
		HashMap<String, ExtractionVO> hashOfBiospecimensWithData = devo.getBiospecimenData();
		HashMap<String, ExtractionVO> hashOfBiospecimenCustomData = devo.getBiospecimenCustomData();

		Set<String> biospecimens = new HashSet<String>();
		biospecimens.addAll(hashOfBiospecimensWithData.keySet());
		biospecimens.addAll(hashOfBiospecimenCustomData.keySet());

		try {
			outputStream = new FileOutputStream(file);
			CsvWriter csv = new CsvWriter(outputStream);

			// Header
			csv.write("SUBJECTUID");
			csv.write("BIOSPECIMENUID");
			for (BiospecimenField bsf : bsfs) {
				if (!bsf.getPublicFieldName().equalsIgnoreCase("biospecimenUid"))
					csv.write(bsf.getPublicFieldName());
			}
			for (CustomFieldDisplay cfd : cfds) {
				csv.write(cfd.getCustomField().getName());
			}
			csv.endLine();

			for (String biospecimenUID : biospecimens) {
				ExtractionVO evo = hashOfBiospecimensWithData.get(biospecimenUID);
				if(evo != null && evo.getSubjectUid() != null){
					csv.write(evo.getSubjectUid());
					csv.write(biospecimenUID);
					
					for (BiospecimenField bsf : bsfs) {
						
						if(evo != null){
							HashMap<String, String> keyValues = evo.getKeyValues();
							if (!bsf.getPublicFieldName().equalsIgnoreCase("biospecimenUid")){
								String valueResult = keyValues.get(bsf.getPublicFieldName());
								if (bsf.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE) && valueResult != null) {
									try {
										DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
										String[] dateFormats = { au.org.theark.core.Constants.DD_MM_YYYY, au.org.theark.core.Constants.yyyy_MM_dd_hh_mm_ss_S };
										Date date = DateUtils.parseDate(valueResult, dateFormats);
										csv.write(dateFormat.format(date));
									}
									catch (ParseException e) {
										csv.write(valueResult);
									}
								}
								else {
									csv.write(valueResult);
								}
							}
							
							
						}
					}
				}

				evo = new ExtractionVO();
				evo = hashOfBiospecimenCustomData.get(biospecimenUID);
				if (evo != null) {
					HashMap<String, String> keyValues = evo.getKeyValues();
					for (CustomFieldDisplay cfd : cfds) {

						String valueResult = keyValues.get(cfd.getCustomField().getName());
						if (cfd.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE) && valueResult != null) {
							try {
								DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
								String[] dateFormats = { au.org.theark.core.Constants.DD_MM_YYYY, au.org.theark.core.Constants.yyyy_MM_dd_hh_mm_ss_S };
								Date date = DateUtils.parseDate(valueResult, dateFormats);
								csv.write(dateFormat.format(date));
							}
							catch (ParseException e) {
								csv.write(valueResult);
							}
						}
						else {
							csv.write(valueResult);
						}
					}
				}
				else {
					// Write out a line with no values (no data existed for subject in question
					for (CustomFieldDisplay cfd : cfds) {
						csv.write("");
					}
				}
				csv.endLine();
			}
			csv.close();
		}
		catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}

		return file;
	}

	public File createBiospecimenDataCustomCSV(Search search, DataExtractionVO devo, List<CustomFieldDisplay> cfds, FieldCategory fieldCategory) {
		HashMap<String, ExtractionVO> hashOfBiospecimenCustomData = devo.getBiospecimenCustomData();
		log.info(" writing out biospecimenCustomData " + hashOfBiospecimenCustomData.size() + " entries for category '" + fieldCategory + "'");

		final String tempDir = System.getProperty("java.io.tmpdir");
		String filename = new String("BIOSPECIMENCUSTOMDATA.csv");
		final java.io.File file = new File(tempDir, filename);
		if (filename == null || filename.isEmpty()) {
			filename = "exportBiospecimenCustomcsv.csv";
		}
		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file);
			CsvWriter csv = new CsvWriter(outputStream);

			csv.write("SUBJECTUID");

			// Header

			for (String key : hashOfBiospecimenCustomData.keySet()) {
				HashMap<String, String> keyValues = hashOfBiospecimenCustomData.get(key).getKeyValues();
				for (String key2 : keyValues.keySet()) {
					csv.write(key2);
				}
				break;
			}
			csv.endLine();

			for (String subjectUID : hashOfBiospecimenCustomData.keySet()) {
				HashMap<String, String> keyValues = hashOfBiospecimenCustomData.get(subjectUID).getKeyValues();
				for (String key : keyValues.keySet()) {
					csv.write(keyValues.get(keyValues.get(key)));
				}
				csv.endLine();
			}
			csv.close();
		}
		catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}

		return file;
	}
	
	public File listToCSV(List<String> list) {
		final String tempDir = System.getProperty("java.io.tmpdir");
		String filename = new String("tmp.csv");
		final java.io.File file = new File(tempDir, filename);

		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file);
			CsvWriter csv = new CsvWriter(outputStream);
			
			for (String value : list){
				csv.write(value);
				csv.endLine();
			}
		}
		catch(FileNotFoundException fne){
			
		}
		return file;
	}
	

	public File createGenoCSV(Search search, DataExtractionVO devo, FieldCategory fieldCategory, Long maxProcessesPerPipeline, Map<Long, Long> maxInputList, Map<Long, Long> maxOutputList){
		final String tempDir = System.getProperty("java.io.tmpdir");
		String filename = new String("GENO.csv");
		final java.io.File file = new File(tempDir, filename);
		
		List<LinkedExtractionVO> genoData = devo.getGenoData();

		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file);
			CsvWriter csv = new CsvWriter(outputStream);

			// Header
			csv.write("SUBJECTUID");
			//csv.write("RECORD_DATE_TIME");??

			csv.write(Constants.GENO_FIELDS_PIPELINE_ID);
			csv.write(Constants.GENO_FIELDS_PIPELINE_NAME);
			csv.write(Constants.GENO_FIELDS_PIPELINE_DECSRIPTION);
			for (int processIndex=1; processIndex<=maxProcessesPerPipeline ; processIndex++) { //one-based humanized
				//process
				csv.write(Constants.GENO_FIELDS_PROCESS_ID + "_forProcess_" + processIndex);
				csv.write(Constants.GENO_FIELDS_PROCESS_NAME + "_forProcess_" + processIndex);
				csv.write(Constants.GENO_FIELDS_PROCESS_DESCRIPTION + "_forProcess_" + processIndex);
				csv.write(Constants.GENO_FIELDS_PROCESS_START_TIME + "_forProcess_" + processIndex);
				csv.write(Constants.GENO_FIELDS_PROCESS_END_TIME + "_forProcess_" + processIndex);
				
				//commmand
				csv.write(Constants.GENO_FIELDS_PROCESS_COMMAND_SERVER_URL + "_" + processIndex);
				csv.write(Constants.GENO_FIELDS_PROCESS_COMMAND_NAME + "_" + processIndex);
				csv.write(Constants.GENO_FIELDS_PROCESS_COMMAND_LOCATION + "_" + processIndex);
//				csv.write(Constants.GENO_FIELDS_PROCESS_COMMAND_INPUT_FILE_FORMAT)//				csv.write(Constants.GENO_FIELDS_PROCESS_COMMAND_OUTPUT_FILE_FORMAT);
					
				//input
				//for each of the inputs..........!!!!  EACH - there COULD be more than one
				long maxInputsForThisProcess = 0L;
				if(maxInputList!=null && maxInputList.get(new Long(processIndex))!=null){
					maxInputsForThisProcess = maxInputList.get(new Long(processIndex)).longValue();
				}

				long maxOutputsForThisProcess = 0L;
				if(maxOutputList!=null && maxOutputList.get(new Long(processIndex))!=null){
					maxOutputsForThisProcess = maxOutputList.get(new Long(processIndex)).longValue();
				}

				/**why ther heck isnt this workinG?
				for(long inputIndex=1L ; inputIndex<=maxInputsForThisProcess ; inputIndex++){
					//csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_SERVER);
					csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_SERVER + (processIndex>1?("_forProcess_"+processIndex):"") + "_" + inputIndex );
					csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_LOCATION + (processIndex>1?("_forProcess_"+processIndex):"") + "_" + inputIndex );
					csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_FILE_HASH + (processIndex>1?("_forProcess_"+processIndex):"") + "_" + inputIndex );
					csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_FILE_TYPE + (processIndex>1?("_forProcess_"+processIndex):"") + "_" + inputIndex );
					csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_KEPT + (processIndex>1?("_forProcess_"+processIndex):"") + "_" + inputIndex );
				}*/
				long inputIndex=1L ; 
				while(inputIndex<=maxInputsForThisProcess ){
					//csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_SERVER);
					csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_SERVER 	+  "_" + inputIndex + "_forProcess_" + processIndex);
					csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_LOCATION 	+ "_" + inputIndex + "_forProcess_" + processIndex );
					csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_FILE_HASH + "_" + inputIndex + "_forProcess_" + processIndex );
					csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_FILE_TYPE + "_" + inputIndex + "_forProcess_" + processIndex );
					csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_KEPT  	+ "_" + inputIndex + "_forProcess_" + processIndex );
					inputIndex++;	
				}
				

				long outputIndex=1L ; 
				while(outputIndex<=maxOutputsForThisProcess ){
					//csv.write(Constants.GENO_FIELDS_PROCESS_OUTPUT_SERVER);
					csv.write(Constants.GENO_FIELDS_PROCESS_OUTPUT_SERVER 	 + "_" + outputIndex  + "_forProcess_" + processIndex);
					csv.write(Constants.GENO_FIELDS_PROCESS_OUTPUT_LOCATION  + "_" + outputIndex  + "_forProcess_" + processIndex);
					csv.write(Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_HASH + "_" + outputIndex + "_forProcess_" + processIndex );
					csv.write(Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_TYPE + "_" + outputIndex + "_forProcess_" + processIndex );
					csv.write(Constants.GENO_FIELDS_PROCESS_OUTPUT_KEPT  	 + "_" + outputIndex + "_forProcess_" + processIndex );
					outputIndex++;	
				}
				
				
		/*		long maxInputsForThisProcess = 0L;
				if(maxInputList!=null && maxInputList.get(new Long(processIndex))!=null){
					maxInputsForThisProcess = maxInputList.get(new Long(processIndex));
				}
				for(long inputIndex=1 ; inputIndex<maxInputsForThisProcess ; inputIndex++){
					//csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_SERVER);
					csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_SERVER + (processIndex>1?("_"+processIndex):"") + "_" + inputIndex );
					csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_LOCATION + (processIndex>1?("_"+processIndex):"") + "_" + inputIndex );
					csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_FILE_HASH + (processIndex>1?("_"+processIndex):"") + "_" + inputIndex );
					csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_FILE_TYPE + (processIndex>1?("_"+processIndex):"") + "_" + inputIndex );
					csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_KEPT + (processIndex>1?("_"+processIndex):"") + "_" + inputIndex );
				}*/
/*
				//input
				//for each of the inputs..........!!!!  EACH - there COULD be more than one
				csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_SERVER + "_" + processIndex);
				csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_LOCATION + "_" + processIndex);
				csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_FILE_HASH + "_" + processIndex);
				csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_FILE_TYPE + "_" + processIndex);
				csv.write(Constants.GENO_FIELDS_PROCESS_INPUT_KEPT + "_" + processIndex);
				
				//output
				//for each of the outputs..........!!!!  EACH - there COULD be more than one
				csv.write(Constants.GENO_FIELDS_PROCESS_OUTPUT_SERVER + "_forProcess_" + processIndex);
				csv.write(Constants.GENO_FIELDS_PROCESS_OUTPUT_LOCATION + "_forProcess_" + processIndex);
				csv.write(Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_HASH + "_forProcess_" + processIndex);
				csv.write(Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_TYPE + "_forProcess_" + processIndex);
				csv.write(Constants.GENO_FIELDS_PROCESS_OUTPUT_KEPT + "_forProcess_" + processIndex);
*/
			}
			csv.endLine();

			//now for the actual data
			for (LinkedExtractionVO evo : genoData) {
				//csv.write(evo.getSubjectUid());//ExtractionVO evo = genoData.get(phenoCollectionId);
				
				if (evo != null) {
					csv.write(evo.getSubjectUid());
					//csv.write(evo.getRecordDate());
					LinkedHashMap<String, String> keyValues = evo.getKeyValues();	//TODO:  Something should be done to check that values always match order of columns
/*order is based on the code that constructs this in study dao 
  				Pipeline pl = lssp.getPipeline();
				map.put(Constants.GENO_FIELDS_PIPELINE_ID, pl.getId().toString());
				map.put(Constants.GENO_FIELDS_PIPELINE_NAME, pl.getName());
				map.put(Constants.GENO_FIELDS_PIPELINE_DECSRIPTION, pl.getDescription());
				
				int index = 0;
				for(Process p : pl.getPipelineProcesses()){
					index++;
					
					//TODO : obvbiously need to pre=append the pipeline info/count too
					map.put((Constants.GENO_FIELDS_PROCESS_ID + (index>1?("_"+index):"")), p.getId().toString());
					map.put((Constants.GENO_FIELDS_PROCESS_NAME + (index>1?("_"+index):"")), p.getName());
					map.put((Constants.GENO_FIELDS_PROCESS_DESCRIPTION + (index>1?("_"+index):"")), p.getDescription());
					map.put((Constants.GENO_FIELDS_PROCESS_START_TIME + (index>1?("_"+index):"")), p.getStartTime()!=null?p.getStartTime().toString():"");
					map.put((Constants.GENO_FIELDS_PROCESS_END_TIME + (index>1?("_"+index):"")), p.getEndTime()!=null?p.getEndTime().toString():"");
					Command command = p.getCommand();
					map.put((Constants.GENO_FIELDS_PROCESS_COMMAND_NAME + (index>1?("_"+index):"")), (command==null?"":command.getName()));
					map.put((Constants.GENO_FIELDS_PROCESS_COMMAND_LOCATION + (index>1?("_"+index):"")), (command==null?"":command.getLocation()));
					map.put((Constants.GENO_FIELDS_PROCESS_COMMAND_SERVER_URL + (index>1?("_"+index):"")), (command==null?"":command.getServerUrl()));
*/
					for(String key : keyValues.keySet()){
						log.info(key + keyValues.get(key));
					}
					csv.write(keyValues.get(Constants.GENO_FIELDS_PIPELINE_ID));

					csv.write(keyValues.get(Constants.GENO_FIELDS_PIPELINE_NAME));
					csv.write(keyValues.get(Constants.GENO_FIELDS_PIPELINE_DECSRIPTION));
					for (int processIndex=1; processIndex<=maxProcessesPerPipeline ; processIndex++) { //one-based humanized
						if(processIndex==1){
							//process
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_ID) == null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_ID));
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_NAME) == null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_NAME));
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_DESCRIPTION) == null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_DESCRIPTION));
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_START_TIME) == null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_START_TIME));
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_END_TIME)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_END_TIME));
							// == null)?"":
							//commmand
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_COMMAND_SERVER_URL)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_COMMAND_SERVER_URL));
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_COMMAND_NAME)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_COMMAND_NAME));
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_COMMAND_LOCATION)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_COMMAND_LOCATION));
			//				csv.write(Constants.GENO_FIELDS_PROCESS_COMMAND_INPUT_FILE_FORMAT)//				csv.write(Constants.GENO_FIELDS_PROCESS_COMMAND_OUTPUT_FILE_FORMAT);
							
							long maxInputsForThisProcess = 0L;
							if(maxInputList!=null && maxInputList.get(new Long(processIndex))!=null){
								maxInputsForThisProcess = maxInputList.get(new Long(processIndex));
							}
							long inputIndex=1L;
							while(inputIndex<=maxInputsForThisProcess){
								//input
								//for each of the inputs..........!!!!  EACH - there COULD be more than one
								csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_INPUT_SERVER + "_" + processIndex + "_" + inputIndex )== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_INPUT_SERVER + "_" + processIndex + "_" + inputIndex ));
								csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_INPUT_LOCATION + "_" + processIndex + "_" + inputIndex )== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_INPUT_LOCATION + "_" + processIndex + "_" + inputIndex ));
								csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_INPUT_FILE_HASH + "_" + processIndex + "_" + inputIndex )== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_INPUT_FILE_HASH + "_" + processIndex + "_" + inputIndex ));
								csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_INPUT_FILE_TYPE + "_" + processIndex + "_" + inputIndex )== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_INPUT_FILE_TYPE + "_" + processIndex + "_" + inputIndex ));
								csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_INPUT_KEPT + "_" + processIndex + "_" + inputIndex )== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_INPUT_KEPT + "_" + processIndex + "_" + inputIndex ));
								inputIndex++;
							}
							
							long maxOutputsForThisProcess = 0L;
							if(maxOutputList!=null && maxOutputList.get(new Long(processIndex))!=null){
								maxOutputsForThisProcess = maxOutputList.get(new Long(processIndex));
							}
							for(long outputIndex=1L ; outputIndex<=maxOutputsForThisProcess ; outputIndex++){
								//output
								//for each of the outputs..........!!!!  EACH - there COULD be more than one
								csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_SERVER + "_" + processIndex + "_" + outputIndex )== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_SERVER + "_" + processIndex + "_" + outputIndex ));
								csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_LOCATION + "_" + processIndex + "_" + outputIndex )== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_LOCATION + "_" + processIndex + "_" + outputIndex ));
								csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_HASH + "_" + processIndex + "_" + outputIndex )== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_HASH + "_" + processIndex + "_" + outputIndex ));
								csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_TYPE + "_" + processIndex + "_" + outputIndex )== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_TYPE + "_" + processIndex + "_" + outputIndex ));
								csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_KEPT + "_" + processIndex + "_" + outputIndex )== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_KEPT + "_" + processIndex + "_" + outputIndex ));
							}

							log.info("\n\nprocess=" + processIndex + 
									"    \n max in=" + maxInputsForThisProcess + "   \n  max outs=" + maxOutputsForThisProcess
									+ "\nequation=" + (inputIndex<maxInputsForThisProcess)
									);
							
							/*
							//output
							//for each of the outputs..........!!!!  EACH - there COULD be more than one
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_SERVER)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_SERVER));
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_LOCATION)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_LOCATION));
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_HASH)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_HASH));
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_TYPE)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_TYPE));
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_KEPT)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_KEPT));*/
						}
						else{
							//process
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_ID + "_" + processIndex)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_ID + "_" + processIndex));
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_NAME + "_" + processIndex)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_NAME + "_" + processIndex));
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_DESCRIPTION + "_" + processIndex)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_DESCRIPTION + "_" + processIndex));
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_START_TIME + "_" + processIndex)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_START_TIME + "_" + processIndex));
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_END_TIME + "_" + processIndex)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_END_TIME + "_" + processIndex));
							
							//commmand
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_COMMAND_SERVER_URL + "_" + processIndex)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_COMMAND_SERVER_URL + "_" + processIndex));
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_COMMAND_NAME + "_" + processIndex)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_COMMAND_NAME + "_" + processIndex));
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_COMMAND_LOCATION + "_" + processIndex)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_COMMAND_LOCATION + "_" + processIndex));
			//				csv.write((Constants.GENO_FIELDS_PROCESS_COMMAND_INPUT_FILE_FORMAT)//				csv.write(Constants.GENO_FIELDS_PROCESS_COMMAND_OUTPUT_FILE_FORMAT));

							long maxInputsForThisProcess = 0L;
							if(maxInputList!=null && maxInputList.get(new Long(processIndex))!=null){
								maxInputsForThisProcess = maxInputList.get(new Long(processIndex));
							}

							long inputIndex=1L ;
							while(inputIndex<=maxInputsForThisProcess){
								log.info("pr=" + processIndex + " in=" + inputIndex);
								csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_INPUT_SERVER + "_" + processIndex + "_" + inputIndex )== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_INPUT_SERVER + "_" + processIndex + "_" + inputIndex ));
								csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_INPUT_LOCATION + "_" + processIndex + "_" + inputIndex )== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_INPUT_LOCATION + "_" + processIndex + "_" + inputIndex ));
								csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_INPUT_FILE_HASH + "_" + processIndex + "_" + inputIndex )== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_INPUT_FILE_HASH + "_" + processIndex + "_" + inputIndex ));
								csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_INPUT_FILE_TYPE + "_" + processIndex + "_" + inputIndex )== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_INPUT_FILE_TYPE + "_" + processIndex + "_" + inputIndex ));
								csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_INPUT_KEPT + "_" + processIndex + "_" + inputIndex )== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_INPUT_KEPT + "_" + processIndex + "_" + inputIndex ));
								inputIndex++;
							}


							long maxOutputsForThisProcess = 0L;
							if(maxOutputList!=null && maxOutputList.get(new Long(processIndex))!=null){
								maxOutputsForThisProcess = maxOutputList.get(new Long(processIndex));
							}

							log.info("\n\nprocess=" + processIndex + 
									"    \n max in=" + maxInputsForThisProcess + "   \n  max outs=" + maxOutputsForThisProcess
									+ "\nequation=" + (inputIndex<maxInputsForThisProcess)
									);
							
							for(long outputIndex=1 ; outputIndex<=maxOutputsForThisProcess ; outputIndex++){
								//output
								//for each of the outputs..........!!!!  EACH - there COULD be more than one
								csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_SERVER + "_" + processIndex + "_" + outputIndex )== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_SERVER + "_" + processIndex + "_" + outputIndex ));
								csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_LOCATION + "_" + processIndex + "_" + outputIndex )== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_LOCATION + "_" + processIndex + "_" + outputIndex ));
								csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_HASH + "_" + processIndex + "_" + outputIndex )== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_HASH + "_" + processIndex + "_" + outputIndex ));
								csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_TYPE + "_" + processIndex + "_" + outputIndex )== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_TYPE + "_" + processIndex + "_" + outputIndex ));
								csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_KEPT + "_" + processIndex + "_" + outputIndex )== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_KEPT + "_" + processIndex + "_" + outputIndex ));
							}

							/*output
							//for each of the outputs..........!!!!  EACH - there COULD be more than one
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_SERVER + "_" + processIndex)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_SERVER + "_" + processIndex));
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_LOCATION + "_" + processIndex)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_LOCATION + "_" + processIndex));
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_HASH + "_" + processIndex)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_HASH + "_" + processIndex));
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_TYPE + "_" + processIndex)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_FILE_TYPE + "_" + processIndex));
							csv.write((keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_KEPT + "_" + processIndex)== null)?"":keyValues.get(Constants.GENO_FIELDS_PROCESS_OUTPUT_KEPT + "_" + processIndex));
							 */
						}
					}
					
					//TODO:  replace this with hardcoded use of the field names or use the function if 
					//		available to getColumnOf(GENO_FIELD_PIPELINE_ID) and put it in there at appropriate line
					
					/*
					 * TODO ASAP  : Change this to iterating in order so naming remains the same
					 *
					 * THIS CODE WORKS BUT WE MAY WANT TO MAKE SURE THAT WE HAVE THE CORRECT ORDER
					 *			
					for (String key : keyValues.keySet()) {

						String valueResult = keyValues.get(key);
						if (valueResult != null) {
							if(key.startsWith(Constants.GENO_FIELDS_PROCESS_END_TIME) || key.startsWith(Constants.GENO_FIELDS_PROCESS_START_TIME)){
								try {
									DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
									String[] dateFormats = { au.org.theark.core.Constants.DD_MM_YYYY, au.org.theark.core.Constants.yyyy_MM_dd_hh_mm_ss_S };
									Date date = DateUtils.parseDate(valueResult, dateFormats);
									csv.write(dateFormat.format(date));
								}
								catch (ParseException e) {
									csv.write(valueResult);
								}
							}
							else{
								csv.write(valueResult);
							}
						}
						else {
							csv.write("");
						}
					} */	
				}
				else{
					//not sure if we need this
//					// Write out a line with no values (no data existed for subject in question
//					for (CustomFieldDisplay cfd : cfds) {
//						csv.write("");
//					}
				}

				csv.endLine();
			}
			csv.close();
		}
		catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}

		return file;
	}
	
	private String getPhenoCollectionName(String collectionID) {
		Criteria c = getSession().createCriteria(PhenoCollection.class, "p");
		c.add(Restrictions.eq("p.id", Long.parseLong(collectionID)));

		PhenoCollection pc = (PhenoCollection) c.uniqueResult();
		return pc.getQuestionnaire().getName();
	}
	
	public File createPhenotypicCSV(Search search, DataExtractionVO devo, List<CustomFieldDisplay> cfds, FieldCategory fieldCategory) {
		final String tempDir = System.getProperty("java.io.tmpdir");
		String filename = new String("PHENOTYPIC.csv");
		final java.io.File file = new File(tempDir, filename);
		if (filename == null || filename.isEmpty()) {
			filename = "exportcsv.csv";
		}

		
		HashMap<String, ExtractionVO> hashOfSubjectsWithData = devo.getPhenoCustomData();
		
		HashMap<String, HashMap<String, String>> subjects = new HashMap<String, HashMap<String, String>>(); //Defs not the most elegant solution
		
		Set<String> set = new TreeSet<String>();
		for(String phenoCollectionID : hashOfSubjectsWithData.keySet()) {
			String phenoName = getPhenoCollectionName(phenoCollectionID);
			set.add(phenoName);
			ExtractionVO vo = hashOfSubjectsWithData.get(phenoCollectionID);
			if(subjects.containsKey(vo.getSubjectUid())) {
				HashMap<String, String> map = subjects.get(vo.getSubjectUid());
				for(Entry<String, String> entry : vo.getKeyValues().entrySet()) {
					map.put(entry.getKey(), entry.getValue());
				}
				map.put(phenoName, hashOfSubjectsWithData.get(phenoCollectionID).getRecordDate().toString());
				subjects.put(vo.getSubjectUid(), map);
			} else {
				HashMap<String, String> map = new HashMap<String, String>();
				for(Entry<String, String> entry : vo.getKeyValues().entrySet()) {
					map.put(entry.getKey(), entry.getValue());
				}
				map.put(phenoName, hashOfSubjectsWithData.get(phenoCollectionID).getRecordDate().toString());
				subjects.put(vo.getSubjectUid(), map);
			}
		}
		
		List<String> ids = new ArrayList<String>(subjects.keySet());
		Collections.sort(ids);
		
		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file);
			CsvWriter csv = new CsvWriter(outputStream);

			// Header
			csv.write("SUBJECTUID");
//			csv.write("RECORD_DATE_TIME");
			
			for(String s : set) {
				csv.write("RECORD_DATE_TIME_" + s);
			}
			
			for (CustomFieldDisplay cfd : cfds) {
				csv.write(cfd.getCustomField().getName());
			}

			csv.endLine();
			
			for(String subjectUID : subjects.keySet()) {
				csv.write(subjectUID);

				for(String s : set) {
					csv.write(subjects.get(subjectUID).get(s));
				}
				
				for(CustomFieldDisplay cfd : cfds) {
					String value = subjects.get(subjectUID).get(cfd.getCustomField().getName());
					if(cfd.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE) && value != null) {
						try {
							DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
							String[] dateFormats = { au.org.theark.core.Constants.DD_MM_YYYY, au.org.theark.core.Constants.yyyy_MM_dd_hh_mm_ss_S };
							Date date = DateUtils.parseDate(value, dateFormats);
							csv.write(dateFormat.format(date));
						}
						catch (ParseException e) {
							csv.write(value);
						}
					} else if(value == null) {
						csv.write("");
					} else {
						csv.write(value);
					}
				}
				csv.endLine();
			}
			csv.close();
			

//			for (String phenoCollectionId : hashOfSubjectsWithData.keySet()) {
//				//csv.write(subjectUID);
//				
//				ExtractionVO evo = hashOfSubjectsWithData.get(phenoCollectionId);
//				
//				if (evo != null) {
//					csv.write(evo.getSubjectUid());
//					csv.write(evo.getRecordDate());
//					HashMap<String, String> keyValues = evo.getKeyValues();
//					for (CustomFieldDisplay cfd : cfds) {
//
//						String valueResult = keyValues.get(cfd.getCustomField().getName());
//						if (cfd.getCustomField().getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE) && valueResult != null) {
//							try {
//								DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
//								String[] dateFormats = { au.org.theark.core.Constants.DD_MM_YYYY, au.org.theark.core.Constants.yyyy_MM_dd_hh_mm_ss_S };
//								Date date = DateUtils.parseDate(valueResult, dateFormats);
//								csv.write(dateFormat.format(date));
//							}
//							catch (ParseException e) {
//								csv.write(valueResult);
//							}
//						}
//						else {
//							csv.write(valueResult);
//						}
//					}
//				}
//				else {
//					// Write out a line with no values (no data existed for subject in question
//					for (CustomFieldDisplay cfd : cfds) {
//						csv.write("");
//					}
//				}

//				csv.endLine();
//			}
//			csv.close();
		}
		catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}

		
		return file;
	}
	
	public File createConsentStatusCSV(Search search, DataExtractionVO devo, List<ConsentStatusField> consentStatusFields, FieldCategory fieldCategory) {
		final String tempDir = System.getProperty("java.io.tmpdir");
		String filename = new String("STUDYCOMPONENTCONSENTSTATUS.csv");
		final java.io.File file = new File(tempDir, filename);
		if (filename == null || filename.isEmpty()) {
			filename = "exportcsv.csv";
		}

		HashMap<String, ExtractionVO> hashOfConsentStatusData = devo.getConsentStatusData();

		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file);
			CsvWriter csv = new CsvWriter(outputStream);
			log.info("hash: " + devo.getConsentStatusData());
			for(Entry<String, ExtractionVO> hash : devo.getConsentStatusData().entrySet()) {
				log.info("Entry: " + hash.getKey() + " " + hash.getValue().getSubjectUid() + " " + hash.getValue().getKeyValues());
			}
			
			// Header
			csv.write("SUBJECTUID");
			
			Set<String> fields = new HashSet<String>();
			for (String subjectUID : hashOfConsentStatusData.keySet()) {
				HashMap<String, String> keyValues = hashOfConsentStatusData.get(subjectUID).getKeyValues();
				fields.addAll((keyValues.keySet()));
			}
			for(String f : fields) {
				csv.write(f);
			}
			csv.endLine();
			log.info("keyset: " + hashOfConsentStatusData.keySet().toString());
			for (String subjectUID : hashOfConsentStatusData.keySet()) {
				csv.write(subjectUID);

				for (String f : fields) {
					HashMap<String, String> keyValues = hashOfConsentStatusData.get(subjectUID).getKeyValues();
					csv.write(keyValues.get(f));
					log.info(keyValues.get(f));
					
				}

				/**
				 * for (String subjectUID : hashOfSubjectsWithData.keySet()) { HashMap<String, String> keyValues =
				 * hashOfSubjectsWithData.get(subjectUID).getKeyValues(); log.info(subjectUID + " has " + keyValues.size() + "demo fields"); //
				 * remove(subjectUID).getKeyValues().size() + "demo fields"); for (String key : keyValues.keySet()) { log.info("     key=" + key +
				 * "\t   value=" + keyValues.get(key)); } }
				 */
				ExtractionVO evo = hashOfConsentStatusData.get(subjectUID);
				if (evo != null) {
					HashMap<String, String> keyValues = evo.getKeyValues();
					for (ConsentStatusField csf : consentStatusFields) {

						String valueResult = keyValues.get(csf.getPublicFieldName());
						if (valueResult != null) {
							csv.write(valueResult);
						}
						else {
							csv.write(valueResult);
						}
					}
				}
				else {
					// Write out a line with no values (no data existed for subject in question
					for (ConsentStatusField csf : consentStatusFields) {
						csv.write("");
					}
				}

				csv.endLine();
			}
			csv.close();
		}
		catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}

		return file;
	}
	
}