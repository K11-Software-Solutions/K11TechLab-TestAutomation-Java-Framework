package com.k11.automation.webservices.baseclasses;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import com.k11.automation.selenium.baseclasses.BaseTestCase;
import com.k11.automation.coreframework.logger.Log;
import com.k11.automation.selenium.baseclasses.ApplicationProperties;
import com.k11.automation.webservices.baseclasses.filters.ParallelRequestFilter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;

/**
 * Web Service Related Base Test Setup.
 */
public class BaseWebServiceTest implements BaseTestCase {

    /**
     * Map of method to thread id for parallel execution.
     */
    public static final ThreadLocal<HashMap<Long, String>> METHOD_THREAD = ThreadLocal.withInitial(HashMap::new);


    /**
     * Runs before all tests.
     *
     * @param webServiceLog turns on logging
     */
    @Parameters({
        "webServiceLog"})
    @BeforeTest(alwaysRun = true, enabled = true)
    public void testSetup(@Optional String webServiceLog) {
        RestAssured.reset();

        int clientTimeout = ApplicationProperties.WEB_SERVICE_TIMEOUT.getIntVal();
        if (clientTimeout > 0) {
            Log.LOGGER.info("Setting web service timeout = " + clientTimeout + "ms");
            RestAssured.config = RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig().
                    setParam("http.connection.timeout", clientTimeout).
                    setParam("http.socket.timeout", clientTimeout).
                    setParam("http.connection-manager.timeout", clientTimeout));
        }

        RestAssured.filters(new ParallelRequestFilter(isLogEnabled(webServiceLog)));
    }

    /**
     * Method executed before methods.
     *
     * @param method The method executed
     * @param webServiceUri sets the web service base uri
     * @param webServicePort sets the web service port
     * @param webServicePath sets the web service path
     */
    @Parameters({
        "webServiceUri",
        "webServicePort",
        "webServicePath"})
    @BeforeMethod
    public void beforeMethod(Method method,
            @Optional String webServiceUri,
            @Optional String webServicePort,
            @Optional String webServicePath) {
        Log.LOGGER.info("Running the test method: " + method.getName());

        String finalBaseUri = getWebServiceUri(webServiceUri, method.getName());
        String finalBasePath = getWebServicePath(webServicePath, method.getName());
        int finalPort = getWebServicePort(webServicePort, method.getName());

        RestAssured.baseURI = finalBaseUri;
        RestAssured.basePath = finalBasePath;
        RestAssured.port = finalPort;

        long threadId = Thread.currentThread().getId();
        METHOD_THREAD.get().put(threadId, method.getName());
        Log.LOGGER.info(MessageFormat.format("Put Thread = {0} the test method = {1}", threadId, method.getName()));
    }

    /**
     * Sets loggers.
     *
     * @param webServiceLogOnFailOnly turns on logging only on failure
     */
    public void setLoggers(String webServiceLogOnFailOnly) {
        if ((webServiceLogOnFailOnly != null && webServiceLogOnFailOnly.equalsIgnoreCase("true")) || ApplicationProperties.WEB_SERVICE_LOG_ON_FAIL_ONLY.getBooleanVal()) {
            Log.LOGGER.info("Setting web service log on error only");
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        }
    }

    /**
     * Gets the web service path.
     *
     * @param webServiceUri the initial path
     * @return the final path value
     */
    public String getWebServiceUri(String webServiceUri, String method) {
        String webServiceUrlProperty = "web.service.uri";
        String baseUri = ApplicationProperties.WEB_SERVICE_URI.getStringVal();
        String finalBaseUri = "";

        if (webServiceUri != null && !webServiceUri.isEmpty()) {
            finalBaseUri = webServiceUri;
        } else if (System.getProperty(webServiceUrlProperty) != null && !System.getProperty(webServiceUrlProperty).isEmpty()) {
            finalBaseUri = System.getProperty(webServiceUrlProperty);
        } else if (!baseUri.isEmpty()) {
            finalBaseUri = baseUri;
        }

        Log.LOGGER.info(MessageFormat.format("Set web service uri = {0} for method {1}", finalBaseUri, method));
        return finalBaseUri;
    }

    /**
     * Gets the web service path
     *
     * @param webServicePath The initial path
     * @return The final path
     */
    public String getWebServicePath(String webServicePath, String method) {
        String webServicePathProperty = "web.service.path";
        String basePath = ApplicationProperties.WEB_SERVICE_PATH.getStringVal();
        String finalBasePath = "";

        if (webServicePath != null && !webServicePath.isEmpty()) {
            finalBasePath = webServicePath;
        } else if (System.getProperty(webServicePathProperty) != null && !System.getProperty(webServicePathProperty).isEmpty()) {
            finalBasePath = System.getProperty(webServicePathProperty);
        } else if (!basePath.isEmpty()) {
            finalBasePath = basePath;
        }
        Log.LOGGER.info(MessageFormat.format("Set web service path = {0} for method {1}", finalBasePath, method));
        return finalBasePath;
    }

    /**
     * Gets the web service port.
     *
     * @param webServicePort The initial port value
     * @return the final port value
     */
    public int getWebServicePort(String webServicePort, String method) {
        String webServicePortProperty = "web.service.port";
        int port = ApplicationProperties.WEB_SERVICE_PORT.getIntVal();
        int finalPort = 0;

        if (webServicePort != null && !webServicePort.isEmpty() && Integer.parseInt(webServicePort) >= 0) {
            finalPort = Integer.parseInt(webServicePort);
        } else if (System.getProperty(webServicePortProperty) != null && !System.getProperty(webServicePortProperty).isEmpty()) {
            finalPort = Integer.parseInt(System.getProperty(webServicePortProperty));
        } else if (port != 0) {
            finalPort = port;
        }
        Log.LOGGER.info(MessageFormat.format("Set web service port = {0} for method {1}", finalPort, method));
        return finalPort;
    }

    /**
     * Returns if logging should be enabled.
     *
     * @param log the optional log parameter
     * @return returns true if logging is enabled.
     */
    public boolean isLogEnabled(String log) {
        boolean logApplication = ApplicationProperties.WEB_SERVICE_LOG.getBooleanVal();
        String webServiceLogSystem = "web.service.uri";
        if (log != null) {
            return log.equalsIgnoreCase("true");
        } else if (System.getProperty(webServiceLogSystem) != null && !System.getProperty(webServiceLogSystem).isEmpty()) {
            return System.getProperty(webServiceLogSystem).equalsIgnoreCase("true");
        }

        return logApplication;
    }
}
