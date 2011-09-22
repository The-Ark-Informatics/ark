package au.org.theark.lims.web.component.biolocation;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

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
	private Label										notAllocatedLbl;

	public BioLocationNotAllocatedPanel(String id) {
		super(id);
		setOutputMarkupPlaceholderTag(true);

		initialisePanel();
		addComponents();
	}

	public void initialisePanel() {
		notAllocatedLbl = new Label("notAllocated", "<i>Biospecimen not allocated</i>");
		notAllocatedLbl.setEscapeModelStrings(false);
	}

	public void addComponents() {
		add(notAllocatedLbl);
	}
}