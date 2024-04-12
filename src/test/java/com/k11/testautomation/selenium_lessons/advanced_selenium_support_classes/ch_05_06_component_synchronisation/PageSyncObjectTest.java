package com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_05_06_component_synchronisation;

import com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_05_06_component_synchronisation.LoadableSupportPage;
import com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_05_06_component_synchronisation.MessageHistoryComponent;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class PageSyncObjectTest {

    WebDriver driver;

    @BeforeEach
    public void createDriver(){

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test
    public void canSeeMessageInHistory(){

        // to show a component on the page
        LoadableSupportPage page = new LoadableSupportPage(driver);

        page.get();

        page.select("Option 2");
        Assertions.assertEquals("Received message: selected 2",
                                    page.getMessage());
        MessageHistoryComponent history = page.messageHistory();

        // wait for the history component to be ready
        history.get();

        Assertions.assertEquals(1, history.countSingleHistoryMessages());
        Assertions.assertEquals("Received message: selected 2",
                                    history.getSingleHistoryMessage(0));
    }

    @AfterEach
    public void closeDriver(){
        driver.quit();
    }
}
