package au.org.theark.core.web.component.panel;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * Panel that contains an icon then label next to each other
 * 
 * @author elam
 */
public abstract class IconLabelPanel<T> extends Panel
{
	private static final long serialVersionUID = 1L;

	protected IModel<T> innerModel;
	/**
	 * Constructs the panel.
	 * 
	 * @param id
	 *            component id
	 * @param labelModel
	 *            model that holds the label content
	 */
	public IconLabelPanel(String id, IModel<?> labelModel, IModel<T> innerModel)
	{
		super(id, innerModel);
		this.innerModel = innerModel;
		
		// Use the default label if null
      if (labelModel == null) {
      	labelModel = new StringResourceModel("defaultLabel", this, null);
      }
		addComponents(labelModel);
	}

	/**
	 * Adds the icon and content components to the panel. Override for additional components if required.
	 * 
	 * @param model
	 *            model that holds the label content
	 */
	protected void addComponents(IModel<?> labelModel)
	{
		add(newImageComponent("iconImage"));
		add(newLabelComponent("label", labelModel));
	}

	/**
	 * Creates the icon component for the node
	 * 
	 * @param componentId
	 * @return icon image component
	 */
	protected Image newImageComponent(String componentId)
	{
		return new Image(componentId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected ResourceReference getImageResourceReference()
			{
				return IconLabelPanel.this.getImageResourceReference();
			}
		};
	}

	/**
	 * Creates the label component 
	 * 
	 * @param componentId
	 * @param model
	 * @return content component
	 */
	protected Label newLabelComponent(String componentId, IModel<?> labelModel)
	{
		if (labelModel == null)
		{
			labelModel = new Model<String>("");
		}
		return new Label(componentId, labelModel);
	}

	/**
	 * Sets the tooltip (title attribute) for this panel
	 * 
	 * @param tooltipModel
	 */	
	public void setTooltip(IModel<?> tooltipModel) {
		if (tooltipModel != null && tooltipModel.getObject() != null)
		{
			this.add(new AttributeModifier("title", tooltipModel));
		}
	}

	/**
	 * Returns the image resource reference (defined by the implementation)
	 * 
	 * @param node
	 * @return image resource reference
	 */
	abstract protected ResourceReference getImageResourceReference();

	/**
	 * Returns resource reference for download icon.
	 * 
	 * @param node
	 * @return resource reference
	 */
	protected ResourceReference getResourceDownloadIcon()
	{
		return RESOURCE_DOWNLOAD;
	}

	/**
	 * Returns resource reference for delete icon.
	 * 
	 * @param node
	 * @return resource reference
	 */
	protected ResourceReference getResourceDeleteIcon()
	{
		return RESOURCE_DELETE;
	}

	/**
	 * Returns resource reference for report icon.
	 * 
	 * @param node
	 * @return resource reference
	 */
	protected ResourceReference getResourceReportIcon()
	{
		return RESOURCE_REPORT;
	}
	
	private static final ResourceReference RESOURCE_DELETE = new PackageResourceReference(
		IconLabelPanel.class, "res/delete.png");
	private static final ResourceReference RESOURCE_DOWNLOAD = new PackageResourceReference(
		IconLabelPanel.class, "res/download.png");
	private static final ResourceReference RESOURCE_REPORT = new PackageResourceReference(
			IconLabelPanel.class, "res/report.png");
}

