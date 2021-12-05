package com.sp.framework.common.utils.process;

import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 多个Excel工作集类
 *
 * @author Alex Lu
 * @date 2019/12/13
 */
@Getter
@Setter
public class MultipleSheetModel {
    /**
     * 源数据集合
     */
    private List<? extends BaseRowModel> dataList;

    /**
     * Excel工作集对象
     */
    private Sheet sheet;
}
