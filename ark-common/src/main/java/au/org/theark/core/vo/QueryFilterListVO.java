package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.report.entity.Search;
import au.org.theark.core.model.study.entity.Study;


public class QueryFilterListVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Study study;
	private Search search;

	public QueryFilterListVO(){
		
	}
	
	public QueryFilterListVO(Search search){
		this.search = search;
	}

	
	public QueryFilterListVO(SearchVO searchVO){
		this.search = searchVO==null?(new Search()):searchVO.getSearch();
	}
	
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

	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
	}

}
