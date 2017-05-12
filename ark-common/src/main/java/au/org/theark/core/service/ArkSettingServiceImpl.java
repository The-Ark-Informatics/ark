package au.org.theark.core.service;

import au.org.theark.core.Constants;
import au.org.theark.core.dao.IArkSettingDao;
import au.org.theark.core.exception.ArkCheckSumNotSameException;
import au.org.theark.core.exception.ArkFileNotFoundException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.config.entity.*;
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

    private IArkCommonService iArkCommonService;

    @Autowired
    public void setArkSettingDao(IArkSettingDao iArkSettingDao) {
        this.iArkSettingDao = iArkSettingDao;
    }

    @Autowired
    public void setiArkCommonService(IArkCommonService iArkCommonService) {
        this.iArkCommonService = iArkCommonService;
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
    public void saveSetting(Setting setting) {
        iArkSettingDao.saveSetting(setting);
    }

    @Override
    public int getSettingsCount(Setting setting) {
        return iArkSettingDao.getSettingsCount(setting);
    }

    @Override
    public List<Setting> searchPageableSettings(Setting setting, int first, int count) {
        return iArkSettingDao.searchPageableSettings(setting, first, count);
    }

    @Override
    public Setting getUserSpecificSetting(ArkUser arkUser, String propertyName) {
        return iArkSettingDao.getUserSpecificSetting(arkUser, propertyName);
    }

    @Override
    public void createSettingFile(SettingFile sf) {
        iArkSettingDao.createSettingFile(sf);
    }

    @Override
    public SettingFile getSettingFileByFileId(String fileId) {
        return iArkSettingDao.getSettingFileByFileId(fileId);
    }

    @Override
    public void delete(SettingFile settingFile, String arkSettingsDir) throws ArkSystemException, ArkFileNotFoundException {
        String fileId = settingFile.getFileId();
        String checksum = settingFile.getChecksum();
        if (iArkCommonService.deleteArkFileAttachment(null, null, fileId, arkSettingsDir, checksum)) {
            iArkSettingDao.deleteSettingFile(settingFile);
        } else {
            System.out.println("Could not find the file - " + fileId);
        }
    }

    @Override
    public SettingFile getSettingFileFromSetting(String key, Study study, ArkUser arkUser) {
        return iArkSettingDao.getSettingFileFromSetting(key, study, arkUser);
    }

    @Override
    public String getSettingFilePath(SettingFile settingFile) {
        try {
           return iArkCommonService.retriveArkFileAttachmentAsFile(null, null, Constants.ARK_SETTINGS_DIR, settingFile.getFileId(), settingFile.getChecksum()).getAbsolutePath();
        } catch (ArkSystemException e) {
            e.printStackTrace();
        } catch (ArkFileNotFoundException e) {
            e.printStackTrace();
        } catch (ArkCheckSumNotSameException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SettingValidator> getSettingValidatorsForSetting(Setting setting) {
        return iArkSettingDao.getSettingValidatorsForSetting(setting);
    }
}
