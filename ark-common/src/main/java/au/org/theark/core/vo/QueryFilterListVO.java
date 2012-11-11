package au.org.theark.core.vo;

import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.Study;


public class QueryFilterListVO {

	private Study study;
	private List<QueryFilterVO> queryFilterVOs = new ArrayList<QueryFilterVO>();

	public List<QueryFilterVO> getQueryFilterVOs() {
		return queryFilterVOs;
	}

	public void setQueryFilterVOs(List<QueryFilterVO> queryFilterVOs) {
		this.queryFilterVOs = queryFilterVOs;
	}

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

}
