package au.org.theark.core.web.component.hideshow;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class HideShowPanel extends Panel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 678875273789181319L;
	private Panel					panel	= new Panel("panel");
	private AjaxLink<Void>		showHideLink;
	private Image					showHideImage;
	private boolean 				showPanel = false;
	private ResourceReference	showPanelImage				= new ResourceReference(HideShowPanel.class, "showPanelImage.png");
	private ResourceReference	hidePanelImage					= new ResourceReference(HideShowPanel.class, "hidePanelImage.png");
	private IModel<String>		titleModel;

	public HideShowPanel(String id, IModel<String> titleModel, Panel panel, boolean showPanel) {
		super(id);
		setOutputMarkupId(true);
		setOutputMarkupPlaceholderTag(true);
		this.titleModel = titleModel;
		this.panel = panel;
		this.showPanel = showPanel;
		initialisePanel();
		addComponents();
	}

	public void initialisePanel() {
		showHideLink = new AjaxLink<Void>("showHideLink") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 2650687855479265673L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				panel.setVisible(!showPanel);
				target.addComponent(panel);
				target.addComponent(showHideImage);
			}

		};
		
		showHideImage = new Image("showHideImage") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 3966113343164803942L;

			@Override
			public ResourceReference getImageResourceReference() {
				return showPanel ? hidePanelImage : showPanelImage;
			}
		};
		showHideImage.setOutputMarkupId(true);
		showHideImage.setOutputMarkupPlaceholderTag(true);

		panel.setOutputMarkupId(true);
		panel.setOutputMarkupPlaceholderTag(true);
	}

	public void addComponents() {
		add(showHideLink);
		showHideLink.add(showHideImage);
		add(new Label("titlePanel", titleModel));
		addOrReplace(panel);
	}
}