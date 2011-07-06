package au.org.theark.lims.web.component.subjectSub.biospecimen;

import org.apache.wicket.ajax.AjaxRequestTarget;

import au.org.theark.core.web.component.AbstractDetailModalWindow;

public class DetailModalWindow extends AbstractDetailModalWindow
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2377548208519099827L;
	private BiospecimenModalDetailPanel detailPanel;


	public DetailModalWindow(String id) {
		super(id);
	}

	/**
	 * @return the detailPanel
	 */
	public BiospecimenModalDetailPanel getDetailPanel()
	{
		return detailPanel;
	}

	/**
	 * @param detailPanel the detailPanel to set
	 */
	public void setDetailPanel(BiospecimenModalDetailPanel detailPanel)
	{
		this.detailPanel = detailPanel;
	}

	@Override
	protected void onCloseModalWindow(AjaxRequestTarget target)
	{
		// TODO Auto-generated method stub
		
	}
}