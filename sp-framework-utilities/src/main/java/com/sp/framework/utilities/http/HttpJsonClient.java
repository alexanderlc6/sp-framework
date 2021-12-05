package com.sp.framework.utilities.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sp.framework.utilities.GZIPUtils;
import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sp.framework.utilities.json.JsonUtil;

public class HttpJsonClient {

	private static final Logger logger = LoggerFactory.getLogger(HttpJsonClient.class);

	static CloseableHttpClient httpClient = null;
	private static final int defautTimeoutSecond = 20;

	static RequestConfig defaultRequestConfig = null;
	static {
		defaultRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
		// 设置默认的配置
		httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();

	}

	/**
	 * @param url URL
	 * @param params 参数
	 * @param second 秒
	 * @return
	 */
	public static String getJsonData(String url, Map<String, Object> params, int second) {
		try {
			return getJsonData(url, params, second, null);
		} catch (Exception e) {
			logger.error("get data from [" + url + "] error", e);
			return "";
			// throw new
			// WoyoHttpClientException("get data from ["+url+"] error",e);
		}
	}

	public static String getJsonData(String url, Map<String, Object> params) {
		try {
			return getJsonData(url, params, 0, null);
		} catch (Exception e) {
			logger.error("get data from [" + url + "] error", e);
			return "";
			// throw new
			// WoyoHttpClientException("get data from ["+url+"] error",e);
		}
	}

	/**
	 * 生成本次使用的请求配置
	 * @param second 超时秒数
	 * @return
	 */
	private static RequestConfig makeLocalRequestConfig(int second) {

		RequestConfig requestConfig = null;

		if (second > 0) {
			requestConfig = RequestConfig.copy(defaultRequestConfig)
					.setSocketTimeout(second * 1000)
					.setConnectTimeout(second * 1000)
					.setConnectionRequestTimeout(second * 1000).build();

		} else {
			requestConfig = RequestConfig.copy(defaultRequestConfig)
					.setSocketTimeout(defautTimeoutSecond * 1000)
					.setConnectTimeout(defautTimeoutSecond * 1000)
					.setConnectionRequestTimeout(defautTimeoutSecond * 1000)
					.build();
		}

		return requestConfig;

	}

	public static String getJsonData(String url, Map<String, Object> params, int second, Header[] headers) {

		try {
			logger.debug("begin to get url:" + url);
			if (params != null && !(params.isEmpty())) {
				List<NameValuePair> values = new ArrayList<NameValuePair>();
				for (Map.Entry<String, Object> entity : params.entrySet()) {
					BasicNameValuePair pare = new BasicNameValuePair(entity.getKey(), entity.getValue().toString());
					values.add(pare);

				}
				String str = URLEncodedUtils.format(values, "UTF-8");
				if (url.indexOf("?") > -1) {
					url += "&" + str;
				} else {
					url += "?" + str;
				}
			}
			logger.debug("after url:" + url);

			HttpGet httpget = new HttpGet(url);

			httpget.setConfig(makeLocalRequestConfig(second));

			if (headers != null)
				httpget.setHeaders(headers);

			CloseableHttpResponse r = httpClient.execute(httpget);

			String result = EntityUtils.toString(r.getEntity());
			r.close();
			if (result != null) {
				Matcher m = Pattern.compile("<\\/script>", Pattern.CASE_INSENSITIVE).matcher(result);
				result = m.replaceAll("<\\\\/script>");
			}
			return result;
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException,get data from [" + url + "] error", e);
			return "";
		} catch (IOException e) {
			logger.error("IOException,get data from [" + url + "] error", e);
			return "";
		} finally {
			// httpclient.getConnectionManager().shutdown();
		}
	}
	
	
	/**
	 * 下载
	 * @param url
	 * @param params
	 * @param filePath
	 * @param second
	 * @return
	 */
	public static void getJsonDown(String url, Map<String, Object> params,String filePath,int second) {
		 
		try {
			logger.debug("begin to get url:" + url);
			if (params != null && !(params.isEmpty())) {
				List<NameValuePair> values = new ArrayList<NameValuePair>();
				for (Map.Entry<String, Object> entity : params.entrySet()) {
					BasicNameValuePair pare = new BasicNameValuePair(entity.getKey(), entity.getValue().toString());
					values.add(pare);
					
				}
				String str = URLEncodedUtils.format(values, "UTF-8");
				if (url.indexOf("?") > -1) {
					url += "&" + str;
				} else {
					url += "?" + str;
				}
			}
			logger.debug("after url:"+url);
			//System.out.print(url);
			
			HttpGet httpget = new HttpGet(url);

			httpget.setConfig(makeLocalRequestConfig(second));

			

	
			HttpResponse response = httpClient.execute(httpget);
			
			if(response.getStatusLine().getStatusCode()!=200){
				logger.error("url:["+url+"] error:"+response.getStatusLine().getStatusCode());
				return;
			}
			
			HttpEntity entity = response.getEntity();  
	        InputStream input = null;  
	        try {  
	            input = entity.getContent();  
	            File file = new File(filePath);  
	            FileOutputStream output = FileUtils.openOutputStream(file);  
	            try {  
	                IOUtils.copy(input, output);  
	            } finally {  
	                IOUtils.closeQuietly(output);  
	            }  
	        } finally {  
	            IOUtils.closeQuietly(input);  
	        }  
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException,get data from ["+url+"] error", e);
			
		} catch (IOException e) {
			logger.error("IOException,get data from ["+url+"] error", e);
			
		}finally {
			
		}
	}
	

	public static String deleteIndexData(String url) throws ClientProtocolException, IOException {

		try {
			logger.debug("begin to get url:" + url);
			HttpDelete httpget = new HttpDelete(url);
			CloseableHttpResponse r = httpClient.execute(httpget);

			String result = EntityUtils.toString(r.getEntity());
			r.close();
			return result;
		}  catch (Exception e) {
			logger.error("get data from [" + url + "] error", e);
			return "";
		}finally {

		}
	}

	public static String postJsonDataByJson(String url, Map<String, ?> params) throws ClientProtocolException, IOException {
		return postJsonDataByJson(url, params, 0);
	}

	/**
	 * 参数是一个json字符串
	 * @param url
	 * @param jsonParam josn字符串参数
	 * @param second
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String postJsonDataByJson(String url, String jsonParam, int second) throws ClientProtocolException, IOException {

		try {
			HttpPost httpPost = new HttpPost(url);

			httpPost.setConfig(makeLocalRequestConfig(second));

			if (jsonParam != null) {

				ContentType ct = ContentType.create("text/xml", Charset.forName("UTF-8"));

				ByteArrayEntity mult = new ByteArrayEntity(jsonParam.getBytes("UTF-8"), ct);
				httpPost.setEntity(mult);
			}

			logger.debug("begin to post url:" + url);

			CloseableHttpResponse r = httpClient.execute(httpPost);
			// r.getEntity().getContent()

			String result = EntityUtils.toString(r.getEntity());
			r.close();

			return result;

		}  catch (Exception e) {
			logger.error("get data from [" + url + "] error", e);
			return "";
		}finally {

		}

	}
	
	/**
	 * 参数是一个json字符串
	 * @param url
	 * @param jsonParam
	 *            josn字符串参数
	 * @param second
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	/**
	 * @Project common-utilities
	 * @Package com.sp.framework.utilities.http
	 * @Method postJsonDataByJson方法.<br>
	 * @Description 使用字节流交互
	 * @author Alex Lu
	 * @date 2015年12月24日 下午2:19:35
	 * @param url	请求的地址
	 * @param b		body的字节流
	 * @param second	
	 * @return
	 * @throws IOException
	 */
	public static InputStream postJsonDataByJson(String url, byte[] b, int second) throws IOException {
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(makeLocalRequestConfig(second));
			if (b != null) {
				ContentType ct = ContentType.create("text/xml", Charset.forName("UTF-8"));
				ByteArrayEntity mult = new ByteArrayEntity(b, ct);
				httpPost.setEntity(mult);
			}
			logger.debug("begin to post url:" + url);
			CloseableHttpResponse r = httpClient.execute(httpPost);
			InputStream is = r.getEntity().getContent();
			r.close();
			return is;
		}  catch (Exception e) {
			logger.error("get data from [" + url + "] error", e);
			return null;
		}finally {

		}
	}

	public static String postJsonDataByJson(String url, Map<String, ?> params, int second) throws ClientProtocolException, IOException {

		try {
			String str = null;
			if (params != null) {
				str = JsonUtil.writeValue(params);
			}
			return postJsonDataByJson(url, str, second);
		} catch (Exception e) {
			logger.error("get data from [" + url + "] error", e);
			return "";
		} finally {

		}

	}

	public static String postJsonData(String url, Map<String, ?> params, int second, Header[] headers) {

		try {
			HttpPost httpPost = new HttpPost(url);

			httpPost.setConfig(makeLocalRequestConfig(second));

			if (headers != null)
				httpPost.setHeaders(headers);

			if (params != null) {
				List<NameValuePair> values = new ArrayList<NameValuePair>();
				for (Map.Entry<String, ?> entity : params.entrySet()) {
					BasicNameValuePair pare = new BasicNameValuePair(
							entity.getKey(), entity.getValue().toString());
					values.add(pare);

				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(values, "UTF-8");
				httpPost.setEntity(entity);
			}

			logger.debug("begin to post url:" + url);

			CloseableHttpResponse r = httpClient.execute(httpPost);

			String result = EntityUtils.toString(r.getEntity());
			r.close();
			return result;
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException!Post data to url:" + url + "error!", e);
			return "";
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException!Post data to url:" + url + "error!", e);
			return "";
		} catch (IOException e) {
			logger.error("IOException!Post data to url:" + url + "error!", e);
			return "";
		} finally {
			// httpclient.getConnectionManager().shutdown();
		}
	}

	public static String postJsonData(String url, Map<String, ?> params) {
		try {
			return postJsonData(url, params, 0, null);
		} catch (Exception e) {
			logger.error("get data from [" + url + "] error", e);
			return "";
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T postJsonObjectData(String url,
			HashMap<String, Object> params, Class<T> cls)
			throws ClientProtocolException, IOException {
		String str = postJsonData(url, params, 0, null);
		return (T) JsonUtil.readValue(str, cls);
	}


	public static Map<String, String> postJsonDataByJson(String url, String jsonParam, boolean gzip, int second, Header[] headers) {
		HashMap resultMap = new HashMap();

		try {
			try {
				HttpPost httpPost = new HttpPost(url);
				httpPost.setConfig(makeLocalRequestConfig(second));
				if (headers != null && headers.length > 0) {
					httpPost.setHeaders(headers);
				}

				Header[] cookies;
				if (jsonParam != null) {
					ContentType ct = ContentType.create("application/json", Charset.forName("UTF-8"));
					cookies = null;
					byte[] entitybytes;
					if (gzip) {
						entitybytes = GZIPUtils.compress(jsonParam);
						if (entitybytes != null) {
							httpPost.setHeader("Content-Encoding", "gzip");
						}
					} else {
						entitybytes = jsonParam.getBytes("UTF-8");
					}

					httpPost.setEntity(new ByteArrayEntity(entitybytes, ct));
				}

				logger.debug("begin to post url:" + url);
				CloseableHttpResponse r = httpClient.execute(httpPost);
				resultMap.put("status", Integer.toString(r.getStatusLine().getStatusCode()));
				resultMap.put("result", EntityUtils.toString(r.getEntity()));
				cookies = r.getHeaders("Set-Cookie");
				if (headers != null && cookies.length > 0) {
					Header cookieHeader = cookies[0];
					resultMap.put("cookie", cookieHeader.getValue());
				}

				r.close();
			} catch (Exception var13) {
				resultMap.put("error", var13.getMessage());
				logger.error("post data to [" + url + "] error", var13);
			}

			return resultMap;
		} finally {
			;
		}
	}

	public static void main(String[] args) {

		String str = HttpJsonClient.getJsonData("http://www.baidu.com", null);
		System.out.println(str);
		System.out
				.println("------------------------------------------------------");
		String str2 = HttpJsonClient.getJsonData(
				"https://cas-test.yonghui.cn:8443/casweb/login", null);
		System.out.println(str2);

		String url = "http://127.0.0.1:8080/person/recive2";
		// String
		// data="{"user_info":"{\"loginName\":\"rili123\",\"userName\":\"日历123\",\"jobNumber\":\"rl123\",\"email\":\"11@11.cn\",\"phone\":\"\",\"createTime\":1441516718000,\"lastLoginTime\":null,\"expiryDate\":null}"}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("str", "中文");
		HttpJsonClient.postJsonData(url, params);
	}

}
