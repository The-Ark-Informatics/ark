package au.org.theark.worktracking.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.worktracking.entity.WorkRequest;

public class WorkRequestVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WorkRequest  		workRequest;
	private List<WorkRequest> 	workRequestList;
	private int 				mode;

	public WorkRequestVo() {
		workRequest = new WorkRequest();
		workRequestList = new ArrayList<WorkRequest>();
	}

	public WorkRequest getWorkRequest() {
		return workRequest;
	}

	public void setWorkRequest(WorkRequest workRequest) {
		this.workRequest = workRequest;
	}

	public List<WorkRequest> getWorkRequestList() {
		return workRequestList;
	}

	public void setWorkRequestList(List<WorkRequest> workRequestList) {
		this.workRequestList = workRequestList;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

}
