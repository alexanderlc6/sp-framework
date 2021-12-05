package com.sp.framework.common.utils.process;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Excel头行-测试类
 *
 * @author Alex Lu
 * @date 2019/12/13
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExcelHeaderTestModel extends BaseRowModel {
    /**
     * 姓名
     */
    @ExcelProperty(value = "name", index = 0)
    private String name;

    /**
     * 年龄
     */
    @ExcelProperty(value = "age", index = 1)
    private Integer age;

    /**
     * 学校
     */
    @ExcelProperty(value = "school", index = 2)
    private String school;
}
