package au.org.theark.core.model.study.entity;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

	/**
	 * TestPhone entity. @author MyEclipse Persistence Tools
	 */
	@Entity
	@Table(name = "TEST_PHONE", schema = "ETA")
	public class TestPhone implements java.io.Serializable {
	
		// Fields
	
	private BigDecimal id;
	private String phoneNumber;
	private boolean status;
	private Date joinDate;
	
	// Constructors
	
	/** default constructor */
	public TestPhone() {
	}
	
	/** minimal constructor */
	public TestPhone(BigDecimal id) {
		this.id = id;
	}
	
	/** full constructor */
	public TestPhone(BigDecimal id, String phoneNumber, boolean status,
			Date joinDate) {
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.status = status;
		this.joinDate = joinDate;
	}
	
	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 38, scale = 0)
	public BigDecimal getId() {
		return this.id;
	}
	
	public void setId(BigDecimal id) {
		this.id = id;
	}
	
	@Column(name = "PHONE_NUMBER", length = 0)
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Column(name = "STATUS", precision = 1, scale = 0)
	public boolean getStatus() {
		return this.status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "JOIN_DATE", length = 11)
	public Date getJoinDate() {
		return this.joinDate;
	}
	
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}


}
