package au.org.theark.arkcalendar.data;

import java.util.ArrayList;
import java.util.List;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import com.googlecode.wicket.jquery.ui.calendar.CalendarEvent;

public class ArkCalendarEvent extends CalendarEvent
{
	private static final long serialVersionUID = 1L;

	public enum Category
	{
		PUBLIC("public", "#5C9CCC"),
		PRIVATE("private", "#F6A828");

		private final String name;
		private final String color;

		private Category(String name, String color)
		{
			this.name = name;
			this.color = color;
		}

		public String getColor()
		{
			return this.color;
		}

		@Override
		public String toString()
		{
			return this.name;
		}
	}

	private Category category;
		
	private int calenderId;
	
	private String subjectUID;
	
	private String demoStart;

	private String demoEnd;
	
	private String demoCategory;
	
	private List<CalendarCustomFieldData> customFieldData = null;

	public ArkCalendarEvent(int id, String title, Category category, LocalDateTime date)
	{
		this(id, title, category, date, null);
	}

	public ArkCalendarEvent(int id, String title, Category category, LocalDateTime start, LocalDateTime end)
	{
		super(id, title, start, end);
		this.customFieldData = new ArrayList<CalendarCustomFieldData>();
		this.setCategory(category);
	}

	public final void setCategory(Category category)
	{
		this.category = category;

		if (this.category != null)
		{
			this.setColor(this.category.getColor());
			this.setClassName(this.category.toString());
		}
	}

	public Category getCategory()
	{
		return this.category;
	}

	public int getCalenderId() {
		return calenderId;
	}

	public void setDemoStart(String demoStart) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		LocalDateTime ls= LocalDateTime.parse(demoStart.substring(0, 16),formatter);
		
		setStart(ls);
		
		this.demoStart = demoStart;
	}

	public String getDemoEnd() {
		return demoEnd;
	}

	public void setDemoEnd(String demoEnd) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		LocalDateTime ls= LocalDateTime.parse(demoEnd.substring(0, 16),formatter);
		
		setEnd(ls);

		this.demoEnd = demoEnd;		
	}

	public String getDemoCategory() {
		return demoCategory;
	}

	public void setDemoCategory(String demoCategory) {
		Category[] list= Category.values();
		for(Category cat: list){
			if(cat.name.equalsIgnoreCase(demoCategory)){
				setCategory(cat);
			}
		}
		this.demoCategory = demoCategory;
	}

	public String getSubjectUID() {
		return subjectUID;
	}

	public void setSubjectUID(String subjectUID) {
		this.subjectUID = subjectUID;
	}

	public String getDemoStart() {
		return demoStart;
	}

	public void setCalenderId(int calenderId) {
		this.calenderId = calenderId;
	}

	public List<CalendarCustomFieldData> getCustomFieldData() {
		return customFieldData;
	}

	public void setCustomFieldData(List<CalendarCustomFieldData> customFieldData) {
		this.customFieldData = customFieldData;
	}
	
//DO NOT OVERRIDE the TOSTRING 
//WILL break the calendar app
//	@Override
//	public String toString() {
//		return "ArkCalendarEvent [category=" + category + ", calenderId=" + calenderId + ", subjectUID=" + subjectUID + ", demoStart=" + demoStart + ", demoEnd=" + demoEnd + ", demoCategory=" + demoCategory + "]";
//	}
}
