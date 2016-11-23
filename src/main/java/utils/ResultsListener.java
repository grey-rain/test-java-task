package utils;

import dto.TestResultDto;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.ArrayList;
import java.util.List;

public class ResultsListener implements ITestListener {

    private List<TestResultDto> results = new ArrayList<TestResultDto>();
    private TestResultDto testResultDto;

    public void onTestStart(ITestResult iTestResult) {
        testResultDto = new TestResultDto();
        Object[] data = iTestResult.getParameters();
        testResultDto.setSender(data[0].toString());
    }

    public void onTestSuccess(ITestResult iTestResult) {
        testResultDto.setStatus("Passed");
        results.add(testResultDto);
    }

    public void onTestFailure(ITestResult iTestResult) {
        testResultDto.setStatus("Failed");
        testResultDto.setException(iTestResult.getThrowable().getClass().getCanonicalName());
        results.add(testResultDto);
    }

    public void onTestSkipped(ITestResult iTestResult) {
        testResultDto.setStatus("Skipped");
        results.add(testResultDto);
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    public void onStart(ITestContext iTestContext) {
    }

    public void onFinish(ITestContext iTestContext) {
        System.out.print("===============================================\n");
        System.out.print("Sender Account, Passed / Failed, Cause\n");
        System.out.print("===============================================\n");
        for (TestResultDto resultDto : results) {
            System.out.println(resultDto.getSender() + ", " + resultDto.getStatus() + ", " + resultDto.getException());
            System.out.print("-----------------------------------------------\n");
        }
    }
}
