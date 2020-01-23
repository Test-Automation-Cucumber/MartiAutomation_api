package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import io.restassured.RestAssured;

import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;

public class TestBase {
	static ExtentReports extent;
	ExtentHtmlReporter reporter;
	ExtentTest logger;
	protected String API_ROOT;
	protected String ENDPOINT;

	protected static Configuration configurationGet = Configuration.getInstance();

	@BeforeSuite
	public void beforeSuite() {
		extent = new ExtentReports();
		// reporter = new ExtentHtmlReporter(System.getProperty("user.dir") +
		// "\\Reports\\AutomationReport\\"+ String.valueOf(new
		// File(".\\Reports\\AutomationReport\\").list().length+1)+".html");
		reporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "\\reports\\extentReport_api.html");
		System.out.println(reporter.getFilePath());
		extent.attachReporter(reporter);
	}

	@BeforeClass
	public void beforeClass() {
	}

	@BeforeMethod
	public void beforeMethod(Method method) throws Exception {
		try {
			////////////////////Parameters by Jenkins
			System.setProperty("apiRoot", "https://jsonplaceholder.typicode.com");
			System.setProperty("endPoint", "posts");

			API_ROOT = System.getProperty("apiRoot");
			ENDPOINT = System.getProperty("endPoint");
			RestAssured.baseURI = this.API_ROOT;
			logger = extent.createTest(method.getName());
			logger.info("Restfull API test has started.");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	@AfterMethod
	public void tearDown(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {
          	System.out.println("!!!!! ERROR === " + result.getThrowable().getMessage());
			logger.fail(result.getThrowable().getMessage());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			logger.info("Test succeeded.");
		} else if(result.getStatus()==ITestResult.SKIP){
	           System.out.println(result.getThrowable().getMessage());
	           System.out.println("========================================================================");
	           System.out.println("*******************************************");
	           System.out.println(">>>>>>>>>>>>>>>>>  Test will be run again..  <<<<<<<<<<<<<<<<<<<<<<");
	           System.out.println("*******************************************");
	           System.out.println("========================================================================");
	           
        }
	}

	@AfterClass()
	public void afterClass() {
	}

	@AfterSuite()
	public void afterSuite() {
		extent.flush();
	}
}
