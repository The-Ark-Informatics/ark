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

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

public abstract class AbstractDetailModalWindow extends ModalWindow {

	private static final long	serialVersionUID	= -794586150493541168L;
	protected String				title;
	protected FeedbackPanel		feedbackPanel;
	protected Panel				panel;
	protected Component			component;

	public AbstractDetailModalWindow(String id) {
		super(id);
		this.title = "";
		this.panel = new EmptyPanel("content");
		initialise();
		initialiseContentPanel(panel);
	}

	protected void initialise() {
		setTitle(this.title);
		setResizable(false);
		setWidthUnit("%");
		setInitialWidth(90);
		setHeightUnit("%");
		setInitialHeight(100);
		setCssClassName(CSS_CLASS_GRAY);
		add(initialiseFeedBackPanel());
	}

	protected WebMarkupContainer initialiseFeedBackPanel() {
		/* Feedback Panel */
		feedbackPanel = new FeedbackPanel("modalWindowFeedback");
		feedbackPanel.setOutputMarkupId(true);
		return feedbackPanel;
	}

	protected void initialiseContentPanel(Panel panel) {
		// Set the content panel, implementing the abstract methods
		setContent(panel);
	}

	@Override
	public void close(final AjaxRequestTarget target) {
		super.close(target);
		onCloseModalWindow(target);
		if(component != null) {
			target.add(component);
		}
	}
	
	/**
	 * What component to repaint after modalWindow is closed
	 * @param component
	 */
	public void repaintComponent(final Component component) {
		this.component = component;
	}

	abstract protected void onCloseModalWindow(AjaxRequestTarget target);

	/**
	 * @return the feedbackPanel
	 */
	public FeedbackPanel getModalFeedbackPanel() {
		return feedbackPanel;
	}

	/**
	 * @param feedbackPanel
	 *           the feedbackPanel to set
	 */
	public void setModalFeedbackPanel(FeedbackPanel feedbackPanel) {
		this.feedbackPanel = feedbackPanel;
	}
}
