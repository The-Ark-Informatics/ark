package au.org.theark.lims.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Generate a unique ID, based on a simple formatted current timestamp. The method is synchronized, (thread safe) to hopefully maintain uniqueness
 * @author cellis
 * 
 */
public class UniqueIdGenerator {
	private static String	uniqueID	= new String();
	private static Calendar	calendar;

	/**
	 * Generate a unique ID, based on a simple formatted current timestamp
	 * 
	 * @return String
	 */
	public synchronized static String generateUniqueId() {
		calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		uniqueID = simpleDateFormat.format(now);
		return uniqueID;
	}
}
