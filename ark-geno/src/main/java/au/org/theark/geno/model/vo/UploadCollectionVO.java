/**
 * 
 */
package au.org.theark.geno.model.vo;

import java.io.Serializable;
import java.util.ArrayList;

import au.org.theark.geno.model.entity.FileFormat;
import au.org.theark.geno.model.entity.GenoCollection;
import au.org.theark.geno.model.entity.Upload;
import au.org.theark.geno.model.entity.UploadCollection;

/**
 * @author elam
 *
 */
public class UploadCollectionVO implements Serializable {

	private Upload upload;
	private GenoCollection genoCollection;
	private UploadCollection uploadCollection;
	private FileFormat fileFormat;
	private java.util.Collection<UploadCollection> uploadCollectionList;
	
	
	public UploadCollectionVO() {
		this.upload = new Upload();
		this.genoCollection = new GenoCollection();
		this.uploadCollection = new UploadCollection();
		this.fileFormat = new FileFormat();
		this.uploadCollectionList = new ArrayList<UploadCollection>();
	}

	public Upload getUpload() {
		return upload;
	}
	
	public void setUpload(Upload upload) {
		this.upload = upload;
	}

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
