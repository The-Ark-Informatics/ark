/**
 * 
 */
package au.org.theark.study.web.component.consentFile;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.study.entity.ConsentFile;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.study.web.component.consentFile.form.ContainerForm;
import au.org.theark.study.web.component.consentFile.form.SearchForm;


/**
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public class SearchPanel extends Panel
{

	private FeedbackPanel feedBackPanel;
	private WebMarkupContainer searchMarkupContainer;
	private WebMarkupContainer listContainer;
	private WebMarkupContainer detailsContainer;
	private WebMarkupContainer viewButtonContainer;
	private WebMarkupContainer editButtonContainer;
	private WebMarkupContainer detailFormContainer;
	private PageableListView<ConsentFile> pageableListView;
	
	/* Constructor */
	public SearchPanel(	String id, 
					FeedbackPanel feedBackPanel, 
					WebMarkupContainer searchMarkupContainer, 
					PageableListView<ConsentFile> listView, 
					WebMarkupContainer resultListContainer,
					WebMarkupContainer detailPanelContainer, 
					DetailPanel detail, 
					ContainerForm containerForm,
					WebMarkupContainer viewButtonContainer,
					WebMarkupContainer editButtonContainer,
					WebMarkupContainer detailPanelFormContainer)
	{
		super(id);
		this.searchMarkupContainer =  searchMarkupContainer;
		this.pageableListView = listView;
		this.feedBackPanel = feedBackPanel;
		this.listContainer = resultListContainer;
		this.detailsContainer = detailPanelContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailFormContainer = detailPanelFormContainer;
	}


	public void initialisePanel(CompoundPropertyModel<ConsentVO> consentVOCpm)
	{

		SearchForm searchForm = new SearchForm(	au.org.theark.core.Constants.SEARCH_FORM, 
				consentVOCpm,
				pageableListView,
				feedBackPanel,
				listContainer,
				searchMarkupContainer,
				detailsContainer,
				detailFormContainer,
				viewButtonContainer,
				editButtonContainer	);
		
		add(searchForm);
	}
}