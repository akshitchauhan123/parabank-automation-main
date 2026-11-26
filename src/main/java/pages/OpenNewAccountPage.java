package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

public class OpenNewAccountPage extends BasePage {

    private final By pageHeading = By.xpath("//h1[contains(text(),'Open New Account')]");
    private final By formContainer = By.id("openAccountForm");
    private final By accountTypeDropdown = By.id("type");
    private final By accountTypeOptions = By.cssSelector("#type option");
    private final By fromAccountDropdown = By.id("fromAccountId");
    private final By fromAccountOptions = By.cssSelector("#fromAccountId option");
    private final By openAccountButton = By.xpath("//input[@value='Open New Account']");

    // Result elements
    private final By resultContainer = By.id("openAccountResult");
    private final By resultHeading = By.xpath("//div[@id='openAccountResult']/h1[contains(text(),'Account Opened!')]");
    private final By confirmationMessage = By.xpath("//div[@id='openAccountResult']/p[contains(text(),'Congratulations')]");
    private final By newAccountIdLink = By.id("newAccountId");

    public OpenNewAccountPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOpenAccountPageDisplayed() {
        return isDisplayed(pageHeading) || driver.getCurrentUrl().contains("openaccount.htm");
    }

    public String getPageHeading() {
        return getText(pageHeading);
    }

    public boolean isFormDisplayed() {
        return isDisplayed(formContainer);
    }

    public boolean isAccountTypeDropdownDisplayed() {
        return isDisplayed(accountTypeDropdown);
    }

    public boolean isFromAccountDropdownDisplayed() {
        return isDisplayed(fromAccountDropdown);
    }

    public boolean isOpenAccountButtonDisplayed() {
        return isDisplayed(openAccountButton);
    }

    public void waitForFormToLoad() {
        WaitUtils.getWait(driver, 15).until(ExpectedConditions.numberOfElementsToBeMoreThan(fromAccountOptions, 0));
    }

    public void selectAccountType(String type) {
        waitForFormToLoad();
        Select select = new Select(WaitUtils.waitForElementClickable(driver, accountTypeDropdown));
        select.selectByVisibleText(type);
    }

    public List<String> getAccountTypeOptions() {
        waitForFormToLoad();
        Select select = new Select(driver.findElement(accountTypeDropdown));
        List<String> options = new ArrayList<>();
        for (WebElement option : select.getOptions()) {
            options.add(option.getText().trim());
        }
        return options;
    }

    public void selectFromAccount(String accountId) {
        waitForFormToLoad();
        Select select = new Select(WaitUtils.waitForElementClickable(driver, fromAccountDropdown));
        select.selectByVisibleText(accountId);
    }

    public void clickOpenAccountButton() {
        click(openAccountButton);
    }

    public void waitForAccountOpenedConfirmation() {
        WaitUtils.waitForElementVisible(driver, resultHeading, 15);
        WaitUtils.waitForElementVisible(driver, newAccountIdLink, 15);
    }

    public boolean isAccountOpenedConfirmationDisplayed() {
        return isDisplayed(resultHeading);
    }

    public String getConfirmationHeading() {
        waitForAccountOpenedConfirmation();
        return getText(resultHeading);
    }

    public String getConfirmationMessage() {
        waitForAccountOpenedConfirmation();
        return getText(confirmationMessage);
    }

    public String getNewAccountId() {
        waitForAccountOpenedConfirmation();
        return getText(newAccountIdLink);
    }

    public String openNewAccount(String type) {
        waitForFormToLoad();
        selectAccountType(type);
        clickOpenAccountButton();
        waitForAccountOpenedConfirmation();
        return getNewAccountId();
    }

    public String openNewSavingsAccount() {
        return openNewAccount("SAVINGS");
    }
}
