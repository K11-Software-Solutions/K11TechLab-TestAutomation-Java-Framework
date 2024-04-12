package com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_05_02_page_factory;

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

        driver.get("https://eviltester.github.io/supportclasses/#2000");

    }

    @Test
    public void sendMessage(){

        SupportClassesPage page = new SupportClassesPage(driver);

        page.singleResendButton.click();

//        Assertions.assertEquals("Received message: selected 1",
//         page.message.getText());
    }

    // the default most people use for handling timeout issues with
    // page factory is implicit waits

    @Test
    public void sendMessageWithWaitInPageObject(){

        SupportClassesPage page = new SupportClassesPage(driver);

        page.singleResendButton.click();

        Assertions.assertEquals("Received message: selected 1",
                page.waitForMessage());
    }


    @AfterEach
    public void closeDriver(){
        driver.quit();
    }
}
