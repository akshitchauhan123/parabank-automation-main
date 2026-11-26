package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

public class BasePage {

    protected WebDriver driver;

    // Navigation Menu Locators
    private final By openNewAccountLink = By.linkText("Open New Account");
    private final By accountsOverviewLink = By.linkText("Accounts Overview");
    private final By transferFundsLink = By.linkText("Transfer Funds");
    private final By billPayLink = By.linkText("Bill Pay");
    private final By findTransactionsLink = By.linkText("Find Transactions");
    private final By updateContactInfoLink = By.linkText("Update Contact Info");
    private final By requestLoanLink = By.linkText("Request Loan");
    private final By logoutLink = By.linkText("Log Out");

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    protected void click(By locator) {
        WaitUtils.waitForElementClickable(driver, locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = WaitUtils.waitForElementVisible(driver, locator);
        element.clear();
        if (text != null) {
            element.sendKeys(text);
        }
    }

    protected void typeSlowly(By locator, String text) {
        WebElement element = WaitUtils.waitForElementVisible(driver, locator);
        element.clear();
        if (text != null) {
            element.sendKeys(text);
        }
        pause(100);
    }

    protected String getText(By locator) {
        return WaitUtils.waitForElementVisible(driver, locator).getText().trim();
    }

    protected boolean isDisplayed(By locator) {
        return WaitUtils.isElementDisplayed(driver, locator);
    }

    // Navigation Actions
    public void clickOpenNewAccount() {
        click(openNewAccountLink);
    }

    public void clickAccountsOverview() {
        click(accountsOverviewLink);
    }

    public void clickTransferFunds() {
        click(transferFundsLink);
    }

    public void clickBillPay() {
        click(billPayLink);
    }

    public void clickFindTransactions() {
        click(findTransactionsLink);
    }

    public void clickUpdateContactInfo() {
        click(updateContactInfoLink);
    }

    public void clickRequestLoan() {
        click(requestLoanLink);
    }

    public void logout() {
        click(logoutLink);
    }

    public boolean isLogoutLinkDisplayed() {
        return isDisplayed(logoutLink);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
