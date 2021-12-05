package com.sp.framework.common.utils.process;

import com.sp.framework.common.dto.CsvFileInfoDTO;
import com.sp.framework.common.enums.FileProcessErrorEnum;
import com.sp.framework.common.exception.SystemException;
import com.sp.framework.common.handler.CsvReaderHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Iterator;

/**
 * @description: CSV解析工具类
 * @author: luchao
 * @create: 2021-03-19 18:42
 **/
@Slf4j
public class CsvProcessUtil {
    /**
     * 转换为CSV记录数据
     * @param multipartFile
     * @param fileInfoDTO
     * @return
     */
    public static Iterator<CSVRecord> toCSVRecord(MultipartFile multipartFile, Class<? extends CsvFileInfoDTO> fileInfoDTO) {
        CsvReaderHandler instance = CsvReaderHandler.getInstance();
        String[] headers = instance.getHeaders(fileInfoDTO);
        if (null == headers || headers.length == 0) {
            log.error(FileProcessErrorEnum.CSV_IMPORT_HEADER_ERROR.getMessage());
            throw new SystemException(FileProcessErrorEnum.CSV_IMPORT_HEADER_ERROR.getCode(),
                    FileProcessErrorEnum.CSV_IMPORT_HEADER_ERROR.getMessage());
        }

        Iterator<CSVRecord> read = null;
        try {
            read = instance.read(multipartFile, headers, true);
        } catch (IOException e) {
            log.error(FileProcessErrorEnum.CSV_READ_ERROR.getMessage());
            throw new SystemException(FileProcessErrorEnum.CSV_READ_ERROR.getCode(),
                    FileProcessErrorEnum.CSV_READ_ERROR.getMessage());
        }
        return read;
    }
}
