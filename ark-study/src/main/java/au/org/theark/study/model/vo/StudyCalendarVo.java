package au.org.theark.study.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.StudyCalendar;

public class StudyCalendarVo implements Serializable {
	
	private static final long	serialVersionUID	= 1L;
	private StudyCalendar				studyCalendar;
	private List<StudyCalendar>		studyCalendarList;
	private ArrayList<CustomField> selectedCustomFields;
	private Collection<CustomField> availableCustomFields;
	
	private int						mode;

	public StudyCalendarVo() {
		studyCalendar = new StudyCalendar();
		studyCalendarList = new ArrayList<StudyCalendar>();
		selectedCustomFields = new ArrayList<CustomField>();
		availableCustomFields = new ArrayList<CustomField>();
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public StudyCalendar getStudyCalendar() {
		return studyCalendar;
	}

	public void setStudyCalendar(StudyCalendar studyCalendar) {
		this.studyCalendar = studyCalendar;
	}

	public List<StudyCalendar> getStudyCalendarList() {
		return studyCalendarList;
	}

	public void setStudyCalendarList(List<StudyCalendar> studyCalendarList) {
		this.studyCalendarList = studyCalendarList;
	}
	
	public ArrayList<CustomField> getSelectedCustomFields() {
		return selectedCustomFields;
	}

	public void setSelectedCustomFields(ArrayList<CustomField> selectedCustomFields) {
		this.selectedCustomFields = selectedCustomFields;
	}

	public Collection<CustomField> getAvailableCustomFields() {
		return availableCustomFields;
	}

	public void setAvailableCustomFields(Collection<CustomField> availableCustomFields) {
		this.availableCustomFields = availableCustomFields;
	}

	
}
