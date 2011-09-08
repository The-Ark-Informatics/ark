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
package au.org.theark.study.web.component.consent.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;

import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 * 
 */
public class FormHelper {

	public FormHelper() {

	}

	public void updateStudyCompStatusDates(AjaxRequestTarget target, String statusName, WebMarkupContainer wmcPlain, WebMarkupContainer wmcRequested, WebMarkupContainer wmcRecieved,
			WebMarkupContainer wmcCompleted) {

		if ((statusName.equalsIgnoreCase(Constants.STUDY_STATUS_RECEIVED))) {
			wmcRecieved.setVisible(true);
			wmcPlain.setVisible(false);
			wmcRequested.setVisible(false);
			wmcCompleted.setVisible(false);
			target.addComponent(wmcPlain);
			target.addComponent(wmcRecieved);
			target.addComponent(wmcRequested);
			target.addComponent(wmcCompleted);
		}
		else if ((statusName.equalsIgnoreCase(Constants.STUDY_STATUS_REQUESTED))) {

			wmcRequested.setVisible(true);
			wmcPlain.setVisible(false);
			wmcRecieved.setVisible(false);
			wmcCompleted.setVisible(false);
			target.addComponent(wmcRecieved);
			target.addComponent(wmcRequested);
			target.addComponent(wmcPlain);
			target.addComponent(wmcCompleted);
		}
		else if ((statusName.equalsIgnoreCase(Constants.STUDY_STATUS_COMPLETED))) {

			wmcCompleted.setVisible(true);
			wmcPlain.setVisible(false);
			wmcRecieved.setVisible(false);
			wmcRequested.setVisible(false);
			target.addComponent(wmcCompleted);
			target.addComponent(wmcRecieved);
			target.addComponent(wmcRequested);
			target.addComponent(wmcPlain);
		}
		else {
			setDatePickerDefaultMarkup(target, wmcPlain, wmcRequested, wmcRecieved, wmcCompleted);
		}

	}

	public void setDatePickerDefaultMarkup(AjaxRequestTarget target, WebMarkupContainer wmcPlain, WebMarkupContainer wmcRequested, WebMarkupContainer wmcRecieved, WebMarkupContainer wmcCompleted) {
		wmcPlain.setVisible(true);
		wmcRecieved.setVisible(false);
		wmcRequested.setVisible(false);
		wmcCompleted.setVisible(false);
		target.addComponent(wmcCompleted);
		target.addComponent(wmcRecieved);
		target.addComponent(wmcRequested);
		target.addComponent(wmcPlain);
	}

}
