package com.sp.framework.common.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.sp.framework.common.enums.SystemErrorCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 响应对象VO基类
 * 适用于与前台界面交互响应属性定义
 *
 * @author alex.lu
 * @date 2021/1/30 12:19
 */
@ApiModel("API接口统一返回VO对象")
@Data
public class ResponseVO<T> implements Serializable{

    private static final long serialVersionUID = 5666241484577450742L;
    /**
     * 错误信息
     */
    @ApiModelProperty(value = "返回标识信息",example="Succeed!")
    @JSONField(alternateNames = {"msg", "message"})
    private String msg;

    /**
     * 是否成功标识
     */
    @ApiModelProperty(value = "是否成功标识",example="true")
    private Boolean success = false;

    /**
     * 返回响应码
     */
    @ApiModelProperty(value = "返回响应码(当编码为200000默认为成功标识,包含业务错误码)",example="20000")
    private Integer code = 0;

    /**
     * 返回对象
     */
    @ApiModelProperty(value = "返回对象", required = false)
    private T data;

    public ResponseVO(){}

    public ResponseVO(Integer code,String msg){
        this.msg = msg;
        this.code = code;

        if(code.equals(SystemErrorCodeEnum.SUCCESS.getCode())) {
            this.success = true;
        }
    }

    public ResponseVO(Boolean success,String msg, Integer code){
        this.msg = msg;
        this.code = code;
        this.success = success;
    };

    public ResponseVO(String msg,int code,T data){
        this(code,msg);
        this.data = data;
    };

    public ResponseVO(boolean success,String msg, Integer code,T data){
        this(success, msg, code);
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseVO{" +
                "msg='" + msg + '\'' +
                ", success=" + success +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
