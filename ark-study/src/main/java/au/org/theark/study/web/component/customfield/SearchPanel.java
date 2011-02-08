/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.customfield;

import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.model.study.entity.SubjectCustmFld;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.study.web.component.customfield.form.ContainerForm;
import au.org.theark.study.web.component.customfield.form.SearchForm;

/**
 * @author nivedann
 *
 */
public class SearchPanel  extends Panel{

	
	
	/**
	 * @param id
	 */
	public SearchPanel(	String id, 
						FeedbackPanel feedBackpanel,
						PageableListView<SubjectCustmFld> pageableListView,
						ContainerForm containerForm,
						DetailPanel detailPanel,
						ArkCrudContainerVO arkCrudContainerVO) {
		
		super(id);
		
		SearchForm searchForm = new SearchForm(au.org.theark.core.Constants.SEARCH_FORM,
												containerForm.getModel(), 
												pageableListView,
												feedBackpanel,
												detailPanel,
												arkCrudContainerVO);
		add(searchForm);		
		
	}
	
	
	

}
