package au.org.theark.core.util;

/**
 * Generate a new password consisting of a random 8 characters, and 2 random digits
 * @author arkadmin
 *
 */
public class ArkUserPasswordGenerator {
	/**
	 * Generate a new password consisting of a random 8 characters, and 2 random digits
	 * @return A new random password for the ArkUser
	 */
	public synchronized static String generateNewPassword() {
		StringBuffer password = new StringBuffer();
		String random8Chars = new String(org.apache.commons.lang.RandomStringUtils.randomAlphanumeric(8));
		String random2Digits = new String(org.apache.commons.lang.RandomStringUtils.randomNumeric(2));
		password.append(random8Chars);
		password.append(random2Digits);
		return password.toString();
	}
}
