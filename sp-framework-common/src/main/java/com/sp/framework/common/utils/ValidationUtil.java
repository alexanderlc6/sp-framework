package com.sp.framework.common.utils;

import com.sp.framework.common.constant.CommonConstants;
import com.sp.framework.common.enums.SystemErrorCodeEnum;
import com.sp.framework.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class ValidationUtil {

    /**
     * 正则表达式：验证用户名
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z][a-zA-Z0-9_]{5,25}$";
    //public static final String REGEX_USERNAME = "^[a-z][-a-z0-9]{0,38}[a-z0-9]$";

    /**
     * 正则表达式：验证密码
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,50}$";

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^1[3456789]\\d{9}$";

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z_]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
    /**
     * 正则表达式：验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * 正则表达式：验证IP地址
     */
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

    /**
     * 正则表达式：验证出生日期
     */
    public static final String REGEX_BIRTHDAY = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]((((0?[13578])|(1[02]))[\\-\\/\\s]((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]((((0?[13578])|(1[02]))[\\-\\/\\s]((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]((0?[1-9])|(1[0-9])|(2[0-8]))))))";
    /**
     * 校验用户名
     *
     * @param username
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUsername(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }
    /**
     * 验证出生日期
     *
     * @param birthdayStr
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isBirthday(String birthdayStr) {
        return Pattern.matches(REGEX_BIRTHDAY, birthdayStr);
    }

    /**
     * 校验密码
     *
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 校验汉字
     *
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }

    /**
     * 校验身份证
     *
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

    /**
     * 校验URL
     *
     * @param url
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        return Pattern.matches(REGEX_URL, url);
    }

    /**
     * 校验IP地址
     *
     * @param ipAddr
     * @return
     */
    public static boolean isIPAddr(String ipAddr) {
        return Pattern.matches(REGEX_IP_ADDR, ipAddr);
    }
    /**
     * 验证邮箱格式
     * @param email
     * @param required
     */
    public static void checkEmail(String email,boolean required)
    {
        if(required) {
            if(null== email) {
                throw new BusinessException(SystemErrorCodeEnum.ILLEGAL_PARAMETER.getCode(), "邮箱不能为空!");
            }else{
                if(!isEmail(email)){
                    throw new BusinessException(SystemErrorCodeEnum.ILLEGAL_PARAMETER.getCode(), "邮箱格式错误");
                }
            }
        }else{
            if(StringUtils.isNotEmpty(email)){
                if(!isEmail(email)){
                    throw new BusinessException(SystemErrorCodeEnum.ILLEGAL_PARAMETER.getCode(), "邮箱格式错误");
                }
            }
        }
    }

    /**
     * 验证手机号码格式
     * @param phone
     * @param required
     */
    public static void checkPhone(String phone,boolean required)
    {
        if(required) {
            if(null==phone) {
                throw new BusinessException(SystemErrorCodeEnum.ILLEGAL_PARAMETER.getCode(), "手机不能为空!");
            }else{
                if(!isMobile(phone)){
                    throw new BusinessException(SystemErrorCodeEnum.ILLEGAL_PARAMETER.getCode(), "手机格式错误");
                }
            }
        }else{
            if(StringUtils.isNotEmpty(phone)){
                if(!isMobile(phone)){
                    throw new BusinessException(SystemErrorCodeEnum.ILLEGAL_PARAMETER.getCode(), "手机格式错误");
                }
            }
        }
    }

    /**
     * 验证用户名格式
     * @param userName
     * @param required
     */
    public static void checkUserName(String userName,boolean required)
    {
        if(required) {
            if(null==userName) {
                throw new BusinessException(SystemErrorCodeEnum.ILLEGAL_PARAMETER.getCode(), "用户名不能为空!");
            }else{
                if(!isUsername(userName)){
                    throw new BusinessException(SystemErrorCodeEnum.ILLEGAL_PARAMETER.getCode(), "用户名格式错误!");
                }
            }
        }else{
            if(StringUtils.isNotEmpty(userName)){
                if(!isUsername(userName)){
                    throw new BusinessException(SystemErrorCodeEnum.ILLEGAL_PARAMETER.getCode(), "用户名格式错误!");
                }
            }
        }
    }

    /**
     * 校验密码格式
     * @param password
     * @param required
     */
    public static void checkPassword(String password,boolean required)
    {
        if(required) {
            if(null == password) {
                throw new BusinessException(SystemErrorCodeEnum.ILLEGAL_PARAMETER.getCode(), "密码为空!");
            }else{
                if(!isPassword(password)){
                    throw new BusinessException(SystemErrorCodeEnum.ILLEGAL_PARAMETER.getCode(), "密码格式错误!");
                }
            }
        }else{
            if(StringUtils.isNotEmpty(password)){
                if(!isPassword(password)){
                    throw new BusinessException(SystemErrorCodeEnum.ILLEGAL_PARAMETER.getCode(), "密码名格式错误");
                }
            }
        }
    }

    public static void checkMd5Password(String password) {
        if(password == null || password.length() != CommonConstants.USER_PWD_GENERATED_LENGTH) {
            throw new BusinessException(SystemErrorCodeEnum.ILLEGAL_PARAMETER.getCode(), "密码未进行MD5加密!");
        }
    }

}
