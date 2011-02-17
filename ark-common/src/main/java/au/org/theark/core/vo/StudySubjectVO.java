/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.vo;

import java.io.Serializable;

import au.org.theark.core.model.study.entity.LinkSubjectStudy;

/**
 * @author nivedann
 *
 */
public class StudySubjectVO implements Serializable{
	
	public StudySubjectVO(){
		subjectStudy = new LinkSubjectStudy();
	}
	
	protected LinkSubjectStudy subjectStudy;

	public LinkSubjectStudy getSubjectStudy() {
		return subjectStudy;
	}

	public void setSubjectStudy(LinkSubjectStudy subjectStudy) {
		this.subjectStudy = subjectStudy;
	}
	

}
