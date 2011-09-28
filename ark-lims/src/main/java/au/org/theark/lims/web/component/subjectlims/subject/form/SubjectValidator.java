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
package au.org.theark.lims.web.component.subjectlims.subject.form;

import java.util.Calendar;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

public class SubjectValidator implements IValidator<Long> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5915383351087650967L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
	 */
	public void validate(IValidatable<Long> arg0) {
		if (arg0.getValue() != null) {
			Calendar calendar = Calendar.getInstance();
			int calYear = calendar.get(Calendar.YEAR);
			if (arg0.getValue() > calYear) {

				//ValidationError ve = new ValidationError().addMessageKey("error.found");
				// ve.setVariables(vars);
			}
		}

	}

}
