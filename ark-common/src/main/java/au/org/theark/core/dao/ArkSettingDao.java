package au.org.theark.core.dao;

import au.org.theark.core.model.config.entity.*;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("settingDao")
public class ArkSettingDao extends HibernateSessionDao implements IArkSettingDao {

    public Setting getSettingByID(Long id) {
        Criteria criteria = getSession().createCriteria(Setting.class);
        criteria.add(Restrictions.idEq(id));

        return (Setting) criteria.uniqueResult();
    }

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
    public UserSpecificSetting getUserSpecificSetting(ArkUser arkUser, String key) {
        Criteria criteria = getSession().createCriteria(UserSpecificSetting.class);
        criteria.add(Restrictions.eq("propertyName", key));
        criteria.add(Restrictions.eq("arkUser.id", arkUser.getId()));

        UserSpecificSetting userSpecificSetting = (UserSpecificSetting) criteria.uniqueResult();

        return userSpecificSetting;
    }

    @Override
    public void createSettingFile(SettingFile sf) {
        getSession().save(sf);
    }

    @Override
    public SettingFile getSettingFileByFileId(String fileId) {
        Criteria criteria = getSession().createCriteria(SettingFile.class);
        criteria.add(Restrictions.eq("fileId", fileId));

        SettingFile settingFile = (SettingFile) criteria.uniqueResult();

        return settingFile;
    }

    @Override
    public void deleteSettingFile(SettingFile settingFile) {
       getSession().delete(settingFile);
    }

    @Override
    public SettingFile getSettingFileFromSetting(String key, Study study, ArkUser arkUser) {
        Setting setting = getSetting(key, study, arkUser);
        if(setting != null && setting.getPropertyType() == PropertyType.FILE) {
            SettingFile settingFile = getSettingFileByFileId(setting.getPropertyValue());
            return settingFile;
        } else {
            return null;
        }
    }

    @Override
    public List<SettingValidator> getSettingValidatorsForSetting(Setting setting) {
        Criteria criteria = getSession().createCriteria(SettingValidator.class);
        criteria.add(Restrictions.eq("settingPropertyName", setting.getPropertyName()));

        return (List<SettingValidator>) criteria.list();
    }

    @Override
    public Setting getSetting(String key, Study study, ArkUser arkUser) {

        //If both are null then it will definitely be a system wide setting, so don't bother checking the other kinds.
        if(study == null && arkUser == null) {
            return getSystemWideSetting(key);
        }

        if(arkUser != null) {
            //test UserSpecificSetting when it exists
            Setting userSpecificSetting = getUserSpecificSetting(arkUser, key);
            if (userSpecificSetting != null) {
                return userSpecificSetting;
            }
        }

        if(study != null) {
            Setting studySpecificSetting = getStudySpecificSetting(study, key);
            if (studySpecificSetting != null) {
                return studySpecificSetting;
            }
        }

        Setting setting = getSystemWideSetting(key);
        if(setting != null)
            return setting;

        return null;
    }

    @Override
    public void saveSetting(Setting setting) {
        if (setting.getClass() != SystemWideSetting.class) {
            if (setting.getPropertyValue() == null || setting.getPropertyValue().isEmpty()) {
                if (setting.getId() != null) {
                    Setting fromDB = getSettingByID(setting.getId());
                    if (fromDB != null) {
                        getSession().delete(fromDB);
                        return;
                    }
                }
            }
        }
        getSession().saveOrUpdate(setting);
    }

    @Override
    public int getSettingsCount(Setting setting) {
        Criteria criteria = buildSettingCriteria(setting);
        criteria.setProjection(Projections.rowCount());
        Long count = (Long) criteria.uniqueResult();
        return count.intValue();
    }

    @Override
    public List<Setting> searchPageableSettings(Setting setting, long first, long count) {
        Criteria criteria = buildSettingCriteria(setting);
        criteria.setFirstResult(Math.toIntExact(first));
        criteria.setMaxResults(Math.toIntExact(count));

        return (List<Setting>) criteria.list();
    }

    private Criteria buildSettingCriteria(Setting setting) { //TODO: Improve this
        Criteria criteria = getSession().createCriteria(Setting.class);

        if(setting.getPropertyName() != null) {
            criteria.add(Restrictions.like("propertyName", setting.getPropertyName(), MatchMode.ANYWHERE));
        }

        if(setting.getPropertyValue() != null) {
            criteria.add(Restrictions.like("propertyValue", setting.getPropertyValue(), MatchMode.ANYWHERE));
        }

        if(setting.getPropertyType() != null) {
            criteria.add(Restrictions.eq("propertyType", setting.getPropertyType()));
        }

        if(setting.getHighestType() != null) {
            if(setting.getHighestType().equalsIgnoreCase("system")) {
                criteria.add(Restrictions.eq("class", SystemWideSetting.class));
            }
            if(setting.getHighestType().equalsIgnoreCase("study")) {
                /*criteria.add(
                        Restrictions.or(
                            Restrictions.or(
                                    Restrictions.eq("highestType", setting.getHighestType()),
                                    Restrictions.eq("highestType", "system")
                            ),
                            Restrictions.eq("class", SystemWideSetting.class)
                        )
                );*/
                criteria.add(
                        Restrictions.or(
                                Restrictions.eq("highestType", "study"),
                                Restrictions.eq("highestType", "user")
                        )
                );
                criteria.add(Restrictions.eq("class", SystemWideSetting.class));
            }
            if(setting.getHighestType().equalsIgnoreCase("user")) {
                criteria.add(
                        Restrictions.eq("highestType", "user")
                );
                criteria.add(Restrictions.eq("class", SystemWideSetting.class));
            }
        }

        /*if(setting instanceof StudySpecificSetting) {
            StudySpecificSetting sss = (StudySpecificSetting) setting;
            criteria.add(
                    Restrictions.or(
                        Restrictions.eq("study", sss.getStudy()),
                        Restrictions.isNull("study")
                    )
            );
        }*/

        return criteria;
    }
}
