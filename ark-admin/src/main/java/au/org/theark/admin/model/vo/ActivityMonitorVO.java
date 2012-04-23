package au.org.theark.admin.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.vo.ArkSubjectVO;

public class ActivityMonitorVO implements Serializable {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3901839049428295010L;
	private List<ArkSubjectVO>	activeUsers;

	public ActivityMonitorVO() {
		activeUsers = new ArrayList<ArkSubjectVO>();
	}
	
	public List<ArkSubjectVO> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(List<ArkSubjectVO> activeUsers) {
		this.activeUsers = activeUsers;
	}
}
