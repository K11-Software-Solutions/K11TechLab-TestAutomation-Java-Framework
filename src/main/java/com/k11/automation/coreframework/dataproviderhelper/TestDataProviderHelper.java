package com.k11.automation.coreframework.dataproviderhelper;

import com.k11.automation.selenium.baseclasses.ApplicationProperties;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.json.JSONObject;

import java.io.*;
import java.util.*;


public class TestDataProviderHelper {

        public static void main(String[] args)
        {
            // You can specify your excel file path.
            String excelFilePath = ApplicationProperties.TESTDATA_DIR.getStringVal()+"k11_TestData.xls";

            // This method will read each sheet data from above excel file and create a JSON and a text file to save the sheet data.
            createJSONAndTextFileFromExcel(excelFilePath, "csr");
           // readJsonObjectsFromFile(excelFilePath.replace(".xls", ".xls.json"));
            Object[][] excelDataRowObjects= TestDataProviderHelper.convertExcelDataToObjectArray(excelFilePath, "csr");
            System.out.println(excelDataRowObjects[0][0].toString());


        }


        /* Read data from an excel file and output each sheet data to a json file and a text file.
         * filePath :  The excel file store path.
         * */
        public static void createJSONAndTextFileFromExcel(String filePath, String _sheet)
        {
            try{
                /* First need to open the file. */
                FileInputStream fInputStream = new FileInputStream(filePath.trim());

                /* Create the workbook object to access excel file. */
                Workbook excelWorkBook = new HSSFWorkbook(fInputStream);
                Sheet sheet = excelWorkBook.getSheet(_sheet);
                // Get current sheet data in a list table.
                        List<List<String>> sheetDataTable = getSheetDataList(sheet);

                        // Generate JSON format of above sheet data and write to a JSON file.
                        String jsonString = getJSONStringFromList(sheetDataTable);
                        String jsonFileName = new File(filePath).getName() + ".json";
                        writeStringToFile(jsonString, ApplicationProperties.TESTDATA_DIR.getStringVal()+jsonFileName);

                        // Generate text table format of above sheet data and write to a text file.
                        String textTableString = getTextTableStringFromList(sheetDataTable);
                        String textTableFileName = new File(filePath).getName() + ".txt";
                        writeStringToFile(textTableString, ApplicationProperties.TESTDATA_DIR.getStringVal()+textTableFileName);

            }catch(Exception ex){
                ex.printStackTrace();
                System.err.println(ex.getMessage());
            }
        }


        /* Return sheet data in a two dimensional list.
         * Each element in the outer list is represent a row,
         * each element in the inner list represent a column.
         * The first row is the column name row.*/
        private static List<List<String>> getSheetDataList(Sheet sheet)
        {
            List<List<String>> ret = new ArrayList<List<String>>();

            // Get the first and last sheet row number.
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum = sheet.getLastRowNum();

            if(lastRowNum > 0)
            {
                // Loop in sheet rows.
                for(int i=firstRowNum; i<lastRowNum + 1; i++)
                {
                    // Get current row object.
                    Row row = sheet.getRow(i);

                    // Get first and last cell number.
                    int firstCellNum = row.getFirstCellNum();
                    int lastCellNum = row.getLastCellNum();

                    // Create a String list to save column data in a row.
                    List<String> rowDataList = new ArrayList<String>();

                    // Loop in the row cells.
                    for(int j = firstCellNum; j < lastCellNum; j++)
                    {
                        // Get current cell.
                        Cell cell = row.getCell(j);
                        if(cell!=null)
                        try{
                        // Get cell type.
                        String cellValue = cell.getStringCellValue();
                        rowDataList.add(cellValue);
                        } catch (Exception e ){
                                cell.setCellType(CellType.STRING);
                                String cellValue = cell.getStringCellValue();
                                rowDataList.add(cellValue);
                        }
                }

                    // Add current row data list in the return list.
                    ret.add(rowDataList);
                }
            }
            return ret;
        }

        /* Return a JSON string from the string list. */
        private static String getJSONStringFromList(List<List<String>> dataTable)
        {
            String ret = "";

            if(dataTable != null)
            {
                int rowCount = dataTable.size();

                if(rowCount > 1)
                {
                    // Create a JSONObject to store table data.
                    JSONObject tableJsonObject = new JSONObject();

                    // The first row is the header row, store each column name.
                    List<String> headerRow = dataTable.get(0);

                    int columnCount = headerRow.size();

                    // Loop in the row data list.
                    for(int i=1; i<rowCount; i++)
                    {
                        // Get current row data.
                        List<String> dataRow = dataTable.get(i);

                        // Create a JSONObject object to store row data.
                        JSONObject rowJsonObject = new JSONObject();

                        for(int j=0;j<columnCount;j++)
                        {
                            String columnName = headerRow.get(j);
                            String columnValue = dataRow.get(j);

                            rowJsonObject.put(columnName, columnValue);
                        }

                        tableJsonObject.put("Row " + i, rowJsonObject);
                    }

                    // Return string format data of JSONObject object.
                    ret = tableJsonObject.toString();

                }
            }
            return ret;
        }

    /* Return a JSON string from the string list. */
    public static Map<String, JSONObject> getJSONObjectsFromExcelData(List<List<String>> dataTable)
    {
        String ret = "";
        Map<String, JSONObject> tableJsonObjectMap=new LinkedHashMap<>();

        if(dataTable != null)
        {
            int rowCount = dataTable.size();

            if(rowCount > 1)
            {
               // The first row is the header row, store each column name.
                List<String> headerRow = dataTable.get(0);

                int columnCount = headerRow.size();

                // Loop in the row data list.
                for(int i=1; i<rowCount; i++)
                {
                    // Get current row data.
                    List<String> dataRow = dataTable.get(i);

                    // Create a JSONObject object to store row data.
                    JSONObject rowJsonObject = new JSONObject();

                    for(int j=0;j<columnCount;j++)
                    {
                        String columnName = headerRow.get(j);
                        String columnValue = dataRow.get(j);

                        rowJsonObject.put(columnName, columnValue);
                    }

                    tableJsonObjectMap.put("Row " + i,rowJsonObject);
                }

            }
        }
        return tableJsonObjectMap;
    }


        /* Return a text table string from the string list. */
        private static String getTextTableStringFromList(List<List<String>> dataTable)
        {
            StringBuffer strBuf = new StringBuffer();

            if(dataTable != null)
            {
                // Get all row count.
                int rowCount = dataTable.size();

                // Loop in the all rows.
                for(int i=0;i<rowCount;i++)
                {
                    // Get each row.
                    List<String> row = dataTable.get(i);

                    // Get one row column count.
                    int columnCount = row.size();

                    // Loop in the row columns.
                    for(int j=0;j<columnCount;j++)
                    {
                        // Get column value.
                        String column = row.get(j);

                        // Append column value and a white space to separate value.
                        strBuf.append(column);
                        strBuf.append("    ");
                    }

                    // Add a return character at the end of the row.
                    strBuf.append("\r\n");
                }

            }
            return strBuf.toString();
        }

        /* Write string data to a file.*/
        private static void writeStringToFile(String data, String fileName)
        {
            try
            {
                // Get current executing class working directory.
                String currentWorkingFolder = System.getProperty("user.dir");

                // Get file path separator.
                String filePathSeperator = System.getProperty("file.separator");

                // Get the output file absolute path.
                String filePath = currentWorkingFolder + filePathSeperator + fileName;

                // Create File, FileWriter and BufferedWriter object.
                File file = new File(filePath);

                FileWriter fw = new FileWriter(file);

                BufferedWriter buffWriter = new BufferedWriter(fw);

                // Write string data to the output file, flush and close the buffered writer object.
                buffWriter.write(data);

                buffWriter.flush();

                buffWriter.close();

                System.out.println(filePath + " has been created.");

            }catch(IOException ex)
            {
                System.err.println(ex.getMessage());
            }
        }


    public static void readJsonObjectsFromFile(String filepath) {
        String jsonString = null;
        try {
            jsonString = loadJSONFile(filepath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        print(jsonObject);
    }

    private static String loadJSONFile(String filepath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader(filepath));
        StringBuilder stringBuilder = new StringBuilder();

        while (scanner.hasNext()) {
            stringBuilder.append(scanner.next());
        }

        scanner.close();

        return stringBuilder.toString();
    }

    private static void print(JsonObject jsonObject) {
        Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();

        for (Map.Entry<String, JsonElement> entry : entries) {
            System.out.println(entry.getKey() + ": " + entry.getValue());

            try {
                JsonElement jsonElement = new JsonParser().parse(String.valueOf(entry.getValue()));
                JsonObject innerJsonObject = jsonElement.getAsJsonObject();

                print(innerJsonObject);
            } catch (Exception e) {
                // is not a JSON
            }
        }
    }

    public static Object[][] convertExcelDataToObjectArray(String filePath, String _sheet){
        List<List<String>> sheetDataTable = null;
        try {
            /* First need to open the file. */
            FileInputStream fInputStream = new FileInputStream(filePath.trim());

            /* Create the workbook object to access excel file. */
            Workbook excelWorkBook = new HSSFWorkbook(fInputStream);
            Sheet sheet = excelWorkBook.getSheet(_sheet);
            // Get current sheet data in a list table.
            sheetDataTable= getSheetDataList(sheet);

        } catch (Exception e) {
            // is not a JSON
        }
        Map<String, JSONObject> excelDataRowObjects= getJSONObjectsFromExcelData(sheetDataTable);
        return convertToObjectArray(excelDataRowObjects);

        }


    public static Object[][] convertToObjectArray(Object object) {
        // Converts single instance of any type as an object at position [0][0] in an Object double array of size
        // [1][1].
        Object[][] objArray = new Object[][] { { object } };

        Class<?> rootClass = object.getClass();

        // Convert a LinkedHashMap (e.g. Yaml Associative Array) to an Object double array.
        if (rootClass.equals(LinkedHashMap.class)) { // NOSONAR
            LinkedHashMap<?, ?> objAsLinkedHashMap = (LinkedHashMap<?, ?>) object;
            Collection<?> allValues = objAsLinkedHashMap.values();
            objArray = new Object[allValues.size()][1];
            int i = 0;
            for (Object eachValue : allValues) {
                objArray[i][0] = eachValue;
                i++;
            }
        }

        // Converts an ArrayList (e.g. Yaml List) to an Object double array.
        else if (rootClass.equals(ArrayList.class)) { // NOSONAR
            ArrayList<?> objAsArrayList = (ArrayList<?>) object;
            objArray = new Object[objAsArrayList.size()][1];

            int i = 0;
            for (Object eachArrayListObject : objAsArrayList) {

                /*
                 * Handles LinkedHashMap nested in a LinkedHashMap (e.g. Yaml associative array). This block removes the
                 * first mapping since that data serves as visual organization of data within a Yaml. If the parent is a
                 * LinkedHashMap and the child is a LinkedHashMap or an ArrayList, then assign the child to the Object
                 * double array instead of the parent.
                 */
                objArray[i][0] = eachArrayListObject;
                if (eachArrayListObject.getClass().equals(LinkedHashMap.class)) { // NOSONAR
                    LinkedHashMap<?, ?> eachArrayListObjectAsHashMap = (LinkedHashMap<?, ?>) eachArrayListObject;
                    for (Object eachEntry : eachArrayListObjectAsHashMap.values()) {
                        if (eachEntry.getClass().equals(LinkedHashMap.class) // NOSONAR
                                || eachEntry.getClass().equals(ArrayList.class)) { // NOSONAR
                            objArray[i][0] = eachEntry;
                        }
                    }
                }
                i++;
            }
        }

        // Converts an List of simple types to an Object double array.
        else if (rootClass.isArray()) {
            int i = 0;

            if (!(object instanceof Object[])) {
                if (object instanceof int[]) {
                    objArray = new Object[((int[]) object).length][1];
                    for (int item : (int[]) object) {
                        objArray[i++][0] = item;
                    }
                } else if (object instanceof char[]) {
                    objArray = new Object[((char[]) object).length][1];
                    for (char item : (char[]) object) {
                        objArray[i++][0] = item;
                    }
                } else if (object instanceof short[]) {
                    objArray = new Object[((short[]) object).length][1];
                    for (short item : (short[]) object) {
                        objArray[i++][0] = item;
                    }
                } else if (object instanceof boolean[]) {
                    objArray = new Object[((boolean[]) object).length][1];
                    for (boolean item : (boolean[]) object) {
                        objArray[i++][0] = item;
                    }
                } else if (object instanceof long[]) {
                    objArray = new Object[((long[]) object).length][1];
                    for (long item : (long[]) object) {
                        objArray[i++][0] = item;
                    }
                } else if (object instanceof double[]) {
                    objArray = new Object[((double[]) object).length][1];
                    for (double item : (double[]) object) {
                        objArray[i++][0] = item;
                    }
                } else if (object instanceof float[]) {
                    objArray = new Object[((float[]) object).length][1];
                    for (float item : (float[]) object) {
                        objArray[i++][0] = item;
                    }
                } else if (object instanceof byte[]) {
                    objArray = new Object[((byte[]) object).length][1];
                    for (byte item : (byte[]) object) {
                        objArray[i++][0] = item;
                    }
                }
            }

            // Converts unknown object Array to an Object double array.
            else {
                objArray = new Object[((Object[]) object).length][1];
                for (Object item : (Object[]) object) {
                    objArray[i++][0] = item;
                }
            }
        }

        // Passing no arguments to exiting() because implementation to print 2D array could be highly recursive.
        return objArray;
    }
}
