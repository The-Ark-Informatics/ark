package au.org.theark.geno.model.vo;

import java.util.ArrayList;
import java.util.Collection;

import au.org.theark.core.vo.BaseVO;
import au.org.theark.core.vo.ArkVo;

public class RowListVO extends BaseVO implements ArkVo{

	private static final long serialVersionUID = 1L;

	private Collection<RowVO> rows = new ArrayList<RowVO>();

	public RowListVO() {
		super();
		rows = new ArrayList<RowVO>();
	}

	public RowListVO(Collection<RowVO> rows) {
		super();
		this.rows = rows;
	}

	public Collection<RowVO> getRows() {
		return rows;
	}

	public void setRows(Collection<RowVO> rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "RowListVO [rows=" + rows + "]";
	}
	
	@Override
	public String getArkVoName(){
		return "Row";
	}
}
