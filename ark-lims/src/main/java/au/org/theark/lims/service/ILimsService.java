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
package au.org.theark.lims.service;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioCollectionCustomFieldData;
import au.org.theark.core.model.lims.entity.BioSampletype;
import au.org.theark.core.model.lims.entity.BioTransaction;
import au.org.theark.core.model.lims.entity.BioTransactionStatus;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.BiospecimenAnticoagulant;
import au.org.theark.core.model.lims.entity.BiospecimenCustomFieldData;
import au.org.theark.core.model.lims.entity.BiospecimenGrade;
import au.org.theark.core.model.lims.entity.BiospecimenQuality;
import au.org.theark.core.model.lims.entity.BiospecimenStatus;
import au.org.theark.core.model.lims.entity.BiospecimenStorage;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.model.lims.entity.TreatmentType;
import au.org.theark.core.model.lims.entity.Unit;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.lims.model.vo.LimsVO;

public interface ILimsService {
	/**
	 * Look up a Person based on the supplied Long ID that represents a Person primary key. This id is the primary key of the Person table that can
	 * represent a subject or contact.
	 * 
	 * @param personId
	 * @return
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public Person getPerson(Long personId) throws EntityNotFoundException, ArkSystemException;

	/**
	 * Delete a LIMS collection based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void deleteBioCollection(LimsVO modelObject);

	/**
	 * Update a LIMS collection based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void updateBioCollection(LimsVO modelObject);

	/**
	 * Create a LIMS collection based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void createBioCollection(LimsVO modelObject);

	/**
	 * Search the database for a list of BioCollections based on the supplied BioCollection
	 * 
	 * @param bioCollection
	 *           the bioCollection object to be matched against
	 * @return List of BioCollections
	 * @throws ArkSystemException
	 */
	public List<BioCollection> searchBioCollection(BioCollection bioCollection) throws ArkSystemException;

	/**
	 * Search the database for a BioCollection based on the supplied id
	 * 
	 * @param id
	 *           the unique id of the BioCollection
	 * @return BioCollection
	 * @throws EntityNotFoundException
	 */
	public au.org.theark.core.model.lims.entity.BioCollection getBioCollection(Long id) throws EntityNotFoundException;

	/**
	 * Look up a LIMS biospecimen based on the supplied Long id that represents the primary key
	 * 
	 * @param id
	 * @return Biospecimen
	 * @throws EntityNotFoundException
	 */
	public Biospecimen getBiospecimen(Long id) throws EntityNotFoundException;

	/**
	 * Create a LIMS biospecimen based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void createBiospecimen(LimsVO modelObject);

	/**
	 * Update a LIMS biospecimen based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void updateBiospecimen(LimsVO modelObject);

	/**
	 * Delete a LIMS biospecimen based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void deleteBiospecimen(LimsVO modelObject);

	/**
	 * Look up a LIMS bioTransaction based on the supplied Long id that represents the primary key
	 * 
	 * @param id
	 * @return BioTransaction
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public BioTransaction getBioTransaction(Long id) throws EntityNotFoundException, ArkSystemException;

	/**
	 * Get count of the BioTransaction given the criteria
	 * 
	 * @param BioTransaction
	 *           criteria
	 * @return counts
	 */
	public long getBioTransactionCount(BioTransaction bioTransactionCriteria);

	/**
	 * Look up a List of LIMS BioTransaction(s) based on the supplied bioTransaction criteria
	 * 
	 * @param bioTransactionCriteria
	 * @return List<au.org.theark.core.model.lims.entity.BioTransaction>
	 */
	public List<BioTransaction> searchPageableBioTransactions(BioTransaction bioTransactionCriteria, int first, int count);

	/**
	 * Create a LIMS bioTransaction based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void createBioTransaction(LimsVO modelObject);

	/**
	 * Update a LIMS bioTransaction based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void updateBioTransaction(LimsVO modelObject);

	/**
	 * Delete a LIMS bioTransaction based on the supplied LimsVO
	 * 
	 * @param bioTransaction
	 *           the LimsVO object
	 */
	public void deleteBioTransaction(BioTransaction bioTransaction);

	/**
	 * Get a list of all sampleTypes
	 * 
	 * @return List
	 */
	public List<BioSampletype> getBioSampleTypes();

	/**
	 * Determine if provided linkSubjectStudy has any BioCollections associated
	 * 
	 * @return true if provided linkSubjectStudy has one or more BioCollections
	 */
	public Boolean hasBioCollections(LinkSubjectStudy linkSubjectStudy);

	/**
	 * Determine if provided bioCollection has any biospecimens associated
	 * 
	 * @return true if provided bioCollection has one or more Biospecimens
	 */
	public Boolean hasBiospecimens(BioCollection bioCollection);

	/**
	 * Get count of the BioCollections given the criteria
	 * 
	 * @param BioCollection
	 *           criteria
	 * @return counts
	 */
	public long getBioCollectionCount(BioCollection bioCollection);

	/**
	 * A generic interface that will return a list BioCollections specified by a particular criteria, and a paginated reference point
	 * 
	 * @param BioCollection
	 *           criteria
	 * @return Collection of BioCollection
	 */
	public List<BioCollection> searchPageableBioCollections(BioCollection bioCollection, int first, int count);

	/**
	 * Get count of the Biospecimens given the criteria
	 * 
	 * @param Biospecimens
	 *           criteria
	 * @return counts
	 */
	public long getBiospecimenCount(Biospecimen biospecimenCriteria);

	/**
	 * A generic interface that will return a list Biospecimens specified by a particular criteria, and a paginated reference point
	 * 
	 * @param Biospecimens
	 *           criteria
	 * @return Collection of Biospecimen
	 */
	public List<Biospecimen> searchPageableBiospecimens(Biospecimen biospecimenCriteria, int first, int count);

	/**
	 * Get a Biospecimen entity based on a specified BiospecimenUid
	 * @param biospecimenUid
	 * @return
	 */
	public Biospecimen getBiospecimenByUid(String biospecimenUid);

	public long getBioCollectionCustomFieldDataCount(BioCollection criteria, ArkFunction arkFunction);
	
	public List<BioCollectionCustomFieldData> getBioCollectionCustomFieldDataList(BioCollection bioCollectionCriteria, ArkFunction arkFunction, int first, int count);
	
	/**
	 * Allows to Save(Insert) or Update  BioCollectionCustomFieldData. If there are BioCollectionCustomFieldData
	 * with no data value then it will discard it from the save/update process.
	 * @param bioCollectionCFDataList - List of BioCollectionCustomFieldData to commit to database.
	 * @return a List of BioCollectionCustomFieldData that failed to save (Hibernate caught some exception).
	 */
	public List<BioCollectionCustomFieldData> createOrUpdateBioCollectionCustomFieldData(List<BioCollectionCustomFieldData> bioCollectionCFDataList);

	/**
	 * Get the count of biospecimen(s) based on a LimsVO criteria
	 * @param limsVo
	 * @return
	 */
	public long getBiospecimenCount(LimsVO limsVo);

	/**
	 * Get a list of Biospecimen(s) based on a LimsVO criteria
	 * @param limsVo
	 * @param first
	 * @param count
	 * @return
	 */
	public List<Biospecimen> searchPageableBiospecimens(LimsVO limsVo, int first, int count);

	public long getBiospecimenCustomFieldDataCount(Biospecimen biospecimenCriteria, ArkFunction arkFunction);

	public List<BiospecimenCustomFieldData> getBiospecimenCustomFieldDataList(Biospecimen biospecimenCriteria, ArkFunction arkFunction, int first, int count);
	
	/**
	 * Allows to Save(Insert) or Update  BiospecimenCustomFieldData. If there are BiospecimenCustomFieldData
	 * with no data value then it will discard it from the save/update process.
	 * @param biospecimenCFDataList - List of BiospecimenCustomFieldData to commit to database.
	 * @return a List of BiospecimenCustomFieldData that failed to save (Hibernate caught some exception).
	 */
	public List<BiospecimenCustomFieldData> createOrUpdateBiospecimenCustomFieldData(List<BiospecimenCustomFieldData> biospecimenCFDataList);

	/**
	 * Gets a list of units
	 * @return
	 */
	public List<Unit> getUnits();

	/**
	 * Get a List of treatment types
	 * @return
	 */
	public List<TreatmentType> getTreatmentTypes();
	
	/**
	 * Get a List of BioTransaction Status that can be used for a dropdown choice (i.e. no Initial status)
	 * @return
	 */
	public List<BioTransactionStatus> getBioTransactionStatusChoices();
	
	/**
	 * Get a specific BioTransaction Status by name
	 * @return
	 */
	public BioTransactionStatus getBioTransactionStatusByName(String statusName);
	
	/**
	 * Get the sum of all transactions for a Biospecimens
	 * @param biospecimen
	 * @return
	 */
	public Double getQuantityAvailable(Biospecimen biospecimen);

	public List<BiospecimenGrade> getBiospecimenGradeList();

	public List<BiospecimenStorage> getBiospecimenStorageList();

	public List<BiospecimenAnticoagulant> getBiospecimenAnticoagulantList();

	public List<BiospecimenStatus> getBiospecimenStatusList();

	public List<BiospecimenQuality> getBiospecimenQualityList();
	
	public String getNextGeneratedBiospecimenUID(Study study);
	
	public BioCollectionCustomFieldData getBioCollectionCustomFieldData(BioCollection bioCollectionCriteria, ArkFunction arkFunction, String customFieldName);

	public StringBuffer uploadAndReportMatrixBiospecimenFile(Study study, InputStream inputStream, long size, String fileFormat, char delimiterChar);

	public void batchInsertBiospecimens(Collection<Biospecimen> insertBiospecimens);

	public void batchUpdateBiospecimens(Collection<Biospecimen> updateSubjects);

	public BioSampletype getBioSampleTypeByName(String name);

	public TreatmentType getTreatmentTypeByName(String name);

	public void batchUpdateInvCells(List<InvCell> updateInvCells);

	public BioCollection getBioCollectionByName(String name);

	public Unit getUnitByName(String name);

	public List<Biospecimen> getBiospecimenByBioCollection(BioCollection bioCollection);

	public List<String> getAllBiospecimenUIDs(Study study);
	
	public List<String> getAllBiocollectionUIDs(Study study);
	

	
}