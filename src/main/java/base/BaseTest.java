package base;

import listeners.TestListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import pages.AccountsOverviewPage;
import pages.LoginPage;
import pages.OpenNewAccountPage;
import pages.RegisterPage;
import pages.TransferFundsPage;
import utils.ConfigReader;
import utils.TestContext;
import utils.TestDataGenerator;

@Listeners(TestListener.class)
public class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void globalSuiteSetup() {
        if (TestContext.username == null) {
            logger.info("Initializing global test suite customer provisioning...");
            WebDriver initDriver = DriverFactory.initDriver();
            try {
                String appUrl = ConfigReader.getProperty("url", "https://parabank.parasoft.com/parabank/index.htm");
                logger.info("Navigating to application URL: {}", appUrl);
                initDriver.get(appUrl);
                LoginPage loginPage = new LoginPage(initDriver);
                loginPage.clickRegister();

                RegisterPage registerPage = new RegisterPage(initDriver);
                String uniqueUser = TestDataGenerator.generateUniqueUsername("ste");
                String password = ConfigReader.getProperty("default.password", "Test1234");
                logger.info("Registering suite customer with handle: {}", uniqueUser);
                registerPage.registerNewUser(uniqueUser, password);
                registerPage.waitForRegistrationConfirmation();

                TestContext.username = uniqueUser;
                TestContext.password = password;

                // Retrieve primary account
                registerPage.clickAccountsOverview();
                AccountsOverviewPage overviewPage = new AccountsOverviewPage(initDriver);
                overviewPage.waitForAccountDataToLoad();
                TestContext.primaryAccountNumber = overviewPage.getFirstAccountNumber();
                logger.info("Retrieved primary customer account: {}", TestContext.primaryAccountNumber);

                // Open a secondary savings account
                overviewPage.clickOpenNewAccount();
                OpenNewAccountPage openAccountPage = new OpenNewAccountPage(initDriver);
                openAccountPage.waitForFormToLoad();
                openAccountPage.selectAccountType("SAVINGS");
                openAccountPage.clickOpenAccountButton();
                openAccountPage.waitForAccountOpenedConfirmation();
                TestContext.secondaryAccountNumber = openAccountPage.getNewAccountId();
                logger.info("Provisioned secondary savings account: {}", TestContext.secondaryAccountNumber);

                // Perform an initial transfer so transactions are created
                openAccountPage.clickTransferFunds();
                TransferFundsPage transferPage = new TransferFundsPage(initDriver);
                transferPage.waitForAccountsToLoad();
                transferPage.transferFunds("50.00", TestContext.primaryAccountNumber, TestContext.secondaryAccountNumber);
                transferPage.waitForTransferResult();
                logger.info("Executed seed transfer of $50.00 from {} to {}", TestContext.primaryAccountNumber, TestContext.secondaryAccountNumber);

                transferPage.logout();
                logger.info("Global suite setup and seed provisioning completed successfully.");
            } catch (Exception e) {
                logger.warn("Global setup user provisioning notice: {}", e.getMessage());
            } finally {
                DriverFactory.quitDriver();
            }
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.initDriver();
        String url = ConfigReader.getProperty("url", "https://parabank.parasoft.com/parabank/index.htm");
        logger.info("Navigating to application: {}", url);
        driver.get(url);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result != null) {
            logger.info("Completed execution for test method: {} with status: {}",
                    result.getMethod().getMethodName(),
                    result.getStatus() == ITestResult.SUCCESS ? "SUCCESS" : (result.getStatus() == ITestResult.FAILURE ? "FAILURE" : "SKIPPED"));
        }
        DriverFactory.quitDriver();
        try {
            Thread.sleep(300);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    public WebDriver getDriver() {
        return DriverFactory.getDriver();
    }
}
