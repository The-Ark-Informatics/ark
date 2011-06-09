package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkRole;

public class ArkModuleVO implements Serializable{
	
	private ArkModule arkModule;
	private List<ArkRole> arkModuleRoles;
	
	public ArkModuleVO(){
		arkModule = new ArkModule();
		arkModuleRoles = new ArrayList<ArkRole>();
	}

	public ArkModule getArkModule() {
		return arkModule;
	}

	public void setArkModule(ArkModule arkModule) {
		this.arkModule = arkModule;
	}

	public List<ArkRole> getArkModuleRoles() {
		return arkModuleRoles;
	}

	public void setArkModuleRoles(List<ArkRole> arkModuleRoles) {
		this.arkModuleRoles = arkModuleRoles;
	}

}
