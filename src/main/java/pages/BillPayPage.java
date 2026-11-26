package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import utils.WaitUtils;

public class BillPayPage extends BasePage {

    private final By pageHeading = By.xpath("//h1[contains(text(),'Bill Payment Service')]");
    private final By billPayForm = By.cssSelector("#billpayForm form");

    // Input fields
    private final By payeeNameInput = By.name("payee.name");
    private final By addressInput = By.name("payee.address.street");
    private final By cityInput = By.name("payee.address.city");
    private final By stateInput = By.name("payee.address.state");
    private final By zipCodeInput = By.name("payee.address.zipCode");
    private final By phoneInput = By.name("payee.phoneNumber");
    private final By accountNumberInput = By.name("payee.accountNumber");
    private final By verifyAccountInput = By.name("verifyAccount");
    private final By amountInput = By.name("amount");
    private final By fromAccountIdDropdown = By.name("fromAccountId");
    private final By fromAccountOptions = By.cssSelector("select[name='fromAccountId'] option");
    private final By sendPaymentButton = By.xpath("//input[@value='Send Payment']");

    // Validation errors
    private final By payeeNameError = By.id("validationModel-name");
    private final By addressError = By.id("validationModel-address");
    private final By cityError = By.id("validationModel-city");
    private final By stateError = By.id("validationModel-state");
    private final By zipCodeError = By.id("validationModel-zipCode");
    private final By phoneError = By.id("validationModel-phoneNumber");
    private final By accountEmptyError = By.id("validationModel-account-empty");
    private final By verifyAccountEmptyError = By.id("validationModel-verifyAccount-empty");
    private final By amountEmptyError = By.id("validationModel-amount-empty");
    private final By amountInvalidError = By.id("validationModel-amount-invalid");

    // Results container
    private final By billPayResult = By.id("billpayResult");
    private final By resultHeading = By.xpath("//div[@id='billpayResult']/h1[contains(text(),'Bill Payment Complete')]");
    private final By resultPayeeName = By.id("payeeName");
    private final By resultAmount = By.id("amount");
    private final By resultFromAccountId = By.id("fromAccountId");
    private final By resultMessage = By.cssSelector("#billpayResult p");

    public BillPayPage(WebDriver driver) {
        super(driver);
    }

    public boolean isBillPayPageDisplayed() {
        return isDisplayed(pageHeading) || driver.getCurrentUrl().contains("billpay.htm");
    }

    public String getPageHeading() {
        return getText(pageHeading);
    }

    public boolean isBillPayFormDisplayed() {
        return isDisplayed(billPayForm);
    }

    public boolean isPayeeNameFieldDisplayed() {
        return isDisplayed(payeeNameInput);
    }

    public boolean isAddressFieldDisplayed() {
        return isDisplayed(addressInput);
    }

    public boolean isCityFieldDisplayed() {
        return isDisplayed(cityInput);
    }

    public boolean isStateFieldDisplayed() {
        return isDisplayed(stateInput);
    }

    public boolean isZipCodeFieldDisplayed() {
        return isDisplayed(zipCodeInput);
    }

    public boolean isPhoneFieldDisplayed() {
        return isDisplayed(phoneInput);
    }

    public boolean isAccountNumberFieldDisplayed() {
        return isDisplayed(accountNumberInput);
    }

    public boolean isVerifyAccountFieldDisplayed() {
        return isDisplayed(verifyAccountInput);
    }

    public boolean isAmountFieldDisplayed() {
        return isDisplayed(amountInput);
    }

    public boolean isFromAccountDropdownDisplayed() {
        return isDisplayed(fromAccountIdDropdown);
    }

    public boolean isSendPaymentButtonDisplayed() {
        return isDisplayed(sendPaymentButton);
    }

    public void waitForAccountsToLoad() {
        WaitUtils.getWait(driver, 15).until(ExpectedConditions.numberOfElementsToBeMoreThan(fromAccountOptions, 0));
    }

    public void enterPayeeName(String name) {
        typeSlowly(payeeNameInput, name);
    }

    public void enterAddress(String address) {
        typeSlowly(addressInput, address);
    }

    public void enterCity(String city) {
        typeSlowly(cityInput, city);
    }

    public void enterState(String state) {
        typeSlowly(stateInput, state);
    }

    public void enterZipCode(String zipCode) {
        typeSlowly(zipCodeInput, zipCode);
    }

    public void enterPhone(String phone) {
        typeSlowly(phoneInput, phone);
    }

    public void enterAccountNumber(String accountNumber) {
        typeSlowly(accountNumberInput, accountNumber);
    }

    public void enterVerifyAccount(String verifyAccount) {
        typeSlowly(verifyAccountInput, verifyAccount);
    }

    public void enterAmount(String amount) {
        typeSlowly(amountInput, amount);
    }

    public void selectFromAccountByIndex(int index) {
        waitForAccountsToLoad();
        Select select = new Select(WaitUtils.waitForElementClickable(driver, fromAccountIdDropdown));
        select.selectByIndex(index);
    }

    public void selectFromAccount(String accountNumber) {
        waitForAccountsToLoad();
        Select select = new Select(WaitUtils.waitForElementClickable(driver, fromAccountIdDropdown));
        select.selectByVisibleText(accountNumber);
    }

    public String getSelectedFromAccount() {
        waitForAccountsToLoad();
        Select select = new Select(driver.findElement(fromAccountIdDropdown));
        return select.getFirstSelectedOption().getText().trim();
    }

    public void clickSendPaymentButton() {
        click(sendPaymentButton);
    }

    public void fillBillPayForm(String name, String address, String city, String state,
                                String zipCode, String phone, String accountNumber,
                                String verifyAccount, String amount) {
        waitForAccountsToLoad();
        enterPayeeName(name);
        enterAddress(address);
        enterCity(city);
        enterState(state);
        enterZipCode(zipCode);
        enterPhone(phone);
        enterAccountNumber(accountNumber);
        enterVerifyAccount(verifyAccount);
        enterAmount(amount);
    }

    public void payBill(String payeeName, String accountNumber, String amount) {
        fillBillPayForm(
                payeeName,
                "456 Utility Avenue",
                "Metro City",
                "NY",
                "10001",
                "555-987-6543",
                accountNumber,
                accountNumber,
                amount
        );
        selectFromAccountByIndex(0);
        clickSendPaymentButton();
    }

    public void waitForPaymentResult() {
        WaitUtils.waitForElementVisible(driver, resultHeading, 15);
    }

    public boolean isPaymentCompleteDisplayed() {
        return isDisplayed(resultHeading);
    }

    public String getResultHeadingText() {
        waitForPaymentResult();
        return getText(resultHeading);
    }

    public String getResultPayeeName() {
        waitForPaymentResult();
        return getText(resultPayeeName);
    }

    public String getResultAmount() {
        waitForPaymentResult();
        return getText(resultAmount);
    }

    public String getResultFromAccountId() {
        waitForPaymentResult();
        return getText(resultFromAccountId);
    }

    public String getResultMessageText() {
        waitForPaymentResult();
        return getText(resultMessage);
    }

    public String getPayeeNameError() {
        return getText(payeeNameError);
    }

    public String getAddressError() {
        return getText(addressError);
    }

    public String getCityError() {
        return getText(cityError);
    }

    public String getStateError() {
        return getText(stateError);
    }

    public String getZipCodeError() {
        return getText(zipCodeError);
    }

    public String getPhoneError() {
        return getText(phoneError);
    }

    public String getAccountEmptyError() {
        return getText(accountEmptyError);
    }

    public String getVerifyAccountEmptyError() {
        return getText(verifyAccountEmptyError);
    }

    public String getAmountEmptyError() {
        return getText(amountEmptyError);
    }

    public String getAmountInvalidError() {
        return getText(amountInvalidError);
    }

    public boolean areMandatoryFieldErrorsDisplayed() {
        return isDisplayed(payeeNameError)
                && isDisplayed(addressError)
                && isDisplayed(cityError)
                && isDisplayed(stateError)
                && isDisplayed(zipCodeError)
                && isDisplayed(phoneError)
                && isDisplayed(accountEmptyError)
                && isDisplayed(verifyAccountEmptyError)
                && isDisplayed(amountEmptyError);
    }
}
