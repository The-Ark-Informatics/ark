package au.org.theark.admin.web.component.audit;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.admin.web.component.audit.form.ContainerForm;
import au.org.theark.admin.web.component.audit.form.SearchForm;
import au.org.theark.core.Constants;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.AuditVO;

public class SearchPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private ContainerForm containerForm;
	private FeedbackPanel feedbackPanel;
	private CompoundPropertyModel<AuditVO> cpModel;
	private ArkCrudContainerVO arkCrudContainerVO;
	
	public SearchPanel(String id, FeedbackPanel feedbackPanel, ContainerForm containerForm, CompoundPropertyModel<AuditVO> cpModel, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.containerForm = containerForm;
		this.feedbackPanel = feedbackPanel;
		this.cpModel = cpModel;
		this.arkCrudContainerVO = arkCrudContainerVO;
	}
	
	public void initialisePanel() {
		SearchForm searchForm = new SearchForm(Constants.SEARCH_FORM, cpModel, arkCrudContainerVO, feedbackPanel, containerForm);
		add(searchForm);
	}
	
}
