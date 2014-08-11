package au.org.theark.geno.model.vo;

import au.org.theark.core.model.geno.entity.Row;
import au.org.theark.core.vo.BaseVO;

public class RowVO extends BaseVO {
	private Row row;
	
	public RowVO(Row row) {
		this.row = row;
	}
	
	public RowVO() {
		this.row = new Row();
	}

	public Row getRow() {
		return row;
	}

	public void setRow(Row row) {
		this.row = row;
	}

	@Override
	public String toString() {
		return "RowVO [row=" + row + "]";
	}
}
