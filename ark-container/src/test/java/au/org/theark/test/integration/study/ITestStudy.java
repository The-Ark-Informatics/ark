package au.org.theark.test.integration.study;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.selenium.utilities.WicketBy;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.test.integration.BaseIntegrationTest;
import com.google.common.base.Function;
import org.hibernate.Hibernate;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.util.FileUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ITestStudy extends BaseIntegrationTest {

    private transient static Logger log = LoggerFactory.getLogger(ITestStudy.class);

    @Test
    public void testNewStudy() {
        log.info("Starting test " + new Object(){}.getClass().getEnclosingMethod().getName());
        loginAsSuperUser();

        String studyName = new Object(){}.getClass().getEnclosingMethod().getName();
        String chiefInvestigator = "John Doe";
        String studyStatus = "Active";

        driver.findElement(By.name("searchContainer:searchStudyPanel:searchForm:new")).click();
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:study.name")).sendKeys(studyName);
        Select select = new Select(driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:study.studyStatus")));
        select.selectByVisibleText(studyStatus);
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:study.chiefInvestigator")).sendKeys(chiefInvestigator);

        driver.findElement(By.name("detailContainer:detailPanel:detailForm:editButtonContainer:save")).click();

        waitForElement(By.className("feedbackPanelINFO"));

        Study search_criteria = new Study();
        search_criteria.setName(studyName);
        search_criteria.setChiefInvestigator(chiefInvestigator);

        List<Study> result = iArkCommonService.getStudy(search_criteria);

        assertEquals(1, result.size());

        //Assuming the last assert was correct, then we want the first item from the list
        Study resultStudy = result.get(0);
        assertEquals(studyName, resultStudy.getName());
        assertEquals(chiefInvestigator, resultStudy.getChiefInvestigator());
        assertEquals(studyStatus, resultStudy.getStudyStatus().getName());
        log.info("Ending test " + new Object(){}.getClass().getEnclosingMethod().getName());
    }

    @Test
    public void testAddUserToStudy() {
        log.info("Starting test " + new Object(){}.getClass().getEnclosingMethod().getName());

        loginAsSuperUser();

        String studyName = new Object(){}.getClass().getEnclosingMethod().getName();
        String chiefInvestigator = "John Doe";
        String studyStatus = "Active";

        driver.findElement(By.name("searchContainer:searchStudyPanel:searchForm:new")).click();

        waitForElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:study.name"));

        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:study.name")).sendKeys(studyName);
        Select select = new Select(driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:study.studyStatus")));
        select.selectByVisibleText(studyStatus);
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:study.chiefInvestigator")).sendKeys(chiefInvestigator);

        driver.findElement(By.name("detailContainer:detailPanel:detailForm:editButtonContainer:save")).click();
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:editButtonContainer:cancel")).click();

        waitForElement(By.partialLinkText(studyName)).click();

        waitForElement(By.partialLinkText("Manage Users")).click();

        waitForElement(By.name("searchContainer:searchPanel:searchForm:new")).click();

        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:userName")).sendKeys("test@email.com");
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:email")).sendKeys("test@email.com");
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:firstName")).sendKeys("Test");
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:lastName")).sendKeys("User");
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:groupPasswordContainer:password")).sendKeys("TestUser_1");
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:groupPasswordContainer:confirmPassword")).sendKeys("TestUser_1");

        select = new Select(driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:arkUserAccountPanelcontainer:arkUserRoleList:0:arkRole")));
        select.selectByVisibleText("Study Administrator");
        select = new Select(driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:arkUserAccountPanelcontainer:arkUserRoleList:1:arkRole")));
        select.selectByVisibleText("Subject Administrator");

        driver.findElement(By.name("detailContainer:detailPanel:detailForm:editButtonContainer:save")).click();
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:editButtonContainer:cancel")).click();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            ArkUser newUser = iArkCommonService.getArkUser("test@email.com");
            ArkUserVO arkUserVO = new ArkUserVO();
            arkUserVO.setArkUserEntity(newUser);

            List<Study> studies = iArkCommonService.getStudyListForUser(arkUserVO);

            assertEquals(studies.size(), 1);
            assertTrue(studies.stream().filter(study -> studyName.equals(study.getName())).findAny().isPresent());
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        log.info("Ending test " + new Object(){}.getClass().getEnclosingMethod().getName());
    }

    @Test
    public void testRemoveUserFromStudy() {
        log.info("Starting test " + new Object() {
        }.getClass().getEnclosingMethod().getName());

        loginAsSuperUser();

        String studyName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String chiefInvestigator = "John Doe";
        String studyStatus = "Active";

        driver.findElement(By.name("searchContainer:searchStudyPanel:searchForm:new")).click();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:study.name")).sendKeys(studyName);
        Select select = new Select(driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:study.studyStatus")));
        select.selectByVisibleText(studyStatus);
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:study.chiefInvestigator")).sendKeys(chiefInvestigator);

        driver.findElement(By.name("detailContainer:detailPanel:detailForm:editButtonContainer:save")).click();
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:editButtonContainer:cancel")).click();

        waitForElement(By.partialLinkText(studyName)).click();

        waitForElement(WicketBy.wicketPath("moduleTabsList_panel_studySubMenus_panel_containerForm_detailContainer_detailPanel_detailForm_summaryPanel_studySummaryLabel"));

        waitForElement(By.partialLinkText("Manage Users")).click();

        waitForElement(By.name("searchContainer:searchPanel:searchForm:new")).click();

        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:userName")).sendKeys("test2@email.com");
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:email")).sendKeys("test2@email.com");
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:firstName")).sendKeys("Test");
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:lastName")).sendKeys("User");
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:groupPasswordContainer:password")).sendKeys("TestUser_1");
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:groupPasswordContainer:confirmPassword")).sendKeys("TestUser_1");

        select = new Select(driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:arkUserAccountPanelcontainer:arkUserRoleList:0:arkRole")));
        select.selectByVisibleText("Study Administrator");
        select = new Select(driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:arkUserAccountPanelcontainer:arkUserRoleList:1:arkRole")));
        select.selectByVisibleText("Subject Administrator");

        driver.findElement(By.name("detailContainer:detailPanel:detailForm:editButtonContainer:save")).click();
        waitForElement(By.partialLinkText("Manage Users")).click();

        driver.findElement(By.partialLinkText("test2@email.com")).click();

        driver.findElement(By.name("detailContainer:detailPanel:detailForm:editButtonContainer:remove")).click();

        try {
            WebDriverWait wait = new WebDriverWait(driver, 2);
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (Exception e) {
            //exception handling
            e.printStackTrace();
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try{
            iArkCommonService.getArkUser("test2@email.com");
            fail("EntityNotFoundException should be thrown");
        } catch (EntityNotFoundException e) {
        }

        log.info("Ending test " + new Object() {}.getClass().getEnclosingMethod().getName());
    }

    @Test
    public void testOpenManageUserModal() {

        log.info("Starting test " + new Object() {
        }.getClass().getEnclosingMethod().getName());

        loginAsSuperUser();

        String studyName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String chiefInvestigator = "John Doe";
        String studyStatus = "Active";

        driver.findElement(By.name("searchContainer:searchStudyPanel:searchForm:new")).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:study.name")).sendKeys(studyName);
        Select select = new Select(driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:study.studyStatus")));
        select.selectByVisibleText(studyStatus);
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:study.chiefInvestigator")).sendKeys(chiefInvestigator);

        driver.findElement(By.name("detailContainer:detailPanel:detailForm:editButtonContainer:save")).click();

        driver.findElement(WicketBy.wicketPath("myDetailLink")).click();

        WebElement modal = driver.findElement(WicketBy.wicketPath("modalWindow_content_myDetailsPanel"));

        assertNotNull(modal);

        if(modal != null) {
            driver.findElement(WicketBy.wicketPath("modalWindow_content_myDetailsPanel_userDetailsForm_close")).click();
        }

        log.info("Ending test " + new Object() {}.getClass().getEnclosingMethod().getName());
    }

}
