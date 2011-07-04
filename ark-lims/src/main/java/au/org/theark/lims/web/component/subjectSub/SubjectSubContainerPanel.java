package au.org.theark.lims.web.component.subjectSub;

import org.apache.wicket.markup.html.WebMarkupContainer;

import au.org.theark.core.web.component.AbstractSubContainerPanel;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.component.subject.form.ContainerForm;

/**
 * @author cellis
 * 
 */
public class SubjectSubContainerPanel extends AbstractSubContainerPanel<LimsVO>
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3094500609047816882L;
	private WebMarkupContainer	arkContextMarkup;
	private ContainerForm	containerForm;
	private DetailModalWindow						modalWindow;

	public SubjectSubContainerPanel(String id, WebMarkupContainer arkContextMarkup, ContainerForm containerForm, LimsVO limsVo)
	{
		super(id);
		this.setArkContextMarkup(arkContextMarkup);
		
		initCrudContainerVO();

		add(initialiseFeedBackPanel());
		
		this.containerForm = containerForm;
		modalWindow = new DetailModalWindow("detailModalWindow");
		
		// Add Collection list detail panel
		au.org.theark.lims.web.component.subjectSub.bioCollection.ListDetailPanel collectionListDetailPanel = 
			new au.org.theark.lims.web.component.subjectSub.bioCollection.ListDetailPanel("collectionListDetailPanel", this.feedbackPanel, modalWindow);
		collectionListDetailPanel.initialisePanel(containerForm, modalWindow);
		add(collectionListDetailPanel);
		

		// Add Biospecimen list detail panel
		au.org.theark.lims.web.component.subjectSub.biospecimen.ListDetailPanel biospecimenListDetailPanel = 
			new au.org.theark.lims.web.component.subjectSub.biospecimen.ListDetailPanel("biospecimenListDetailPanel", this.feedbackPanel, modalWindow);
		biospecimenListDetailPanel.initialisePanel(containerForm, modalWindow);
		add(biospecimenListDetailPanel);
		
		//add(containerForm);
		add(modalWindow);
	}

	/**
	 * @param arkContextMarkup
	 *           the arkContextMarkup to set
	 */
	public void setArkContextMarkup(WebMarkupContainer arkContextMarkup)
	{
		this.arkContextMarkup = arkContextMarkup;
	}

	/**
	 * @return the arkContextMarkup
	 */
	public WebMarkupContainer getArkContextMarkup()
	{
		return arkContextMarkup;
	}
	
	/**
	 * @return the modalWindow
	 */
	public DetailModalWindow getModalWindow()
	{
		return modalWindow;
	}

	/**
	 * @param modalWindow the modalWindow to set
	 */
	public void setModalWindow(DetailModalWindow modalWindow)
	{
		this.modalWindow = modalWindow;
	}

	/**
	 * @param subContainerForm the subContainerForm to set
	 */
	public void setSubContainerForm(ContainerForm subContainerForm)
	{
		this.containerForm = subContainerForm;
	}

	/**
	 * @return the subContainerForm
	 */
	public ContainerForm getSubContainerForm()
	{
		return containerForm;
	}
}