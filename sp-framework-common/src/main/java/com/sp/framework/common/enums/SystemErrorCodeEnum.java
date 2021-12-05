package com.sp.framework.common.enums;

import com.sp.framework.common.base.BaseBizEnum;

/**
 * 系统错误码枚举定义
 * 2开头是操作成功代码
 *  * 4开头是认证失败代码 49100以内是保留数字错误码，用户误自定义
 *  * 5开头是系统相关错误 59100以内是保留数字错误码，用户误自定义
 */
public enum SystemErrorCodeEnum implements BaseBizEnum {
    /**
     * 各个子系统统一的SUCCESS code
     */
    SUCCESS(200000, "操作成功!"),

    /**
     * 各个子系统统一的ERROR code
     */
    ILLEGAL_PARAMETER(40001,"参数错误!"),
    SHIRO_FILTER_SUBJECT_INSTANCE_EMPTY(40002,"登录过期,请重新登录!"),
    XSS_EXCEPTION(40003,"疑是XSS攻击，请确认录入信息"),
    DUPLICATE_SUBMIT_EXCEPTION(40004,"请耐心等待响应，忽重复提交"),
    FUNCTIONAL_MAINTENANCE(40005,"功能维护中"),
    GET_INSTANCE_ERROR(40006, "获取实例失败!"),

    SYSTEM_ERROR(50000, "系统错误!"),
    INTERFACE_ERROR(50001,"接口调用错误!"),
    DATABASE_ERROR(50002,"数据库连接错误!"),
    IO_ERROR(50003,"IO错误!"),
    DATA_ENROLL_DB_ERROR(50004,"数据入库失败!"),
    API_ERROR(50005,"API调用失败!"),
    API_NOT_FOUND(50006,"API资源无法找到!"),
    API_PARAM_BIND_EXCEPTION(50007,"API参数绑定校验错误，请检查!"),
    API_REQUEST_PARAM_NOT_VALID_EXCEPTION(50008,"API请求参数非法，请检查!"),
    API_REQUEST_HEADER_PARAM_MISS_EXCEPTION(50009,"API请求头参数缺失，请检查!"),
    DB_SQL_EXECUTE_CONSTRAINT_EXCEPTION(50010, "SQL执行发生约束异常，请检查!"),

    SYSTEM_UNKNOWN_EXCEPTION(500000, "系统繁忙!"),
    BUSINESS_ERROR(60000, "业务发生错误!"),
    ;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String msg;


    /**
     * 使用错误码和错误信息构造枚举
     *
     * @param code    错误码
     * @param msg 错误信息
     */
    SystemErrorCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg != null ? msg : "";
    }

    /**
     * 获取错误码
     *
     * @return int
     */
    @Override
    public Integer getCode() {
        return code;
    }

    /**
     * 获取错误信息
     *
     * @return String
     */
    @Override
    public String getMsg() {
        return msg;
    }
}
