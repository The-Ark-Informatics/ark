/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.core.util;

public class BarChartResult {
	protected double		value;
	protected Comparable	rowKey;
	protected Comparable	columnKey;

	public BarChartResult(double value, Comparable rowKey, Comparable columnKey) {
		this.value = value;
		this.rowKey = rowKey;
		this.columnKey = columnKey;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Comparable getRowKey() {
		return rowKey;
	}

	public void setRowKey(Comparable rowKey) {
		this.rowKey = rowKey;
	}

	public Comparable getColumnKey() {
		return columnKey;
	}

	public void setColumnKey(Comparable columnKey) {
		this.columnKey = columnKey;
	}
}
