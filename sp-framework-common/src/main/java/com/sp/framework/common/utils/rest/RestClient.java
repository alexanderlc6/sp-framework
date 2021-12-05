package com.sp.framework.common.utils.rest;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * @author alexlu
 * @ClassName: RestClient
 * @Description: (http请求工具类)
 * @date 2017年4月6日
 */
public class RestClient {
    private final static Logger logger = LoggerFactory.getLogger(RestClient.class);
    private String serverUrl;
    private String httpMethod;
    private String clientData;
    private int connectTimeout;
    private int socketTimeout;

    public RestClient(String url, String method, String data) {
        this.serverUrl = url;
        this.httpMethod = method;
        this.clientData = data;
        this.connectTimeout = -1;
        this.socketTimeout = -1;
    }

    public RestClient(String url) {
        this.serverUrl = url;
        this.httpMethod = "GET";
        this.clientData = null;
        this.connectTimeout = -1;
        this.socketTimeout = -1;
    }

    public RestClient(String url, String data) {
        this.serverUrl = url;
        this.httpMethod = "GET";
        this.clientData = data;
        this.connectTimeout = -1;
        this.socketTimeout = -1;
    }

    public void setURL(String url) {
        this.serverUrl = url;
    }

    public void setMethod(String method) {
        this.httpMethod = method;
    }

    public void setData(String data) {
        this.clientData = data;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public String execute() throws RestException {
        if (null == this.serverUrl) {
            return null;
        }

        this.clientData = RestCodec.encodeData(this.clientData);
        String ret = httpExecute();
        if (null != ret) {
            System.out.println(ret);
            ret = RestCodec.decodeData(ret);
        }
        return ret;
    }

    public String executeWithNoEncode() {
        if (null == this.serverUrl) {
            return null;
        }
        String ret = "";
        ;
        try {
            ret = httpExecute();
        } catch (RestException e) {
            return ret;
        }
        return ret;
    }

    public String executeWithNoEncodeWithHeader(Map<String, String> reuqestHeader) {
        if (null == this.serverUrl) {
            return null;
        }
        String ret = "";
        ;
        try {
            ret = httpExecuteWithHeader(reuqestHeader);
        } catch (RestException e) {
            return ret;
        }
        return ret;
    }

    public String executeNotDecode() throws RestException {
        if (null == this.serverUrl) {
            return null;
        }

        this.clientData = RestCodec.encodeData(this.clientData);
        String ret = httpExecute();
        return ret;
    }

    private DefaultHttpClient getHttpClient() {
        DefaultHttpClient httpClient = new DefaultHttpClient();

        httpClient.getParams().setParameter("http.protocol.content-charset", "utf-8");
        if (0 < this.connectTimeout) {
            httpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(this.connectTimeout * 1000));
        }

        if (0 < this.socketTimeout) {
            httpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(this.socketTimeout * 1000));
        }

        return httpClient;
    }

    private String getURLWithData() {
        if (this.clientData != null) {
            if (this.serverUrl.endsWith("?")) {
                return this.serverUrl + this.clientData;
            }
            return this.serverUrl + "?" + this.clientData;
        }

        return this.serverUrl;
    }

    public String httpExecute() throws RestException {
        String ret = null;
        DefaultHttpClient httpclient = getHttpClient();
        try {
            ResponseHandler responseHandler = new BasicResponseHandler();
            if ("GET".equalsIgnoreCase(this.httpMethod)) {
                HttpGet req = new HttpGet(getURLWithData());
                ret = (String) httpclient.execute(req, responseHandler);
                System.out.println("------" + ret);
            } else if ("POST".equalsIgnoreCase(this.httpMethod)) {
                HttpPost req = new HttpPost(this.serverUrl);
                if (null != this.clientData) {
                    req.setEntity(new StringEntity(this.clientData, "UTF-8"));
                }
                req.setHeader("Content-Type", "application/json;charset=UTF-8");
                ret = (String) httpclient.execute(req, responseHandler);
            } else if ("PUT".equalsIgnoreCase(this.httpMethod)) {
                HttpPut req = new HttpPut(this.serverUrl);
                if (null != this.clientData) {
                    req.setEntity(new StringEntity(this.clientData));
                }

                ret = (String) httpclient.execute(req, responseHandler);
            } else if ("DELETE".equalsIgnoreCase(this.httpMethod)) {
                HttpDelete req = new HttpDelete(getURLWithData());
                ret = (String) httpclient.execute(req, responseHandler);
            }
        } catch (ClientProtocolException e) {
            throw new RestException(e.getMessage(), e.getCause());
        } catch (IOException e) {
            throw new RestException(e.getMessage(), e.getCause());
        } catch (Exception e) {
            throw new RestException(e.getMessage(), e.getCause());
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return ret;
    }

    /**
     * 带指定http头的请求
     *
     * @param headerMap
     * @return result
     * @throws RestException
     * @author felix
     */
    public String httpExecute(Map<String, String> headerMap) throws RestException {
        String ret = null;
        DefaultHttpClient httpclient = getHttpClient();
        try {
            ResponseHandler<?> responseHandler = new BasicResponseHandler();
            if ("GET".equalsIgnoreCase(this.httpMethod)) {
                HttpGet req = new HttpGet(getURLWithData());
                if (null != headerMap && !headerMap.isEmpty()) {
                    for (String key : headerMap.keySet()) {
                        req.addHeader(new BasicHeader(key, headerMap.get(key)));
                    }
                }
                ret = (String) httpclient.execute(req, responseHandler);
            } else if ("POST".equalsIgnoreCase(this.httpMethod)) {
                HttpPost req = new HttpPost(this.serverUrl);
                if (null != headerMap && !headerMap.isEmpty()) {
                    for (String key : headerMap.keySet()) {
                        req.addHeader(new BasicHeader(key, headerMap.get(key)));
                    }
                }
                if (null != this.clientData) {
                    req.setEntity(new StringEntity(this.clientData, "utf-8"));
                }
                ret = (String) httpclient.execute(req, responseHandler);
            } else if ("PUT".equalsIgnoreCase(this.httpMethod)) {
                HttpPut req = new HttpPut(this.serverUrl);
                if (null != headerMap && !headerMap.isEmpty()) {
                    for (String key : headerMap.keySet()) {
                        req.addHeader(new BasicHeader(key, headerMap.get(key)));
                    }
                }
                if (null != this.clientData) {
                    req.setEntity(new StringEntity(this.clientData));
                }
                ret = (String) httpclient.execute(req, responseHandler);
            } else if ("DELETE".equalsIgnoreCase(this.httpMethod)) {
                HttpDelete req = new HttpDelete(getURLWithData());
                if (null != headerMap && !headerMap.isEmpty()) {
                    for (String key : headerMap.keySet()) {
                        req.addHeader(new BasicHeader(key, headerMap.get(key)));
                    }
                }
                ret = (String) httpclient.execute(req, responseHandler);
            }
        } catch (Exception e) {
            throw new RestException(e.getMessage(), e.getCause());
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return ret;
    }

    /**
     * doPost(post请求)
     */
    public String doPost(String path, String param) {
        try {
            // 创建连接
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();

            // POST请求
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            out.writeBytes(param);
            out.flush();
            out.close();

            // 读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            reader.close();
            // 断开连接
            connection.disconnect();
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String httpExecuteWithHeader(Map<String, String> headerMap) throws RestException {
        String ret = null;
        DefaultHttpClient httpclient = getHttpClient();
        try {
            ResponseHandler responseHandler = new BasicResponseHandler();
            if ("GET".equalsIgnoreCase(this.httpMethod)) {
                HttpGet req = new HttpGet(getURLWithData());
                if (headerMap != null && headerMap.containsKey("Authorization")) {
                    req.setHeader("Authorization", headerMap.get("Authorization"));
                }
                ret = (String) httpclient.execute(req, responseHandler);
                System.out.println("------" + ret);
            } else if ("POST".equalsIgnoreCase(this.httpMethod)) {
                HttpPost req = new HttpPost(this.serverUrl);
                if (null != this.clientData) {
                    req.setEntity(new StringEntity(this.clientData, "UTF-8"));
                }
                if (headerMap != null && headerMap.containsKey("version")) {
                    req.setHeader("version", headerMap.get("version"));
                }
                if (headerMap != null && headerMap.containsKey("Authorization")) {
                    req.setHeader("Authorization", headerMap.get("Authorization"));
                }
                req.setHeader("Content-Type", "application/json;charset=UTF-8");
                ret = (String) httpclient.execute(req, responseHandler);
            } else if ("PUT".equalsIgnoreCase(this.httpMethod)) {
                HttpPut req = new HttpPut(this.serverUrl);
                if (null != this.clientData) {
                    req.setEntity(new StringEntity(this.clientData));
                }

                ret = (String) httpclient.execute(req, responseHandler);
            } else if ("DELETE".equalsIgnoreCase(this.httpMethod)) {
                HttpDelete req = new HttpDelete(getURLWithData());
                ret = (String) httpclient.execute(req, responseHandler);
            }
        } catch (ClientProtocolException e) {
            throw new RestException(e.getMessage(), e.getCause());
        } catch (IOException e) {
            throw new RestException(e.getMessage(), e.getCause());
        } catch (Exception e) {
            throw new RestException(e.getMessage(), e.getCause());
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return ret;
    }

}
