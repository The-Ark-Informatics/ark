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

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wickettree.AbstractTree;
import wickettree.ITreeProvider;
import wickettree.NestedTree;
import wickettree.content.StyledLinkLabel;
import wickettree.util.ProviderSubset;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.component.inventory.tree.nestedtree.BiospecimenNestedTreePanel;
import au.org.theark.lims.web.component.inventory.tree.nestedtree.BiospecimenTreeProvidor;
import au.org.theark.lims.web.component.subjectlims.lims.biocollection.BioCollectionListPanel;
import au.org.theark.lims.web.component.subjectlims.lims.biocollection.BioCollectionModalDetailPanel;
import au.org.theark.lims.web.component.subjectlims.lims.biospecimen.BiospecimenListPanel;
import au.org.theark.lims.web.component.subjectlims.lims.biospecimen.BiospecimenModalDetailPanel;
import au.org.theark.lims.web.component.subjectlims.lims.form.ContainerForm;

/**
 * @author elam
 * @author cellis
 * 
 */
public class LimsContainerPanel extends Panel {

	private static final long						serialVersionUID	= -1L;
	private static final Logger							log					= LoggerFactory.getLogger(LimsContainerPanel.class);
	
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
				target.add(bioTreePanel);
				target.add(biocollectionListPanel);
				target.add(bioSpecimenListPanel);
			}
		};
		modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			public void onClose(AjaxRequestTarget target)
         {
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
		
		provider = new BiospecimenTreeProvidor(iLimsService, cpModel);
		state = new ProviderSubset<Object>(provider);
		bioTreePanel = new BiospecimenNestedTreePanel("tree", cpModel, provider, newStateModel());
		
		
		//tree = createTree();
		
		containerForm.add(bioTreePanel);
		containerForm.add(modalWindow);
		containerForm.add(biospecimenListPanel);
		
		this.add(containerForm);
	}

	@SuppressWarnings("unchecked")
	private AbstractTree<Object> createTree() {
		return new NestedTree("tree", provider, newStateModel()){
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Component newContentComponent(String id, IModel model) {
				if(model.getObject() instanceof LinkSubjectStudy) {
					LinkSubjectStudy lss = (LinkSubjectStudy)model.getObject();
					return new Label(id, lss.getSubjectUID());
				}
				if(model.getObject() instanceof BioCollection) {
					final BioCollection bc = (BioCollection) model.getObject();
					StyledLinkLabel<String> styledLink = new StyledLinkLabel<String>(id, new Model<String>(bc.getBiocollectionUid())) {
						private static final long	serialVersionUID	= 1L;

						@Override
						protected String getStyleClass() {
							// TODO Auto-generated method stub
							return null;
						}
						@Override
						protected void onClick(AjaxRequestTarget target) {
							log.info("BiospecimenUID: " + getModelObject().toString());
							CompoundPropertyModel<LimsVO> newModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
							try {
								BioCollection biocollectionFromDB = iLimsService.getBioCollection(bc.getId());
								newModel.getObject().setBioCollection(biocollectionFromDB);
								newModel.getObject().setTreeModel(cpModel.getObject().getTreeModel());
								
								BioCollectionModalDetailPanel modalContentPanel = new BioCollectionModalDetailPanel("content", modalWindow, newModel);

								// Set the modalWindow title and content
								modalWindow.setTitle("Biocollection Detail");
								modalWindow.setContent(modalContentPanel);
								modalWindow.show(target);
								modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
								{
									/**
									 * 
									 */
									private static final long	serialVersionUID	= 1L;

									public void onClose(AjaxRequestTarget target)
						         {
						         }
								});
								
							}
							catch (EntityNotFoundException e) {
								log.error(e.getMessage());
							}
						}
						
						@Override
						protected boolean isClickable() {
							return true;
						}
					};
					
					return styledLink;
				}
				else if (model.getObject() instanceof Biospecimen) {
					final Biospecimen b = (Biospecimen) model.getObject();
					StyledLinkLabel<String> styledLink = new StyledLinkLabel<String>(id, new Model<String>(b.getBiospecimenUid())) {
						private static final long	serialVersionUID	= 1L;

						@Override
						protected String getStyleClass() {
							// TODO Auto-generated method stub
							return null;
						}
						@Override
						protected void onClick(AjaxRequestTarget target) {
							log.info("BiospecimenUID: " + getModelObject().toString());
							CompoundPropertyModel<LimsVO> newModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
							try {
								Biospecimen biospecimenFromDB = iLimsService.getBiospecimen(b.getId());
								newModel.getObject().setBiospecimen(biospecimenFromDB);
								newModel.getObject().setTreeModel(cpModel.getObject().getTreeModel());
								
								BiospecimenModalDetailPanel modalContentPanel = new BiospecimenModalDetailPanel("content", modalWindow, newModel);

								// Set the modalWindow title and content
								modalWindow.setTitle("Biospecimen Detail");
								modalWindow.setContent(modalContentPanel);
								modalWindow.show(target);
								modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
								{
									/**
									 * 
									 */
									private static final long	serialVersionUID	= 1L;

									public void onClose(AjaxRequestTarget target)
						         {
						         }
								});
								
							}
							catch (EntityNotFoundException e) {
								log.error(e.getMessage());
							}
						}
						
						@Override
						protected boolean isClickable() {
							return true;
						}
					};
					
					return styledLink;
				}
				return null;
			}
			
		};
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