package au.org.theark.core.model.config.entity;

import au.org.theark.core.model.study.entity.Study;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Audited
@Entity
@DiscriminatorValue("study")
public class StudySpecificSetting extends Setting {

    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDY_ID")
    public Study getStudy() {
        return this.study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    @Override
    public String toString() {
        return "StudySpecificSetting{" +
                "id=" + getId() + '\'' +
                ", study_id='" + (study != null ? study.getId() : "NULL") + '\'' +
                ", propertyName='" + getPropertyName() + '\'' +
                ", propertyValue='" + getPropertyValue() + '\'' +
                ", settingType='" + getPropertyType().toString() + '\'' +
                '}';
    }
}
