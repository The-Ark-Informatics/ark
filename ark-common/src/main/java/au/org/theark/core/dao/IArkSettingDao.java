package au.org.theark.core.dao;

import au.org.theark.core.model.config.entity.StudySpecificSetting;
import au.org.theark.core.model.config.entity.Setting;
import au.org.theark.core.model.config.entity.SystemWideSetting;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;

import java.util.List;

public interface IArkSettingDao {

    public SystemWideSetting getSystemWideSetting(String key);

    public StudySpecificSetting getStudySpecificSetting(Study study, String key);

    public Setting getSetting(String key, Study study, ArkUser arkUser);

    public void save(Object object);

    public int getSettingsCount(Setting setting);

    public List<Setting> searchPageableSettings(Setting setting, int first, int count);
    
}
