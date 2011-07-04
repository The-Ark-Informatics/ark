package au.org.theark.lims.web.component.subjectSub.bioCollection;

import org.apache.wicket.ajax.AjaxRequestTarget;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.lims.web.component.subject.form.ContainerForm;

public class DetailModalWindow extends AbstractDetailModalWindow
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4739679880093551297L;
	private DetailPanel detailPanel;

	public DetailModalWindow(String id, ArkCrudContainerVO arkCrudContainerVo)
	{
		super(id, "Collection Detail");
		detailPanel = new DetailPanel("content", this);
		initialiseContentPanel(detailPanel);
	}
	
	public DetailModalWindow(String id, ArkCrudContainerVO arkCrudContainerVo, ContainerForm containerForm)
	{
		super(id, "Collection Detail");
		detailPanel = new DetailPanel("content", this, containerForm);
		initialiseContentPanel(detailPanel);
	}

	/**
	 * @return the detailPanel
	 */
	public DetailPanel getDetailPanel()
	{
		return detailPanel;
	}

	/**
	 * @param detailPanel the detailPanel to set
	 */
	public void setDetailPanel(DetailPanel detailPanel)
	{
		this.detailPanel = detailPanel;
	}

	@Override
	protected void onCloseModalWindow(AjaxRequestTarget target)
	{
		
	}
}