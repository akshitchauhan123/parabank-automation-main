package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import utils.WaitUtils;

import java.util.List;

public class TransferFundsPage extends BasePage {

    private final By pageHeading = By.xpath("//h1[contains(text(),'Transfer Funds')]");
    private final By formContainer = By.id("showForm");
    private final By amountInput = By.id("amount");
    private final By fromAccountDropdown = By.id("fromAccountId");
    private final By toAccountDropdown = By.id("toAccountId");
    private final By fromAccountOptions = By.cssSelector("#fromAccountId option");
    private final By toAccountOptions = By.cssSelector("#toAccountId option");
    private final By transferButton = By.xpath("//input[@value='Transfer']");

    // Client-side and server error elements
    private final By amountEmptyError = By.xpath("//p[@id='amount.errors' and contains(text(),'cannot be empty')]");
    private final By amountInvalidError = By.xpath("//p[@id='amount.errors' and contains(text(),'valid amount')]");
    private final By showErrorContainer = By.id("showError");
    private final By serverErrorMessage = By.cssSelector("#showError p.error");
    private final By serverErrorHeading = By.cssSelector("#showError h1.title");

    // Results container
    private final By resultContainer = By.id("showResult");
    private final By resultHeading = By.xpath("//div[@id='showResult']/h1[contains(text(),'Transfer Complete!')]");
    private final By amountResult = By.id("amountResult");
    private final By fromAccountIdResult = By.id("fromAccountIdResult");
    private final By toAccountIdResult = By.id("toAccountIdResult");

    public TransferFundsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isTransferPageDisplayed() {
        return isDisplayed(pageHeading) || driver.getCurrentUrl().contains("transfer.htm");
    }

    public String getPageHeading() {
        return getText(pageHeading);
    }

    public boolean isFormDisplayed() {
        return isDisplayed(formContainer);
    }

    public boolean isAmountFieldDisplayed() {
        return isDisplayed(amountInput);
    }

    public boolean isFromAccountDropdownDisplayed() {
        return isDisplayed(fromAccountDropdown);
    }

    public boolean isToAccountDropdownDisplayed() {
        return isDisplayed(toAccountDropdown);
    }

    public boolean isTransferButtonDisplayed() {
        return isDisplayed(transferButton);
    }

    public void waitForAccountsToLoad() {
        WaitUtils.getWait(driver, 15).until(ExpectedConditions.numberOfElementsToBeMoreThan(fromAccountOptions, 0));
        WaitUtils.getWait(driver, 15).until(ExpectedConditions.numberOfElementsToBeMoreThan(toAccountOptions, 0));
    }

    public void enterAmount(String amount) {
        WebElement element = WaitUtils.waitForElementVisible(driver, amountInput);
        element.clear();
        if (amount != null && !amount.isEmpty()) {
            element.sendKeys(amount);
        }
    }

    public void selectFromAccount(String accountNumber) {
        waitForAccountsToLoad();
        Select select = new Select(WaitUtils.waitForElementClickable(driver, fromAccountDropdown));
        select.selectByVisibleText(accountNumber);
    }

    public void selectToAccount(String accountNumber) {
        waitForAccountsToLoad();
        Select select = new Select(WaitUtils.waitForElementClickable(driver, toAccountDropdown));
        select.selectByVisibleText(accountNumber);
    }

    public String getFirstAvailableFromAccount() {
        waitForAccountsToLoad();
        Select select = new Select(driver.findElement(fromAccountDropdown));
        return select.getOptions().get(0).getText().trim();
    }

    public String getFirstAvailableToAccount() {
        waitForAccountsToLoad();
        Select select = new Select(driver.findElement(toAccountDropdown));
        List<WebElement> options = select.getOptions();
        if (options.size() > 1) {
            return options.get(1).getText().trim();
        }
        return options.get(0).getText().trim();
    }

    public void clickTransferButton() {
        click(transferButton);
    }

    public void transferFunds(String amount, String fromAccount, String toAccount) {
        waitForAccountsToLoad();
        enterAmount(amount);
        if (fromAccount != null) {
            selectFromAccount(fromAccount);
        }
        if (toAccount != null) {
            selectToAccount(toAccount);
        }
        clickTransferButton();
    }

    public void waitForTransferResult() {
        WaitUtils.waitForElementVisible(driver, resultHeading, 15);
    }

    public boolean isTransferCompleteDisplayed() {
        return isDisplayed(resultHeading);
    }

    public String getResultHeadingText() {
        waitForTransferResult();
        return getText(resultHeading);
    }

    public String getTransferredAmountResult() {
        waitForTransferResult();
        return getText(amountResult);
    }

    public String getFromAccountIdResult() {
        waitForTransferResult();
        return getText(fromAccountIdResult);
    }

    public String getToAccountIdResult() {
        waitForTransferResult();
        return getText(toAccountIdResult);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(showErrorContainer) || isDisplayed(serverErrorMessage) || isAmountEmptyErrorDisplayed() || isAmountInvalidErrorDisplayed();
    }

    public String getErrorMessage() {
        if (isDisplayed(serverErrorMessage)) {
            return getText(serverErrorMessage);
        }
        if (isAmountEmptyErrorDisplayed()) {
            return getAmountEmptyErrorText();
        }
        if (isAmountInvalidErrorDisplayed()) {
            return getAmountInvalidErrorText();
        }
        return "";
    }

    public boolean isAmountEmptyErrorDisplayed() {
        return isDisplayed(amountEmptyError);
    }

    public String getAmountEmptyErrorText() {
        return getText(amountEmptyError);
    }

    public boolean isAmountInvalidErrorDisplayed() {
        return isDisplayed(amountInvalidError);
    }

    public String getAmountInvalidErrorText() {
        return getText(amountInvalidError);
    }
}
