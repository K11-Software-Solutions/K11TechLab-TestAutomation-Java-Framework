package com.k11.testautomation.selenium_lessons.selenium_basics;
import java.util.Set;

import com.k11.automation.coreframework.util.DriverManagerUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WindowHandle_Demo {
  public static void main(String[] args) throws Exception {

     //System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
     //WebDriver driver = new ChromeDriver();
     WebDriver driver= DriverManagerUtils.launchBrowser();

     driver.manage().window().maximize();
     // Loading the website
     driver.get("https://www.google.com/");
     // Click on a link that opens a new window/tab
     WebElement newWindowLink = driver.findElement(By.tagName("a"));
     newWindowLink.click();

     // Get the handles of all open windows
     Set<String> windowHandles = driver.getWindowHandles();

     // Iterate through each window handle
     for (String handle : windowHandles) {
        // Switch to the window with the handle
        driver.switchTo().window(handle);

        // Print the title of the current window
        System.out.println("Current Window Title: " + driver.getTitle());

        // Check if the current window is the main window (google.com)
        if (driver.getTitle().contains("Google")) {
           // Perform actions on the main window
           System.out.println("Performing actions on the main window...");

           // For example, you can do something like searching for a term
           WebElement searchBox = driver.findElement(By.name("q"));
           searchBox.sendKeys("Selenium WebDriver");
           searchBox.submit();

           // Wait for a few seconds to see the result
           try {
              Thread.sleep(3000);
           } catch (InterruptedException e) {
              e.printStackTrace();
           }
        } else {
           // Perform actions on the new window (opened by clicking the link)
           System.out.println("Performing actions on the new window...");

           // For example, you can get the URL of the new window
           String newWindowUrl = driver.getCurrentUrl();
           System.out.println("New Window URL: " + newWindowUrl);
        }
     }

     // Close the browser
     driver.quit();
  }
}