package com.sp.framework.common.utils;

/**
  * @Title: HttpApiUtil.java
  * @Package com.sp.framework.common.utils
  * @Description: HttpApiUtil
  * @author alexlu
  * @date 2017年4月25日
  * @version V1.0  
  */

import com.sp.framework.common.utils.rest.RestClient;
import com.sp.framework.common.vo.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: HttpApiUtil
 * @Description: HttpApiUtil
 * @author alexlu
 * @date 2017年4月25日
 *
 */

public class HttpApiUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpApiUtil.class);
    
    /**
     * 
        * @Title: getHostIp
        * @Description: 获取客户端ip
        * @param @param req
        * @param @return    参数
        * @return String    返回类型
        * @throws
     */
    public static String getHostIp(HttpServletRequest request){
	String ip = request.getHeader("X-Forwarded-For");
	if(!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
	    //多次反向代理后会有多个ip值，第一个ip才是真实ip
	    int index = ip.indexOf(",");
	    if(index != -1){
	        return ip.substring(0,index);
	    }else{
	        return ip;
	    }
        }
        ip = request.getHeader("X-Real-IP");
        if(!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
               return ip;
        }
        return request.getRemoteAddr();
    }
    
    


    /**
     * 
     * @Title: execute @Description: 调用api接口 @param @param url @param @param
     * method @param @param data @param @param times @param @return 参数 @return
     * ResponseVO 返回类型 @throws
     */
    public static ResponseVO execute(String url, String method, String data, int times) {
	if (times <= 0) {
	    times = 1;
	}

	ResponseVO responseVo = null;
	RestClient restClient = new RestClient(url, method, data);
	String result = "";
	for (int i = 0; i < times; i++) {
	    try {
		result = restClient.execute();
		if (!"".equals(result)) {
		    responseVo = JsonFormatter.toObject(result, ResponseVO.class);
		    break;
		} else {
		    responseVo.setSuccess(false);
		}
	    } catch (Exception e) {
		continue;
	    }
	}
	return responseVo;
    }

    /**
     * 
     * @Title: executeNoEncode @Description: 调用API不要base64编码 @param @param
     * url @param @param method @param @param data @param @param
     * times @param @return 参数 @return ResponseVO 返回类型 @throws
     */
    public static ResponseVO executeNoEncode(String url, String method, String data, int times) {
	if (times <= 0) {
	    times = 1;
	}
	ResponseVO responseVo = null;
	RestClient restClient = new RestClient(url, method, data);
	String result = "";
	for (int i = 0; i < times; i++) {
	    try {
		result = restClient.executeWithNoEncode();
		if (!"".equals(result)) {
		    responseVo = JsonFormatter.toObject(result, ResponseVO.class);
		    break;
		} else {
		    responseVo.setSuccess(false);
		}
	    } catch (Exception e) {
		continue;
	    }
	}
	return responseVo;
    }

    /**
     * 
     * @Title: execute @Description: 调用api接口without times @param @param
     * url @param @param method @param @param data @param @return 参数 @return
     * ResponseVO 返回类型 @throws
     */
    public static ResponseVO execute(String url, String method, String data) {
	ResponseVO responseVo = null;
	RestClient restClient = new RestClient(url, method, data);
	String result = "";
	// 默认3次
	for (int i = 0; i < 3; i++) {
	    try {
		result = restClient.execute();
		if (!"".equals(result)) {
		    responseVo = JsonFormatter.toObject(result, ResponseVO.class);
		    break;
		} else {
		    responseVo.setSuccess(false);
		}
	    } catch (Exception e) {
		continue;
	    }
	}
	return responseVo;
    }

    /**
     * 
     * @Title: executeNoEncode @Description:调用api没有加密 @param @param
     * url @param @param method @param @param data @param @return 参数 @return
     * ResponseVO 返回类型 @throws
     */
    public static ResponseVO executeNoEncode(String url, String method, String data) {
	ResponseVO responseVo = null;
	RestClient restClient = new RestClient(url, method, data);
	String result = "";
	// 默认3次
	for (int i = 0; i < 3; i++) {
	    try {
		result = restClient.executeWithNoEncode();
		if (!"".equals(result)) {
		    responseVo = JsonFormatter.toObject(result, ResponseVO.class);
		    break;
		} else {
		    responseVo.setSuccess(false);
		}
	    } catch (Exception e) {
		continue;
	    }
	}
	return responseVo;
    }

    /**
     * 
     * @Title: executeNoEncode @Description: http without method @param @param
     * url @param @param data @param @return 参数 @return ResponseVO 返回类型 @throws
     */
    public static ResponseVO executeNoEncode(String url, String data) {
	ResponseVO responseVo = null;
	RestClient restClient = new RestClient(url, "POST", data);
	String result = "";
	// 默认3次
	for (int i = 0; i < 3; i++) {
	    try {
		result = restClient.executeWithNoEncode();
		if (!"".equals(result)) {
		    responseVo = JsonFormatter.toObject(result, ResponseVO.class);
		    break;
		} else {
		    responseVo.setSuccess(false);
		}
	    } catch (Exception e) {
		continue;
	    }
	}
	return responseVo;
    }

    /**
     * 
     * @Title: executeNoEncodeFroList @Description:获取 T 调用接口返回时list的时候（特殊性 慎用
     * ） @param @param url @param @param data @param @param clazz @param @return
     * 参数 @return T 返回类型 @throws
     */
    public static <T> T executeNoEncodeFroList(String url, String data, Class<T> clazz) {
	ResponseVO responseVo = null;
	T t = null;
	RestClient restClient = new RestClient(url, "POST", data);
	String result = "";
	// 默认3次
	for (int i = 0; i < 3; i++) {
	    try {
		result = restClient.executeWithNoEncode();
		if (!"".equals(result)) {
		    responseVo = JsonFormatter.toObject(result, ResponseVO.class);
		    break;
		} else {
		    responseVo.setSuccess(false);
		}
	    } catch (Exception e) {
		continue;
	    }
	}
	if (responseVo != null && responseVo.getSuccess() && responseVo.getData() != null) {
	    try {
		List<Object> list = JsonFormatter.toObject(JsonFormatter.toJsonString(responseVo.getData()),
			List.class);
		if (!ListUtil.isEmpty(list)) {
		    Object o = list.get(0);
		    t = JsonFormatter.toObject(JsonFormatter.toJsonString(o), clazz);
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return t;
    }
	public static <T> T executeNoEncodeFroClass(String url, String data, Class<T> clazz) {
		ResponseVO responseVo = null;
		T t = null;
		RestClient restClient = new RestClient(url, "POST", data);
		String result = "";
		// 默认3次
		for (int i = 0; i < 3; i++) {
			try {
				result = restClient.executeWithNoEncode();
				if (!"".equals(result)) {
					responseVo = JsonFormatter.toObject(result, ResponseVO.class);
					break;
				} else {
					responseVo.setSuccess(false);
				}
			} catch (Exception e) {
				continue;
			}
		}
		if (responseVo != null && responseVo.getSuccess() && responseVo.getData() != null) {
			try {


					t = JsonFormatter.toObject(JsonFormatter.toJsonString(responseVo.getData()), clazz);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return t;
	}
    /**
     * 
     * @Title: executeNoEncodeList @Description: http return list @param @param
     * url @param @param data @param @param clazz @param @return 参数 @return
     * List<T> 返回类型 @throws
     */
    public static <T> List<T> executeNoEncodeList(String url, String data, Class<T> clazz) {
	ResponseVO responseVo = new ResponseVO();
	RestClient restClient = new RestClient(url, "POST", data);
	List<T> list = null;
	String result = "";
	// 默认3次
	for (int i = 0; i < 3; i++) {
	    try {
		result = restClient.executeWithNoEncode();
		if (!"".equals(result)) {
		    responseVo = JsonFormatter.toObject(result, ResponseVO.class);
		    break;
		} else {
		    responseVo.setSuccess(false);
		}
	    } catch (Exception e) {
		continue;
	    }
	}
	if (responseVo != null && responseVo.getSuccess() && responseVo.getData() != null) {
	    try {
		List<Object> lst = JsonFormatter.toObject(JsonFormatter.toJsonString(responseVo.getData()), List.class);
		if (!ListUtil.isEmpty(lst)) {
		    list = lst.stream().map(j -> JsonUtil.formatObject(j, clazz)).collect(Collectors.toList());
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return list;
    }
    
    /**
     * @param username 用户名
     * @param password 密码
     * @param endPointUrl 请求地址
     * @param xmlString 请求报文
     * @return
     * @throws Exception 异常信息
     */
    public static String xmlRequestWithAuth(final String username, final String password, String endPointUrl, String xmlString) throws Exception {
        Authenticator.setDefault(new Authenticator() {
            // This method is called when a password-protected URL is accessed
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });
        logger.info("Request Data：" + xmlString);
        HttpURLConnection con = (HttpURLConnection) new URL(endPointUrl).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("content-type", "application/soap+xml; charset=utf-8");
        con.setDoOutput(true);
        con.getOutputStream().write(xmlString.getBytes());
        con.getOutputStream().flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        String lines;
        StringBuffer sb = new StringBuffer();
        while ((lines = reader.readLine()) != null) {
            sb.append(lines);
        }
        logger.info("Response Data："+sb.toString());
        return sb.toString();
    }
    
    /**
     * @param endPointUrl 请求URl
     * @param xmlString xml字符串
     * @return
     * @throws Exception 异常
     */
    public static String xmlRequestWithNotAuth(String endPointUrl, String xmlString) throws Exception {
        logger.info("Request Data：" + xmlString);
        HttpURLConnection con = (HttpURLConnection) new URL(endPointUrl).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("content-type", "text/xml; charset=utf-8");
        con.setRequestProperty("SOAPAction","http://tempuri.org/IAPIService/SendNotification");//Soap
        con.setConnectTimeout(20000);  
        con.setReadTimeout(30000); 
        con.setDoOutput(true);
        con.getOutputStream().write(xmlString.getBytes());
        con.getOutputStream().flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        String lines;
        StringBuffer sb = new StringBuffer();
        while ((lines = reader.readLine()) != null) {
            sb.append(lines);
        }
        logger.info("Response Data："+sb.toString());
        return sb.toString();
    }
}
