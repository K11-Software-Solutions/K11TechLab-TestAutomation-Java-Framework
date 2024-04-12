package com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_04_01_synchronisation;

import com.k11.automation.coreframework.util.DriverManagerUtils;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;


public class SynchronisationExampleTest {

    /*
    explanation of synchronisation with an example of what happens when we don't synchronise
    and a test works once, but doesn't work second time through

    Explain difference between implicit wait and explicit wait and why we use WebDriverWait as an explicit wait.

    The key to writing good synchronisation.

    Explain how WebDriverWait works and general practices around doing it

    - wait and return, rather than wait, then find
    - re-use waits
    - timeouts
    - this is synchronisation not 'sleep' based on time
    - we can also use WebDriverWait as an assertion mechanism so we don't need asserts in page objects
     */


    WebDriver driver;

    @BeforeEach
    public void createDriver(){

        driver= DriverManagerUtils.launchBrowser();
        // trigger time delays with the hash
        driver.get("https://eviltester.github.io/supportclasses/#2000");

    }

    @Test
    public void whyWaitsAreRequired(){

        final WebElement resendButton = driver.findElement(By.id("resend-select"));
        resendButton.click();

        // the message is not immediately displayed, need to wait
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            final WebElement message = driver.findElement(By.cssSelector("#single-list li.message"));
        });

    }


    @Test
    public void implicitWait(){

        final WebElement resendButton = driver.findElement(By.id("resend-select"));
        resendButton.click();

        // implicit wait forces all findElement to poll until it passes or timesout end 5 seconds
        driver.manage().timeouts().
                implicitlyWait(5000, TimeUnit.MILLISECONDS);
        // the message is not immediately displayed so this line will fail without an implicit wait
        final WebElement message = driver.findElement(By.cssSelector("#single-list li.message"));

        // slows down tests on failures
        // may cause some tests to pass which should not
        // only option is to increase global timeout when timing issues happen, which slows tests down further
        // hard to check for absence of something since it takes as long as the timeout

        // remember to set implicit waits off if you use them otherwise it will affect all findElement commands
        driver.manage().timeouts().
                implicitlyWait(0, TimeUnit.MILLISECONDS);
    }

    @Test
    public void explicitWait(){

        // explicit wait means only waiting at specific points

        final WebElement resendButton = driver.findElement(By.id("resend-select"));
        resendButton.click();

        // the message is not immediately displayed so I need wait for
        // visibility of Element to change
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("#single-list li.message")
                )
        );

        // need to wait for the element to be visible prior to trying to find it
        // and assert on the result
        final WebElement message = driver.findElement(By.cssSelector("#single-list li.message"));
        Assertions.assertTrue(message.getText().startsWith("Received message:"));
    }

    @Test
    public void shareWaitAndUseOnReturn(){

        // often we share a wait e.g. set this up in @BeforeX methods

        final WebElement resendButton = driver.findElement(By.id("resend-select"));
        resendButton.click();

        // normally we 'wait and return' rather than wait then repeat find
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("#single-list li.message")));

        //final WebElement message = driver.findElement(By.cssSelector("#single-list li.message"));
        Assertions.assertTrue(message.getText().startsWith("Received message:"));
    }

    @AfterEach
    public void closeDriver(){
        driver.quit();
    }


}
