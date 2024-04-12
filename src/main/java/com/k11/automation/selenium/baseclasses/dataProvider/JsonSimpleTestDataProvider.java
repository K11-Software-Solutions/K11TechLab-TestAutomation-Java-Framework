package com.k11.automation.selenium.baseclasses.dataProvider;

import com.k11.automation.selenium.baseclasses.ApplicationProperties;
import com.k11.automation.coreframework.logger.Log;
import com.k11.automation.coreframework.exceptions.DataProviderException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import com.k11.automation.coreframework.annotations.K11DataProvider;
import com.k11.automation.coreframework.dataproviderhelper.JsonDataProviderHelper;


/**
 * TestNG JSON DataProvider Utility Class
 *
 */
public class JsonSimpleTestDataProvider {

    public static String testCaseName = "NA";

    @DataProvider(name = "k11-data-provider")
    public static Object[][] fetchTestData(Method method) {
        return fetchData(method);
    }

    @DataProvider(name = "k11-data-provider-parallel", parallel = true)
    public static Object[][] fetchTestDataParallel(Method method) {
        return fetchData(method);
    }


    /**
     * fetchData method to retrieve test data for specified method
     *
     * @param method
     * @return Object[][]
     */
    private static Object[][] fetchData(Method method) {
        Object[][] result;
        String parameters=getParametersFromAnnotation(method);
        Log.info("The parameters are: "+parameters.toString());
        String dataFile = getDataFileNameFromAnnotation(method);
        String dataPath = ApplicationProperties.TESTDATA_DIR.getStringVal();

        testCaseName = method.getName();
        List<JSONObject> testDataList = new ArrayList<>();
        JSONArray testData = (JSONArray) JsonDataProviderHelper.extractData_JSON(dataPath + dataFile).get(testCaseName);
        for (int i = 0; i < testData.size(); i++) {
            testDataList.add((JSONObject) testData.get(i));
        }

        // reassign testRows after filtering tests
        testDataList = JsonDataProviderHelper.filterTestData(testDataList);
        Log.info("The number of test cases to be executed is: " + testDataList.size());

        // create object for dataprovider to return
        try {
            result = new Object[testDataList.size()][testDataList.get(0).size()];
            for (int i = 0; i < testDataList.size(); i++) {
                if(parameters.contains("environment"))
                    result[i] = new Object[]{System.getProperty("environment"), testDataList.get(i)};
                else
                    result[i] = new Object[]{testDataList.get(i)};
            }
        } catch (IndexOutOfBoundsException ie) {
            Log.info(ie.getMessage());
            result = new Object[0][0];
        }
        return result;
    }


    public static String getDataFileNameFromAnnotation(Method method){
        String dataFile="";
        try {
            dataFile = method.getDeclaredAnnotation(K11DataProvider.class).dataFile();
        }catch(Exception e){
            throw new DataProviderException("The test data file name has not been specified. " +
                    "Please specify filename in the k11CustomDataProvider annotation.");
        }
        return dataFile;
    }

    public static String getParametersFromAnnotation(Method method){
        String parameter ="";
        try {
            parameter = method.getDeclaredAnnotation(Parameters.class).toString();
        }catch(Exception e){
            Log.info("No parameters found in the testNG 'parameters' annotation.");
        }
        return parameter;
    }
}
