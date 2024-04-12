package com.k11.automation.coreframework.util.fileUtil;

import com.google.common.base.Preconditions;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Utilitarian class that provides simple file I/O operations
 */
public class FileAssistant {
    private FileAssistant() {

    }

    /**
     * Load a file resource via the {@link ClassLoader}
     * 
     * @param fileName
     *            The name of the file
     * @return An object of type {@link InputStream} that represents the stream of a file that was read from the file
     *         system.
     */
    public static InputStream loadFile(String fileName) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream iStream = loader.getResourceAsStream(fileName);
        if (iStream == null) {
            try {
                iStream = new FileInputStream(fileName);
            } catch (FileNotFoundException e) { // NOSONAR
                // Gobbling the checked exception here and doing nothing with it
                // because we are explicitly checking if the inputstream is null
                // and then throwing a runtime exception
            }
        }
        if (iStream == null) {
            throw new IllegalArgumentException("[" + fileName + "] is not a valid resource");
        }
        return iStream;
    }

    /**
     * Load a file resource via the {@link ClassLoader}
     * 
     * @param file
     *            An object of type {@link File} which represents a file object
     * @return An object of type {@link InputStream} that represents the stream of a file that was read from the file
     *         system.
     */
    public static InputStream loadFile(File file) {
        return loadFile(file.getAbsolutePath());
    }

    /**
     * Read a file resource via the {@link ClassLoader}. Return it as a {@link String}.
     * 
     * @param fileName
     *            The file name can either be an absolute path or a relative path from the project's base directory.
     * @return content of the file
     * @throws IOException
     */
    public static String readFile(String fileName) throws IOException {
        Preconditions.checkArgument(StringUtils.isNotBlank(fileName), "File name cannot be null (or) empty.");
        StringBuilder output = new StringBuilder();
        try (BufferedReader buffreader = new BufferedReader(new InputStreamReader(loadFile(fileName),
                Charset.forName("UTF-8")))) {
            String line = null;
            while ((line = buffreader.readLine()) != null) {
                output.append(line);
            }
        }
        return output.toString();
    }

    /**
     * Write an {@link InputStream} to a file
     * @param isr the {@link InputStream} 
     * @param fileName The target file name to use. Do not include the path.
     * @param outputFolder The target folder to use.
     * @throws IOException
     */
    public static void writeStreamToFile(InputStream isr, String fileName, String outputFolder) throws IOException {
        FileUtils.copyInputStreamToFile(isr, new File(outputFolder + "/" + fileName));
    }
}