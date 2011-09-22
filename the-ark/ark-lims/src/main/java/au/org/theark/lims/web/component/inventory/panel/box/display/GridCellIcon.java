package au.org.theark.lims.web.component.inventory.panel.box.display;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.web.component.image.MouseOverImage;

public class GridCellIcon extends Panel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7296587386654258036L;

	public GridCellIcon(String id, ResourceReference image, ResourceReference mouseoverImage, String imageAltText) {
		super(id);
		MouseOverImage icon = new MouseOverImage("icon", image, mouseoverImage, imageAltText);
		this.add(icon);
	}
}
