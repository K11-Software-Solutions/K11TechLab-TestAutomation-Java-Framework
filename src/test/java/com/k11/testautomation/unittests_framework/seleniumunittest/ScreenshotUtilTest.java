package com.k11.testautomation.unittests_framework.seleniumunittest;

import com.k11.automation.coreframework.util.ScreenShotUtil;
import com.k11.automation.selenium.baseclasses.ApplicationProperties;
import com.k11.automation.selenium.baseclasses.BaseSeleniumTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.File;

/**
 * Screen shot utility Test.
 */
public class ScreenshotUtilTest {

    /**
     * The site url.
     */
    private static final String siteAutomationUrl = ApplicationProperties.APPLICATION_BASE_URL.getStringVal();

    /**
     * Test the Screen shot method name generator.
     */
    @Test
    public void screenShotFileNameTest() {
        String screenShotName = ScreenShotUtil.generateScreenshotFileName("screenShotPathTest");
        Assert.assertTrue(screenShotName.contains("screenShotPathTest"));
    }

    /**
     * Create a screen shot test.
     */
    @Test
    public void createScreenShotTest() {
        BaseSeleniumTest testCase = new BaseSeleniumTest();
        String screenShotName = ScreenShotUtil.generateScreenshotFileName("screenShotPathTest");
        testCase.getDriver().get(siteAutomationUrl);
        String screenShotPath = ScreenShotUtil.createScreenshot(testCase.getDriver(), screenShotName);
        testCase.quitAndRemoveDriver();
        File ss = new File(screenShotPath);
        Assert.assertTrue(ss.exists() && ss.isFile());
    }

    /**
     * Create a full screen shot test.
     */
    @Test
    public void createFullScreenShotTest() {
        BaseSeleniumTest testCase = new BaseSeleniumTest();
        String screenShotName = ScreenShotUtil.generateScreenshotFileName("screenShotPathTest");
        testCase.getDriver().get(siteAutomationUrl);
        String screenShotPath = ScreenShotUtil.captureFullPageScreenshot(testCase.getDriver(), screenShotName);
        testCase.quitAndRemoveDriver();
        File ss = new File(screenShotPath);
        Assert.assertTrue(ss.exists() && ss.isFile());
    }


}
