package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.TransferFundsPage;
import utils.ConfigReader;
import utils.TestContext;

public class TransferFundsTest extends BaseTest {

    private String getValidUsername() {
        return (TestContext.username != null) ? TestContext.username : "john";
    }

    private String getValidPassword() {
        return (TestContext.password != null) ? TestContext.password : ConfigReader.getProperty("default.password", "Test1234");
    }

    private void loginAndNavigateToTransferFunds() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(getValidUsername(), getValidPassword());
        loginPage.clickTransferFunds();
    }

    @Test(priority = 1, description = "TC_TRANSFER_001: Verify Transfer Funds page opens")
    public void testTransferFundsPageOpens() {
        loginAndNavigateToTransferFunds();

        TransferFundsPage transferPage = new TransferFundsPage(driver);
        Assert.assertTrue(transferPage.isTransferPageDisplayed(),
                "Transfer Funds page is not displayed");
        Assert.assertEquals(transferPage.getPageHeading(), "Transfer Funds",
                "Page heading mismatch for Transfer Funds");
        Assert.assertTrue(transferPage.isFormDisplayed(),
                "Transfer form is not displayed on Transfer Funds page");
    }

    @Test(priority = 2, description = "TC_TRANSFER_002: Verify transfer form fields")
    public void testVerifyTransferFormFields() {
        loginAndNavigateToTransferFunds();

        TransferFundsPage transferPage = new TransferFundsPage(driver);
        transferPage.waitForAccountsToLoad();

        Assert.assertTrue(transferPage.isAmountFieldDisplayed(),
                "Amount field should be visible on Transfer Funds page");
        Assert.assertTrue(transferPage.isFromAccountDropdownDisplayed(),
                "From Account dropdown should be visible on Transfer Funds page");
        Assert.assertTrue(transferPage.isToAccountDropdownDisplayed(),
                "To Account dropdown should be visible on Transfer Funds page");
        Assert.assertTrue(transferPage.isTransferButtonDisplayed(),
                "Transfer button should be visible on Transfer Funds page");
    }

    @Test(priority = 3, description = "TC_TRANSFER_003: Successful transfer with valid amount and accounts")
    public void testSuccessfulTransfer() {
        loginAndNavigateToTransferFunds();

        TransferFundsPage transferPage = new TransferFundsPage(driver);
        transferPage.waitForAccountsToLoad();

        String fromAccount = transferPage.getFirstAvailableFromAccount();
        String toAccount = transferPage.getFirstAvailableToAccount();

        transferPage.transferFunds("50.00", fromAccount, toAccount);
        transferPage.waitForTransferResult();

        Assert.assertTrue(transferPage.isTransferCompleteDisplayed(),
                "Transfer Complete confirmation heading should be displayed");
        Assert.assertEquals(transferPage.getResultHeadingText(), "Transfer Complete!",
                "Confirmation heading text does not match 'Transfer Complete!'");
    }

    @Test(priority = 4, description = "TC_TRANSFER_004: Transfer with empty amount")
    public void testTransferWithEmptyAmount() {
        loginAndNavigateToTransferFunds();

        TransferFundsPage transferPage = new TransferFundsPage(driver);
        transferPage.waitForAccountsToLoad();

        transferPage.enterAmount("");
        transferPage.clickTransferButton();

        // ParaBank handles empty transfer by returning error view/message
        Assert.assertTrue(transferPage.isErrorDisplayed(),
                "Error should be displayed when submitting transfer with empty amount");
        String error = transferPage.getErrorMessage();
        Assert.assertTrue(error.contains("An internal error has occurred") || error.contains("cannot be empty"),
                "Unexpected error message text: " + error);
    }

    @Test(priority = 5, description = "TC_TRANSFER_005: Transfer with invalid amount")
    public void testTransferWithInvalidAmount() {
        loginAndNavigateToTransferFunds();

        TransferFundsPage transferPage = new TransferFundsPage(driver);
        transferPage.waitForAccountsToLoad();

        transferPage.enterAmount("invalid_amount");
        transferPage.clickTransferButton();

        // ParaBank handles non-numeric transfer by returning error view/message
        Assert.assertTrue(transferPage.isErrorDisplayed(),
                "Error should be displayed when submitting transfer with invalid amount");
        String error = transferPage.getErrorMessage();
        Assert.assertTrue(error.contains("An internal error has occurred") || error.contains("valid amount"),
                "Unexpected error message text: " + error);
    }

    @Test(priority = 6, description = "TC_TRANSFER_006: Verify transfer confirmation details")
    public void testVerifyTransferConfirmationDetails() {
        loginAndNavigateToTransferFunds();

        TransferFundsPage transferPage = new TransferFundsPage(driver);
        transferPage.waitForAccountsToLoad();

        String fromAccount = transferPage.getFirstAvailableFromAccount();
        String toAccount = transferPage.getFirstAvailableToAccount();

        transferPage.transferFunds("25.00", fromAccount, toAccount);
        transferPage.waitForTransferResult();

        Assert.assertEquals(transferPage.getTransferredAmountResult(), "$25.00",
                "Transferred amount result does not match '$25.00'");
        Assert.assertEquals(transferPage.getFromAccountIdResult(), fromAccount,
                "From account ID in confirmation does not match the selected from account");
        Assert.assertEquals(transferPage.getToAccountIdResult(), toAccount,
                "To account ID in confirmation does not match the selected to account");
    }
}
