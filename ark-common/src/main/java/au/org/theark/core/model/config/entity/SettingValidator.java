package au.org.theark.core.model.config.entity;

import au.org.theark.core.Constants;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by george on 28/2/17.
 */
@Audited
@Entity
@Table(name = "settings_validator", schema = Constants.CONFIG_SCHEMA)
public class SettingValidator implements Serializable {

    private long id;
    private String settingPropertyName;
    private String value;
    @Enumerated(EnumType.STRING)
    private ValidationType type;
    //TODO: Add error message

    @Id
    @SequenceGenerator(name = "setting_validator_generator", sequenceName = "SETTING_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "setting_validator_generator")
    @Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "SETTING_PROPERTY_NAME")
    public String getSettingPropertyName() {
        return settingPropertyName;
    }

    public void setSettingPropertyName(String settingPropertyName) {
        this.settingPropertyName = settingPropertyName;
    }

    @Column(name = "VALUE")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(name = "VALIDATION_TYPE")
    public ValidationType getType() {
        return type;
    }

    public void setType(ValidationType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SettingValidator{" +
                "id=" + id +
                ", settingPropertyName='" + settingPropertyName + '\'' +
                ", value='" + value + '\'' +
                ", type=" + type +
                '}';
    }
}
