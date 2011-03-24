package au.org.theark.core.model.study.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

public class LinkCorrespondencePerson implements Serializable {

	private Long id;
	private Correspondences correspondence;
	private Person person;
	
    @Id
    @SequenceGenerator(name="link_correspondence_person_generator", sequenceName="LINK_CORRESPONDENCE_PERSON_SEQUENCE")
    @GeneratedValue(strategy=GenerationType.AUTO, generator = "link_correspondence_person_generator")
    @Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CORRESPONDENCE_ID")
	public Correspondences getCorrespondence() {
		return correspondence;
	}
	
	public void setCorrespondences(Correspondences correspondence) {
		this.correspondence = correspondence;
	}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_ID")
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
}
