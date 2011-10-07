package au.org.theark.core.util;

import java.util.Random;

/**
 * Generate a new password, of the pattern:
	 *         <ul>
	 *         <li>at least 1 digit</li>
	 *         <li>a lower case letter</li>
	 *         <li>an upper case letter</li>
	 *         <li>a special character (@#$%) and</li>
	 *         <li>length of 10 characters.</li>
	 *         </ul>
 * @author cellis
 * 
 */
public class ArkUserPasswordGenerator {
	protected static final int		DEFAULT_PASSWORD_LENGTH	= 8;
	protected static final char[]	SPECIAL_CHAR				= { '@', '#', '$', '%' };

	/**
	 * Generate a new password, of the pattern:
	 *         <ul>
	 *         <li>at least 1 digit</li>
	 *         <li>a lower case letter</li>
	 *         <li>an upper case letter</li>
	 *         <li>a special character (@#$%) and</li>
	 *         <li>length of 10 characters.</li>
	 *         </ul>
	 * 
	 * @return A new random password for the ArkUser
	 */
	public synchronized static String generate() {
		StringBuffer password = new StringBuffer();

		// Randomly generate the "pieces" of the password
		String randomSpecialChar = new String(org.apache.commons.lang.RandomStringUtils.random(1, SPECIAL_CHAR));
		String randomChars = new String(org.apache.commons.lang.RandomStringUtils.randomAlphanumeric(DEFAULT_PASSWORD_LENGTH));
		String randomNumeric = new String(org.apache.commons.lang.RandomStringUtils.randomNumeric(1));

		// Randomly replace one of the randomChars with a special character
		Random rand = new Random();
		int index = rand.nextInt(DEFAULT_PASSWORD_LENGTH);
		char randomChar = randomChars.charAt(index);
		String replacedChars = randomChars.replace(Character.toString(randomChar), randomSpecialChar);

		password.append(replacedChars);
		password.append(randomNumeric);

		return password.toString();
	}
}
