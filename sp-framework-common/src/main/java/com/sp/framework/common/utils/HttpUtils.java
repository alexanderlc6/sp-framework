package com.sp.framework.common.utils;

import org.apache.http.*;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;

public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static final int TIMEOUT = 10 * 1000;

    private static volatile CloseableHttpClient httpClient;

    private static HttpUtils instance;

    private HttpUtils() {
        init();
    }

    public synchronized static HttpUtils getInstance() {
        if (instance == null) {
            instance = new HttpUtils();
        }
        return instance;
    }

    private void init() {
        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(new TrustStrategy() {
                        @Override
                        public boolean isTrusted(final X509Certificate[] chain, final String authType)
                                throws CertificateException {
                            return true;
                        }
                    }).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            logger.error("Http client initial failure!", e);
        }

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext, SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        // Create a registry of custom connection socket factories for supported protocol schemes.
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", sslsf).build();

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry);
        connManager.setMaxTotal(200);
        connManager.setDefaultMaxPerRoute(100);

        // Create connection configuration
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8)
                .build();
        // Configure the connection manager to use connection configuration
        // either
        // by default or for a specific host.
        connManager.setDefaultConnectionConfig(connectionConfig);

        // Create global request configuration
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.DEFAULT)
                .setExpectContinueEnabled(true)
                .setSocketTimeout(TIMEOUT)
                .setConnectTimeout(TIMEOUT)
                .setConnectionRequestTimeout(TIMEOUT)
                .build();

        httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(defaultRequestConfig)
                .addInterceptorFirst(new HttpRequestInterceptor() {// 请求
                    @Override
                    public void process(final HttpRequest request, final HttpContext context)
                            throws HttpException, IOException {
                        if (!request.containsHeader("Accept-Encoding")) {
                            request.addHeader("Accept-Encoding", "gzip");
                        }
                        if (!request.containsHeader("Content-Type")) {
                            request.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                        }
                        if (!request.containsHeader("User-Agent")) {
                            request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; rv:45.0) Gecko/20100101 Firefox/45.0");
                        }
                        request.addHeader("Connection", "keep-alive");
                        request.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
                        request.addHeader("Accept-Charset", "utf-8");
                        request.addHeader("Accept-Encoding", "gzip");
                    }
                }).addInterceptorFirst(new HttpResponseInterceptor() {// 响应
                    @Override
                    public void process(final HttpResponse response, final HttpContext context)
                            throws HttpException, IOException {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            Header ceHeader = entity.getContentEncoding();
                            if (ceHeader != null) {
                                HeaderElement[] elements = ceHeader.getElements();
                                for (HeaderElement element : elements) {
                                    if ("gzip".equalsIgnoreCase(element.getName())) {
                                        response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }).build();
    }

    /**
     * 发送给请求
     *
     * @param url
     * @return
     */
    public String get(final String url) throws IOException {
        return request(new HttpGet(url));
    }

    public String post(final String url, final String param) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        if (param != null) {
            httpPost.setEntity(new StringEntity(param, "utf-8"));
        }
        return request(httpPost);
    }

    public String postJson(final String url, final String param) throws IOException {
        long startTime = System.currentTimeMillis();
        HttpPost httpPost = new HttpPost(url);
        if (param != null) {
            httpPost.setEntity(new StringEntity(param, "utf-8"));
            httpPost.setHeader("Content-type", "application/json"); // update by lixueyang
        }
        String result = request(httpPost);
        if(logger.isDebugEnabled()){
            logger.debug("postJson耗时{},url={},param={}",System.currentTimeMillis()-startTime,url,param);
        }
        return result;
    }

    public String postJson(String apiUrl, String jsonData, Map<String,String> headers){
        HttpRequest request = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        StringEntity entity;
        String respContent=null;
        try {
            HttpPost httpPost = (HttpPost) request;
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("charset","utf-8");
            Set<Map.Entry<String,String>> entries = headers.entrySet();
            for (Map.Entry<String,String> entry:entries){
                httpPost.addHeader(entry.getKey(),entry.getValue());
            }
            entity = new StringEntity(jsonData,"utf-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            response=httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == 200) {
                 HttpEntity he = response.getEntity();
                respContent = EntityUtils.toString(he,"UTF-8");
            }
            return respContent;
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }


    public String request(final HttpRequestBase httpRequest) throws IOException {
        String result;
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpRequest);
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity, "utf-8");
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode / 100 == 2) {
                if (logger.isTraceEnabled()) {
                    logger.trace("http:url={}, status={}", httpRequest.getURI().toString(),
                            statusLine.getStatusCode() + "," + statusLine.getReasonPhrase());
                }
            } else {
                logger.warn("接口调用失败！url:{}, StatusCode:{}, response:{}",
                        httpRequest.getURI().toString(), statusLine.getStatusCode(), result);
            }
            EntityUtils.consume(httpEntity);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception ex) {
                logger.warn("Http response close failure！", ex);
            }
        }
        return result;
    }

    /*
    private class AllTrustManager implements X509TrustManager{
        @Override
        public void checkClientTrusted(X509Certificate[] arg0,
                String arg1) throws CertificateException {

        }
        @Override
        public void checkServerTrusted(X509Certificate[] arg0,
                String arg1) throws CertificateException {

        }
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }
    }
    */
}
