package au.org.theark.test.integration.global;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.selenium.utilities.WicketBy;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.test.integration.BaseIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        String[] subjectUIDsToAdd = {"1-2", "1-02", "1-20", "10-20", "fred", "jane", "pic01",
                "pic2", "pic02", "pic02a", "pic3", "pic4", "pic 4 else", "pic05", "pic 5",
                "pic 5 something", "pic 6", "pic   7", "pic100", "pic100a", "pic120", "pic121",
                "pic02000", "tom", "x2-g8", "x2-y7", "x2-y08", "x8-y8"}; //examples taken from NaturalOrderComparator.java

        System.out.println("subjects to add length:" + subjectUIDsToAdd.length);
        List scrambled = Arrays.asList(subjectUIDsToAdd);
        Collections.shuffle(scrambled); //scramble the subjectUIDs to they are added in a random order
        System.out.println("scrambled subjects to add length:" + scrambled.size());

        int counter = 0;
        for(String subjectUID : subjectUIDsToAdd) {
            log.info("Creating Subject: " + subjectUID);

            waitForElement(By.name("searchContainer:searchComponentPanel:searchForm:new")).click();
            waitForElement(By.name("detailContainer:detailPanel:detailsForm:detailFormContainer:linkSubjectStudy.subjectUID")).sendKeys(subjectUID);

            select = new Select(driver.findElement(By.name("detailContainer:detailPanel:detailsForm:detailFormContainer:linkSubjectStudy.subjectStatus")));
            select.selectByVisibleText("Subject");

            waitForElement(By.name("detailContainer:detailPanel:detailsForm:editButtonContainer:save")).click();
            waitForElement(By.className("feedbackPanelINFO"));
            waitForElement(By.name("detailContainer:detailPanel:detailsForm:editButtonContainer:cancel")).click();

            counter++;
        }

        System.out.println("Counter: " + counter);

        String[] sortedSubjectUIDs = {"1-02", "1-2", "1-20", "10-20", "fred", "jane", "pic01",
                "pic2", "pic02", "pic02a", "pic3", "pic4", "pic 4 else", "pic05", "pic 5",
                "pic 5 something", "pic 6", "pic   7", "pic100", "pic100a", "pic120", "pic121",
                "pic02000", "tom", "x2-g8", "x2-y7", "x2-y08", "x8-y8"};

        System.out.println("sorted subjects length:" + sortedSubjectUIDs.length);

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

        log.info("Ending test " + new Object(){}.getClass().getEnclosingMethod().getName());
    }
}
