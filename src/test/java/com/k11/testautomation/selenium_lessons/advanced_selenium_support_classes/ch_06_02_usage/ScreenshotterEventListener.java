package com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_06_02_usage;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

import java.io.File;
import java.io.IOException;

public class ScreenshotterEventListener extends AbstractWebDriverEventListener{

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        File screenshot = ((TakesScreenshot) driver).
                                getScreenshotAs(OutputType.FILE);

        File screenshotFolder = new File(System.getProperty("user.dir"),
                                "screenshots");

        screenshotFolder.mkdirs();

        try {
            FileUtils.copyFile(screenshot, new File(screenshotFolder,
                    System.currentTimeMillis()+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
