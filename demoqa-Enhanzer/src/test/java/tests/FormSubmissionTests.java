package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.AutomationPracticeFormPage;

public class FormSubmissionTests extends BaseTest {
    // Data: {firstName, lastName, email, mobile, description}
    @DataProvider(name = "mandatoryFieldData")
    public Object[][] getMandatoryFieldData() {
        return new Object[][] {
                // Missing First Name
                {"", "Doe", "test@test.com", "1234567890", "Missing First Name"},
                // Missing Last Name
                {"John", "", "test@test.com", "1234567890", "Missing Last Name"},
                // Short Mobile Number (assuming 10 digits required)
                {"John", "Doe", "test@test.com", "12345", "Short Mobile Number"},
        };
    }

    // --- Test Scenarios ---

    //Valid form submission with all correct inputs
    @Test(description = "1. Verify successful form submission with all mandatory fields filled (Positive Test).")
    public void testValidFormSubmission() {
        // Initialize the Page Object instance here
        AutomationPracticeFormPage formPage = new AutomationPracticeFormPage(driver);

        formPage.fillForm("John", "Doe", "valid@test.com", "1234567890");
        formPage.clickSubmit();

        //Verify UI elements, form validation, and submission status.
        Assert.assertTrue(formPage.isSubmissionSuccessful(), "Assertion Failed: Submission modal did not display.");
    }

    //Form submission with mandatory fields left blank (Data-Driven Testing)
    @Test(dataProvider = "mandatoryFieldData", description = "2. Verify form validation prevents submission when mandatory fields are missing or invalid.")
    public void testMandatoryFieldsValidation(String fName, String lName, String email, String mobile, String description) {
        //Initialize the Page Object instance here
        AutomationPracticeFormPage formPage = new AutomationPracticeFormPage(driver);

        formPage.fillForm(fName, lName, email, mobile);
        formPage.clickSubmit();

        // Assertion: Verify submission failure
        Assert.assertFalse(formPage.isSubmissionSuccessful(), "Assertion Failed: Form submitted with missing/invalid data: " + description);

        // Check specific validation message for the Mobile field if it was the failure point
        if (mobile.length() < 10 && mobile.length() > 0) {
            Assert.assertNotNull(formPage.getMobileNumberNativeErrorMessage(), "Assertion Failed: No native error message for short mobile number.");
        }
    }

    //Validation of invalid email format
    @Test(description = "3. Verify client-side validation for an invalid email format.")
    public void testInvalidEmailFormatValidation() {
        AutomationPracticeFormPage formPage = new AutomationPracticeFormPage(driver);

        formPage.fillForm("Test", "User", "invalid-email-format", "1234567890"); // Invalid email
        formPage.clickSubmit();

        // Assertion: Verify the form submission failed
        Assert.assertFalse(formPage.isSubmissionSuccessful(), "Assertion Failed: Form submitted successfully with an invalid email.");

        // Assertion: Verify UI Validation (if the email field shows an error state)
        Assert.assertTrue(formPage.isEmailFieldInvalid(), "Assertion Failed: Email field did not show an invalid state.");
    }

    // 4. File upload functionality (Use a known dummy file path)
    @Test(description = "4. Verify file upload functionality with a valid file type.")
    public void testValidFileUpload() {
        AutomationPracticeFormPage formPage = new AutomationPracticeFormPage(driver);

        String validFilePath = System.getProperty("user.dir") + "/dummy.png";

        formPage.fillForm("Upload", "Test", "upload@test.com", "1234567890");
        formPage.uploadFile(validFilePath);
        formPage.clickSubmit();
        Assert.assertTrue(formPage.isSubmissionSuccessful(), "Assertion Failed: Form submission failed after file upload.");
    }
    @Test(description = "5. Edge Case: Attempt to submit XSS payload in address field to check security.")
    public void testXSSInjectionAttempt() {
        AutomationPracticeFormPage formPage = new AutomationPracticeFormPage(driver);

        formPage.fillForm("Security", "Test", "safe@test.com", "1234567890");

        // XSS Payload
        String payload = "<script>console.log('XSS-Test')</script>";
        formPage.fillCurrentAddress(payload);

        formPage.clickSubmit();

        // Assertion: We assert that the application remained functional (submitted).
        // A true security failure would be the script execution/alert, which we cannot assert for directly here.
        Assert.assertTrue(formPage.isSubmissionSuccessful(), "Assertion Failed: Form failed to submit due to XSS input.");
    }
}
