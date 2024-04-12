package com.k11.automation.coreframework.util;

import com.k11.automation.coreframework.logger.Log;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.testng.Reporter;
import org.testng.SkipException;

public class Validator {
    public static <T> boolean verifyThat(String reason, T actual, Matcher<? super T> matcher) {
        boolean result = matcher.matches(actual);
        Description description = new StringDescription();
        description.appendText(reason).appendText("\nExpected: ").appendDescriptionOf(matcher)
                .appendText("\n     Actual: ");

        matcher.describeMismatch(actual, description);
        String msg = description.toString();
        if (msg.endsWith("Actual: ")) {
            msg = String.format(msg + "%s", actual);
        }
        msg = msg.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        Reporter.log(msg + ": "+ (result ? "PASS": "FAIL"));

        return result;
    }

    public static <T> boolean verifyThat(T actual, Matcher<? super T> matcher) {
        return verifyThat("", actual, matcher);
    }

    public static <T> void assertThat(T actual, Matcher<? super T> matcher) {
        assertThat("", actual, matcher);
    }

    public static <T> void assertThat(String reason, T actual, Matcher<? super T> matcher) {
        if (!verifyThat(reason, actual, matcher)) {
            throw new AssertionError();
        }
    }

    public static <T> void givenThat(String reason, T actual, Matcher<? super T> matcher) {
        if (!verifyThat(reason, actual, matcher)) {
            throw new SkipException("Precondition not satisfied");
        }
    }

    public static <T> void givenThat(T actual, Matcher<? super T> matcher) {
        givenThat("", actual, matcher);
    }

    public static boolean verifyTrue(boolean condition, String failMessage,
                                     String successMsg) {
        if (condition) {
            Log.LOGGER.info(successMsg+ ": "+ "PASS");
            Reporter.log(successMsg+ ": "+ "PASS");
        } else {
            Log.LOGGER.info(failMessage+ ": "+ "FAIL");
            Reporter.log(failMessage+ ": "+ "FAIL");
        }
        return condition;

    }

    public static boolean verifyFalse(boolean condition, String failMessage,
                                      String successMsg) {
        if (!condition) {
            Log.LOGGER.info(successMsg+ ": "+ "PASS");
        } else {
            Log.LOGGER.info(failMessage+ ": "+ "FAIL");
        }
        return !condition;

    }
    public static void assertTrue(boolean condition, String failMessage,
                                  String successMsg) {
        if (!verifyTrue(condition, failMessage, successMsg)) {
            throw new AssertionError(failMessage);
        }
    }

    public static void assertFalse(boolean condition, String failMessage,
                                   String successMsg) {
        if (!verifyFalse(condition, failMessage, successMsg)) {
            throw new AssertionError(failMessage);
        }
    }
}
