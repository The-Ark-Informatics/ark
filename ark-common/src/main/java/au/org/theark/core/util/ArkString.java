package au.org.theark.core.util;

/**
 * The Ark specific <code>String</code> conversions
 * @author thilina
 *
 */
public final class ArkString {

	/**
	 * Convert the input String value to sentence case or empty.
	 * @param inputVal Input String value. 
	 * @return Sentence case String value.
	 */
	public static final String sentenceCase(final String inputVal) {
		// Empty strings should be returned as-is.

		if (inputVal == null || inputVal.length() == 0)
			return "";

		// Strings with only one character uppercased.

		if (inputVal.length() == 1)
			return inputVal.toUpperCase();

		// Otherwise uppercase first letter, lowercase the rest.

		return inputVal.substring(0, 1).toUpperCase() + inputVal.substring(1).toLowerCase();
	}

}
