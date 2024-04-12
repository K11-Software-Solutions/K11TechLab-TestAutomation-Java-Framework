package com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_01_01_overview;

import com.k11.automation.coreframework.util.DriverManagerUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class InitialTest {

    private static WebDriver driver;

    @BeforeAll
    public static void createDriver() {

        driver= DriverManagerUtils.launchBrowser();

        driver.get("https://eviltester.github.io/supportclasses/");
    }

    @Test
    public void anInitialTest() {

        Assertions.assertEquals("Support Classes Example", driver.getTitle());
    }

    @AfterAll
    public static void closeDriver() {

        driver.quit();
    }
}
