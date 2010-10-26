package au.org.theark.phenotypic.model.entity;

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

import au.org.theark.study.model.entity.Person;

/**
 * MetaData entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "FIELD_DATA", schema = "PHENOTYPIC")
public class FieldData implements java.io.Serializable {

	// Fields
	private Long id;
	private Field field;
	private Collection collection;
	private Date dateCollected;
	private Person person;
	private String value;
	private String userId;
	private Date insertTime;
	private String updateUserId;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public FieldData() {
	}

	/** minimal constructor */
	public FieldData(Long id, Field field,	Collection collection, Date dateCollected, Person person, String userId, Date insertTime) {
		this.id = id;
		this.field = field;
		this.collection = collection;
		this.dateCollected = dateCollected;
		this.setPerson(person);
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public FieldData(Long id, Field field,	Collection collection, Date dateCollected, Person person, String value, String userId, Date insertTime, String updateUserId, Date updateTime) {
		this.id = id;
		this.field = field;
		this.collection = collection;
		this.value = value;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
		this.setPerson(person);
	}

	// Property accessors
	@Id
	@SequenceGenerator(name="FieldData_PK_Seq",sequenceName="GDMI.META_DATA_PK_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO,generator="FieldData_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FIELD_ID", nullable = false)
	public Field getField() {
		return this.field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COLLECTION_ID", nullable = false)
	public Collection getCollection() {
		return this.collection;
	}

	public void setCollection(Collection collection) {
		this.collection = collection;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_COLLECTED", nullable = false)
	public Date getDateCollected() {
		return this.dateCollected;
	}

	public void setDateCollected(Date dateCollected) {
		this.dateCollected = dateCollected;
	}
	
	@Column(name = "VALUE", length = 2000)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "USER_ID", nullable = false, length = 50)
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

	@Column(name = "UPDATE_USER_ID", length = 50)
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

	/**
	 * @param person the person to set
	 */
	public void setPerson(Person person)
	{
		this.person = person;
	}

	/**
	 * @return the person
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID")
	// TODO: Change to PERSON_ID once MySQL database refactored 
	public Person getPerson()
	{
		return person;
	}
}