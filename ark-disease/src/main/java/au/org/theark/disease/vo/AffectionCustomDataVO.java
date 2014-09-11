package au.org.theark.disease.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import au.org.theark.core.model.disease.entity.AffectionCustomFieldData;
import au.org.theark.core.web.component.customfield.dataentry.CustomDataVO;

public class AffectionCustomDataVO extends CustomDataVO<AffectionCustomFieldData> implements Serializable {

	private static final long	serialVersionUID	= 1L;

	public AffectionCustomDataVO() {
		super();
	}
	
	public AffectionCustomDataVO(Collection<AffectionCustomFieldData> data) {
		super();
		setCustomFieldDataList(new ArrayList<AffectionCustomFieldData>(data));
	}
	
}
