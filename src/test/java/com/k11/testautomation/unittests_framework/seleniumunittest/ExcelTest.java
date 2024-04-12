package com.k11.testautomation.unittests_framework.seleniumunittest;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import com.k11.automation.selenium.baseclasses.ApplicationProperties;
import com.k11.automation.coreframework.util.DataManager;
import org.junit.Test;
import org.testng.Assert;

import java.io.IOException;
import java.util.HashMap;

public class ExcelTest {
    @Test
    public void getDMExcelData() throws IOException, InvalidFormatException {

        String dataPath = ApplicationProperties.TESTDATA_DIR.getStringVal();
        String filename = "xlsfile.xls";//"inputfile.xlsx";
        String filePath = dataPath + filename;
        String fullPath2 = this.getClass().getClassLoader().getResource(filePath).getPath();
        DataManager DM = new DataManager(fullPath2);
        Assert.assertEquals(DM.totalNumberOfRows(),1);
        HashMap<String, String> record;
        record = DM.GetNextRecord();
        Assert.assertEquals(record.get("fname"),"Kalyani");
        Assert.assertEquals(DM.totalNumberOfRows(),1);
    }
    @Test
    public void getDMXlxsExcelData() throws IOException, InvalidFormatException {

        String dataPath = ApplicationProperties.TESTDATA_DIR.getStringVal();
        String filename = "inputfile.xlsx";//"inputfile.xlsx";
        String filePath = dataPath + filename;
        String fullPath2 = this.getClass().getClassLoader().getResource(filePath).getPath();
        DataManager DM = new DataManager(fullPath2);
        Assert.assertEquals(DM.totalNumberOfRows(),1);
        HashMap<String, String> record;
        record = DM.GetNextRecord();
        Assert.assertEquals(record.get("fname"),"satya");
        Assert.assertEquals(DM.totalNumberOfRows(),1);
    }

    @Test
    public void getFetchExcelData() throws IOException, InvalidFormatException {

        String dataPath = ApplicationProperties.TESTDATA_DIR.getStringVal();
        String filename = "inputfile.xlsx";//"inputfile.xlsx";
        String filePath = dataPath + filename;
        String fullPath2 = this.getClass().getClassLoader().getResource(filePath).getPath();
        String cellvalue;
        cellvalue = DataManager.ExcelFeatch(fullPath2,0,1,2);

        Assert.assertEquals(cellvalue,"01/01/2011");
        //Assert.assertEquals(DM.totalNumberOfRows(),1);
    }
}
