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
package au.org.theark.web.component;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.feedback.IFeedback;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

@SuppressWarnings({"serial", "unchecked", "unused"})
public class FormFeedbackBorder extends Border implements IFeedback
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private boolean				visible;

	/**
	 * Error indicator that will be shown whenever there is an error-level message for the collecting component.
	 */
	private final class ErrorIndicator extends WebMarkupContainer
	{

		/**
		 * 
		 */
		private static final long	serialVersionUID	= 1L;

		public ErrorIndicator(String id)
		{
			super(id);
			add(new ErrorTextLabel("errorText", new Model("*")));
			add(new ErrorStyleAttributeModifier("style", true, new Model("color:red;")));
		}

		// An error style whose visiblity is determined by the presence
		// of feedback error messages.

		class ErrorStyleAttributeModifier extends AttributeModifier
		{
			public ErrorStyleAttributeModifier(String attribute, boolean addAttributeIfNotPresent, IModel replaceModel)
			{
				super(attribute, addAttributeIfNotPresent, replaceModel);
			}

			public boolean isVisible()
			{
				return visible;
			}
		}

		// An error text label whose visibility is determined by the presence
		// of feedback error messages.
		class ErrorTextLabel extends Label
		{
			public ErrorTextLabel(String id, IModel model)
			{
				super(id, model);
			}

			public boolean isVisible()
			{
				return visible;
			}
		}
	}

	public FormFeedbackBorder(final String id)
	{
		super(id);
		add(new ErrorIndicator("errorIndicator"));
	}

	// The ContainerFeedbackMessageFilter is used to filter out
	// feedback messages belonging to this component.
	protected IFeedbackMessageFilter getMessagesFilter()
	{
		return new ContainerFeedbackMessageFilter(this);
	}

	/*
	 * This method will be called on the component during Ajax render so that it gets a chance to determine the presence of error messages.
	 */
	public void updateFeedback()
	{
		visible = true;
	}

}
