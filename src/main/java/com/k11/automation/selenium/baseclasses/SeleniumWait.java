package com.k11.automation.selenium.baseclasses;

import com.k11.automation.coreframework.logger.Log;
import com.k11.automation.coreframework.util.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Class wrapper around wait functions.
 */
public class
SeleniumWait {

    /**
     * WebDriver for this page.
     */
    private WebDriver driver;

    /**
     * Default Timeout.
     */
    private static final int DEFAULT_TIMEOUT = ApplicationProperties.DEFAULT_TIMEOUT.getIntVal();

    /**
     * Default time to wait before checking again.
     */
    private static final int WAIT_POLL_TIMEOUT = ApplicationProperties.WAIT_POLL_TIMEOUT.getIntVal();

    /**
     * Public constructor to create the driver.
     *
     * @param webDriver the web driver
     */
    public SeleniumWait(WebDriver webDriver) {
        this.driver = webDriver;
    }

    /**
     * Gets the webdriver.
     *
     * @return returns the webdriver
     */
    public WebDriver getDriver() {
        return this.driver;
    }

    /**
     * Gets a new WaitDriver using the default timeout.
     *
     * @return new WaitDriver
     */
    public WebDriverWait getNewWaitDriver() {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    /**
     * Gets a new WaitDriver using the specified timeout.
     *
     * @param defaultTimeout the default timeout
     * @return new WaitDriver
     */
    public WebDriverWait getNewWaitDriver(int defaultTimeout) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(defaultTimeout));
    }

    /**
     * Returns a new Fluent wait object.
     *
     * @return new fluent wait
     */
    public FluentWait getNewDriverFluentWait() {
        return getNewDriverFluentWait(DEFAULT_TIMEOUT, WAIT_POLL_TIMEOUT);
    }

    /**
     * Returns a new Fluent wait object.
     *
     * @param defaultTime the default timeout
     * @param pollingSeconds the polling seconds before retry
     * @return new fluent wait
     */
    public FluentWait getNewDriverFluentWait(int defaultTime, int pollingSeconds) {
        return new FluentWait(driver)
                .withTimeout(Duration.ofSeconds(defaultTime))
                .pollingEvery(Duration.ofSeconds(pollingSeconds))
                .ignoring(NotFoundException.class);
    }

    /**
     * Returns a new Fluent wait object.
     *
     * @param element the element
     * @return new fluent wait
     */
    public FluentWait getNewElementFluentWait(WebElement element) {
        return getNewElementFluentWait(element, DEFAULT_TIMEOUT, WAIT_POLL_TIMEOUT);
    }

    /**
     * Returns a new Fluent wait object.
     *
     * @param element the element
     * @param defaultTime the default timeout
     * @param pollingSeconds the polling seconds before retry
     * @return new fluent wait
     */
    public FluentWait getNewElementFluentWait(WebElement element, int defaultTime, int pollingSeconds) {
        return new FluentWait(element)
                .withTimeout(Duration.ofSeconds(defaultTime))
                .pollingEvery(Duration.ofSeconds(pollingSeconds))
                .ignoring(NotFoundException.class);
    }

    /**
     * Waits for the condition to be return true or hit the default timeout
     * limit.
     *
     * @param condition the wait condition
     * @return webelement if wait was successful
     */
    public WebElement waitForWebElement(ExpectedCondition<WebElement> condition) {
        return waitForWebElement(condition, DEFAULT_TIMEOUT);
    }

    /**
     * Waits for the condition to be return true or hit the timeout limit.
     *
     * @param condition the wait condition
     * @param timeout the max time to wait
     * @return webelement if wait was successful
     */
    public WebElement waitForWebElement(ExpectedCondition<WebElement> condition, int timeout) {
        return this.getNewWaitDriver(timeout).until(condition);
    }

    /**
     * Waits for the condition to be return true or hit the default timeout
     * limit.
     *
     * @param condition the wait condition
     * @return webelement if wait was successful
     */
    public Boolean waitForBoolean(ExpectedCondition<Boolean> condition) {
        return waitForBoolean(condition, DEFAULT_TIMEOUT);
    }

    /**
     * Waits for the condition to be return true or hit the timeout limit.
     *
     * @param condition the wait condition
     * @param timeout the max time to wait
     * @return webelement if wait was successful
     */
    public Boolean waitForBoolean(ExpectedCondition<Boolean> condition, int timeout) {
        return this.getNewWaitDriver(timeout).until(condition);
    }

    /**
     * Waits for the element to be present.
     *
     * @param locator the element By selector
     * @return returns true if the element is presente on the page
     */
    public Boolean waitForElementToBePresent(By locator) {
        return waitForElementToBePresent(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Waits for the element to be present.
     *
     * @param locator the element By selector
     * @param maxWaitTime the max wait time
     * @return returns true if the element is presente on the page
     */
    public Boolean waitForElementToBePresent(By locator, int maxWaitTime) {
        try {
            this.waitForWebElement(ExpectedConditions.presenceOfElementLocated(locator), maxWaitTime);
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    /**
     * Waits for web element to be visible.
     *
     * @param locator the by selector
     * @return returns the webelement
     */
    public WebElement waitForElementToBeVisible(final By locator) {
        return waitForElementToBeVisible(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Waits for web element to be visible.
     *
     * @param locator the by selector
     * @param timeoutSeconds the timeout
     * @return returns the webelement
     */
    public WebElement waitForElementToBeVisible(final By locator, int timeoutSeconds) {
        WebElement element = null;
        FluentWait<WebDriver> webWait = this.getNewDriverFluentWait(timeoutSeconds, WAIT_POLL_TIMEOUT)
                .ignoring(NoSuchElementException.class);

        try {
            element = webWait.until(webDriver -> webDriver.findElement(locator));
            webWait.until(ExpectedConditions.visibilityOf(element));
        } catch (NoSuchElementException
                | StaleElementReferenceException
                | TimeoutException e) {
            Log.LOGGER.error("Error: Element is not visible. " + e.getMessage());
            throw e;
        }

        return element;
    }

    /**
     * Waits for web element to be visible.
     *
     * @param element the by selector
     */
    public void waitForElementToBeVisible(final WebElement element) {
        this.waitForElementToBeVisible(element, DEFAULT_TIMEOUT, WAIT_POLL_TIMEOUT);
    }

    /**
     * Waits for web element to be visible.
     *
     * @param element the by selector
     * @param timeoutSeconds the timeout
     * @param pollintTime the polling time
     */
    public void waitForElementToBeVisible(final WebElement element, int timeoutSeconds, int pollintTime) {
        this.getNewDriverFluentWait(timeoutSeconds, pollintTime)
                .until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits for web element to be clickable.
     *
     * @param locator the by selector
     * @return returns the webelement
     */
    public WebElement waitForElementToBeClickable(final By locator) {
        return waitForElementToBeClickable(this.getDriver().findElement(locator), DEFAULT_TIMEOUT, WAIT_POLL_TIMEOUT);
    }

    /**
     * Waits for web element to be clickable.
     *
     * @param locator the by selector
     * @param timeoutSeconds the timeout
     * @param pollingTime the polling time
     * @return returns the webelement
     */
    public WebElement waitForElementToBeClickable(final By locator, int timeoutSeconds, int pollingTime) {
        return waitForElementToBeClickable(this.getDriver().findElement(locator), timeoutSeconds, pollingTime);
    }

    /**
     * Waits for web element to be clickable.
     *
     * @param element the by selector
     * @return returns the webelement
     */
    public WebElement waitForElementToBeClickable(final WebElement element) {
        return waitForElementToBeClickable(element, DEFAULT_TIMEOUT, WAIT_POLL_TIMEOUT);
    }

    /**
     * Waits for web element to be clickable.
     *
     * @param element the by selector
     * @param timeoutSeconds the timeout
     * @param pollingTime the polling time
     * @return returns the webelement
     */
    public WebElement waitForElementToBeClickable(final WebElement element, int timeoutSeconds, int pollingTime) {
        Wait<WebDriver> webWait = this.getNewDriverFluentWait(timeoutSeconds, pollingTime);

        try {
            webWait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (NoSuchElementException
                | StaleElementReferenceException
                | TimeoutException e) {
            Log.LOGGER.error("Error: " + e.getMessage());
            throw e;
        }
        return element;
    }

    /**
     * Waits for the Frame to be available.
     *
     * @param frameName The frame locator
     */
    public void waitForIframeToLoad(String frameName) {
        this.getNewWaitDriver().until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameName));
    }

    /**
     * Waits for the page to finish loading, in a ready state.
     *
     * @return true if the page source has not changed within the amount of time
     * and if the document is in a ready state
     */
    public boolean waitForPageToLoad() {

        this.getNewWaitDriver().until((ExpectedCondition<Boolean>) d -> (((JavascriptExecutor) getDriver()).executeScript("return document.readyState").equals("complete")));

        String pageSourceBefore;
        String pageSoureAfter;
        int counter = DEFAULT_TIMEOUT / WAIT_POLL_TIMEOUT;

        do {
            try {
                counter--;
                pageSourceBefore = this.driver.getPageSource();
                WaitUtil.waitSeconds(WAIT_POLL_TIMEOUT);
                pageSoureAfter = this.driver.getPageSource();
            } catch (Exception e) {
                Log.LOGGER.error("Failed to wait for page to load. " + e.getMessage() + e.getStackTrace());
                throw e;
            }
        } while (!pageSourceBefore.equals(pageSoureAfter) && counter > 0);

        return pageSourceBefore.equals(pageSoureAfter);
    }

    /**
     * Waits until the element is visible.
     *
     * @param locator the By selector
     * @return returns true if the element is visible
     */
    public boolean untilElementAppears(By locator) {
        return untilElementAppears(this.getDriver().findElement(locator));
    }

    /**
     * Waits until the element is visible.
     *
     * @param element the By selector
     * @return returns true if the element is visible
     */
    public boolean untilElementAppears(WebElement element) {
        return untilElementAppears(element);
    }

    /**
     * Waits until the element is visible.
     *
     * @param element the By selector
     * @param maxTimeout max timeout
     * @param pollingTimeout polling time
     * @return returns true if the element is visible
     */
   /* public boolean untilElementAppears(WebElement element, int maxTimeout, int pollingTimeout) {
        FluentWait<WebElement> fluentWait = this.getNewElementFluentWait(element, maxTimeout, pollingTimeout);

        Function<WebElement, Boolean> function = obj -> {
            try {
                return obj.isDisplayed();
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                // Do not throw these exceptions here. Instead return false and let the fluent wait try again.
                return false;
            }
        };
        return fluentWait.until(function);
    }
*/
    /**
     * Waits until the element is not visible.
     *
     * @param locator the By selector
     * @return returns true if the element is not visible
     */
    public boolean untilElementDisappears(By locator) {
        try {
            return untilElementDisappears(this.getDriver().findElement(locator));
        } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException e) {
            // If this.getDriver.findElement fails with these exception, it does not exist and has disappeared.
            return true;
        }
    }

    /**
     * Waits until the element is not visible.
     *
     * @param locator the By selector
     * @param maxTimeout max timeout
     * @param pollingTimeout polling time
     * @return returns true if the element is not visible
     */
    public boolean untilElementDisappears(By locator, int maxTimeout, int pollingTimeout) {
        try {
            return untilElementDisappears(locator, maxTimeout, pollingTimeout);
        } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException e) {
            // If this.getDriver.findElement fails with these exception, it does not exist and has disappeared.
            return true;
        }
    }

    /**
     * Waits until the element is not visible.
     *
     * @param element the By selector
     * @return returns true if the element is not visible
     */
    public boolean untilElementDisappears(WebElement element) {
        return untilElementDisappears(element);
    }

    /**
     * Waits until the element is visible.
     *
     * @param element the By selector
     * @param maxTimeout max timeout
     * @param pollingTimeout polling time
     * @return returns true if the element is visible
     */
  /*  public boolean untilElementDisappears(WebElement element, int maxTimeout, int pollingTimeout) {
        FluentWait<WebElement> fluentWait = this.getNewElementFluentWait(element, maxTimeout, pollingTimeout);

        Function<WebElement, Boolean> function = obj -> {
            try {
                return !obj.isDisplayed();
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                // Do not throw these exceptions here. Instead return true as the element has disappeared.
                return true;
            }
        };

        return fluentWait.until(function);
    }*/

    /**
     * Waits until the element is enabled.
     *
     * @param locator the By selector
     * @return returns true if the element is enabled, else false
     */
    public boolean untilElementIsEnabled(By locator) {
        return untilElementIsEnabled(this.getDriver().findElement(locator));
    }

    /**
     * Waits until the element is enabled.
     *
     * @param element the web element
     * @return returns true if the element is enabled, else false
     */
    public boolean untilElementIsEnabled(WebElement element) {
        return untilElementIsEnabled(element);
    }

    /**
     * Waits until the element is enabled.
     *
     * @param element the web element
     * @param maxTimeout max timeout
     * @param pollingTimeout polling time
     * @return returns true if the element is enabled, else false
     */
  /*  public boolean untilElementIsEnabled(WebElement element, int maxTimeout, int pollingTimeout) {
        FluentWait<WebElement> fluentWait = this.getNewElementFluentWait(element, maxTimeout, pollingTimeout);

        Function<WebElement, Boolean> function = obj -> {
            try {
                return obj.isEnabled();
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                // Do not throw these exceptions here. Instead return false and let the fluentwait try again.
                return false;
            }
        };
        return fluentWait.until(function);
    }*/

    /**
     * Waits until the element is disabled.
     *
     * @param locator the By selector
     * @return returns true if the element is disabled, else false
     */
    public boolean untilElementIsDisabled(By locator) {
        return untilElementIsDisabled(this.getDriver().findElement(locator));
    }

    /**
     * Waits until the element is disabled.
     *
     * @param element the web element
     * @return returns true if the element is disabled, else false
     */
    public boolean untilElementIsDisabled(WebElement element) {
        return untilElementIsDisabled(element, DEFAULT_TIMEOUT, WAIT_POLL_TIMEOUT);
    }

    /**
     * Waits until the element is disabled.
     *
     * @param element the web element
     * @param maxTimeout max timeout
     * @param pollingTimeout polling time
     * @return returns true if the element is disabled, else false
     */
    public boolean untilElementIsDisabled(WebElement element, int maxTimeout, int pollingTimeout) {
        FluentWait<WebElement> fluentWait = this.getNewElementFluentWait(element, maxTimeout, pollingTimeout);
        return fluentWait.until(obj -> !obj.isEnabled());
    }

    public Boolean isDisplayed(By locator, int maxWaitTime){
        try{
            waitForWebElement(ExpectedConditions.visibilityOfElementLocated(locator), maxWaitTime);
        }catch(NoSuchElementException | TimeoutException e){
            return false;
        }
        return true;
    }

    public Boolean isPresent(By locator, int maxWaitTime){
        try{
            waitForWebElement(ExpectedConditions.presenceOfElementLocated(locator), maxWaitTime);
        }catch(TimeoutException e){
            return false;
        }
        return true;
    }



}
