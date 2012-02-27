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
package au.org.theark.lims.web.component.biolocation.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.component.inventory.panel.box.display.GridBoxPanel;
import au.org.theark.lims.web.component.inventory.tree.AllocationLinkTree;

/**
 * Detail form for Biospecimen allocation, as displayed within a modal window
 * 
 * @author cellis
 */
public class BioModalAllocateDetailForm extends Form<LimsVO> {
	/**
	 * 
	 */
	private static final long					serialVersionUID	= 9211020895417833151L;
	private CompoundPropertyModel<LimsVO>	cpModel;
	private AbstractDetailModalWindow		modalWindow;
	private final BaseTree						tree;
	private Panel									gridBoxPanel;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVo
	 * @param modalWindow
	 * @param listDetailPanel
	 */
	public BioModalAllocateDetailForm(String id, FeedbackPanel feedBackPanel, final ArkCrudContainerVO arkCrudContainerVo, final AbstractDetailModalWindow modalWindow,
			final CompoundPropertyModel<LimsVO> cpModel) {
		super(id, cpModel);
		this.cpModel = cpModel;
		this.modalWindow = modalWindow;

		tree = new AllocationLinkTree("tree") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public void boxNodeClicked(AjaxRequestTarget target, InvBox invBox) {
				cpModel.getObject().setInvBox(invBox);
				repaintGridBoxPanel(target);
			}
		};
		tree.getTreeState().collapseAll();
		tree.setRootLess(true);

		gridBoxPanel = new GridBoxPanel("gridBoxPanel", cpModel.getObject(), modalWindow, true);
		gridBoxPanel.setVisible(false);
	}

	public void initialiseDetailForm() {
		addComponents();
	}

	private void addComponents() {
		add(tree);
		add(gridBoxPanel);
	}

	protected void repaintGridBoxPanel(AjaxRequestTarget target) {
		gridBoxPanel = new GridBoxPanel("gridBoxPanel", cpModel.getObject(), modalWindow, true);
		gridBoxPanel.setVisible(true);
		addOrReplace(gridBoxPanel);
		target.add(gridBoxPanel);
	}
}