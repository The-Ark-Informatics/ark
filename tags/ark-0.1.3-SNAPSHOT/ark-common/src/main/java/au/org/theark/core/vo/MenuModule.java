/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.vo;

import java.io.Serializable;

/**
 * @author nivedann
 *
 */
public class MenuModule implements Serializable{
	
	public MenuModule(){
		super();
	}
	private String moduleName;
	private String resourceKey;
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getResourceKey() {
		return resourceKey;
	}
	public void setResourceKey(String resourceKey) {
		this.resourceKey = resourceKey;
	}

}
