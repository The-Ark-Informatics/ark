package au.org.theark.core.model.report.entity;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.DelimiterType;
import au.org.theark.core.model.study.entity.FileFormat;

/**
 * 
 * The search object is used to save what criteria, name, etc is used in a search 
 * 
 * @author travis
 *
 */

@Entity
@Table(name = "SEARCH_RESULT", schema = Constants.REPORT_SCHEMA)
public class SearchResult  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Search search;
	private FileFormat fileFormat;
	private DelimiterType delimiterType;
	private String filename;
	private String checksum;
	private String userId;
	private Date startTime;
	private Date finishTime;
	private SearchPayload searchPayload;

	@Id
	@SequenceGenerator(name = "SearchResult_Gen", sequenceName = "SEARCH_RESULT_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SearchResult_Gen")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param search the search to set
	 */
	public void setSearch(Search search) {
		this.search = search;
	}

	/**
	 * @return the search
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEARCH_ID", nullable = false)
	public Search getSearch() {
		return search;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_FORMAT_ID", nullable = false)
	public FileFormat getFileFormat() {
		return this.fileFormat;
	}

	public void setFileFormat(FileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEARCH_PAYLOAD_ID", nullable = true)
	public SearchPayload getSearchPayload() {
		return searchPayload;
	}

	public void setSearchPayload(SearchPayload searchPayload) {
		this.searchPayload = searchPayload;
	}

	/**
	 * @param delimiterType
	 *            the delimiterType to set
	 */
	public void setDelimiterType(DelimiterType delimiterType) {
		this.delimiterType = delimiterType;
	}

	/**
	 * @return the delimiterType
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DELIMITER_TYPE_ID", nullable = false)
	public DelimiterType getDelimiterType() {
		return delimiterType;
	}

	/**
	 * @return the filename
	 */
	@Column(name = "FILENAME", length = 260)
	public String getFilename() {
		return this.filename;
	}

	/**
	 * @param filename
	 *            the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the checksum
	 */
	@Column(name = "CHECKSUM", nullable = false, length = 50)
	public String getChecksum() {
		return checksum;
	}

	/**
	 * @param checksum
	 *            the checksum to set
	 */
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	/**
	 * @return the startTime
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME", nullable = false)
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the finishTime
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FINISH_TIME")
	public Date getFinishTime() {
		return finishTime;
	}

	/**
	 * @param finishTime
	 *            the finishTime to set
	 */
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	@Column(name = "USER_ID", nullable = false, length = 100)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	
}
