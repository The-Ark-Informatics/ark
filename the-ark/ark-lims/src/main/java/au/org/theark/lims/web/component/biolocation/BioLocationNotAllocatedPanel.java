package au.org.theark.lims.web.component.biolocation;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.lims.model.vo.LimsVO;

/**
 * Panel displaying the label "Biospecimen not allocated" for the Biospecimen in context
 * 
 * @author cellis
 * 
 */
public class BioLocationNotAllocatedPanel extends Panel {
	/**
	 * 
	 */
	private static final long						serialVersionUID	= 1L;
	protected CompoundPropertyModel<LimsVO>	cpModel;
	private Label										notAllocatedLbl;
	private ArkBusyAjaxButton						allocateButton;
	private AbstractDetailModalWindow 			modalWindow;
	private Panel 										modalContentPanel = new EmptyPanel("content");

	public BioLocationNotAllocatedPanel(String id, CompoundPropertyModel<LimsVO> cpModel) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		this.cpModel = cpModel;
		
		initialisePanel();
		addComponents();
	}

	public void initialisePanel() {
		notAllocatedLbl = new Label("notAllocated", "<i>Biospecimen not allocated</i>");
		notAllocatedLbl.setEscapeModelStrings(false);
		
		allocateButton = new ArkBusyAjaxButton("allocate") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				modalContentPanel = new BioModalAllocateDetailPanel("content", modalWindow, cpModel);

				// Set the modalWindow title and content
				modalWindow.setTitle("Biospecimen Allocation");
				modalWindow.setContent(modalContentPanel);
				modalWindow.setWidthUnit("%");
				modalWindow.setInitialWidth(70);
				modalWindow.show(target);
				modalWindow.repaintComponent(BioLocationNotAllocatedPanel.this.getParent());
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
		};
		allocateButton.setDefaultFormProcessing(false);
		
		modalWindow = new AbstractDetailModalWindow("detailModalWindow") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {
				target.add(BioLocationNotAllocatedPanel.this.getParent());
			}

		};
	}

	public void addComponents() {
		add(notAllocatedLbl);
		add(allocateButton);
		add(modalWindow);
	}
}