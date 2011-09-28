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
package au.org.theark.core.web.form;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractWizardStepPanel extends Panel {

	/**
	 * 
	 */
	private static final long				serialVersionUID	= 2982993968381162494L;

	protected AbstractWizardHeaderPanel	header;
	protected AbstractWizardStepPanel	previous;
	protected AbstractWizardStepPanel	next;
	protected static final String			HEXES					= "0123456789ABCDEF";
	protected transient Logger				log					= LoggerFactory.getLogger(AbstractWizardStepPanel.class);

	public AbstractWizardStepPanel(String id) {
		super(id);
		this.header = new AbstractWizardHeaderPanel("header", null, null);
		this.add(header);
	}

	public AbstractWizardStepPanel(String id, String title, String summary) {
		super(id);
		this.header = new AbstractWizardHeaderPanel("header", summary, title);
		this.add(header);
	}

	public void setNextStep(AbstractWizardStepPanel next) {
		this.next = next;
	}

	public AbstractWizardStepPanel getNextStep() {
		return next;
	}

	public void setPreviousStep(AbstractWizardStepPanel previous) {
		this.previous = previous;
	}

	public AbstractWizardStepPanel getPreviousStep() {
		return previous;
	}

	protected void setContent(AjaxRequestTarget target, Component content) {
		if (!content.getId().equals(getContentId()))
			throw new IllegalArgumentException("Expected content id is " + getContentId() + " but " + content.getId() + " was found.");

		Component current = get(getContentId());
		if (current == null) {
			add(content);
		}
		else {
			current.replaceWith(content);
			if (target != null) {
				target.add(get(getContentId()));
			}
		}

	}

	/**
	 * Called when "next" button submit the current step form to go to next step.
	 * 
	 * @param form
	 * @param target
	 */
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target) {
	}

	/**
	 * Called when "previous" button was pressed to leave this step by going to previous step.
	 * 
	 * @param form
	 * @param target
	 */
	public void onStepOutPrevious(AbstractWizardForm<?> form, AjaxRequestTarget target) {
	}

	/**
	 * Called when "next" button was pressed to go to this step coming from previous step.
	 * 
	 * @param form
	 * @param target
	 */
	public void onStepInNext(AbstractWizardForm<?> form, AjaxRequestTarget target) {
	}

	/**
	 * Called when "previous" button was pressed to go to this step coming from next step.
	 * 
	 * @param form
	 * @param target
	 */
	public void onStepInPrevious(AbstractWizardForm<?> form, AjaxRequestTarget target) {
	}

	/**
	 * Called when "next" button submit the current step form to go to next step, and it fails.
	 * 
	 * @param form
	 * @param target
	 */
	public void onStepOutNextError(AbstractWizardForm<?> form, AjaxRequestTarget target) {
		form.onError(target, form);
	}

	public abstract void handleWizardState(AbstractWizardForm<?> form, AjaxRequestTarget target);

	public static String getContentId() {
		return "panel";
	}

	public static String getTitleId() {
		return "title";
	}

	public static String getHex(byte[] raw) {
		if (raw == null) {
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		for (final byte b : raw) {
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}

}
