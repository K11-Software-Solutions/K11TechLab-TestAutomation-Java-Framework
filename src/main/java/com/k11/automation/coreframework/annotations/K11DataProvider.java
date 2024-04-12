package com.k11.automation.coreframework.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ METHOD, TYPE })
public @interface K11DataProvider {
	/**
	 * Represents meta-data for data provider
	 */
	public enum params {
		DATAFILE, SHEETNAME, KEY, DATAPROVIDER, DATAPROVIDERCLASS, FILTER, FROM, TO, INDICES;
	}

	public static final String NAME= "k11-data-provider";
	public static final String NAME_PARALLEL = "k11-data-provider-parallel";

	/**
	 * Used to provide csv or excel file. 
	 * 
	 * @return
	 */
	String dataFile() default "";

	/**
	 * Optional sheet name (value or property) for excel file. If not provided
	 * first sheet will be considered. 
	 *
	 * @return
	 */
	String sheetName() default "";

	/**
	 * Optional flag to indicate excel data contains header row that need to be
	 * skipped. Default value is false.
	 *
	 * @return
	 */
	boolean hasHeaderRow() default false;

	/***
	 * Optional data label name in excel sheet. Required if want to provide data
	 * start/end cell marked with label.
	 *
	 * @return
	 */
	String key() default "";

	String filter() default "";
}