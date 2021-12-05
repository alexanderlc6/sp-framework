/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (smallchill@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sp.framework.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {


	/**
	 * 获取YYYY格式
	 *
	 * @return
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 获取YYYY格式
	 *
	 * @return
	 */
	public static String getYear(Date date) {
		return formatDate(date, "yyyy");
	}

	/**
	 * 获取YYYY-MM-DD格式
	 *
	 * @return
	 */
	public static String getDay() {
		return formatDate(new Date(), "yyyy-MM-dd");
	}

	/**
	 * 获取YYYY-MM-DD格式
	 *
	 * @return
	 */
	public static String getDay(Date date) {
		return formatDate(date, "yyyy-MM-dd");
	}

	/**
	 * 获取YYYYMMDD格式
	 *
	 * @return
	 */
	public static String getDays() {
		return formatDate(new Date(), "yyyyMMdd");
	}

	/**
	 * 获取YYYYMMDD格式
	 *
	 * @return
	 */
	public static String getDays(Date date) {
		return formatDate(date, "yyyyMMdd");
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 *
	 * @return
	 */
	public static String getTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss.SSS格式
	 *
	 * @return
	 */
	public static String getMsTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
	}

	/**
	 * 获取YYYYMMDDHHmmss格式
	 *
	 * @return
	 */
	public static String getAllTime() {
		return formatDate(new Date(), "yyyyMMddHHmmss");
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 *
	 * @return
	 */
	public static String getTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String formatDate(Date date, String pattern) {
		if (date == null){
			return null;
		}
		String formatDate = null;
		if (StringUtils.isNotBlank(pattern)) {
			formatDate = DateFormatUtils.format(date, pattern);
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * @Title: compareDate
	 * @Description:(日期比较，如果s>=e 返回true 否则返回false)
	 * @param s
	 * @param e
	 * @return boolean
	 * @throws
	 * @author luguosui
	 */
	public static boolean compareDate(String s, String e) {
		if (parseDate(s) == null || parseDate(e) == null) {
			return false;
		}
		return parseDate(s).getTime() >= parseDate(e).getTime();
	}

	/**
	 * 格式化日期
	 *
	 * @return
	 */
	public static Date parseDate(String date) {
		return parse(date,"yyyy-MM-dd");
	}

	/**
	 * 格式化日期
	 *
	 * @return
	 */
	public static Date parseTime(String date) {
		return parse(date,"yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 格式化日期
	 *
	 * @return
	 */
	public static Date parse(String date, String pattern) {
		try {
			return DateUtils.parseDate(date,pattern);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 格式化日期
	 *
	 * @return
	 */
	public static String format(Date date, String pattern) {
		return DateFormatUtils.format(date, pattern);
	}

	/**
	 * 把日期转换为Timestamp
	 *
	 * @param date
	 * @return
	 */
	public static Timestamp format(Date date) {
		return new Timestamp(date.getTime());
	}

	/**
	 * 校验日期是否合法
	 *
	 * @return
	 */
	public static boolean isValidDate(String s) {
		return parse(s, "yyyy-MM-dd HH:mm:ss") != null;
	}

	/**
	 * 校验日期是否合法
	 *
	 * @return
	 */
	public static boolean isValidDate(String s, String pattern) {
        return parse(s, pattern) != null;
	}

	public static int getDiffYear(String startTime, String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(
					startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}

	/**
	 * <li>功能描述：时间相减得到天数
	 *
	 * @param beginDateStr
	 * @param endDateStr
	 * @return long
	 * @author Administrator
	 */
	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd");
		Date beginDate = null;
		Date endDate = null;

		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
		// System.out.println("相隔的天数="+day);

		return day;
	}

	/**
	 * 得到n天之后的日期
	 *
	 * @param days
	 * @return
	 */
	public static String getAfterDayDate(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdfd.format(date);

		return dateStr;
	}

	/**
	 * 得到n天之后是周几
	 *
	 * @param days
	 * @return
	 */
	public static String getAfterDayWeek(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("E");
		String dateStr = sdf.format(date);

		return dateStr;
	}

	public static final String YMD = "yyyy-MM-dd";
	public static final String YMD_HM = "yyyy-MM-dd HH:mm";
	public static final String YMD_HMS = "yyyy-MM-dd HH:mm:ss";

	/**
	 * @Param dayOffset 天数偏离
	 * @return
	 * 获取日期，
	 */
	public static Date getDateWithMinutes(Date date,Integer minuteOffset){
		Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minuteOffset);//取当前日期的30天
		return cal.getTime();
	}

	/**
	 * @Param dayOffset 天数偏离
	 * @return
	 * 获取日期，
	 */
	public static Date getCalendarDate(Integer dayOffset){
		Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
		cal.add(Calendar.DAY_OF_MONTH, dayOffset);//取当前日期的30天
		return cal.getTime();
	}

	/**
	 * @Param dayOffset 天数偏离
	 * @return
	 * 获取日期，
	 */
	public static Date getCalendarDate(Date date,Integer dayOffset){
		Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, dayOffset);//取当前日期的30天
		return cal.getTime();
	}

	/**
	 * @Param dayOffset 周数偏离
	 * @return
	 * 获取日期，
	 */
	public static Date getLaskWeekMonday(Integer weekOffset){
		Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.add(Calendar.WEEK_OF_YEAR, weekOffset);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return cal.getTime();
	}

	/**
	 * @Param dayOffset 周数偏离
	 * @return
	 * 获取日期，
	 */
	public static Date getLaskWeekSunDay(Integer weekOffset){
		Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.add(Calendar.WEEK_OF_YEAR, weekOffset);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return cal.getTime();
	}

	/**
	 * @param date
	 * @return
	 * 获取时间的开始时间
	 */
	public static Date getStartTime(Date date){
		Calendar todayStart = Calendar.getInstance();
		todayStart.setTime(date);
		todayStart.set(Calendar.HOUR_OF_DAY, 0);
		todayStart.set(Calendar.MINUTE, 0);
		todayStart.set(Calendar.SECOND, 0);
		todayStart.set(Calendar.MILLISECOND, 0);
		return todayStart.getTime();
	}

	/**
	 * @param date
	 * @return
	 * 获取时间的结束时间
	 */
	public static Date getEndTime(Date date){
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.setTime(date);
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		todayEnd.set(Calendar.MILLISECOND, 999);
		return todayEnd.getTime();
	}

	/**
	 * @param date 时间
	 * @param hourOffset 小时偏移量
	 * @return 获取偏移后的时间
	 */
	public static Date getHourOffset(Date date,Integer hourOffset){
		Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
		cal.setTime(date);
		cal.add(Calendar.HOUR, hourOffset);
		return cal.getTime();
	}

	/**
	 * 获取当前日期 yyyy-MM-dd
	 * @return
	 */
	public static String getTodayYMD() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(YMD);
		String dateString = formatter.format(currentTime);
		return dateString;
	}


	/**
	 * 日期转字符串
	 * @param date
	 * @return
	 */
	public static String dateToStrYMD(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat(YMD);
		return formatter.format(date);
	}

	/**
	 * 日期转字符串
	 * @param date
	 * @return
	 */
	public static String dateToStrYMD_HMS(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat(YMD_HMS);
		return formatter.format(date);
	}


	/**
	 * 日期转字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateToStr(Date date,String pattern){
		if(date == null){
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}


	/**
	 * 字符串转日期
	 * @param str yyyy-MM-dd
	 * @return
	 */
	public static Date strToDateYMD(String str)throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(YMD);

			return formatter.parse(str);
	}


	/**
	 * 字符串转日期
	 * @param str yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Date strToDateYMD_HMS(String str) throws ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat(YMD_HMS);
			return formatter.parse(str);

	}



	/**
	 * 字符串转日期
	 * @param str
	 * @return
	 */
	public static Date strToDate(String str,String pattern)  throws ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			return formatter.parse(str);
	}


	/**
	 * 获取当前日期 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getTodayYMD_HMS() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(YMD_HMS);
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * @param date 时间
	 * @return 这周的哪一天
	 */
	public static Integer getDayOfWeek(Date date){
		//使用默认时区和语言环境获得一个日历。
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(date);
		Integer dayInt = cal.get(Calendar.DAY_OF_WEEK)-1;
		dayInt = dayInt == 0?7:dayInt;
		return dayInt;
	}

	/**
	 * @param date 时间
	 * @return 这周的哪一天
	 */
	public static String getDayOfWeekStr(Date date){
		Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(date);
		Integer dayInt = cal.get(Calendar.DAY_OF_WEEK)-1;
		dayInt = dayInt == 0?7:dayInt;
		String dateStr = null;
		switch (dayInt) {
			case 1:
				dateStr = "一";
				break;
			case 2:
				dateStr = "二";
				break;
			case 3:
				dateStr = "三";
				break;
			case 4:
				dateStr = "四";
				break;
			case 5:
				dateStr = "五";
				break;
			case 6:
				dateStr = "六";
				break;
			case 7:
				dateStr = "日";
				break;
			default:
				break;
		}
		return dateStr;
	}

	/**
	 * @param date 时间
	 * @return 天的哪一小时
	 */
	public static Integer getHourOfDay(Date date){
		Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * @param date 时间
	 * @return 小时的分钟
	 */
	public static Integer getMinuteOfHour(Date date){
		Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
		cal.setTime(date);
		return cal.get(Calendar.MINUTE);
	}

	/**
	 * 将日期进行加减运算
	 * @param startDate
	 * @param dayOffset
	 * @throws ParseException
	 */
	public static Date addDate(String startDate, Integer dayOffset) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(YMD_HMS);
		Date date=sdf.parse(startDate);
		Calendar cld=Calendar.getInstance();
		cld.setTime(date);
		cld.add(Calendar.DATE, dayOffset);

		return cld.getTime();
	}

	/**
	 * 获取当前日期是周几
	 * weekday=1，当天是周日；weekday=2，当天是周一；...;weekday=7，当天是周六
	 * @param date
	 * @return
	 */
	public static int getWeekDayByDay(Date date) {
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		int weekday=c.get(Calendar.DAY_OF_WEEK);
		if (weekday == 1) {
			return 7;
		}else {
			return weekday - 1;
		}
	}

	/**
	 * 获取指定时间段内，weekDay的日期天数
	 * @param startDate
	 * @param endDate
	 * @param weekDay
	 * @return
	 */
	public static List<String> getBetweenTimeWeekday(Date startDate, Date endDate, int weekDay) {
		List<String> list = new ArrayList<>();
		if (endDate.getTime() < startDate.getTime()) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		if (dateFormat1.format(startDate).equals(dateFormat1.format(endDate))) {
			if (getWeekDayByDay(startDate) == weekDay) {
				list.add(dateFormat1.format(startDate));
				return list;
			}
			else {
				return null;
			}
		}
		Date date = startDate;
		while (!date.equals(endDate)) {
			if (getWeekDayByDay(date) == weekDay) {
				list.add(dateFormat1.format(date));
			}
			c.setTime(date);
			c.add(Calendar.DATE, 1); // 日期加1天
			date = c.getTime();
		}
		return list;
	}

	/**
	 * 是否时间戳格式
	 * @param inputString
	 * @return
	 */
	public static boolean isTimeStampValid(String inputString)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
		try{
			format.parse(inputString);
			return true;
		}
		catch(ParseException e)
		{
			return false;
		}
	}

//	public static void main(String[] args) throws ParseException {
//		DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
//		Date beginDate = dateFormat1.parse("2019-03-01");
//		Date endDate = dateFormat1.parse("2019-03-01");
//		System.out.println(getBetweenTimeWeekday(beginDate, endDate, 5));
//	}

}
