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
package au.org.theark.core.model.config.entity;

import au.org.theark.core.Constants;

import javax.persistence.*;

/**
 * SettingFile entity.
 * 
 * @author gecgooden
 */
@Entity
@Table(name = "SETTING_FILE", schema = Constants.CONFIG_SCHEMA)
public class SettingFile implements java.io.Serializable {

	private static final long serialVersionUID = -3611814204230766317L;
	private Long id;
	private String filename;
	private byte[] payload;
	private String checksum;
	private String fileId;

	public SettingFile() {
	}

	public SettingFile(Long id, String filename) {
		this.id = id;
		this.filename = filename;
	}

	@Id
	@SequenceGenerator(name = "SettingFile_PK_Seq", sequenceName = "SETTINGFILE_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SettingFile_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "FILENAME", length = 260)
	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Lob
	@Column(name = "PAYLOAD")
	public byte[] getPayload() {
		return this.payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	@Column(name = "CHECKSUM")
	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	@Column(name = "FILE_ID", length = 1000)
	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
}
