package au.org.theark.core.vo;

import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ICustomFieldData;
import au.org.theark.core.model.study.entity.IPhenoDataSetFieldData;

abstract public class PhenoDataVO<T extends IPhenoDataSetFieldData> {

	protected ArkFunction arkFunction;
	protected List<T> phenoFieldDataList;

	public PhenoDataVO() {
		phenoFieldDataList = new ArrayList<T>();
	}
	
	public ArkFunction getArkFunction() {
		return arkFunction;
	}

	public void setArkFunction(ArkFunction arkFunction) {
		this.arkFunction = arkFunction;
	}

	public List<T> getPhenoFieldDataList() {
		return phenoFieldDataList;
	}

	public void setPhenoFieldDataList(List<T> phenoFieldDataList) {
		this.phenoFieldDataList = phenoFieldDataList;
	}

	
	
}
