package au.org.theark.core.dao;

import au.org.theark.core.model.config.entity.StudySpecificSetting;
import au.org.theark.core.model.config.entity.Setting;
import au.org.theark.core.model.config.entity.SystemWideSetting;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("settingDao")
public class ArkSettingDao extends HibernateSessionDao implements IArkSettingDao {

    public SystemWideSetting getSystemWideSetting(String key) {
        Criteria criteria = getSession().createCriteria(SystemWideSetting.class);
        criteria.add(Restrictions.eq("propertyName", key));

        SystemWideSetting setting = (SystemWideSetting) criteria.uniqueResult();

        return setting;
    }

    @Override
    public StudySpecificSetting getStudySpecificSetting(Study study, String key) {
        Criteria criteria = getSession().createCriteria(StudySpecificSetting.class);
        criteria.add(Restrictions.eq("propertyName", key));
        criteria.add(Restrictions.eq("study.id", study.getId()));

        StudySpecificSetting studySpecificSetting = (StudySpecificSetting) criteria.uniqueResult();

        return studySpecificSetting;
    }

    @Override
    public Setting getSetting(String key, Study study, ArkUser arkUser) {

        //If both are null then it will definitely be a system wide setting, so don't bother checking the other kinds.
        if(study == null && arkUser == null) {
            return getSystemWideSetting(key);
        }

        //test UserSpecificSetting when it exists

        Setting studySpecificSetting = getStudySpecificSetting(study, key);
        if(studySpecificSetting != null)
            return studySpecificSetting;

        Setting setting = getSystemWideSetting(key);
        if(setting != null)
            return setting;

        return null;
    }

    @Override
    public void save(Object object) {
        getSession().save(object);
    }

    @Override
    public int getSettingsCount(Setting setting) {
        Criteria criteria = buildSettingCriteria(setting);
        criteria.setProjection(Projections.rowCount());
        Long count = (Long) criteria.uniqueResult();
        return count.intValue();
    }

    @Override
    public List<Setting> searchPageableSettings(Setting setting, int first, int count) {
        Criteria criteria = buildSettingCriteria(setting);
        criteria.setFirstResult(first);
        criteria.setMaxResults(count);

        return (List<Setting>) criteria.list();
    }

    private Criteria buildSettingCriteria(Setting setting) {
        Criteria criteria = getSession().createCriteria(Setting.class);

        if(setting.getPropertyName() != null) {
            criteria.add(Restrictions.eq("propertyName", setting.getPropertyName()));
        }

        if(setting.getPropertyType() != null) {
            criteria.add(Restrictions.eq("propertyType", setting.getPropertyType()));
        }

        if(setting.getHighestType() != null) {
            criteria.add(Restrictions.eq("highestType", setting.getHighestType()));
        }

        return criteria;
    }
}
