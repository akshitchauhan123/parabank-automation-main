package utils;

import base.DriverFactory;
import org.openqa.selenium.WebDriver;

public class DriverManager {

    public static WebDriver getDriver() {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) {
            driver = DriverFactory.initDriver();
        }
        return driver;
    }
}

