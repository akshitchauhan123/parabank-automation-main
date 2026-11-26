package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AccountsOverviewPage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.TestContext;

public class AccountOverviewTest extends BaseTest {

    private String getValidUsername() {
        return (TestContext.username != null) ? TestContext.username : "john";
    }

    private String getValidPassword() {
        return (TestContext.password != null) ? TestContext.password : ConfigReader.getProperty("default.password", "Test1234");
    }

    @Test(priority = 1, description = "TC_ACC_001: Verify account overview after login")
    public void testVerifyAccountOverviewAfterLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(getValidUsername(), getValidPassword());

        AccountsOverviewPage overviewPage = new AccountsOverviewPage(driver);
        Assert.assertTrue(overviewPage.isOverviewPageDisplayed(),
                "Accounts overview page is not displayed after login");
        Assert.assertTrue(driver.getCurrentUrl().contains("overview.htm"),
                "Current URL does not contain 'overview.htm'");
        Assert.assertEquals(overviewPage.getPageHeading(), "Accounts Overview",
                "Page heading does not match 'Accounts Overview'");
    }

    @Test(priority = 2, description = "TC_ACC_002: Verify account table is displayed")
    public void testVerifyAccountTableDisplayed() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(getValidUsername(), getValidPassword());

        AccountsOverviewPage overviewPage = new AccountsOverviewPage(driver);
        overviewPage.waitForAccountDataToLoad();

        Assert.assertTrue(overviewPage.isAccountTableDisplayed(),
                "Account table is not displayed on the Accounts Overview page");
        Assert.assertTrue(overviewPage.getAccountCount() > 0,
                "Account table does not contain any account records");

        String primaryAccount = overviewPage.getFirstAccountNumber();
        Assert.assertNotNull(primaryAccount, "Primary account number should not be null");
        Assert.assertFalse(primaryAccount.trim().isEmpty(), "Primary account number should not be empty");
    }
}
