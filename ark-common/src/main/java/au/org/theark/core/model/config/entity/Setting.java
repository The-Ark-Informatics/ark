package au.org.theark.core.model.config.entity;

import au.org.theark.core.Constants;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;

@Audited
@Entity
@Table(name = "settings", schema = Constants.CONFIG_SCHEMA)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class Setting implements Serializable {

    private Long id;
    private String propertyName;
    private String propertyValue;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "PROPERTY_NAME")
    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    @Column(name = "PROPERTY_VALUE")
    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    @Enumerated(EnumType.STRING)
    private SettingType settingType;

    public SettingType getSettingType() {
        return settingType;
    }

    public void setSettingType(SettingType settingType) {
        this.settingType = settingType;
    }

    @Transient
    public int getIntValue() throws NumberFormatException {
        return new Integer(propertyValue).intValue();
    }

    @Transient
    public float getFloatValue() throws NumberFormatException {
        return new Integer(propertyValue).floatValue();
    }

    @Override
    public String toString() {
        return "Setting{" +
                "id=" + id +
                ", propertyName='" + propertyName + '\'' +
                ", propertyValue='" + propertyValue + '\'' +
                ", settingType='" + getSettingType().toString() + '\'' +
                '}';
    }
}
