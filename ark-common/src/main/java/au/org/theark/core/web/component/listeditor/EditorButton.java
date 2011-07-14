package au.org.theark.core.web.component.listeditor;

import java.util.List;

import org.apache.wicket.markup.html.form.Button;

public abstract class EditorButton extends Button
{
    /**
	 * 
	 */
	private static final long	serialVersionUID	= 2239994447099355647L;
	private transient ListItem< ? > parent;

    public EditorButton(String id)
    {
        super(id);
    }

    protected final ListItem< ? > getItem()
    {
        if (parent == null)
        {
            parent = findParent(ListItem.class);
        }
        return parent;
    }

    protected final List< ? > getList()
    {
        return getEditor().items;
    }

    protected final AbstractListEditor< ? > getEditor()
    {
        return (AbstractListEditor< ? >)getItem().getParent();
    }


    @Override
    protected void onDetach()
    {
        parent = null;
        super.onDetach();
    }

}