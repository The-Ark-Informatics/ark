package au.org.theark.lims.model.dao;

import au.org.theark.core.dao.IStudyDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.study.entity.*;
import au.org.theark.core.testcategories.UnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/test/resources/applicationContext.xml"})
@Category(UnitTests.class)
public class BiospecimenDaoTest {

    private IBiospecimenDao iBiospecimenDao;

    public IBiospecimenDao getBiospecimenDao() {
        return iBiospecimenDao;
    }

    @Autowired
    public void setBiospecimenDao(IBiospecimenDao biospecimenDao) {
        this.iBiospecimenDao = biospecimenDao;
    }

    private IStudyDao iStudyDao;

    public IStudyDao getStudyDao() {
        return iStudyDao;
    }

    @Autowired
    public void setStudyDao(IStudyDao studyDao) {
        this.iStudyDao = studyDao;
    }

    @Test
    @Transactional
    public void testCreateBiospecimen() {
        Biospecimen biospecimen = new Biospecimen();

        try {
            iBiospecimenDao.createBiospecimen(biospecimen);
        } catch (ArkSystemException e) {
            //This won't happen as the new biospecimen has no uid
        }


    }

}
