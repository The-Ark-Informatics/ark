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
package au.org.theark.geno.model.dao;

import java.util.Collection;
import java.util.List;

import au.org.theark.core.model.geno.entity.CollectionImport;
import au.org.theark.core.model.geno.entity.DelimiterType;
import au.org.theark.core.model.geno.entity.FileFormat;
import au.org.theark.core.model.geno.entity.GenoCollection;
import au.org.theark.core.model.geno.entity.MetaData;
import au.org.theark.core.model.geno.entity.MetaDataField;
import au.org.theark.core.model.geno.entity.MetaDataType;
import au.org.theark.core.model.geno.entity.Status;
import au.org.theark.core.model.geno.entity.Upload;
import au.org.theark.core.model.geno.entity.UploadCollection;

public interface ICollectionDao {

	// Create
	public void createCollection(GenoCollection col);
	public void createMetaData(MetaData metaData);
	public void createCollectionImport(CollectionImport colImport);
	public void createMetaDataField(MetaDataField mdf);
	public void createUploadCollection(UploadCollection upload);

	// Read
	public List<GenoCollection> getCollectionMatches(GenoCollection colExample);
	public Collection<Status> getStatusCollection();
	public Collection<FileFormat> getFileFormatCollection();
	public Collection<DelimiterType> getDelimiterTypeCollection();
	public GenoCollection getCollection(Long id);
	public Status getStatusByName(String statusName);
	//public MetaDataField getMetaDataFieldByName(Long studyId, String mdfName);
	public MetaDataField getMetaDataField(Long metaDataFieldId);
	public MetaDataType getMetaDataTypeByName(String typeName);
    //TODO: Patch the Long studyId with a true study object?
    //(it should already be in context)
	public Collection<UploadCollection> getFileUploadMatches(
			UploadCollection uploadCollectionCriteria);

	// Update
	public void updateCollection(GenoCollection colEntity);
	public void updateMetaData(MetaData mdEntity);
	public void updateMetaDataField(MetaDataField mdf);
	
	// Delete
	public void deleteCollection(GenoCollection col);
	public void deleteUploadCollection(UploadCollection uploadCollection);

}
