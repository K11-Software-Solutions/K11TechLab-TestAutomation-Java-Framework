package com.k11.automation.coreframework.k11CustomDataProvider.Impl;

import com.k11.automation.coreframework.k11CustomDataProvider.DataResource;
import com.k11.automation.coreframework.util.fileUtil.FileAssistant;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * FileSystemResource defines the file name of the data source to be used for data provider consumption. Loading a
 * file (Yaml for e.g.,) containing user-defined (complex) objects also requires passing in the object class.
 * Passing in a complex object with nested complex objects does not require any additional parameters.
 * 
 * <br>
 * <h3>Sample usage:</h3>
 * 
 * <pre>
 * public static Object[][] dataProviderGetAllDocuments() throws IOException {
 *     FileSystemResource resource = new FileSystemResource(fileName, USER.class);
 *     Object[][] data = YamlDataProvider.getAllData(resource);
 *     return data;
 * }
 * </pre>
 * 
 * If test requires passing multiple arguments, multiple YamlResources can be defined to create such a data provider.
 * 
 * <br>
 * <h3>Sample usage:</h3>
 * 
 * <pre>
 * public static Object[][] dataProviderGetMultipleArguments() throws IOException {
 *     List&lt;FileSystemResource&gt; yamlResources = new ArrayList&lt;FileSystemResource&gt;();
 *     yamlResources.add(new FileSystemResource(fileName1, USER.class));
 *     yamlResources.add(new FileSystemResource(fileName2, USER.class));
 * 
 *     Object[][] data = YamlDataProvider.getAllDataMultipleArgs(yamlResources);
 * 
 *     return data;
 * }
 * </pre>
 * 
 * Test method signature example:
 * 
 * <pre>
 *   public void testExample(USER user1, USER user2)
 * </pre>
 * 
 * 
 */
public class FileSystemResource implements DataResource {

    private String fileName;
    private Class<?> cls;

    /**
     * Use this constructor when a data source file can be found as a resource and contains a user-defined object.
     * 
     * @param fileName
     * @param cls
     */
    public FileSystemResource(String fileName, Class<?> cls) {
        this.fileName = fileName;
        this.cls = cls;
    }


    /**
     * Use this constructor when a data source file can be found as a resource and does NOT contain a user-defined object.
     * 
     * @param fileName
     */
    public FileSystemResource(String fileName) {
        this(fileName, null);
    }

    @Override
    public Class<?> getCls() {
        return cls;
    }

    @Override
    public final void setCls(Class<?> cls) {
        this.cls = cls;
    }

    @Override
    public InputStream getInputStream() {
        return new BufferedInputStream(FileAssistant.loadFile(this.fileName));
    }

    @Override
    public String getType() {

        if(fileName == null || StringUtils.isBlank(fileName)) {
            return null;
        }
        //Extract the file extension and format to upper case
        return StringUtils.substringAfterLast(fileName, ".");
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("FileSystemResource: [ ");
        str.append("fileName = " + this.fileName + ", ");
        str.append("class = " + this.cls + " ]");
        return str.toString();
    }
}