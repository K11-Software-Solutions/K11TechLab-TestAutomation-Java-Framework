package com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_02_01_element_abstractions;

import com.k11.automation.coreframework.util.DriverManagerUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class SelectExampleTest {


    /*
        There are many types of abstractions that we can use and a very simple,
        low level abstraction is an Element Abstraction,
        a wrapper around an element on the screen.
        In this video we will explain what this means and how it can help us using Select as an example.
     */

    static WebDriver driver;

    @BeforeAll
    public static void createDriver(){

        driver= DriverManagerUtils.launchBrowser();
        driver.get("https://eviltester.github.io/supportclasses/");
    }


    @Test
    public void canSelectAnOptionUsingSelect(){

        final WebElement selectMenu = driver.findElement(By.id("select-menu"));
        final Select select = new Select(selectMenu);
        select.selectByVisibleText("Option 3");
        Assertions.assertEquals("3", select.getFirstSelectedOption().
                                    getAttribute("value"));
    }
    // or we could instantiate a Select object to wrap the selectMenu WebElement
    // then use the Select methods like selectByVisibleText
    // and assert with methods like getFirstSelectedOption
    // and if I did that I should have a method named
    // canSelectAnOptionUsingSelect


    // explore the rest of the select class functions

    @Test
    public void canGetInfoAboutSelect(){


        final WebElement selectMenu = driver.findElement(By.id("select-menu"));

        final Select select = new Select(selectMenu);

        // the isMultiple method should be false for the select-menu item

        final WebElement multiSelectMenu = driver.findElement(By.id("select-multi"));

        final Select multiSelect = new Select(multiSelectMenu);

        // the isMultiple method should be true for multi select
        Assertions.assertFalse(select.isMultiple());
        Assertions.assertTrue(multiSelect.isMultiple());
    }

    @Test
    public void canGetAllOptionsFromSelect(){


        final WebElement selectMenu = driver.findElement(By.id("select-menu"));

        final Select select = new Select(selectMenu);

        // getOptions will return a List of WebElement
        // and allow me to access the options using
        // simple List methods
        List<WebElement> options = select.getOptions();
        Assertions.assertEquals(4,options.size());
        Assertions.assertEquals("Option 1", options.get(0).getText());
    }

    @Test
    public void canSelectSingleOptions(){

        // demo test to show single-select capabilities
        final WebElement selectMenu = driver.findElement(By.id("select-menu"));

        final Select select = new Select(selectMenu);

        // select will do nothing because this option is selected by default
        select.selectByIndex(0);
        Assertions.assertEquals("Option 1", select.getFirstSelectedOption().getText());

        // I can select the second item by Index 1 to chooose Option 2
        select.selectByIndex(1);
        Assertions.assertEquals("Option 2", select.getFirstSelectedOption().getText());

        // I can select the first by using the value "1"
        select.selectByValue("1");
        Assertions.assertEquals("Option 1", select.getFirstSelectedOption().getText());

        // and I can select using the text in the option
        select.selectByVisibleText("Option 3");
        Assertions.assertEquals("3", select.getFirstSelectedOption().getAttribute("value"));
    }


    @Test
    public void canSelectAndDeselectMultiOptions(){


        // demo test to show multi-select capabilities

        final WebElement selectMenu = driver.findElement(By.id("select-multi"));

        final Select select = new Select(selectMenu);

        // make sure nothing is selected with deselectAll
        select.deselectAll();

        // A normal select by index to get the First item
        select.selectByIndex(0);
        Assertions.assertEquals("First", select.getFirstSelectedOption().getText());

        // if I select I can deselect - by index, text or value
        select.deselectByIndex(0);

        // when nothing is selected a NoSuchElementException is thrown
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            select.getFirstSelectedOption();
        });

        // select two items - values 20 and 30
        select.selectByValue("20");
        select.selectByValue("30");

        // use getAllSelectedOptions, there should be 2 in the list
        final List<WebElement> selected = select.getAllSelectedOptions();
        Assertions.assertEquals(2, selected.size());

        // assert on the getText for the list entries
        Assertions.assertEquals("Second", selected.get(0).getText());
        Assertions.assertEquals("Third", selected.get(1).getText());

        // deselect the first one - assert visible text "Second"
        select.deselectByVisibleText("Second");

        // and assert that the first selected option text is "Third"
        Assertions.assertEquals("Third", select.getFirstSelectedOption().getText());

        // deselect them all to finish
        select.deselectAll();
        Assertions.assertEquals(0, select.getAllSelectedOptions().size());

        Assertions.assertEquals(selectMenu, select.getWrappedElement());
    }


    @AfterAll
    public static void closeDriver(){
        driver.quit();
    }
}
