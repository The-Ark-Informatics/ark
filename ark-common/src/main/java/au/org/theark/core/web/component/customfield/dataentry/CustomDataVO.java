package au.org.theark.core.web.component.customfield.dataentry;

import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ICustomFieldData;

abstract public class CustomDataVO<T extends ICustomFieldData> {

	protected ArkFunction arkFunction;
	protected List<T> customFieldDataList;

	public CustomDataVO() {
		customFieldDataList = new ArrayList<T>();
	}
	
	public ArkFunction getArkFunction() {
		return arkFunction;
	}

	public void setArkFunction(ArkFunction arkFunction) {
		this.arkFunction = arkFunction;
	}

	public List<T> getCustomFieldDataList() {
		return customFieldDataList;
	}
	
	public void setCustomFieldDataList(List<T> customFieldDataList) {
		this.customFieldDataList = customFieldDataList;
	}
	
}
