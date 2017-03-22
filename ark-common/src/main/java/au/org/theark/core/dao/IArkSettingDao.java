package au.org.theark.core.dao;

import au.org.theark.core.model.config.entity.*;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;

import java.util.List;

public interface IArkSettingDao {

    public SystemWideSetting getSystemWideSetting(String key);

    public StudySpecificSetting getStudySpecificSetting(Study study, String key);

    public Setting getSetting(String key, Study study, ArkUser arkUser);

    public void saveSetting(Setting setting);

    public int getSettingsCount(Setting setting);

    public List<Setting> searchPageableSettings(Setting setting, long first, long count);

    public Setting getUserSpecificSetting(ArkUser arkUser, String propertyName);

    public void createSettingFile(SettingFile sf);

    public SettingFile getSettingFileByFileId(String fileId);

    public void deleteSettingFile(SettingFile settingFile);

    public SettingFile getSettingFileFromSetting(String key, Study study, ArkUser arkUser);

    public List<SettingValidator> getSettingValidatorsForSetting(Setting setting);
}
