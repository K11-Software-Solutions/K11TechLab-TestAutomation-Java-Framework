package com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_01_02_quick_wins;

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
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.Colors;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Quotes;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuickWinsTest {

/*
    This test contains code to demo:

    - Select, which is a WebElement Abstraction
    - a custom By Selector ByIdOrName
    - Quotes for creating XPath quoted locators
    - Colours for working with Colours
 */

    static WebDriver driver;

    @BeforeAll
    public static void createDriver(){

        driver= DriverManagerUtils.launchBrowser();

        driver.get("https://eviltester.github.io/supportclasses/");

    }

    // The select class makes it easy to work with Select options
    // rather than finding the select menu and then all the options
    // below it - this is the only Element Abstraction in the
    // support classes
    @Test
    public void canSelectAnOptionUsingSelect(){

        final WebElement selectMenu = driver.findElement(By.id("select-menu"));
        final Select select = new Select(selectMenu);
        select.selectByVisibleText("Option 3");
        Assertions.assertEquals("3",
                                    select.getFirstSelectedOption().
                                    getAttribute("value"));
    }

    @Test
    public void findInstructionsByIdOrName(){

        // findElement returns the element with the id if it exists, and if not searches for it via the name
        final WebElement instructionsPara = driver.findElement(
                                                    new ByIdOrName("instruction-text"));
        final List<WebElement> instructionsParaAgain = driver.findElements(
                                                    new ByIdOrName("instructions"));

        Assertions.assertEquals(instructionsPara.getText(),
                                instructionsParaAgain.get(0).getText());
    }


    @Test
    public void quotesEscapingToCreateXPath(){

        Assertions.assertEquals("\"literal\"",
                                    Quotes.escape("literal"));
        Assertions.assertEquals("\"'single-quoted'\"",
                                    Quotes.escape("'single-quoted'"));
        Assertions.assertEquals("'\"double-quoted\"'",
                                    Quotes.escape("\"double-quoted\""));
        Assertions.assertEquals("concat(\"\", '\"', \"quot'end\", '\"')",
                                    Quotes.escape("\"quot'end\""));
        Assertions.assertEquals("concat(\"'quo\", '\"', \"ted'\")",
                                    Quotes.escape("'quo\"ted'"));
    }

    @Test
    public void colors(){

        final WebElement title = driver.findElement(By.id("instruction-title"));

        // Colors is an enum of named Color objects

        final Color blackValue = Colors.BLACK.getColorValue();

        // Color has methods to help convert between RBG, HEX

        Assertions.assertEquals("#000000",blackValue.asHex());
        Assertions.assertEquals("rgba(0, 0, 0, 1)",blackValue.asRgba());
        Assertions.assertEquals("rgb(0, 0, 0)",blackValue.asRgb());

        // color values returned by WebElement's getCSSValue are always
        // RGBA format, not the HTML source HEX or RGB

        Assertions.assertEquals(title.getCssValue("background-color"),
                                    blackValue.asRgba());

        // can create custom colors using the RGB input constructor
        // if the Colors enum does not have what we need

        final Color redValue = new Color(255,0,0, 1);
        Assertions.assertEquals(title.getCssValue("color"), redValue.asRgba());

    }

    @Test
    public void waitForMessage() {

        final WebElement selectMenu = driver.findElement(By.id("select-menu"));

        final Select select = new Select(selectMenu);

        select.selectByVisibleText("Option 2");

        // We are so used to using WebDriverWait and the ExpectedConditions class
        // that we might not have realised these are part of the support packages

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.textToBe(
                        By.id("message"), "Received message: selected 2"));

    }

    @AfterAll
    public static void closeDriver(){
        driver.quit();
    }
}
