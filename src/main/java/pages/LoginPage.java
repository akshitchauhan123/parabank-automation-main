package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By usernameField = By.name("username");
    private final By passwordField = By.name("password");
    private final By loginButton = By.xpath("//input[@value='Log In']");
    private final By errorMessage = By.cssSelector("p.error");
    private final By errorTitle = By.cssSelector("#rightPanel h1.title");
    private final By loginPanel = By.id("loginPanel");
    private final By registerLink = By.linkText("Register");
    private final By logoutLink = By.linkText("Log Out");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void enterUsername(String username) {
        type(usernameField, username);
    }

    public void enterPassword(String password) {
        type(passwordField, password);
    }

    public void clickLogin() {
        click(loginButton);
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }

    public String getErrorTitle() {
        return getText(errorTitle);
    }

    public void clickLogout() {
        logout();
    }

    public boolean isLogoutDisplayed() {
        return isDisplayed(logoutLink);
    }

    public boolean isLoginPanelDisplayed() {
        return isDisplayed(loginPanel);
    }

    public void clickRegister() {
        click(registerLink);
    }
}

