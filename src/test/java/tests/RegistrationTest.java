package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.RegisterPage;
import utils.ConfigReader;
import utils.TestDataGenerator;

public class RegistrationTest extends BaseTest {

    @Test(priority = 1, description = "TC_REG_001: Successful registration with unique generated username")
    public void testSuccessfulRegistration() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickRegister();

        RegisterPage registerPage = new RegisterPage(driver);
        String uniqueUsername = TestDataGenerator.generateUniqueUsername("reg");
        String password = ConfigReader.getProperty("default.password", "Test1234");

        registerPage.registerNewUser(uniqueUsername, password);

        Assert.assertTrue(registerPage.isRegistrationSuccessful(),
                "Registration was not successful; confirmation title not displayed as expected");
        Assert.assertTrue(registerPage.getConfirmationMessage().contains("Your account was created successfully."),
                "Confirmation message does not contain success text");
    }

    @Test(priority = 2, description = "TC_REG_002: Empty mandatory fields")
    public void testEmptyMandatoryFields() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickRegister();

        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.clickRegisterButton();

        Assert.assertTrue(registerPage.areMandatoryFieldErrorsDisplayed(),
                "Validation error messages should be displayed for all mandatory fields");
        Assert.assertEquals(registerPage.getFirstNameError(), "First name is required.",
                "First name validation error mismatch");
        Assert.assertEquals(registerPage.getLastNameError(), "Last name is required.",
                "Last name validation error mismatch");
        Assert.assertEquals(registerPage.getAddressError(), "Address is required.",
                "Address validation error mismatch");
        Assert.assertEquals(registerPage.getCityError(), "City is required.",
                "City validation error mismatch");
        Assert.assertEquals(registerPage.getStateError(), "State is required.",
                "State validation error mismatch");
        Assert.assertEquals(registerPage.getZipCodeError(), "Zip Code is required.",
                "Zip code validation error mismatch");
        Assert.assertEquals(registerPage.getSsnError(), "Social Security Number is required.",
                "SSN validation error mismatch");
        Assert.assertEquals(registerPage.getUsernameError(), "Username is required.",
                "Username validation error mismatch");
        Assert.assertEquals(registerPage.getPasswordError(), "Password is required.",
                "Password validation error mismatch");
        Assert.assertEquals(registerPage.getConfirmPasswordError(), "Password confirmation is required.",
                "Confirm password validation error mismatch");
    }

    @Test(priority = 3, description = "TC_REG_003: Verify registration confirmation")
    public void testRegistrationConfirmation() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickRegister();

        RegisterPage registerPage = new RegisterPage(driver);
        String uniqueUsername = TestDataGenerator.generateUniqueUsername("cnf");
        String password = ConfigReader.getProperty("default.password", "Test1234");

        registerPage.registerNewUser(uniqueUsername, password);

        String expectedHeading = "Welcome " + uniqueUsername;
        String expectedMessage = "Your account was created successfully. You are now logged in.";

        Assert.assertEquals(registerPage.getConfirmationHeading(), expectedHeading,
                "Confirmation heading does not match expected welcome user format");
        Assert.assertEquals(registerPage.getConfirmationMessage(), expectedMessage,
                "Confirmation message body does not match expected text");
    }
}
