package au.org.theark.core.web.component.image;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * Mouse-over image. Takes 2 image resource-references: one for initial image, second for mouseover image. 
 * Takes in 
 * 
 * <pre>
 * USAGE:
 * MouseOverImage image = new MouseOverImage("wicketId", imageOut, imageOver, "imageAltText");
 * add(image);
 * </pre>
 * 
 * @author cellis
 */
public class MouseOverImage extends Image {
	private static final long			serialVersionUID	= 1L;

	private final ResourceReference	mImage;
	private final ResourceReference	mMouseoverImage;
	private final String					mImageAltText;

	public MouseOverImage(String id, ResourceReference image, ResourceReference mouseoverImage, String imageAltText) {
		super(id, image);
		mImage = image;
		mMouseoverImage = mouseoverImage;
		mImageAltText = imageAltText;
		
		add(new AttributeModifier("onmouseover",  new LoadableDetachableModel<String>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected String load() {
				return "this.src='" + urlFor(mMouseoverImage,null) + "';";
			}

		}));
		
		add(new AttributeModifier("onmouseout", new LoadableDetachableModel<String>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected String load() {
				return "this.src='" + urlFor(mImage,null) + "';";
			}

		}));
		
		add(new AttributeModifier("alt",  new LoadableDetachableModel<String>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected String load() {
				return "alt='" + mImageAltText + "';";
			}

		}));
	}
}