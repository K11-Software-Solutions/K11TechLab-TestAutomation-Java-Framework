package com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_05_05_page_synchronisation;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class MessageHistoryComponent {

    private final WebDriver driver;

    @FindBy(how = How.CSS, using="#multi-list li")
    List<WebElement>multiMessages;

    @FindBy(how = How.CSS, using="#single-list li")
    List<WebElement>singleMessages;

    public MessageHistoryComponent(final WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public int countSingleHistoryMessages() {
        return singleMessages.size();
    }


    public String getSingleHistoryMessage(final int index) {
        if(singleMessages.size()>index){
            return singleMessages.get(index).getText();
        }
        return "";
    }

    public void waitTillReady() {

        Duration timeOutInSeconds = Duration.ofSeconds(10);
        Clock clock = Clock.systemDefaultZone();
        Instant end = clock.instant().plus(timeOutInSeconds);

        while (clock.instant().isBefore(end)) {
            if(singleMessages.size() >0 || multiMessages.size() >0){
                return;
            }
        }

        throw new TimeoutException("Component not ready");

    }
}
