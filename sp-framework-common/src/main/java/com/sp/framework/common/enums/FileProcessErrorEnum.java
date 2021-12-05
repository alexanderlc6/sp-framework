package com.sp.framework.common.enums;

/**
 * 文件操作错误枚举定义
 *
 * @author Alex Lu
 * @date 2019/12/13
 */
public enum  FileProcessErrorEnum {

    SUCCESS(1000, "操作成功!"),
    NOT_FOUND_FILE_EXCEPTION(2001, "找不到文件或文件路径错误,文件地址:{}"),
    FILE_READ_EXCEPTION(2002, "Excel文件读取失败, 失败原因:{}"),
    FILE_EXPORT_FAILED(2003, "Excel文件导出失败, 失败原因：{}"),
    CSV_IMPORT_HEADER_ERROR(4201, "csv文件导入未发现预定表头!"),
    CSV_READ_ERROR(4202, "CSV文件读取失败！!");

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String message;


    /**
     * 使用错误码和错误信息构造枚举
     *
     * @param code    错误码
     * @param message 错误信息
     */
    FileProcessErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message != null ? message : "";
    }

    /**
     * 获取错误码
     *
     * @return int
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取错误信息
     *
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * 根据错误码获取错误信息
     * @param code
     * @return
     */
    public static String getValueByCode(Integer code) {
        for (FileProcessErrorEnum item : FileProcessErrorEnum.values()) {
            if (item.getCode().equals(code)) {
                return item.getMessage();
            }
        }
        return null;
    }
}
