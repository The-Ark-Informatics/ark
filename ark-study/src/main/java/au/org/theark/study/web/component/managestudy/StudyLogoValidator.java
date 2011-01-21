package au.org.theark.study.web.component.managestudy;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.springframework.util.StringUtils;

public class StudyLogoValidator implements IValidator<FileUpload>
{

	private static final long			serialVersionUID	= -8116224338791429342L;
	public static final List<String>	fileExtensions			= Arrays.asList("jpg", "gif", "jpeg", "png");
	public static final Bytes fileSize	= Bytes.kilobytes(au.org.theark.study.web.Constants.STUDY_LOGO_FILESIZE);
   private BufferedImage image;
   
	public void validate(IValidatable<FileUpload> pValidatable)
	{
		FileUpload fileUploadImage = pValidatable.getValue();
		String fileExtension = StringUtils.getFilenameExtension(fileUploadImage.getClientFileName());
		ValidationError error = new ValidationError();
		
		// Read image, to work out width and height
		try
		{
			image = ImageIO.read(fileUploadImage.getInputStream());
			
			// Check extension ok
			if (fileExtension != null && !fileExtensions.contains(fileExtension.toLowerCase()))
			{	
				error.addMessageKey("study.studyLogoFileType");
				error.setVariable("extensions", fileExtensions.toString());
				pValidatable.error(error);
			}// Check size ok
			else if (fileUploadImage.getSize() > fileSize.bytes())
			{
				error.addMessageKey("study.studyLogoFileSize");
				pValidatable.error(error);
			}
			else if (image.getWidth() > 100 || image.getHeight() > 100)
			{
				error.addMessageKey("study.studyLogoPixelSize");
				pValidatable.error(error);
			}
			else
			{
				
			}
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			error.addMessageKey("study.studyLogoImageError");
			pValidatable.error(error);
		}
	}
}