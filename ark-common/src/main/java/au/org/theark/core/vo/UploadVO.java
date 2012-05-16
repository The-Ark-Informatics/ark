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
package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.html.form.upload.FileUpload;

import au.org.theark.core.model.study.entity.FileFormat;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyUpload;

/**
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public class UploadVO implements Serializable {
	private StudyUpload								upload;
	private List<String>								uidsToUpload;
	private FileFormat								fileFormat;
	private String										uploadType;//TODO could create type/enum/ref table
	private FileUpload								fileUpload;
	private Study										study;
	private java.util.Collection<StudyUpload>	uploadCollection;
	private int											mode;
	private java.util.Collection<String>		validationMessages;
	private Boolean									updateChkBox;

	public UploadVO() {
		upload = new StudyUpload();
		setUploadCollection(new ArrayList<StudyUpload>());
	}

	/**
	 * @return the upload
	 */
	public StudyUpload getUpload() {
		return upload;
	}

	/**
	 * @param field
	 *           the field to set
	 */
	public void setUpload(StudyUpload upload) {
		this.upload = upload;
	}

	/**
	 * @param uploadCollection
	 *           the uploadCollection to set
	 */
	public void setUploadCollection(java.util.Collection<StudyUpload> uploadCollection) {
		this.uploadCollection = uploadCollection;
	}

	/**
	 * @return the uploadCollection
	 */
	public java.util.Collection<StudyUpload> getUploadCollection() {
		return uploadCollection;
	}

	/**
	 * @param fileFormat
	 *           the fileFormat to set
	 */
	public void setFileFormat(FileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}

	/**
	 * @return the fileFormat
	 */
	public FileFormat getFileFormat() {
		return fileFormat;
	}

	/**
	 * @param mode
	 *           the mode to set
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * @param validationMessages
	 *           the validationMessages to set
	 */
	public void setValidationMessages(java.util.Collection<String> validationMessages) {
		this.validationMessages = validationMessages;
	}

	/**
	 * @return the validationMessages
	 */
	public java.util.Collection<String> getValidationMessages() {
		return validationMessages;
	}

	/**
	 * @return the validationMessages
	 */
	public String getValidationMessagesAsString() {
		StringBuffer stringBuffer = new StringBuffer("");
		java.util.Collection<String> msgs = getValidationMessages();

		if (msgs != null) {
			for (Iterator<String> iterator = msgs.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				stringBuffer.append(string);
				stringBuffer.append("\n");
			}
		}
		return stringBuffer.toString();
	}

	/**
	 * @param study
	 *           the study to set
	 */
	public void setStudy(Study study) {
		this.study = study;
	}

	/**
	 * @return the study
	 */
	public Study getStudy() {
		return study;
	}

	/**
	 * @param updateChkBox
	 *           the updateChkBox to set
	 */
	public void setUpdateChkBox(Boolean updateChkBox) {
		this.updateChkBox = updateChkBox;
	}

	/**
	 * @return the updateChkBox
	 */
	public Boolean getUpdateChkBox() {
		return updateChkBox;
	}

	/**
	 * @param fileUpload
	 *           the fileUpload to set
	 */
	public void setFileUpload(FileUpload fileUpload) {
		this.fileUpload = fileUpload;
	}

	/**
	 * @return the fileUpload
	 * 
	 * TODO : see java docs re this class;
	 " Model for file uploads. Objects of this class should not be kept between requests, and should
  therefore be marked as <code>transient</code> if they become a property of an IModel."
  Do we need to analyze what this is doing
	 */
	public FileUpload getFileUpload() {
		return fileUpload;
	}

	public void setUidsToUpload(List<String> uidsToUpload) {
		this.uidsToUpload = uidsToUpload;
	}

	public List<String> getUidsToUpload() {
		return uidsToUpload;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

	public String getUploadType() {
		return uploadType;
	}
}
