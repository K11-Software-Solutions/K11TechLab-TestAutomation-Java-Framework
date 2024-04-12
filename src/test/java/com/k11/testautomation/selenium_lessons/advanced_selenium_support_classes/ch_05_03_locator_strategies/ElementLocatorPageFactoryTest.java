package com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_05_03_locator_strategies;

import com.k11.automation.coreframework.util.DriverManagerUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

public class ElementLocatorPageFactoryTest {

    WebDriver driver;

    @BeforeEach
    public void createDriver(){
        driver= DriverManagerUtils.launchBrowser();

        // trigger time delays with a hash e.g. #2000
        // trigger extra delay to display with an underscore #_2000
        driver.get("https://eviltester.github.io/supportclasses/#_2000");

    }

    @Test
    public void sendMessage(){

        SupportPage page = new SupportPage(driver);

        page.singleResendButton.click();

        Assertions.assertEquals("Received message: selected 1",
                page.waitForMessage());
    }


    @AfterEach
    public void closeDriver(){
        driver.quit();
    }
}
