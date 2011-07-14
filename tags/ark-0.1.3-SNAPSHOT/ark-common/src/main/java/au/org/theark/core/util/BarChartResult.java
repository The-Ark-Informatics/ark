package au.org.theark.core.util;

public class BarChartResult{
	protected double value;
	protected Comparable rowKey;
	protected Comparable columnKey;
	
	public BarChartResult(double value, Comparable rowKey, Comparable columnKey)
	{
		this.value = value;
		this.rowKey = rowKey;
		this.columnKey = columnKey;
	}
	
	public double getValue()
	{
		return value;
	}

	public void setValue(double value)
	{
		this.value = value;
	}

	public Comparable getRowKey()
	{
		return rowKey;
	}

	public void setRowKey(Comparable rowKey)
	{
		this.rowKey = rowKey;
	}

	public Comparable getColumnKey()
	{
		return columnKey;
	}

	public void setColumnKey(Comparable columnKey)
	{
		this.columnKey = columnKey;
	}
}