package au.org.theark.web.rest.model;

import java.util.List;

public class BinaryCustomFieldsResponse {
	
	private List<String> customFieldNames;

	public List<String> getCustomFieldNames() {
		return customFieldNames;
	}

	public void setCustomFieldNames(List<String> customFieldNames) {
		this.customFieldNames = customFieldNames;
	}

}
