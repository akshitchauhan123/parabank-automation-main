package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.WaitUtils;

public class RegisterPage extends BasePage {

    // Registration Form Inputs
    private final By firstNameInput = By.id("customer.firstName");
    private final By lastNameInput = By.id("customer.lastName");
    private final By addressInput = By.id("customer.address.street");
    private final By cityInput = By.id("customer.address.city");
    private final By stateInput = By.id("customer.address.state");
    private final By zipCodeInput = By.id("customer.address.zipCode");
    private final By phoneInput = By.id("customer.phoneNumber");
    private final By ssnInput = By.id("customer.ssn");
    private final By usernameInput = By.id("customer.username");
    private final By passwordInput = By.id("customer.password");
    private final By confirmPasswordInput = By.id("repeatedPassword");
    private final By registerButton = By.xpath("//input[@value='Register']");

    // Validation Error Message Spans
    private final By firstNameError = By.id("customer.firstName.errors");
    private final By lastNameError = By.id("customer.lastName.errors");
    private final By addressError = By.id("customer.address.street.errors");
    private final By cityError = By.id("customer.address.city.errors");
    private final By stateError = By.id("customer.address.state.errors");
    private final By zipCodeError = By.id("customer.address.zipCode.errors");
    private final By ssnError = By.id("customer.ssn.errors");
    private final By usernameError = By.id("customer.username.errors");
    private final By passwordError = By.id("customer.password.errors");
    private final By confirmPasswordError = By.id("repeatedPassword.errors");

    // Confirmation Elements
    private final By confirmationHeading = By.cssSelector("#rightPanel h1.title");
    private final By confirmationMessage = By.cssSelector("#rightPanel p");

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public void enterFirstName(String firstName) {
        typeSlowly(firstNameInput, firstName);
    }

    public void enterLastName(String lastName) {
        typeSlowly(lastNameInput, lastName);
    }

    public void enterAddress(String address) {
        typeSlowly(addressInput, address);
    }

    public void enterCity(String city) {
        typeSlowly(cityInput, city);
    }

    public void enterState(String state) {
        typeSlowly(stateInput, state);
    }

    public void enterZipCode(String zipCode) {
        typeSlowly(zipCodeInput, zipCode);
    }

    public void enterPhone(String phone) {
        typeSlowly(phoneInput, phone);
    }

    public void enterSsn(String ssn) {
        typeSlowly(ssnInput, ssn);
    }

    public void enterUsername(String username) {
        typeSlowly(usernameInput, username);
    }

    public void enterPassword(String password) {
        typeSlowly(passwordInput, password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        typeSlowly(confirmPasswordInput, confirmPassword);
    }

    public void clickRegisterButton() {
        pause(300);
        click(registerButton);
    }

    public void fillRegistrationForm(String firstName, String lastName, String address, String city,
                                     String state, String zipCode, String phone, String ssn,
                                     String username, String password, String confirmPassword) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterAddress(address);
        enterCity(city);
        enterState(state);
        enterZipCode(zipCode);
        enterPhone(phone);
        enterSsn(ssn);
        enterUsername(username);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
    }

    public void registerNewUser(String username, String password) {
        fillRegistrationForm(
                "Jane",
                "Doe",
                "123 Quality Street",
                "Metro City",
                "NY",
                "10001",
                "555-123-4567",
                "987-65-4321",
                username,
                password,
                password
        );
        clickRegisterButton();
    }

    public boolean isRegistrationSuccessful() {
        try {
            return WaitUtils.waitForTitleContains(driver, "Customer Created");
        } catch (Exception e) {
            try {
                return WaitUtils.waitForTextToBePresentInElement(driver, confirmationHeading, "Welcome");
            } catch (Exception ex) {
                return false;
            }
        }
    }

    public void waitForRegistrationConfirmation() {
        try {
            WaitUtils.waitForTitleContains(driver, "Customer Created");
        } catch (Exception e) {
            WaitUtils.getWait(driver, 15).until(ExpectedConditions.textToBePresentInElementLocated(confirmationHeading, "Welcome"));
        }
    }

    public String getConfirmationHeading() {
        waitForRegistrationConfirmation();
        return getText(confirmationHeading);
    }

    public String getConfirmationMessage() {
        waitForRegistrationConfirmation();
        return getText(confirmationMessage);
    }

    public String getFirstNameError() {
        return getText(firstNameError);
    }

    public String getLastNameError() {
        return getText(lastNameError);
    }

    public String getAddressError() {
        return getText(addressError);
    }

    public String getCityError() {
        return getText(cityError);
    }

    public String getStateError() {
        return getText(stateError);
    }

    public String getZipCodeError() {
        return getText(zipCodeError);
    }

    public String getSsnError() {
        return getText(ssnError);
    }

    public String getUsernameError() {
        return getText(usernameError);
    }

    public String getPasswordError() {
        return getText(passwordError);
    }

    public String getConfirmPasswordError() {
        return getText(confirmPasswordError);
    }

    public boolean areMandatoryFieldErrorsDisplayed() {
        return isDisplayed(firstNameError)
                && isDisplayed(lastNameError)
                && isDisplayed(addressError)
                && isDisplayed(cityError)
                && isDisplayed(stateError)
                && isDisplayed(zipCodeError)
                && isDisplayed(ssnError)
                && isDisplayed(usernameError)
                && isDisplayed(passwordError)
                && isDisplayed(confirmPasswordError);
    }
}
