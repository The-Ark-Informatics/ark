package au.org.theark.core.web.component.customfield.dataentry;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.model.IModel;

/**
 * Use this class to chain from a Date model to an underlying String model
 * @author elam
 *
 */
public class StringDateModel implements IModel<Date> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	IModel<String>	stringModel;
	String datePattern;

	public StringDateModel(IModel<String> stringModel, String datePattern) {
		this.stringModel = stringModel;
		this.datePattern = datePattern;
	}
	
	public Date getObject() {
		Date objectValue = null;
		// use this parseObject method signature to avoid needing try/catch
		// (parseObject returns null if failed to convert the dateString)
		if (stringModel != null && stringModel.getObject() != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
			objectValue = (Date) dateFormat.parseObject(stringModel.getObject(), new ParsePosition(0));
		}
		return objectValue;
	}

	public void setObject(Date object) {
		if (object == null) {
			stringModel.setObject(null);
		}
		else {
			SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
			stringModel.setObject(dateFormat.format(object));
		}
	}

	public void detach() {
	}

}