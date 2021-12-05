package com.sp.framework.common.exception;

import com.sp.framework.common.enums.SystemErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;

/**
* @Description: 已知系统异常定义
* @author Alex Lu
* @date 2017年4月5日
*/
@Slf4j
public class KnownSysException extends SystemException {
	private static final long serialVersionUID = -2565271004860670219L;

	/**
	 * 错误码
	 */
	private Integer code;

	/**
	 * 返回错误消息
	 */
	private String msg;

	public KnownSysException(String msg) {
		super(msg);
		this.msg = msg;
		this.code = SystemErrorCodeEnum.SYSTEM_UNKNOWN_EXCEPTION.getCode();
	}

	public KnownSysException(Integer code, String msg, Throwable cause) {
		super(cause);
		this.msg = msg;
		this.code = code;
	}

	public KnownSysException(Integer code, String msg) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public KnownSysException() {
		super();
	}

	public KnownSysException(String msg, Throwable cause,
                             Boolean enableSuppression, Boolean writableStackTrace) {
		super(msg, cause, enableSuppression, writableStackTrace);
	}

	public KnownSysException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public KnownSysException(Throwable cause) {
		super(cause);
	}

	@Override
	public Integer getCode() {
		return code;
	}

	@Override
	public void setCode(Integer code) {
		this.code = code;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
