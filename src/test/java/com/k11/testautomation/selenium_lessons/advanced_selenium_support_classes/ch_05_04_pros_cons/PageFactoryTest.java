package com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_05_04_pros_cons;

import com.k11.automation.coreframework.util.DriverManagerUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

public class PageFactoryTest {

    WebDriver driver;

    @BeforeEach
    public void createDriver(){
        driver= DriverManagerUtils.launchBrowser();

        driver.get("https://eviltester.github.io/supportclasses");

    }

    @Test
    public void sendMessageWithWaitInPageObject(){

        SupportClassesPage page = new SupportClassesPage(driver);
        Assertions.assertEquals(0, page.countSingleMessageHistory());
        page.clickResendSingleButton();

        Assertions.assertEquals("Received message: selected 1",
                                page.waitForMessage());
        Assertions.assertEquals(1, page.countSingleMessageHistory());
    }

    @AfterEach
    public void closeDriver(){
        driver.quit();
    }
}
