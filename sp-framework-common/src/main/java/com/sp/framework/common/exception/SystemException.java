package com.sp.framework.common.exception;

import com.sp.framework.common.enums.SystemErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;

/**
* @ClassName: SystemException
* @Description: 系统异常定义
* @author Alex Lu
* @date 2017年4月5日
*/
@Slf4j
public class SystemException extends RuntimeException {
	private static final long serialVersionUID = -2565271004860670219L;

	/**
	 * 错误码
	 */
	private Integer code;

	/**
	 * 返回错误消息
	 */
	private String msg;

	public SystemException(String msg) {
		super(msg);
		this.msg = SystemErrorCodeEnum.SYSTEM_UNKNOWN_EXCEPTION.getMsg();
		this.code = SystemErrorCodeEnum.SYSTEM_UNKNOWN_EXCEPTION.getCode();
	}

	public SystemException(Integer code, String msg, Throwable cause) {
		super(cause);
		this.msg = msg;
		this.code = code;
	}
	
	public SystemException(Integer code, String msg) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public SystemException() {
		super();
	}

	public SystemException(String msg, Throwable cause,
						   Boolean enableSuppression, Boolean writableStackTrace) {
		super(msg, cause, enableSuppression, writableStackTrace);
	}

	public SystemException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public SystemException(Throwable cause) {
		super(cause);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String retMsg) {
		this.msg = retMsg;
	}
}
