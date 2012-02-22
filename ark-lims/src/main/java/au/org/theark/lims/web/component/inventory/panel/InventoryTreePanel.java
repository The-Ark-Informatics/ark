package au.org.theark.lims.web.component.inventory.panel;

import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.tree.AbstractTree;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wickettree.NestedTree;
import wickettree.util.ProviderSubset;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.inventory.form.ContainerForm;
import au.org.theark.lims.web.component.inventory.panel.box.BoxDetailPanel;
import au.org.theark.lims.web.component.inventory.panel.freezer.FreezerDetailPanel;
import au.org.theark.lims.web.component.inventory.panel.rack.RackDetailPanel;
import au.org.theark.lims.web.component.inventory.panel.site.SiteDetailPanel;
import au.org.theark.lims.web.component.inventory.tree.BiospecimenProvidor;
import au.org.theark.lims.web.component.inventory.tree.InventoryLinkTree;

public class InventoryTreePanel extends AbstractInventoryTreePanel {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= -6281929692337423853L;
	private static final Logger	log					= LoggerFactory.getLogger(AbstractInventoryTreePanel.class);
	private final BaseTree			tree;
	private Form<Void>				treeForm				= new Form<Void>("treeForm");
	private FeedbackPanel			feedbackPanel;
	private WebMarkupContainer		detailContainer;
	private ContainerForm			containerForm;
	
	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService						iLimsService;
	private BiospecimenProvidor provider = new BiospecimenProvidor(iLimsService); 
	private Set<Biospecimen> state = new ProviderSubset<Biospecimen>(provider);

	public InventoryTreePanel(String id, FeedbackPanel feedbackPanel, WebMarkupContainer detailContainer, ContainerForm containerForm) {
		super(id);
		this.feedbackPanel = feedbackPanel;
		this.detailContainer = detailContainer;
		this.containerForm = containerForm;

		tree = new InventoryLinkTree("tree", createTreeModel(), feedbackPanel, detailContainer, containerForm);
		// tree.getTreeState().collapseAll();
		tree.setRootLess(true);
		
		
		NestedTree<Biospecimen> biospecimenTree = new NestedTree<Biospecimen>("bioTree", provider){

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Component newContentComponent(String id, IModel<Biospecimen> model) {
				
				return new Label(id, model.getObject().getBiospecimenUid());
			}
		};
		
		addOrReplace(biospecimenTree);
		
		addComponents();
	}
	
	private IModel<Set<Biospecimen>> newStateModel()
	{
		return new AbstractReadOnlyModel<Set<Biospecimen>>()
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public Set<Biospecimen> getObject()
			{
				return state;
			}

			/**
			 * Super class doesn't detach - would be nice though.
			 */
			@Override
			public void detach()
			{
				((IDetachable)state).detach();
			}
		};
	}

	@Override
	protected void onBeforeRender() {
		tree.setModelObject(createTreeModel());
		super.onBeforeRender();
	}

	private void addComponents() {
		treeForm.addOrReplace(addSite);
		treeForm.addOrReplace(addFreezer);
		treeForm.addOrReplace(addRack);
		treeForm.addOrReplace(addBox);

		addOrReplace(treeForm);
		addOrReplace(tree);
	}

	@Override
	protected AbstractTree getTree() {
		return tree;
	}

	@Override
	public void onAddSiteSubmit(AjaxRequestTarget target) {
		resetModel();

		SiteDetailPanel detailPanel = new SiteDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, tree, null);
		detailPanel.initialisePanel();

		refreshDetailPanel(target, detailPanel);
	}

	@Override
	public void onAddFreezerSubmit(AjaxRequestTarget target) {
		resetModel();

		FreezerDetailPanel detailPanel = new FreezerDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, tree, null);
		detailPanel.initialisePanel();

		refreshDetailPanel(target, detailPanel);
	}

	@Override
	public void onAddRackSubmit(AjaxRequestTarget target) {
		resetModel();

		RackDetailPanel detailPanel = new RackDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, tree, null);
		detailPanel.initialisePanel();

		refreshDetailPanel(target, detailPanel);
	}

	@Override
	public void onAddBoxSubmit(AjaxRequestTarget target) {
		resetModel();

		BoxDetailPanel detailPanel = new BoxDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, tree, null);
		detailPanel.initialisePanel();

		refreshDetailPanel(target, detailPanel);
	}

	/**
	 * Reset the model object to a new LimsVO
	 */
	public void resetModel() {
		LimsVO limsVo = new LimsVO();
		containerForm.setModelObject(limsVo);
	}

	/**
	 * Refresh/repaint the detailPanel
	 * 
	 * @param target
	 * @param detailPanel
	 */
	public void refreshDetailPanel(AjaxRequestTarget target, Panel detailPanel) {
		detailContainer.addOrReplace(detailPanel);
		detailContainer.setVisible(true);

		target.add(feedbackPanel);
		target.add(detailContainer);
		target.add(detailPanel);
	}

	public static Logger getLog() {
		return log;
	}
}