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
package au.org.theark.core.web.component;

import org.apache.wicket.markup.html.basic.Label;

/**
 * Creates a &lt;script&gt; component that is attached to a form to determine if any changes made to the form
	 * Requires HTML as follows:<p>
	 * &lt;script type="text/javascript" wicket:id="jQueryDirtyFormScript"&gt;
	 * <br>&nbsp;&nbsp;[script will be rendered here]
	 * <br>&lt;/script&gt;</p>
 * @author cellis
 *
 */
public class JQueryDirtyFormScript extends Label {


	private static final long	serialVersionUID	= 1L;
	
	/**
	 * Creates a &lt;script&gt; component that is attached to a form to determine if any changes made to the form
	 * Requires HTML as follows:<p>
	 * &lt;script type="text/javascript" wicket:id="jQueryDirtyFormScript"&gt;
	 * <br>&nbsp;&nbsp;[script will be rendered here]
	 * <br>&lt;/script&gt;</p>
	 * @param id the markup id of the &lt;script&gt; tag
	 * @param formMarkupId the markup id of the form to bind/watch to
	 
	 */
	public JQueryDirtyFormScript(String id, String formMarkupId) {
		super(id, jQueryScript(formMarkupId));
		setOutputMarkupPlaceholderTag(true);
		setEscapeModelStrings(false); // do not HTML escape JavaScript code
	}
	
	/**
	 * Sets up the jQuery script to attach the DirtyForms method to the form, and notify if changes made
	 * @param componentId
	 * @return
	 */
	public static String jQueryScript(String componentId) {
		StringBuilder jQueryScript = new StringBuilder();
		jQueryScript.append("jQuery.noConflict();");
		jQueryScript.append("\n");
		jQueryScript.append("jQuery(document).ready(function() {");
		jQueryScript.append("\n\t");
		jQueryScript.append("jQuery('#");
		jQueryScript.append(componentId);
		jQueryScript.append("').dirtyForms();");
		jQueryScript.append("\n});");
		return jQueryScript.toString();
	}
}