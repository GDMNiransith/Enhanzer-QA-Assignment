package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setup() {
        // Use WebDriverManager to automatically handle driver setup
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterMethod
    // Capturing screenshots for failures automatically
    public void tearDown(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            captureScreenshot(result.getMethod().getMethodName());
            System.out.println("TEST FAILED: Screenshot saved for " + result.getMethod().getMethodName());
        }
        if (driver != null) {
            driver.quit();
        }
    }

    // Helper method for capturing and saving the screenshot
    private void captureScreenshot(String testName) {
        try {
            File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName + "_" + timestamp + ".png";

            // Define the destination path relative to the project root
            File destinationFile = new File("target/screenshots/" + fileName);
            FileUtils.copyFile(sourceFile, destinationFile);

        } catch (IOException e) {
            System.err.println("Exception while taking screenshot: " + e.getMessage());
        }
    }
}
