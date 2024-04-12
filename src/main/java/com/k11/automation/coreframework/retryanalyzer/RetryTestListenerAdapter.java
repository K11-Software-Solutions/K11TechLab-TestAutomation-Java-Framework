package com.k11.automation.coreframework.retryanalyzer;
import com.k11.automation.selenium.baseclasses.ApplicationProperties;
import org.testng.*;

import java.util.Set;

public class RetryTestListenerAdapter extends TestListenerAdapter {

    public void onTestFailure(ITestResult result) {
        if(!(ApplicationProperties.RETRY_CNT.getIntVal()==0)) {
            if (result.getMethod().getRetryAnalyzer() != null) {
                RetryAnalyzer retryAnalyzer = (RetryAnalyzer)result.getMethod().getRetryAnalyzer();
                if (retryAnalyzer.getRetryCount()>0) {
                    result.setStatus(ITestResult.SKIP);
                } else {
                 result.setStatus(ITestResult.FAILURE);
                }
                Reporter.setCurrentTestResult(result);
            }
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        Set<ITestResult> failedTests = context.getFailedTests().getAllResults();
        for (ITestResult temp : failedTests) {
            ITestNGMethod method = temp.getMethod();
            if (context.getFailedTests().getResults(method).size() > 1) {
                failedTests.remove(temp);
            } else {
                if (context.getPassedTests().getResults(method).size() > 0) {
                    failedTests.remove(temp);
                }
            }
        }
    }
}
