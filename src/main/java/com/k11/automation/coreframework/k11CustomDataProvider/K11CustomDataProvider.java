package com.k11.automation.coreframework.k11CustomDataProvider;

import com.k11.automation.coreframework.k11CustomDataProvider.filter.DataProviderFilter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;


/**
 * This interface defines prototype to implement own data provider implementation to parse the specific format data
 * file. User can create own data provider by implementing this interface.
 */
public interface K11CustomDataProvider {

    /**
     * Generates a two dimensional array for TestNG DataProvider from the data file.
     * 
     * @return A two dimensional object array
     * @throws IOException
     */
    Object[][] getAllData() throws IOException;

    /**
     * Generates an object array in iterator as TestNG DataProvider from the data filtered per {@code dataFilter}.
     * 
     * @param dataFilter
     *            an implementation class of {@link DataProviderFilter}
     * @return An iterator over a collection of Object Array to be used with TestNG DataProvider
     * @throws IOException
     */
    Iterator<Object[]> getDataByFilter(DataProviderFilter dataFilter) throws IOException;

    /**
     * Generates an object array in iterator as TestNG DataProvider from the data filtered per given indexes string.
     * This method may throw {@link DataProviderException} when an unexpected error occurs during data provision from
     * data file.
     * 
     * @param indexes
     *            The indexes for which data is to be fetched as a conforming string pattern.
     * 
     * @return Object[][] to be used with TestNG DataProvider.
     */
    Object[][] getDataByIndex(String indexes) throws IOException;

    /**
     * Generates an object array in iterator as TestNG DataProvider from the data filtered per given indexes.
     * This method may throw {@link DataProviderException} when an unexpected error occurs during data provision from
     * data file.
     *
     * @param indexes
     *            The indexes for which data is to be fetched as a conforming string pattern.
     *
     * @return Object[][] to be used with TestNG DataProvider.
     */
    Object[][] getDataByIndex(int[] indexes) throws IOException;

    /**
     * Generates a two dimensional array for TestNG DataProvider from the data representing a map of name value
     * collection filtered by keys.
     * 
     * @param keys
     *            The string keys to filter the data.
     * @return A two dimensional object array.
     */
    Object[][] getDataByKeys(String[] keys);

    /**
     * A utility method to give output data as HashTable.
     * 
     * @return The data as a {@link Hashtable}
     */
    Hashtable<String, Object> getDataAsHashtable();

}