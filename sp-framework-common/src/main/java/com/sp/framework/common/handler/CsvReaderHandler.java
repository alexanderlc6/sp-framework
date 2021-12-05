package com.sp.framework.common.handler;

import com.sp.framework.common.annotation.ObtainFieldNames;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * CSV读取处理器
 * @author Alex Lu
 * @description： scv处理器
 * @date ：Created in 2020/5/20 17:17
 */
public class CsvReaderHandler {
    private static final CsvReaderHandler instance;

    private CsvReaderHandler() {
    }

    static {
        instance = new CsvReaderHandler();
    }

    public static CsvReaderHandler getInstance() {
        return instance;
    }

    /**
     * 通过文件路径读取CSV文件
     * @param csvFilePath
     * @param fileHeader
     * @param skipHeader
     * @return
     * @throws IOException
     */
    public Iterator<CSVRecord> read(final String csvFilePath, final String[] fileHeader, boolean skipHeader) throws IOException {
        CSVFormat format;
        if (skipHeader) {
            //这里显式配置一下CSV文件的Header，然后设置跳过Header(否则读的时候会把头也当成一条记录)
            format = CSVFormat.DEFAULT.withHeader(fileHeader).withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim();
            //format = CSVFormat.DEFAULT.withHeader(fileHeader).withIgnoreHeaderCase().withTrim().withSkipHeaderRecord();
        } else {
            format = CSVFormat.DEFAULT.withHeader(fileHeader).withIgnoreHeaderCase().withTrim();
        }

        Reader reader = null;
        CSVParser csvParser = null;
        Iterator<CSVRecord> iterator = null;
        try {
            reader = Files.newBufferedReader(Paths.get(csvFilePath));
            csvParser = new CSVParser(reader, format);
            iterator = csvParser.iterator();
        } finally {
            if (csvParser != null) {
                csvParser.close();
            }
            if (reader != null) {
                reader.close();
            }
        }

        return iterator;
    }

    /**
     * 通过文件对象读取CSV文件
     * @param csvFileObj
     * @param fileHeader
     * @param skipHeader
     * @return
     * @throws IOException
     */
    public Iterator<CSVRecord> read(final MultipartFile csvFileObj, final String[] fileHeader, boolean skipHeader) throws IOException {
        CSVFormat format;
        // 判断是否为空
        if (csvFileObj.isEmpty()) {
            return null;
        }

        //这里显式配置一下CSV文件的Header，然后设置跳过Header(否则读的时候会把头也当成一条记录)
        if (skipHeader) {
            format = CSVFormat.DEFAULT.withHeader(fileHeader).withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim();
        } else {
            format = CSVFormat.DEFAULT.withHeader(fileHeader).withIgnoreHeaderCase().withTrim();
        }

        InputStream inputStream = csvFileObj.getInputStream();
        InputStreamReader is = new InputStreamReader(inputStream);
        Reader reader = new BufferedReader(is);

        CSVParser csvParser = new CSVParser(reader, format);
        Iterator<CSVRecord> iterator = csvParser.iterator();
        return iterator;
    }

    public String[] getHeaders(Class clazz) {
        List<String> list = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ObtainFieldNames annotation = field.getAnnotation(ObtainFieldNames.class);
            if (null != annotation && annotation.value()) {
                list.add(field.getName());
            }
        }
        if (!CollectionUtils.isEmpty(list)) {
            return list.toArray(new String[list.size()]);
        }
        return null;
    }

}
