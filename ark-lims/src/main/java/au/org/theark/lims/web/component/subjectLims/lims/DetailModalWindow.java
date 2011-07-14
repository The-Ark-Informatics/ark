package au.org.theark.lims.web.component.subjectLims.lims;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.web.component.AbstractDetailModalWindow;

public class DetailModalWindow extends AbstractDetailModalWindow
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4739679880093551297L;
	private Panel listDetailPanel;
	private Form<?> listDetailForm;
	
	public DetailModalWindow(String id)
	{
		super(id);
	}
	
	protected void onCloseModalWindow(AjaxRequestTarget target)
	{
		target.addComponent(listDetailForm);
		target.addComponent(listDetailPanel);
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
	 * @param listDetailPanel the listDetailPanel to set
	 */
	public void setListDetailPanel(Panel listDetailPanel)
	{
		this.listDetailPanel = listDetailPanel;
	}

	/**
	 * @return the listDetailPanel
	 */
	public Panel getListDetailPanel()
	{
		return listDetailPanel;
	}
}