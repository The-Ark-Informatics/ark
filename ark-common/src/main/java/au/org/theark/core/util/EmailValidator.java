package au.org.theark.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Checks for invalid characters
 * in email addresses
 */
public class EmailValidator
{
	public static void main(String[] args) throws Exception
	{
		String input = "@sun.com";
		// Checks for email addresses starting with
		// inappropriate symbols like dots or @ signs.
		Pattern p = Pattern.compile("^\\.|^\\@");
		Matcher m = p.matcher(input);
		if (m.find())
			System.err.println("Email addresses don't start" + " with dots or @ signs.");
		// Checks for email addresses that start with
		// www. and prints a message if it does.
		p = Pattern.compile("^www\\.");
		m = p.matcher(input);
		if (m.find())
		{
			System.out.println("Email addresses don't start" + " with \"www.\", only web pages do.");
		}
		p = Pattern.compile("[^A-Za-z0-9\\.\\@_\\-~#]+");
		m = p.matcher(input);
		StringBuffer sb = new StringBuffer();
		boolean result = m.find();
		boolean deletedIllegalChars = false;

		while (result)
		{
			deletedIllegalChars = true;
			m.appendReplacement(sb, "");
			result = m.find();
		}

		// Add the last segment of input to the new String
		m.appendTail(sb);

		input = sb.toString();

		if (deletedIllegalChars)
		{
			System.out.println("It contained incorrect characters" + " , such as spaces or commas.");
		}
	}
	
	/**
	 * 
	 * @param emailAddress
	 * @return true if a valid email address
	 */
	public boolean isValid(String emailAddress){
		String input = "@sun.com";
		// Checks for email addresses starting with
		// inappropriate symbols like dots or @ signs.
		Pattern p = Pattern.compile("^\\.|^\\@");
		Matcher m = p.matcher(input);
		if (m.find()){
			return false;
		}
		
		// Checks for email addresses that start with www. and returns false if it does.
		p = Pattern.compile("^www\\.");
		m = p.matcher(input);
		if (m.find())
		{
			return false;
		}
		p = Pattern.compile("[^A-Za-z0-9\\.\\@_\\-~#]+");
		m = p.matcher(input);
		StringBuffer sb = new StringBuffer();
		boolean result = m.find();
		boolean deletedIllegalChars = false;

		while (result)
		{
			deletedIllegalChars = true;
			m.appendReplacement(sb, "");
			result = m.find();
		}

		// Add the last segment of input to the new String
		m.appendTail(sb);

		input = sb.toString();

		if (deletedIllegalChars)
		{
			return false;
		}
		
		return true;
	}
}
