package com.k11.testautomation.selenium_lessons.advanced_selenium_support_classes.ch_06_02_usage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

public class HighLighterEventListener extends AbstractWebDriverEventListener {
    private WebElement lastElement;

    // JavaScript to set style off use element.style.border = 'none';
    // JavaScript to set style on use element.style.border='10px dashed red';
    // e.g.
    // document.getElementById("resend-select").style.border='10px dashed red';
    // JavascriptExecutor uses arguments[0] for the elements passed in as params


    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        if(lastElement!=null){
            try {
                ((JavascriptExecutor)driver).executeScript(
                        "arguments[0].style.border='none'",lastElement
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        lastElement=null;
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        lastElement = element;
        ((JavascriptExecutor)driver).executeScript(
            "arguments[0].style.border='10px dashed red'",element
        );
    }
}
