package au.org.theark.lims.web.component.inventory.tree.nestedtree;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;
import org.apache.wicket.extensions.markup.html.repeater.tree.NestedTree;
import org.apache.wicket.extensions.markup.html.repeater.tree.theme.WindowsTheme;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.Set;

public class NestedTreePanel<T> extends Panel {

	private static final long	serialVersionUID	= 1L;
	private ITreeProvider<T> provider;

	public NestedTreePanel(String id, ITreeProvider<T> provider) {
		super(id);
		this.setProvider(provider);
		initTree(null);
	}
	
	public NestedTreePanel(String id, ITreeProvider<T> provider, IModel<Set<T>> state) {
		super(id);
		this.setProvider(provider);
		initTree(state);
	}
	
	private void initTree(IModel<Set<T>> state) {
		NestedTree<T> tree = new NestedTree<T>("tree", provider, state)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Component newContentComponent(String id, IModel<T> model)
			{
				return new Label(id, model);
			}
		};

		tree.add(new WindowsTheme());

		add(tree);
	}



	public void setProvider(ITreeProvider<T> provider) {
		this.provider = provider;
	}

	public ITreeProvider<T> getProvider() {
		return provider;
	}
}
