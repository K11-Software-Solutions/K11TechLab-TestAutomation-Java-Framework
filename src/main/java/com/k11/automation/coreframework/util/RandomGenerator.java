package com.k11.automation.coreframework.util;
import com.google.common.base.CharMatcher;
import org.apache.commons.lang3.RandomStringUtils;
import com.k11.automation.coreframework.exceptions.AutomationError;
import com.k11.automation.selenium.baseclasses.constants.PermittedCharacters;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.*;

public final class RandomGenerator {

    public static String random(Integer length, PermittedCharacters permittedCharacters) {
        String randomString = null;
        if (PermittedCharacters.ALPHABETS.equals(permittedCharacters)) {
            randomString = randomString(length);
        } else if (PermittedCharacters.NUMERIC.equals(permittedCharacters)) {
            randomString = randomInteger(length);
        } else if (PermittedCharacters.ALPHANUMERIC.equals(permittedCharacters)) {
            randomString = randomAlphanumeric(length);
        } else if (PermittedCharacters.ANY_CHARACTERS.equals(permittedCharacters)) {
            randomString = randomAsciiCharacters(length);
        } else if (PermittedCharacters.ANY_CHARACTERS_SUPPORTS_MULTILINGUAL.equals(permittedCharacters)) {
            randomString = randomAsciiCharacters(length);
        }
        return randomString;
    }

    /**
     * Generates random Number.
     *
     * @param length length of random number to be generated
     */
    private static String randomInteger(Integer length) {
        return RandomStringUtils.randomNumeric(length);
    }

    /**
     * Generates random String.
     *
     * @param length length of random characters to be generated
     */
    private static String randomString(Integer length) {
        return RandomStringUtils.random(length, true, false);
    }

    /**
     * Generates random alphanumeric String.
     *
     * @param length length of random alphanumeric characters to be generated
     */
    private static String randomAlphanumeric(Integer length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    /**
     * Generates random alphabetic String.
     *
     * @param length length of random alphabetic characters to be generated
     */
    public static String randomAlphabetic(Integer length) {
        return RandomStringUtils.randomAlphabetic(length).toLowerCase();
    }

    /**
     * Generates random emailaddress for @example.com domain  in lower case
     *
     * @param length length of random alphanumeric characters to be generated for the local part of email address
     */
    public static String randomEmailAddress(Integer length) {
        String email = randomAlphanumeric(length) + "@example.com";
        return email.toLowerCase();
    }

    /*	To Generate Random E-Mail IDs(Auto Generate Domain Names.*/
    public static String GenerateRandomEMAILIDs()
    {
        String EmailID = RandomStringUtils.randomAlphabetic(15).toString();
        String Domain = RandomStringUtils.randomAlphabetic(7).toLowerCase().toString();

        return EmailID + "@" + Domain + ".com";
    }

    /*	To Generate Random E-Mail IDs.*/
    public static String GenerateRandomEMAILIDs(String DomainName)
    {
        String EmailID = RandomStringUtils.randomAlphabetic(15).toString();

        return EmailID + "@" + DomainName ;
    }

    /**
     * Generates random gender in short text form "M" , "F" , "U"
     * M = Male , F = Female , U = Unspecified
     */
    public static String randomGenderShortText() {
        List<String> gender = new LinkedList<>();
        gender.add("M");
        gender.add("F");
        gender.add("U");
        Random rand = new Random();
        int choice = rand.nextInt(gender.size());
        return gender.get(choice);
    }

    /**
     * Generates random gender in full text form
     * Male , Female , Unspecified
     */
    public static String randomGenderFullText() {
        List<String> gender = new LinkedList<>();
        gender.add("Male");
        gender.add("Female");
        Random rand = new Random();
        int choice = rand.nextInt(gender.size());
        return gender.get(choice);
    }

    /**
     * Generates random plus or minus
     * "-" , "+"
     */

    public static String randomPlusOrMinus() {
        List<String> item = new LinkedList<>();
        item.add("-");
        item.add("+");

        Random rand = new Random();
        int choice = rand.nextInt(item.size());
        return item.get(choice);
    }


    public static DateTime randomAdultsDOB() {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.minusYears((int) (18 + (Math.random() * ((50 - 18) + 1))));
        return dateTime.minusYears((int) (18 + (Math.random() * ((50 - 18) + 1))));
    }

    public static String formatDate(DateTime dateTime, String dateformat) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(dateformat);
        return dateTime.toString(dateTimeFormatter);
    }

    public static DateTimeFormatter dateFormatterWithLocale(Locale locale) {
        return DateTimeFormat.mediumDate().withLocale(locale);
    }

    public static String dateWithNoLeadingZero(String dateWithLeadingZero) {
        String dateWithNoLeadingZero;
        dateWithNoLeadingZero = CharMatcher.is('0').trimLeadingFrom(dateWithLeadingZero);
        return dateWithNoLeadingZero;
    }

    public static String randomFutureFormattedDate(String dateformat) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(dateformat);
        DateTime dateTime = new DateTime();
        final DateTime plusYears = dateTime.plusYears((int) (1 + Math.random() * (10 - 1)));
        return plusYears.toString(dateTimeFormatter);
    }

    private static String randomAsciiCharacters(Integer characterAmount) {
        return RandomStringUtils.random(characterAmount, 32, 127, false, false);
    }

    public static String generateRandomBirthdate() {
        GregorianCalendar gc = new GregorianCalendar();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int year = generateRandomInt(System.currentTimeMillis(), currentYear - 99, currentYear - 19);
        gc.set(gc.YEAR, year);
        int dayOfYear = generateRandomInt(System.currentTimeMillis(), 1, gc.getActualMaximum(gc.DAY_OF_YEAR));
        gc.set(gc.DAY_OF_YEAR, dayOfYear);
        String randomBirthdate = twoDigitMonth(gc) + "/" + twoDigitDay(gc) + "/" + gc.get(gc.YEAR);
        return randomBirthdate;
    }

    public static String generateRandomBirthdate(String format) {
        if(format.equals("yyyy-MM-dd"))
            return DateUtil.getFormattedDate(new Date(generateRandomBirthdate()),format);
        else
            throw new AutomationError("Invalid Date format.");
    }

    public static String generateRandomBirthdateForChild() {
        GregorianCalendar gc = new GregorianCalendar();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int year = generateRandomInt(System.currentTimeMillis(), currentYear - 18, currentYear - 1);
        gc.set(gc.YEAR, year);
        int dayOfYear = generateRandomInt(System.currentTimeMillis(), 1, gc.getActualMaximum(gc.DAY_OF_YEAR));
        gc.set(gc.DAY_OF_YEAR, dayOfYear);
        String randomBirthdate = twoDigitMonth(gc) + "/" + twoDigitDay(gc) + "/" + gc.get(gc.YEAR);
        return randomBirthdate;
    }

    public static int generateRandomInt(long seed, int lowerbound, int upperbound) {
        Random rand = new Random(seed);
        int randomInteger = rand.nextInt(upperbound - lowerbound) + lowerbound;
        return randomInteger;
    }

    public static int getTenDigitRandomNumber() {
        Random random = new Random();
        return random.nextInt(1_000_000_000) + 1_000_000_000;
    }

    public static String twoDigitMonth(GregorianCalendar gc) {
        String twoDigitMonth = "";
        int month = gc.get(gc.MONTH) + 1;
        if (month < 10) {
            twoDigitMonth = "0" + month;
        } else {
            twoDigitMonth = month + "";
        }
        return twoDigitMonth;
    }

    public static String twoDigitDay(GregorianCalendar gc) {
        String twoDigitDay = "";
        int day = gc.get(gc.DAY_OF_MONTH);
        if (day < 10) {
            twoDigitDay = "0" + day;
        } else {
            twoDigitDay = day + "";
        }
        return twoDigitDay;
    }

    public static String generateRandomGenericSsn() {
        String ssn = "";
        String firstPart;
        String secondPart;
        String thirdPart;
        boolean valid = false;
        int randomNr = 0;
        randomNr = generateRandomInt(System.currentTimeMillis(), 1, 799);
        firstPart = String.format("%03d", randomNr);
        randomNr = generateRandomInt(System.currentTimeMillis(), 1, 99);
        secondPart = String.format("%02d", randomNr);
        randomNr = generateRandomInt(System.currentTimeMillis(), 1, 9999);
        thirdPart = String.format("%04d", randomNr);
        ssn = firstPart + "-" + secondPart + "-" + thirdPart;
        return ssn;
    }

    public static String generateRandomSsn() {
        String ssn = "";
        String firstPart;
        String secondPart;
        String thirdPart;
        boolean valid = false;
        int randomNr = 0;
        while (!valid) {
            randomNr = generateRandomInt(System.currentTimeMillis(), 1, 899);
            if (randomNr == 600 || randomNr == 123 || randomNr == 234 || randomNr == 345 || randomNr == 456
                    || randomNr == 567 || randomNr == 678 || randomNr == 789 || randomNr == 111
                    || randomNr == 222 || randomNr == 333 || randomNr == 444 || randomNr == 555
                    || randomNr == 666 || randomNr == 777 || randomNr == 888 || randomNr == 999) {
                valid = false;
            } else {
                valid = true;
            }
        }
        firstPart = String.format("%03d", randomNr);
        randomNr = generateRandomInt(System.currentTimeMillis(), 1, 99);
        secondPart = String.format("%02d", randomNr);
        randomNr = generateRandomInt(System.currentTimeMillis(), 1, 9999);
        thirdPart = String.format("%04d", randomNr);
        ssn = firstPart + "-" + secondPart + "-" + thirdPart;
        return ssn;
    }

}
