package tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import pages.LoginPage;
import pages.RequestLoanPage;
import utils.DriverManager;
import utils.TestContext;

public class RequestLoanTest {

    @Test
    public void verifyUserCanRequestLoan() {
        WebDriver driver = DriverManager.getDriver();
        driver.get("https://parabank.parasoft.com/parabank/index.htm");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(TestContext.username, TestContext.password);

        try { Thread.sleep(3000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        driver.get("https://parabank.parasoft.com/parabank/requestloan.htm");
        try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        RequestLoanPage loanPage = new RequestLoanPage(driver);
        String status = loanPage.requestLoan("5000", "500");

        System.out.println("Loan status: " + status);

        driver.quit();
    }
}