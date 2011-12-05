package au.org.theark.lims.web.component.inventory.panel;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LabelIconPanel;
import org.apache.wicket.model.IModel;

public class InventoryLinkIconPanel extends LabelIconPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6175278892194404598L;

	/**
	 * Constructs the panel.
	 * 
	 * @param id
	 *            component id
	 * @param model
	 *            model that is used to access the TreeNode
	 * @param tree
	 */
	public InventoryLinkIconPanel(String id, IModel<Object> model, BaseTree tree)
	{
		super(id, model, tree);
	}

	/**
	 * @see org.apache.wicket.markup.html.tree.LabelIconPanel#addComponents(org.apache.wicket.model.IModel,
	 *      org.apache.wicket.markup.html.tree.BaseTree)
	 */
	@Override
	protected void addComponents(final IModel<Object> model, final BaseTree tree)
	{
		BaseTree.ILinkCallback callback = new BaseTree.ILinkCallback()
		{
			private static final long serialVersionUID = 1L;

			public void onClick(AjaxRequestTarget target)
			{
				onNodeLinkClicked(model.getObject(), tree, target);
			}
		};

		MarkupContainer link = tree.newLink("iconLink", callback);
		add(link);
		link.add(newImageComponent("icon", tree, model));

		link = tree.newLink("contentLink", callback);
		add(link);
		link.add(newContentComponent("content", tree, model));
	}

	/**
	 * Handler invoked when the link is clicked. By default makes the node selected
	 * 
	 * @param node
	 * @param tree
	 * @param target
	 */
	protected void onNodeLinkClicked(Object node, BaseTree tree, AjaxRequestTarget target)
	{
		tree.getTreeState().selectNode(node, !tree.getTreeState().isNodeSelected(node));
		tree.updateTree(target);
	}
}