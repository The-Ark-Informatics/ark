package au.org.theark.core.vo;

import au.org.theark.core.model.study.entity.Relationship;

public class RelationWrapperVo {
	
	private ArkUserVO arkUserVO;
	private SubjectVO subjectVO;
	private SubjectVO relativeVO;
	private Relationship relationship;
	
	public SubjectVO getSubjectVO() {
		return subjectVO;
	}
	public void setSubjectVO(SubjectVO subjectVO) {
		this.subjectVO = subjectVO;
	}
	public SubjectVO getRelativeVO() {
		return relativeVO;
	}
	public void setRelativeVO(SubjectVO relativeVO) {
		this.relativeVO = relativeVO;
	}
	public Relationship getRelationship() {
		return relationship;
	}
	public void setRelationship(Relationship relationship) {
		this.relationship = relationship;
	}
	public ArkUserVO getArkUserVO() {
		return arkUserVO;
	}
	public void setArkUserVO(ArkUserVO arkUserVO) {
		this.arkUserVO = arkUserVO;
	}

}
