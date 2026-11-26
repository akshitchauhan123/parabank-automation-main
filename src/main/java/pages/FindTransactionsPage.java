package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

public class FindTransactionsPage extends BasePage {

    private final By pageHeading = By.xpath("//h1[contains(text(),'Find Transactions')]");
    private final By formContainer = By.id("transactionForm");
    private final By accountSelect = By.id("accountId");
    private final By accountOptions = By.cssSelector("#accountId option");

    // Search input fields and action buttons
    private final By transactionIdInput = By.id("transactionId");
    private final By findByIdButton = By.id("findById");
    private final By transactionDateInput = By.id("transactionDate");
    private final By findByDateButton = By.id("findByDate");
    private final By fromDateInput = By.id("fromDate");
    private final By toDateInput = By.id("toDate");
    private final By findByDateRangeButton = By.id("findByDateRange");
    private final By amountInput = By.id("amount");
    private final By findByAmountButton = By.id("findByAmount");

    // Result table
    private final By transactionTable = By.id("transactionTable");
    private final By transactionRows = By.cssSelector("#transactionTable tbody tr");
    private final By transactionFirstRowLink = By.cssSelector("#transactionTable tbody tr td a");

    // Validation errors
    private final By transactionIdError = By.id("transactionIdError");
    private final By transactionDateError = By.id("transactionDateError");

    public FindTransactionsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isFindTransactionsPageDisplayed() {
        return isDisplayed(pageHeading) || driver.getCurrentUrl().contains("findtrans.htm");
    }

    public String getPageHeading() {
        return getText(pageHeading);
    }

    public boolean isFormDisplayed() {
        return isDisplayed(formContainer);
    }

    public boolean isAccountSelectDisplayed() {
        return isDisplayed(accountSelect);
    }

    public void waitForAccountsToLoad() {
        WaitUtils.getWait(driver, 15).until(ExpectedConditions.numberOfElementsToBeMoreThan(accountOptions, 0));
    }

    public List<String> getAccountOptions() {
        waitForAccountsToLoad();
        Select select = new Select(driver.findElement(accountSelect));
        List<String> options = new ArrayList<>();
        for (WebElement option : select.getOptions()) {
            options.add(option.getText().trim());
        }
        return options;
    }

    public String getSelectedAccountId() {
        waitForAccountsToLoad();
        Select select = new Select(driver.findElement(accountSelect));
        return select.getFirstSelectedOption().getText().trim();
    }

    public void selectAccount(String accountId) {
        waitForAccountsToLoad();
        Select select = new Select(WaitUtils.waitForElementClickable(driver, accountSelect));
        select.selectByVisibleText(accountId);
    }

    public void searchByTransactionId(String transactionId) {
        typeSlowly(transactionIdInput, transactionId);
        click(findByIdButton);
    }

    public void searchByDate(String dateStr) {
        typeSlowly(transactionDateInput, dateStr);
        click(findByDateButton);
    }

    public void searchByDateRange(String fromDate, String toDate) {
        typeSlowly(fromDateInput, fromDate);
        typeSlowly(toDateInput, toDate);
        click(findByDateRangeButton);
    }

    public void searchByAmount(String amount) {
        typeSlowly(amountInput, amount);
        click(findByAmountButton);
    }

    public void waitForResultsToLoad() {
        WaitUtils.waitForElementVisible(driver, transactionTable, 15);
    }

    public boolean isTransactionTableDisplayed() {
        return isDisplayed(transactionTable);
    }

    public int getTransactionCount() {
        waitForResultsToLoad();
        List<WebElement> rows = driver.findElements(transactionRows);
        return rows.size();
    }

    public String getFirstTransactionDescription() {
        waitForResultsToLoad();
        List<WebElement> rows = driver.findElements(transactionRows);
        if (!rows.isEmpty()) {
            List<WebElement> cells = rows.get(0).findElements(By.tagName("td"));
            if (cells.size() > 1) {
                return cells.get(1).getText().trim();
            }
        }
        return null;
    }

    public String getFirstTransactionDate() {
        waitForResultsToLoad();
        List<WebElement> rows = driver.findElements(transactionRows);
        if (!rows.isEmpty()) {
            List<WebElement> cells = rows.get(0).findElements(By.tagName("td"));
            if (!cells.isEmpty()) {
                return cells.get(0).getText().trim();
            }
        }
        return null;
    }

    public String getTransactionIdErrorText() {
        return getText(transactionIdError);
    }

    public String getTransactionDateErrorText() {
        return getText(transactionDateError);
    }
}
