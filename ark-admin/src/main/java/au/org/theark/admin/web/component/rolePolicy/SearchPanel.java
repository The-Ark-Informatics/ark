package au.org.theark.admin.web.component.rolePolicy;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.web.component.rolePolicy.form.ContainerForm;
import au.org.theark.admin.web.component.rolePolicy.form.SearchForm;
import au.org.theark.core.Constants;
import au.org.theark.core.vo.ArkCrudContainerVO;

public class SearchPanel extends Panel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2689739291154679914L;
	private ContainerForm containerForm;
	private FeedbackPanel feedBackPanel;
	private CompoundPropertyModel<AdminVO> cpmModel;
	private ArkCrudContainerVO arkCrudContainerVO;
	
	/**
	 * Constructor That uses the VO
	 * @param id
	 * @param studyCrudContainerVO
	 */
	public SearchPanel(String id, FeedbackPanel feedbackPanel, ContainerForm containerForm, CompoundPropertyModel<AdminVO> cpmModel, ArkCrudContainerVO arkCrudContainerVO){
		
		super(id);
		this.containerForm = containerForm;
		this.feedBackPanel = feedbackPanel;
		this.cpmModel = cpmModel;
		this.arkCrudContainerVO = arkCrudContainerVO;
	}
	
	public void initialisePanel()
	{
		SearchForm searchForm = new SearchForm(Constants.SEARCH_FORM, cpmModel, arkCrudContainerVO, feedBackPanel, containerForm);
		add(searchForm);
	}
}
