package com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_04_02_expected_conditions;

import com.k11.automation.coreframework.util.DriverManagerUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ExpectedConditionsExampleTest {

    /*
    The ExpectedConditions class provides a lot of pre-built
    methods for synchronisation,

    provide a description of these. showing some in action.

    Look at the code to see how they work as static methods
    that return an Expected Condition

    Create an expected condition based on the patterns shown
    in ExpectedConditions class to sync on something specific.

    */

    WebDriver driver;

    @BeforeEach
    public void createDriver(){

        driver= DriverManagerUtils.launchBrowser();
        // trigger time delays with the hash
        driver.get("https://eviltester.github.io/supportclasses/#2000");

    }


    @Test
    public void explicitWaitUsingExpectedConditions(){

        final WebElement resendButton = driver.findElement(By.id("resend-select"));
        resendButton.click();

        // the message is not immediately displayed but our explicit wait will handle this
        // presenceOfElementLocated
        // visibilityOfElementLocated
        // explain other conditions

        final WebElement message = new WebDriverWait(driver, Duration.ofSeconds(10)).
                until(ExpectedConditions.
                        visibilityOfElementLocated(
                                By.cssSelector("#single-list li.message")));
        // view code and see how expected conditions work

        Assertions.assertTrue(message.getText().startsWith("Received message:"));

    }




    @Test
    public void explicitWaitUsingCustomExpectedCondition(){

        final WebElement resendButton = driver.findElement(By.id("resend-select"));
        resendButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                historyMessagesIncreaseInNumber()
        );
        WebElement message = driver.findElement(By.cssSelector("#single-list li.message"));
        Assertions.assertTrue(message.getText().startsWith("Received message:"));
    }

    private ExpectedCondition<Boolean> historyMessagesIncreaseInNumber() {
        return new ExpectedCondition<Boolean>(){

            private int initialCount=driver.findElements(By.cssSelector("li.message")).size();

            @Override
            public Boolean apply(WebDriver webDriver) {
                int currentCount = driver.findElements(By.cssSelector("li.message")).size();
                return currentCount>initialCount;
            }
        };
    }


    @AfterEach
    public void closeDriver(){
        driver.quit();
    }
}
