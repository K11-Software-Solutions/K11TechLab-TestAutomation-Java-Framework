package com.k11.automation.coreframework.k11CustomDataProvider.Impl;

import com.k11.automation.coreframework.k11CustomDataProvider.DataResource;
import com.k11.automation.coreframework.k11CustomDataProvider.K11CustomDataProvider;
import com.k11.automation.coreframework.k11CustomDataProvider.helper.DataProviderHelper;
import com.k11.automation.coreframework.k11CustomDataProvider.filter.DataProviderFilter;
import com.k11.automation.coreframework.exceptions.DataProviderException;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.composer.ComposerException;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
/**
 * This class provides several methods to retrieve test data from yaml files. Users can get data returned in an Object
 * 2D array by loading the yaml file with Snakeyaml. If the entire yaml file is not needed then specific data entries
 * can be retrieved by indexes. If the yaml file is formatted to return a LinkedHashMap data type from Snakeyaml, user
 * can get an Object 2D array containing data for select keys or get the entire contents of the yaml file in a Hashtable
 * instead of an Object 2D array.
 *
 */

public final class YamlDataProviderImpl implements K11CustomDataProvider {

    private final DataResource resource;

    public YamlDataProviderImpl(DataResource resource) {
        this.resource = resource;
    }

    /**
     * Converts a yaml file into an Object 2D array for <a
     * href="http://testng.org/doc/documentation-main.html#parameters-dataproviders"> TestNG Dataprovider</a>
     * consumption. User-defined objects can be passed in to be added/mapped into the Snakeyaml constructor. <br>

     *
     * @return Object[][] two dimensional object to be used with TestNG DataProvider
     * @throws IOException
     */
    @Override
    public Object[][] getAllData() throws IOException {
        InputStream inputStream = resource.getInputStream();
        Yaml yaml = constructYaml(resource.getCls());

        Object yamlObject;

        // Mark the input stream in case multiple documents has been detected
        // so we can reset it.
        inputStream.mark(100);

        try {
            yamlObject = yaml.load(inputStream);
        } catch (ComposerException composerException) {
            if (composerException.getMessage().contains("expected a single document")) {
                inputStream.reset();
                yamlObject = loadDataFromDocuments(yaml, inputStream);
            } else {
                throw new DataProviderException("Error reading YAML data", composerException);
            }
        }

        Object[][] objArray = DataProviderHelper.convertToObjectArray(yamlObject);
        return objArray;
    }

    /**
     * Gets yaml data by applying the given filter. Throws {@link DataProviderException} when unexpected error occurs
     * during processing of YAML file data by filter
     *
     * @param dataFilter
     *            an implementation class of {@link DataProviderFilter}
     * @return An iterator over a collection of Object Array to be used with TestNG DataProvider
     * @throws IOException
     */
    @Override
    public Iterator<Object[]> getDataByFilter(DataProviderFilter dataFilter)
            throws IOException {
        InputStream inputStream = resource.getInputStream();
        Yaml yaml = constructYaml(resource.getCls());

        Object yamlObject;

        // Mark the input stream in case multiple documents has been detected
        // so we can reset it.
        inputStream.mark(100);

        try {
            yamlObject = yaml.load(inputStream);
        } catch (ComposerException composerException) {
            String msg = composerException.getMessage();
            msg = (msg == null) ? "" : msg;
            if (msg.toLowerCase().contains("expected a single document")) {
                inputStream.reset();
                yamlObject = loadDataFromDocuments(yaml, inputStream);
            } else {
                throw new DataProviderException("Error reading YAML data", composerException);
            }
        }
        return DataProviderHelper.filterToListOfObjects(yamlObject, dataFilter).iterator();
    }

    /**
     * Gets yaml data by key identifiers. Only compatible with a yaml file formatted to return a map. <br>
     * <br>
     * YAML file example:
     *
     * <pre>
     * test1:
     *     name: 1
     *     email: user1@delta.org
     *     userId: 10686626
     * test2:
     *     name: 2
     *     email: user2@delta.org
     *     userId: 10686627
     * </pre>
     *
     * @param keys
     *            A String array that represents the keys.
     *
     * @return Object[][] two dimensional object to be used with TestNG DataProvider
     */
    @Override
    public Object[][] getDataByKeys(String[] keys) {
        InputStream inputStream = resource.getInputStream();
        Yaml yaml = constructYaml(resource.getCls());

        LinkedHashMap<?, ?> map = yaml.load(inputStream);

        Object[][] objArray = DataProviderHelper.getDataByKeys(map, keys);
        return objArray;
    }

    /**
     * Gets yaml data and returns in a hashtable instead of an Object 2D array. Only compatible with a yaml file
     * formatted to return a map. <br>
     * <br>
     * YAML file example:
     *
     * <pre>
     * test1:
     *     name: 1
     *     email: user1@paypal.com
     *     userId: 10686626
     * test2:
     *     name: 2
     *     email: user2@paypal.com
     *     userId: 10686627
     * </pre>
     *
     * @return yaml data in form of a Hashtable.
     */
    @Override
    public Hashtable<String, Object> getDataAsHashtable() {
        InputStream inputStream = resource.getInputStream();
        Yaml yaml = constructYaml(resource.getCls());

        Hashtable<String, Object> yamlHashTable = new Hashtable<>();

        LinkedHashMap<?, ?> yamlObject = (LinkedHashMap<?, ?>) yaml.load(inputStream);

        for (Entry<?, ?> entry : yamlObject.entrySet()) {
            yamlHashTable.put((String) entry.getKey(), entry.getValue());
        }
        return yamlHashTable;
    }

    /**
     * Gets yaml data for requested indexes.
     *
     * @param indexes
     *            the input string represent the indexes to be parse
     *
     * @return Object[][] Two dimensional object to be used with TestNG DataProvider
     * @throws IOException
     */
    @Override
    public Object[][] getDataByIndex(String indexes) throws IOException,
            DataProviderException {
        int[] arrayIndex = DataProviderHelper.parseIndexString(indexes);
        Object[][] yamlObjRequested = getDataByIndex(arrayIndex);
        return yamlObjRequested;
    }

    /**
     * Generates an object array in iterator as TestNG DataProvider from the YAML data filtered per given indexes. This
     * method may throw {@link DataProviderException} when an unexpected error occurs during data provision from YAML
     * file.
     *
     * @param indexes
     *            The indexes for which data is to be fetched as a conforming string pattern.
     *
     * @return An Object[][] object to be used with TestNG DataProvider.
     * @throws IOException
     */
    @Override
    public Object[][] getDataByIndex(int[] indexes) throws IOException {
        Object[][] yamlObj = getAllData();
        Object[][] yamlObjRequested = new Object[indexes.length][yamlObj[0].length];

        int i = 0;
        for (Integer index : indexes) {
            index--;
            yamlObjRequested[i] = yamlObj[index];
            i++;
        }

        return yamlObjRequested;
    }

    /**
     * Converts a yaml file into an Object 2D array for <a
     * href="http://testng.org/doc/documentation-main.html#parameters-dataproviders"> TestNG Dataprovider</a>
     * consumption.
     *
     * <br>
     * A proper <a href="https://code.google.com/p/snakeyaml/wiki/Documentation#JavaBeans">JavaBean</a> must be defined
     * or else an exception will be thrown while attempting to load the yaml file. <br>
     * <br>

     * Test method signature example:
     *
     * <pre>
     * public void testExample(MyObject myObject)
     * </pre>
     *
     * @param yaml
     *            A {@link Yaml} object that represents a Yaml document.
     * @param inputStream
     *            A {@link InputStream} object.
     * @return an List containing multiple yaml documents loaded by SnakeYaml
     */
    private List<Object> loadDataFromDocuments(Yaml yaml, InputStream inputStream) {
        Iterator<?> documents = yaml.loadAll(inputStream).iterator();
        List<Object> objList = new ArrayList<>();

        while (documents.hasNext()) {
            objList.add(documents.next());
        }
        return objList;
    }

    private Yaml constructYaml(Class<?> cls) {
        if (cls != null) {
            Constructor constructor = new Constructor();
            constructor.addTypeDescription(new TypeDescription(cls, "!" + cls.getSimpleName()));
            return new Yaml(constructor);
        }

        return new Yaml();
    }

}