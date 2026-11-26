package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import pages.LoginPage;
import pages.BasePage;
import utils.DriverManager;
import utils.TestContext;

public class LogoutTest {

    @Test
    public void verifyUserCanLogOut() {
        WebDriver driver = DriverManager.getDriver();
        driver.get("https://parabank.parasoft.com/parabank/index.htm");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(TestContext.username, TestContext.password);

        try { Thread.sleep(3000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        BasePage basePage = new BasePage(driver);
        basePage.logout();

        System.out.println("URL after logout: " + driver.getCurrentUrl());
        Assert.assertTrue(driver.getCurrentUrl().contains("index.htm"),
                "Logout should return to the login page, but didn't");

        driver.quit();
    }
}