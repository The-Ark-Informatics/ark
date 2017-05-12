package au.org.theark.lims.web.component.inventory.tree.nestedtree;

import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.vo.LimsVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.component.subjectlims.lims.biocollection.BioCollectionDataEntryModalDetailPanel;
import au.org.theark.lims.web.component.subjectlims.lims.biospecimen.BiospecimenDataEntryModalDetailPanel;
import wickettree.AbstractTree;
import wickettree.ITreeProvider;
import wickettree.NestedTree;
import wickettree.content.StyledLinkLabel;

public class BiospecimenNestedTreePanel extends Panel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private static final Logger							log					= LoggerFactory.getLogger(BiospecimenNestedTreePanel.class);
	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_SERVICE)
	private ILimsService											iLimsService;
	protected CompoundPropertyModel<LimsVO>	cpModel;
	private ITreeProvider<Object> provider;
	private IModel<Set<Object>> state;
	public AbstractTree<Object> tree;
	protected AbstractDetailModalWindow modalWindow;

	public BiospecimenNestedTreePanel(String id, CompoundPropertyModel<LimsVO>	cpModel, ITreeProvider<Object> provider, final IModel<Set<Object>> state, AbstractDetailModalWindow modalWindow) {
		super(id);
		setOutputMarkupId(true);
		this.cpModel = cpModel;
		this.provider = provider;
		this.state = state;
		tree = createTree();
		this.modalWindow = modalWindow;
		add(tree);
	}

	@SuppressWarnings("unchecked")
	private AbstractTree<Object> createTree() {
		return new NestedTree("tree", provider, state){
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
								
								BioCollectionDataEntryModalDetailPanel modalContentPanel = new BioCollectionDataEntryModalDetailPanel("content", modalWindow, newModel);

								// Set the modalWindow title and content
								modalWindow.setTitle("Biocollection Detail");
								modalWindow.setContent(modalContentPanel);
								modalWindow.show(target);
								
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
								
								BiospecimenDataEntryModalDetailPanel modalContentPanel = new BiospecimenDataEntryModalDetailPanel("content", modalWindow, newModel);

								// Set the modalWindow title and content
								modalWindow.setTitle("Biospecimen Detail");
								modalWindow.setContent(modalContentPanel);
								modalWindow.show(target);
								
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



	public void setProvider(ITreeProvider<Object> provider) {
		this.provider = provider;
	}

	public ITreeProvider<Object> getProvider() {
		return provider;
	}
}
