package au.org.theark.test.integration.global;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.selenium.utilities.WicketBy;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.test.integration.BaseIntegrationTest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.jetty.util.ArrayUtil;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ITestGlobal extends BaseIntegrationTest {

    private transient static Logger log = LoggerFactory.getLogger(ITestGlobal.class);

    @Test
    public void testNaturalSortOrder() {
        log.info("Starting test " + new Object(){}.getClass().getEnclosingMethod().getName());
        loginAsSuperUser();

        String studyName = new Object(){}.getClass().getEnclosingMethod().getName();
        String chiefInvestigator = "John Doe";
        String studyStatus = "Active";

        driver.findElement(By.name("searchContainer:searchStudyPanel:searchForm:new")).click();
        waitForElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:study.name")).sendKeys(studyName);
        Select select = new Select(driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:study.studyStatus")));
        select.selectByVisibleText(studyStatus);
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:study.chiefInvestigator")).sendKeys(chiefInvestigator);

        driver.findElement(By.name("detailContainer:detailPanel:detailForm:editButtonContainer:save")).click();

        waitForElement(By.className("feedbackPanelINFO"));
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:editButtonContainer:cancel")).click();
        waitForElement(By.partialLinkText(studyName)).click();
        waitForElement(By.partialLinkText("Subject")).click();

        String[] subjectUIDsToAdd = {"C-1358920", "C-132494", "C-5001", "C-15921", "C-00001", "C-500"};

        for(String subjectUID : subjectUIDsToAdd) {
            log.info("Creating Subject: " + subjectUID);

            waitForElement(By.name("searchContainer:searchComponentPanel:searchForm:new")).click();
            waitForElement(By.name("detailContainer:detailPanel:detailsForm:detailFormContainer:linkSubjectStudy.subjectUID")).sendKeys(subjectUID);

            select = new Select(driver.findElement(By.name("detailContainer:detailPanel:detailsForm:detailFormContainer:linkSubjectStudy.subjectStatus")));
            select.selectByVisibleText("Subject");

            waitForElement(By.name("detailContainer:detailPanel:detailsForm:editButtonContainer:save")).click();
            waitForElement(By.className("feedbackPanelINFO"));
            waitForElement(By.name("detailContainer:detailPanel:detailsForm:editButtonContainer:cancel")).click();
        }

        String[] sortedSubjectUIDs = {"C-00001", "C-500", "C-5001", "C-15921", "C-132494", "C-1358920"};

        waitForElement(By.name("searchContainer:searchComponentPanel:searchForm:new"));

        WebElement table_element = waitForElement(By.className("dataview"));
        List<WebElement> tr_collection = table_element.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
        tr_collection.remove(0); //Removing the header

        List<String> tableSubjectUIDs = new ArrayList<>();
        for(WebElement trElement : tr_collection)
        {
            List<WebElement> td_collection = trElement.findElements(By.tagName("td"));

            String subjectUID = td_collection.get(1).getText();
            tableSubjectUIDs.add(subjectUID);
        }

        assertEquals(sortedSubjectUIDs.length, tableSubjectUIDs.size());

        Assert.assertArrayEquals(sortedSubjectUIDs, tableSubjectUIDs.toArray());

        //Add second study to test global search
        waitForElement(By.partialLinkText("Study")).click();

        studyName+="2";

        waitForElement(By.name("searchContainer:searchStudyPanel:searchForm:new")).click();
        waitForElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:study.name")).sendKeys(studyName);
        select = new Select(driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:study.studyStatus")));
        select.selectByVisibleText(studyStatus);
        driver.findElement(By.name("detailContainer:detailPanel:detailForm:detailFormContainer:study.chiefInvestigator")).sendKeys(chiefInvestigator);

        driver.findElement(By.name("detailContainer:detailPanel:detailForm:editButtonContainer:save")).click();

        waitForElement(By.className("feedbackPanelINFO"));
        waitForElement(By.partialLinkText("Subject")).click();

        String[] subjectUIDsToAdd2 = {"C-1358921", "C-133494", "C-501", "C-15921", "C-00001", "C-500"};

        log.info("Creating subjects for " + studyName + " study");

        for(String subjectUID : subjectUIDsToAdd2) {
            log.info("Creating Subject: " + subjectUID);

            waitForElement(By.name("searchContainer:searchComponentPanel:searchForm:new")).click();
            waitForElement(By.name("detailContainer:detailPanel:detailsForm:detailFormContainer:linkSubjectStudy.subjectUID")).sendKeys(subjectUID);

            select = new Select(driver.findElement(By.name("detailContainer:detailPanel:detailsForm:detailFormContainer:linkSubjectStudy.subjectStatus")));
            select.selectByVisibleText("Subject");

            waitForElement(By.name("detailContainer:detailPanel:detailsForm:editButtonContainer:save")).click();
            waitForElement(By.className("feedbackPanelINFO"));
            waitForElement(By.name("detailContainer:detailPanel:detailsForm:editButtonContainer:cancel")).click();
        }

        waitForElement(By.name("searchContainer:searchComponentPanel:searchForm:new")).getText();
        if(waitForElement(WicketBy.wicketPath("moduleTabsList_tabs-container_tabs_5_link")).getText().equalsIgnoreCase("Global Search")) {
            waitForElement(By.partialLinkText("Global")).click();
        }

        //Wait for Biospecimen Data tab to be available. This means that the Demographic data tab must have loaded
        waitForElement(WicketBy.wicketPath("moduleTabsList_panel_globalSearchSubMenu_tabs-container_tabs_1_link_title"));

        String[] sortedSubjectUIDs2 = {"C-00001", "C-500", "C-5001", "C-15921", "C-132494", "C-1358920",
                                        "C-00001", "C-500", "C-501", "C-15921", "C-133494", "C-1358921" };

        table_element = waitForElement(By.className("dataview"));
        tr_collection = table_element.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
        tr_collection.remove(0); //Removing the header

        tableSubjectUIDs = new ArrayList<>();
        for(WebElement trElement : tr_collection)
        {
            List<WebElement> td_collection = trElement.findElements(By.tagName("td"));

            String subjectUID = td_collection.get(1).getText();
            tableSubjectUIDs.add(subjectUID);
        }

        assertEquals(sortedSubjectUIDs2.length, tableSubjectUIDs.size());

        Assert.assertArrayEquals(sortedSubjectUIDs2, tableSubjectUIDs.toArray());

        log.info("Ending test " + new Object(){}.getClass().getEnclosingMethod().getName());
    }
}
