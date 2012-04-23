package au.org.theark.phenotypic.web.component.customfieldgroup;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldGroupVO;
import au.org.theark.phenotypic.web.component.customfieldgroup.form.SearchForm;

/**
 * @author nivedann
 *
 */
public class SearchPanel extends Panel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FeedbackPanel	feedbackPanel;
	private ArkCrudContainerVO	arkCrudContainerVO;
	private CompoundPropertyModel<CustomFieldGroupVO> cpModel;
	
	/**
	 * Constructor
	 * @param id
	 */
	public SearchPanel(String id, CompoundPropertyModel<CustomFieldGroupVO> cpModel,ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel) {
		super(id);
		this.feedbackPanel = feedBackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.cpModel = cpModel;
	}
	
	/**
	 * Instantiate the SearchForm and link it with the Panel
	 */
	public void initialisePanel() {
		
		SearchForm searchForm = new SearchForm(au.org.theark.core.Constants.SEARCH_FORM,cpModel,feedbackPanel,arkCrudContainerVO);
		add(searchForm);
		
	}

}
