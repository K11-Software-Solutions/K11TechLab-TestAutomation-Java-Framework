package com.k11.automation.selenium.baseclasses;

import com.k11.automation.coreframework.logger.Log;
import com.k11.automation.coreframework.util.WaitUtil;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * Base page model class.
 */
public class BaseTestPage {

    /**
     * WebDriver for this page.
     */
    protected WebDriver driver;

    /**
     * Selenium wait.
     */
    protected SeleniumWait wait;

    /**
     * Default Timeout.
     */
    private static final int DEFAULT_TIMEOUT = ApplicationProperties.DEFAULT_TIMEOUT.getIntVal();

    /**
     * Default time to wait before checking again.
     */
    private static final int WAIT_POLL_TIMEOUT = ApplicationProperties.WAIT_POLL_TIMEOUT.getIntVal();

    /**
     * Default retry count for various actions.
     */
    private static final int DEFAULT_RETRY_ACTIONS_CNT = ApplicationProperties.DEFAULT_RETRY_ACTIONS_CNT.getIntVal();

    /**
     * Creates BaseTestPage.
     * @param driver the web driver object
     */
    public BaseTestPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new SeleniumWait(this.driver);
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
     * Gets the SeleniumWait class.
     *
     * @return returns the SeleniumWait class
     */
    public SeleniumWait getSeleniumWait() {
        return this.wait;
    }

    /**
     * Gets the javascript webdriver.
     *
     * @return returns the webdriver
     */
    public JavascriptExecutor getJavaScriptDriver() {
        return (JavascriptExecutor) driver;
    }

    /**
     * Sets the SeleniumWait Class.
     *
     * @param seleniumWait the waitdriver
     */
    public void setSeleniumWait(SeleniumWait seleniumWait) {
        this.wait = seleniumWait;
    }

    /**
     * Gets the page title.
     *
     * @return returns the page title
     */
    public String getPageTitle() {
        return getDriver().getTitle();
    }

    /**
     * Navigates to the url. synonym to driver.navigate().to(url)
     *
     * @param url the url to navigate to
     */
    public void open(String url) {
        getDriver().get(url);
    }

    /**
     * Checks if element is present.
     *
     * @param locator element By selector
     * @return Returns true if element is present
     */
    public Boolean isElementPresent(By locator) {
        return !getDriver().findElements(locator).isEmpty();
    }

    /**
     * Checks if element is displayed.
     *
     * @param locator the element By selector
     * @return returns true if the element is visible
     */
    public Boolean isDisplayed(By locator) {
        try {
            return isDisplayed(this.driver.findElement(locator));
        } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException e) {
            return false;
        }
    }

    /**
     * Checks if element is displayed.
     *
     * @param element the element By selector
     * @return returns true if the element is visible
     */
    public Boolean isDisplayed(WebElement element) {
        return element.isDisplayed();
    }

    /**
     * Checks if the element is absent from the page.
     *
     * @param locator the element By selector
     * @return returns true if element is absent, else false.
     */
    public ExpectedCondition<Boolean> isAbsent(final By locator) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {

                try {
                    webDriver.findElement(locator);
                    return false;
                } catch (NoSuchElementException | StaleElementReferenceException e) {
                    return true;
                }
            }

            @Override
            public String toString() {
                return "element not present: " + locator;
            }
        };
    }

    /**
     * Checks if the element is absent from the page.
     *
     * @param element the element
     * @return returns true if element is absent, else false.
     */
    public ExpectedCondition<Boolean> isAbsent(final WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                try {
                    element.isDisplayed();
                    return false;
                } catch (NoSuchElementException | StaleElementReferenceException e) {
                    return true;
                }
            }

            @Override
            public String toString() {
                return "element not present: " + element.toString();
            }
        };
    }

    /**
     * Checks if the element is clickable.
     *
     * @param locator the element By selector
     * @return returns true if the element is clickable
     */
    public boolean isClickable(final By locator) {
        return this.isClickable(getDriver().findElement(locator));
    }

    /**
     * Checks if the element is clickable.
     *
     * @param element the element By selector
     * @return returns true if the element is clickable
     */
    public boolean isClickable(final WebElement element) {
        if (element == null) {
            return false;
        } else {
            //If not visible, element isn't clickable
            if (!element.isDisplayed()) {
                return false;
            } else {
                if (element.getSize().getHeight() <= 0
                        || element.getSize().getWidth() <= 0) { // If width or height is 0, element is not clickable
                    return false;
                } else {
                    if (!element.isEnabled()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Waits for the element to be clickable and clicks the element.
     *
     * @param element the web element
     */
    public void clickElement(WebElement element) {
        wait.waitForElementToBeClickable(element);
        Log.LOGGER.info("Clicking on Element: " + element.toString());
        element.click();
    }

    /**
     * Waits for the element to be clickable and clicks the element.
     *
     * @param locator the element By selector
     */
    public void clickElement(final By locator) {
        clickElement(this.driver.findElement(locator));
    }

    /**
     * Waits for the element to be clickable and clicks the element.
     *
     * @param element the element
     * @param attempts the number of attempts to click the element
     */
    public void clickElementWithAttemps(WebElement element, int attempts) {
        int totalAttemps = attempts > 0 ? attempts : DEFAULT_RETRY_ACTIONS_CNT;
        int iAttempts = 0;
        while (iAttempts < totalAttemps) {
            try {
                clickElement(element);
                break;
            } catch (Exception e) {
                Log.LOGGER.error(MessageFormat.format("Error attempting to click on the element: = {0}. Retrying Click {1}", element, iAttempts));
            }
            iAttempts++;
        }
    }

    /**
     * Waits for the element to be clickable and clicks the element.
     *
     * @param locator the element By locator
     * @param attempts the number of attempts to click the element
     */
    public void clickElementWithAttemps(final By locator, int attempts) {
        clickElementWithAttemps(driver.findElement(locator), attempts);
    }

    /**
     * Clicks element when visible.
     *
     * @param element the element
     */
    public void clickWhenVisible(WebElement element) {
        int counter = 0;
        boolean retry;
        do {
            try {
                retry = false;
                wait.waitForElementToBeVisible(element);
                element.click();
            } catch (WebDriverException ex) {
                if (counter < DEFAULT_RETRY_ACTIONS_CNT) {
                    counter++;
                    Log.LOGGER.info("Retry number " + counter);
                    retry = true;
                } else {
                    Log.LOGGER.debug("Unable to click on the element: " + element.toString());
                    throw ex;
                }
            }
        } while (retry);
    }

    /**
     * Clicks element when visible.
     *
     * @param locator the element By selector
     */
    public void clickWhenVisible(By locator) {
        int counter = 0;
        boolean retry;
        do {
            try {
                retry = false;
                WebElement element = wait.waitForElementToBeVisible(locator);
                element.click();
            } catch (WebDriverException ex) {
                if (counter < DEFAULT_RETRY_ACTIONS_CNT) {
                    counter++;
                    Log.LOGGER.info("Retry number " + counter);
                    retry = true;
                } else {
                    Log.LOGGER.debug("Unable to click on the element: " + locator.toString());
                    throw ex;
                }
            }
        } while (retry);
    }

    /**
     * Clicks on the webelement using javascript.
     *
     * @param locator the element By selector
     */
    public void clickOnElementUsingJs(By locator) {
        this.clickOnElementUsingJs(this.getDriver().findElement(locator));
    }

    /**
     * Clicks on the webelement using javascript.
     *
     * @param element the web element
     */
    public void clickOnElementUsingJs(WebElement element) {
        this.getJavaScriptDriver().executeScript("arguments[0].click();", element);
    }

    /**
     * Clicks on the webelement using Selenium Actions.
     *
     * @param locator the element By selector
     */
    public void clickOnElementUsingAction(By locator) {
        this.clickOnElementUsingAction(this.getDriver().findElement(locator));
    }

    /**
     * Clicks on the webelement using Selenium Actions.
     *
     * @param element the element
     */
    public void clickOnElementUsingAction(WebElement element) {
        Actions builder = new Actions(getDriver());
        try {
            Action clickElement = builder.moveToElement(element).click().build();
            clickElement.perform();
        } catch (Exception e) {
            try {
                Action clickElement = builder.moveToElement(element).doubleClick().build();
                clickElement.perform();
            } catch (Exception e1) {
                Log.LOGGER.error("Unable to click on the element using Action. Exception = " + e1.getStackTrace());
                throw e1;
            }
        }
    }

    /**
     * Double clicks the element using Selenium Actions.
     *
     * @param locator the element By selector
     */
    public void doubleClickOnElementUsingAction(By locator) {
        this.doubleClickOnElementUsingAction(this.getDriver().findElement(locator));
    }

    /**
     * Double clicks the element using Selenium Actions.
     *
     * @param element the element
     */
    public void doubleClickOnElementUsingAction(WebElement element) {
        Actions builder = new Actions(driver);
        Action clickElement = builder.moveToElement(element).doubleClick().build();
        clickElement.perform();
    }




    /**
     * Clicks the alert popup and waits for popup to appear.
     *
     * @param locator the element By selector
     */
    public void clickThenWaitForPopupToAppear(By locator) {
        this.clickThenWaitForPopupToAppear(this.getDriver().findElement(locator));
    }

    /**
     * Clicks the alert popup and waits for popup to appear. Switches to alert.
     *
     * @param element the element
     */
    public void clickThenWaitForPopupToAppear(WebElement element) {
        long timeout = System.currentTimeMillis() + DEFAULT_TIMEOUT;
        boolean foundPopup = false;
        while (!driver.getWindowHandles().isEmpty() && System.currentTimeMillis() < timeout) {
            // loops for DEFAULT_TIMEOUT
            String mainWindowHandle = driver.getWindowHandle();
            clickElement(element);
            int retryCount = 0;
            while (retryCount < DEFAULT_RETRY_ACTIONS_CNT && !foundPopup) {
                for (String winHandle : driver.getWindowHandles()) {
                    if (!winHandle.equals(mainWindowHandle)) {
                        driver.switchTo().window(winHandle);
                        foundPopup = true;
                        break;
                    }
                }
                WaitUtil.waitSeconds(WAIT_POLL_TIMEOUT);
                retryCount++;
            }
            if (foundPopup) {
                break;
            }
        }

        if (!foundPopup) {
            throw new NotFoundException("popup not found");
        }
    }

    /**
     * Type the string value into the locator input field using Selenium's
     * SendKeys Function.
     *
     * @param locator the element By selector for the input field
     * @param value the value to type
     */
    public void enterValue(By locator, String value) {
        WebElement element = wait.waitForElementToBeClickable(locator);
        this.enterValue(element, value);
    }

    /**
     * Type the string value into the element field using Selenium's SendKeys
     * Function.
     *
     * @param element the element for the input field
     * @param value the value to type
     */
    public void enterValue(WebElement element, String value) {
        wait.waitForElementToBeClickable(element);
        element.clear();
        element.sendKeys(value);
        String attributeValue = getAttributeValue(element);
        Log.LOGGER.info("Entered value: " + attributeValue);
        if (!attributeValue.equals(value)) {
            typeInField(element, value);
        }
    }

    /**
     * Types each character into the input field using Selenium's SendKeys
     * Function.
     *
     * @param locator the element By selector for the input field
     * @param value the value to type
     */
    public void typeInField(By locator, String value) {
        WebElement element = this.getDriver().findElement(locator);
        String val = value;
        element.clear();
        for (int i = 0; i < val.length(); i++) {
            char c = val.charAt(i);
            String s = new StringBuilder().append(c).toString();
            element.sendKeys(s);
        }
        Log.LOGGER.info("Entered text: " + getAttributeValue(element));
    }

    /**
     * Types each character into the input field using Selenium's SendKeys
     * Function.
     *
     * @param element the element for the input field
     * @param value the value to type
     */
    public void typeInField(WebElement element, String value) {
        wait.waitForElementToBeVisible(element);
        String val = value;
        element.clear();
        for (int i = 0; i < val.length(); i++) {
            char c = val.charAt(i);
            String s = new StringBuilder().append(c).toString();
            element.sendKeys(s);
        }
        Log.LOGGER.info("Entered value: " + getAttributeValue(element));
    }

    /**
     * Gets the elements attribute "value".
     *
     * @param element The web element.
     * @return The attribute value.
     */
    public String getAttributeValue(WebElement element) {
        return element.getAttribute("value");
    }

    /**
     * Sets the attribute of the element.
     *
     * @param locator the element By selector
     * @param attName the attribute name to set
     * @param attValue the attribute value to set
     */
    public void setAttribute(By locator, String attName, String attValue) {
        this.setAttribute(this.driver.findElement(locator), attName, attValue);
    }

    /**
     * Sets the attribute of the element.
     *
     * @param element the element
     * @param attName the attribute name to set
     * @param attValue the attribute value to set
     */
    public void setAttribute(WebElement element, String attName, String attValue) {
        this.getJavaScriptDriver().executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);",
                element, attName, attValue);
    }

    /**
     * Scrolls element into viewport.
     *
     * @param locator the element By selector
     */
    public void scrollIntoViewElementUsingJs(By locator) {
        this.scrollIntoViewElementUsingJs(this.getDriver().findElement(locator));
    }

    /**
     * Scrolls element into viewport.
     *
     * @param element the element
     */
    public void scrollIntoViewElementUsingJs(WebElement element) {
        this.getJavaScriptDriver().executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Scroll element into view using coordinates.
     *
     * @param locator the element By selector
     */
    public void scrollIntoView(By locator) {
        this.scrollIntoView(this.getDriver().findElement(locator));
    }

    /**
     * Scroll element into view using coordinates.
     *
     * @param element the element
     */
    public void scrollIntoView(WebElement element) {
        int scrollHeight = getWindowInnerHeight();
        int y = Math.max(0, element.getLocation().getY() - scrollHeight / 2); //Subtract half the window height so its in the middle of the viewable area.
        executeJavascript(format("window.scrollTo(%d, %d)", 0, y));
    }

    /**
     * Scrolls down the page window.
     *
     * @param pixels the number of pixels to scroll down
     */
    public void scrollDown(int pixels) {
        String scrollBy = String.format("window.scrollBy(0,%s)", pixels);
        executeJavascript(scrollBy);
    }

    /**
     * Executes standalone javascript.
     *
     * @param script The javascript script to execute
     * @return returns the object after execution
     */
    public Object executeJavascript(String script) {
        Log.LOGGER.info("Executing javascript: '{}'" + script);
        return this.getJavaScriptDriver().executeScript(script);
    }

    /**
     * Refreshes the Iframe.
     *
     * @param iFrameId the id of the Iframe
     */
    public void refreshIFrameByJavaScriptExecutor(String iFrameId) {
        String script = MessageFormat.format("document.getElementById('{0}').src = document.getElementById('{0}').src", iFrameId);
        this.getJavaScriptDriver().executeScript(script);
    }

    /**
     * Selects the visible text from a drown down menu.
     *
     * @param locator the dropdown By selector
     * @param visibleText The text option to select
     */
    public void selectDropDownOptionByVisibleText(By locator, String visibleText) {
        this.selectDropDownOptionByVisibleText(wait.waitForElementToBeVisible(locator), visibleText);
    }

    /**
     * Selects the visible text from a drown down menu.
     *
     * @param element the dropdown element
     * @param visibleText The text option to select
     */
    public void selectDropDownOptionByVisibleText(WebElement element, String visibleText) {
        Select selectDropDown = getSelectDropDownElement(element);
        selectDropDown.selectByVisibleText(visibleText);
        Log.LOGGER.info("Selected Dropdown Option by Text: " + visibleText);
    }

    /**
     * Selects the value from a drown down menu.
     *
     * @param locator the dropdown By selector
     * @param value The value to select
     */
    public void selectDropDownOptionByValue(By locator, String value) {
        this.selectDropDownOptionByValue(wait.waitForElementToBeVisible(locator), value);
    }

    /**
     * Selects the value from a drown down menu.
     *
     * @param element the dropdown element
     * @param value The value to select
     */
    public void selectDropDownOptionByValue(WebElement element, String value) {
        Select selectDropDown = getSelectDropDownElement(element);
        selectDropDown.selectByValue(value);
        Log.LOGGER.info("Selected Dropdown Option by Value: " + value);
    }

    /**
     * Selects the index from a drown down menu.
     *
     * @param locator the dropdown By selector
     * @param index The index option to select
     */
    public void selectDropDownOptionByIndex(By locator, int index) {
        this.selectDropDownOptionByIndex(wait.waitForElementToBeVisible(locator), index);
    }

    /**
     * Selects the index from a drown down menu.
     *
     * @param element the dropdown By selector
     * @param index The index option to select
     */
    public void selectDropDownOptionByIndex(WebElement element, int index) {
        Select selectDropDown = getSelectDropDownElement(element);
        selectDropDown.selectByIndex(index);
        Log.LOGGER.info("Selected Dropdown Option: " + index);
    }

    /**
     * Gets the select drop down menu.
     *
     * @param locator the dropdown By selector
     * @return the select downdown element
     */
    public Select getSelectDropDownElement(By locator) {
        return this.getSelectDropDownElement(wait.waitForElementToBeClickable(locator));
    }

    /**
     * Gets the select drop down menu.
     *
     * @param element the dropdown By selector
     * @return the select downdown element
     */
    public Select getSelectDropDownElement(WebElement element) {
        wait.waitForElementToBeClickable(element);
        Select selectDropDown = new Select(element);
        WaitUtil.waitMillis(WAIT_POLL_TIMEOUT);

        return selectDropDown;
    }

    /**
     * Gets the options for a drop down menu.
     *
     * @param locator the dropdown By selector
     * @return Returns a list of the available options for the drop down
     * selector
     */
    public List<String> getDropDownOptionList(By locator) {
        wait.waitForElementToBePresent(locator);
        return getDropDownOptionList(this.driver.findElement(locator));
    }

    /**
     * Gets the options for a drop down menu.
     *
     * @param element the dropdown By selector
     * @return Returns a list of the available options for the drop down
     * selector
     */
    public List<String> getDropDownOptionList(WebElement element) {
        List<String> dropDownValues = new ArrayList<>();
        Select select = new Select(element);
        List<WebElement> options = select.getOptions();
        for (WebElement item : options) {
            dropDownValues.add(item.getText());
        }
        return dropDownValues;
    }

    /**
     * Verifys if the drop down menu contains the value, this will match using a
     * .contains comparison.
     *
     * @param locator the dropdown By selector
     * @param option The option to check
     * @return returns true if the option contains the option text
     */
    public boolean verifyDropDownOption(By locator, String option) {
        wait.waitForElementToBePresent(locator);
        return verifyDropDownOption(this.driver.findElement(locator), option);
    }

    /**
     * Verifys if the drop down menu contains the value, this will match using a
     * .contains comparison.
     *
     * @param element the dropdown element
     * @param option The option to check
     * @return returns true if the option contains the option text
     */
    public boolean verifyDropDownOption(WebElement element, String option) {
        List<String> options = getDropDownOptionList(element);
        for (String item : options) {
            if (item.contains(option)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if an alert is present.
     *
     * @return Returns true if the driver can switch to an alert
     */
    public boolean isAlertPresent() {
        return this.getDriver().switchTo().alert() != null;
    }

    /**
     * Checks if an alert is present and accepts it.
     */
    public void acceptAlertIfPresent() {
        try {
            Alert alert = this.getDriver().switchTo().alert();
            if (alert != null) {
                alert.accept();
            }
        } catch (NoAlertPresentException e) {
            Log.LOGGER.info("No Alert is present");
        }
    }

    /**
     * Switches to the tab.
     *
     * @param tabIndex The tab index to switch to
     */
    public void switchToTab(int tabIndex) {
        ArrayList<String> availableWindows = new ArrayList<>(getDriver().getWindowHandles());
        if (!availableWindows.isEmpty()) {
            driver.switchTo().window(availableWindows.get(tabIndex));
        }
    }

    /**
     * Gets the window inner height.
     *
     * @return Returns the int value of the window height
     */
    private int getWindowInnerHeight() {
        Object innerHeight = executeJavascript(
                "return window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;");
        if (!(innerHeight instanceof Long)) {
            Log.LOGGER.info("Error getting the inner height, a null value was returned from Javascript. Using outer window height.");
            return driver.manage().window().getSize().getHeight();
        }
        return ((Long) innerHeight).intValue();
    }

    /**
     * Highlights an element.
     *
     * @param locator The By element selector
     */
    public void highlightElement(By locator) {
        WebElement element = this.getDriver().findElement(locator);
        this.getJavaScriptDriver().executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "color: black; border: 3px solid black;");
    }

    /**
     * Highlights an element.
     *
     * @param locator The element By selector
     * @param duration The duration to highlight the element
     * @throws InterruptedException Throws exception if interrupted
     */
    public void highlightElement(By locator, int duration) throws InterruptedException {
        WebElement element = this.getDriver().findElement(locator);
        // Store original style so it can be reset later
        String style = "style";
        String originalStyle = element.getAttribute(style);
        this.getJavaScriptDriver().executeScript(
                "arguments[0].setAttribute(arguments[1], arguments[2])",
                element,
                style,
                "border: 2px solid red; border-style: dashed;");

        // Keep element highlighted for a spell and then revert
        if (duration > 0) {
            Thread.sleep((long) duration * WAIT_POLL_TIMEOUT);
            this.getJavaScriptDriver().executeScript(
                    "arguments[0].setAttribute(arguments[1], arguments[2])",
                    element,
                    style,
                    originalStyle);
        }
    }
}
