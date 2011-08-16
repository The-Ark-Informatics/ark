package au.org.theark.study.web.component.subjectcustomdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.SubjectCustomFieldData;

public class SubjectCustomDataVO implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	protected LinkSubjectStudy linkSubjectStudy;
	protected ArkModule arkModule;
	protected List<SubjectCustomFieldData> subjectCustomFieldDataList;
	
	SubjectCustomDataVO() {
		linkSubjectStudy = new LinkSubjectStudy();
		subjectCustomFieldDataList = new ArrayList<SubjectCustomFieldData>();
	}

	public LinkSubjectStudy getLinkSubjectStudy() {
		return linkSubjectStudy;
	}

	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
	}

	public ArkModule getArkModule() {
		return arkModule;
	}

	public void setArkModule(ArkModule arkModule) {
		this.arkModule = arkModule;
	}

	public List<SubjectCustomFieldData> getSubjectCustomFieldDataList() {
		return subjectCustomFieldDataList;
	}

	public void setSubjectCustomFieldDataList(List<SubjectCustomFieldData> subjectCustomDataList) {
		this.subjectCustomFieldDataList = subjectCustomDataList;
	}

}
