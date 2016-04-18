package au.org.theark.arkcalendar.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import au.org.theark.arkcalendar.data.ArkCalendarEvent;
import au.org.theark.arkcalendar.data.ArkCalendarEvent.Category;
import au.org.theark.arkcalendar.data.ArkCalendarVo;
import au.org.theark.arkcalendar.data.CalendarCustomFieldData;

public class ArkCalendarDao {
	
	private static final Logger LOG = LoggerFactory.getLogger(ArkCalendarDao.class);

	private static Sql2o sql2o;

	static {
		try {
			Class.forName(System.getProperty("db.driver"));
			sql2o = new Sql2o(System.getProperty("db.URL"), System.getProperty("db.username"), System.getProperty("db.password"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static ArkCalendarDao instance = null;

	/** new event id */
	private static int ID = -1;

	private static synchronized ArkCalendarDao get() {
		if (instance == null) {
			instance = new ArkCalendarDao();
		}
		return instance;
	}

	public static boolean isNew(ArkCalendarEvent event) {
		return event != null && event.getId() == ID;
	}

	public static ArkCalendarEvent newEvent(LocalDateTime date) {
		return new ArkCalendarEvent(ID, "", Category.PUBLIC, date);
	}

	public static ArkCalendarEvent newEvent(LocalDateTime start, LocalDateTime end) {
		return new ArkCalendarEvent(ID, "", Category.PUBLIC, start, end);
	}
	
	public static final List<CalendarCustomFieldData> getCustomFieldList(long calendarId, String subjectUID){
		
		String sql= "select scfd.ID id, lss.ID linkSubjectStudyId, cfd.ID customFieldDisplayId,\n" + 
				"			scfd.TEXT_DATA_VALUE textDataValue, scfd.DATE_DATA_VALUE dateDataValue, scfd.NUMBER_DATA_VALUE numberDataValue,\n" + 
				"			scfd.ERROR_DATA_VALUE errorDataValue,\n" + 
				"			cfd.CUSTOM_FIELD_ID customFieldId, cfd.CUSTOM_FIELD_GROUP_ID customFieldGroupId, cfd.REQUIRED required,\n" + 
				"			cfd.REQUIRED_MESSAGE requiredMessage, cfd.ALLOW_MULTIPLE_SELECTION allowMultiselect, cfd.SEQUENCE sequence,\n" + 
				"			cf.NAME name, cf.CATEGORY_ID customFieldCategoryId, cf.DESCRIPTION description, ft.name fieldType,\n" + 
				"			cf.STUDY_ID studyId, cf.UNIT_TYPE_ID unitTypeId, cf.MIN_VALUE minValue, cf.MAX_VALUE 'maxValue',\n" + 
				"			cf.ENCODED_VALUES encodedValues, cf.MISSING_VALUE missingValue, cf.HAS_DATA customFieldHasData,\n" + 
				"			cf.CUSTOM_FIELD_LABEL fieldLabel, cf.DEFAULT_VALUE defaultValue, cf.UNIT_TYPE_IN_TEXT unitTypeInText\n" + 
				"from study.link_calendar_custom_field lccf\n" + 
				"	left outer join study.study_calendar sc on sc.ID = lccf.CALENDAR_ID\n" + 
				"	left outer join study.custom_field cf on cf.ID = lccf.CUSTOM_FIELD_ID \n" + 
				"	left outer join study.link_subject_study lss on lss.SUBJECT_UID = :subjectUID  and lss.STUDY_ID = sc.STUDY_ID\n" + 
				"	left outer join study.field_type ft on ft.ID =cf.FIELD_TYPE_ID\n" + 
				"	left outer join study.custom_field_display cfd on cfd.CUSTOM_FIELD_ID = cf.ID and cfd.CUSTOM_FIELD_GROUP_ID is null \n" + 
				"	left outer join study.subject_custom_field_data scfd on scfd.CUSTOM_FIELD_DISPLAY_ID = cfd.ID and scfd.LINK_SUBJECT_STUDY_ID = lss.ID\n" + 
				"where lccf.CALENDAR_ID = :calendarId";
		
		List<CalendarCustomFieldData> calendarCustomFields = new ArrayList<CalendarCustomFieldData>();
		
		try (Connection con = ArkCalendarDao.sql2o.open()) {
			calendarCustomFields = con.createQuery(sql).addParameter("subjectUID", subjectUID).addParameter("calendarId", calendarId).executeAndFetch(CalendarCustomFieldData.class);	
		}
		
		return calendarCustomFields;
	}

	public static ArkCalendarEvent getEvent(int eventId) {
		
		ArkCalendarEvent event = null;

		String sql = "SELECT ce.ID id, ce.STUDY_CALENDAR_ID calenderId, ce.SUBJECT_UID subjectUID, ce.TITLE title, ce.START_TIME demoStart, ce.END_TIME demoEnd, ce.CATEGORY demoCategory, ce.ALL_DAY allDay \n" + "FROM calendar.calendar_event ce\n" + "WHERE ce.ID = :eventId";
		try (Connection con = ArkCalendarDao.sql2o.open()) {
			List<ArkCalendarEvent> events = con.createQuery(sql).addParameter("eventId", eventId).executeAndFetch(ArkCalendarEvent.class);
			event = events.size() > 0 ? events.get(0) : null;
		}

		return event;
	}

	public static List<ArkCalendarEvent> getEvents(LocalDate start, LocalDate end, int calendarId) {
		
		List<ArkCalendarEvent> events = new ArrayList<ArkCalendarEvent>();

		String sql = "SELECT ce.ID id, ce.STUDY_CALENDAR_ID calenderId, ce.SUBJECT_UID subjectUID, ce.TITLE title, ce.START_TIME demoStart, ce.END_TIME demoEnd, ce.CATEGORY demoCategory, ce.ALL_DAY allDay \n" + "FROM calendar.calendar_event ce\n" + "			inner join study.study_calendar sc on sc.ID = ce.STUDY_CALENDAR_ID\n" + "			inner join study.study st on st.id = sc.STUDY_ID\n" + "where sc.ID = :calendarId "
		+ "AND ce.START_TIME >= :start "
		+ "AND ce.END_TIME < :end";

		try (Connection con = ArkCalendarDao.sql2o.open()) {
			Query query = con.createQuery(sql).addParameter("calendarId", calendarId)
			 .addParameter("start", formatDateTime(start))
			 .addParameter("end", formatDateTime(end));
			
			LOG.debug(query.toString());
			
			events=query.executeAndFetch(ArkCalendarEvent.class);
					
		}
		return events;
	}

	public static void addEvent(ArkCalendarEvent event) {
		if (event != null) {
			String sql = "insert into calendar.calendar_event ( STUDY_CALENDAR_ID, SUBJECT_UID, TITLE, START_TIME, END_TIME, CATEGORY, ALL_DAY ) values ( :val1,:val2,:val3,:val4,:val5,:val6,:val7  )";

			try (Connection con = sql2o.open()) {
				long insertedId = (long) con.createQuery(sql, true).addParameter("val1", event.getCalenderId()).addParameter("val2", event.getSubjectUID()).addParameter("val3", event.getTitle()).addParameter("val4", formatDateTime(event.getStart())).addParameter("val5", formatDateTime(event.getEnd())).addParameter("val6", event.getCategory()).addParameter("val7", event.isAllDay()).executeUpdate().getKey();

				event.setId((int) insertedId);
			}
		}

	}
	
	public static void updateEvent(ArkCalendarEvent event) {

		if (event != null) {
			String updateEventSql = "update calendar.calendar_event set STUDY_CALENDAR_ID = :val1, SUBJECT_UID = :val2, TITLE = :val3, START_TIME = :val4, END_TIME = :val5, CATEGORY = :val6, ALL_DAY = :val7  where id = :val8";

			try (Connection con = sql2o.open()) {
				Query query=con.createQuery(updateEventSql);
				
				query.addParameter("val1", event.getCalenderId())
				.addParameter("val2", event.getSubjectUID())
				.addParameter("val3", event.getTitle())
				.addParameter("val4", formatDateTime(event.getStart()))
				.addParameter("val5", formatDateTime(event.getEnd()))
				.addParameter("val6", event.getCategory())
				.addParameter("val7", event.isAllDay())
				.addParameter("val8", event.getId())
				.executeUpdate();
				
				
				LOG.debug(query.toString());
				
			}
			
			String insertCustomDataSql = "insert into study.subject_custom_field_data ( LINK_SUBJECT_STUDY_ID, CUSTOM_FIELD_DISPLAY_ID, TEXT_DATA_VALUE, DATE_DATA_VALUE, ERROR_DATA_VALUE, NUMBER_DATA_VALUE ) values ( :val1,:val2,:val3,:val4,:val5,:val6 )";
			
			String updateCustomDataSql = "update study.subject_custom_field_data set  TEXT_DATA_VALUE =:textValue, DATE_DATA_VALUE=:dateValue,NUMBER_DATA_VALUE=:numValue where ID =:id";
			
			Query query=null;
			
			for(CalendarCustomFieldData data: event.getCustomFieldData()){
				try(Connection con = sql2o.open()){
					
					if(data.getId() == null || data.getId() == 0){
						query=con.createQuery(insertCustomDataSql);
						query.addParameter("val1", data.getLinkSubjectStudyId())
						.addParameter("val2", data.getCustomFieldDisplayId())
						.addParameter("val3", data.getTextDataValue())
						.addParameter("val4", data.getDateDataValue())
						.addParameter("val5", data.getErrorDataValue())
						.addParameter("val6", data.getNumberDataValue())
						.executeUpdate();
					}else{
						query=con.createQuery(updateCustomDataSql);
						query.addParameter("textValue", data.getTextDataValue())
						.addParameter("dateValue", data.getDateDataValue())
						.addParameter("numValue", data.getNumberDataValue())
						.addParameter("id", data.getId())
						.executeUpdate();
					}
				}
			}
//			try(Connection con = sql2o.open()){
//				String csql="update calendar.calendar_event set START_TIME = :val1, END_TIME = :val2 where id = :val3";
//				Query query=con.createQuery(csql);
//			
//				query.addParameter("val1", formatDateTime(event.getStart()))
//				.addParameter("val2", formatDateTime(event.getEnd()))
//				.addParameter("val3", event.getId())
//				.executeUpdate();
//			
//				Logger.getLogger("CalendarDao").info(query.toString());			
//			}
		}

	}

	private static String formatDateTime(LocalDateTime dateIn) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		String formattedDateTime = dateIn.format(formatter);

		return formattedDateTime;

	}
	
	private static String formatDateTime(LocalDate dateIn) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		String formattedDateTime = dateIn.format(formatter);

		return formattedDateTime;

	}

	private final List<ArkCalendarEvent> list;
	private int id = 0;

	public ArkCalendarDao() {
		this.list = new ArrayList<ArkCalendarEvent>();
		this.initList();
	}

	private final void initList() {
		this.list.add(new ArkCalendarEvent(this.newId(), "Public event", Category.PUBLIC, LocalDateTime.now()));
		this.list.add(new ArkCalendarEvent(this.newId(), "Private event", Category.PRIVATE, LocalDateTime.now()));
	}
	 

	protected final int newId() {
		return ++this.id;
	}

	public static void cancelEvent(ArkCalendarEvent event) {
		if (event != null) {
			String deleteSql = "delete from calendar.calendar_event where id = :id";

			try (Connection con = sql2o.open()) {
				con.createQuery(deleteSql).addParameter("id", event.getId()).executeUpdate();
			}
		}

	}

	/**
	 * Helper that indicates whether an event is in the given date range
	 * (between start date & end date)
	 *
	 * @param event
	 * @param start
	 * @param end
	 * @return true or false
	 */
	protected boolean isInRange(ArkCalendarEvent event, LocalDateTime start, LocalDateTime end) {
		LocalDateTime dateS = event.getStart();
		LocalDateTime dateE = event.getEnd();

		return dateS != null && start.compareTo(dateS) <= 0 && end.compareTo(dateS) >= 0 && 
				(dateE == null || (start.compareTo(dateE) <= 0 && end.compareTo(dateE) >= 0));
	}

	public List<ArkCalendarVo> getArkStudyCalendarList(String user) {

//		String sql = "select sc.id calId, sc.name  calName, sc.DESCRIPTION calDesc, st.NAME study " + "from study.study_calendar sc " + "	left outer join study.study st on st.id = sc.STUDY_ID" + "	left outer join study.study_comp scom on scom.id = sc.STUDY_COMPONENT_ID;";
		
		
		StringBuilder sb = new StringBuilder("select sc.id calId, sc.name  calName, sc.DESCRIPTION calDesc, st.NAME study ");
		sb.append("from study.study_calendar sc ");
		sb.append("	inner join study.study st on st.id = sc.STUDY_ID ");
		sb.append("	left outer join study.study_comp scom on scom.id = sc.STUDY_COMPONENT_ID ");
		
		if(!"arksuperuser@ark.org.au".equals(user)){
			sb.append("	inner join  study. ark_user_role aur on aur.STUDY_ID = st.id "); 
			sb.append("	inner join study.ark_user au on au.id = aur.ARK_USER_ID "); 
			sb.append("	inner join study.ark_role ar on ar.ID =aur.ARK_ROLE_ID "); 
			sb.append("	inner join study.ark_module am on am.id = aur.ARK_MODULE_ID ");
			sb.append("where au.LDAP_USER_NAME = 'tranaweera@unimelb.edu.au' ");
			sb.append("			and ar.NAME in ('Super Administrator','Calendar Administrator','Calendar Data Manager','Calendar Read-Only User') ");
			sb.append("			and am.NAME = 'Calendar';	");
		}
		try (Connection con = ArkCalendarDao.sql2o.open()) {
			return con.createQuery(sb.toString()).executeAndFetch(ArkCalendarVo.class);
		}
	}

	public static String[] getUserRoles(String user){
		String roles[]=null;
		String sql = "SELECT ar.Name FROM study.ark_role ar \n" + 
				"					inner join  study.ark_user_role aur on aur.ARK_ROLE_ID = ar.id\n" + 
				"					inner join study.ark_user au on au.id = aur.ARK_USER_ID\n" + 
				"where au.LDAP_USER_NAME = :user\n" + 
				"			and ar.NAME in ('Super Administrator','Calendar Administrator','Calendar Data Manager','Calendar Read-Only User')\n" + 
				"group by ar.name;";
		try (Connection con = ArkCalendarDao.sql2o.open()) {
			List<String> results  =  con.createQuery(sql).addParameter("user", user).executeAndFetch(String.class);
			roles = new String[results.size()];
			for(int i =0; i< results.size();++i){
				roles[i]= results.get(i);
			}
		}		
		return roles;
	}
	
	public static String getUserRole(String user, int calendarId){
		String role=null;
		String sql = "SELECT ar.Name \n" + 
				"FROM study.ark_role ar \n" + 
				"	inner join  study.ark_user_role aur on aur.ARK_ROLE_ID = ar.id\n" + 
				"	inner join study.study_calendar sc on sc.STUDY_ID = aur.STUDY_ID\n" + 
				"	inner join study.ark_user au on au.id = aur.ARK_USER_ID\n" + 
				"	inner join study.ark_module am on am.id = aur.ARK_MODULE_ID\n" + 
				"where au.LDAP_USER_NAME = :user \n" + 
				"			and ar.NAME in ('Super Administrator','Calendar Administrator','Calendar Data Manager','Calendar Read-Only User')\n" + 
				"			and am.NAME = 'Calendar'\n" + 
				"			and sc.ID = :calendarId;";
		try (Connection con = ArkCalendarDao.sql2o.open()) {
			List<String> results  =  con.createQuery(sql).addParameter("user", user).addParameter("calendarId", calendarId).executeAndFetch(String.class);
			
			if(results.size() > 0){
				role = results.get(0);
			}
		}	
		return role;
	}
	
	public static boolean isSubjectUIDExists(int calendarId, String subjectUID){
		boolean exists=false;
		String sql="select lss.ID\n" + 
				"from study.study_calendar sc\n" + 
				"			left outer join study.study st on st.id = sc.STUDY_ID\n" + 
				"			left outer join study.link_subject_study lss on lss.STUDY_ID = st.ID\n" + 
				"where lss.SUBJECT_UID = :subjectUID \n" + 
				"				and sc.ID = :calendarId";
		
		try (Connection con = ArkCalendarDao.sql2o.open()) {
			List<Long> results  =  con.createQuery(sql).addParameter("subjectUID", subjectUID).addParameter("calendarId", calendarId).executeAndFetch(Long.class);
			
			if(results.size() > 0){
				exists=true;
			}
		}	
		
		return exists;
	}
	
}
