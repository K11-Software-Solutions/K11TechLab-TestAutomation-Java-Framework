package com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_05_03_locator_strategies;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;


public class SupportPage extends PageFactory {
    private final WebDriver driver;

    @FindBy(how = How.ID, using = "resend-select")
    public WebElement singleResendButton;

    @FindBy(how = How.CSS, using = "#message")
    public WebElement message;

    public SupportPage(WebDriver driver){

        this.driver = driver;
        //initElements(new AjaxElementLocatorFactory(driver, 10),
        //        this);
        initElements(new VisibleAjaxElementFactory(driver, 10),
                this);

    }


    public String waitForMessage(){


        return message.getText();
    }
}
