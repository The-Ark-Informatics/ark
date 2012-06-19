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
package au.org.theark.phenotypic.service;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.DelimiterType;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.pheno.entity.PhenoData;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.pheno.entity.QuestionnaireStatus;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.FileFormat;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.Upload;
import au.org.theark.core.util.BarChartResult;
import au.org.theark.core.vo.CustomFieldGroupVO;
import au.org.theark.core.vo.PhenoDataCollectionVO;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.model.vo.UploadVO;

public interface IPhenotypicService {

	public java.util.Collection<PhenoCollection> getPhenoCollectionByStudy(Study study);

//	public java.util.Collection<PhenoCollection> searchPhenoCollection(PhenoCollection phenoCollection);
//
//	public PhenoCollectionVO getPhenoCollectionAndFields(Long id);

	public void createCollection(PhenoCollection col);

	//public void createCollection(PhenoCollectionVO colVo);

	public void updateCollection(PhenoCollection col);

	//public void updateCollection(PhenoCollectionVO colVo);

	public void deleteCollection(PhenoCollection col) throws ArkSystemException, EntityCannotBeRemoved;

	//public void deleteCollection(PhenoCollectionVO colVo);

	//public int clearPhenoCollection(PhenoCollection phenoCollection);

	public boolean phenoCollectionHasData(PhenoCollection phenoCollection);

	// Upload phenotypic data file
	//public void uploadPhenotypicDataFile(org.apache.wicket.util.file.File file, String fileFormat, char delimiterChar);

	//public StringBuffer uploadAndReportPhenotypicDataFile(org.apache.wicket.util.file.File file, String fileFormat, char delimiterChar);

	//public void uploadPhenotypicDataFile(InputStream inputStream, String fileFormat, char delimiterChar);

	//public StringBuffer uploadAndReportPhenotypicDataFile(InputStream inputStream, String fileFormat, char delimiterChar);

//	public StringBuffer uploadAndReportPhenotypicDataFile(UploadVO uploadVo);

	//public PhenoCollectionVO getPhenoCollectionAndUploads(Long id);

	//public PhenoCollectionVO getPhenoCollectionAndUploads(PhenoCollection phenoCollection);

	//public PhenoUpload getUpload(Long id);

	//blic void createUpload(PhenoUpload upload);

	//public void updateUpload(PhenoUpload upload);

	//public void deleteUpload(PhenoUpload upload) throws ArkSystemException, EntityCannotBeRemoved;

	// Delimiter Type
	public Collection<DelimiterType> getDelimiterTypes();

	//public long getCountOfFieldsInStudy(Study study);

	//public long getCountOfFieldsWithDataInStudy(Study study);

	public long getCountOfCollectionsInStudy(Study study);

	public long getCountOfCollectionsWithDataInStudy(Study study);

	/**
	 * A generic interface that will return a list PhenoCollectionVO specified by a particular criteria, and a paginated reference point
	 * 
	 * @return Collection of PhenoCollectionVO
	 */
	//public List<PhenoCollectionVO> searchPageableFieldData(PhenoCollectionVO phenoCollectionVoCriteria, int first, int count);

	/**
	 * A generic interface that will return count of the fieldData's in the study
	 * 
	 * @return int
	 */
	//public long getStudyFieldDataCount(PhenoCollectionVO phenoCollectionVoCriteria);

	public DelimiterType getDelimiterType(Long id);

	//public java.util.List<BarChartResult> getFieldsWithDataResults(Study study);

	public String getDelimiterTypeByDelimiterChar(char phenotypicDelimChr);

	public FileFormat getFileFormatByName(String name);

	public List<PhenoData> createOrUpdatePhenoData(List<PhenoData> customFieldDataList);

	public long getPhenoDataCount(PhenoCollection phenoCollection);

	public List<PhenoData> getPhenoDataList(PhenoCollection phenoCollection, int first, int count);

	public PhenoCollection getPhenoCollection(Long id);
	
	/**
	 * Creates a Custom Field Group and assoicates a list of Custom Fields to it.
	 * @param customFieldGroupVO
	 */
	public void createCustomFieldGroup(CustomFieldGroupVO customFieldGroupVO) throws EntityExistsException, ArkSystemException;

	public void updateCustomFieldGroup(CustomFieldGroupVO customFieldGroupVO) throws EntityExistsException,ArkSystemException;
	
	public void deleteCustomFieldGroup(CustomFieldGroupVO customFieldGroupVO);
	
	public long getPhenoCollectionCount(PhenoDataCollectionVO criteria);

	public List<PhenoCollection> searchPageablePhenoCollections(PhenoDataCollectionVO criteria, int first, int count);

	public List<CustomField> getCustomFieldsLinkedToCustomFieldGroup(CustomFieldGroup customFieldCriteria);

	public void createPhenoCollection(PhenoCollection phenoCollection);

	public void updatePhenoCollection(PhenoCollection phenoCollection);

//	public void deletePhenoCollection(PhenoCollection phenoCollection);
	public void deletePhenoCollection(PhenoCollection phenoCollection) throws ArkSystemException, EntityCannotBeRemoved;
	
	public List<QuestionnaireStatus> getPhenoCollectionStatusList();
	
	public Collection<CustomFieldDisplay> getCFDLinkedToQuestionnaire(CustomFieldGroup customFieldGroup , int first, int count);
	
	public long getCFDLinkedToQuestionnaireCount(CustomFieldGroup customFieldGroup);

	public QuestionnaireStatus getPhenoCollectionStatusByName(String statusName);

	public QuestionnaireStatus getDefaultPhenoCollectionStatus();
	
	public java.util.Collection<Upload> searchUpload(Upload upload);
	
	public void deleteUpload(Upload studyUpload);
	
	public Upload getUpload(Long id);

	/****TODO IMPLEMENT THIS THING AGAIN!****/
	public StringBuffer uploadAndReportCustomDataFile(InputStream inputStream, long size, String fileFormat, char delimiter, Long studyId, List<String> uidsToUpdate, CustomFieldGroup customFieldGroup,
			PhenoCollection phenoCollection);

	/****TODO IMPLEMENT THIS THING AGAIN!****/
	public void refreshUpload(Upload upload);

	public Collection<CustomFieldGroup> getCustomFieldGroupList(Study study);
	
}
