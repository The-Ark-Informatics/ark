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
package au.org.theark.lims.web.component.button;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

/**
 * 
 * @author cellis
 * 
 */
public class NumberOfLabelsPanel extends Panel {

	/**
	 * 
	 */
	private static final long				serialVersionUID	= 2578315620375936043L;
	protected Integer							selected				= new Integer(1);
	protected Label							labelsToPrint;
	protected DropDownChoice<Integer>	numberOfLabelsToPrint;
	/** available numbers for selection. */
	private static final List<Integer>	INTEGERS				= Arrays.asList(1, 5, 10);

	public NumberOfLabelsPanel(String id) {
		super(id);
		setOutputMarkupId(true);
		setDefaultModel(new PropertyModel<Integer>(this, "selected"));
		initPanel();
		add(new AttributeModifier("class", "floatLeft"));
	}

	private void initPanel() {
		labelsToPrint = new Label("labelsToPrint", "No. of labels:");
		this.add(labelsToPrint);

		numberOfLabelsToPrint = new DropDownChoice<Integer>("numberOfLabelsToPrint", new PropertyModel<Integer>(this, "selected"), INTEGERS);
		numberOfLabelsToPrint.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				// Reset the panel model when the dropdown changes
				NumberOfLabelsPanel.this.setDefaultModelObject(getDefaultModelObject());
			}
		});
		this.add(numberOfLabelsToPrint);
	}
}