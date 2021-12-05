package com.sp.framework.utilities.sftp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;

/**
 * 时间类型工具类
 * 
 * @Title:
 * @Company:宝尊电子商务有限公司
 * @Description:
 * @Author:unknow
 * @Since:2015年4月10日
 * @Copyright:Copyright (c) 2015
 * @ModifyDate:
 * @Version:1.1.0
 */
public final class DateUtil {

    /**
     * 年-月-日 时《两位年》
     */
    public static final String PATTERN_MONITOR_TIME = "yy-MM-dd HH";
    /**
     * 年-月-日 时《四位年》
     */
    public static final String PATTERN_FULL_HOUR = "yyyy-MM-dd HH";
    /**
     * 24小时
     */
    public static final String HOUR = "HH";
    /**
     * 年-月-日
     */
    public static final String PATTERN_SIMPLE = "yyyy-MM-dd";
    /**
     * 年-月-日 时:分
     */
    public static final String PATTERN_MINUTES = "yyyy-MM-dd HH:mm";
    /**
     * 年-月-日 时:分:秒
     */
    public static final String PATTERN_NORMAL = "yyyy-MM-dd HH:mm:ss";
    /**
     * 年-月-日 时:分:秒 毫秒
     */
    public static final String PATTERN_FULL = "yyyy-MM-dd HH:mm:ss S";
    /**
     * 年月日时分秒毫秒
     */
    public static final String PATTERN_FULL_SIMPLE = "yyyyMMddHHmmss";
    /**
     * 年月日
     */
    public static final String PATTERN_FULL_DAY = "yyyyMMdd";
    /**
     * 表示没用过种格式
     */
    public static final String ORACLE_FORMAT = "YYYY-MM-DD HH24:MI:SS";

    /**
     * 年月日时分秒四位毫秒
     */
    public static final String SSSS = "yyyyMMddHHmmssSSSS";

    /**
     * 根据pattern格式，解析src日期字符串
     * 
     * @param src
     * @param pattern
     * @return
     * @throws ParseException
     * @Description:
     */
    public static Date parse(String src, String pattern) throws ParseException {
        SimpleDateFormat util = new SimpleDateFormat();
        util.applyPattern(pattern);
        return util.parse(src);
    }

    /**
     * 自动解析日期字符串 年-月-日 年-月-日 时:分:秒 年-月-日 时:分:秒 毫秒 其它格式抛出异常（）
     * 
     * @param src
     * @return
     * @Description:
     */
    public static Date parse(String src) {
        SimpleDateFormat util = new SimpleDateFormat(PATTERN_SIMPLE);
        Date ret = null;
        if (!StringUtils.isEmpty(src)) {
            util.applyPattern(PATTERN_NORMAL);
            try {
                ret = util.parse(src);
            } catch (ParseException e) {}
            if (ret == null) {
                try {
                    util.applyPattern(PATTERN_SIMPLE);
                    ret = util.parse(src);
                } catch (ParseException e) {}
            }
            if (ret == null) {
                try {
                    util.applyPattern(PATTERN_FULL);
                    ret = util.parse(src);
                } catch (ParseException e) {}
            }
        }
        if (ret == null) {
            throw new IllegalArgumentException("## cant parse to Date . not supported by default pattern: $" + src + "$");
        }
        return ret;
    }

    /**
     * 将时间数字解析成{@link Date}
     * 
     * @param timeMills
     * @return
     * @Description:
     */
    public static Date parse(Long timeMills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMills);
        // taobao 使用估计标准时间，北京时间需+8小时
        calendar.add(Calendar.HOUR_OF_DAY, 8);
        return calendar.getTime();
    }

    /**
     * 得到某一天的开始时间 清空时分秒毫秒 即为当天的00:00:00
     * 
     * @param date
     * @return
     * @Description:
     */
    public static Date getDayBegin(Date date) {
        if (date == null) {
            date = nowDate();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.clear(Calendar.HOUR_OF_DAY);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        return cal.getTime();
    }

    /**
     * 得到某一天的结束，清空时分秒，天数加1，减一秒钟，即为当天的23：59：59
     * 
     * @param date
     * @return
     * @Description:
     */
    public static Date getDayEnd(Date date) {
        Date ret = getDayBegin(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ret);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.add(Calendar.SECOND, -1);
        return calendar.getTime();
    }

    /**
     * 根据pattern 格式化显示date
     * 
     * @param date
     * @param pattern
     * @return
     * @Description:
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat util = new SimpleDateFormat(pattern);
        String str = util.format(date);
        return str;
    }

    /**
     * 得到field的num时间 field为Calendar.YEAR|Calendar.MONTH|Calendar.DAY_OF_MONTH|....... num可为正可为负，为负表示往前
     * 
     * <pre>
	 * example:
	 *    1: DateUtil.addField('2015-04-10 18:00:00',{@link Calendar}.YEAR, 1)
	 *             ----->result:'2016-04-10 18:00:00'
	 *    2: DateUtil.addField('2015-04-10 18:00:00',{@link Calendar}.SECOND, -4)
     *             ----->result:'2015-04-10 17:59:56'
	 *     </pre>
     * 
     * @param date
     * @param field
     * @param num
     * @return
     * @Description:
     */
    public static Date addField(Date date, int field, int num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, num);
        return cal.getTime();
    }

    public static Date addMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static Date addSeconds(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    public static Date addHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hours);
        return calendar.getTime();
    }

    /**
     * 清除分秒和毫秒
     * 
     * @param date
     * @return
     * @Description:
     */
    public static Date clearMinutesAndSeconds(Date date) {
        if (null == date) {
            throw new IllegalArgumentException();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        return calendar.getTime();
    }

    /**
     * 清除秒和毫秒
     * @param date
     * @return
     */
    public static Date clearSeconds(Date date) {
        if (null == date) {
            throw new IllegalArgumentException();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        return calendar.getTime();
    }

    /**
     * 获取当前时间String：yyyyMMddHHmmss
     * @return
     */
    public static String now() {
        return formatDate(Calendar.getInstance().getTime(), PATTERN_FULL_SIMPLE);
    }
    /**
     * 按dateFormat格式获取当前时间String
     * @param dateFormat
     * @return
     */
    public static String now(String dateFormat) {
        return formatDate(Calendar.getInstance().getTime(), dateFormat);
    }
    /**
     * 获取当前时间Date
     * @return
     */
    public static Date nowDate() {
        return Calendar.getInstance().getTime();
    }

    public static String day() {
        return formatDate(Calendar.getInstance().getTime(), PATTERN_FULL_DAY);
    }

    public static String ssss() {
        return formatDate(Calendar.getInstance().getTime(), SSSS);
    }

    public static String compare(Date beginTime, Date endTime) {
        long l = endTime.getTime() - beginTime.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return "" + day + "天" + hour + "小时" + min + "分" + s + "秒";
    }

    /** For creating xml dates. */
    private static final ThreadLocal<DatatypeFactory> threadDTF = new ThreadLocal<DatatypeFactory>();

    public static DatatypeFactory getDTF() {
        DatatypeFactory dtf = threadDTF.get();
        if (dtf == null) {
            try {
                dtf = DatatypeFactory.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            threadDTF.set(dtf);
        }
        return dtf;
    }

    public static XMLGregorianCalendar geneXMLGregorianCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getDTF().newXMLGregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND), 0, cal.get(Calendar.ZONE_OFFSET));
    }

    public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar gc = null;
        try {
            gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return gc;
    }

    public static Date convertToDate(XMLGregorianCalendar cal) throws Exception {
        GregorianCalendar ca = cal.toGregorianCalendar();
        return ca.getTime();
    }
}
