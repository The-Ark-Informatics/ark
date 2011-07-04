package au.org.theark.lims.web.component.subjectSub.bioCollection;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.component.subjectSub.DetailModalWindow;
import au.org.theark.lims.web.component.subjectSub.bioCollection.form.ListDetailForm;
import au.org.theark.lims.web.component.subject.form.ContainerForm;

public class ListDetailPanel extends Panel
{	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2329695170775963267L;
	protected FeedbackPanel feedbackPanel;
	private ListDetailForm listDetailForm;
	
	public ListDetailPanel(String id, FeedbackPanel feedBackPanel)
	{
		super(id);
		this.feedbackPanel = feedBackPanel;
	}

	public ListDetailPanel(String id, FeedbackPanel feedBackPanel, CompoundPropertyModel<LimsVO> compoundPropertyModel)
	{
		super(id);
		this.feedbackPanel = feedBackPanel;
	}

	public ListDetailPanel(String id, FeedbackPanel feedbackPanel, DetailModalWindow modalWindow)
	{
		super(id);
		this.feedbackPanel = feedbackPanel;
	}

	public void initialisePanel()
	{
		listDetailForm = new ListDetailForm("collectionListDetailForm", feedbackPanel, new CompoundPropertyModel<LimsVO>(new LimsVO()));
		listDetailForm.initialiseForm();
		add(listDetailForm);
	}
	
	public void initialisePanel(ContainerForm containerForm, DetailModalWindow modalWindow)
	{
		listDetailForm = new ListDetailForm("collectionListDetailForm", feedbackPanel, containerForm, modalWindow);
		listDetailForm.initialiseForm();
		add(listDetailForm);
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
