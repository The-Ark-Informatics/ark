package au.org.theark.core.web.component.listeditor;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPreprocessingCallDecorator;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.model.IModel;

@SuppressWarnings("unchecked")
public abstract class AjaxEditorButton extends AjaxButton
{
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 2239994447099355647L;

	private final IModel				confirm;
	private transient ListItem<?>	parent;

	public AjaxEditorButton(String id, IModel confirm, IModel label)
	{
		super(id);
		this.setModel(label);
		this.confirm = confirm;
	}

	protected final ListItem<?> getItem()
	{
		if (parent == null)
		{
			parent = findParent(ListItem.class);
		}
		return parent;
	}

	protected final List<?> getList()
	{
		return getEditor().items;
	}

	protected final AbstractListEditor<?> getEditor()
	{
		return (AbstractListEditor<?>) getItem().getParent();
	}

	@Override
	protected void onDetach()
	{
		parent = null;
		super.onDetach();
	}

	@Override
	protected IAjaxCallDecorator getAjaxCallDecorator()
	{
		return new AjaxPreprocessingCallDecorator(super.getAjaxCallDecorator())
		{
			private static final long	serialVersionUID	= 7495281332320552876L;

			@Override
			public CharSequence preDecorateScript(CharSequence script)
			{
				return 	"if(!confirm('" + confirm.getObject() + "'))" + 
							"{ " + "	return false " + "} " + 
							"else " + 
							"{ " + 
							"	this.disabled = true; " + "};" + 
							script;
			}
		};
	}
}