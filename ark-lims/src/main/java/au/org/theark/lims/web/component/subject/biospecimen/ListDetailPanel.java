package au.org.theark.lims.web.component.subject.biospecimen;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.component.subject.biospecimen.form.ListDetailForm;

public class ListDetailPanel extends Panel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7224168117680252835L;
	protected FeedbackPanel feedBackPanel;
	protected FeedbackPanel listDetailFeedBackPanel;
	private ListDetailForm listDetailForm;
	
	public ListDetailPanel(String id, FeedbackPanel feedBackPanel)
	{
		super(id);
		this.feedBackPanel = feedBackPanel;
		
		// Local feedback panel to ListDetail
		add(initialiseFeedBackPanel());
	}

	public void initialisePanel()
	{
		listDetailForm = new ListDetailForm("biospecimenListDetailForm", new CompoundPropertyModel<LimsVO>(new LimsVO()), feedBackPanel);
		listDetailForm.initialiseForm();
		add(listDetailForm);
	}
	
	protected WebMarkupContainer initialiseFeedBackPanel(){
		listDetailFeedBackPanel= new FeedbackPanel("biospecimenListDetailFeedback");
		listDetailFeedBackPanel.setOutputMarkupId(true);
		return listDetailFeedBackPanel;
	}

	/**
	 * @return the listDetailForm
	 */
	public ListDetailForm getListDetailForm()
	{
		return listDetailForm;
	}

	/**
	 * @param listDetailForm the listDetailForm to set
	 */
	public void setListDetailForm(ListDetailForm listDetailForm)
	{
		this.listDetailForm = listDetailForm;
	}
}