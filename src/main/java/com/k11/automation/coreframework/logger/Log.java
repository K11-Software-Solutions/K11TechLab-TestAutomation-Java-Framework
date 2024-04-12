package com.k11.automation.coreframework.logger;

import org.apache.log4j.*;
import com.k11.automation.selenium.baseclasses.ApplicationProperties;
import org.testng.Reporter;

import java.io.IOException;

/**
 * contains all the methods to show the logs on console and save the logs in
 * LogFile.txt of each run.
 */
public class Log {

    public static final Logger LOGGER = Logger.getLogger("logger");
    private static PatternLayout layout = new PatternLayout("%d{dd MMM yyyy HH:mm:ss} %5p %c{1} - %m%n");
    private static FileAppender appender;
    private static ConsoleAppender consoleAppender;
    static	{
        try {
            consoleAppender = new ConsoleAppender(layout, "System.out");
            appender= new FileAppender(layout, ApplicationProperties.REPORT_DIR.getStringVal()+ ApplicationProperties.REPORT_LOG_FILE_NAME.getStringVal(),true);
        }
        catch (IOException exception) {
            exception.printStackTrace();

        }
    }

    public static void setConsoleAppender(){
        consoleAppender.setName("Console");
        LOGGER.addAppender(consoleAppender);
        LOGGER.addAppender(appender);
    }
    public static void info(String message){
        setConsoleAppender();
        LOGGER.setLevel((Level) Level.INFO);
        LOGGER.info(message);
    }

    public static void info(String message, boolean logToReport){
        info(message);
        if(logToReport)
            Reporter.log(message);
    }

    public static void error(String message){
        setConsoleAppender();
        LOGGER.setLevel((Level) Level.ERROR);
        LOGGER.info(message);
    }
    public static void debug(String message){
        setConsoleAppender();
        LOGGER.setLevel((Level) Level.DEBUG);
        LOGGER.info(message);
    }
}
