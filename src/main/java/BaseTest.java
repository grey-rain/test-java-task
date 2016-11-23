import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BaseTest {
    private WebDriver driver;
    private static final String SCR_FOLDER = "./src/test/scr/";
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
    public void afterMethod(ITestResult testResult) {
        takeSceenshotOnFailure(testResult);
        log.info("Shut down driver.");
        driver.quit();
    }

    private void takeSceenshotOnFailure(ITestResult testResult) {
        if (testResult.getStatus() == ITestResult.FAILURE) {
            log.debug("Test failed. Trying to take screenshot.");
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String timestamp = dateFormat.format(new Date(System.currentTimeMillis()));
                File scrFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFile, new File(String.format(SCR_FOLDER + "%s.png", timestamp)));
                log.debug("Success.");
            } catch (java.io.IOException io) {
                log.error("Failed to access path to save screenshot. {}", io.getLocalizedMessage());
            }
        }
    }
}
