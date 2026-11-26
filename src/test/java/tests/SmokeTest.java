package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import utils.DriverManager;

public class SmokeTest {
	@Test
	public void verifyParaBankHomePageLoads() {
		WebDriver driver = DriverManager.getDriver();
		driver.get("https://parabank.parasoft.com/parabank/index.htm");
		
		String title = driver.getTitle();
		Assert.assertTrue(title.contains("ParaBank"), "Title did not match, found: " +title);
		
		driver.quit();
	}

}
