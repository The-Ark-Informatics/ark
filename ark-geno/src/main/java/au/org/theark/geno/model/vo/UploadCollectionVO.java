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
package au.org.theark.geno.model.vo;

import java.io.Serializable;
import java.util.ArrayList;

import au.org.theark.core.model.geno.entity.FileFormat;
import au.org.theark.core.model.geno.entity.GenoCollection;
import au.org.theark.core.model.geno.entity.Upload;
import au.org.theark.core.model.geno.entity.UploadCollection;

/**
 * @author elam
 *
 */
public class UploadCollectionVO implements Serializable {

//	private Upload upload;
	private GenoCollection genoCollection;
	private UploadCollection uploadCollection;
	private FileFormat fileFormat;
	private java.util.Collection<UploadCollection> uploadCollectionList;
	
	
	public UploadCollectionVO() {
//		this.upload = new Upload();
		this.genoCollection = new GenoCollection();
		this.uploadCollection = new UploadCollection();
		this.fileFormat = new FileFormat();
		this.uploadCollectionList = new ArrayList<UploadCollection>();
	}

//	public Upload getUpload() {
//		return upload;
//	}
//	
//	public void setUpload(Upload upload) {
//		this.upload = upload;
//	}

	public void setGenoCollection(GenoCollection genoCollection) {
		this.genoCollection = genoCollection;
	}

	public GenoCollection getGenoCollection() {
		return genoCollection;
	}

	public void setUploadCollection(UploadCollection uploadCollection) {
		this.uploadCollection = uploadCollection;
	}

	public UploadCollection getUploadCollection() {
		return uploadCollection;
	}
	
	public void setFileFormat(FileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}

	public FileFormat getFileFormat() {
		return fileFormat;
	}

	public java.util.Collection<UploadCollection> getUploadCollectionList() {
		return uploadCollectionList;
	}
	
	public void setUploadCollectionList(
			java.util.Collection<UploadCollection> uploadCollectionList) {
		this.uploadCollectionList = uploadCollectionList;
	}
}
