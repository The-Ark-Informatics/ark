package au.org.theark.phenotypic.model.vo;

import java.io.File;
import java.util.ArrayList;

import au.org.theark.core.model.study.entity.CustomFieldCategoryUpload;
import au.org.theark.core.model.study.entity.CustomFieldUpload;
import au.org.theark.core.model.study.entity.PhenoDataSetFieldCategoryUpload;
import au.org.theark.core.model.study.entity.PhenoFieldUpload;
import au.org.theark.core.vo.UploadVO;

public class PhenoDataSetFieldUploadVO extends UploadVO {

	private static final long	serialVersionUID	= -3761087990900803427L;

	//private java.util.Collection<CustomFieldUpload>	customFieldUploadCollection = new ArrayList<CustomFieldUpload>();
	private java.util.Collection<PhenoFieldUpload>	phenoFieldUploadCollection = new ArrayList<PhenoFieldUpload>();
	
	private java.util.Collection<PhenoDataSetFieldCategoryUpload >	phenoFieldUploadCategoryCollection = new ArrayList<PhenoDataSetFieldCategoryUpload>();
	
	//private java.util.Collection<CustomFieldCategoryUpload>	customFieldUploadCategoryCollection = new ArrayList<CustomFieldCategoryUpload>();
	private File tempFile = null;

	
	public java.util.Collection<PhenoFieldUpload> getPhenoFieldUploadCollection() {
		return phenoFieldUploadCollection;
	}

	public void setPhenoFieldUploadCollection(
			java.util.Collection<PhenoFieldUpload> phenoFieldUploadCollection) {
		this.phenoFieldUploadCollection = phenoFieldUploadCollection;
	}

	public java.util.Collection<PhenoDataSetFieldCategoryUpload> getPhenoFieldUploadCategoryCollection() {
		return phenoFieldUploadCategoryCollection;
	}

	public void setPhenoFieldUploadCategoryCollection(
			java.util.Collection<PhenoDataSetFieldCategoryUpload> phenoFieldUploadCategoryCollection) {
		this.phenoFieldUploadCategoryCollection = phenoFieldUploadCategoryCollection;
	}

	public void setTempFile(File tempFile) {
		this.tempFile = tempFile;
	}

	public File getTempFile() {
		return tempFile;
	}
	
}
