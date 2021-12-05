package com.sp.framework.web.handler;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.sp.framework.common.constant.CommonConstants;
import com.sp.framework.common.enums.SystemErrorCodeEnum;
import com.sp.framework.common.exception.BusinessException;
import com.sp.framework.common.exception.KnownSysException;
import com.sp.framework.common.exception.SystemException;
import com.sp.framework.common.utils.ResponseUtil;
import com.sp.framework.common.vo.ResponseVO;

/**
 * @Description: 捕获全局异常增强处理器
 * @author Alex Lu
 * @date 2021年4月9日
 * @version
 */
@ControllerAdvice
public class GlobalExceptionHandler implements HandlerExceptionResolver {
    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义的系统异常或业务异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = {SystemException.class, BusinessException.class, KnownSysException.class,})
    @ResponseBody
    public ResponseVO<Object> handleBizException(Throwable e) {
        logger.error("执行发生异常:{}", e.getMessage(), e);

        if(e instanceof BusinessException) {
            return ResponseUtil.getFailure(((BusinessException) e).getCode(), ((BusinessException) e).getMsg());
        }

        if(e instanceof SystemException){
            return ResponseUtil.getFailure(((SystemException) e).getCode(), ((SystemException) e).getMsg());
        }

        if(e instanceof KnownSysException){
            return ResponseUtil.getFailure(((KnownSysException) e).getCode(), ((KnownSysException) e).getMsg());
        }

        return ResponseUtil.getFailure(SystemErrorCodeEnum.SYSTEM_ERROR.getCode(),
                SystemErrorCodeEnum.SYSTEM_ERROR.getMsg());
    }

    /**
     * 处理运行时业务异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public ResponseVO<Object> handleRuntimeBizException(HttpServletRequest req, RuntimeException e) {
        recordRequestData(req);
        logger.error("发生业务异常！原因是：{}", e.getMessage(), e);
        return ResponseUtil.getFailure(SystemErrorCodeEnum.SYSTEM_ERROR.getMsg());
    }

    /**
     * 处理方法参数校验不合法异常,输出所有校验提示文本
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVO<Object> checkMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error("API请求体参数校验异常！原因是:{}", e.getMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        String errorMsg = "";
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMsg += fieldError.getDefaultMessage() + "!";
        }

        return ResponseUtil.getFailure(SystemErrorCodeEnum.API_REQUEST_PARAM_NOT_VALID_EXCEPTION.getCode(),
                SystemErrorCodeEnum.API_REQUEST_PARAM_NOT_VALID_EXCEPTION.getMsg() + errorMsg);
    }

    /**
     * 处理API请求参数校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = ServletRequestBindingException.class)
    @ResponseBody
    public ResponseVO<Object> checkServletRequestBindingException(ServletRequestBindingException e) {
        logger.error("API请求参数校验异常！原因是:{}", e.getMessage(), e);

        return ResponseUtil.getFailure(SystemErrorCodeEnum.API_REQUEST_PARAM_NOT_VALID_EXCEPTION.getCode(),
                String.format("%s(%s)", SystemErrorCodeEnum.API_REQUEST_PARAM_NOT_VALID_EXCEPTION.getMsg(),
                        e.getMessage()));
    }

    /**
     * 处理参数校验数据绑定异常,输出绑定异常提示文本
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ResponseVO<Object> checkParamBindException(BindException e) {
        logger.error("校验绑定发生异常！原因是:{}", e.getMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        String errorMesssage = Strings.EMPTY;
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMesssage += fieldError.getDefaultMessage() + "!";
        }

        return ResponseUtil.getFailure(SystemErrorCodeEnum.API_PARAM_BIND_EXCEPTION.getCode(),
                SystemErrorCodeEnum.API_PARAM_BIND_EXCEPTION.getMsg() + errorMesssage);
    }

    /**
     * 处理DB SQL约束校验执行异常,输出违反约束异常提示文本
     * @param e
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public ResponseVO<Object> checkConstraintViolationException(ConstraintViolationException e) {
        logger.error("校验执行异常！原因是:{}", e.getMessage(), e);
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();
        List<String> msgList = new ArrayList();
        while (iterator.hasNext()) {
            ConstraintViolation<?> cvl = iterator.next();
            msgList.add(cvl.getMessageTemplate());
        }

        return ResponseUtil.getFailure(SystemErrorCodeEnum.DB_SQL_EXECUTE_CONSTRAINT_EXCEPTION.getCode(),
                SystemErrorCodeEnum.DB_SQL_EXECUTE_CONSTRAINT_EXCEPTION.getMsg()
                        + String.join(CommonConstants.TAG_COMMA, msgList));
    }

    /**
     * 处理兜底全局拦截异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseVO<Object> handleRootException(HttpServletRequest req, Exception e) {
        logger.error("未知异常！原因是:{}", e.getMessage(), e);
        return ResponseUtil.getFailure(SystemErrorCodeEnum.SYSTEM_ERROR.getMsg());
    }

    /**
     * 记录API请求数据
     * 仅当Debug模式才输出
     * @param request
     */
    private void recordRequestData(HttpServletRequest request){
        if(!logger.isDebugEnabled()){
            return;
        }

        StringBuilder sb = new StringBuilder();
        String line;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception ignore) {
        }

        logger.debug("[Request] IP:{}, Local Port:{}, Url:{}, Post Data:{}",
                getIpAddr(request), request.getLocalPort(), request.getRequestURI(), sb.toString());
    }

    /**
     * 获取客户端IP地址
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception ex) {
	    return null;
    }
}
