package au.org.theark.lims.web.component.subjectLims.lims.bioCollection;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.component.subjectLims.lims.bioCollection.form.BioCollectionListForm;

public class BioCollectionListPanel extends Panel
{	
	/**
	 * 
	 */
	private static final long serialVersionUID	= -2329695170775963267L;

	protected CompoundPropertyModel<LimsVO> cpModel;
	
	protected FeedbackPanel feedbackPanel;
	protected BioCollectionListForm bioCollectionListForm;

	public BioCollectionListPanel(String id, FeedbackPanel feedbackPanel, /*AbstractDetailModalWindow modalWindow, */CompoundPropertyModel<LimsVO> cpModel)
	{
		super(id);
		this.feedbackPanel = feedbackPanel;
		this.cpModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
	
		initialisePanel();
		setOutputMarkupPlaceholderTag(true);
	}
	
	public void initialisePanel()
	{
		final BioCollectionListPanel panelToRepaint = this;
		AbstractDetailModalWindow modalWindow = new AbstractDetailModalWindow("detailModalWindow") {

			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {
				target.addComponent(panelToRepaint);
			}
		
		};		
		
		bioCollectionListForm = new BioCollectionListForm("collectionListForm", feedbackPanel, modalWindow, cpModel);
		bioCollectionListForm.initialiseForm();

		add(bioCollectionListForm);
	}

}
