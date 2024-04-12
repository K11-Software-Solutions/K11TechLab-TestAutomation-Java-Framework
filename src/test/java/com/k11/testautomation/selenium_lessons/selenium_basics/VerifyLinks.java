package com.k11.testautomation.selenium_lessons.selenium_basics;

import com.k11.automation.coreframework.util.DriverManagerUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VerifyLinks {

    public static void main(String[] args) {
        // Set the path to chromedriver.exe (download from https://chromedriver.chromium.org/downloads)
        //System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");

        // Create a new instance of the Chrome driver
       // WebDriver driver = new ChromeDriver();

        WebDriver driver= DriverManagerUtils.launchBrowser();

        // Maximize the browser window
        driver.manage().window().maximize();

        // Set implicit wait time to 10 seconds
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        // Navigate to the URL
        driver.get("https://www.google.com/");

        // Get all the links on the page
        List<WebElement> links = driver.findElements(By.tagName("a"));

        // Print the total number of links found
        System.out.println("Total links found: " + links.size());

        // Create an HttpClient instance
        HttpClient client = HttpClientBuilder.create().build();

        // Check each link
        for (WebElement link : links) {
            String url = link.getAttribute("href");
            if (url != null && !url.isEmpty()) {
                try {
                    // Create an HTTP GET request
                    HttpGet request = new HttpGet(url);

                    // Execute the request and get the response
                    HttpResponse response = client.execute(request);

                    // Check the response code
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        System.out.println("Link is valid: " + url);
                    } else {
                        System.out.println("Broken link found: " + url + " (Response code: " + statusCode + ")");
                    }
                } catch (IOException e) {
                    System.out.println("Error checking link: " + url + " (" + e.getMessage() + ")");
                }
            }
        }

        // Close the browser
        driver.quit();
    }
}
