import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.concurrent.TimeUnit;

public class BaseTest {
    private WebDriver driver;
    private Logger log = LoggerFactory.getLogger(BaseTest.class);

    protected WebDriver getDriver() {
        return driver;
    }

    @BeforeMethod
    public void prepareDriver() {
        log.info("Init driver.");
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterMethod
    public void afterSuite() {
        log.info("Shut down driver.");
        driver.quit();
    }
}
