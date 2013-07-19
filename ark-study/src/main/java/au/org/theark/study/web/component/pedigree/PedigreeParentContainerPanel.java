package au.org.theark.study.web.component.pedigree;

import org.apache.wicket.markup.html.WebMarkupContainer;

import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.study.web.component.subject.SubjectContainerPanel;

public class PedigreeParentContainerPanel extends SubjectContainerPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	

	public PedigreeParentContainerPanel(String id, WebMarkupContainer arkContextMarkup, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup,AbstractDetailModalWindow modalWindow,String gender) {
		super(id, arkContextMarkup, studyNameMarkup, studyLogoMarkup,modalWindow,gender);
		setOutputMarkupId(true);
		// TODO Auto-generated constructor stub
	}

}
