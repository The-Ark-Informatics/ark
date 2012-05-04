/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.core.web.component.customfield.dataentry;

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
