package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import pages.LoginPage;
import pages.UpdateContactInfoPage;
import utils.DriverManager;
import utils.TestContext;

public class UpdateContactInfoTest {

    @Test
    public void verifyUserCanUpdatePhoneNumber() {
        WebDriver driver = DriverManager.getDriver();
        driver.get("https://parabank.parasoft.com/parabank/index.htm");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(TestContext.username, TestContext.password);

        try { Thread.sleep(3000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        driver.get("https://parabank.parasoft.com/parabank/updateprofile.htm");
        try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        UpdateContactInfoPage updatePage = new UpdateContactInfoPage(driver);
        updatePage.updatePhoneNumber("9998887777");

        String pageText = driver.findElement(By.tagName("body")).getText();
        System.out.println("Page text after profile update: " + pageText.substring(0, Math.min(300, pageText.length())));

        driver.quit();
    }
}
