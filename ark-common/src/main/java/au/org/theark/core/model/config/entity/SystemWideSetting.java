package au.org.theark.core.model.config.entity;

import org.hibernate.envers.Audited;

import javax.persistence.*;

@Audited
@Entity
@DiscriminatorValue("system")
public class SystemWideSetting extends Setting {

    @Override
    public String toString() {
        return "SystemWideSetting{" +
                "id=" + getId() + '\'' +
                ", propertyName='" + getPropertyName() + '\'' +
                ", propertyValue='" + getPropertyValue() + '\'' +
                ", settingType='" + ((getPropertyType()!=null)? getPropertyType().toString():"") + '\'' +
                ", highestType='" + getHighestType() + '\'' +
                '}';
    }
}
