package au.org.theark.arkcalendar.data;

import java.io.Serializable;

public class ArkCalendarVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int calId;

	private String study;

	private String calName;

	private String calDesc;
	
	public int getCalId() {
		return calId;
	}

	public void setCalId(int calId) {
		this.calId = calId;
	}

	public String getStudy() {
		return study;
	}

	public void setStudy(String study) {
		this.study = study;
	}

	public String getCalName() {
		return calName;
	}

	public void setCalName(String calName) {
		this.calName = calName;
	}

	public String getCalDesc() {
		return calDesc;
	}

	public void setCalDesc(String calDesc) {
		this.calDesc = calDesc;
	}

	@Override
	public String toString() {
		return "ArkCalendarVo [calId=" + calId + ", study=" + study + ", calName=" + calName + ", calDesc=" + calDesc + "]";
	}
	
}
