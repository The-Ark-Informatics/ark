package au.org.theark.lims.web.component.subject.bioCollection;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.component.subject.bioCollection.form.ListDetailForm;

public class ListDetailPanel extends Panel
{	
	private ListDetailForm listDetailForm;
	protected FeedbackPanel feedBackPanel;

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2329695170775963267L;

	public ListDetailPanel(String id, FeedbackPanel feedBackPanel)
	{
		super(id);
		this.feedBackPanel = feedBackPanel; 
	}
	
	public void initialisePanel()
	{
		listDetailForm = new ListDetailForm("listDetailForm", new CompoundPropertyModel<LimsVO>(new LimsVO()), feedBackPanel);
		listDetailForm.initialiseForm();
		add(listDetailForm);
	}
	
	/**
	 * @return the listBioCollectionsForm
	 */
	public ListDetailForm getListBioCollectionsForm()
	{
		return listDetailForm;
	}

	/**
	 * @param listBioCollectionsForm the listBioCollectionsForm to set
	 */
	public void setListBioCollectionsForm(ListDetailForm listBioCollectionsForm)
	{
		this.listDetailForm = listBioCollectionsForm;
	}

}
