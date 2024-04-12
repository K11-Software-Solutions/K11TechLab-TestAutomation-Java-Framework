package com.k11.automation.coreframework.util.locatorUtil;

import com.k11.automation.coreframework.exceptions.AutomationError;
import org.openqa.selenium.By;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ObjectMap {
    Properties prop;

    public ObjectMap(String strPath) {
        prop = new Properties();

        try {
            FileInputStream fis = new FileInputStream(strPath);
            prop.load(fis);
            fis.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public By getLocator(String strElement) {
        try {
            // retrieve the specified object from the object list
            String locator = prop.getProperty(strElement);

            // extract the locator type and value from the object
            String locatorType = locator.split(":=")[0];
            String locatorValue = locator.split(":=")[1];

            // for testing and debugging purposes
            System.out.println("Retrieving object of type '" + locatorType + "' and value '" + locatorValue + "' from the object map");

            // return a instance of the By class based on the type of the locator
            // this By can be used by the browser object in the actual test
            if (locatorType.toLowerCase().equals("id"))
                return By.id(locatorValue);
            else if (locatorType.toLowerCase().equals("name"))
                return By.name(locatorValue);
            else if ((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class")))
                return By.className(locatorValue);
            else if ((locatorType.toLowerCase().equals("tagname")) || (locatorType.toLowerCase().equals("tag")))
                return By.className(locatorValue);
            else if ((locatorType.toLowerCase().equals("linktext")) || (locatorType.toLowerCase().equals("link")))
                return By.linkText(locatorValue);
            else if (locatorType.toLowerCase().equals("partiallinktext"))
                return By.partialLinkText(locatorValue);
            else if ((locatorType.toLowerCase().equals("cssselector")) || (locatorType.toLowerCase().equals("css")))
                return By.cssSelector(locatorValue);
            else if (locatorType.toLowerCase().equals("xpath"))
                return By.xpath(locatorValue);
            else
                throw new Exception("Unknown locator type '" + locatorType + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new AutomationError("Unable to get the By locator from the specified locator string.");
    }

    public static String getXPathLoc(String id) {
        return "//*[@id='" + id + "']";
    }

    public static String getCssLoc(String id) {
        return "css=*#" + id;
    }
}
