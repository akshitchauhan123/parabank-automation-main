package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AccountsOverviewPage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.TestContext;

public class LoginTest extends BaseTest {

    private String getValidUsername() {
        return (TestContext.username != null) ? TestContext.username : "john";
    }

    private String getValidPassword() {
        return (TestContext.password != null) ? TestContext.password : ConfigReader.getProperty("default.password", "Test1234");
    }

    @Test(priority = 1, description = "TC_LOGIN_001: Valid login")
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(getValidUsername(), getValidPassword());

        AccountsOverviewPage overviewPage = new AccountsOverviewPage(driver);
        Assert.assertTrue(overviewPage.isOverviewPageDisplayed(),
                "Valid login failed: did not navigate to Accounts Overview page");
        Assert.assertTrue(loginPage.isLogoutDisplayed(),
                "Logout link should be visible after successful login");
    }

    @Test(priority = 2, description = "TC_LOGIN_002: Invalid username")
    public void testInvalidUsername() {
        LoginPage loginPage = new LoginPage(driver);
        String invalidUsername = "inv" + System.currentTimeMillis() % 100000;
        loginPage.login(invalidUsername, getValidPassword());

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed for invalid username");
        Assert.assertEquals(loginPage.getErrorMessage(),
                "The username and password could not be verified.",
                "Error message text does not match expected");
    }

    @Test(priority = 3, description = "TC_LOGIN_003: Invalid password")
    public void testInvalidPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(getValidUsername(), "WrongPass999");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed for invalid password");
        Assert.assertEquals(loginPage.getErrorMessage(),
                "The username and password could not be verified.",
                "Error message text does not match expected");
    }

    @Test(priority = 4, description = "TC_LOGIN_004: Empty username and password")
    public void testEmptyUsernameAndPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickLogin();

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed when submitting empty credentials");
        Assert.assertEquals(loginPage.getErrorMessage(),
                "Please enter a username and password.",
                "Error message text does not match expected for empty credentials");
    }

    @Test(priority = 5, description = "TC_LOGIN_005: Logout after login")
    public void testLogoutAfterLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(getValidUsername(), getValidPassword());

        AccountsOverviewPage overviewPage = new AccountsOverviewPage(driver);
        Assert.assertTrue(overviewPage.isOverviewPageDisplayed(),
                "Pre-condition failed: User was not logged in prior to logout test");

        loginPage.clickLogout();

        Assert.assertTrue(loginPage.isLoginPanelDisplayed(),
                "Login panel should be displayed after logout");
        Assert.assertTrue(driver.getCurrentUrl().contains("index.htm"),
                "URL should contain index.htm after logout");
    }
}
