package utility;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;

public class BaseTest {
    protected WebDriver driver;
    protected ExtentTest test;
    protected ExtentReports extent;

    @BeforeTest
    public void setUpExtent() {
        deleteOldScreenshots();
        ExtentSparkReporter spark = new ExtentSparkReporter("target/ExtentReports/ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Initialize the test for ExtentReports
        test = extent.createTest(this.getClass().getSimpleName());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = takeScreenshot(result.getName());
            test.fail("Test Failed: " + result.getThrowable());
            test.addScreenCaptureFromPath(screenshotPath);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            String screenshotPath = takeScreenshot(result.getName());
            test.addScreenCaptureFromPath(screenshotPath);
            test.pass("Test Passed");
        }

        if (driver != null) {
            driver.quit();
        }
    }

    @AfterTest
    public void tearDownExtent() {
        // Ensure the extent report is written
        if (extent != null) {
            extent.flush();
        }
    }

    public String takeScreenshot(String screenshotName) {
        String relativePath = "target/screenshots/" + screenshotName + ".png";
        String absolutePath = System.getProperty("user.dir") + "/" + relativePath;
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshot, new File(absolutePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return absolutePath;
    }
    public void deleteOldScreenshots() {
        File screenshotDirectory = new File(System.getProperty("user.dir") + "/target/screenshots/");
        if (screenshotDirectory.exists()) {
            try {
                FileUtils.cleanDirectory(screenshotDirectory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
