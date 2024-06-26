package com.k11.automation.coreframework.k11CustomDataProvider.filter;

import com.k11.automation.coreframework.k11CustomDataProvider.helper.DataProviderHelper;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This filter implementation filters data based on the indexes set. Indexes can be in the format of
 * 
 * <ul>
 * <li>"1, 2, 3" for individual indexes.</li>
 * <li>"1-4, 6-8, 9-10" for ranges of indexes.</li>
 * <li>"1, 3, 5-7, 10, 12-14" for mixing individual and range of indexes.</li>
 * </ul>
 * This filter always collects data whose data index is in given filterIndexes. The indexes corresponds to the
 * invocation sequence for the test method run for each supplied data, and so is 1-based.
 * 
 */
public class SimpleIndexInclusionFilter implements DataProviderFilter {

    private int invocationCount;
    
    private final int[] indexes;

    /**
     * Initializes indexes to be included for filter using a conforming string.
     * @param filterIndexes
     *            - sets the indexes string that will be used in the filtering. It is in the format of:
     * 
     *            <ul>
     *            <li>"1, 2, 3" for individual indexes.</li>
     *            <li>"1-4, 6-8, 9-10" for ranges of indexes.</li>
     *            <li>"1, 3, 5-7, 10, 12-14" for mixing individual and range of indexes.</li>
     *            </ul>
     * 
     */
    public SimpleIndexInclusionFilter(String filterIndexes) {
        checkArgument(filterIndexes != null, "Please provide valid indexes for filtering");
        this.indexes = DataProviderHelper.parseIndexString(filterIndexes);
    }
    
    /**
     * Initializes indexes to be included for filter using an array of indexes.
     * @param indexes
     *            - sets the indexes that will be used in the filtering. It is an array of integers starting from 1.
     * 
     */
    public SimpleIndexInclusionFilter(int[] indexes) {
        checkArgument(indexes != null, "Please provide valid indexes for filtering");
        this.indexes = indexes.clone();
    }

    /**
     * This function identifies whether the object falls in the filtering criteria or not based on the indexes provided.
     * For this we are using the invocation count for comparing the index.
     * 
     * @param data
     *            the object to be filtered.
     * @return boolean - true if object falls in the filter criteria.
     */
    @Override
    public boolean filter(Object data) {
        invocationCount += 1;
        for (int index : this.indexes) {
            if (invocationCount == index) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a string representation of the indexes.
     */
    public String toString() {
        return "Filter Indexes :" + Arrays.toString(this.indexes);
    }

}