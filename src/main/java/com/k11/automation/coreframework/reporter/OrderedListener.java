package com.k11.automation.coreframework.reporter;

import com.vimalselvam.testng.listener.ExtentTestNgFormatter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

import java.util.List;

public class OrderedListener extends ExtentTestNgFormatter {

    /**
     * Generate a report for the given suites into the specified output directory.
     */
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        super.generateReport(xmlSuites, suites, outputDirectory);
        EmailableReporter emailReport = new EmailableReporter();
        emailReport.generateReport(xmlSuites, suites, outputDirectory);
    }
}
