package au.org.theark.core.model.study.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import au.org.theark.core.Constants;

@Entity
@Table(name = "LINK_SUBJECT_PEDIGREE", schema = Constants.STUDY_SCHEMA)
public class LinkSubjectPedigree implements Serializable {
   
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer familyId;
    private LinkSubjectStudy subject;
    private LinkSubjectStudy relative;
    private Relationship relationship;
   
    public LinkSubjectPedigree(){
    }
   
    public LinkSubjectPedigree(Integer id, Integer familyId,
            LinkSubjectStudy subject, LinkSubjectStudy relative,
            Relationship relationship) {
        super();
        this.id = id;
        this.familyId = familyId;
        this.subject = subject;
        this.relative = relative;
        this.relationship = relationship;
    }

    @Id
    @SequenceGenerator(name = "link_subject_pedigree_generator", sequenceName = "LINK_SUBJECT_PEDIGREE_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "link_subject_pedigree_generator")
    @Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
   
    @Column(name = "FAMILY_ID", nullable = false, precision = 22, scale = 0)
    public Integer getFamilyId() {
        return familyId;
    }
    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }
   
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LINK_SUBJECT_STUDY_ID")
    public LinkSubjectStudy getSubject() {
        return subject;
    }
    public void setSubject(LinkSubjectStudy subject) {
        this.subject = subject;
    }
   
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RELATIVE_ID")
    public LinkSubjectStudy getRelative() {
        return relative;
    }
    public void setRelative(LinkSubjectStudy relative) {
        this.relative = relative;
    }
   
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RELATIONSHIP_ID")
    public Relationship getRelationship() {
        return relationship;
    }
    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }
}