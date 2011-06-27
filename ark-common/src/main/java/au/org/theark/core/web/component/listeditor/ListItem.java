package au.org.theark.core.web.component.listeditor;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class ListItem<T> extends Item<T>
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2012719539470105336L;

	public ListItem(String id, int index)
	{
		super(id, index);
		setModel(new ListItemModel());
	}

	private class ListItemModel extends AbstractReadOnlyModel<T>
	{
		/**
		 * 
		 */
		private static final long	serialVersionUID	= -1830140228837079498L;

		@SuppressWarnings("unchecked")
		@Override
		public T getObject()
		{
			return ((AbstractListEditor<T>) ListItem.this.getParent()).items.get(getIndex());
		}
	}
}
