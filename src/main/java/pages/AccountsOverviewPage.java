package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

public class AccountsOverviewPage extends BasePage {

    private final By pageHeading = By.xpath("//h1[contains(text(),'Accounts Overview')]");
    private final By accountTable = By.id("accountTable");
    private final By accountRowLinks = By.cssSelector("#accountTable tbody tr td a");
    private final By accountRows = By.cssSelector("#accountTable tbody tr");

    public AccountsOverviewPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOverviewPageDisplayed() {
        return isDisplayed(pageHeading) || driver.getCurrentUrl().contains("overview.htm");
    }

    public String getPageHeading() {
        return getText(pageHeading);
    }

    public boolean isAccountTableDisplayed() {
        return isDisplayed(accountTable);
    }

    public void waitForAccountDataToLoad() {
        WaitUtils.waitForElementVisible(driver, accountTable);
        WaitUtils.waitForElementsVisible(driver, accountRowLinks);
    }

    public List<String> getAccountNumbers() {
        waitForAccountDataToLoad();
        List<WebElement> links = driver.findElements(accountRowLinks);
        List<String> accountNumbers = new ArrayList<>();
        for (WebElement link : links) {
            String text = link.getText().trim();
            if (!text.isEmpty()) {
                accountNumbers.add(text);
            }
        }
        return accountNumbers;
    }

    public String getFirstAccountNumber() {
        List<String> accounts = getAccountNumbers();
        return accounts.isEmpty() ? null : accounts.get(0);
    }

    public int getAccountCount() {
        return getAccountNumbers().size();
    }
}

