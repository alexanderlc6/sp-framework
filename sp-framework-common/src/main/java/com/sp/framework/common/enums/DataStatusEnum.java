package com.sp.framework.common.enums;

/**
 * 数据状态
 * Created by shaohan.zhao on 2017/10/15.
 */
public enum DataStatusEnum {
    NORMAL(0, "正常"),
    DISABLED(1, "禁用");

    private Integer code;
    private String value;

    private DataStatusEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static String valueOf(Integer code) {
        if (code == null) {
            return "";
        } else {
            for (DataStatusEnum item : DataStatusEnum.values()) {
                if (item.getCode().equals(code)) {
                    return item.getValue();
                }
            }

            return "";
        }
    }

    public static DataStatusEnum getTypeByCode(Integer code) {
        DataStatusEnum curr = null;
        for (DataStatusEnum item : DataStatusEnum.values()) {
            if (item.getCode().equals(code)) {
                curr = item;
                break;
            }
        }
        return curr;
    }
}
