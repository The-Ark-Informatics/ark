/**
 * 
 */
package au.org.theark.phenotypic.model.vo;

import java.io.Serializable;
import java.util.ArrayList;

import au.org.theark.phenotypic.model.entity.FileFormat;
import au.org.theark.phenotypic.model.entity.PhenoCollection;
import au.org.theark.phenotypic.model.entity.PhenoCollectionUpload;
import au.org.theark.phenotypic.model.entity.PhenoUpload;

/**
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public class UploadVO implements Serializable
{
	private PhenoUpload								upload;
	private FileFormat								fileFormat;
	private PhenoCollection							phenoCollection;
	private PhenoCollectionUpload					phenoCollectionUpload;
	private java.util.Collection<PhenoUpload>	uploadCollection;
	private int											mode;
	private java.util.Collection<String>		validationMessages;

	public UploadVO()
	{
		upload = new PhenoUpload();
		setUploadCollection(new ArrayList<PhenoUpload>());
	}

	/**
	 * @return the upload
	 */
	public PhenoUpload getUpload()
	{
		return upload;
	}

	/**
	 * @param field
	 *           the field to set
	 */
	public void setUpload(PhenoUpload upload)
	{
		this.upload = upload;
	}

	/**
	 * @param uploadCollection
	 *           the uploadCollection to set
	 */
	public void setUploadCollection(java.util.Collection<PhenoUpload> uploadCollection)
	{
		this.uploadCollection = uploadCollection;
	}

	/**
	 * @return the uploadCollection
	 */
	public java.util.Collection<PhenoUpload> getUploadCollection()
	{
		return uploadCollection;
	}

	/**
	 * @param fileFormat
	 *           the fileFormat to set
	 */
	public void setFileFormat(FileFormat fileFormat)
	{
		this.fileFormat = fileFormat;
	}

	/**
	 * @return the phenoCollection
	 */
	public PhenoCollection getPhenoCollection()
	{
		return phenoCollection;
	}

	/**
	 * @param phenoCollection
	 *           the phenoCollection to set
	 */
	public void setPhenoCollection(PhenoCollection phenoCollection)
	{
		this.phenoCollection = phenoCollection;
	}

	/**
	 * @param phenoCollectionUpload the phenoCollectionUpload to set
	 */
	public void setPhenoCollectionUpload(PhenoCollectionUpload phenoCollectionUpload)
	{
		this.phenoCollectionUpload = phenoCollectionUpload;
	}

	/**
	 * @return the phenoCollectionUpload
	 */
	public PhenoCollectionUpload getPhenoCollectionUpload()
	{
		return phenoCollectionUpload;
	}

	/**
	 * @return the fileFormat
	 */
	public FileFormat getFileFormat()
	{
		return fileFormat;
	}

	/**
	 * @param mode
	 *           the mode to set
	 */
	public void setMode(int mode)
	{
		this.mode = mode;
	}

	/**
	 * @return the mode
	 */
	public int getMode()
	{
		return mode;
	}

	/**
	 * @param validationMessages the validationMessages to set
	 */
	public void setValidationMessages(java.util.Collection<String> validationMessages)
	{
		this.validationMessages = validationMessages;
	}

	/**
	 * @return the validationMessages
	 */
	public java.util.Collection<String> getValidationMessages()
	{
		return validationMessages;
	}
}
