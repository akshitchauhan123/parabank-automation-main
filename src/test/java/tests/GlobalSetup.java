package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;

import pages.OpenNewAccountPage;
import pages.RegistrationPage;
import utils.DriverManager;
import utils.TestContext;
import utils.TestDataGenerator;

public class GlobalSetup {

    @BeforeSuite
    public void setupFreshTestUser() {
        WebDriver driver = DriverManager.getDriver();
        driver.get("https://parabank.parasoft.com/parabank/register.htm");

        RegistrationPage registrationPage = new RegistrationPage(driver);
        String uname = TestDataGenerator.generateUniqueUsername();
        registrationPage.registerNewUser(uname, TestContext.password);
        TestContext.username = uname;

        try { Thread.sleep(3000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        // registration confirms on register.htm itself — navigate to Accounts Overview
        // to actually reach the page that has the account table
        driver.get("https://parabank.parasoft.com/parabank/overview.htm");
        try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        String primaryAccount = driver.findElement(By.cssSelector("#accountTable tbody tr td a")).getText();
        TestContext.primaryAccountNumber = primaryAccount;

        driver.get("https://parabank.parasoft.com/parabank/openaccount.htm");
        OpenNewAccountPage openAccountPage = new OpenNewAccountPage(driver);
        String secondaryAccount = openAccountPage.openNewSavingsAccount();
        TestContext.secondaryAccountNumber = secondaryAccount;

        System.out.println("Fresh test user ready: " + uname);
        System.out.println("Primary account: " + primaryAccount + " | Secondary account: " + secondaryAccount);

        driver.quit();
    }
}