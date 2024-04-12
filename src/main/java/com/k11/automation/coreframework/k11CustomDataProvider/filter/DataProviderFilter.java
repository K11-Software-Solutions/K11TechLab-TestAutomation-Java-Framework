package com.k11.automation.coreframework.k11CustomDataProvider.filter;

/**
 * This Interface provides the facility to provide the custom filtering logic based on the needs. user can create the
 * custom filter class by implementing this filter and invoke the filter based methods to apply the filter. <br>
 * <br>
 * Example dataproviderfilter:
 * 
 * <pre>
 * public class SimpleIndexInclusionDataProviderFilter implements IDataProviderFilter {
 * 
 *     &#064;Override
 *     public Object[][] filter(Object[][] data, String... args) {
 *         // filtering logic here
 *         return filteredData;
 *     }
 * 
 * }
 * </pre>
 */
public interface DataProviderFilter {

    /**
     * This function identifies whether the given object falls in the injected filter criteria.
     * 
     * @param data
     *            Object the object to be filtered.
     * @return boolean - true if object falls in the filter criteria.
     */
    boolean filter(Object data);
}