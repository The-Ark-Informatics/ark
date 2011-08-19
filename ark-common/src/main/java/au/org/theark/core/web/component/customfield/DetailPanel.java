/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.web.component.customfield;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.web.component.customfield.form.DetailForm;

/**
 * @author elam
 * 
 */
public class DetailPanel extends Panel {
	private DetailForm			detailForm;
	private FeedbackPanel		feedbackPanel;

	private CompoundPropertyModel<CustomFieldVO> cpModel;
	private ArkCrudContainerVO arkCrudContainerVO;

	public DetailPanel(String id, FeedbackPanel feedbackPanel, CompoundPropertyModel<CustomFieldVO> cpModel, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, cpModel);
		this.feedbackPanel = feedbackPanel;
		this.cpModel = cpModel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.setOutputMarkupPlaceholderTag(true);
		
		initialisePanel();
	}

	public void initialisePanel() {
		detailForm = new DetailForm("detailForm", cpModel, feedbackPanel, arkCrudContainerVO);
		detailForm.initialiseDetailForm();
		add(detailForm);
	}

	public DetailForm getDetailForm() {
		return detailForm;
	}

	public void setDetailForm(DetailForm detailsForm) {
		this.detailForm = detailsForm;
	}
}