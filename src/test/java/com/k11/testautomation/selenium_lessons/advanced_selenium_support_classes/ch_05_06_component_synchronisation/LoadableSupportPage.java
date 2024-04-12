package com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_05_06_component_synchronisation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoadableSupportPage extends LoadableComponent {
    private final WebDriverWait wait;
    private final WebDriver driver;

    @FindBy(how = How.ID, using="select-menu")
    private WebElement selectMenu;

    @FindBy(how = How.ID, using="resend-select")
    private WebElement resendSelect;

    @FindBy(how = How.ID, using="message")
    private WebElement message;

    public LoadableSupportPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void select(final String singleOptionText) {
        final Select select = new Select(selectMenu);
        select.selectByVisibleText(singleOptionText);
    }

    public String getMessage() {
        wait.until(ExpectedConditions.visibilityOf(message));
        wait.until(ExpectedConditions.textToBePresentInElement(message, "Received"));
        return message.getText();
    }

    public MessageHistoryComponent messageHistory() {
        return new MessageHistoryComponent(driver);
    }


    @Override
    protected void load() {
        driver.get("https://eviltester.github.io/supportclasses/#_2000");
    }

    @Override
    protected void isLoaded() throws Error {

        boolean ready=false;

        try {
            boolean hasTitleLoaded = driver.getTitle().equals("Support Classes Example");
            boolean hasButtonLoaded = resendSelect.isEnabled();
            ready = hasTitleLoaded && hasButtonLoaded;

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!ready){
            throw new Error("Page has not loaded");
        }
    }
}
