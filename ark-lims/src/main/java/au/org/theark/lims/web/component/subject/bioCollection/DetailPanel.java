package au.org.theark.lims.web.component.subject.bioCollection;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.lims.web.component.subject.bioCollection.form.DetailForm;
import au.org.theark.lims.web.component.subject.bioCollection.form.ListDetailForm;




@SuppressWarnings("serial")
public class DetailPanel extends Panel
{
	protected WebMarkupContainer detailPanelFormContainer;
	protected DetailForm							detailForm;
	protected FeedbackPanel						feedBackPanel;
	protected ListDetailForm						containerForm;
	protected ArkCrudContainerVO	arkCrudContainerVo;

	public DetailPanel(	String id, 
					FeedbackPanel feedBackPanel,
					ListDetailForm containerForm,
					WebMarkupContainer detailPanelFormContainer)
	{
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.containerForm = containerForm;
		this.detailPanelFormContainer = detailPanelFormContainer;
	}

	public DetailPanel(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVo, ListDetailForm containerForm)
	{
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.containerForm = containerForm;
		this.arkCrudContainerVo = arkCrudContainerVo;
	}

	public void initialisePanel()
	{
		detailForm = new DetailForm(	"detailForm", 
										feedBackPanel, 
										arkCrudContainerVo,
										containerForm);
		
		detailForm.initialiseDetailForm();
		add(detailForm);
	}

	public DetailForm getDetailForm()
	{
		return detailForm;
	}

	public void setDetailForm(DetailForm detailsForm)
	{
		this.detailForm = detailsForm;
	}
}