package au.org.theark.core.vo;

import java.io.Serializable;

import au.org.theark.core.model.config.entity.UserConfig;

public class UserConfigVO extends BaseVO {
	
	private static final long serialVersionUID = 1L;
	
	private UserConfig userConfig;
	
	public UserConfigVO() {
		super();
		this.userConfig = new UserConfig();
	}
	
	public UserConfigVO(UserConfig userConfig) {
		super();
		this.userConfig = userConfig;
	}

	public UserConfig getUserConfig() {
		return userConfig;
	}

	public void setUserConfig(UserConfig userConfig) {
		this.userConfig = userConfig;
	}
}