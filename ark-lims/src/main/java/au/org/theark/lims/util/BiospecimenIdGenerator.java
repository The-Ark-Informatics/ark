package au.org.theark.lims.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author cellis
 *
 */
public class BiospecimenIdGenerator
{
	private static String biospecimenId = new String();
	private static Calendar calendar = Calendar.getInstance();
	
	/**
	 * Generate a unique BiospecimenID, based on a simple formatted current timestamp
	 * @return String
	 */
	public static String generateBiospecimenId()
	{
		java.util.Date now = calendar.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		biospecimenId = simpleDateFormat.format(now);
		return biospecimenId;
	}
}
