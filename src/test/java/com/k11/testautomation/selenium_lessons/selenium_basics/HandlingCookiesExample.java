package com.k11.testautomation.selenium_lessons.selenium_basics;

import com.k11.automation.coreframework.util.DriverManagerUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Maor on 4/17/2018.
 */
public class HandlingCookiesExample {

    WebDriver driver;

    @Test
    public void AddGetCookies() {
        WebDriver driver= DriverManagerUtils.launchBrowser();
        driver.get("https://stackoverflow.com");
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        // Pass name and value for cookie as parameters
        Cookie name = new Cookie("seleniumtestcookie", "123456789123");
        driver.manage().addCookie(name);

        // To get our particular Cookie by name
        System.out.println(driver.manage().getCookieNamed("seleniumtestcookie").getValue());

        // To return all the cookies of the current domain
        Set<Cookie> cookiesForCurrentURL = driver.manage().getCookies();
        for (Cookie cookie : cookiesForCurrentURL) {
            System.out.println(" Cookie Name - " + cookie.getName()
                + " Cookie Value - "  + cookie.getValue());
            }
        }

    @Test
    public void deleteCookieByName()
    {
        WebDriver driver= DriverManagerUtils.launchBrowser();
        driver.get("https://stackoverflow.com");
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        // Delete our particular Cookie by name
        driver.manage().deleteCookieNamed("seleniumtestcookie");
    }

    @Test
    public void deleteAllCookies()
    {
        WebDriver driver= DriverManagerUtils.launchBrowser();
        driver.get("https://stackoverflow.com");
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.manage().deleteAllCookies();
    }

    @AfterTest
    // Closing the whole browser session
    public void tearDown() {
        if (driver != null) {
            System.out.println("Closing browser...Please wait");
            driver.quit();
        }
    }
}
