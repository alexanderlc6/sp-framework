package com.sp.framework.common.dto;

import com.sp.framework.common.constant.CommonConstants;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author alexlu
 * @date 2019/4/3
 */
@Data
public class SortPropertyInfoDTO {
    /**
     * 排序标签名称
     */
    private String sortTagName;

    /**
     * 排序属性名称
     */
    private String sortPropertyName;

    /**
     * 字段名
     */
    @JsonIgnore
    private String sortField;

    /**
     * 排序顺序(asc/desc)
     */
    private String sortDirection = CommonConstants.QUERY_SORT_DIRECTION_ASC;

    public SortPropertyInfoDTO() {
    }

    public SortPropertyInfoDTO(String sortTagName, String sortPropertyName, String sortField, String sortDirection) {
        this.sortTagName = sortTagName;
        this.sortPropertyName = sortPropertyName;
        this.sortField = sortField;
        this.sortDirection = sortDirection;
    }
}
