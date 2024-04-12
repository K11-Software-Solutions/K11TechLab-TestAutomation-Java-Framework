package com.k11.automation.coreframework.k11CustomDataProvider;

import java.io.InputStream;

/**
 * This interface declare the prototype for data source which will be used in DataProvider Impl.
 */
public interface DataResource {

    /**
     * Load the input stream of the data file
     *
     * @return The inputStream
     */
    InputStream getInputStream();

    /**
     * Fetch the user defined POJO class
     *
     * @return The class
     */
    Class<?> getCls();

    /**
     * Set the user defined POJO class to map data
     *
     * @param cls
     */
    void setCls(Class<?> cls);

    /**
     * Fetch the data file extension
     *
     * @return The type
     */
    String getType();
}