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
package au.org.theark.core.web.component.tabbedPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * Ajax based extension of the dynamic tabbed panel
 * 
 * @author Jonny Wray
 */
public class AjaxDynamicTabbedPanel extends DynamicTabbedPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -553938295672538628L;

	public AjaxDynamicTabbedPanel(final String id, final IModel<List<ITab>> tabModel) {
		super(id, tabModel);
		setOutputMarkupId(true);
		setVersioned(false);
	}

	@Override
	protected WebMarkupContainer newLink(final String linkId, final int index) {
		return new AjaxFallbackLink<Void>(linkId) {

			private static final long	serialVersionUID	= 1L;

			@Override
			public void onClick(final AjaxRequestTarget target) {
				setSelectedTab(index);
				if (target != null) {
					target.add(AjaxDynamicTabbedPanel.this);
				}
				onAjaxUpdate(target);
			}

		};
	}

	/**
	 * A template method that lets users add additional behavior when ajax update occurs. This method is called after the current tab has been set so
	 * access to it can be obtained via {@link #getSelectedTab()}.
	 * <p>
	 * <strong>Note</strong> Since an {@link AjaxFallbackLink} is used to back the ajax update the <code>target</code> argument can be null when the
	 * client browser does not support ajax and the fallback mode is used. See {@link AjaxFallbackLink} for details.
	 * 
	 * @param target
	 *           ajax target used to update this component
	 */
	protected void onAjaxUpdate(final AjaxRequestTarget target) {
	}
}
