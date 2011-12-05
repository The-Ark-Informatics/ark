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
package au.org.theark.core.model.lims.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * 
 * @author cellis
 * 
 */
@Entity
@Table(name = "barcode_label_data", schema = Constants.LIMS_TABLE_SCHEMA)
public class BarcodeLabelData implements java.io.Serializable {
	private Long			id;
	private BarcodeLabel	barcodeLabel;
	private String			command;
	private Integer		xCoord;
	private Integer		yCoord;
	private String			p1;
	private String			p2;
	private String			p3;
	private String			p4;
	private String			p5;
	private String			p6;
	private String			p7;
	private String			p8;
	private String			quoteLeft;
	private String			data;
	private String			quoteRight;
	private String			lineFeed;

	public BarcodeLabelData() {
	}

	public BarcodeLabelData(Long id, BarcodeLabel barcodeLabel, String command, Integer xCoord, Integer yCoord, String p1, String p2, String p3, String p4, String p5, String p6, String p7, String p8,
			String quoteLeft, String data, String quoteRight, String lineFeed) {
		this.id = id;
		this.barcodeLabel = barcodeLabel;
		this.command = command;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
		this.p5 = p5;
		this.p6 = p6;
		this.p7 = p7;
		this.p8 = p8;
		this.quoteLeft = quoteLeft;
		this.data = data;
		this.quoteRight = quoteRight;
		this.lineFeed = lineFeed;
	}

	@Id
	@SequenceGenerator(name = "barcodelabeldata_generator", sequenceName = "BARCODELABELDATA_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "barcodelabeldata_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BARCODE_LABEL_ID", nullable = false)
	public BarcodeLabel getBarcodeLabel() {
		return this.barcodeLabel;
	}

	public void setBarcodeLabel(BarcodeLabel barcodeLabel) {
		this.barcodeLabel = barcodeLabel;
	}

	@Column(name = "COMMAND", length = 10)
	public String getCommand() {
		return this.command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	@Column(name = "X_COORD")
	public Integer getXCoord() {
		return this.xCoord;
	}

	public void setXCoord(Integer xCoord) {
		this.xCoord = xCoord;
	}

	@Column(name = "Y_COORD")
	public Integer getYCoord() {
		return this.yCoord;
	}

	public void setYCoord(Integer yCoord) {
		this.yCoord = yCoord;
	}

	@Column(name = "P1")
	public String getP1() {
		return p1;
	}

	public void setP1(String p1) {
		this.p1 = p1;
	}

	@Column(name = "P2")
	public String getP2() {
		return p2;
	}

	public void setP2(String p2) {
		this.p2 = p2;
	}

	@Column(name = "P3")
	public String getP3() {
		return p3;
	}

	public void setP3(String p3) {
		this.p3 = p3;
	}

	@Column(name = "P4")
	public String getP4() {
		return p4;
	}

	public void setP4(String p4) {
		this.p4 = p4;
	}

	@Column(name = "P5")
	public String getP5() {
		return p5;
	}

	public void setP5(String p5) {
		this.p5 = p5;
	}

	@Column(name = "P6")
	public String getP6() {
		return p6;
	}

	public void setP6(String p6) {
		this.p6 = p6;
	}

	@Column(name = "P7")
	public String getP7() {
		return p7;
	}

	public void setP7(String p7) {
		this.p7 = p7;
	}

	@Column(name = "P8")
	public String getP8() {
		return p8;
	}

	public void setP8(String p8) {
		this.p8 = p8;
	}

	@Column(name = "QUOTE_LEFT")
	public String getQuoteLeft() {
		return quoteLeft;
	}

	public void setQuoteLeft(String quoteLeft) {
		this.quoteLeft = quoteLeft;
	}

	@Column(name = "DATA")
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Column(name = "QUOTE_RIGHT")
	public String getQuoteRight() {
		return quoteRight;
	}

	public void setQuoteRight(String quoteRight) {
		this.quoteRight = quoteRight;
	}

	@Column(name = "LINE_FEED")
	public String getLineFeed() {
		return lineFeed;
	}

	public void setLineFeed(String lineFeed) {
		this.lineFeed = lineFeed;
	}
}
