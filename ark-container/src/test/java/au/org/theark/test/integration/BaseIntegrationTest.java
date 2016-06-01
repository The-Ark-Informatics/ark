package au.org.theark.test.integration;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.web.pages.login.LoginPage;
import com.google.common.base.Function;
import com.google.common.net.HostAndPort;
import junit.framework.TestCase;
import org.apache.wicket.util.tester.WicketTester;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.service.internal.StandardServiceRegistryImpl;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
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

import java.util.Properties;
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
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
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
        }
        return ipAddress;
    }

    protected WebElement waitForElement(final By locator) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        return wait.until((Function<WebDriver, WebElement>) driver -> driver.findElement(locator));
    }


    //TODO: Manage case if there is different password, use env_file?
    public void loginAsSuperUser() {
        loginAsUser("arksuperuser@ark.org.au", "Password_1");
    }

    public void loginAsUser(String username, String password) {
        driver.findElement(By.name("userName")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("signInButton")).click();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
