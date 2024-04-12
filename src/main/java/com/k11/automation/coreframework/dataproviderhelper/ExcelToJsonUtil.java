package com.k11.automation.coreframework.dataproviderhelper;

import com.k11.automation.selenium.baseclasses.ApplicationProperties;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelToJsonUtil {
     public static JSONObject[][] getExcelToJson(String filename, String _sheet) throws JSONException, IOException {
              String dataPath= ApplicationProperties.TESTDATA_DIR.getStringVal();
              String filePath = dataPath + filename;
              String[] header;
              FileInputStream fileInputStream = new FileInputStream(filePath);
              HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
              Sheet sheet = workbook.getSheet(_sheet);
              Cell cell = null;
              int columns = sheet.getRow(0).getLastCellNum();

              int rows = sheet.getLastRowNum();
              System.out.println("The last row number is: "+rows);
              if(rows>=1)
                  rows=rows+1;
              JSONObject[][] data = new JSONObject[rows - 1][1];
              header = new String[columns];
              Iterator<Cell> headerIterator;
              Iterator<Cell> dataIterator;
              int headerIndex = 0;
              for (headerIterator = sheet.getRow(0).cellIterator(); headerIterator.hasNext(); ) {
                     cell = headerIterator.next();
                    header[headerIndex++] = cell.getStringCellValue();
              }

              for (int row = 1; row <rows; row++) {
                    dataIterator = sheet.getRow(row).cellIterator();
                    JSONObject record = new JSONObject();
                    for (headerIndex = 0; headerIndex < columns; headerIndex++) {
                    try {
                           cell = dataIterator.next();
                           record.put(header[headerIndex], cell.getStringCellValue());
                         } catch (IllegalStateException e) {
                            cell.setCellType(CellType.STRING);
                            record.put(header[headerIndex], cell.getStringCellValue());
                           } catch (ArrayIndexOutOfBoundsException e) {
                            record.put(header[headerIndex], "");
                           }
                           }
                      data[row - 1][0] = record;
               }
         return data;
      }

    public static List<JSONObject> getListOfRecordsFromExcelSheet(String filename, String _sheet) throws JSONException, IOException {
        String dataPath= ApplicationProperties.TESTDATA_DIR.getStringVal();
        String filePath = dataPath + filename;
        String[] header;
        List<JSONObject> records=new ArrayList<>();
        FileInputStream fileInputStream = new FileInputStream(filePath);
        HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheet(_sheet);
        Cell cell = null;
        int columns = sheet.getRow(0).getLastCellNum();

        int rows = sheet.getLastRowNum();
        if(rows>=1)
            rows=rows+1;
        JSONObject[][] data = new JSONObject[rows - 1][1];
        header = new String[columns];
        Iterator<Cell> headerIterator;
        Iterator<Cell> dataIterator;
        int headerIndex = 0;
        for (headerIterator = sheet.getRow(0).cellIterator(); headerIterator.hasNext(); ) {
            cell = headerIterator.next();
            header[headerIndex++] = cell.getStringCellValue();
        }

        for (int row = 1; row <rows; row++) {
            dataIterator = sheet.getRow(row).cellIterator();
            JSONObject record = new JSONObject();
            for (headerIndex = 0; headerIndex < columns; headerIndex++) {
                try {
                    cell = dataIterator.next();
                    record.put(header[headerIndex], cell.getStringCellValue());
                } catch (IllegalStateException e) {
                    cell.setCellType(CellType.STRING);
                    record.put(header[headerIndex], cell.getStringCellValue());
                } catch (ArrayIndexOutOfBoundsException e) {
                    record.put(header[headerIndex], "");
                }
            }
            data[row - 1][0] = record;
            records.add(record);
        }
        return records;
     }


    public static void writeInExcelFile(String FilePathName, String data, int rowIndex, int colIndex) throws IOException {

        // Open existing file as an input stream
        FileInputStream fileInputStream = new FileInputStream(FilePathName);
        // create an object of Workbook and pass the FileInputStream object into it
        HSSFWorkbook workbook = new HSSFWorkbook (fileInputStream);
        // use getSheetAt() to pass sheet number. Here index is 0.
        HSSFSheet sheet = workbook.getSheetAt(0);
        // Create a cell where we want to enter a value.
        Row row;
        // If there's no existing row, create one.
        if (sheet.getPhysicalNumberOfRows() < rowIndex + 1) {
            row = sheet.createRow(rowIndex);
        } else {
            row = sheet.getRow(rowIndex);
        }
        Cell cell = row.createCell(colIndex);
        // Now we need to find out the type of the value we want to enter.
        // For a string, we need to set the cell type as string
        cell.setCellType(CellType.STRING);
        cell.setCellValue(data);
        FileOutputStream fileOutputStream = new FileOutputStream(FilePathName);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }


    public static String getCellData(String FilePathName, String sheetName, String colName, int rowNum) throws IOException {
        // Open existing file as an input stream
        FileInputStream fileInputStream = new FileInputStream(FilePathName);
        // create an object of Workbook and pass the FileInputStream object into it
        HSSFWorkbook workbook = new HSSFWorkbook (fileInputStream);
        // use getSheetAt() to pass sheet number. Here index is 0.
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow row = null;
        HSSFCell cell = null;
        int colNum=0;
        try
        {
            colNum = -1;
            sheet = workbook.getSheet(sheetName);
            row = sheet.getRow(0);
            for(int i = 0; i < row.getLastCellNum(); i++)
            {
                if(row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
                    colNum = i;
            }

            row = sheet.getRow(rowNum - 1);
            cell = row.getCell(colNum);

            String cellValue = String.valueOf(cell.getStringCellValue());
            return cellValue;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "row "+rowNum+" or column "+colNum +" does not exist  in Excel";
        }
    }
}
