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
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.model.report.entity.BiocollectionFieldSearch;
import au.org.theark.core.model.report.entity.BiospecimenFieldSearch;
import au.org.theark.core.model.report.entity.DemographicFieldSearch;
import au.org.theark.core.model.report.entity.FieldCategory;
import au.org.theark.core.model.report.entity.Search;
import au.org.theark.core.util.CsvWriter;
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
	public File createSubjectDemographicCSV(Search search, HashMap<String, ExtractionVO> hashOfSubjectsWithData, FieldCategory fieldCategory) {
		final String tempDir = System.getProperty("java.io.tmpdir");
		String filename = new String("SUBJECTDEMOGRAPHICS.csv");
		final java.io.File file = new File(tempDir, filename);
		if(filename == null || filename.isEmpty()) {
			filename = "exportcsv.csv";
		}
		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file);
			CsvWriter csv = new CsvWriter(outputStream);

			// Header
			csv.write("SUBJECTUID");
			for (DemographicFieldSearch dfs : search.getDemographicFieldsToReturn()) {
				csv.write(dfs.getDemographicField().getPublicFieldName());
			}
			csv.endLine();
			
			for (String subjectUID : hashOfSubjectsWithData.keySet()) {
				csv.write(subjectUID);
				
				for (DemographicFieldSearch dfs : search.getDemographicFieldsToReturn()) {
					HashMap<String, String> keyValues = hashOfSubjectsWithData.get(subjectUID).getKeyValues();
					csv.write(keyValues.get(dfs.getDemographicField().getPublicFieldName()));
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
	public File createBiocollectionCSV(Search search, HashMap<String, ExtractionVO> hashOfBiocollectionsWithData, FieldCategory fieldCategory) {
		final String tempDir = System.getProperty("java.io.tmpdir");
		String filename = new String("BIOCOLLECTION.csv");
		final java.io.File file = new File(tempDir, filename);
		if(filename == null || filename.isEmpty()) {
			filename = "exportBiocollectioncsv.csv";
		}
		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file);
			CsvWriter csv = new CsvWriter(outputStream);

			// Header
			csv.write("BIOCOLLECTIONUID");
			for (BiocollectionFieldSearch bcfs : search.getBiocollectionFieldsToReturn()) {
				csv.write(bcfs.getBiocollectionField().getPublicFieldName());
			}
			csv.endLine();
			
			for (String biocollectionUID : hashOfBiocollectionsWithData.keySet()) {
				csv.write(biocollectionUID);
				
				for (BiocollectionFieldSearch bcfs : search.getBiocollectionFieldsToReturn()) {
					HashMap<String, String> keyValues = hashOfBiocollectionsWithData.get(biocollectionUID).getKeyValues();
					csv.write(keyValues.get(bcfs.getBiocollectionField().getPublicFieldName()));
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
	public File createBiospecimenCSV(Search search, HashMap<String, ExtractionVO> hashOfBiospecimensWithData, FieldCategory fieldCategory) {
		final String tempDir = System.getProperty("java.io.tmpdir");
		String filename = new String("BIOSPECIMEN.csv");
		final java.io.File file = new File(tempDir, filename);
		if(filename == null || filename.isEmpty()) {
			filename = "exportBiospecimencsv.csv";
		}
		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file);
			CsvWriter csv = new CsvWriter(outputStream);

			// Header
			csv.write("BIOSPECIMENUID");
			for (BiospecimenFieldSearch bsfs : search.getBiospecimenFieldsToReturn()) {
				csv.write(bsfs.getBiospecimenField().getPublicFieldName());
			}
			csv.endLine();
			
			for (String biospecimenUID : hashOfBiospecimensWithData.keySet()) {
				csv.write(biospecimenUID);
				
				for (BiospecimenFieldSearch bsfs : search.getBiospecimenFieldsToReturn()) {
					HashMap<String, String> keyValues = hashOfBiospecimensWithData.get(biospecimenUID).getKeyValues();
					csv.write(keyValues.get(bsfs.getBiospecimenField().getPublicFieldName()));
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
	
	public File createBiospecimenDataCustomCSV(Search search, HashMap<String, ExtractionVO> hashOfBiospecimenCustomData, FieldCategory fieldCategory) {
		log.info(" writing out biospecimenCustomData " + hashOfBiospecimenCustomData.size() + " entries for category '" + fieldCategory + "'");
		
		final String tempDir = System.getProperty("java.io.tmpdir");
		String filename = new String("BIOSPECIMENCUSTOMDATA.csv");
		final java.io.File file = new File(tempDir, filename);
		if(filename == null || filename.isEmpty()) {
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
				for(String key2 : keyValues.keySet()){
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
}
