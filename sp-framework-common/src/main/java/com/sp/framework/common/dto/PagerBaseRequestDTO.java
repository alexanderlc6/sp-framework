package com.sp.framework.common.dto;

import com.sp.framework.common.constant.CommonConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell on 2017/11/1.
 */
@ApiModel("API统一分页请求对象")
@Data
public class PagerBaseRequestDTO implements Serializable {
    /**
     * 是否进行分页(0: 不分页(默认),1：分页)
     */
    @ApiModelProperty(value = "是否进行分页(0: 不分页(默认),1：分页)",example="1")
    private Integer isPager = 0;

    /**
     * 分页页号(默认为第一页,索引为1)
     */
    @ApiModelProperty(value = "分页页号(默认为第一页,索引为1)",example="1")
    private Integer pageNum = CommonConstants.DEFAULT_LIST_PAGE_INDEX;

    /**
     * 分页每页记录数(默认20)
     */
    @ApiModelProperty(value = "分页每页记录数(默认20)",example="30")
    private Integer pageSize = CommonConstants.DEFAULT_LIST_PAGE_SIZE;

    /**
     * 排序属性标签名称列表
     */
    @ApiModelProperty(value = "排序属性标签名称列表",example="['STG_T1','STG_T2']")
    private List<String> sortPropertyTagNameList;

    public PagerBaseRequestDTO() {
    }

    public PagerBaseRequestDTO(Integer isPager, Integer pageNum, Integer pageSize) {
        this.isPager = isPager;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public PagerBaseRequestDTO(Integer isPager, Integer pageNum, Integer pageSize, List<String> sortPropertyTagNameList) {
        this.isPager = isPager;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.sortPropertyTagNameList = sortPropertyTagNameList;
    }
}
