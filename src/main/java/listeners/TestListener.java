package listeners;

import base.DriverFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ExtentReportManager;
import utils.ScreenshotUtils;

public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("========================================================================");
        logger.info("TEST SUITE STARTED: {}", context.getName());
        logger.info("========================================================================");
        ExtentReportManager.getExtentReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        String className = result.getTestClass().getRealClass().getSimpleName();

        String displayName = (description != null && !description.isEmpty())
                ? description
                : className + " - " + methodName;

        logger.info("------------------------------------------------------------------------");
        logger.info("STARTING TEST: {} [{}]", methodName, displayName);
        logger.info("------------------------------------------------------------------------");

        ExtentTest test = ExtentReportManager.createTest(methodName, displayName);
        test.assignCategory(className);
        test.log(Status.INFO, "Execution initiated for method: " + methodName);
        if (description != null && !description.isEmpty()) {
            test.log(Status.INFO, "Test Objective: " + description);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        long duration = result.getEndMillis() - result.getStartMillis();
        logger.info("TEST PASSED: {} (Duration: {} ms)", methodName, duration);

        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.PASS, MarkupHelper.createLabel("TEST PASSED: " + methodName, ExtentColor.GREEN));
            test.log(Status.INFO, "Execution time: " + duration + " ms");
        }
        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();
        logger.error("TEST FAILED: {} - Exception: {}", methodName, (throwable != null ? throwable.getMessage() : "Unknown"), throwable);

        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.FAIL, MarkupHelper.createLabel("TEST FAILED: " + methodName, ExtentColor.RED));
            if (throwable != null) {
                test.fail(throwable);
            }

            WebDriver driver = DriverFactory.getDriver();
            if (driver != null) {
                String screenshotPath = ScreenshotUtils.takeScreenshot(driver, methodName);
                if (screenshotPath != null) {
                    logger.info("Failure screenshot captured at: {}", screenshotPath);
                    ExtentReportManager.attachScreenshot(screenshotPath, "Failure Screenshot for " + methodName);
                } else {
                    logger.warn("Could not capture screenshot for failed test: {}", methodName);
                }
            }
        }
        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        logger.warn("TEST SKIPPED: {}", methodName);

        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.SKIP, MarkupHelper.createLabel("TEST SKIPPED: " + methodName, ExtentColor.YELLOW));
            if (result.getThrowable() != null) {
                test.skip(result.getThrowable());
            }
        }
        ExtentReportManager.removeTest();
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("========================================================================");
        logger.info("TEST SUITE FINISHED: {}", context.getName());
        logger.info("Total: {}, Passed: {}, Failed: {}, Skipped: {}",
                context.getAllTestMethods().length,
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
        logger.info("========================================================================");

        ExtentReportManager.flushReports();
    }
}

