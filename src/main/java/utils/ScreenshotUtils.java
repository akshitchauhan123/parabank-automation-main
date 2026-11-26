package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {

    public static String takeScreenshot(WebDriver driver, String testName) {
        if (driver == null) {
            System.err.println("Driver instance is null; cannot take screenshot for " + testName);
            return null;
        }

        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File sourceFile = ts.getScreenshotAs(OutputType.FILE);

            String dirPath = ConfigReader.getProperty("screenshot.dir", "screenshots");
            Path destinationDir = Paths.get(dirPath);
            if (!Files.exists(destinationDir)) {
                Files.createDirectories(destinationDir);
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            String fileName = sanitizeFileName(testName) + "_" + timestamp + ".png";
            Path destinationPath = destinationDir.resolve(fileName);

            Files.copy(sourceFile.toPath(), destinationPath);
            return destinationPath.toAbsolutePath().toString();
        } catch (IOException e) {
            System.err.println("Failed to save screenshot for " + testName + ": " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error taking screenshot for " + testName + ": " + e.getMessage());
            return null;
        }
    }

    private static String sanitizeFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}

