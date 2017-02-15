package au.org.theark.core.service;

import au.org.theark.core.model.config.entity.Setting;
import au.org.theark.core.model.config.entity.StudySpecificSetting;
import au.org.theark.core.model.config.entity.SystemWideSetting;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;

import java.util.List;

public interface IArkSettingService {

    public SystemWideSetting getSystemWideSetting(String key);

    public StudySpecificSetting getStudySpecificSetting(Study study, String key);

    public Setting getSetting(String key, Study study, ArkUser arkUser);

    public void saveSetting(Setting setting);

    public int getSettingsCount(Setting setting);

    public List<Setting> searchPageableSettings(Setting setting, int first, int count);

    public Setting getUserSpecificSetting(ArkUser arkUser, String propertyName);


}
