package com.k11.automation.coreframework.k11CustomDataProvider;

import com.k11.automation.coreframework.k11CustomDataProvider.Impl.ExcelDataProviderImpl;
import com.k11.automation.coreframework.k11CustomDataProvider.Impl.JsonDataProviderImpl;
import com.k11.automation.coreframework.k11CustomDataProvider.Impl.YamlDataProviderImpl;

import java.io.IOException;


/**
 * This factory class is responsible for providing the data provider implementation instance based on data type.
 *
 */
public final class DataProviderFactory {

    private DataProviderFactory() {
        // Utility class. So hide the constructor
    }

    /**
     * Load the Data provider implementation for the data file type
     *
     * @param resource - resource of the data file
     * @return Data provider Impl
     * @throws IOException
     */
    public static K11CustomDataProvider getDataProvider(DataResource resource) throws IOException {

        if(resource == null) {
            return null;
        }

        switch (resource.getType().toUpperCase()) {
    /*    case "XML":
            return new XmlDataProviderImpl((XmlDataSource) resource);*/
        case "JSON":
            return new JsonDataProviderImpl(resource);
        case "YAML":
        case "YML":
            return new YamlDataProviderImpl(resource);
        case "XLSX":
        case "XLS":
            return new ExcelDataProviderImpl(resource);
        default:
            return null;
        }
     }
}