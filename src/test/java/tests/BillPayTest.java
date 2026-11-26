package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BillPayPage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.TestContext;

public class BillPayTest extends BaseTest {

    private String getValidUsername() {
        return (TestContext.username != null) ? TestContext.username : "john";
    }

    private String getValidPassword() {
        return (TestContext.password != null) ? TestContext.password : ConfigReader.getProperty("default.password", "Test1234");
    }

    private void loginAndNavigateToBillPay() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(getValidUsername(), getValidPassword());
        loginPage.clickBillPay();
    }

    @Test(priority = 1, description = "TC_BILL_001: Verify Bill Payment page opens")
    public void testBillPaymentPageOpens() {
        loginAndNavigateToBillPay();

        BillPayPage billPayPage = new BillPayPage(driver);
        Assert.assertTrue(billPayPage.isBillPayPageDisplayed(),
                "Bill Payment page should be displayed");
        Assert.assertEquals(billPayPage.getPageHeading(), "Bill Payment Service",
                "Bill Payment page heading mismatch");
        Assert.assertTrue(billPayPage.isBillPayFormDisplayed(),
                "Bill Payment form should be visible");
    }

    @Test(priority = 2, description = "TC_BILL_002: Verify mandatory fields")
    public void testVerifyMandatoryFields() {
        loginAndNavigateToBillPay();

        BillPayPage billPayPage = new BillPayPage(driver);
        billPayPage.waitForAccountsToLoad();

        Assert.assertTrue(billPayPage.isPayeeNameFieldDisplayed(), "Payee Name field should be displayed");
        Assert.assertTrue(billPayPage.isAddressFieldDisplayed(), "Address field should be displayed");
        Assert.assertTrue(billPayPage.isCityFieldDisplayed(), "City field should be displayed");
        Assert.assertTrue(billPayPage.isStateFieldDisplayed(), "State field should be displayed");
        Assert.assertTrue(billPayPage.isZipCodeFieldDisplayed(), "Zip Code field should be displayed");
        Assert.assertTrue(billPayPage.isPhoneFieldDisplayed(), "Phone number field should be displayed");
        Assert.assertTrue(billPayPage.isAccountNumberFieldDisplayed(), "Account number field should be displayed");
        Assert.assertTrue(billPayPage.isVerifyAccountFieldDisplayed(), "Verify Account field should be displayed");
        Assert.assertTrue(billPayPage.isAmountFieldDisplayed(), "Amount field should be displayed");
        Assert.assertTrue(billPayPage.isFromAccountDropdownDisplayed(), "From Account dropdown should be displayed");
        Assert.assertTrue(billPayPage.isSendPaymentButtonDisplayed(), "Send Payment button should be displayed");
    }

    @Test(priority = 3, description = "TC_BILL_003: Successful bill payment with valid data")
    public void testSuccessfulBillPayment() {
        loginAndNavigateToBillPay();

        BillPayPage billPayPage = new BillPayPage(driver);
        billPayPage.payBill("Central Electric Co", "987654", "45.00");
        billPayPage.waitForPaymentResult();

        Assert.assertTrue(billPayPage.isPaymentCompleteDisplayed(),
                "Bill Payment Complete confirmation heading should be displayed");
        Assert.assertEquals(billPayPage.getResultHeadingText(), "Bill Payment Complete",
                "Confirmation heading does not match 'Bill Payment Complete'");
    }

    @Test(priority = 4, description = "TC_BILL_004: Empty mandatory fields validation")
    public void testEmptyMandatoryFieldsValidation() {
        loginAndNavigateToBillPay();

        BillPayPage billPayPage = new BillPayPage(driver);
        billPayPage.waitForAccountsToLoad();
        billPayPage.clickSendPaymentButton();

        Assert.assertTrue(billPayPage.areMandatoryFieldErrorsDisplayed(),
                "Validation error messages should be displayed for mandatory fields");
        Assert.assertEquals(billPayPage.getPayeeNameError(), "Payee name is required.",
                "Payee name validation error mismatch");
        Assert.assertEquals(billPayPage.getAddressError(), "Address is required.",
                "Address validation error mismatch");
        Assert.assertEquals(billPayPage.getCityError(), "City is required.",
                "City validation error mismatch");
        Assert.assertEquals(billPayPage.getStateError(), "State is required.",
                "State validation error mismatch");
        Assert.assertEquals(billPayPage.getZipCodeError(), "Zip Code is required.",
                "Zip code validation error mismatch");
        Assert.assertEquals(billPayPage.getPhoneError(), "Phone number is required.",
                "Phone number validation error mismatch");
        Assert.assertEquals(billPayPage.getAccountEmptyError(), "Account number is required.",
                "Account number validation error mismatch");
        Assert.assertEquals(billPayPage.getVerifyAccountEmptyError(), "Account number is required.",
                "Verify account validation error mismatch");
        Assert.assertEquals(billPayPage.getAmountEmptyError(), "The amount cannot be empty.",
                "Amount empty validation error mismatch");
    }

    @Test(priority = 5, description = "TC_BILL_005: Invalid amount validation")
    public void testInvalidAmountValidation() {
        loginAndNavigateToBillPay();

        BillPayPage billPayPage = new BillPayPage(driver);
        billPayPage.fillBillPayForm(
                "City Water Supply",
                "100 Aqua Way",
                "Metro City",
                "NY",
                "10001",
                "555-111-2222",
                "112233",
                "112233",
                "invalid_amount"
        );
        billPayPage.clickSendPaymentButton();

        Assert.assertEquals(billPayPage.getAmountInvalidError(), "Please enter a valid amount.",
                "Validation error for invalid amount was not displayed");
    }

    @Test(priority = 6, description = "TC_BILL_006: Verify payment confirmation")
    public void testVerifyPaymentConfirmation() {
        loginAndNavigateToBillPay();

        BillPayPage billPayPage = new BillPayPage(driver);
        String payee = "National Gas Provider";
        String amount = "80.00";
        billPayPage.payBill(payee, "554433", amount);
        billPayPage.waitForPaymentResult();

        Assert.assertEquals(billPayPage.getResultPayeeName(), payee,
                "Confirmed payee name does not match expected payee");
        Assert.assertTrue(billPayPage.getResultAmount().contains(amount),
                "Confirmed amount does not match expected amount");
        Assert.assertTrue(billPayPage.getResultMessageText().contains("was successful."),
                "Confirmation message should state payment was successful");
    }
}
