package au.org.theark.core.web.component.listeditor;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

public abstract class AjaxListDeleteButton extends AjaxEditorButton
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8812772472624903905L;

	@SuppressWarnings("unchecked")
	public AjaxListDeleteButton(String id, IModel confirm, IModel label)
    {
        super(id, confirm, label);
        setDefaultFormProcessing(false);
    }

    @Override
    public boolean isEnabled()
    {
        return getEditor().checkRemove(getItem());
    }

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form)
	{
		int idx = getItem().getIndex();

      for (int i = idx + 1; i < getItem().getParent().size(); i++)
      {
          ListItem< ? > item = (ListItem< ? >)getItem().getParent().get(i);
          item.setIndex(item.getIndex() - 1);
      }

      getList().remove(idx);
      getEditor().remove(getItem());
      
      // only repaint ListDetailForm
      target.addComponent(form);
      onDeleteConfirmed(target, form);
	}
	
	@Override
	protected void onError(AjaxRequestTarget target, Form<?> form)
	{
		// TODO Auto-generated method stub
		super.onError(target, form);
	}

	protected abstract void onDeleteConfirmed(AjaxRequestTarget target, Form<?> form);
}
