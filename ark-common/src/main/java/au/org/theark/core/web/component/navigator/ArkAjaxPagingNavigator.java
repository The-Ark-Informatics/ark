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
package au.org.theark.core.web.component.navigator;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigation;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationBehavior;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationIncrementLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigationLink;
import org.apache.wicket.model.StringResourceModel;

/**
 * An extended AjaxPagingNavigator that implements jQuery to monitor if the current form is "dirty", and warn the user when the pageable links aer clicked
 * @author cellis
 * 
 */
public class ArkAjaxPagingNavigator extends AjaxPagingNavigator {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 9222529241791387063L;
	protected Component			component;
	protected Label				label;

	/**
	 * 
	 * @param id
	 *           the markup identifier
	 * @param pageable
	 *           the pageable component
	 * @param component
	 *           the componenent you may wish to refresh
	 * @param label
	 *           the label that refers the the jQuery script that allows monitoring of the form to check if "dirty"
	 */
	public ArkAjaxPagingNavigator(String id, IPageable pageable, Component component, Label label) {
		super(id, pageable);
		this.component = component;
		this.label = label;
	}

	/**
	 * Wraps the default onclick event call for a pageable link with a confirmation if the current form is "dirty"
	 * @param onClickEvent the default Wicket AJAX onclick event
	 * @return
	 */
	private String dirtyFormJavaScript(String onClickEvent) {
		StringBuffer sb = new StringBuffer();
		sb.append("if(jQuery.DirtyForms.isDirty()) { if(!confirm('");
		StringResourceModel confirmation = new StringResourceModel("confirmation", this, null);
		sb.append(confirmation.getString());
		sb.append("')) { return false } else { ");
		sb.append(onClickEvent);
		sb.append("}}");
		return sb.toString();
	}

	@Override
	protected void onAjaxEvent(AjaxRequestTarget target) {
		target.add(this);
		
		// Refresh the specified component
		target.add(component);
		// refresh the jQuery dirtyForm script
		target.add(label);
	}

	@Override
	protected Link<?> newPagingNavigationIncrementLink(String id, IPageable pageable, int increment) {
		AjaxPagingNavigationIncrementLink link = new AjaxPagingNavigationIncrementLink(id, pageable, increment) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onInitialize() {
				super.onInitialize();
				add(new AjaxPagingNavigationBehavior(this, pageable, "onclick") {
					/**
						 * 
						 */
					private static final long	serialVersionUID	= 1L;

					@Override
					protected void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						String onClickEvent = tag.getAttribute("onclick");
						if (onClickEvent != null) {
							tag.put("onclick", dirtyFormJavaScript(onClickEvent));
						}
					}
				});
			}
		};
		return link;
	}

	@Override
	protected Link<?> newPagingNavigationLink(String id, IPageable pageable, int pageNumber) {
		AjaxPagingNavigationLink link = new AjaxPagingNavigationLink(id, pageable, pageNumber) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onInitialize() {
				super.onInitialize();
				add(new AjaxPagingNavigationBehavior(this, pageable, "onclick") {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;

					@Override
					protected void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						String onClickEvent = tag.getAttribute("onclick");
						if (onClickEvent != null) {
							tag.put("onclick", dirtyFormJavaScript(onClickEvent));
						}
					}
				});
			}
		};
		return link;
	}

	@Override
	protected PagingNavigation newNavigation(String id, IPageable pageable, IPagingLabelProvider labelProvider) {
		return new AjaxPagingNavigation("navigation", pageable, labelProvider) {
			private static final long	serialVersionUID	= 1456846335814294449L;

			protected PagingNavigationLink<?> newPagingNavigationLink(String id, IPageable pageable, int pageIndex) {
				AjaxPagingNavigationLink link = new AjaxPagingNavigationLink(id, pageable, pageIndex) {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1L;

					@Override
					protected void onInitialize() {
						super.onInitialize();
						add(new AjaxPagingNavigationBehavior(this, pageable, "onclick") {
							/**
							 * 
							 */
							private static final long	serialVersionUID	= 1L;

							@Override
							protected void onComponentTag(ComponentTag tag) {
								super.onComponentTag(tag);
								String onClickEvent = tag.getAttribute("onclick");
								if (onClickEvent != null) {
									tag.put("onclick", dirtyFormJavaScript(onClickEvent));
								}
							}
						});
					}
				};
				return link;
			}
		};
	}
}