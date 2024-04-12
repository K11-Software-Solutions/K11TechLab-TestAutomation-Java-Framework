package com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_05_01_page_objects;

import com.k11.automation.coreframework.util.DriverManagerUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

public class SupportClassesTestPageTest {

    WebDriver driver;

    @BeforeEach
    public void createDriver(){

        driver= DriverManagerUtils.launchBrowser();

        driver.get("https://eviltester.github.io/supportclasses/#2000");

    }

    /*
        Test using abstractions is:

        - easy to read
        - only has to change if intent of test changes
        - does not have to change if application changes - the page objects change
        - page objects used in multiple tests

     */
    @Test
    public void canSendMessage(){

        SupportClassesTestPage page = new SupportClassesTestPage(driver);

        page.selectSingleOptionMessage("Option 2");

        page.waitForMessageReceived();

        Assertions.assertEquals("Received message: selected 2",
                                page.getLastSingleMessage());

    }

    @AfterEach
    public void closeDriver(){
        driver.quit();
    }
}
