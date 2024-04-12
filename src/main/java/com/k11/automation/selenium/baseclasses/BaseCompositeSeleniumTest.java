package com.k11.automation.selenium.baseclasses;

import com.k11.automation.webservices.baseclasses.BaseWebServiceTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import java.lang.reflect.Method;

/**
 * Class used to integrate rest assured and selenium tests.This class will first run BaseSeleniumTest @BeforeTest, @BeforeMethod, etc.. methods, then BaseCompositeSeleniumTest methods.
 */
public class BaseCompositeSeleniumTest extends BaseSeleniumTest {

    /**
     * Method to run before all tests.
     * @param webServiceLog true to enable logging for web services.
     */
    @Parameters({
            "webServiceLog"})
    @BeforeTest(alwaysRun = true)
    protected void beforeCompositeTest(@Optional String webServiceLog) {
        BaseWebServiceTest webServiceTest = new BaseWebServiceTest();
        webServiceTest.testSetup(webServiceLog);
    }

    /**
     * Method executed before Selenium and Rest Assured methods.
     *
     * @param method The method executed
     * @param webServiceUri sets the web service base uri
     * @param webServicePort sets the web service port
     * @param webServicePath sets the web service path
     */
    @Parameters({
            "webServiceUri",
            "webServicePort",
            "webServicePath"})
    @BeforeMethod
    protected void beforeCompositeMethod(Method method,
                             @Optional String webServiceUri,
                             @Optional String webServicePort,
                             @Optional String webServicePath) {
        BaseWebServiceTest webServiceTest = new BaseWebServiceTest();
        webServiceTest.beforeMethod(method, webServiceUri, webServicePort, webServicePath);
    }
}
