package com.sp.framework.common.utils;
/**
 * @Title: TimeUtil.java
 * @Package com.baozun.common.utils
 * @Description: time 工具类
 * @author alexlu
 * @date 2017年4月8日
 * @version V1.0
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: TimeUtil
 * @Description: time 工具类
 * @author alexlu
 * @date 2017年4月8日
 *
 */

public class TimeUtil {

    private static SimpleDateFormat simpleDateFormat;
    public static final String YMD_HMS = "yyyyMMddHHmmss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static Date getMinTime() {
        return getFormatDate("0001-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
    }

    public static Date addMinute(Date date, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        date = calendar.getTime();
        return date;
    }


    public static Date addHour(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        date = calendar.getTime();
        return date;
    }


    public static Date addDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, day);
        date = calendar.getTime();
        return date;
    }

    public static Date addMilliseconds(Date date, int milliSecordss) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, milliSecordss);
        date = calendar.getTime();
        return date;
    }

    /**
     *
     * @return
     */
    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     *
     * @return
     */
    public static String getCurrentTime() {
        return getFormatDateStr(getCurrentDate(), YMD_HMS);
    }

    public static String getFormatDateStr(Date date, String pattern) {
        if (date == null || !(date instanceof Date)) {
            return "";
        }
        simpleDateFormat = new SimpleDateFormat(pattern);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    public static Date getFormatDate(String dateStr, String pattern) {
        simpleDateFormat = new SimpleDateFormat(pattern);
        Date date;
        try {
            if (dateStr == null || "".equals(dateStr)) {
                return null;
            }
            date = simpleDateFormat.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
        return date;
    }


    public static Date getFormatDate(Date date, String pattern) {
        simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            if (date == null) {
                return null;
            }
            date = simpleDateFormat.parse(simpleDateFormat.format(date));
        } catch (Exception e) {
            return null;
        }
        return date;
    }

    public static Date formDateByYMD(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        return cal.getTime();
    }


    public static Date toTimeOffSet(Date date, int gapMinutes, boolean isDayLightSaving, boolean isBackward) {
        if (isBackward) {
            gapMinutes *= -1;
        }
        if (isDayLightSaving) {
            if (isBackward) {
                gapMinutes -= 60;
            } else {
                gapMinutes += 60;
            }
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, gapMinutes);
        date = cal.getTime();
        return date;
    }


    public static long getOverLap(List<Date> startTimes, List<Date> finishTimes) {
        long overLapTime = 0;
        if (startTimes == null || finishTimes == null || startTimes.size() != finishTimes.size()) {
            return 0;
        }
        for (int i = 0; i < startTimes.size(); i++) {
            startTimes.set(i, startTimes.get(i).getTime() < finishTimes.get(i).getTime() ? startTimes.get(i) : finishTimes.get(i));
            finishTimes.set(i, finishTimes.get(i).getTime() > startTimes.get(i).getTime() ? finishTimes.get(i) : startTimes.get(i));
        }
        Date startTimeMax = Collections.max(startTimes);
        Date startTimeMin = Collections.min(startTimes);
        Date finishTimeMax = Collections.max(finishTimes);
        Date finishTimeMin = Collections.min(finishTimes);
        overLapTime = finishTimeMax.getTime() - startTimeMin.getTime();
        overLapTime -= startTimeMax.getTime() - startTimeMin.getTime();
        overLapTime -= finishTimeMax.getTime() - finishTimeMin.getTime();
        overLapTime = overLapTime < 0 ? 0 : overLapTime;
        return overLapTime;

    }


    public static int compareTime(Date date1, Date date2) {
        int result = 0;
        if (date1 == null || date2 == null) {
            return result;
        } else {
            long time1 = date1.getTime();
            long time2 = date2.getTime();
            result = time1 > time2 ? 1 : (time1 < time2 ? -1 : 0);
        }
        return result;
    }


    public static long diffMinutes(Date date1, Date date2) {
        long diffMinutes = 0;
        long times1 = date1.getTime();
        long times2 = date2.getTime();
        diffMinutes = (times1 - times2) / (1000 * 60);
        return diffMinutes;
    }

    public static Date getDateByStr(String dateStr, String timeStr) {
        Date date = null;
        if (StringUtil.isEmpty(dateStr) || StringUtil.isEmpty(timeStr)) {
            return null;
        }
        String datetimeStr = dateStr + " " + timeStr;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            date = simpleDateFormat.parse(datetimeStr);
        } catch (Exception e) {
            return null;
        }
        return date;
    }

    public static int getDayOfWeek(Date date) {
        int weekDay = -1;
        if (date == null) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (weekDay == 0) {
            weekDay = 7;
        }
        return weekDay;
    }

    public static Date nextDayWithWeekday(Integer week) {
        return nextDayWithWeekday(new Date(), week);
    }

    public static Date nextDayWithWeekday(Date date, Integer week) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Integer weekDaySys = cal.get(Calendar.DAY_OF_WEEK);
        if (weekDaySys > (week + 1) || (week == 7 && weekDaySys != 1)) {
            cal.add(Calendar.WEEK_OF_YEAR, 1);
        }
        cal.set(Calendar.DAY_OF_WEEK, week + 1);

        return cal.getTime();
    }


//    public static void main(String[] args) {
//	Date startTime = TimeUtil.getFormatDate("2017-05-22", "yyyy-MM-dd");
////	    Date now =  new Date();
////	    now = TimeUtil.getFormatDate(now, "yyyy-MM-dd");
//		System.out.println(TimeUtil.getFormatDateStr(TimeUtil.nextDayWithWeekday(startTime, 7), "yyyy-MM-dd"));
//    }


}
