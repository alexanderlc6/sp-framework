package com.sp.framework.common.utils.rest;

import java.io.IOException;

public class RestException extends IOException{

	private static final long serialVersionUID = -4552262380605298678L;

	public RestException()
	  {
	  }

	  public RestException(String message)
	  {
	    super(message);
	  }

	  public RestException(Throwable cause)
	  {
	    initCause(cause);
	  }

	  public RestException(String message, Throwable cause)
	  {
	    super(message);
	    initCause(cause);
	  }
}
