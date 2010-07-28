package au.org.theark.gdmi.model.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Upload entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "UPLOAD", schema = "GDMI")
public class Upload implements java.io.Serializable {

	// Fields

	private long id;
	private FileFormat fileFormat;
	private String filename;
	private Set<UploadMarkerGroup> uploadMarkerGroups = new HashSet<UploadMarkerGroup>(
			0);
	private Set<UploadCollection> uploadCollections = new HashSet<UploadCollection>(
			0);

	// Constructors

	/** default constructor */
	public Upload() {
	}

	/** minimal constructor */
	public Upload(long id, FileFormat fileFormat) {
		this.id = id;
		this.fileFormat = fileFormat;
	}

	/** full constructor */
	public Upload(long id, FileFormat fileFormat, String filename,
			Set<UploadMarkerGroup> uploadMarkerGroups,
			Set<UploadCollection> uploadCollections) {
		this.id = id;
		this.fileFormat = fileFormat;
		this.filename = filename;
		this.uploadMarkerGroups = uploadMarkerGroups;
		this.uploadCollections = uploadCollections;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_FORMAT_ID", nullable = false)
	public FileFormat getFileFormat() {
		return this.fileFormat;
	}

	public void setFileFormat(FileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}

	@Column(name = "FILENAME", length = 4000)
	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "upload")
	public Set<UploadMarkerGroup> getUploadMarkerGroups() {
		return this.uploadMarkerGroups;
	}

	public void setUploadMarkerGroups(Set<UploadMarkerGroup> uploadMarkerGroups) {
		this.uploadMarkerGroups = uploadMarkerGroups;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "upload")
	public Set<UploadCollection> getUploadCollections() {
		return this.uploadCollections;
	}

	public void setUploadCollections(Set<UploadCollection> uploadCollections) {
		this.uploadCollections = uploadCollections;
	}

}