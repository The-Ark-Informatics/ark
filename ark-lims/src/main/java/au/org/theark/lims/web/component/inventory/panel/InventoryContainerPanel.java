package au.org.theark.lims.web.component.inventory.panel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.component.inventory.form.ContainerForm;

public class InventoryContainerPanel extends AbstractInventoryPanel<LimsVO> {

	private static final long			serialVersionUID	= -8575670114976786294L;

	private ContainerForm				containerForm;
	private InventoryTreePanel			treePanel;
	private EmptyPanel					detailPanel;

	public InventoryContainerPanel(String id) {
		super(id);

		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<LimsVO>(new LimsVO());

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedbackPanel());
		containerForm.add(initialiseDetailContainer());
		
		/* Tree not in the containrForm (avoid lag of ArkFormVisitor) */ 
		add(initialiseTreeContainer());
		//TODO: This was added to overcome Wicket 1.5.0's strange multipart bug...shouldn't really be needed!
		containerForm.setMultiPart(true);
		add(containerForm);
	}
	
	/**
	 * Initialise the tree
	 * @return
	 */
	protected WebMarkupContainer initialiseTreeContainer() {
		treePanel = new InventoryTreePanel("treePanel", feedbackPanel, detailContainer, containerForm);
		treeContainer.addOrReplace(treePanel);
		return treeContainer;
	}

	/**
	 * Initialise empty Panel for placeholder
	 * @return
	 */
	protected WebMarkupContainer initialiseDetailContainer() {
		detailPanel = new EmptyPanel("detailPanel");
		detailPanel.setOutputMarkupPlaceholderTag(true);
		detailContainer.addOrReplace(detailPanel);
		return detailContainer;
	}
}