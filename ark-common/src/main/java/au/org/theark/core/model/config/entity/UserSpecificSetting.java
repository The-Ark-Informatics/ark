package au.org.theark.core.model.config.entity;

import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;

@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@DiscriminatorValue("user")
public class UserSpecificSetting extends Setting {

    private ArkUser arkUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    public ArkUser getArkUser() {
        return this.arkUser;
    }

    public void setArkUser(ArkUser arkUser) {
        this.arkUser = arkUser;
    }

    @Override
    public String toString() {
        return "UserSpecificSetting{" +
                "id=" + getId() + '\'' +
                ", arkUser_id='" + (arkUser != null ? arkUser.getId() : "NULL") + '\'' +
                ", propertyName='" + getPropertyName() + '\'' +
                ", propertyValue='" + getPropertyValue() + '\'' +
                ", settingType='" + getPropertyType().toString() + '\'' +
                '}';
    }
}
