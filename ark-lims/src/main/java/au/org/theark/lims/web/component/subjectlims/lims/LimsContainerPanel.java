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
package au.org.theark.lims.web.component.subjectlims.lims;

import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import wickettree.AbstractTree;
import wickettree.ITreeProvider;
import wickettree.util.InverseSet;
import wickettree.util.ProviderSubset;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.component.inventory.tree.nestedtree.BiospecimenNestedTreePanel;
import au.org.theark.lims.web.component.inventory.tree.nestedtree.BiospecimenTreeProvidor;
import au.org.theark.lims.web.component.subjectlims.lims.biocollection.BioCollectionListPanel;
import au.org.theark.lims.web.component.subjectlims.lims.biospecimen.BiospecimenListPanel;
import au.org.theark.lims.web.component.subjectlims.lims.form.ContainerForm;

/**
 * @author elam
 * @author cellis
 * 
 */
public class LimsContainerPanel extends Panel {

	private static final long						serialVersionUID	= -1L;
	
	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_SERVICE)
	private ILimsService											iLimsService;

	protected LimsVO									limsVO				= new LimsVO();
	protected CompoundPropertyModel<LimsVO>	cpModel;

	protected FeedbackPanel							feedbackPanel;
	protected WebMarkupContainer					arkContextMarkup;
	protected ContainerForm							containerForm;
	protected Panel									collectionListPanel;
	protected Panel									biospecimenListPanel;
	
	private ITreeProvider<Object> provider;
	private Set<Object> state;
	protected AbstractTree<Object> tree;
	protected AbstractDetailModalWindow modalWindow;
	public BiospecimenNestedTreePanel bioTreePanel;
	public BioCollectionListPanel biocollectionListPanel;
	public BiospecimenListPanel bioSpecimenListPanel;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		iArkCommonService;
	
	public LimsContainerPanel(String id, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		cpModel = new CompoundPropertyModel<LimsVO>(limsVO);
		initialisePanel();
	}

	public LimsContainerPanel(String id, IModel<LimsVO> model) {
		super(id);
		cpModel = new CompoundPropertyModel<LimsVO>(model.getObject());
		initialisePanel();
	}

	public LimsContainerPanel(String id, WebMarkupContainer arkContextMarkup, IModel<LimsVO> model) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		cpModel = new CompoundPropertyModel<LimsVO>(model.getObject());
		initialisePanel();
	}

	public void initialisePanel() {
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		
		modalWindow = new AbstractDetailModalWindow("detailModalWindow") {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {
				provider = new BiospecimenTreeProvidor(iArkCommonService, iLimsService, cpModel);
				state = new ProviderSubset<Object>(provider);
				((IDetachable)state).detach();
				state = new InverseSet<Object>(new ProviderSubset<Object>(provider));
				bioTreePanel = new BiospecimenNestedTreePanel("tree", cpModel, provider, newStateModel(), modalWindow);

				containerForm.addOrReplace(bioTreePanel);

				target.add(bioTreePanel);
				target.add(biocollectionListPanel);
				target.add(bioSpecimenListPanel);
			}
		};
		modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long	serialVersionUID	= 1L;

			public void onClose(AjaxRequestTarget target)
			{
				provider = new BiospecimenTreeProvidor(iArkCommonService, iLimsService, cpModel);
				state = new ProviderSubset<Object>(provider);
				((IDetachable)state).detach();
				state = new InverseSet<Object>(new ProviderSubset<Object>(provider));
				bioTreePanel = new BiospecimenNestedTreePanel("tree", cpModel, provider, newStateModel(), modalWindow);
				
				containerForm.addOrReplace(bioTreePanel);
				target.add(bioTreePanel);
				target.add(biocollectionListPanel);
				target.add(bioSpecimenListPanel);
			}
		});
		
		
		biocollectionListPanel = new BioCollectionListPanel("biocollectionListPanel", feedbackPanel, cpModel, modalWindow);
		collectionListPanel = biocollectionListPanel;
		containerForm.add(collectionListPanel);

		bioSpecimenListPanel = new BiospecimenListPanel("biospecimenListPanel", feedbackPanel, cpModel, modalWindow);
		biospecimenListPanel = bioSpecimenListPanel;
		
		provider = new BiospecimenTreeProvidor(iArkCommonService, iLimsService, cpModel);
		state = new ProviderSubset<Object>(provider);
		((IDetachable)state).detach();
		state = new InverseSet<Object>(new ProviderSubset<Object>(provider));
		bioTreePanel = new BiospecimenNestedTreePanel("tree", cpModel, provider, newStateModel(), modalWindow);
		
		containerForm.add(bioTreePanel);
		containerForm.add(modalWindow);
		containerForm.add(biospecimenListPanel);
		
		this.add(containerForm);
	}

	private IModel<Set<Object>> newStateModel() {
		return new AbstractReadOnlyModel<Set<Object>>()
		{
			private static final long	serialVersionUID	= 1L;

			@Override
			public Set<Object> getObject()
			{
				return state;
			}

			@Override
			public void detach()
			{
				((IDetachable)state).detach();
			}
		};
	}

	protected WebMarkupContainer initialiseFeedBackPanel() {
		/* Feedback Panel */
		feedbackPanel = new FeedbackPanel("feedbackMessage");
		feedbackPanel.setOutputMarkupId(true);
		return feedbackPanel;
	}
}