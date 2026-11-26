package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AccountsOverviewPage;
import pages.LoginPage;
import pages.OpenNewAccountPage;
import utils.ConfigReader;
import utils.TestContext;

import java.util.List;

public class OpenNewAccountTest extends BaseTest {

    private String getValidUsername() {
        return (TestContext.username != null) ? TestContext.username : "john";
    }

    private String getValidPassword() {
        return (TestContext.password != null) ? TestContext.password : ConfigReader.getProperty("default.password", "Test1234");
    }

    private void loginAndNavigateToOpenAccount() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(getValidUsername(), getValidPassword());
        loginPage.clickOpenNewAccount();
    }

    @Test(priority = 1, description = "TC_ACCOUNT_001: Open New Account page")
    public void testOpenNewAccountPage() {
        loginAndNavigateToOpenAccount();

        OpenNewAccountPage openAccountPage = new OpenNewAccountPage(driver);
        Assert.assertTrue(openAccountPage.isOpenAccountPageDisplayed(),
                "Open New Account page is not displayed");
        Assert.assertEquals(openAccountPage.getPageHeading(), "Open New Account",
                "Open New Account page heading mismatch");
        Assert.assertTrue(openAccountPage.isFormDisplayed(),
                "Open New Account form should be displayed");
    }

    @Test(priority = 2, description = "TC_ACCOUNT_002: Verify account type dropdown")
    public void testVerifyAccountTypeDropdown() {
        loginAndNavigateToOpenAccount();

        OpenNewAccountPage openAccountPage = new OpenNewAccountPage(driver);
        openAccountPage.waitForFormToLoad();

        Assert.assertTrue(openAccountPage.isAccountTypeDropdownDisplayed(),
                "Account type dropdown should be displayed");

        List<String> options = openAccountPage.getAccountTypeOptions();
        Assert.assertTrue(options.contains("CHECKING"), "Dropdown should contain 'CHECKING' option");
        Assert.assertTrue(options.contains("SAVINGS"), "Dropdown should contain 'SAVINGS' option");
    }

    @Test(priority = 3, description = "TC_ACCOUNT_003: Open a new checking account")
    public void testOpenNewCheckingAccount() {
        loginAndNavigateToOpenAccount();

        OpenNewAccountPage openAccountPage = new OpenNewAccountPage(driver);
        openAccountPage.waitForFormToLoad();

        openAccountPage.selectAccountType("CHECKING");
        openAccountPage.clickOpenAccountButton();
        openAccountPage.waitForAccountOpenedConfirmation();

        Assert.assertTrue(openAccountPage.isAccountOpenedConfirmationDisplayed(),
                "Account Opened confirmation should be displayed");
    }

    @Test(priority = 4, description = "TC_ACCOUNT_004: Verify new account confirmation")
    public void testVerifyNewAccountConfirmation() {
        loginAndNavigateToOpenAccount();

        OpenNewAccountPage openAccountPage = new OpenNewAccountPage(driver);
        openAccountPage.waitForFormToLoad();

        openAccountPage.selectAccountType("SAVINGS");
        openAccountPage.clickOpenAccountButton();
        openAccountPage.waitForAccountOpenedConfirmation();

        Assert.assertEquals(openAccountPage.getConfirmationHeading(), "Account Opened!",
                "Confirmation heading does not match 'Account Opened!'");
        Assert.assertTrue(openAccountPage.getConfirmationMessage().contains("Congratulations, your account is now open."),
                "Confirmation message does not contain congratulations text");

        String newAccountId = openAccountPage.getNewAccountId();
        Assert.assertNotNull(newAccountId, "Generated account ID should not be null");
        Assert.assertTrue(newAccountId.matches("\\d+"), "Generated account ID should be numeric: " + newAccountId);
    }

    @Test(priority = 5, description = "TC_ACCOUNT_005: Verify new account appears in account overview")
    public void testVerifyNewAccountAppearsInAccountOverview() {
        loginAndNavigateToOpenAccount();

        OpenNewAccountPage openAccountPage = new OpenNewAccountPage(driver);
        openAccountPage.waitForFormToLoad();

        openAccountPage.selectAccountType("CHECKING");
        openAccountPage.clickOpenAccountButton();
        openAccountPage.waitForAccountOpenedConfirmation();

        String newlyCreatedAccountId = openAccountPage.getNewAccountId();
        Assert.assertNotNull(newlyCreatedAccountId, "New account ID must not be null");

        // Navigate to Accounts Overview via UI navigation
        openAccountPage.clickAccountsOverview();

        AccountsOverviewPage overviewPage = new AccountsOverviewPage(driver);
        overviewPage.waitForAccountDataToLoad();

        List<String> accountNumbers = overviewPage.getAccountNumbers();
        Assert.assertTrue(accountNumbers.contains(newlyCreatedAccountId),
                "Newly created account " + newlyCreatedAccountId + " should be present in Accounts Overview table: " + accountNumbers);
    }
}
