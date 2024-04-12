package com.k11.automation.coreframework.listeners;

import com.k11.automation.coreframework.util.DriverManagerUtils;
import com.k11.automation.selenium.baseclasses.ApplicationProperties;
import com.k11.automation.coreframework.annotations.Headless;
import com.k11.automation.coreframework.annotations.URL;
import com.k11.automation.coreframework.reporter.ExtentTestManager;
import com.k11.automation.coreframework.testConfigUtils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.util.Strings;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import com.k11.automation.coreframework.testConfigUtils.*;
import com.k11.automation.coreframework.util.StringUtil;
import com.k11.automation.selenium.baseclasses.BaseTestPage;
import com.k11.automation.coreframework.testConfigUtils.SEConfigs;

public class SeleniumTestMethodListener implements IClassListener, IInvokedMethodListener, ISuiteListener {
    private static final Logger logger =LoggerFactory.getLogger(SeleniumTestMethodListener.class);
    private static final String TESTNG_BROWSER_PARAM_KEY =
            StringUtil.removeQuoteMark(SEConfigs.getConfigInstance().testngXmlBrowserParamKey());

    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        Method method =
                iInvokedMethod.getTestMethod().getConstructorOrMethod().getMethod();
      /*  DriverManager driverManager =
                DriverManagerFactory.getManager(browserName);*/
      if(method.isAnnotationPresent(Headless.class))
        System.setProperty("headless", "true");

        List<String> groups = TestGroupUtils.getMethodTestGroups(method);

        ExtentTestManager.createTest(
                method, ApplicationProperties.BROWSER.getStringVal(), AuthorUtils.getMethodAuthors(method),
                groups, iTestResult.getParameters());
        // open url if URL annotation had value
        Optional.ofNullable(method.getAnnotation(URL.class))
                .ifPresent(url -> openRemoteURL(url.value().trim()));
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {

    }

    @Override
    public void onStart(ISuite iSuite) {
    }

    @Override
    public void onFinish(ISuite iSuite) {

    }

    @Override
    public void onBeforeClass(ITestClass iTestClass) {
  
    }

    @Override
    public void onAfterClass(ITestClass iTestClass) {

    }

    private void openRemoteURL(String url) {
        if (Strings.isNotNullAndNotEmpty(url)) {
            WebDriver driver= DriverManagerUtils.launchBrowser();
            driver.get(url);
          Reporter.log(String.format("Open URL: %s", url));
        }
    }
}