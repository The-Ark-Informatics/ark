package au.org.theark.core.web.component.listeditor;

public class RemoveButton extends EditorButton
{

    /**
	 * 
	 */
	private static final long	serialVersionUID	= 3908287885911044925L;

	public RemoveButton(String id)
    {
        super(id);
        setDefaultFormProcessing(false);
    }

    @Override
    public void onSubmit()
    {
        int idx = getItem().getIndex();

        for (int i = idx + 1; i < getItem().getParent().size(); i++)
        {
            ListItem< ? > item = (ListItem< ? >)getItem().getParent().get(i);
            item.setIndex(item.getIndex() - 1);
        }

        getList().remove(idx);
        getEditor().remove(getItem());
    }

    @Override
    public boolean isEnabled()
    {
        return getEditor().checkRemove(getItem());
    }
/*
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
	}
	*/
}
