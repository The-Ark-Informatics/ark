package au.org.theark.lims.web.component.subjectSub.bioCollection;

import org.apache.wicket.ajax.AjaxRequestTarget;

import au.org.theark.core.web.component.AbstractDetailModalWindow;

public class DetailModalWindow extends AbstractDetailModalWindow
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4739679880093551297L;
	private CollectionModalDetailPanel detailPanel;

	public DetailModalWindow(String id) {
		super(id);
	}
	
	/**
	 * @return the detailPanel
	 */
	public CollectionModalDetailPanel getDetailPanel()
	{
		return detailPanel;
	}

	/**
	 * @param detailPanel the detailPanel to set
	 */
	public void setDetailPanel(CollectionModalDetailPanel detailPanel)
	{
		this.detailPanel = detailPanel;
	}

	@Override
	protected void onCloseModalWindow(AjaxRequestTarget target)
	{
		
	}
}