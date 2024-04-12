package com.k11.automation.coreframework.dataproviderhelper;

import com.k11.automation.selenium.baseclasses.ApplicationProperties;
import com.k11.automation.coreframework.logger.Log;
import com.k11.automation.coreframework.annotations.K11DataProvider;
import com.k11.automation.coreframework.exceptions.DataProviderException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;

public class ExcelTestDataProvider {


    @DataProvider(name = "k11-data-provider", parallel = true)
    public static Object[][] fetchExcelData(Method method){
        Log.debug("Initializing the test data");
        String dataPath= ApplicationProperties.TESTDATA_DIR.getStringVal();
        String dataFile = getDataFileNameFromAnnotation(method);
        return ExcelDataProviderHelper.convertExcelDataToObjectArray(dataPath+dataFile, "csr");

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
