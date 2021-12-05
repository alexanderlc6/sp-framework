package com.sp.framework.utilities.http.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @description:
 * @author: luchao
 * @date: Created in 4/7/21 6:19 PM
 */
public class GzipFilter {
    public GzipFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        String contentEncoding = req.getHeader("Content-Encoding");
        if (null != contentEncoding && contentEncoding.toLowerCase().indexOf("gzip") != -1) {
            chain.doFilter(new GzipRequestWrapper(req), response);
        } else {
            chain.doFilter(request, response);
        }

    }

    public void destroy() {
    }
}
