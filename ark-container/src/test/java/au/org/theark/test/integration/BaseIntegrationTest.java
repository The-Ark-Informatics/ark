package au.org.theark.test.integration;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.web.pages.login.LoginPage;
import com.google.common.base.Function;
import com.google.common.net.HostAndPort;
import junit.framework.TestCase;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/test/resources/applicationContext.xml"})
public class BaseIntegrationTest extends TestCase {

    private transient static Logger log = LoggerFactory.getLogger(BaseIntegrationTest.class);

    protected WicketTester tester;

    protected static FirefoxDriver driver;
    protected WebElement element;

    protected IArkCommonService iArkCommonService;

    public IArkCommonService getiArkCommonService() {
        return iArkCommonService;
    }

    @Autowired
    public void setiArkCommonService(IArkCommonService iArkCommonService) {
        this.iArkCommonService = iArkCommonService;
    }

    @BeforeClass
    public static void openBrowser() {
        driver = new FirefoxDriver();
        Dimension size = driver.manage().window().getSize();
        driver.manage().window().maximize();
        // If the window size didn't change, assume that it couldn't resize the window so set the size to 1080p
        if (driver.manage().window().getSize().equals(size)) {
            driver.manage().window().setSize(new Dimension(1920, 1080));
        }
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Before
    public void setup() {
        tester = new WicketTester();
        tester.startPage(LoginPage.class);
        driver.get("http://" + getHostIP() + ":8080/ark");
    }

    @AfterClass
    public static void closeBrowser() {
        driver.quit();
    }

    public String getHostIP() {
        String ipAddress = "127.0.0.1";
        String DOCKER_HOST = System.getenv("DOCKER_HOST");
        if (DOCKER_HOST != null && !DOCKER_HOST.isEmpty()) {
            ipAddress = DOCKER_HOST.replaceAll(".*://", "");
            ipAddress = HostAndPort.fromString(ipAddress).getHostText();
        } else if (System.getenv("DOCKER") != null) {
            // This is the case if the test was started in a container via docker-compose
            ipAddress = "tomcat";
        }
        return ipAddress;
    }

    protected WebElement waitForElement(final By locator) {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        return wait.until((Function<WebDriver, WebElement>) driver -> driver.findElement(locator));
    }

    public void loginAsSuperUser() {
        String superUserName = "arksuperuser@ark.org.au";
        String superUserPassword = "Password_1";

        if (System.getenv("ARK_USERNAME") != null) {
            superUserName = System.getenv("ARK_USERNAME");
        }
        if (System.getenv("ARK_SUPERUSER_PASSWORD") != null) {
            superUserPassword = System.getenv("ARK_SUPERUSER_PASSWORD");
        }

        loginAsUser(superUserName, superUserPassword);
    }

    public void loginAsUser(String username, String password) {
        driver.findElement(By.name("userName")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("signInButton")).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
