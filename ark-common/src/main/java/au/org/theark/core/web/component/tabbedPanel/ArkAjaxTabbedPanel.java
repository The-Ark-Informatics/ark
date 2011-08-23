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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPostprocessingCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.web.component.ArkMainTab;

public class ArkAjaxTabbedPanel extends AjaxTabbedPanel {
	/**
	 * 
	 */
	private static final long	serialVersionUID		= -3340777937373315256L;
	private transient Logger	log						= LoggerFactory.getLogger(ArkAjaxTabbedPanel.class);
	protected List<ArkMainTab>	mainTabs;

	protected String				setBusyIndicatorOn	= "document.getElementById('busyIndicator').style.display ='inline'; " + "overlay = document.getElementById('overlay'); "
																			+ "overlay.style.visibility = 'visible';";

	protected String				setBusyIndicatorOff	= "document.getElementById('busyIndicator').style.display ='none'; " + "overlay = document.getElementById('overlay'); "
																			+ "overlay.style.visibility = 'hidden';";

	public ArkAjaxTabbedPanel(String id, List<ITab> tabs) {
		super(id, tabs);

		mainTabs = new ArrayList<ArkMainTab>(0);
		for (Iterator<ITab> iterator = tabs.iterator(); iterator.hasNext();) {
			ITab iTab = (ITab) iterator.next();
			if (iTab instanceof ArkMainTab) {
				mainTabs.add((ArkMainTab) iTab);
			}
		}
		if (!((mainTabs.size() == tabs.size()) || mainTabs.size() == 0)) {
			log.error("Not all main tabs are using/extending ArkMainTab....");
		}
	}

	@SuppressWarnings("unchecked")
	protected WebMarkupContainer newLink(final String linkId, final int index) {
		return new AjaxFallbackLink(linkId) {
			private static final long	serialVersionUID	= 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				if (mainTabs.size() == 0 || (mainTabs.size() > 0 && mainTabs.get(index).isAccessible())) {
					setSelectedTab(index);
				}

				if (target != null) {
					target.addComponent(ArkAjaxTabbedPanel.this);
				}
				onAjaxUpdate(target);
			}

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() {
				return new AjaxPostprocessingCallDecorator(super.getAjaxCallDecorator()) {
					private static final long	serialVersionUID	= 1L;

					@Override
					public CharSequence postDecorateScript(CharSequence script) {
						return script + setBusyIndicatorOn;
					}

					@Override
					public CharSequence postDecorateOnFailureScript(CharSequence script) {
						return script + setBusyIndicatorOff;
					}

					@Override
					public CharSequence postDecorateOnSuccessScript(CharSequence script) {
						return script + setBusyIndicatorOff;
					}
				};
			}
		};
	}
}
