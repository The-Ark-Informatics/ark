package au.org.theark.lims.web.component.inventory.tree.nestedtree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;

import wickettree.ITreeProvider;
import wickettree.NestedTree;
import wickettree.theme.HumanTheme;
import wickettree.theme.WindowsTheme;

public class NestedTreePanel<T> extends Panel {
	/**
	 * 
	 */
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
		
		tree.add(new Behavior()
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				ResourceReference theme;
				List<ResourceReference> themes = new ArrayList<ResourceReference>();

				themes.add(new WindowsTheme());
				themes.add(new HumanTheme());

				theme = themes.get(0);
				response.renderCSSReference(theme);
			}
		});
		
		add(tree);
	}



	public void setProvider(ITreeProvider<T> provider) {
		this.provider = provider;
	}

	public ITreeProvider<T> getProvider() {
		return provider;
	}
}
