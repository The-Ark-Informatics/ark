/**
 * 
 */
package au.org.theark.phenotypic.web.component.phenoUpload;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.phenotypic.model.entity.Upload;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.web.component.phenoUpload.form.ContainerForm;
import au.org.theark.phenotypic.web.component.phenoUpload.form.SearchForm;

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
	private WebMarkupContainer			detailContainer;
	private PageableListView<Upload>	listView;
	private ContainerForm				containerForm;
	private DetailPanel							detailPanel;
	private WebMarkupContainer viewButtonContainer;
	private WebMarkupContainer editButtonContainer;
	private WebMarkupContainer detailPanelFormContainer;

	/* Constructor */
	public SearchPanel(	String id, 
					FeedbackPanel feedBackPanel, 
					WebMarkupContainer searchMarkupContainer, 
					PageableListView<Upload> listView, 
					WebMarkupContainer resultListContainer,
					WebMarkupContainer detailPanelContainer, 
					DetailPanel detail, 
					ContainerForm containerForm,
					WebMarkupContainer viewButtonContainer,
					WebMarkupContainer editButtonContainer,
					WebMarkupContainer detailPanelFormContainer)
	{
		super(id);
		this.searchMarkupContainer = searchMarkupContainer;
		this.listView = listView;
		this.feedBackPanel = feedBackPanel;
		this.detailContainer = detailPanelContainer;
		this.detailPanel = detail;
		this.containerForm = containerForm;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
		listContainer = resultListContainer;
	}

	public void initialisePanel()
	{
		SearchForm searchForm = new SearchForm(au.org.theark.core.Constants.SEARCH_FORM, 
												(CompoundPropertyModel<UploadVO>) containerForm.getModel(),
												listView,
												feedBackPanel,
												detailPanel,
												listContainer,
												searchMarkupContainer,
												detailContainer,
												detailPanelFormContainer,
												viewButtonContainer,
												editButtonContainer
												);
		add(searchForm);
	}
}