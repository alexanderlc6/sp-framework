package com.sp.framework.web.handler;


import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Arrays;

import com.sp.framework.common.enums.SystemErrorCodeEnum;
import com.sp.framework.common.vo.ResponseVO;

/**
 * @Description: 全局统一返回类型增强处理
 * @author Alex Lu
 * @date 2021年4月9日
 * @version
 */
@ControllerAdvice
public class CommonApiResponseHandler implements ResponseBodyAdvice<Object> {
    private final static String[] RESERVED_URLS = new String[]{
            "/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs",
            "/**/health","/druid/**","/actuator/**"};

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (BeanUtils.isSimpleProperty(returnType.getParameterType())){
            return false;
        }

        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
            MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response) {
        //判断是否在需要跳过的白名单之内
        final String curRequestUri = request.getURI().getPath();
        PathMatcher pathMatcher = new AntPathMatcher();
        if(Arrays.stream(RESERVED_URLS)
                .filter(t -> t.equals(curRequestUri) || pathMatcher.match(t, curRequestUri))
                .findAny().isPresent()){
            return body;
        }

        if (MediaType.APPLICATION_JSON.equals(selectedContentType)
                || MediaType.APPLICATION_JSON_UTF8.equals(selectedContentType)) {
            //判断响应的Content-Type为JSON格式的body
            if (body instanceof ResponseVO) {
                // 如果响应返回的对象为统一响应体，则直接返回body
                return body;
            } else {
                ResponseVO<Object> responseVO = new ResponseVO();
                responseVO.setSuccess(true);
                responseVO.setCode(SystemErrorCodeEnum.SUCCESS.getCode());
                responseVO.setData(body);

                //只有正常返回的结果才会进入此判断流程，所以返回正常成功的状态码
                return responseVO;
            }
        }

        //非JSON格式body直接返回即可
        return body;
    }
}
