package au.org.theark.phenotypic.model.vo;

import java.util.ArrayList;

import au.org.theark.core.model.study.entity.CustomFieldUpload;
import au.org.theark.core.vo.UploadVO;

public class PhenoFieldUploadVO extends UploadVO {

	private static final long	serialVersionUID	= -3761087990900803427L;

	private java.util.Collection<CustomFieldUpload>	customFieldUploadCollection = new ArrayList<CustomFieldUpload>();

	public void setCustomFieldUploadCollection(java.util.Collection<CustomFieldUpload> customFieldUploadCollection) {
		this.customFieldUploadCollection = customFieldUploadCollection;
	}

	public java.util.Collection<CustomFieldUpload> getCustomFieldUploadCollection() {
		return customFieldUploadCollection;
	}
	
}
