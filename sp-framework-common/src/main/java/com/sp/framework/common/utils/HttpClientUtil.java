/**
 * @Copyright ®2015 Sinosoft Co. Ltd. All rights reserved.<br/>
 * 项目名称 : 电子商务云服务平台
 * 创建日期 : 2017年3月16日
 * 修改历史 : 
 *     1. [2017年3月16日]创建文件 by chihouliang
 */
package com.sp.framework.common.utils;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 【httpClient工具】
 * @author ganjunfang
 * @date 2018年3月15日
 * @version V1.0
 */
public class HttpClientUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
    public static String sendGet(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        InputStream is = null;
        //封装请求参数
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String str = "";
        StringBuffer build = new StringBuffer();
        try {
            //转换为键值对
            str = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
            //创建Get请求
            HttpGet httpGet = new HttpGet(url + "?" + str);
            //执行Get请求，
            response = httpClient.execute(httpGet);
            //得到响应体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                is = entity.getContent();
                //转换为字节输入流
                BufferedReader br = new BufferedReader(new InputStreamReader(is, Consts.UTF_8));
                String body = null;
                while ((body = br.readLine()) != null) {
                    build.append(body);
                    System.out.println(body);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭输入流，释放资源
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //消耗实体内容
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //关闭相应 丢弃http连接
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return build.toString();

    }

    public static String sendPost(String url, Map<String, String> paras) {
        String result = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        try {

            List<NameValuePair> list = new ArrayList<>();

            for (String key : paras.keySet()) {
                String value = paras.get(key);
                list.add(new BasicNameValuePair(key, value));
            }
            UrlEncodedFormEntity urlEntity = new UrlEncodedFormEntity(list,"utf-8");
            urlEntity.setContentEncoding("utf-8");

            post.setEntity(urlEntity);
            CloseableHttpResponse response = httpClient.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, "utf-8");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String request(String urlstr, String req) {
        HttpURLConnection conn = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            URL url = new URL(urlstr);
            // System.out.println("SERVER: info - " + telegram.toJsonString());
            byte[] info = req == null ? null : req.getBytes("UTF-8");

            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setReadTimeout(100000);
            conn.setConnectTimeout(100000);
            conn.setRequestProperty("Content-Type", "application/json");
            if (info != null){
                conn.setRequestProperty("Content-Length", String.valueOf(info.length)); }
            OutputStream up = conn.getOutputStream();
            if (info != null){
                up.write(info);}
            up.flush();
            up.close();

            in = conn.getInputStream();
            return stringOf(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null){
                    in.close();}
            } catch (IOException e) {}

            try {
                if (out != null){
                    out.close();}
            } catch (IOException e) {}

            if (conn != null){
                conn.disconnect();}
        }

        return null;
    }

    public static String stringOf(InputStream in) {
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();

            byte[] b = new byte[2048];
            int c;
            while ((c = in.read(b)) >= 0) {
                out.write(b, 0, c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null){
                    out.close();}
            } catch (IOException e) {}
        }

        byte[] b = out.toByteArray();

        try {
            String str = new String(b, "UTF-8");
            return str == null || "".equals(str) || "null".equalsIgnoreCase(str) ? null : str;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
