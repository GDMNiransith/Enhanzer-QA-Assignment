package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class AutomationPracticeFormPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // --- Locators ---
    private final By firstNameField = By.id("firstName");
    private final By lastNameField = By.id("lastName");
    private final By userEmailField = By.id("userEmail");
    private final By genderMaleRadio = By.xpath("//label[text()='Male']");
    private final By userMobileField = By.id("userNumber");
    private final By uploadPictureButton = By.id("uploadPicture");
    private final By currentAddressArea = By.id("currentAddress");
    private final By submitButton = By.id("submit");
    private final By thanksHeader = By.id("example-modal-sizes-title-lg");

    private final By requiredFieldError = By.cssSelector(".was-validated :invalid");

    public AutomationPracticeFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get("https://demoqa.com/automation-practice-form");
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
    }



    public void fillForm(String fName, String lName, String email, String mobile) {
        driver.findElement(firstNameField).sendKeys(fName);
        driver.findElement(lastNameField).sendKeys(lName);
        driver.findElement(userEmailField).sendKeys(email);
        driver.findElement(userMobileField).sendKeys(mobile);
        driver.findElement(genderMaleRadio).click();
    }


    public void fillCurrentAddress(String payload) {
        driver.findElement(currentAddressArea).sendKeys(payload);
    }

    public void uploadFile(String filePath) {
        // SendKeys works directly on the hidden input field for file uploads
        driver.findElement(uploadPictureButton).sendKeys(filePath);
    }

    public void clickSubmit() {
        WebElement submitBtn = driver.findElement(submitButton);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitBtn);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);
    }

    // --- Verification Methods (Assertions) ---

    /**
     * Checks if the successful submission modal is displayed.
     */
    public boolean isSubmissionSuccessful() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(thanksHeader)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getMobileNumberNativeErrorMessage() {
        return driver.findElement(userMobileField).getAttribute("validationMessage");
    }

    public boolean isEmailFieldInvalid() {

        return driver.findElement(userEmailField).getAttribute("class").contains("field-error")
                || driver.findElement(userEmailField).getAttribute("class").contains("is-invalid");
    }
}
