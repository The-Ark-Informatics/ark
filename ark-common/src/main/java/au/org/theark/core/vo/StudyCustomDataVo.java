package au.org.theark.core.vo;

import au.org.theark.core.model.study.entity.ICustomFieldData;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.web.component.customfield.dataentry.CustomDataVO;

public abstract class StudyCustomDataVo<T extends ICustomFieldData> extends CustomDataVO<T> {
	protected LinkSubjectStudy	linkSubjectStudy;

	public StudyCustomDataVo() {
		super();
		this.linkSubjectStudy = new LinkSubjectStudy();
	}

	public LinkSubjectStudy getLinkSubjectStudy() {
		return linkSubjectStudy;
	}

	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
	}

}
