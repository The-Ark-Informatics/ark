package au.org.theark.core.web.component;

import java.io.Serializable;

public class ArkGridCell implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2945343327357120514L;
	int col;
	int row;
	
	public ArkGridCell(int col, int row)
	{
		this.col = col;
		this.row = row;
	}
	
	public int getCol()
	{
		return col;
	}
	public void setCol(int col)
	{
		this.col = col;
	}
	public int getRow()
	{
		return row;
	}
	public void setRow(int row)
	{
		this.row = row;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArkGridCell other = (ArkGridCell) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
	
}
