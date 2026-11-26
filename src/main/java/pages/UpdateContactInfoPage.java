package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class UpdateContactInfoPage extends BasePage {

    private By firstName = By.id("customer.firstName");
    private By lastName = By.id("customer.lastName");
    private By address = By.id("customer.address.street");
    private By city = By.id("customer.address.city");
    private By state = By.id("customer.address.state");
    private By zipCode = By.id("customer.address.zipCode");
    private By phone = By.id("customer.phoneNumber");
    private By updateButton = By.xpath("//input[@value='Update Profile']");

    public UpdateContactInfoPage(WebDriver driver) {
        super(driver);
    }

    public void updatePhoneNumber(String newPhone) {
        typeSlowly(phone, newPhone);
        pause(500);
        driver.findElement(updateButton).click();
        pause(2000);
    }
}
