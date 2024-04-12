/*//////////////////////////////////////////////////////////////////////////////////////////////
' Class: DataManager
'
' About:
'  This library provides a common technique for accessing data stored in an Excel (.xlx or .xlsx)
' spreadsheet.  Each row of data is read out in to a record, stored in a HashMap<String, String) for
' easy access.
'
' Usage:
'Case 1: Access xlsx file and get the data from column "fname"
'        String dataPath = ApplicationProperties.TESTDATA_DIR.getStringVal();
'        String filename = "inputfile.xlsx";
'        String filePath = dataPath + filename;
'        String fullPath2 = this.getClass().getClassLoader().getResource(filePath).getPath();
'        ExcelDataManager DM = new ExcelDataManager(fullPath2);
'        Assert.assertEquals(DM.totalNumberOfRows(),1);
'        HashMap<String, String> record;
'        record = DM.GetNextRecord();
'        Assert.assertEquals(record.get("fname"),"satya");
'        Assert.assertEquals(DM.totalNumberOfRows(),1);
'Case2: Access xls and get the data from column "fname"
'        String dataPath = ApplicationProperties.TESTDATA_DIR.getStringVal();
'        String filename = "xlsfile.xls";//"inputfile.xlsx";
'        String filePath = dataPath + filename;
'        String fullPath2 = this.getClass().getClassLoader().getResource(filePath).getPath();
'        DataManager DM = new DataManager(fullPath2);
'        Assert.assertEquals(DM.totalNumberOfRows(),1);
'        HashMap<String, String> record;
'        record = DM.GetNextRecord();
'        Assert.assertEquals(record.get("fname"),"Kalyani");
'        Assert.assertEquals(DM.totalNumberOfRows(),1);
'Case 3: Have static method which can allow to get the data from any cell.
'        String dataPath = ApplicationProperties.TESTDATA_DIR.getStringVal();
'        String filename = "inputfile.xlsx";//"inputfile.xlsx";
'        String filePath = dataPath + filename;
'        String fullPath2 = this.getClass().getClassLoader().getResource(filePath).getPath();
'        String cellvalue;
'        cellvalue = DataManager.ExcelFeatch(fullPath2,0,1,2);

'        Assert.assertEquals(cellvalue,"01/01/2011");
        //Assert.assertEquals(DM.totalNumberOfRows(),1)
'###############################################################################*/
package  com.k11.automation.coreframework.util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataManager {
   //Variables
    private String fileName;
    private int currentRow =0;
    private int currentSheet = 0;
    List<String> headerList = new ArrayList<String>();
    Workbook workbookObject = null;
    FileInputStream fileObject = null;
    Sheet workSheet  = null;
    Row row = null;
    SimpleDateFormat dateInFormate = new  SimpleDateFormat ("MM/dd/yyyy");

    //methods
    //Constructor
    public DataManager(String inputfileName) throws IOException, InvalidFormatException {
        fileName = inputfileName;
        fileObject = new FileInputStream (fileName);
        workbookObject = WorkbookFactory.create(fileObject);
        workSheet = workbookObject.getSheetAt(currentSheet);

        loadHeader();
        currentRow = currentRow + 1;


    }
	/////////////////////////////////////////////
	//Method: getCurrentFile
	//        The method will return current file name 
	//        and the file name will set at constructor.
	//
	////////////////////////////////////////////
    public String getCurrentFile () {
        return fileName;
    }
    /////////////////////////////////////////////
	//Method: setCurrentFile
	//        The method will set current file name .
	//        
	//
	////////////////////////////////////////////
    public void setCurrentFile (String inputfile) {
        fileName = inputfile;
    }
    /////////////////////////////////////////////
	//Method: getCurrntRow
	//        The method will return current row.
	//
	/////////////////////////////////////////////
    public int getCurrentRow(){
        return currentRow;
    }
    /////////////////////////////////////////////
	//Method: setCurrntRow
	//        The method will set current row.
	//
	/////////////////////////////////////////////
    public void setCurrentRow (int setCurrentRow) {
        currentRow = setCurrentRow;
    }
    public int totalNumberOfRows(){
        int numberOfUsageRows;
        numberOfUsageRows = workSheet.getLastRowNum();
        return numberOfUsageRows;
    }
	/////////////////////////////////////////////
	//Method: totalNumberOfColsInRow
	//        The method will return total number of
	//        rows.
	//
	/////////////////////////////////////////////
    public int totalNumberOfColsInRow(int row){
        int numberOfUsageCols;
        numberOfUsageCols = workSheet.getRow(row).getLastCellNum();
        return numberOfUsageCols;
    }
	/////////////////////////////////////////////
	//Method:totalNumberOfSheets
	//       The method will return total number of
	//       sheets.
	//
	///////////////////////////////////////////////
    public int totalNumberOfSheets(){
        int numberOfSheets;
        numberOfSheets = workbookObject.getNumberOfSheets();
        return numberOfSheets;
    }
    /////////////////////////////////////////////
	//Method: loadHeader
	//        The method will load the header details
	//        into list object.
	//
	/////////////////////////////////////////////
    public void loadHeader(){
        for (int i=0; i<totalNumberOfColsInRow(currentRow);i++){
            String getvalue = workSheet.getRow(currentRow).getCell(i).getStringCellValue();
            headerList.add(getvalue);
        }

    }
    /////////////////////////////////////////////
	//Method: Rewind
	//        The method will set DM to previous record.
	//
	/////////////////////////////////////////////
    public void Rewind(){
        setCurrentRow (getCurrentRow() -1);
    }
    /////////////////////////////////////////////
	//Method: CheckForEOF
	//        The method will cheeck end of file.
	//
	//////////////////////////////////////////////
    public boolean CheckForEOF(){
        Boolean returnflag = true;
        if (getCurrentRow() <= totalNumberOfRows()){
            returnflag = false;
        }
            return returnflag;
    }
    /////////////////////////////////////////////
	//Method: GetNextRecord
	//        The method will return next record.
	//
	///////////////////////////////////////////////
    public HashMap<String, String> GetNextRecord() {
        List<String> dataList = new ArrayList<String>();
        HashMap<String, String> data = new HashMap<String, String>();
        String getvalue;
        int getnumber;
        try{
            row = workSheet.getRow(currentRow);
            for (int i=0; i<totalNumberOfColsInRow(currentRow);i++){
                if (workSheet.getRow(currentRow).getCell(i) != null){
                    if (workSheet.getRow(currentRow).getCell(i).toString().trim().equals("N/A")){
                        dataList.add("");
                    }else if(workSheet.getRow(currentRow).getCell(i).getCellType() == CellType.STRING){
                        getvalue = workSheet.getRow(currentRow).getCell(i).getStringCellValue();
                        dataList.add (getvalue);
                    } else if (workSheet.getRow(currentRow).getCell(i).getCellType() == CellType.NUMERIC){
                        if (DateUtil.isCellDateFormatted(workSheet.getRow(currentRow).getCell(i))){
                            String formattedate = dateInFormate.format(workSheet.getRow(currentRow).getCell(i).getDateCellValue());
                            dataList.add (formattedate);
                        } else{
                            getnumber = (int) workSheet.getRow(currentRow).getCell(i).getNumericCellValue();
                            getvalue = Integer.toString((int)getnumber);
                            dataList.add ( getvalue);
                        }
                    }
                }else{
                    dataList.add("");
                }
            }
            //add data into HashMap
            for (int i = 0; i < headerList.size(); i++) {
                data.put(headerList.get(i), dataList.get(i).trim());
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        currentRow = currentRow + 1;
        return data;

    }
	/////////////////////////////////////////////
	//Method: getCellString
	//        The method will return cell data assertEquals
	//        a String value.
	//
	/////////////////////////////////////////////
    public static String getCellString(Cell cell) {
        String returnValue = null;
        SimpleDateFormat dateInFormate = new  SimpleDateFormat ("MM/dd/yyyy");
        int getnumber =0;

        if (cell != null){
             if(cell.getCellType() == CellType.STRING){
                 returnValue = cell.getStringCellValue();
            } else if (cell.getCellType() == CellType.NUMERIC){
                if (DateUtil.isCellDateFormatted(cell)){
                    returnValue = dateInFormate.format(cell.getDateCellValue());
                } else{
                    getnumber = (int) cell.getNumericCellValue();
                    returnValue = Integer.toString((int)getnumber);
                }
            } else if (cell.getCellType() == CellType.FORMULA) {
                 returnValue = String.valueOf(cell.getNumericCellValue());
             }
        } else {
            returnValue ="";
        }

        return returnValue;
    }
	/////////////////////////////////////////////
	//Method: ExcelFeatch
	//        The method will return cell data as a 
	//        String value and it is static method.
	//
	//////////////////////////////////////////////
    public static String ExcelFeatch(String inputfile, int sheetnum, int rownum, int colnum) throws IOException, InvalidFormatException {
        String returnValue =null;
        String fileName;
        Workbook workbookObject = null;
        FileInputStream fileObject = null;
        Sheet workSheet  = null;
        Row row = null;
        Cell cell = null;

         try {
             fileName = inputfile;
             fileObject = new FileInputStream(fileName);
             workbookObject = WorkbookFactory.create(fileObject);
             workSheet = workbookObject.getSheetAt(sheetnum);
             row = workSheet.getRow(rownum);
             int totalnumberOfCols;
             totalnumberOfCols = row.getLastCellNum();
             if (totalnumberOfCols < colnum){
                 returnValue = "column does not exists";
             } else {
                 cell = row.getCell(colnum);
                 returnValue = getCellString(cell);
             }
         } catch (Exception e){
             e.printStackTrace();
         }finally {
             workSheet = null;
             if (workbookObject != null ){
                 workbookObject.close();
                 fileObject.close();
             }
         }

        return returnValue;
    }

}
