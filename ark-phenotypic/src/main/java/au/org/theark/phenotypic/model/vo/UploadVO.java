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
package au.org.theark.phenotypic.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.wicket.markup.html.form.upload.FileUpload;

//import au.org.theark.core.model.pheno.entity.FieldUpload;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
//import au.org.theark.core.model.pheno.entity.PhenoCollectionUpload;
//import au.org.theark.core.model.pheno.entity.Upload;

/**
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public class UploadVO implements Serializable {/*
//	private Upload								upload;
	private FileUpload								fileUpload;
	private PhenoCollection							phenoCollection;
//	private PhenoCollectionUpload					phenoCollectionUpload;
	private java.util.Collection<PhenoUpload>	uploadCollection;
/	private int											mode;
	private java.util.Collection<String>		validationMessages;
	private Boolean									overrideDataValidationChkBox;
	private Boolean									updateChkBox;
	private java.util.Collection<FieldUpload>	fieldUploadCollection;

	public UploadVO() {
		upload = new PhenoUpload();
		setUploadCollection(new ArrayList<PhenoUpload>());
		setFieldUploadCollection(new ArrayList<FieldUpload>());
	}

	/**
	 * @return the upload
	 *
	public PhenoUpload getUpload() {
		return upload;
	}

	/**
	 * @param field
	 *           the field to set
	 *
	public void setUpload(PhenoUpload upload) {
		this.upload = upload;
	}

	/**
	 * @param uploadCollection
	 *           the uploadCollection to set
	 *
	public void setUploadCollection(java.util.Collection<PhenoUpload> uploadCollection) {
		this.uploadCollection = uploadCollection;
	}

	/**
	 * @return the uploadCollection
	 *
	public java.util.Collection<PhenoUpload> getUploadCollection() {
		return uploadCollection;
	}

	/**
	 * @return the phenoCollection
	 *
	public PhenoCollection getPhenoCollection() {
		return phenoCollection;
	}

	/**
	 * @param phenoCollection
	 *           the phenoCollection to set
	 *
	public void setPhenoCollection(PhenoCollection phenoCollection) {
		this.phenoCollection = phenoCollection;
	}

	/**
	 * @param phenoCollectionUpload
	 *           the phenoCollectionUpload to set
	 *
	public void setPhenoCollectionUpload(PhenoCollectionUpload phenoCollectionUpload) {
		this.phenoCollectionUpload = phenoCollectionUpload;
	}

	/**
	 * @return the phenoCollectionUpload
	 *
	public PhenoCollectionUpload getPhenoCollectionUpload() {
		return phenoCollectionUpload;
	}

	/**
	 * @param mode
	 *           the mode to set
	 *
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * @return the mode
	 *
	public int getMode() {
		return mode;
	}

	/**
	 * @param validationMessages
	 *           the validationMessages to set
	 *
	public void setValidationMessages(java.util.Collection<String> validationMessages) {
		this.validationMessages = validationMessages;
	}

	/**
	 * @return the validationMessages
	 *
	public java.util.Collection<String> getValidationMessages() {
		return validationMessages;
	}

	/**
	 * @return the validationMessages
	 *
	public String getValidationMessagesAsString() {
		StringBuffer stringBuffer = new StringBuffer("");
		java.util.Collection<String> msgs = getValidationMessages();

		if (getValidationMessages() != null) {
			for (Iterator<String> iterator = msgs.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				stringBuffer.append(string);
				stringBuffer.append("\n");
			}
		}
		return stringBuffer.toString();
	}

	/**
	 * @param overrideDataValidationChkBox
	 *           the overrideDataValidationChkBox to set
	 *
	public void setOverrideDataValidationChkBox(Boolean overrideDataValidationChkBox) {
		this.overrideDataValidationChkBox = overrideDataValidationChkBox;
	}

	/**
	 * @return the overrideDataValidationChkBox
	 *
	public Boolean getOverrideDataValidationChkBox() {
		return overrideDataValidationChkBox;
	}

	public void setFileUpload(FileUpload fileUpload) {
		this.fileUpload = fileUpload;
	}

	public FileUpload getFileUpload() {
		return fileUpload;
	}

	/**
	 * @param updateChkBox
	 *           the updateChkBox to set
	 *
	public void setUpdateChkBox(Boolean updateChkBox) {
		this.updateChkBox = updateChkBox;
	}

	/**
	 * @return the updateChkBox
	 *
	public Boolean getUpdateChkBox() {
		return updateChkBox;
	}

	public void setFieldUploadCollection(java.util.Collection<FieldUpload> fieldUploadCollection) {
		this.fieldUploadCollection = fieldUploadCollection;
	}

	public java.util.Collection<FieldUpload> getFieldUploadCollection() {
		return fieldUploadCollection;
	}
	*/
}
