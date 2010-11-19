package au.org.theark.phenotypic.web.component.phenotypicImport.form;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.Constants;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.phenotypic.model.entity.PhenoCollection;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.web.component.phenotypicImport.DetailPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "serial", "unused" })
public class SearchForm extends AbstractSearchForm<PhenoCollectionVO>
{
	private PageableListView<PhenoCollection>				listView;
	private CompoundPropertyModel<PhenoCollectionVO>	cpmModel;
	private DetailPanel											detailPanel;

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<PhenoCollectionVO> model, PageableListView<PhenoCollection> listView, FeedbackPanel feedBackPanel, DetailPanel detailPanel,
			WebMarkupContainer listContainer, WebMarkupContainer searchMarkupContainer, WebMarkupContainer detailContainer, WebMarkupContainer detailPanelFormContainer,
			WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer)
	{

		super(id, model, detailContainer, detailPanelFormContainer, viewButtonContainer, editButtonContainer, searchMarkupContainer, listContainer, feedBackPanel);

		this.cpmModel = model;
		this.listView = listView;
		this.detailPanel = detailPanel;
		initialiseFieldForm();
		
		// Commented this section of code.Sub-classes should enforce this check until we have a mechanism to disable tabs.
		Long sessionPhenoCollectionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);		
		disableSearchButtons(sessionPhenoCollectionId, "There is no Phenotypic Collection in context. Please select a Phenotypic Collection");
		
	}

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<PhenoCollectionVO> compoundPropertyModel)
	{
		super(id, compoundPropertyModel);
		this.cpmModel = compoundPropertyModel;
		initialiseFieldForm();
	}

	private void initDropDownChoice()
	{
		// Initialise any drop-downs
	}

	public void initialiseFieldForm()
	{
		// For summary module, override the default search form buttons isVisible method to false
		newButton = new AjaxButton(Constants.NEW){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				//Make the details panel visible
				onNew(target);
			}
			
			@Override
			public boolean isVisible(){
				return false;
			}
		};
		
		
		searchButton = new AjaxButton(Constants.SEARCH){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				//Make the details panel visible
				onSearch(target);
			}
			
			public boolean isVisible(){
				return false;
			}
		};
		
		resetButton = new Button(Constants.RESET){
			public void onSubmit(){
				onReset();
			}
			
			public boolean isVisible(){
				return false;
			}
		};
		
		// Set up fields on the form
		initDropDownChoice();
		addFieldComponents();
	}

	private void addFieldComponents()
	{
		// Add the field components
//		// For summary module, disable buttons
//		this.viewButtonContainer.setVisible(false);
//		this.editButtonContainer.setVisible(false);
	}

	@Override
	protected void onNew(AjaxRequestTarget target)
	{
		// What to do on New button click
	}

	@Override
	protected void onSearch(AjaxRequestTarget target)
	{
		// What to do on Search button click
	}

	@Override
	protected boolean isSecure()
	{
		// Summary module has no buttons
		return false;
	}
}