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
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;
    private String highestType;

    @Id
    @SequenceGenerator(name = "setting_generator", sequenceName = "SETTING_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "setting_generator")
    @Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
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

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    /**
     * The `type` column associated with this entity is used to differentiate between the three implemented types of
     * settings;
     *
     * 1. System,
     * 2. Study,
     * 3. User.
     *
     * Each tier after System is a sub-set of the tiers above it, e.g. Study settings won't contain all the System
     * settings and User won't contain all the System or Study settings.
     *
     * Contents of this column must match the acceptable discriminator values.
     *
     * @return The highest tier that this setting can be used for
     */
    @Column(name = "HIGHEST_TYPE")
    public String getHighestType() {
        return this.highestType;
    }

    public void setHighestType(String highestType) {
        this.highestType = highestType;
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
                ", propertyType='" + propertyType + '\'' +
                ", highestType='" + highestType + '\'' +
                '}';
    }
}
