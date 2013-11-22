package au.org.theark.study.web.component.pedigree;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;

import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.study.model.vo.RelationshipVo;
import au.org.theark.study.web.component.subject.SubjectContainerPanel;

public class PedigreeParentContainerPanel extends SubjectContainerPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	

	public PedigreeParentContainerPanel(String id, WebMarkupContainer arkContextMarkup, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup,AbstractDetailModalWindow modalWindow,String gender,List<RelationshipVo> relatives) {
		super(id, arkContextMarkup, studyNameMarkup, studyLogoMarkup,modalWindow,gender,relatives);
		setOutputMarkupId(true);
	}

}
