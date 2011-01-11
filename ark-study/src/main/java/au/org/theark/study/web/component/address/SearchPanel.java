/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.address;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.vo.AddressVO;
import au.org.theark.study.web.component.address.form.ContainerForm;
import au.org.theark.study.web.component.address.form.SearchForm;


/**
 * @author nivedann
 *
 */
public class SearchPanel extends Panel{

	private FeedbackPanel feedBackPanel;
	private WebMarkupContainer searchMarkupContainer;
	private WebMarkupContainer listContainer;
	private WebMarkupContainer detailsContainer;
	private WebMarkupContainer viewButtonContainer;
	private WebMarkupContainer editButtonContainer;
	private WebMarkupContainer detailFormContainer;
	private PageableListView<Address> pageableListView;

	
	/* Constructor */
	public SearchPanel(	String id, 
					FeedbackPanel feedBackPanel, 
					WebMarkupContainer searchMarkupContainer, 
					PageableListView<Address> listView, 
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
	
	public void initialisePanel(CompoundPropertyModel<AddressVO> addressVoCpm)
	{

		SearchForm searchForm = new SearchForm(	au.org.theark.core.Constants.SEARCH_FORM, 
				addressVoCpm,
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
