package au.org.theark.lims.web.component.inventory.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.tree.AbstractTree;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.component.inventory.form.ContainerForm;
import au.org.theark.lims.web.component.inventory.panel.box.BoxDetailPanel;
import au.org.theark.lims.web.component.inventory.panel.site.SiteDetailPanel;
import au.org.theark.lims.web.component.inventory.panel.tank.TankDetailPanel;
import au.org.theark.lims.web.component.inventory.panel.tray.TrayDetailPanel;
import au.org.theark.lims.web.component.inventory.tree.InventoryLinkTree;

public class InventoryTreePanel extends AbstractInventoryTreePanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6281929692337423853L;
	private final BaseTree		tree;
	private Form<Void>			treeForm				= new Form<Void>("treeForm");
	private FeedbackPanel		feedbackPanel;
	private WebMarkupContainer	detailContainer;
	private ContainerForm		containerForm;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	public InventoryTreePanel(String id, FeedbackPanel feedbackPanel, WebMarkupContainer detailContainer, ContainerForm containerForm) {
		super(id);
		this.feedbackPanel = feedbackPanel;
		this.detailContainer = detailContainer;
		this.containerForm = containerForm;

		tree = new InventoryLinkTree("tree", createTreeModel(), feedbackPanel, detailContainer, containerForm);
		//tree.getTreeState().collapseAll();
		tree.setRootLess(true);
		addComponents();
	}

	private void addComponents() {
		treeForm.add(addSite);
		treeForm.add(addTank);
		treeForm.add(addTray);
		treeForm.add(addBox);

		add(treeForm);
		add(tree);
	}

	@Override
	protected AbstractTree getTree() {
		return tree;
	}
	
	@Override
	public void onAddSiteSubmit(AjaxRequestTarget target) {
		resetModel();
		
		SiteDetailPanel detailPanel = new SiteDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, tree);
		detailPanel.initialisePanel();
		
		refreshDetailPanel(target, detailPanel);
	}

	@Override
	public void onAddTankSubmit(AjaxRequestTarget target) {
		resetModel();
		
		TankDetailPanel detailPanel = new TankDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, tree);
		detailPanel.initialisePanel();
		
		refreshDetailPanel(target, detailPanel);
	}

	@Override
	public void onAddTraySubmit(AjaxRequestTarget target) {
		resetModel();
		
		TrayDetailPanel detailPanel = new TrayDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, tree);
		detailPanel.initialisePanel();
		
		refreshDetailPanel(target, detailPanel);
	}

	@Override
	public void onAddBoxSubmit(AjaxRequestTarget target) {
		resetModel();
		
		BoxDetailPanel detailPanel = new BoxDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, tree);
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
	 * @param target
	 * @param detailPanel
	 */
	public void refreshDetailPanel(AjaxRequestTarget target, Panel detailPanel) {
		detailContainer.addOrReplace(detailPanel);
		detailContainer.setVisible(true);
		
		target.addComponent(feedbackPanel);
		target.addComponent(detailContainer);
		target.addComponent(detailPanel);
	}
}