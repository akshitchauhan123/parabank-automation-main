package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.FindTransactionsPage;
import pages.LoginPage;
import pages.TransferFundsPage;
import utils.ConfigReader;
import utils.TestContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FindTransactionsTest extends BaseTest {

    private String getValidUsername() {
        return (TestContext.username != null) ? TestContext.username : "john";
    }

    private String getValidPassword() {
        return (TestContext.password != null) ? TestContext.password : ConfigReader.getProperty("default.password", "Test1234");
    }

    private void loginAndNavigateToFindTransactions() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(getValidUsername(), getValidPassword());
        loginPage.clickFindTransactions();
    }


    @Test(priority = 1, description = "TC_TRANSACTION_001: Verify Find Transactions page")
    public void testFindTransactionsPage() {
        loginAndNavigateToFindTransactions();

        FindTransactionsPage findPage = new FindTransactionsPage(driver);
        Assert.assertTrue(findPage.isFindTransactionsPageDisplayed(),
                "Find Transactions page should be displayed");
        Assert.assertEquals(findPage.getPageHeading(), "Find Transactions",
                "Find Transactions page heading mismatch");
        Assert.assertTrue(findPage.isFormDisplayed(),
                "Find Transactions form should be visible");
        Assert.assertTrue(findPage.isAccountSelectDisplayed(),
                "Account selection dropdown should be visible");
    }

    @Test(priority = 2, description = "TC_TRANSACTION_002: Search by account number")
    public void testSearchByAccountNumber() {
        loginAndNavigateToFindTransactions();

        FindTransactionsPage findPage = new FindTransactionsPage(driver);
        findPage.waitForAccountsToLoad();

        List<String> accounts = findPage.getAccountOptions();
        Assert.assertFalse(accounts.isEmpty(), "Account options dropdown should not be empty");

        String targetAccount = accounts.get(0);
        findPage.selectAccount(targetAccount);
        Assert.assertEquals(findPage.getSelectedAccountId(), targetAccount,
                "Selected account ID does not match expected account");
    }

    @Test(priority = 3, description = "TC_TRANSACTION_003: Search by transaction ID")
    public void testSearchByTransactionId() {
        loginAndNavigateToFindTransactions();

        FindTransactionsPage findPage = new FindTransactionsPage(driver);
        findPage.waitForAccountsToLoad();

        // Search with a numeric transaction ID
        String searchId = (TestContext.transactionId != null) ? TestContext.transactionId : "14101";
        findPage.searchByTransactionId(searchId);
        findPage.waitForResultsToLoad();

        Assert.assertTrue(findPage.isTransactionTableDisplayed(),
                "Transaction results table should be displayed after search by ID");
    }

    @Test(priority = 4, description = "TC_TRANSACTION_004: Search by date")
    public void testSearchByDate() {
        loginAndNavigateToFindTransactions();

        FindTransactionsPage findPage = new FindTransactionsPage(driver);
        findPage.waitForAccountsToLoad();

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        findPage.searchByDate(today);
        findPage.waitForResultsToLoad();

        Assert.assertTrue(findPage.isTransactionTableDisplayed(),
                "Transaction results table should be displayed after searching by date");
    }

    @Test(priority = 5, description = "TC_TRANSACTION_005: Validate transaction search results")
    public void testValidateTransactionSearchResults() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(getValidUsername(), getValidPassword());

        // Create a second account to ensure genuine cross-account transfer
        loginPage.clickOpenNewAccount();
        pages.OpenNewAccountPage openAccountPage = new pages.OpenNewAccountPage(driver);
        openAccountPage.waitForFormToLoad();
        String newAcc = openAccountPage.openNewAccount("SAVINGS");

        // Transfer funds to generate an authentic transaction record
        openAccountPage.clickTransferFunds();
        TransferFundsPage transferPage = new TransferFundsPage(driver);
        transferPage.waitForAccountsToLoad();
        String from = transferPage.getFirstAvailableFromAccount();
        transferPage.transferFunds("15.00", from, newAcc);
        transferPage.waitForTransferResult();

        // Navigate to Find Transactions and select source account
        transferPage.clickFindTransactions();
        FindTransactionsPage findPage = new FindTransactionsPage(driver);
        findPage.waitForAccountsToLoad();
        findPage.selectAccount(from);

        // Search by amount 15.00 that was transferred
        findPage.searchByAmount("15.00");
        findPage.waitForResultsToLoad();

        Assert.assertTrue(findPage.isTransactionTableDisplayed(),
                "Transaction table should be displayed for amount search");
        Assert.assertTrue(findPage.getTransactionCount() > 0,
                "Transaction table should contain at least 1 record for the recent transaction");

        String description = findPage.getFirstTransactionDescription();
        Assert.assertNotNull(description, "Transaction description should not be null");
        Assert.assertTrue(description.contains("Funds Transfer"),
                "Transaction description should contain 'Funds Transfer': " + description);
    }
}
