package com.sp.framework.common.exception;


/**
 * 
    * @ClassName: ApiException
    * @Description: 接口异常
    * @author felixxu
    * @date 2017年4月5日
    *
 */
public class ApiException extends RuntimeException {
	/**
	 * serialVersionUID
	 *
	 * @since Ver 1.1
	 */
	private static final long serialVersionUID = -1292294549050077206L;
	private Integer errorCode;
	// 返回错误消息
	private String retMsg;

	public ApiException(Integer errorCode, String retMsg) {
		this.errorCode = errorCode;
		this.retMsg = retMsg;
	}

	public ApiException(Integer errorCode, String retMsg, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
		this.retMsg = retMsg;
	}

	public ApiException() {
		super();
	}

	public ApiException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiException(String message) {
		super(message);
	}

	public ApiException(Throwable cause) {
		super(cause);
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

}
