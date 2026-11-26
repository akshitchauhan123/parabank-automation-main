package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentReportManager {

    private static final Logger logger = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();
    private static String reportFilePath;

    public static synchronized ExtentReports getExtentReports() {
        if (extent == null) {
            try {
                String reportDir = "test-output";
                Path dirPath = Paths.get(reportDir);
                if (!Files.exists(dirPath)) {
                    Files.createDirectories(dirPath);
                }

                reportFilePath = reportDir + File.separator + "ExtentReport.html";
                ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFilePath);

                sparkReporter.config().setDocumentTitle("ParaBank Automation Test Report");
                sparkReporter.config().setReportName("Enterprise ParaBank Test Execution Results");
                sparkReporter.config().setTheme(Theme.STANDARD);
                sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
                sparkReporter.config().setEncoding("UTF-8");

                extent = new ExtentReports();
                extent.attachReporter(sparkReporter);

                // System & Runtime Metadata
                extent.setSystemInfo("Application", "ParaBank Online Banking");
                extent.setSystemInfo("Environment", "Live Demo Server");
                extent.setSystemInfo("URL", ConfigReader.getProperty("url", "https://parabank.parasoft.com/parabank/index.htm"));
                extent.setSystemInfo("Browser", ConfigReader.getProperty("browser", "chrome").toUpperCase());
                extent.setSystemInfo("OS", System.getProperty("os.name"));
                extent.setSystemInfo("Java Version", System.getProperty("java.version"));
                extent.setSystemInfo("User", System.getProperty("user.name"));

                logger.info("ExtentReports initialized successfully. Destination: {}", reportFilePath);
            } catch (Exception e) {
                logger.error("Failed to initialize ExtentReports: {}", e.getMessage(), e);
            }
        }
        return extent;
    }

    public static synchronized ExtentTest createTest(String testName, String description) {
        ExtentTest test = getExtentReports().createTest(testName, description);
        testThreadLocal.set(test);
        logger.debug("Created ExtentTest: {} - {}", testName, description);
        return test;
    }

    public static ExtentTest getTest() {
        return testThreadLocal.get();
    }

    public static void removeTest() {
        testThreadLocal.remove();
    }

    public static synchronized void flushReports() {
        if (extent != null) {
            extent.flush();
            logger.info("ExtentReports flushed. Report available at: {}", reportFilePath);
        }
    }

    public static void attachScreenshot(String screenshotPath, String title) {
        ExtentTest test = getTest();
        if (test != null && screenshotPath != null) {
            try {
                test.fail(title, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                logger.info("Attached failure screenshot to ExtentReport: {}", screenshotPath);
            } catch (Exception e) {
                logger.warn("Failed to attach screenshot to ExtentReport: {}", e.getMessage());
            }
        }
    }

    public static String getReportFilePath() {
        return reportFilePath;
    }
}

