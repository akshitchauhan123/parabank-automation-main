package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class WaitUtils {

    private static final int DEFAULT_TIMEOUT = ConfigReader.getIntProperty("explicit.wait", 15);

    public static WebDriverWait getWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public static WebDriverWait getWait(WebDriver driver, int timeoutInSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    public static WebElement waitForElementVisible(WebDriver driver, By locator) {
        return getWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForElementVisible(WebDriver driver, By locator, int timeoutInSeconds) {
        return getWait(driver, timeoutInSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForElementClickable(WebDriver driver, By locator) {
        return getWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForElementClickable(WebDriver driver, WebElement element) {
        return getWait(driver).until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitForElementPresence(WebDriver driver, By locator) {
        return getWait(driver).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static List<WebElement> waitForElementsVisible(WebDriver driver, By locator) {
        return getWait(driver).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static boolean waitForUrlContains(WebDriver driver, String fraction) {
        return getWait(driver).until(ExpectedConditions.urlContains(fraction));
    }

    public static boolean waitForTitleContains(WebDriver driver, String title) {
        return getWait(driver).until(ExpectedConditions.titleContains(title));
    }

    public static boolean waitForTextToBePresentInElement(WebDriver driver, By locator, String text) {
        return getWait(driver).until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public static boolean isElementDisplayed(WebDriver driver, By locator) {
        return isElementDisplayed(driver, locator, 3);
    }

    public static boolean isElementDisplayed(WebDriver driver, By locator, int timeoutInSeconds) {
        try {
            return waitForElementVisible(driver, locator, timeoutInSeconds).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

