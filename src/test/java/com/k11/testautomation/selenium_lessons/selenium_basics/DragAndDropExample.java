package com.k11.testautomation.selenium_lessons.selenium_basics;

/**
 * Created by Maor on 11/04/2018.
 */

import com.k11.automation.coreframework.util.DriverManagerUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.concurrent.TimeUnit;

public class DragAndDropExample {

    WebDriver driver;

    @BeforeTest
    public void start(){
        driver= DriverManagerUtils.launchBrowser();
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

    }

    @Test
    public void Example() throws InterruptedException {
        driver.get("http://jqueryui.com/droppable/");
        driver.switchTo().frame(0);
        WebElement dragElement = driver.findElement(By.id ("draggable"));
        WebElement dropElement = driver.findElement(By.id ("droppable"));

        // Configure the Action
        Actions builder = new Actions(driver);
        Action dragAndDrop = builder.clickAndHold(dragElement)
                .moveToElement(dropElement)
                .release(dropElement)
                // Get the action
                .build();
        // Execute the Action
        dragAndDrop.perform();
        // Just a simple sleep to see the result
        Thread.sleep(3000);
    }

    @AfterTest
    // Closing the whole browser session
    public void tearDown() {
        if(driver!= null) {
            System.out.println("Closing chrome browser...Please wait");
            driver.quit();
        }
    }
}