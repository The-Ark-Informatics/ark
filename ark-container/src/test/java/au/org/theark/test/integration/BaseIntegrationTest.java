package au.org.theark.test.integration;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.test.util.externalresource.ScreenRecordingExternalResource;
import au.org.theark.test.util.runners.NameAwareRunner;
import au.org.theark.test.util.Reference;
import au.org.theark.web.pages.login.LoginPage;
import com.google.common.base.Function;
import com.google.common.net.HostAndPort;
import junit.framework.TestCase;
import org.apache.wicket.util.tester.WicketTester;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacv.*;
import org.junit.*;
import org.junit.rules.ExternalResource;
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
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

@Transactional
@RunWith(NameAwareRunner.class)
@ContextConfiguration(locations = {"file:src/test/resources/applicationContext.xml"})
public class BaseIntegrationTest extends TestCase {

    private transient static Logger log = LoggerFactory.getLogger(BaseIntegrationTest.class);

    protected static FirefoxDriver driver;
    protected WebElement element;

    private ApplicationContext context;
    File databaseDump;

    @Autowired
    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }

    protected IArkCommonService iArkCommonService;

    public IArkCommonService getiArkCommonService() {
        return iArkCommonService;
    }

    @Autowired
    public void setiArkCommonService(IArkCommonService iArkCommonService) {
        this.iArkCommonService = iArkCommonService;
    }

    @Rule
    public ExternalResource databaseResource = new MySQLExternalResource();

    @Rule
    public ExternalResource screenRecordingResource = new ScreenRecordingExternalResource();

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
        WebDriverWait wait = new WebDriverWait(driver, 60);
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

    //This only works as an internal class
    class MySQLExternalResource extends ExternalResource {

        @Override
        protected void before() throws Throwable {
            if(databaseDump == null) {
                try {
                    databaseDump = new File("/output/" + Reference.currentTestName + ".pre.sql");
                    databaseDump = createDatabaseDump(databaseDump);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void after() {
            if(databaseDump != null) {
                try {
                    File postTestDump = new File("/output/" + Reference.currentTestName + ".post.sql");
                    createDatabaseDump(postTestDump);
                    restoreDatabaseDump(databaseDump);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public File createDatabaseDump(File dumpFile) throws IOException {

            StringBuilder mysqlDumpCommandBuilder = new StringBuilder();
            mysqlDumpCommandBuilder.append("/usr/bin/mysqldump")
                    .append(" -h ")
                    .append(getDatabaseHost())
                    .append(" -u ")
                    .append(getDatabaseUser())
                    .append(" -p")
                    .append(getDatabasePassword())
                    .append(" --hex-blob --routines --triggers --disable-keys --extended-insert --quick --no-autocommit --databases admin audit config disease geno lims pheno reporting study spark")
                    .append(" > ")
                    .append(dumpFile.getAbsolutePath());

            String mysqlDumpCommand = mysqlDumpCommandBuilder.toString();

            log.info("Creating database dump...");
            log.debug(mysqlDumpCommand);
            ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", mysqlDumpCommand);
            Process p = pb.start();
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return dumpFile;
        }

        public void restoreDatabaseDump(File inputFile) throws IOException {
            StringBuilder mysqlRestoreCommandBuilder = new StringBuilder();
            mysqlRestoreCommandBuilder.append("/usr/bin/mysql")
                    .append(" -h ")
                    .append(getDatabaseHost())
                    .append(" -u ")
                    .append(getDatabaseUser())
                    .append(" -p")
                    .append(getDatabasePassword())
                    .append(" < ")
                    .append(inputFile.getAbsolutePath());

            String mysqlRestoreCommand = mysqlRestoreCommandBuilder.toString();

            log.info("Restoring database dump...");
            log.debug(mysqlRestoreCommand);
            ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", mysqlRestoreCommand);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        protected String getDatabaseHost() {
            return getDatabaseURI().getHost();
        }

        protected URI getDatabaseURI() {
            try {
                return new URI(getDataSource().getUrl().replace("jdbc:", ""));
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return null;
            }
        }

        protected String getDatabaseUser() {
            return getDataSource().getUsername();
        }

        protected String getDatabasePassword() {
            return getDataSource().getPassword();
        }

        protected DriverManagerDataSource getDataSource() {
            return (DriverManagerDataSource) context.getBean("dataSource");
        }
    }
}
