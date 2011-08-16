package au.org.theark.core.web.component.customfield.dataentry;

import java.io.Serializable;

public class EncodedValueVO implements Serializable {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	String key;
	String value;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
