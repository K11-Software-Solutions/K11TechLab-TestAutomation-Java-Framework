package com.k11.automation.coreframework.util;
import com.k11.automation.coreframework.logger.Log;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";

	/**
	 * Convert date string from one format to another format.
	 * formatDate("2012-01-11",
				"yyy-MM-dd", "MMM d, yyyy"))
	 * will retrun "Jan 11, 2012"
	 * @param dateString
	 *            : date string to be formated
	 * @param formatFrom
	 *            : format of the given date string
	 * @param formatTo
	 *            : String expected format
	 * @return date string in expected format
	 */
	public static String getFormattedDate(String dateString, String formatFrom, String formatTo) {
		SimpleDateFormat aformat = new SimpleDateFormat(formatFrom);
		SimpleDateFormat eformat = new SimpleDateFormat(formatTo);
		Date d;
		try {
			d = aformat.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
		return eformat.format(d);
	}

	public static String getFormattedDate(Date date, String formatTo, String toLocale) {
		SimpleDateFormat eformat = new SimpleDateFormat(formatTo, new Locale(toLocale));
		return eformat.format(date);
	}

	public static String getFormattedDate(String dateString, String formatFrom, String formatTo, String fromLocale,
										  String toLocale) {
		SimpleDateFormat aformat = new SimpleDateFormat(formatFrom, new Locale(fromLocale));
		SimpleDateFormat eformat = new SimpleDateFormat(formatTo, new Locale(toLocale));
		Date d;
		try {
			d = aformat.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
		return eformat.format(d);
	}

	/**
	 * Get date for today, before/after today as string
	 * 
	 * @param days
	 *            : number of days to be added in current day.
	 * @param format
	 *            : (option) format in which require date string, default is
	 *            "MM/dd/yyyy"
	 * @return date string for expected day. Example:
	 *         <ol>
	 *         <li>getDate(0) will return today's date
	 *         <li>getDate(1) will return tomorrow's date
	 *         <li>getDate(-1) will return yesterday's date
	 *         </ol>
	 */
	public static String getDate(int days, String... format) {
		String outformat = (format == null) || (format.length < 1) ? DEFAULT_DATE_FORMAT : format[0];

		return new SimpleDateFormat(outformat).format(getDate(days));

	}

	/**
	 * Get date for today, before/after today. Example:
	 * <ol>
	 * <li>getDate(0) will return today's date
	 * <li>getDate(1) will return tomorrow's date
	 * <li>getDate(-1) will return yesterday's date
	 * </ol>
	 * 
	 * @param days
	 * @return date
	 */
	public static Date getDate(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}

	/**
	 * Get date after/before base date. Example:
	 * <ol>
	 * <li>getDate(baseDate, 5) will return 5 day's date after baseDate
	 * <li>getDate(baseDate, -5) will return 5 day's date before baseDate
	 * </ol>
	 * 
	 * @param date
	 * @param days
	 * @return date
	 */
	public static Date getDateAfter(Date date, int days) {
		Calendar cal = getCalendarDate(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}


    public static Date getMonthsAfter(Date date, int months) {
        Calendar cal = getCalendarDate(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

	public static int getMonthsDifference(Date date1, Date date2) {

		Calendar cal1 = getCalendarDate(date1);
		Calendar cal2 = getCalendarDate(date2);

		int m1 = (cal1.get(Calendar.YEAR) * 12) + cal1.get(Calendar.MONTH);
		int m2 = (cal2.get(Calendar.YEAR) * 12) + cal2.get(Calendar.MONTH);
		return m2 - m1;

	}

	/**
	 * Returns number of days between date1 and date2, such that date1 + days =
	 * date2
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDateDifference(Date date1, Date date2) {
		int oneDay = 1000 * 60 * 60 * 24;
		Calendar cal1 = getCalendarDate(date1);
		Calendar cal2 = getCalendarDate(date2);

		return (int) ((cal1.getTimeInMillis() - cal2.getTimeInMillis()) / oneDay);
	}

	public static Calendar getCalendarDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static Date parseDate(String dateStr, String format) {
		Date date = null;
		if (StringUtil.isBlank(format)) {
			format = DEFAULT_DATE_FORMAT;
		}
		try {
			date= new SimpleDateFormat(format).parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			Log.LOGGER.debug("Unable to parse the Date. "+dateStr);
		}
		return date;
	}

	public static Date parseDate(String d) {
		String result = null;
		Date dateObject = null;

		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			dateObject = dateFormatter.parse(d);
			dateFormatter.applyPattern("MM/dd/yyyy");
			result = dateFormatter.format(dateObject);
			dateObject=new SimpleDateFormat("MM/dd/yyyy").parse(result);
		} catch (ParseException e) {
			System.out.println(e);
		}

		return dateObject;

	}

	public static String getFormattedDate(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}


	public static String getFromattedDateNumberOfDaysAgo(int numberOfDaysAgo){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -numberOfDaysAgo);
		java.util.Date newDate = cal.getTime();

		Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = formatter.format(newDate);

		return formattedDate;
	}

	public static String getFirstDayOfNextMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date nextMonthFirstDay = calendar.getTime();
		Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = formatter.format(nextMonthFirstDay);
		System.out.println(formattedDate);
		return formattedDate;
	}

	public static String getFirstDayOfLastMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date nextMonthFirstDay = calendar.getTime();
		Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = formatter.format(nextMonthFirstDay);
		System.out.println(formattedDate);
		return formattedDate;
	}

	public static String getFirstDayOfMonthsAgo(int numberOfMonthsAgo) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -numberOfMonthsAgo);
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date nextMonthFirstDay = calendar.getTime();
		Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = formatter.format(nextMonthFirstDay);
		System.out.println(formattedDate);
		return formattedDate;
	}

    public static String getFirstDayOfCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date currentMonthFirstDay = calendar.getTime();
        Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = formatter.format(currentMonthFirstDay);
        System.out.println(formattedDate);
        return formattedDate;
    }
}
