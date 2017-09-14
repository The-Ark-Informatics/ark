package au.org.theark.core.vo;

import au.org.theark.core.model.study.entity.TwinType;

public class TwinWrapperVO {
	
	private ArkUserVO arkUserVO;
	private SubjectVO subjectVO;
	private SubjectVO relativeVO;
	private TwinType twinType;
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
	public TwinType getTwinType() {
		return twinType;
	}
	public void setTwinType(TwinType twinType) {
		this.twinType = twinType;
	}
	public ArkUserVO getArkUserVO() {
		return arkUserVO;
	}
	public void setArkUserVO(ArkUserVO arkUserVO) {
		this.arkUserVO = arkUserVO;
	}

}
