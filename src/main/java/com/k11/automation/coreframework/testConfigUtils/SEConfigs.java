package com.k11.automation.coreframework.testConfigUtils;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.aeonbits.owner.Config.HotReloadType.ASYNC;

public class SEConfigs {
    private static SEConfiguration config;

    @Config.HotReload(value = 500, unit = MILLISECONDS, type = ASYNC)
    @Config.Sources({
            "classpath:application.properties",
            "classpath:envirnment.properties",
            "classpath:service.properties"})
    public interface SEConfiguration extends Config {

        // SE utils identify keys
        @Key("default.browser")
        @DefaultValue("Chrome")
        String defaultBrowser();

        @Key("test.tag.class.size.of.testngxml")
        @DefaultValue("10")
        int testTagClassSizeOfTestNgXml();

        @Key("testing.package.names")
        @DefaultValue("")
        @Separator(",")
        List<String> testingPackageNames();

        @Key("testing.test.groups")
        @DefaultValue("")
        @Separator(",")
        List<String> testingTestGroups();

        @Key("testing.testng.classes")
        @DefaultValue("")
        @Separator(",")
        List<String> testingTestNGClasses();

        @Key("testng.listeners")
        @DefaultValue(
                "com.k11.automation.coreframework.retryAnalyzer.AnnotationTransformer, "
                + "com.k11.automation.coreframework.retryAnalyzer.RetryTestListenerAdapter, "
                + "com.github.ansonliao.selenium.parallel.SeleniumTestMethodListener")
        @Separator(",")
        List<String> testngListeners();

        @Key("testng.class.prefix")
        @DefaultValue("test")
        String testngTestClassPrefix();

        // key set to "testng.xml.browser.parameter.key" if needed
        @DefaultValue("browser")
        String testngXmlBrowserParamKey();

        @Key("testng.test.preserve.order")
        @DefaultValue("false")
        boolean testngPreserveOrder();


        // selenium grid, remote webdriver
        @Key("selenium.hub.url")
        @DefaultValue("")
        String seleniumHubUrl();

    }

    public static synchronized SEConfiguration getConfigInstance() {
        if (config == null) {
            config = ConfigFactory.create(SEConfiguration.class, System.getProperties(), System.getenv());
        }
        return config;
    }

}
