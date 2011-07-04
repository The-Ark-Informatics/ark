package au.org.theark.lims.web.component.subjectSub;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.listeditor.AbstractListEditor;

public class DetailModalWindow extends AbstractDetailModalWindow
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4739679880093551297L;
	private ArkCrudContainerVO arkCrudContainerVo;
	private Form<?> listDetailForm;
	private AbstractListEditor<?> listEditor;
	
	public DetailModalWindow(String id)
	{
		super(id);
		this.setArkCrudContainerVo(new ArkCrudContainerVO());
	}
	
	public DetailModalWindow(String id, String title)
	{
		super(id, title);
		this.setArkCrudContainerVo(new ArkCrudContainerVO());
	}
	
	public DetailModalWindow(String id, String title, ArkCrudContainerVO arkCrudContainerVo)
	{
		super(id, title);
		this.setArkCrudContainerVo(arkCrudContainerVo);
	}

	public DetailModalWindow(String id, String title, ArkCrudContainerVO arkCrudContainerVo, Panel panel)
	{
		super(id, title, panel);
		this.setArkCrudContainerVo(arkCrudContainerVo);
	}
	
	protected void onCloseModalWindow(AjaxRequestTarget target)
	{
		listEditor.removeAll();
		target.addComponent(listDetailForm);
	}

	/**
	 * @return the listDetailForm
	 */
	public Form<?> getListDetailForm()
	{
		return listDetailForm;
	}

	/**
	 * @param listDetailForm the listDetailForm to set
	 */
	public void setListDetailForm(Form<?> listDetailForm)
	{
		this.listDetailForm = listDetailForm;
	}

	/**
	 * @param listEditor the listEditor to set
	 */
	public void setListEditor(AbstractListEditor<?> listEditor)
	{
		this.listEditor = listEditor;
	}

	/**
	 * @return the listEditor
	 */
	public AbstractListEditor<?> getListEditor()
	{
		return listEditor;
	}

	/**
	 * @param arkCrudContainerVo the arkCrudContainerVo to set
	 */
	public void setArkCrudContainerVo(ArkCrudContainerVO arkCrudContainerVo)
	{
		this.arkCrudContainerVo = arkCrudContainerVo;
	}

	/**
	 * @return the arkCrudContainerVo
	 */
	public ArkCrudContainerVO getArkCrudContainerVo()
	{
		return arkCrudContainerVo;
	}
}