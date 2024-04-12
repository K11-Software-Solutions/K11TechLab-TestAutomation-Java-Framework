package com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_05_01_page_objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.regex.Pattern;

public class SupportClassesTestPage {
    private final WebDriverWait wait;
    private final WebDriver driver;

    By selectMenuLocator = By.id("select-menu");
    By messageLocator = By.id("message");

    // basic constructor using WebDriver
    public SupportClassesTestPage(WebDriver driver){
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void selectSingleOptionMessage(final String singleOptionText) {
        WebElement singleSelectMenu = wait.until(
                                    ExpectedConditions.
                                    visibilityOfElementLocated(selectMenuLocator));
        final Select select = new Select(singleSelectMenu);
        select.selectByVisibleText(singleOptionText);
    }


    public void waitForMessageReceived() {
        wait.until(ExpectedConditions.
                    visibilityOfElementLocated(messageLocator));
        wait.until(ExpectedConditions.
                    textMatches(messageLocator, Pattern.compile("\\S")));
    }

    public String getLastSingleMessage() {
        return driver.findElement(messageLocator).getText();
    }

}
