package com.k11.automation.coreframework.dataproviderhelper;

import com.k11.automation.selenium.baseclasses.ApplicationProperties;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.k11.automation.coreframework.dataproviderhelper.ExcelDataProviderHelper.convertToObjectArray;

public class CSVTestDataProvider {

    public static void main(String[] args) throws Exception {
        String dataPath= ApplicationProperties.TESTDATA_DIR.getStringVal();
        File input = new File(dataPath+"filename.csv");
        File output = new File(dataPath+"filename.json");

        List<Map<?, ?>> data = readObjectsFromCsv(input);
        writeAsJson(data, output);
        Object[][] csvDataRowObjects= convertToObjectArray(data);
        System.out.println(csvDataRowObjects[0][0].toString().replaceAll(", =", ""));
        System.out.println(csvDataRowObjects[1][0].toString().replaceAll(", =", ""));

    }
    public static List<Map<?, ?>> readObjectsFromCsv(File file) throws IOException {
        CsvSchema bootstrap = CsvSchema.emptySchema().withHeader();
        CsvMapper csvMapper = new CsvMapper();
        MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader(Map.class).with(bootstrap).readValues(file);

        return mappingIterator.readAll();
    }

    public static void writeAsJson(List<Map<?, ?>> data, File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, data);
    }
}