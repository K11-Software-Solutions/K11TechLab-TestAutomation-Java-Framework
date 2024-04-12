package com.k11.automation.coreframework.dataproviderhelper;

import com.k11.automation.coreframework.logger.Log;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonDataProviderHelper {


    /**
     * extractData_JSON method to get JSON data from file
     *
     * @param file
     * @return JSONObject
     * @throws Exception
     */
    public static JSONObject extractData_JSON(String file) {
        FileReader reader = null;
        try {
        reader = new FileReader(file);
        JSONParser jsonParser = new JSONParser();
        return (JSONObject) jsonParser.parse(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<JSONObject> filterTestData(List<JSONObject> testDataList){
        // include Filter
        if ( System.getProperty("includePattern") != null ) {
            String include = System.getProperty("includePattern");
            List<JSONObject> newList = new ArrayList<JSONObject>();
            List<String> tests = Arrays.asList(include.split(",", -1));
            if(!(tests.isEmpty()||tests.contains("NONE")))
            Log.info("Filtering the test data based on the following patterns: "+tests);
            else return testDataList;
            for ( String getTest : tests ) {
                for ( int i = 0; i < testDataList.size(); i++ ) {
                    if ( testDataList.get(i).toString().contains(getTest) ) {
                        newList.add(testDataList.get(i));
                    }
                }
            }

            // reassign testRows after filtering tests
            testDataList = newList;
            System.out.println("Filtered Test Data List based on parameters: "+testDataList);
        }

        // exclude Filter
        if ( System.getProperty("excludePattern") != null ) {
            String exclude =System.getProperty("excludePattern");
            List<String> tests = Arrays.asList(exclude.split(",", -1));

            for ( String getTest : tests ) {
                // start at end of list and work backwards so index stays in sync
                for ( int i = testDataList.size() - 1 ; i >= 0; i-- ) {
                    if ( testDataList.get(i).toString().contains(getTest) ) {
                        testDataList.remove(testDataList.get(i));
                    }
                }
            }
        }
        return testDataList;
    }



}
