package com.sp.framework.common.constant;

/**
 * 文件相关常量定义
 *
 * @author Alex Lu
 * @date 2019/12/13
 */
public class FileConstant {
    /**
     * 待处理Excel文件类型
     * 1-少量数据(少于1000行)
     * 2-大量数据(大于1000行)
     */
    public static final Integer PROCESS_EXCEL_TYPE_FEW_CONTENT_ROW = 1;
    public static final Integer PROCESS_EXCEL_TYPE_BULK_CONTENT_ROW = 2;
}
