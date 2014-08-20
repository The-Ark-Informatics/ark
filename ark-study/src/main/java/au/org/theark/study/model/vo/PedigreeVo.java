package au.org.theark.study.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.StudyPedigreeConfiguration;

public class PedigreeVo implements Serializable {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;

	private List<RelationshipVo>	relationshipList;

	private StudyPedigreeConfiguration pedigreeConfig;

	public PedigreeVo() {
		relationshipList = new ArrayList<RelationshipVo>();
		pedigreeConfig = new StudyPedigreeConfiguration();
	}

	public List<RelationshipVo> getRelationshipList() {
		return relationshipList;
	}

	public void setRelationshipList(List<RelationshipVo> relationshipList) {
		this.relationshipList = relationshipList;
	}

	public StudyPedigreeConfiguration getPedigreeConfig() {
		return pedigreeConfig;
	}

	public void setPedigreeConfig(StudyPedigreeConfiguration pedigreeConfig) {
		this.pedigreeConfig = pedigreeConfig;
	}

}
