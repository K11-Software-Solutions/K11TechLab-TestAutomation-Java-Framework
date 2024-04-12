package com.k11.automation.selenium.baseclasses.dataProvider;

import com.k11.automation.selenium.baseclasses.ApplicationProperties;
import com.k11.automation.coreframework.annotations.K11DataProvider;
import com.k11.automation.coreframework.exceptions.DataProviderException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.k11.automation.coreframework.dataproviderhelper.TestDataProviderHelper;

public class CSVTestDataProvider {

    @DataProvider(name = "k11-data-provider")
    public static Object[][] fetchCSVData(Method method){
       return getAllCSVData(method);
    }

    @DataProvider(name = "k11-data-provider-parallel", parallel = true)
    public static Object[][] fetchCSVDataParallel(Method method) {
        return getAllCSVData(method);
    }




    public static Object[][] getAllCSVData(Method method){
        String dataPath= ApplicationProperties.TESTDATA_DIR.getStringVal();
        String dataFile = getDataFileNameFromAnnotation(method);
        File input = new File(dataPath+dataFile);
        File output = new File(dataPath+dataFile.replace(".csv", ".json"));

        List<Map<?, ?>> data = null;
        try {
            data = readObjectsFromCsv(input);
            writeAsJson(data, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return TestDataProviderHelper.convertToObjectArray(data);

    }

    public static List<Map<?, ?>> readObjectsFromCsv(File file) throws IOException {
        CsvSchema bootstrap = CsvSchema.emptySchema().withHeader();
        CsvMapper csvMapper = new CsvMapper();
        MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader(Map.class).with(bootstrap).readValues(file);

        return mappingIterator.readAll();
    }

    public static void writeAsJson(List<Map<?, ?>> data, File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.writeValue(file, data);
    }

    public static void parse(String json)  {
        JsonFactory factory = new JsonFactory();

        ObjectMapper mapper = new ObjectMapper(factory);
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<Map.Entry<String,JsonNode>> fieldsIterator = rootNode.fields();
        while (fieldsIterator.hasNext()) {

            Map.Entry<String,JsonNode> field = fieldsIterator.next();
            System.out.println("Key: " + field.getKey() + "\tValue:" + field.getValue());
        }
    }

    public static String getDataFileNameFromAnnotation(Method method){
        String dataFile="";
        try {
            dataFile = method.getDeclaredAnnotation(K11DataProvider.class).dataFile();
        }catch(Exception e){
            throw new DataProviderException("The test data file name has not been specified. " +
                    "Please specify filename in the K11DataProvider annotation.");
        }
        return dataFile;
    }
}