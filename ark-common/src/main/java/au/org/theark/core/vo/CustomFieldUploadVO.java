package au.org.theark.core.vo;

import java.io.File;
import java.util.ArrayList;

import au.org.theark.core.model.study.entity.CustomFieldCategoryUpload;
import au.org.theark.core.model.study.entity.CustomFieldUpload;

public class CustomFieldUploadVO extends UploadVO {

	private static final long	serialVersionUID	= -3761087990900803427L;

	private java.util.Collection<CustomFieldUpload>	customFieldUploadCollection = new ArrayList<CustomFieldUpload>();
	
	private java.util.Collection<CustomFieldCategoryUpload>	customFieldUploadCategoryCollection = new ArrayList<CustomFieldCategoryUpload>();
	private File tempFile = null;

	public void setCustomFieldUploadCollection(java.util.Collection<CustomFieldUpload> customFieldUploadCollection) {
		this.customFieldUploadCollection = customFieldUploadCollection;
	}

	public java.util.Collection<CustomFieldUpload> getCustomFieldUploadCollection() {
		return customFieldUploadCollection;
	}
	public java.util.Collection<CustomFieldCategoryUpload> getCustomFieldUploadCategoryCollection() {
		return customFieldUploadCategoryCollection;
	}

	public void setCustomFieldUploadCategoryCollection(
			java.util.Collection<CustomFieldCategoryUpload> customFieldUploadCategoryCollection) {
		this.customFieldUploadCategoryCollection = customFieldUploadCategoryCollection;
	}

	public void setTempFile(File tempFile) {
		this.tempFile = tempFile;
	}

	public File getTempFile() {
		return tempFile;
	}
	
}
