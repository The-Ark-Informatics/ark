/**
 * 
 */
package au.org.theark.core.web.component.customfield;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.pheno.entity.Field;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.web.component.customfield.form.ContainerForm;
import au.org.theark.core.web.component.customfield.form.SearchForm;

/**
 * @author elam
 * 
 */
@SuppressWarnings("serial")
public class SearchPanel extends Panel {
	
	private CompoundPropertyModel<CustomFieldVO> cpModel;

	private FeedbackPanel			feedbackPanel;
	private ArkCrudContainerVO		arkCrudContainerVO;

	/* Constructor */
	public SearchPanel(String id, CompoundPropertyModel<CustomFieldVO> cpModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel) {
		super(id);
		this.cpModel = cpModel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
	}

	public void initialisePanel() {
		SearchForm searchForm = new SearchForm(au.org.theark.core.Constants.SEARCH_FORM, cpModel, feedbackPanel, arkCrudContainerVO);
		add(searchForm);
	}
}