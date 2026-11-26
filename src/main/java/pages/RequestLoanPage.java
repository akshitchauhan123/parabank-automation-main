package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RequestLoanPage extends BasePage {

    private By loanAmount = By.id("amount");
    private By downPayment = By.id("downPayment");
    private By fromAccountDropdown = By.id("fromAccountId");
    private By applyButton = By.xpath("//input[@value='Apply Now']");
    private By loanStatus = By.id("loanStatus");

    public RequestLoanPage(WebDriver driver) {
        super(driver);
    }

    public String requestLoan(String amount, String downPaymentAmount) {
        typeSlowly(loanAmount, amount);
        typeSlowly(downPayment, downPaymentAmount);
        pause(500);
        driver.findElement(applyButton).click();
        pause(3000);

        return driver.findElement(loanStatus).getText();
    }
}