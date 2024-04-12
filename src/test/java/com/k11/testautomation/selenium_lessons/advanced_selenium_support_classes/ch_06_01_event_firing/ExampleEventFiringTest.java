package com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_06_01_event_firing;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class ExampleEventFiringTest {

    WebDriver driver;

    @BeforeEach
    public void createDriver(){

        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();

        driver.get("https://eviltester.github.io/supportclasses/");

    }

    @Test
    public void loggingFindingElements() {

        final By resend = By.id("resend-select");
        final By noSuchElement = By.id("no-such-element");

        EventFiringWebDriver events = new EventFiringWebDriver(driver);
        events.register(new LocalEventFiringListener());

        WebElement resendElem = events.findElement(resend);
        Assertions.assertNotNull(resendElem);

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            events.findElement(noSuchElement);
        });

    }


    @AfterEach
    public void closeDriver(){
        driver.quit();
    }

    private class LocalEventFiringListener extends AbstractWebDriverEventListener {

        @Override
        public void beforeFindBy(By by, WebElement element, WebDriver driver) {
            System.out.println("Looking For " + by.toString());
        }

        @Override
        public void afterFindBy(By by, WebElement element, WebDriver driver) {
            System.out.println("Finished looking for " + by.toString());
        }
    }
}
