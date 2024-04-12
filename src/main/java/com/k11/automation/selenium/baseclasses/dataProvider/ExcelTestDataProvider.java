package com.k11.automation.selenium.baseclasses.dataProvider;

import com.k11.automation.selenium.baseclasses.ApplicationProperties;
import com.k11.automation.coreframework.logger.Log;
import com.k11.automation.coreframework.annotations.K11DataProvider;
import com.k11.automation.coreframework.dataproviderhelper.TestDataProviderHelper;
import com.k11.automation.coreframework.exceptions.DataProviderException;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

public class ExcelTestDataProvider {

    @DataProvider(name = "k11-data-provider")
    public static Object[][] fetchExcelData(Method method){
        Log.debug("Initializing the test data");
        String dataPath= ApplicationProperties.TESTDATA_DIR.getStringVal();
        String dataFile = getDataFileNameFromAnnotation(method);
        return TestDataProviderHelper.convertExcelDataToObjectArray(dataPath+dataFile, "csr");
    }

    @DataProvider(name = "k11-data-provider-parallel", parallel = true)
    public static Object[][] fetchExcelDataParallel(Method method){
        Log.debug("Initializing the test data");
        String dataPath= ApplicationProperties.TESTDATA_DIR.getStringVal();
        String dataFile = getDataFileNameFromAnnotation(method);
        return TestDataProviderHelper.convertExcelDataToObjectArray(dataPath+dataFile, "csr");

    }

    public static String getDataFileNameFromAnnotation(Method method){
        String dataFile="";
        try {
            dataFile = method.getDeclaredAnnotation(K11DataProvider.class).dataFile();
        }catch(Exception e){
            throw new DataProviderException("The test data file name has not been specified. " +
                    "Please specify filename in the k11DataProvider annotation.");
        }
        return dataFile;
    }



}
