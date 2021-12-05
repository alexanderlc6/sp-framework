package com.sp.framework.common.exception;


import com.sp.framework.common.enums.SystemErrorCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @ClassName: BusinessException
* @Description: 业务异常定义
* @author Alex Lu
* @date 2017年4月5日
*/
public class BusinessException extends RuntimeException {
	private static final Logger logger = LoggerFactory.getLogger(BusinessException.class);

	private static final long serialVersionUID = -5771568110674413033L;

	/**
	 * 错误码
	 */
	private Integer code;

	/**
	 * 返回的错误消息
	 */
	private String msg;

	public BusinessException(String retMsg) {
		super(retMsg);
		this.msg = retMsg;
		this.code = SystemErrorCodeEnum.BUSINESS_ERROR.getCode();
		logger.error(retMsg);
	}

	public BusinessException(Integer errorCode, String retMsg) {
		super(retMsg);
		this.msg = retMsg;
		this.code = errorCode;
		logger.error(retMsg);
	}

	public BusinessException(Integer errorCode, String retMsg, String message) {
		super(message);
		this.msg = retMsg;
		this.code = errorCode;
		logger.error(retMsg);
	}
	
	public BusinessException(Integer errorCode, String retMsg, Throwable cause) {
		super(cause);
		this.msg = retMsg;
		this.code = errorCode;
		logger.error(retMsg);
	}

	public BusinessException() {
		super();
	}

	public BusinessException(String message, Throwable cause,
							 Boolean enableSuppression, Boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.msg = message;
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
		this.msg = message;
	}

	public BusinessException(Throwable cause) {
		super(cause);
		this.msg = cause.getMessage();
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

	public void setMsg(String msg) {
		this.msg = msg;
	}
}

