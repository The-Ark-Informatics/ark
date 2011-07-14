package au.org.theark.lims.web.component.subjectLims.lims.biospecimen;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.subjectLims.lims.bioCollection.BioCollectionListPanel;
import au.org.theark.lims.web.component.subjectLims.lims.biospecimen.form.BiospecimenListForm;

public class BiospecimenListPanel extends Panel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7224168117680252835L;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService iLimsService;
	
	protected CompoundPropertyModel<LimsVO> cpModel;

	protected FeedbackPanel feedbackPanel;
	private BiospecimenListForm listDetailForm;
	
	public BiospecimenListPanel(String id, FeedbackPanel feedbackPanel, /*AbstractDetailModalWindow modalWindow, */CompoundPropertyModel<LimsVO> cpModel)
	{
		super(id);
		this.feedbackPanel = feedbackPanel;
		this.cpModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
		initialisePanel();
		setOutputMarkupPlaceholderTag(true);
	}
	
	public void initialisePanel()
	{
		final BiospecimenListPanel panelToRepaint = this;
		AbstractDetailModalWindow modalWindow = new AbstractDetailModalWindow("detailModalWindow") {

			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {
				target.addComponent(panelToRepaint);
			}
		
		};		
		listDetailForm = new BiospecimenListForm("biospecimenListForm", feedbackPanel, modalWindow, cpModel);
		listDetailForm.initialiseForm();
		add(listDetailForm);
	}

}