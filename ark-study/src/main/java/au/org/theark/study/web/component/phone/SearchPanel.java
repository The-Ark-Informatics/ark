/**
 * 
 */
package au.org.theark.study.web.component.phone;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.vo.PhoneVO;
import au.org.theark.study.web.component.phone.form.ContainerForm;
import au.org.theark.study.web.component.phone.form.SearchForm;

/**
 * @author Nivedan
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
	private PageableListView<Phone> pageableListView;
	private ContainerForm containerForm;
	
	/* Constructor */
	public SearchPanel(	String id, 
					FeedbackPanel feedBackPanel, 
					WebMarkupContainer searchMarkupContainer, 
					PageableListView<Phone> listView, 
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
		this.containerForm = containerForm;
	}


	public void initialisePanel(CompoundPropertyModel<PhoneVO> phoneVOCpm)
	{

		SearchForm searchForm = new SearchForm(	au.org.theark.core.Constants.SEARCH_FORM, 
				phoneVOCpm,
				pageableListView,
				feedBackPanel,
				listContainer,
				searchMarkupContainer,
				detailsContainer,
				detailFormContainer,
				viewButtonContainer,
				editButtonContainer,
				containerForm		);
		
		add(searchForm);
	}
}