package au.org.theark.study.web.component.pedigree;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.study.model.vo.PedigreeVo;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.pedigree.form.SaveForm;

public class SavePanel extends Panel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private FeedbackPanel					feedBackPanel;
	private ArkCrudContainerVO				arkCrudContainerVO;
	private AbstractDetailModalWindow 	modalWindow;
	

	public SavePanel(String id,FeedbackPanel feedBackPanel,ArkCrudContainerVO arkCrudContainerVO, AbstractDetailModalWindow 	modalWindow) {
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.modalWindow = modalWindow;
		// TODO Auto-generated constructor stub
	}
	
	
	public void initialisePanel(CompoundPropertyModel<PedigreeVo> studyCompCpm) {

		SaveForm saveForm = new SaveForm(Constants.SAVE_FORM,feedBackPanel, arkCrudContainerVO, modalWindow);
		add(saveForm);
	}

}
