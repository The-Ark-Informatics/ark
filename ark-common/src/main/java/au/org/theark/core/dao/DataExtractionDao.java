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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.Constants;
import au.org.theark.core.model.report.entity.BiocollectionFieldSearch;
import au.org.theark.core.model.report.entity.BiospecimenField;
import au.org.theark.core.model.report.entity.DemographicField;
import au.org.theark.core.model.report.entity.Entity;
import au.org.theark.core.model.report.entity.FieldCategory;
import au.org.theark.core.model.report.entity.Search;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.util.CsvWriter;
import au.org.theark.core.vo.DataExtractionVO;
import au.org.theark.core.vo.ExtractionVO;

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
	
	public File createPhenotypicCSV(Search search, DataExtractionVO devo, List<CustomFieldDisplay> cfds, FieldCategory fieldCategory) {
		final String tempDir = System.getProperty("java.io.tmpdir");
		String filename = new String("PHENOTYPIC.csv");
		final java.io.File file = new File(tempDir, filename);
		if (filename == null || filename.isEmpty()) {
			filename = "exportcsv.csv";
		}
		
		HashMap<String, ExtractionVO> hashOfSubjectsWithData = devo.getPhenoCustomData();

		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file);
			CsvWriter csv = new CsvWriter(outputStream);

			// Header
			csv.write("SUBJECTUID");
			csv.write("RECORD_DATE_TIME");
			
			for (CustomFieldDisplay cfd : cfds) {
				csv.write(cfd.getCustomField().getName());
			}

			csv.endLine();

			for (String phenoCollectionId : hashOfSubjectsWithData.keySet()) {
				//csv.write(subjectUID);
				
				ExtractionVO evo = hashOfSubjectsWithData.get(phenoCollectionId);
				csv.write(evo.getSubjectUid());
				csv.write(evo.getRecordDate());
				
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
}
