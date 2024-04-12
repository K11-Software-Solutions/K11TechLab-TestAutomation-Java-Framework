package com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_06_02_usage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ExampleEventFiringUsageTest {

    WebDriver driver;

    @BeforeEach
    public void createDriver(){

        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();

        // trigger time delays with the hash and extra delay with _
        driver.get("https://eviltester.github.io/supportclasses/#_1000");

    }

    @Test
    public void createFromAbstract(){

        // TODO: create an EventFiringWebDriver
        // with a HighLighterEventListener
        // and a ScreenshotterEventListener

        EventFiringWebDriver events = new EventFiringWebDriver(driver);
        events.register(new HighLighterEventListener());
        events.register(new ScreenshotterEventListener());

        final SimpleSupportPageObject page = new SimpleSupportPageObject(events);

        String message = page.chooseOption(4);
        Assertions.assertEquals("Received message: selected 4", message);

        message = page.chooseOption(3);
        Assertions.assertEquals("Received message: selected 3", message);

        message = page.chooseOption(2);
        Assertions.assertEquals("Received message: selected 2", message);

        message = page.chooseOption(1);
        Assertions.assertEquals("Received message: selected 1", message);
    }


    @AfterEach
    public void closeDriver(){
        driver.quit();
    }


    private class SimpleSupportPageObject {
        private final WebDriver driver;
        private final WebDriverWait wait;

        public SimpleSupportPageObject(final WebDriver driver) {
            this.driver = driver;
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        }

        public String chooseOption(int optionNumber){

            final WebElement option = this.driver.findElement(By.id("select-menu"));
            final Select selectOption = new Select(option);
            selectOption.selectByIndex(optionNumber-1);

            // wait for all messages to render and return the message response text
            WebElement listMesssage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#single-list li.message")));
            final WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#message")));
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("#message"), "Received message:"));

            return message.getText();
        }
    }
}
