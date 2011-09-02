/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.study.web.component.managestudy;

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

import au.org.theark.core.util.SerializableBufferedImage;

public class StudyLogoValidator implements IValidator<FileUpload> {

	private static final long				serialVersionUID	= -8116224338791429342L;
	public static final List<String>		fileExtensions		= Arrays.asList("jpg", "gif", "jpeg", "png");
	public static final Bytes				fileSize				= Bytes.kilobytes(au.org.theark.study.web.Constants.STUDY_LOGO_FILESIZE_KB);
	private SerializableBufferedImage	image;

	public void validate(IValidatable<FileUpload> pValidatable) {
		FileUpload fileUploadImage = pValidatable.getValue();
		String fileExtension = StringUtils.getFilenameExtension(fileUploadImage.getClientFileName());
		ValidationError error = new ValidationError();

		try {
			// Check extension ok
			if (fileExtension != null && !fileExtensions.contains(fileExtension.toLowerCase())) {
				error.addMessageKey("study.studyLogoFileType");
				error.setVariable("extensions", fileExtensions.toString());
				pValidatable.error(error);
			}// Check size ok
			else if (fileUploadImage.getSize() > fileSize.bytes()) {
				error.addMessageKey("study.studyLogoFileSize");
				pValidatable.error(error);
			}
			else {
				// Read image, to work out width and height
				image = new SerializableBufferedImage(ImageIO.read(fileUploadImage.getInputStream()));

				if (image.getHeight() > 100) {
					error.addMessageKey("study.studyLogoPixelSize");
					pValidatable.error(error);
				}
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			error.addMessageKey("study.studyLogoImageError");
			pValidatable.error(error);
		}
	}
}
