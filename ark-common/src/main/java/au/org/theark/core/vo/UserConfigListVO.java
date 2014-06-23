package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserConfigListVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<UserConfigVO> userConfigVOs = new ArrayList<UserConfigVO>();

	public UserConfigListVO() {
		
	}
	
	public List<UserConfigVO> getUserConfigVOs() {
		return userConfigVOs;
	}

	public void setUserConfigVOs(List<UserConfigVO> userConfigVOs) {
		this.userConfigVOs = userConfigVOs;
	}
	
}
