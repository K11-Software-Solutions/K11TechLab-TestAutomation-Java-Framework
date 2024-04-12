package com.k11.testautomation.selenium_lessons.selenium4_latest_features;

import java.util.concurrent.TimeUnit;

import com.k11.automation.coreframework.util.DriverManagerUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.locators.RelativeLocator;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class RelativeLocatorsTest {

	WebDriver driver;

		@BeforeTest
		public void start(){
			driver= DriverManagerUtils.launchBrowser();
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

		}

		@Test
		public void relativeLocatorsExample() throws InterruptedException {
		driver.get("https://www.way2automation.com/way2auto_jquery/index.php");

		// Relative locator above - use the input element above the select element 
		driver.findElement(RelativeLocator.with(By.tagName("input")).above(By.tagName("select"))).sendKeys("asi@gmail.com");
		
		// Relative locator below - use the input element below the select element 
		driver.findElement(RelativeLocator.with(By.tagName("input")).below(By.tagName("select"))).sendKeys("London");
		
		// Relative locator near - use the link element which contains 'THE' near the link element 'signin'  
		driver.findElement(RelativeLocator.with(By.partialLinkText("THE")).near(By.linkText("Signin"))).click();
		
		// Relative locator rightOf - use the input element type password which is right of the label element
		//driver.findElement(RelativeLocator.with(By.xpath("//input[@type='password']")).toRightOf(By.tagName("label"))).sendKeys("mypassword");
		
		// Relative locator leftOf - click the sigiin link which is left of the Submit button
		//driver.findElement(RelativeLocator.with(By.linkText("Signin")).toLeftOf(By.xpath("(//*[@id=\"load_form\"]/div[1]/div[2]/input)[2]"))).click();
		
		// Chaining Relative Locators
		/*driver.findElement(RelativeLocator.with(By.tagName("input"))
				.below(By.xpath("//*[@id=\"load_form\"]/fieldset[5]/input"))  // below the city field
				.above(By.xpath("//*[@id=\"load_form\"]/fieldset[7]/input"))) // above the password field
				.sendKeys("asi");

		 */
	}
}