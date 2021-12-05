package com.sp.framework.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 常用常量定义
 * Created by alexlu on 9/20/2017.
 */
public class CommonConstants {
    /**
     * 公共常用常量
     */
    public final static String DEFAULT_VALUE = "default";

    /**
     * 公共请求头
     */
    public final static String COMMON_HTTP_HEAD_USER_TOKEN = "X-YH-UserToken";
    public final static String COMMON_HTTP_HEAD_TENANT_CODE = "X-YH-TenantCode";
    public final static String COMMON_HTTP_HEAD_USER_CODE = "X-YH-UserCode";
    public final static String COMMON_HTTP_HEAD_USER_ID = "X-YH-UserId";
    public final static String COMMON_HTTP_HEAD_USER_INFO = "X-YH-UserInfo";

    /**
     * 删除或更新标识 全局使用
     */
    public final static Integer DELETE_FLAG = 1;
    public final static Integer NOT_DELETE_FLAG = 0;

    public static final String GENDER_M = "1";
    public static final String GENDER_F = "2";

    public static final String USER_EXPAND_FIELD_QQ = "QQ_";
    public static final String USER_EXPAND_FIELD_WEIBO = "weiBo_";
    public static final String USER_EXPAND_FIELD_WEIXIN = "weiXin_";

    /**
     * 数据缓存时间范围常量
     */
    //Cache data expiration set
    public final static int CACHE_EXPIRATION = 50 * 60 * 60;
    public final static int CACHE_EXPIRATION_WEEKLY = (7 * 24 + 2) * 60 * 60;
    public final static int CACHE_EXPIRATION_WEEKLY_NEW = (14 * 24) * 60 * 60;
    //一天
    public final static int CACHE_EXPIRATION_ONE_DAY = 24 * 60 * 60;
    //60秒
    public final static int CACHE_EXPIRATION_ONE_MINUTE = 60;
    //5分钟  300秒
    public final static int CACHE_EXPIRATION_FIVE_MINUTES = 300;
    //15分钟
    public final static int CACHE_EXPIRATION_FIFTEEN_MINUTE = 15 * 60;

    /**
     * 分页相关(索引/每页显示条数)
     */
    public final static int DEFAULT_LIST_PAGE_INDEX = 1;
    public final static int DEFAULT_LIST_PAGE_SIZE = 20;
    public final static int DEFAULT_CAHCE_PAGE_DATA_OFFSET = 500;
    public final static int SPEED_CAHCE_PAGE_DATA_OFFSET = 1000;
    public final static int DEFAULT_BACK_SYS_LIST_PAGE_INDEX = 0;
    public final static int DEFAULT_BACK_SYS_LIST_PAGE_SIZE = 20;

    /**
     * 排序规则相关
     */
    public final static String QUERY_SORT_FIELD_NONE = "none";
    public final static String QUERY_SORT_DIRECTION_ASC = "asc";
    public final static String QUERY_SORT_DIRECTION_DESC = "desc";

    /**
     * 默认数据库记录插入值
     */
    public final static int DEFAULT_CREATOR_ID = 275;
    public final static boolean DEFAULT_VALID = false;
    public final static boolean DEFAULT_INVALID = true;

    /**
     * Default time/date parseStr
     */
    public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public final static String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    public final static String DEFAULT_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String CONSULTANT_FREE_TIME_DATE_FORMAT = "yyyyMMdd";

    /**
     * SSO相关
     */
    //token 生存时间 单位毫秒
    public final static int TOKEN_EXPIRATION = 24 * 60 * 60 * 1000;
    public final static int PC_TOKEN_EXPIRATION = 24 * 60 * 60 * 1000;
    public final static int MOBLIE_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000;
    public final static String USER_NAME_LOGIN = "1";
    public final static String MOBILE_NO_LOGIN = "2";
    public final static String E_MAIL_LOGIN = "3";
    /**
     * Base64Security签名key
     */
    public final static String TOKEN_SECURITY_KEY = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";
    public final static String TOKEN_AUDIENCE = "www.yonghui.cn";
    public final static String TOKEN_ISSUER = "www.yonghui.cn";
    public final static String COOKIE_USER_TOKEN = "userToken";
    public final static String DEL_USER_TOKEN_KEY = "delUserTokenKey_";
    //url签名相关
    public final static String URL_SIGN_KEY="ABC121CBA";

    /**
     * 平台类型
     */
    public final static String PLATORM_PC_INDUSTRY_PORTAL="1";
    public final static String PLATORM_PC_INDUSTRY_BACKSYSTEM="2";
    public final static String PLATORM_MOBLIE_INDUSTRY="3";
    /**
     * 用户删除状态常量定义
     * 0-正常  1-删除
     */
    public final static int NORMAL_USER = 0;
    public final static int INVALID_USER = 1;

    /***
     * 图片验证码 像素相关
     */
    //普通模式
    public final static String NORMAL_IMG_CAPTCHA = "normal";
    //普通模式 验证码高度
    public final static Integer NORMAL_IMG_CAPTCHA_HEIGHT = 50;
    //普通模式 验证码宽度
    public final static Integer NORMAL_IMG_CAPTCHA_WIDTH = 180;
    //普通模式 验证码 长度
    public final static Integer NORMAL_IMG_CAPTCHA_LENGTH = 4;
    //普通模式 验证码 样式
    public final static Integer NORMAL_IMG_CAPTCHA_CLASS = 0;
    //普通模式 验证码 类型 0: 数字 1: 字母 2:数字和字母混合
    public final static Integer NORMAL_IMG_CAPTCHA_TYPE = 0;
    //普通模式 验证码 字体大小
    public final static Integer NORMAL_IMG_CAPTCHA_FONTSIZE = 16;

    /**
     * 密钥或Token相关
     */
    //用户密码MD5加密长度
    public final static Integer USER_PWD_GENERATED_LENGTH = 32;
    //应用信息AppKey长度
    public final static Integer APP_INFO_GENERATED_KEY_LENGTH = 10;
    //应用信息Secret长度
    public final static Integer APP_INFO_GENERATED_SECRET_LENGTH = 32;
    //应用信息Secret对应的随机数盐值的长度
    public final static Integer APP_INFO_SECRET_RANDOM_SALT_LENGTH = 6;
    //应用信息Token长度
    public final static Integer APP_INFO_GENERATED_TOKEN_LENGTH = 32;
    //应用信息Token盐值
    public final static String APP_INFO_GENERATED_TOKEN_SALT_STR = "YH" + System.currentTimeMillis();
    //缓存当前用户应用的权限信息的盐值
    public final static String STORE_CURRENT_USER_APP_AUTH_SALT_STR = "YH-Auth";

    /**
     * 基础符号定义
     */
    public final static String SEPARATOR = ",";
    public final static String TAG_COMMA = ",";
    public final static String TAG_DELIMITER = "-";
    public final static String TAG_DOT = ".";
    public final static String TAG_EQUALS = "=";
    public final static String TAG_POUND = "#";
    public final static String TAG_UNDERLINE = "_";
    public final static String TAG_COLON = ":";
    public final static String TAG_SEPARATOR = "-";
    public final static String TAG_POINT = ".";
    public final static String SEMICOLON = ";";
    public final static String TAG_SINGLE_QUOTE = "'";
    public final static String TAG_DOUBLE_QUOTE = "\"";
    public final static String TAG_LEFT_BRACE = "{";
    public final static String TAG_RIGHT_BRACE = "}";
    public static final String TAG_EXCLAMATION = "!";
    public static final String TAG_SLASH = "/";
    public static final String TAG_QUESTION = "/";

    public final static String TAG_EXPRESSION_AND = " && ";
    public final static String TAG_EXPRESSION_OR = " || ";
    public final static String TAG_EXPRESSION_NE = " != ";

    /**
     * 条件操作符
     */
    public final static String CRITERIA_OPERATOR_AND = "and";
    public final static String CRITERIA_OPERATOR_OR = "or";
    public final static String CRITERIA_OPERATOR_EQUALS = "=";
    public final static String CRITERIA_OPERATOR_NOT_EQUALS = "!=";
    public final static String CRITERIA_OPERATOR_GT = ">";
    public final static String CRITERIA_OPERATOR_GT_EQUALS = ">=";
    public final static String CRITERIA_OPERATOR_LT = "<";
    public final static String CRITERIA_OPERATOR_LT_EQUALS = "<=";
    public final static String CRITERIA_OPERATOR_BOTH_LIKE = ":";
    public final static String CRITERIA_OPERATOR_LEFT_LIKE = "l:";
    public final static String CRITERIA_OPERATOR_RIGHT_LIKE = ":l";
    public final static String CRITERIA_OPERATOR_BOTH_NULL = "null";
    public final static String CRITERIA_OPERATOR_NOT_NULL = "!null";
    public final static String CRITERIA_OPERATOR_IN = "in";
    public final static Integer DEFAULT_QUERY_IS_PAGER = 1;

    /**
     * 访问来源客户端类型
     */
    public final static String ACCESS_CLIENT_TYPE_PC = "PC";
    public final static String ACCESS_CLIENT_TYPE_APP = "APP";

    /**
     * 移动端相关常量
     */
    public final static String IOS_MOBILE = "ios";
    public final static String ANDRIOD_MOBILE = "android";
    public final static  String RSA_PUBLIC_KEY_ = "RSAPublicKey_";
    public final static  String RSA_PRIVATE_KEY_ = "RSAPrivateKey_";

    public final static Integer APP_FLAG_STATUS_NON_EXAMINE = 3;
    public final static Integer APP_FLAG_STATUS_EXAMINE_REFUSE = 1;
    public final static Integer APP_FLAG_STATUS_EXAMINE_PASSED = 2;

    public final static Map<Integer, String> APP_FLAG_STATUS_MAP = new HashMap<>();
    static {
        APP_FLAG_STATUS_MAP.put(APP_FLAG_STATUS_NON_EXAMINE, "审核中");
        APP_FLAG_STATUS_MAP.put(APP_FLAG_STATUS_EXAMINE_REFUSE, "审核失败");
        APP_FLAG_STATUS_MAP.put(APP_FLAG_STATUS_EXAMINE_PASSED, "审核通过");
    }

    //数字常量
    public final static int NUMBER_1 = 1;
    public final static int NUMBER_2 = 2;

    //请求标识和协议
    public final static String HTTP_METHOD_POST = "POST";
    public final static String HTTP_METHOD_GET = "GET";
    public static final String REQUEST_PROTOCOL_LB = "lb://";
    public static final String REQUEST_PROTOCOL_HTTP = "http://";
    public static final String REQUEST_PROTOCOL_HTTPS = "https://";
    public static final String REQUEST_PROTOCOL_DUBBO = "dubbo://";

    //发送消息返回结果文本
    public final static String TIPS_MSG_SEND_PROCESS_COMPLETE_SUCCESS = "消息发送处理操作成功!";
    public final static String TIPS_MSG_SEND_PROCESS_COMPLETE_FAILED = "消息发送处理操作失败!";

    /**
     * 发送类型:1-即时消息,2-延时消息
     */
    public static final Integer SEND_TYPE_INSTANT_MSG = 1;
    public static final Integer SEND_TYPE_DELAY_MSG = 2;

    /**
     * 消息渠道
     */
    public static final String MSG_CHANNEL_WECHAT = "WECHAT";
    public static final String MSG_CHANNEL_SMS = "SMS";
    public static final String MSG_CHANNEL_APP = "APP";
    public static final String MSG_CHANNEL_MAIL = "MAIL";

    /**
     * 用户公共字段定义
     */
    public static final String USER_LOGIN_NAME = "userLoginName";
    public static final String USER_CODE = "userCode";
    public static final String ORG_CODE = "orgCode";
    public static final String CREATED_BY = "createdBy";
    public static final String UPDATED_BY = "updatedBy";
}
