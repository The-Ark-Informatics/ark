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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.dao.IStudyDao;
import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.model.lims.entity.AccessRequest;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioCollectionCustomFieldData;
import au.org.theark.core.model.lims.entity.BioSampletype;
import au.org.theark.core.model.lims.entity.BioTransaction;
import au.org.theark.core.model.lims.entity.BioTransactionStatus;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.BiospecimenAnticoagulant;
import au.org.theark.core.model.lims.entity.BiospecimenCustomFieldData;
import au.org.theark.core.model.lims.entity.BiospecimenGrade;
import au.org.theark.core.model.lims.entity.BiospecimenProtocol;
import au.org.theark.core.model.lims.entity.BiospecimenQuality;
import au.org.theark.core.model.lims.entity.BiospecimenStatus;
import au.org.theark.core.model.lims.entity.BiospecimenStorage;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.model.lims.entity.TreatmentType;
import au.org.theark.core.model.lims.entity.Unit;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.lims.model.dao.IBioCollectionDao;
import au.org.theark.lims.model.dao.IBioTransactionDao;
import au.org.theark.lims.model.dao.IBiospecimenDao;
import au.org.theark.lims.model.dao.IInventoryDao;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.util.BiospecimenUploader;
import au.org.theark.lims.web.Constants;

/**
 * @author cellis
 * 
 */
@Transactional
@Service(au.org.theark.lims.web.Constants.LIMS_SERVICE)
public class LimsServiceImpl implements ILimsService {
	private static Logger		log	= LoggerFactory.getLogger(LimsServiceImpl.class);

	@SuppressWarnings("unchecked")
	private IArkCommonService	arkCommonService;
	private IStudyDao				iStudyDao;
	private IBioCollectionDao	iBioCollectionDao;
	private IBiospecimenDao		iBiospecimenDao;
	private IBioTransactionDao	iBioTransactionDao;
	private IInventoryDao 		iInventoryDao;
	private IInventoryService  iInventoryService;

	/**
	 * @param arkCommonService
	 *           the arkCommonService to set
	 */
	@SuppressWarnings("unchecked")
	@Autowired
	public void setArkCommonService(IArkCommonService arkCommonService) {
		this.arkCommonService = arkCommonService;
	}



	/**
	 * @param iStudyDao
	 *           the iStudyDao to set
	 */
	@Autowired
	public void setiStudyDao(IStudyDao iStudyDao) {
		this.iStudyDao = iStudyDao;
	}

	/**
	 * @param iBioCollectionDao
	 *           the iBioCollectionDao to set
	 */
	@Autowired
	public void setiBioCollectionDao(IBioCollectionDao iBioCollectionDao) {
		this.iBioCollectionDao = iBioCollectionDao;
	}

	/**
	 * @param iBiospeciemenDao
	 *           the iBiospeciemenDao to set
	 */
	@Autowired
	public void setiBiospeciemenDao(IBiospecimenDao iBiospecimenDao) {
		this.iBiospecimenDao = iBiospecimenDao;
	}

	/**
	 * @param iBioTransactionDao
	 *           the iBioTransactionDao to set
	 */
	@Autowired
	public void setiBioTransactionDao(IBioTransactionDao iBioTransactionDao) {
		this.iBioTransactionDao = iBioTransactionDao;
	}
	
	/**
	 * @param iInventoryDao
	 *           the iInventoryDao to set
	 */
	@Autowired
	public void setiInventoryDao(IInventoryDao iInventoryDao) {
		this.iInventoryDao = iInventoryDao;
	}
	
	/**
	 * @param iInventoryService the iInventoryService to set
	 */
	@Autowired
	public void setiInventoryService(IInventoryService iInventoryService) {
		this.iInventoryService = iInventoryService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#createCollection(au.org.theark.lims.model.vo.LimsVO)
	 */
	public void createBioCollection(LimsVO modelObject) throws ArkSystemException {
		log.debug("Creating bioCollection: " + modelObject.getBioCollection().getBiocollectionUid());
		iBioCollectionDao.createBioCollection(modelObject.getBioCollection());
	}

	public BioCollection createBioCollection(BioCollection modelObject)  throws ArkSystemException {
		return iBioCollectionDao.createBioCollection(modelObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#deleteBioCollection(au.org.theark.lims.model.vo.LimsVO)
	 */
	public void deleteBioCollection(LimsVO modelObject) {
		log.debug("Deleting bioCollection: " + modelObject.getBioCollection().getBiocollectionUid());
		iBioCollectionDao.deleteBioCollection(modelObject.getBioCollection());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#getBioCollection(au.org.theark.core.model.lims.entity.Collection)
	 */
	public BioCollection getBioCollection(Long id) throws EntityNotFoundException {
		return iBioCollectionDao.getBioCollection(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#getPerson(java.lang.Long)
	 */
	public Person getPerson(Long id) throws EntityNotFoundException, ArkSystemException {
		return iStudyDao.getPerson(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#searchLimsCollection(au.org.theark.core.model.lims.entity.Collection)
	 */
	public java.util.List<BioCollection> searchBioCollection(BioCollection bioCollection) throws ArkSystemException {
		return iBioCollectionDao.searchBioCollection(bioCollection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#updateCollection(au.org.theark.lims.model.vo.LimsVO)
	 */
	public void updateBioCollection(LimsVO modelObject) throws ArkSystemException {
		log.debug("Updating bioCollection: " + modelObject.getBioCollection().getBiocollectionUid());
		iBioCollectionDao.updateBioCollection(modelObject.getBioCollection());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#getBiospecimen(Long)
	 */
	public Biospecimen getBiospecimen(Long id) throws EntityNotFoundException {
		return iBiospecimenDao.getBiospecimen(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#searchBiospecimen(Biospecimen)
	 */
	public List<Biospecimen> searchBiospecimen(Biospecimen biospecimen) throws ArkSystemException {
		return iBiospecimenDao.searchBiospecimen(biospecimen);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#createBiospecimen(au.org.theark.lims.model.vo.LimsVO)
	 */
	public void createBiospecimen(LimsVO modelObject) throws ArkSystemException {
		Biospecimen biospecimen = modelObject.getBiospecimen();
		iBiospecimenDao.createBiospecimen(biospecimen);
		
		// Inheriently create a transaction for the initial quantity
		BioTransaction bioTransaction = modelObject.getBioTransaction();
		bioTransaction.setBiospecimen(biospecimen);
		bioTransaction.setTransactionDate(Calendar.getInstance().getTime());
		
		if(bioTransaction.getQuantity() != null ) {
			bioTransaction.setQuantity(bioTransaction.getQuantity());
		}
		else {
			bioTransaction.setQuantity(new Double(0));
		}
		
		bioTransaction.setReason(Constants.BIOTRANSACTION_STATUS_INITIAL_QUANTITY);
		
		BioTransactionStatus initialStatus = getBioTransactionStatusByName(Constants.BIOTRANSACTION_STATUS_INITIAL_QUANTITY);
		bioTransaction.setStatus(initialStatus);	//ensure that the initial transaction can be identified
		iBioTransactionDao.createBioTransaction(bioTransaction);
		
		// update quantity
		biospecimen.setQuantity(getQuantityAvailable(biospecimen));
		iBiospecimenDao.updateBiospecimen(biospecimen);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#updateBiospecimen(au.org.theark.lims.model.vo.LimsVO)
	 */
	public void updateBiospecimen(LimsVO modelObject) throws ArkSystemException {
		iBiospecimenDao.updateBiospecimen(modelObject.getBiospecimen());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#deleteBiospecimen(au.org.theark.lims.model.vo.LimsVO)
	 */
	public void deleteBiospecimen(LimsVO modelObject) {
		log.debug("Deleting Biospecimen");
		
		// Need to set the InvCell reference to null (if it had one)
		Biospecimen biospecimen = modelObject.getBiospecimen();
		InvCell invCell = iInventoryDao.getInvCellByBiospecimen(biospecimen);
		if (invCell != null && invCell.getId() != null) {
			// must be a real InvCell
			invCell.setBiospecimen(null);
			iInventoryDao.updateInvCell(invCell);
		}
		iBiospecimenDao.deleteBiospecimen(biospecimen);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#getBioTransaction(Long)
	 */
	public BioTransaction getBioTransaction(Long id) throws EntityNotFoundException, ArkSystemException {
		return iBioTransactionDao.getBioTransaction(id);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#getBioTransactionCount(Long)
	 */
	public long getBioTransactionCount(BioTransaction bioTransaction) {
		return iBioTransactionDao.getBioTransactionCount(bioTransaction);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#searchPageableBioTransaction(Long)
	 */
	public List<BioTransaction> searchPageableBioTransactions(BioTransaction bioTransaction, int first, int count) {
		return iBioTransactionDao.searchPageableBioTransactions(bioTransaction, first, count);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#createBioTransaction(au.org.theark.lims.model.vo.LimsVO)
	 */
	public void createBioTransaction(LimsVO modelObject) {
		iBioTransactionDao.createBioTransaction(modelObject.getBioTransaction());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#deleteBioTransaction(au.org.theark.lims.model.vo.LimsVO)
	 */
	public void deleteBioTransaction(BioTransaction bioTransaction) {
		iBioTransactionDao.deleteBioTransaction(bioTransaction);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#updateBioTransaction(au.org.theark.lims.model.vo.LimsVO)
	 */
	public void updateBioTransaction(LimsVO modelObject) {
		iBioTransactionDao.updateBioTransaction(modelObject.getBioTransaction());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#getSampleTypes()
	 */
	public List<BioSampletype> getBioSampleTypes() {
		return iBioCollectionDao.getSampleTypes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#hasBioCollections()
	 */
	public Boolean hasBioCollections(LinkSubjectStudy linkSubjectStudy) {
		return iBioCollectionDao.hasBioCollections(linkSubjectStudy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#hasBiospecimens(BioCollection bioCollection)
	 */
	public Boolean hasBiospecimens(BioCollection bioCollection) {
		return iBioCollectionDao.hasBiospecimens(bioCollection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#getBioCollectionCount(BioCollection bioCollection)
	 */
	public long getBioCollectionCount(BioCollection bioCollectionCriteria) {
		return iBioCollectionDao.getBioCollectionCount(bioCollectionCriteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#searchPageableBioCollections()
	 */
	public List<BioCollection> searchPageableBioCollections(BioCollection bioCollectionCriteria, int first, int count) {
		return iBioCollectionDao.searchPageableBioCollections(bioCollectionCriteria, first, count);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#getBiospecimenCount()
	 */
	public long getBiospecimenCount(Biospecimen biospecimenCriteria) {
		return iBiospecimenDao.getBiospecimenCount(biospecimenCriteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#searchPageableBiospecimens()
	 */
	public List<Biospecimen> searchPageableBiospecimens(Biospecimen biospecimenCriteria, int first, int count) {
		return iBiospecimenDao.searchPageableBiospecimens(biospecimenCriteria, first, count);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#getBiospecimenByUid()
	 */
	public Biospecimen getBiospecimenByUid(String biospecimenUid, Study study) {
		return iBiospecimenDao.getBiospecimenByUid(biospecimenUid, study);
	}
	
	public long getBioCollectionCustomFieldDataCount(BioCollection criteria, ArkFunction arkFunction) {
		return iBioCollectionDao.getBioCollectionCustomFieldDataCount(criteria, arkFunction);
	}

	public List<BioCollectionCustomFieldData> getBioCollectionCustomFieldDataList(BioCollection bioCollectionCriteria, ArkFunction arkFunction, int first, int count) {
		List<BioCollectionCustomFieldData> customfieldDataList = new ArrayList<BioCollectionCustomFieldData>();
		customfieldDataList  = iBioCollectionDao.getBioCollectionCustomFieldDataList(bioCollectionCriteria, arkFunction, first, count);
		return customfieldDataList;
	}
	
	public BioCollectionCustomFieldData getBioCollectionCustomFieldData(BioCollection bioCollectionCriteria, ArkFunction arkFunction, String customFieldName) {
		return iBioCollectionDao.getBioCollectionCustomFieldData(bioCollectionCriteria, arkFunction, customFieldName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.lims.service.ILimsService#createOrUpdateBioCollectionCustomFieldData(List<BioCollectionCustomFieldData> bioCollectionCFDataList)
	 */
	public List<BioCollectionCustomFieldData> createOrUpdateBioCollectionCustomFieldData(List<BioCollectionCustomFieldData> bioCollectionCFDataList) {

		List<BioCollectionCustomFieldData> listOfExceptions = new ArrayList<BioCollectionCustomFieldData>();
		/* Iterate the list and call DAO to persist each Item */
		for (BioCollectionCustomFieldData bioCollectionCFData : bioCollectionCFDataList) {
			
			
			try {
			/* Insert the Field if it does not have a  ID and has the required fields */
				if (canInsert(bioCollectionCFData)) {
		
					iBioCollectionDao.createBioCollectionCustomFieldData(bioCollectionCFData);
					Long id = bioCollectionCFData.getCustomFieldDisplay().getCustomField().getId();
					
					CustomField customField = arkCommonService.getCustomField(id);
					customField.setCustomFieldHasData(true);
					CustomFieldVO customFieldVO = new CustomFieldVO();
					customFieldVO.setCustomField(customField);
					
					arkCommonService.updateCustomField(customFieldVO);

				}
				else if (canUpdate(bioCollectionCFData)) {
					
					//If there was bad data uploaded and the user has now corrected it on the front end then set/blank out the error data value and updated the record.
					if (bioCollectionCFData.getErrorDataValue() != null) {
						bioCollectionCFData.setErrorDataValue(null);
					} 
					iBioCollectionDao.updateBioCollectionCustomFieldData(bioCollectionCFData);
				
				}
				else if (canDelete(bioCollectionCFData)) {
					//Check if the CustomField is used by anyone else and if not set the customFieldHasData to false;
					Long count  = iBioCollectionDao.isCustomFieldUsed(bioCollectionCFData);
					
					iBioCollectionDao.deleteBioCollectionCustomFieldData(bioCollectionCFData);
					if(count <= 1) {
						//Then update the CustomField's hasDataFlag to false;
						Long id = bioCollectionCFData.getCustomFieldDisplay().getCustomField().getId();//Reload since the session was closed in the front end and the child objects won't be lazy loaded
						CustomField customField = arkCommonService.getCustomField(id);
						customField.setCustomFieldHasData(false);
						CustomFieldVO customFieldVO = new CustomFieldVO();
						customFieldVO.setCustomField(customField);
						arkCommonService.updateCustomField(customFieldVO); //Update it
					}
				}
			}
			catch (Exception someException) {
				listOfExceptions.add(bioCollectionCFData);//Continue with rest of the list
				someException.printStackTrace();
			}
		}
		
		return listOfExceptions;
	}
	
	
	/**
	 * In order to delete it must satisfy the following conditions
	 * 1. BioCollectionCustomFieldData must be a persistent entity(with a valid primary key/ID) AND
	 * 2. BioCollectionCustomFieldData should have a valid BioCollection linked to it and must not be null AND
	 * 3. BioCollectionCustomFieldData.TextDataValue is NULL OR is EMPTY
	 * 4. BioCollectionCustomFieldData.NumberDataValue is NULL
	 * 5. BioCollectionCustomFieldData.DatewDataValue is NULL
	 * When these conditions are satisfied this method will return Boolean TRUE
	 * @param bioCollectionCFData
	 * @return
	 */
	private Boolean canDelete(BioCollectionCustomFieldData bioCollectionCFData){
		Boolean flag = false;
		
		if(bioCollectionCFData.getId() != null &&  bioCollectionCFData.getBioCollection() != null && 
				( bioCollectionCFData.getTextDataValue() == null  	||		
				  bioCollectionCFData.getTextDataValue().isEmpty()  	|| 
				  bioCollectionCFData.getNumberDataValue() == null 	||
				  bioCollectionCFData.getDateDataValue() == null ) ){
			
			flag=true;
			
		}
		return flag;
	}
	
	/**
	 * In order to Update a BioCollectionCustomFieldData instance the following conditions must be met
	 * 1. BioCollectionCustomFieldData must be a persistent entity(with a valid primary key/ID) AND
	 * 2. BioCollectionCustomFieldData should have a valid BioCollection linked to it and must not be null AND
	 * 3. BioCollectionCustomFieldData.TextDataValue is NOT NULL AND NOT EMPTY OR
	 * 4. BioCollectionCustomFieldData.NumberDataValue is NOT NULL
	 * 5. BioCollectionCustomFieldData.DateDataValue is NOT NULL	
	 * When these conditions are satisfied the method will return Boolean TRUE
	 * @param bioCollectionCFData
	 * @return
	 */
	private Boolean canUpdate(BioCollectionCustomFieldData bioCollectionCFData){
		Boolean flag = false;
		
		if(bioCollectionCFData.getId() != null && bioCollectionCFData.getBioCollection() != null && 
				(( bioCollectionCFData.getTextDataValue() != null 	&& 
				   !bioCollectionCFData.getTextDataValue().isEmpty()) || 
				   bioCollectionCFData.getDateDataValue() != null  	|| 
				   bioCollectionCFData.getNumberDataValue() != null) ){
			
			flag=true;
			
		}
		return flag;
	}
	
	/**
	 * In order to Insert a BioCollectionCustomFieldData instance the following conditions must be met.
	 * 1. BioCollectionCustomFieldData must be a transient entity(Not yet associated with an ID/PK) AND
	 * 2. BioCollectionCustomFieldData should have a valid BioCollection linked to it and must not be null AND
	 * 3. BioCollectionCustomFieldData.TextDataValue is NOT NULL  OR
	 * 4. BioCollectionCustomFieldData.NumberDataValue is NOT NULL OR
	 * 5. BioCollectionCustomFieldData.DateDataValue is NOT NULL	
	 * When these conditions are satisfied the method will return Boolean TRUE
	 * @param bioCollectionCFData
	 * @return
	 */
	private Boolean canInsert(BioCollectionCustomFieldData bioCollectionCFData){
		Boolean flag = false;
		
		if(bioCollectionCFData.getId() == null &&  bioCollectionCFData.getBioCollection() != null && 
				(		bioCollectionCFData.getNumberDataValue() != null || 
						bioCollectionCFData.getTextDataValue() != null 	|| 
						bioCollectionCFData.getDateDataValue() != null )){
			
			flag=true;
			
		}
		return flag;
	}

	public long getBiospecimenCount(LimsVO limsVo) {
		return iBiospecimenDao.getBiospecimenCount(limsVo);
	}

	public List<Biospecimen> searchPageableBiospecimens(LimsVO limsVo, int first, int count) {
		return iBiospecimenDao.searchPageableBiospecimens(limsVo, first, count);
	}

	public long getBiospecimenCustomFieldDataCount(Biospecimen biospecimenCriteria, ArkFunction arkFunction) {
		return iBiospecimenDao.getBiospecimenCustomFieldDataCount(biospecimenCriteria, arkFunction);
	}

	public List<BiospecimenCustomFieldData> getBiospecimenCustomFieldDataList(Biospecimen biospecimenCriteria, ArkFunction arkFunction, int first, int count) {
		List<BiospecimenCustomFieldData> customfieldDataList = new ArrayList<BiospecimenCustomFieldData>();
		customfieldDataList  = iBiospecimenDao.getBiospecimenCustomFieldDataList(biospecimenCriteria, arkFunction, first, count);
		return customfieldDataList;
	}

	public List<BiospecimenCustomFieldData> createOrUpdateBiospecimenCustomFieldData(List<BiospecimenCustomFieldData> biospecimenCFDataList) {
		List<BiospecimenCustomFieldData> listOfExceptions = new ArrayList<BiospecimenCustomFieldData>();
		/* Iterate the list and call DAO to persist each Item */
		for (BiospecimenCustomFieldData biospecimanCFData : biospecimenCFDataList) {
			
			
			try {
			/* Insert the Field if it does not have a  ID and has the required fields */
				if (canInsert(biospecimanCFData)) {
		
					iBiospecimenDao.createBiospecimenCustomFieldData(biospecimanCFData);
					Long id = biospecimanCFData.getCustomFieldDisplay().getCustomField().getId();
					
					CustomField customField = arkCommonService.getCustomField(id);
					customField.setCustomFieldHasData(true);
					CustomFieldVO customFieldVO = new CustomFieldVO();
					customFieldVO.setCustomField(customField);
					
					arkCommonService.updateCustomField(customFieldVO);

				}
				else if (canUpdate(biospecimanCFData)) {
					
					//If there was bad data uploaded and the user has now corrected it on the front end then set/blank out the error data value and updated the record.
					if (biospecimanCFData.getErrorDataValue() != null) {
						biospecimanCFData.setErrorDataValue(null);
					} 
					iBiospecimenDao.updateBiospecimenCustomFieldData(biospecimanCFData);
				
				}
				else if (canDelete(biospecimanCFData)) {
					//Check if the CustomField is used by anyone else and if not set the customFieldHasData to false;
					Long count  = iBiospecimenDao.isCustomFieldUsed(biospecimanCFData);
					
					iBiospecimenDao.deleteBiospecimenCustomFieldData(biospecimanCFData);
					if(count <= 1) {
						//Then update the CustomField's hasDataFlag to false;
						Long id = biospecimanCFData.getCustomFieldDisplay().getCustomField().getId();//Reload since the session was closed in the front end and the child objects won't be lazy loaded
						CustomField customField = arkCommonService.getCustomField(id);
						customField.setCustomFieldHasData(false);
						CustomFieldVO customFieldVO = new CustomFieldVO();
						customFieldVO.setCustomField(customField);
						arkCommonService.updateCustomField(customFieldVO); //Update it
					}
				}
			}
			catch (Exception someException) {
				listOfExceptions.add(biospecimanCFData);//Continue with rest of the list
				someException.printStackTrace();
			}
		}
		
		return listOfExceptions;
	}

	/**
	 * In order to delete it must satisfy the following conditions
	 * 1. BiospecimenCustomFieldData must be a persistent entity(with a valid primary key/ID) AND
	 * 2. BiospecimenCustomFieldData should have a valid Biospecimen linked to it and must not be null AND
	 * 3. BiospecimenCustomFieldData.TextDataValue is NULL OR is EMPTY
	 * 4. BiospecimenCustomFieldData.NumberDataValue is NULL
	 * 5. BiospecimenCustomFieldData.DatewDataValue is NULL
	 * When these conditions are satisfied this method will return Boolean TRUE
	 * @param biospecimenCFData
	 * @return
	 */
	private Boolean canDelete(BiospecimenCustomFieldData biospecimenCFData){
		Boolean flag = false;
		
		if(biospecimenCFData.getId() != null &&  biospecimenCFData.getBiospecimen() != null && 
				( biospecimenCFData.getTextDataValue() == null  	||		
				  biospecimenCFData.getTextDataValue().isEmpty()  	|| 
				  biospecimenCFData.getNumberDataValue() == null 	||
				  biospecimenCFData.getDateDataValue() == null ) ){
			
			flag=true;
			
		}
		return flag;
	}
	
	/**
	 * In order to Update a BioCollectionCustomFieldData instance the following conditions must be met
	 * 1. BiospecimenCustomFieldData must be a persistent entity(with a valid primary key/ID) AND
	 * 2. BiospecimenCustomFieldData should have a valid Biospecimen linked to it and must not be null AND
	 * 3. BiospecimenCustomFieldData.TextDataValue is NOT NULL AND NOT EMPTY OR
	 * 4. BiospecimenCustomFieldData.NumberDataValue is NOT NULL
	 * 5. BiospecimenCustomFieldData.DateDataValue is NOT NULL	
	 * When these conditions are satisfied the method will return Boolean TRUE
	 * @param biospecimenCFData
	 * @return
	 */
	private Boolean canUpdate(BiospecimenCustomFieldData biospecimenCFData){
		Boolean flag = false;
		
		if(biospecimenCFData.getId() != null && biospecimenCFData.getBiospecimen() != null && 
				(( biospecimenCFData.getTextDataValue() != null 	&& 
				   !biospecimenCFData.getTextDataValue().isEmpty()) || 
				   biospecimenCFData.getDateDataValue() != null  	|| 
				   biospecimenCFData.getNumberDataValue() != null) ){
			
			flag=true;
			
		}
		return flag;
	}
	
	/**
	 * In order to Insert a BioCollectionCustomFieldData instance the following conditions must be met.
	 * 1. BiospecimenCustomFieldData must be a transient entity(Not yet associated with an ID/PK) AND
	 * 2. BiospecimenCustomFieldData should have a valid Biospecimen linked to it and must not be null AND
	 * 3. BiospecimenCustomFieldData.TextDataValue is NOT NULL  OR
	 * 4. BiospecimenCustomFieldData.NumberDataValue is NOT NULL OR
	 * 5. BiospecimenCustomFieldData.DateDataValue is NOT NULL	
	 * When these conditions are satisfied the method will return Boolean TRUE
	 * @param biospecimenCFData
	 * @return
	 */
	private Boolean canInsert(BiospecimenCustomFieldData biospecimenCFData){
		Boolean flag = false;
		
		if(biospecimenCFData.getId() == null &&  biospecimenCFData.getBiospecimen() != null && 
				(		biospecimenCFData.getNumberDataValue() != null || 
						biospecimenCFData.getTextDataValue() != null 	|| 
						biospecimenCFData.getDateDataValue() != null )){
			
			flag=true;
			
		}
		return flag;
	}

	public List<Unit> getUnits() {
		return iBiospecimenDao.getUnits();
	}

	public List<TreatmentType> getTreatmentTypes() {
		return iBioTransactionDao.getTreatmentTypes();
	}

	public List<BioTransactionStatus> getBioTransactionStatusChoices() {
		return iBioTransactionDao.getBioTransactionStatusChoices();
	}

	public BioTransactionStatus getBioTransactionStatusByName(String statusName) {
		return iBioTransactionDao.getBioTransactionStatusByName(statusName);
	}

	public Double getQuantityAvailable(Biospecimen biospecimen){
		return iBiospecimenDao.getQuantityAvailable(biospecimen);
	}

	public List<BiospecimenAnticoagulant> getBiospecimenAnticoagulantList() {
		return iBiospecimenDao.getBiospecimenAnticoagulantList();
	}

	public List<BiospecimenGrade> getBiospecimenGradeList() {
		return iBiospecimenDao.getBiospecimenGradeList();
	}

	public List<BiospecimenQuality> getBiospecimenQualityList() {
		return iBiospecimenDao.getBiospecimenQualityList();
	}

	public List<BiospecimenStatus> getBiospecimenStatusList() {
		return iBiospecimenDao.getBiospecimenStatusList();
	}

	public List<BiospecimenStorage> getBiospecimenStorageList() {
		return iBiospecimenDao.getBiospecimenStorageList();
	}
	
	public String getNextGeneratedBiospecimenUID(Study study) {
		return iBiospecimenDao.getNextGeneratedBiospecimenUID(study);
	}

	public void batchInsertBiospecimens(Collection<Biospecimen> insertBiospecimens) {
		iBiospecimenDao.batchInsertBiospecimens(insertBiospecimens);
	}

	public void batchUpdateBiospecimens(Collection<Biospecimen> updateBiospecimens) {
		iBiospecimenDao.batchUpdateBiospecimens(updateBiospecimens);
	}

	public void batchUpdateInvCells(List<InvCell> updateInvCells) {
		iInventoryDao.batchUpdateInvCells(updateInvCells);
	}

	public BioSampletype getBioSampleTypeByName(String name) {
		return iBiospecimenDao.getBioSampleTypeByName(name);
	}

	public TreatmentType getTreatmentTypeByName(String name) {
		return iBiospecimenDao.getTreatmentTypeByName(name);
	}

	public StringBuffer uploadAndReportMatrixBiospecimenFile(Study study, InputStream inputStream, long size, String fileFormat, char delimiterChar) {
		StringBuffer uploadReport = null;
		BiospecimenUploader biospecimenUploader = new BiospecimenUploader(study, arkCommonService, this, iInventoryService);
		
		try {
			log.debug("Importing and reporting Biospecimen file");
			uploadReport = biospecimenUploader.uploadAndReportMatrixBiospecimenFile(inputStream, size, fileFormat, delimiterChar);
		}
		catch (FileFormatException ffe) {
			log.error(au.org.theark.core.Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(au.org.theark.core.Constants.ARK_BASE_EXCEPTION + abe);
		}
		return uploadReport;
	}

	public BioCollection getBioCollectionByUID(String biocollectionUid,Long studyId) {
		return iBioCollectionDao.getBioCollectionByUID(biocollectionUid,studyId);
	}

	public Unit getUnitByName(String name) {
		return iBiospecimenDao.getUnitByName(name);
	}

	public List<Biospecimen> getBiospecimenByBioCollection(BioCollection bioCollection) {
		return iBiospecimenDao.getBiospecimenByBioCollection(bioCollection);
	}

	public List<String> getAllBiospecimenUIDs(Study study){
		return iBiospecimenDao.getAllBiospecimenUIDs(study);
	}
	
	public List<String> getAllBiocollectionUIDs(Study study){
		return iBioCollectionDao.getAllBiocollectionUIDs(study);
	}

	public List<AccessRequest> getAccessRequests() {
		return iBioTransactionDao.getAccessRequests();
	}
	
	public void batchAliquotBiospecimens(List<Biospecimen> biospecimenList) {
		iBiospecimenDao.batchAliquotBiospecimens(biospecimenList);		
	}

	public List<BiospecimenProtocol> getBiospecimenProtocolList() {
		return iBiospecimenDao.getBiospecimenProtocolList();
	}
}
