package au.org.theark.core.service;

import au.org.theark.core.Constants;
import au.org.theark.core.dao.IArkSettingDao;
import au.org.theark.core.model.config.entity.Setting;
import au.org.theark.core.model.config.entity.StudySpecificSetting;
import au.org.theark.core.model.config.entity.SystemWideSetting;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service(Constants.ARK_SETTING_SERVICE)
public class ArkSettingServiceImpl implements IArkSettingService {

    private IArkSettingDao iArkSettingDao;

    @Autowired
    public void setArkSettingDao(IArkSettingDao iArkSettingDao) {
        this.iArkSettingDao = iArkSettingDao;
    }

    @Override
    public SystemWideSetting getSystemWideSetting(String key) {
        return iArkSettingDao.getSystemWideSetting(key);
    }

    @Override
    public StudySpecificSetting getStudySpecificSetting(Study study, String key) {
        return iArkSettingDao.getStudySpecificSetting(study, key);
    }

    @Override
    public Setting getSetting(String key, Study study, ArkUser arkUser) {
        return iArkSettingDao.getSetting(key, study, arkUser);
    }

    @Override
    public void save(Object object) {
        iArkSettingDao.save(object);
    }

    @Override
    public int getSettingsCount(Setting setting) {
        return iArkSettingDao.getSettingsCount(setting);
    }

    @Override
    public List<Setting> searchPageableSettings(Setting setting, int first, int count) {
        return iArkSettingDao.searchPageableSettings(setting, first, count);
    }


}
