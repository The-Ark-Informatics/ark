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
	protected static final char[]	SPECIAL_CHARS				= { '@', '#', '$', '%' };
	protected static final char[] CHAR_LIST = {
      'A','B','C','D','E','F','G','H',
      'I','J','K','L','M','N','O','P',
      'Q','R','S','T','U','V','W','X',
      'Y','Z','a','b','c','d','e','f',
      'g','h','i','j','k','l','m','n',
      'o','p','q','r','s','t','u','v',
      'w','x','y','z','0','1','2','3',
      '4','5','6','7','8','9',
	};
	
	// 1 digit, 1 lower, 1 upper, 1 symbol "@#$%", from 6 to 20
	public static final String			PASSWORD_PATTERN	= "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

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
		String password = generatePassword();
		return password;
	}

	protected static String generatePassword() {
		StringBuffer password = new StringBuffer();

		// A random lowercase letter
		String randomLowercaseChar = org.apache.commons.lang.RandomStringUtils.randomAlphabetic(1).toLowerCase();
		// A random string from the base set of chars
		String randomString = org.apache.commons.lang.RandomStringUtils.random(DEFAULT_PASSWORD_LENGTH, 0, CHAR_LIST.length, true, true, CHAR_LIST);
		// A special character
		String randomSpecialChar = new String(org.apache.commons.lang.RandomStringUtils.random(1, SPECIAL_CHARS));
		// A random number (enforced)
		String randomNumeric = new String(org.apache.commons.lang.RandomStringUtils.randomNumeric(1));

		// Randomly replace one of the randomChars with a special character
		Random rand = new Random();
		int index = rand.nextInt(DEFAULT_PASSWORD_LENGTH);
		char randomChar = randomString.charAt(index);
		String replacedChars = randomString.replace(Character.toString(randomChar), randomSpecialChar);

		password.append(randomLowercaseChar);
		password.append(replacedChars);
		password.append(randomNumeric);
		return password.toString();
	}
}
