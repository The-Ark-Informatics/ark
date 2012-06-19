package au.org.theark.admin.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.vo.ArkSubjectSessionVO;

public class ActivityMonitorVO implements Serializable {

	private static final long	serialVersionUID	= 3901839049428295010L;
	private List<ArkSubjectSessionVO>	activeUsers;

	public ActivityMonitorVO() {
		activeUsers = new ArrayList<ArkSubjectSessionVO>();
	}
	
	public List<ArkSubjectSessionVO> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(List<ArkSubjectSessionVO> activeUsers) {
		this.activeUsers = activeUsers;
	}
}
