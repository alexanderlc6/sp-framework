package com.sp.framework.common.enums;

/**
 * 广告素材类型
 * Created by shaohan.zhao on 2017/10/15.
 */
public enum MaterialTypeEnum {
    TEXT(1, "文字"),
    IMAGE(2, "图片"),
    FLASH(3, "flash"),
    SCRIPT(4, "代码"),
    VIDEO(5, "视频");

    private Integer code;
    private String value;

    private MaterialTypeEnum(Integer code, String value) {
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
            for (MaterialTypeEnum item : MaterialTypeEnum.values()) {
                if (item.getCode().equals(code)) {
                    return item.getValue();
                }
            }

            return "";
        }
    }

    public static MaterialTypeEnum getTypeByCode(Integer code) {
        MaterialTypeEnum curr = null;
        for (MaterialTypeEnum item : MaterialTypeEnum.values()) {
            if (item.getCode().equals(code)) {
                curr = item;
                break;
            }
        }
        return curr;
    }
}
