package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import utils.ConfigReader;

import java.time.Duration;
import java.util.Collections;

public class DriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public static WebDriver initDriver() {
        String browser = ConfigReader.getProperty("browser", "chrome");
        return initDriver(browser);
    }

    public static WebDriver initDriver(String browser) {
        WebDriver driver;
        boolean headless = ConfigReader.getBooleanProperty("headless", false);

        if (browser == null) {
            browser = "chrome";
        }

        logger.info("Initializing WebDriver for browser: {} (headless={})", browser, headless);

        switch (browser.toLowerCase().trim()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                }
                driver = new EdgeDriver(edgeOptions);
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
                chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
                chromeOptions.setExperimentalOption("useAutomationExtension", false);
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--window-size=1920,1080");
                }
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        driver.manage().window().maximize();

        int implicitWait = ConfigReader.getIntProperty("implicit.wait", 0);
        if (implicitWait > 0) {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        }

        driverThreadLocal.set(driver);
        logger.info("WebDriver instance successfully initialized and bound to thread: {}", Thread.currentThread().getId());
        return driver;
    }

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver closed successfully for thread: {}", Thread.currentThread().getId());
            } catch (Exception e) {
                logger.warn("Warning while closing browser: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
}
