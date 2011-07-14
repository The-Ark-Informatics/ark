package au.org.theark.study.web.component.managestudy;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.apache.wicket.util.io.Streams;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.util.ImageResource;

public class StudyHelper implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8485137084667518625L;
	
	NonCachingImage studyLogoImage;
	ContextImage noStudyLogoImage;
	Label studyNameLabel = null;
	Label studyLabel = null;
	
	public void setStudyLogo(Study study, AjaxRequestTarget target, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup)
	{
		// Set the study logo
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if(sessionStudyId != null && study.getStudyLogoBlob() != null)
		{
			setStudyLogoImage(study, "studyLogoImage", studyLogoMarkup);
			studyNameMarkup.setVisible(false);
			studyLogoMarkup.setVisible(true);
		}
		else
		{
			// Only show study name, no logo	
			studyNameLabel = new Label("studyNameLabel", new Model(study.getName()));
			studyNameMarkup.replace(studyNameLabel);
			studyNameMarkup.setVisible(true);
			studyLogoMarkup.setVisible(false);
		}
		
		target.addComponent(studyNameMarkup);
		target.addComponent(studyLogoMarkup);
	}
	
	
	@SuppressWarnings({ "unchecked", "serial" })
	public void setStudyLogoImage(Study study, String id, WebMarkupContainer studyLogoImageContainer)
	{
		// Set the study logo
		try
		{
			if(study != null && study.getStudyLogoBlob() != null)
			{
				// Get the Study logo Blob as an array of bytes
				java.sql.Blob studyLogoBlob = study.getStudyLogoBlob();
				
				if (studyLogoBlob != null)
				{
					InputStream in = studyLogoBlob.getBinaryStream();
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					Streams.copy(in, out);
					// Get the Study logo Blob as an array of bytes
					final byte[] data = out.toByteArray(); //studyLogoBlob.getBytes(1, (int) studyLogoBlob.length());
				
					studyLogoImage = new NonCachingImage(id, 
					new AbstractReadOnlyModel() { 
	            	@Override 
	               public Object getObject() {
	            		return new ImageResource(data,"gif"); 
	               } 
	            });
	         
					studyLogoImageContainer.replace(studyLogoImage);
				}
			}
			else
			{
				noStudyLogoImage = new ContextImage("study.studyLogoImage", new Model<String>("images/no_study_logo.gif"));;
				studyLogoImageContainer.replace(noStudyLogoImage);	
			}
		}
		catch (SQLException sqle)
		{
			// Log SQL exception
			sqle.printStackTrace();
		}
		catch (IOException ioe)
		{
			// Log IO Exception
			ioe.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setStudyContextLabel(AjaxRequestTarget target, String studyName, WebMarkupContainer arkContextMarkup)
	{
		studyLabel =  new Label("studyLabel", new Model("Study: " + studyName));
		arkContextMarkup.addOrReplace(studyLabel);
		target.addComponent(arkContextMarkup);
	}

}
