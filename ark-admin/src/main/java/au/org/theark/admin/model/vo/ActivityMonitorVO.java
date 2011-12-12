package au.org.theark.admin.model.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.subject.Subject;

public class ActivityMonitorVO {
	List<Subject>	activeUsers;

	public ActivityMonitorVO() {
		activeUsers = new ArrayList<Subject>();
	}
	
	public List<Subject> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(List<Subject> activeUsers) {
		this.activeUsers = activeUsers;
	}
}
