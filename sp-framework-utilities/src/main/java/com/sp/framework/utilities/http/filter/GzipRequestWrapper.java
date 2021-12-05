package com.sp.framework.utilities.http.filter;

import java.io.IOException;
import java.util.zip.GZIPInputStream;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @description:
 * @author: luchao
 * @date: Created in 4/7/21 6:19 PM
 */
public class GzipRequestWrapper extends HttpServletRequestWrapper {
    private HttpServletRequest request;
    private static final Logger logger = LoggerFactory.getLogger(GzipRequestWrapper.class);

    public GzipRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ServletInputStream stream = this.request.getInputStream();
        String contentEncoding = this.request.getHeader("Content-Encoding");
        if (null != contentEncoding && contentEncoding.indexOf("gzip") != -1) {
            try {
                final GZIPInputStream gzipInputStream = new GZIPInputStream(stream);
                ServletInputStream newStream = new ServletInputStream() {
                    public int read() throws IOException {
                        return gzipInputStream.read();
                    }
                };
                return newStream;
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }

        return stream;
    }
}
