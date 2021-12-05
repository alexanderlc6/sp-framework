package com.sp.framework.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 基础分页结果响应类
 * Created by alexlu on 2018/6/22.
 */
@ApiModel("API统一分页返回对象")
@Data
public class PagerBaseResponseDTO<T> implements Serializable {
    /**
     * 总记录数
     */
    @ApiModelProperty(value = "总记录数", example="120")
    private Long totalCount;

    /**
     * 分页(单页)数据
     */
    @ApiModelProperty(value = "分页(单页)数据", example="[{xxx1:xxx},{xxx2:xxx}]")
    private List<T> pageData;

    /**
     * 排序属性集合
     */
    @ApiModelProperty(value = "排序属性集合", example="[{sortTagName:'STG_T1',sortPropertyName:'Test1',sortField:'test_1',sortDirection:asc}")
    private List<SortPropertyInfoDTO> sortPropertyInfoList;

    public PagerBaseResponseDTO() {
    }

    public PagerBaseResponseDTO(Long totalCount, List<T> pageData) {
        this.totalCount = totalCount;
        this.pageData = pageData;
    }

    public PagerBaseResponseDTO(Long totalCount, List<T> pageData, List<SortPropertyInfoDTO> sortPropertyInfoList) {
        this.totalCount = totalCount;
        this.pageData = pageData;
        this.sortPropertyInfoList = sortPropertyInfoList;
    }
}
