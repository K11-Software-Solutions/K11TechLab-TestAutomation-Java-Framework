package com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_03_01_locators;

import com.k11.automation.coreframework.util.DriverManagerUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.util.List;

public class LocatorsTest {

    static WebDriver driver;

    @BeforeAll
    public static void createDriver(){

        driver= DriverManagerUtils.launchBrowser();
        driver.get("https://eviltester.github.io/supportclasses/");

    }


    @Test
    public void coreByExploration(){

        // Most of the By classes are in core selenium webdriver
        WebElement title = driver.findElement(By.id("instruction-title"));

        // support classes have
        // ByIdOrName
        // ByAll - union of all by
        // ByChained

    }

    @Test
    public void byIdOrName(){

        WebElement idButton = driver.findElement(By.id("resend-select"));
        Assertions.assertEquals("Resend Single Option Message",
                                    idButton.getText());

        WebElement namedButton = driver.findElement(By.name("resend-select"));
        Assertions.assertEquals("Resend Multi Option Message",
                                namedButton.getText());

        // ByIdOrName can match by id, and if that doesn't match treat it as a name
        // use ByIdOrName to find a button element "resend-select"
        // and the assertions should pass
        WebElement button = driver.findElement(new ByIdOrName("resend-select"));
         Assertions.assertEquals(idButton, button);
         Assertions.assertNotEquals(namedButton, button);


        //ByIdOrName findElements returns all id and name matches
        //findElements for "resend-select" should find 2 buttons
        List<WebElement> buttons = driver.findElements(new ByIdOrName("resend-select"));
        Assertions.assertEquals(2, buttons.size());

        // the elements identified should be the same as we found initially
        Assertions.assertTrue(buttons.contains(idButton));
        Assertions.assertTrue(buttons.contains(namedButton));
    }

    @Test
    public void byAll(){

        // we could use ByAll to find by id or by name
        // by all is a collator, so given a number of locators, find all items that match
        final List<WebElement> buttons = driver.findElements(
                                                new ByAll(By.id("resend-select"),
                                                        By.name("resend-select")));

        Assertions.assertEquals(2, buttons.size());
        Assertions.assertTrue(buttons.contains(driver.findElement(By.id("resend-select"))));
        Assertions.assertTrue(buttons.contains(driver.findElement(By.name("resend-select"))));
    }

    @Test
    public void byChained(){

        final WebElement resendSingle = driver.findElement(By.id("resend-select"));
        resendSingle.click();
        resendSingle.click();
        resendSingle.click();
        resendSingle.click();

        final WebElement resend = driver.findElement(By.id("resend-multi"));
        resend.click();
        resend.click();

        // TODO: make this more specific to only find messages under a 'list'
        final List<WebElement> allMessages = driver.findElements(
                                                new ByChained(By.name("list"),
                                                        By.className("message")));
        Assertions.assertEquals(6, allMessages.size());

        // then just the #single list .message
        final List<WebElement> singleMessages = driver.findElements(
                new ByChained(By.id("single"),By.name("list"),
                        By.className("message")));
        Assertions.assertEquals(4, singleMessages.size());
        // then the #multi list .message
        final List<WebElement> multiMessages = driver.findElements(
                new ByChained(By.id("multi"),By.name("list"),
                        By.className("message")));
        Assertions.assertEquals(2, multiMessages.size());
    }


    @AfterAll
    public static void closeDriver(){
        driver.quit();
    }

}
