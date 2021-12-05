package com.sp.framework.common.utils.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


public class RestServer {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private static final Logger LOG = LoggerFactory.getLogger(RestServer.class);

	public RestServer(HttpServletRequest req, HttpServletResponse resp) {
		this.request = req;
		this.response = resp;
	}
	/**
	 * getRestData(获取请求base64加密的body数据) 
	 * @return
	 * @throws RestException 
	 */
	public String getRestData() throws RestException {
		if (null == this.request) {
			return null;
		}

		String method = this.request.getMethod();
		String ret = null;
		if (("GET".equalsIgnoreCase(method))
				|| ("DELETE".equalsIgnoreCase(method))) {
			ret = this.request.getQueryString();
		} else {
            ret = getBodyData();
        }

		if (null == ret) {
			return null;
		}
		LOG.info("接收到的信息为{}",ret);
		return RestCodec.decodeData(ret);
	}
	
	public String getRestDataNoBase64() throws RestException {
		if (null == this.request) {
			return null;
		}

		String method = this.request.getMethod();
		String ret = null;
		if (("GET".equalsIgnoreCase(method))
				|| ("DELETE".equalsIgnoreCase(method))) {
			ret = this.request.getQueryString();
		} else {
            ret = getBodyData();
        }

		if (null == ret) {
			return null;
		}
		LOG.info("接收到的信息为{}",ret);
		return ret;
	}
	
	public boolean sendRestData(String data) throws RestException {
		if (null == this.response) {
			return false;
		}

		this.response.setContentType("application/json");
		this.response.setCharacterEncoding("utf-8");
		PrintWriter writer = null;
		try {
			writer = this.response.getWriter();
			writer.print(RestCodec.encodeData(data));
		} catch (IOException e) {
			throw new RestException(e.getMessage(), e.getCause());
		} finally {
		}
		return true;
	}

	private String getBodyData() throws RestException {
		StringBuffer data = new StringBuffer();
		String line = null;
		BufferedReader reader = null;
		try {
			reader = this.request.getReader();
			while (null != (line = reader.readLine())) {
                data.append(line);
            }
		} catch (IOException e) {
			throw new RestException(e.getMessage(), e.getCause());
		} finally {
		}
		return data.toString();
	}
}

