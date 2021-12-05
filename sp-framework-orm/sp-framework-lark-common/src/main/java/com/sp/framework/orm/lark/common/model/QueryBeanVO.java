/**
 * 
 */
package com.sp.framework.orm.lark.common.model;

import com.sp.framework.orm.lark.common.dao.Page;
import com.sp.framework.orm.lark.common.dao.Sort;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * 查询请求条件VO定义
 * @author Alex Lu
 * @creattime 2021-4-5
 */
@ApiModel("查询条件对象VO定义")
public class QueryBeanVO {
    @ApiModelProperty("分页条件对象")
    private Page page = new Page();

    @ApiModelProperty("排序条件对象")
    private Sort[] sorts;

    @ApiModelProperty("查询参数集合")
    private Map<String, Object> params;

    @ApiModelProperty("排序参数集合")
    private Map<String, String> sortDirection;

    public Map<String, String> getSortDirection() {
	return sortDirection;
    }

    public void setSortDirection(Map<String, String> sortDirection) {
	this.sortDirection = sortDirection;
    }

    public Sort[] getSorts() {
	return sorts;
    }

    public void setSorts(Sort[] sorts) {
	this.sorts = sorts;
    }

    public Map<String, Object> getParams() {
	return params;
    }

    public void setParams(Map<String, Object> params) {
	this.params = params;
    }

    public Page getPage() {
	return page;
    }

    public void setPage(Page page) {
	this.page = page;
    }
}
