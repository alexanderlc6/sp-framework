package com.sp.framework.common.dto;

import com.sp.framework.common.annotation.ObtainFieldNames;
import lombok.Data;

/**
 * @description: CAS文件信息DTO
 * @author: luchao
 * @create: 2021-03-19 18:15
 **/
@Data
public class CsvFileInfoDTO {
    /**
     * CAS文件路径
     */
    @ObtainFieldNames
    private String path;

}
