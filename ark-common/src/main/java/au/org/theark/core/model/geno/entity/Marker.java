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
package au.org.theark.core.model.geno.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;

import au.org.theark.core.model.Constants;

/**
 * Marker entity. @author MyEclipse Persistence Tools
 */
@Entity(name = "au.org.theark.geno.model.entity.Marker")
@Table(name = "MARKER", schema = Constants.GENO_TABLE_SCHEMA)
public class Marker implements java.io.Serializable {

	// Fields

	private Long								id;
	private MarkerGroup						markerGroup;
	private String								name;
	private String								description;
	private String								chromosome;
	private Long								position;
	private String								gene;
	private String								majorAllele;
	private String								minorAllele;
	private String								userId;
	private Date								insertTime;
	private String								updateUserId;
	private Date								updateTime;
	private Set<SubjectMarkerMetaData>	subjectMarkerMetaDatas	= new HashSet<SubjectMarkerMetaData>(0);
	private Set<MarkerMetaData>			markerMetaDatas			= new HashSet<MarkerMetaData>(0);
	private Set<DecodeMask>					decodeMasks					= new HashSet<DecodeMask>(0);

	// Constructors

	/** default constructor */
	public Marker() {
	}

	/** minimal constructor */
	public Marker(Long id, MarkerGroup markerGroup, String chromosome, String userId, Date insertTime) {
		this.id = id;
		this.markerGroup = markerGroup;
		this.chromosome = chromosome;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public Marker(Long id, MarkerGroup markerGroup, String name, String description, String chromosome, Long position, String gene, String majorAllele, String minorAllele, String userId,
			Date insertTime, String updateUserId, Date updateTime, Set<SubjectMarkerMetaData> subjectMarkerMetaDatas, Set<MarkerMetaData> markerMetaDatas, Set<DecodeMask> decodeMasks) {
		this.id = id;
		this.markerGroup = markerGroup;
		this.name = name;
		this.description = description;
		this.chromosome = chromosome;
		this.position = position;
		this.gene = gene;
		this.majorAllele = majorAllele;
		this.minorAllele = minorAllele;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
		this.subjectMarkerMetaDatas = subjectMarkerMetaDatas;
		this.markerMetaDatas = markerMetaDatas;
		this.decodeMasks = decodeMasks;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name = "Marker_PK_Seq", sequenceName = Constants.MARKER_PK_SEQ)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "Marker_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@Cascade({ org.hibernate.annotations.CascadeType.REPLICATE, org.hibernate.annotations.CascadeType.SAVE_UPDATE })
	@JoinColumn(name = "MARKER_GROUP_ID", nullable = false)
	public MarkerGroup getMarkerGroup() {
		return this.markerGroup;
	}

	public void setMarkerGroup(MarkerGroup markerGroup) {
		this.markerGroup = markerGroup;
	}

	@Column(name = "NAME", length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 1024)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "CHROMOSOME", nullable = false, length = 50)
	public String getChromosome() {
		return this.chromosome;
	}

	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}

	@Column(name = "POSITION", precision = 22, scale = 0)
	public Long getPosition() {
		return this.position;
	}

	public void setPosition(Long position) {
		this.position = position;
	}

	@Column(name = "GENE", length = 100)
	public String getGene() {
		return this.gene;
	}

	public void setGene(String gene) {
		this.gene = gene;
	}

	@Column(name = "MAJOR_ALLELE", length = 10)
	public String getMajorAllele() {
		return this.majorAllele;
	}

	public void setMajorAllele(String majorAllele) {
		this.majorAllele = majorAllele;
	}

	@Column(name = "MINOR_ALLELE", length = 10)
	public String getMinorAllele() {
		return this.minorAllele;
	}

	public void setMinorAllele(String minorAllele) {
		this.minorAllele = minorAllele;
	}

	@Column(name = "USER_ID", nullable = false)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERT_TIME", nullable = false)
	public Date getInsertTime() {
		return this.insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	@Column(name = "UPDATE_USER_ID")
	public String getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "marker")
	public Set<SubjectMarkerMetaData> getSubjectMarkerMetaDatas() {
		return this.subjectMarkerMetaDatas;
	}

	public void setSubjectMarkerMetaDatas(Set<SubjectMarkerMetaData> subjectMarkerMetaDatas) {
		this.subjectMarkerMetaDatas = subjectMarkerMetaDatas;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "marker")
	public Set<MarkerMetaData> getMarkerMetaDatas() {
		return this.markerMetaDatas;
	}

	public void setMarkerMetaDatas(Set<MarkerMetaData> markerMetaDatas) {
		this.markerMetaDatas = markerMetaDatas;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "marker")
	public Set<DecodeMask> getDecodeMasks() {
		return this.decodeMasks;
	}

	public void setDecodeMasks(Set<DecodeMask> decodeMasks) {
		this.decodeMasks = decodeMasks;
	}

}
