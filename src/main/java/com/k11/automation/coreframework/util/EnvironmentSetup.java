package com.k11.automation.coreframework.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Map;
import java.util.Properties;
import com.k11.automation.coreframework.logger.Log;

/**
 * Class to support accessing environment properties.
 */
public final class EnvironmentSetup {

    /**
     * Private constructor for utility class.
     */
    private EnvironmentSetup() { }

    /**
     * Gets the operating system name.
     */
    private static final String OSNAME = System.getProperty("os.name");

    /**
     * Gets the operating system architecture.
     */
    public static final String OSARCHITECTURE = System.getProperty("os.arch");

    /**
     * Gets the operating system version.
     */
    public static final String OSVERSION = System.getProperty("os.version");

    /**
     * Sets up the systemconfig.properties file with environment properties.
     */
    public static void environmentSetup() {
        try {
            Properties properties = new Properties();
            properties.setProperty("OS", OSNAME);
            properties.setProperty("OS Architecture", OSARCHITECTURE);
            properties.setProperty("OS Version", OSVERSION);
            properties.setProperty("Java Version", Runtime.class.getPackage().getImplementationVersion());
            properties.setProperty("Host Name", InetAddress.getLocalHost().getHostName());
            properties.setProperty("Host IP Address", InetAddress.getLocalHost().getHostAddress());

            try {
                File file = new File("./resources/systemconfig.properties");
                FileOutputStream fileOut = new FileOutputStream(file);
                properties.store(fileOut, "Environment Setup");
                fileOut.close();
            } catch (Exception e) {
                Log.LOGGER.error("Error in setting the systemconfig properties. Exception = " + e.getMessage());
            }

            System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
            System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
            System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
            System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all available environment variables.
     */
    public static void getEnvironmentVariables() {
        Map<String, String> env = System.getenv();
        for (String key : env.keySet()) {
            System.out.println(key + ":" + env.get(key));
        }
    }

    /**
     * Gets an environment variable.
     * @param key the environment variable to get
     * @return the value of the key or empty string if the key does not exists
     */
    public static String getEnvValue(String key) {
        String value = "";
        try {
            Properties p = getEnvVars();
            value = p.getProperty(key);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Gets all available system properties.
     * @return Properties available for this environment
     * @throws Throwable error
     */
    public static Properties getEnvVars() throws Throwable {
        Process p = null;
        Properties envVars = new Properties();
        Runtime r = Runtime.getRuntime();
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("windows 9") > -1) {
            p = r.exec("command.com /c set");
        } else if ((os.indexOf("nt") > -1) || (os.indexOf("windows 2000") > -1)
                || (os.indexOf("windows xp") > -1)) {
            p = r.exec("cmd.exe /c set");
        } else if ((os.indexOf("win") >= 0)) {
            p = r.exec("cmd.exe /c set");
        } else {
            // it is Unix os.
            p = r.exec("env");
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(p
                .getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            int idx = line.indexOf('=');
            String key = line.substring(0, idx);
            String value = line.substring(idx + 1);
            envVars.setProperty(key, value);
        }
        return envVars;
    }
}
