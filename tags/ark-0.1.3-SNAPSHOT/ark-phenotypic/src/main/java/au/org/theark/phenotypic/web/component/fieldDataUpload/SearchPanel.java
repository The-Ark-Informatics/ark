/**
 * 
 */
package au.org.theark.phenotypic.web.component.fieldDataUpload;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.pheno.entity.PhenoUpload;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.web.component.fieldDataUpload.form.ContainerForm;
import au.org.theark.phenotypic.web.component.fieldDataUpload.form.SearchForm;

/**
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public class SearchPanel extends Panel
{
	private FeedbackPanel				feedBackPanel;
	private WebMarkupContainer			searchMarkupContainer;
	private WebMarkupContainer			listContainer;
	private WebMarkupContainer			wizardContainer;
	private PageableListView<PhenoUpload>	listView;
	private ContainerForm				containerForm;
	private WizardPanel							wizardPanel;
	private WebMarkupContainer viewButtonContainer;
	private WebMarkupContainer editButtonContainer;
	private WebMarkupContainer wizardPanelFormContainer;

	/* Constructor */
	public SearchPanel(	String id, 
					FeedbackPanel feedBackPanel, 
					WebMarkupContainer searchMarkupContainer, 
					PageableListView<PhenoUpload> listView, 
					WebMarkupContainer resultListContainer,
					WebMarkupContainer wizardPanelContainer, 
					WizardPanel wizard, 
					ContainerForm containerForm,
					WebMarkupContainer viewButtonContainer,
					WebMarkupContainer editButtonContainer,
					WebMarkupContainer wizardPanelFormContainer)
	{
		super(id);
		this.searchMarkupContainer = searchMarkupContainer;
		this.listView = listView;
		this.feedBackPanel = feedBackPanel;
		this.wizardContainer = wizardPanelContainer;
		this.wizardPanel = wizard;
		this.containerForm = containerForm;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.wizardPanelFormContainer = wizardPanelFormContainer;
		listContainer = resultListContainer;
	}

	public void initialisePanel()
	{
		SearchForm searchForm = new SearchForm(au.org.theark.core.Constants.SEARCH_FORM, 
												(CompoundPropertyModel<UploadVO>) containerForm.getModel(),
												listView,
												feedBackPanel,
												wizardPanel,
												listContainer,
												searchMarkupContainer,
												wizardContainer,
												wizardPanelFormContainer,
												viewButtonContainer,
												editButtonContainer
												);
		add(searchForm);
	}
}