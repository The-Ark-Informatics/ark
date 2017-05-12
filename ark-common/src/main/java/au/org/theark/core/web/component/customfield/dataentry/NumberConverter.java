package au.org.theark.core.web.component.customfield.dataentry;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.AbstractDecimalConverter;

public class NumberConverter extends AbstractDecimalConverter<Double>
{
	private static final long serialVersionUID = 1L;

	/**
	 * The singleton instance for a double converter
	 */
	public static final IConverter<Double> INSTANCE = new NumberConverter();
	
	/** The date format to use */
	private final Map<Locale, NumberFormat> numberFormats = new ConcurrentHashMap<Locale, NumberFormat>();
	
	/**
	 * @param locale
	 * @return Returns the numberFormat.
	 */
	@Override
	public NumberFormat getNumberFormat(final Locale locale)
	{
		NumberFormat numberFormat = numberFormats.get(locale);
		if (numberFormat == null)
		{
			numberFormat = newNumberFormat(locale);
			setNumberFormat(locale, numberFormat);
		}
		numberFormat.setGroupingUsed(false);
		return (NumberFormat)numberFormat.clone();
	}

	/**
	 * @see org.apache.wicket.util.convert.IConverter#convertToObject(String, java.util.Locale)
	 */
	public Double convertToObject(final String value, final Locale locale)
	{
		final Number number = parse(value, -Double.MAX_VALUE, Double.MAX_VALUE, locale);
		// Double.MIN is the smallest nonzero positive number, not the largest
		// negative number

		if (number == null)
		{
			return null;
		}

		return number.doubleValue();
	}

	/**
	 * @see org.apache.wicket.util.convert.converter.AbstractConverter#getTargetType()
	 */
	@Override
	protected Class<Double> getTargetType()
	{
		return Double.class;
	}
}