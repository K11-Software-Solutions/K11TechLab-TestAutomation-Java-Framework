package com.k11.automation.coreframework.k11CustomDataProvider.Impl;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import com.k11.automation.coreframework.k11CustomDataProvider.DataResource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import com.k11.automation.coreframework.logger.Log;
import com.k11.automation.coreframework.k11CustomDataProvider.DataProviderFactory;
import com.k11.automation.coreframework.k11CustomDataProvider.DataResource;
import com.k11.automation.coreframework.k11CustomDataProvider.K11CustomDataProvider;
import com.k11.automation.coreframework.k11CustomDataProvider.Impl.ExcelDataProviderImpl;
import com.k11.automation.coreframework.k11CustomDataProvider.Impl.YamlDataProviderImpl;
import com.k11.automation.coreframework.k11CustomDataProvider.filter.DataProviderFilter;
import com.k11.automation.coreframework.k11CustomDataProvider.helper.DataProviderHelper;
import com.k11.automation.coreframework.k11CustomDataProvider.filter.DataProviderFilter;
import com.k11.automation.coreframework.exceptions.DataProviderException;
/**
 * This class takes care of parsing the test data given in the JSON format using the GSON library. The data returned is
 * a 2D Array and there are utility methods to get specific data by index when not all data is required and convert a
 * Json String to a specific type.
 */
public final class JsonDataProviderImpl implements K11CustomDataProvider {

    private final DataResource resource;

    public JsonDataProviderImpl(DataResource resource) {
        this.resource = resource;
    }

    /**
     * Parses the JSON file as a 2D Object array for TestNg dataprovider usage.<br>
     *
     *
     * <i>Test Method Signature</i>
     *
     * {@code public void readJsonArray(TestData testData)}
     * </pre>
     *
     */
    @Override
    public Object[][] getAllData() {
        Class<?> arrayType;
        Object[][] dataToBeReturned = null;
        JsonReader reader = new JsonReader(getReader(resource));
        try {
            // The type specified must be converted to array type for the parser
            // to deal with array of JSON objects
            arrayType = Array.newInstance(resource.getCls(), 0).getClass();
       Log.info( "The Json Data is mapped as "+ arrayType);
            dataToBeReturned = mapJsonData(reader, arrayType);
        } catch (Exception e) {
            throw new DataProviderException("Error while parsing Json Data", e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
        return dataToBeReturned;
    }

    /**
     * Gets JSON data from a resource for the specified indexes.
     *
     * @param indexes
     *            The set of indexes to be fetched from the JSON file.
     */
    @Override
    public Object[][] getDataByIndex(String indexes) {
        validateResourceParams(resource);
        Preconditions.checkArgument(!StringUtils.isEmpty(indexes), "Indexes cannot be empty");
        Object[][] requestedData = getDataByIndex(DataProviderHelper.parseIndexString(indexes));
        return requestedData;
    }

    /**
     * Gets JSON data from a resource for the specified indexes.
     *
     * @param indexes
     *            The set of indexes to be fetched from the JSON file.
     */
    @Override
    public Object[][] getDataByIndex(int[] indexes) {
        validateResourceParams(resource);
        Preconditions.checkArgument((indexes.length != 0), "Indexes cannot be empty");
        Object[][] requestedData = null;
        Class<?> arrayType;
        JsonReader reader = null;
        try {

            requestedData = new Object[indexes.length][1];
            reader = new JsonReader(getReader(resource));
            arrayType = Array.newInstance(resource.getCls(), 0).getClass();
            Log.info( "The Json Data is mapped as "+arrayType);
            Object[][] mappedData = mapJsonData(reader, arrayType);
            int i = 0;
            for (int indexVal : indexes) {
                indexVal--;
                requestedData[i] = mappedData[indexVal];
                i++;
            }
        } catch (IOException e) {
            throw new DataProviderException("Error while getting the data by index from Json file", e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
        return requestedData;
    }

    /**
     * Gets JSON data from a resource by applying the given filter.
     *
     * @param dataFilter
     *            an implementation class of {@link DataProviderFilter}
     */
    @Override
    public Iterator<Object[]> getDataByFilter(DataProviderFilter dataFilter) {
        Preconditions.checkArgument(resource != null, "File resource cannot be null");
        Class<?> arrayType;
        JsonReader reader = null;
        try {
            reader = new JsonReader(getReader(resource));
            arrayType = Array.newInstance(resource.getCls(), 0).getClass();
            Gson myJson = new Gson();
            Object[] mappedData = myJson.fromJson(reader, arrayType);
            return prepareDataAsObjectArrayList(mappedData, dataFilter).iterator();
        } catch (Exception e) {
            throw new DataProviderException(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * A utility method to give out JSON data as HashTable. Please note this method works on the rule that the json
     * object that needs to be parsed MUST contain a key named "id".
     *
     * <pre>
     * [
     * {
     *  <b>"id":</b>"test1",
     *  "password":123456,
     *  "accountNumber":9999999999,
     *  "amount":80000,
     *  "areaCode":[{ "areaCode" :"area3"},
     *              { "areaCode" :"area4"}],
     *  "bank":{
     *         "name" : "Bank1",
     *         "type" : "Current",
     *         "address" : {
     *                     "street":"1234 dark St"
     *                 }
     *         }
     * }
     * ]
     * Here the key to the data in the hashtable will be "test1"
     * </pre>
     *
     * @return The JSON data as a {@link Hashtable}
     */
    @Override
    public Hashtable<String, Object> getDataAsHashtable() {
        Preconditions.checkArgument(resource != null, "File resource cannot be null");
        // Over-writing the resource because there is a possibility that a user
        // can give a type
        resource.setCls(Hashtable[].class);
        Hashtable<String, Object> dataAsHashTable = null;
        JsonReader reader = null;
        try {
            reader = new JsonReader(getReader(resource));
            Object[][] dataObject = mapJsonData(reader, resource.getCls());
            dataAsHashTable = new Hashtable<>();
            for (Object[] currentData : dataObject) {
                // Its pretty safe to cast to array and its also known that a 1D
                // array is packed
                Hashtable<?, ?> value = (Hashtable<?, ?>) currentData[0];
                /*
                 * As per the json specification a Json Object is a unordered collection of name value pairs. To give
                 * out the json data as hash table , a key needs to be figured out. To keep things clear and easy the
                 * .json file must have all the objects with a key "id" whose value can be used as the key in the
                 * hashtable.Users can directly access the data from the hash table using the value.
                 *
                 * Note: The id is harcoded purposefully here because to enforce the contract between data providers to
                 * have common methods.
                 */
                dataAsHashTable.put((String) value.get("id"), currentData);

            }
        } catch (NullPointerException n) { // NOSONAR
            throw new DataProviderException(
                    "Error while parsing Json Data as a Hash table. Root cause: Unable to find a key named id. Please refer Javadoc",
                    n);
        } catch (Exception e) {
            throw new DataProviderException("Error while parsing Json Data as a Hash table", e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
        return dataAsHashTable;
    }

    @Override
    public Object[][] getDataByKeys(String[] keys) {
        Hashtable<String, Object> dataAsHashTable = getDataAsHashtable();

        Object[][] objArray = DataProviderHelper.getDataByKeys(dataAsHashTable, keys);
        return objArray;
    }

    private Object[][] mapJsonData(JsonReader reader, Type typeToMatch) throws IOException {
        Gson myJson = new Gson();
        Object[] mappedData = myJson.fromJson(reader, typeToMatch);
        return prepareDataAsObjectArray(mappedData);
    }

    private Object[][] prepareDataAsObjectArray(Object[] dataToPack) {
        int entitySize = dataToPack.length;
        Log.info("Entity Size to be mapped to 2D array:" + entitySize);
        Object[][] dataArray = new Object[entitySize][1];
        int i = 0;
        for (Object currentData : dataToPack) {
            dataArray[i][0] = currentData;
            i++;
        }
        return dataArray;
    }

    private List<Object[]> prepareDataAsObjectArrayList(Object[] dataToPack, DataProviderFilter dataFilter) {
        Log.info("Entity Size to be mapped to ArrayList :" + dataToPack.length);
        List<Object[]> list = new ArrayList<>();
        for (Object currentData : dataToPack) {
            if (dataFilter.filter(currentData)) {
                list.add(new Object[] { currentData });
            }
        }
        return list;
    }

    private void validateResourceParams(DataResource jsonResource) {
        Preconditions.checkArgument(jsonResource != null, "File resource cannot be null");
        Preconditions.checkArgument(jsonResource.getCls() != null, "Cannot map json data to a null type");
    }

    private Reader getReader(DataResource resource) {
        return new InputStreamReader(resource.getInputStream());
    }
}