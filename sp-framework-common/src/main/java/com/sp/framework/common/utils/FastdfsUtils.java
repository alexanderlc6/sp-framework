package com.sp.framework.common.utils;

/**
 * Created by Alexa on 2017/8/7.
 */

import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class FastdfsUtils {
    private static final Logger logger = LoggerFactory.getLogger(FastdfsUtils.class);

    public FastdfsUtils() {
    }

    private static String upload(String orgFile, String uploadUrl, boolean isDelete, String uploader) {
        String result = null;
        File file = new File(orgFile);
        if(file.exists()) {
            try {
                result = upload(new FileInputStream(file), file.getName(), uploadUrl, uploader);
            } catch (IOException var7) {
                logger.error("FastdfsUtils 涓婁紶鏂囦欢澶辫触!{}", var7);
            }

            if(isDelete) {
                file.delete();
            }
        }

        return result;
    }



    private static String upload(InputStream inputStream, String filename, String uploadUrl, String uploader) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpPost httpPost = new HttpPost(uploadUrl);
            String textValue = "textValue";
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addTextBody("text", textValue, ContentType.DEFAULT_BINARY);
            builder.addBinaryBody("file", inputStream, ContentType.DEFAULT_BINARY, filename);
            builder.addTextBody("uploader", uploader);
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            Map map = (Map)JacksonUtil.nonDefaultMapper().fromJson(EntityUtils.toString(response.getEntity()), Map.class);
            if(map != null && MapUtils.getBooleanValue(map, "success")) {
                Map result = MapUtils.getMap(map, "result");
                String var12 = MapUtils.getString(result, "group") + "/" + MapUtils.getString(result, "path");
                return var12;
            }
        } finally {
            httpClient.close();
        }

        return null;
    }
}

