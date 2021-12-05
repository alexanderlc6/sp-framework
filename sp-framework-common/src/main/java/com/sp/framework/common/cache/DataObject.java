package com.sp.framework.common.cache;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 数据存储对象
 *
 * @author Alex Lu
 * @date 2020/4/20
 */
@Getter
@Setter
public class DataObject implements Serializable {
    private Object data;

    public DataObject(Object data) {
        this.data = data;
    }

    private static int objectCounter = 0;

    public static DataObject get(Object data){
        objectCounter++;
        return new DataObject(data);
    }
}
