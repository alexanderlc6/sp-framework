package com.sp.framework.orm.lark.orm.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;

/**
 * @author abba
 */
public class DateConvertUtils {

	public static java.util.Date parse(String dateString, String dateFormat) {
		return parse(dateString, dateFormat, java.util.Date.class);
	}

	@SuppressWarnings("unchecked")
	public static <T extends java.util.Date> T parse(String dateString, String dateFormat, Class<T> targetResultType) {
		if (StringUtils.isEmpty(dateString))
			return null;
		if (dateString.length() == 10 && dateString.indexOf("/") > -1) {
			dateFormat = "MM/dd/yyyy";
		} else if (dateString.length() == 10 && dateString.indexOf("-") > -1) {
			dateFormat = "yyyy-MM-dd";
		}
		DateFormat df = new SimpleDateFormat(dateFormat);
		try {
			long time = df.parse(dateString).getTime();
			java.util.Date t = targetResultType.getConstructor(long.class).newInstance(time);
			return (T) t;
		} catch (ParseException e) {
			String errorInfo = "cannot use dateformat:" + dateFormat + " parse datestring:" + dateString;
			throw new IllegalArgumentException(errorInfo, e);
		} catch (Exception e) {
			throw new IllegalArgumentException("error targetResultType:" + targetResultType.getName(), e);
		}
	}

	public static String format(java.util.Date date, String dateFormat) {
		if (date == null)
			return null;
		DateFormat df = new SimpleDateFormat(dateFormat);
		return df.format(date);
	}
}
