package au.org.theark.disease.vo;

import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.disease.entity.Affection;
import au.org.theark.core.model.disease.entity.Disease;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.vo.BaseVO;

public class AffectionListVO extends BaseVO {

	private Disease				disease;
	private LinkSubjectStudy	linkSubjectStudy;
	private List<Affection>		affectionList	= new ArrayList<Affection>();

	public AffectionListVO() {
		this.disease = new Disease();
		this.linkSubjectStudy = new LinkSubjectStudy();
	}

	public AffectionListVO(Disease disease, LinkSubjectStudy linkSubjectStudy, List<Affection> affectionList) {
		this.disease = disease;
		this.affectionList = affectionList;
		this.linkSubjectStudy = linkSubjectStudy;
	}

	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	public List<Affection> getAffectionList() {
		return affectionList;
	}

	public void setAffectionList(List<Affection> affectionList) {
		this.affectionList = affectionList;
	}

	public LinkSubjectStudy getLinkSubjectStudy() {
		return linkSubjectStudy;
	}

	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
	}

	@Override
	public String toString() {
		return "AffectionListVO [disease=" + disease + ", linkSubjectStudy=" + linkSubjectStudy + ", affectionList=" + affectionList + "]";
	}

}
