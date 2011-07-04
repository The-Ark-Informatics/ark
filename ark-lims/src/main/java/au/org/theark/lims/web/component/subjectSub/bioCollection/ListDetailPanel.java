package au.org.theark.lims.web.component.subjectSub.bioCollection;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.lims.web.component.subject.form.ContainerForm;
import au.org.theark.lims.web.component.subjectSub.DetailModalWindow;
import au.org.theark.lims.web.component.subjectSub.bioCollection.form.ListDetailForm;

public class ListDetailPanel extends Panel
{	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2329695170775963267L;
	protected FeedbackPanel feedbackPanel;
	private ListDetailForm listDetailForm;

	public ListDetailPanel(String id, FeedbackPanel feedbackPanel, DetailModalWindow modalWindow)
	{
		super(id);
		this.feedbackPanel = feedbackPanel;
		setOutputMarkupPlaceholderTag(true);
	}
	
	public void initialisePanel(ContainerForm containerForm, DetailModalWindow modalWindow)
	{
		listDetailForm = new ListDetailForm("collectionListDetailForm", feedbackPanel, containerForm, modalWindow, this);
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
