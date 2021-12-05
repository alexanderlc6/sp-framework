package com.sp.framework.common.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 数据库实体PO对象基类
 * created on 2018/9/13.
 */
@Data
public class BasePO {
    /**
     * 递增ID(主键字段)
     */
    private Long id;

    /**
     * 乐观锁标识
     */
    @JsonIgnore
    protected Long version;

    /**
     * 是否有效(0:未删除的有效记录 1:逻辑删除的无效记录)
     */
    @JsonIgnore
    protected Boolean delete;

    /**
     * 创建人
     */
    @JsonIgnore
    private String createdBy;

    /**
     * 更新人
     */
    @JsonIgnore
    private String updatedBy;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @JsonIgnore
    private Date createdAt;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @JsonIgnore
    private Date updatedAt;
}
