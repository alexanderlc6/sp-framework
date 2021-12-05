package com.sp.framework.common.utils.process;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.util.StringUtils;
import com.sp.framework.common.constant.FileConstant;
import com.sp.framework.common.enums.FileProcessErrorEnum;
import com.sp.framework.common.utils.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Excel处理工具类
 *
 * @author Alex Lu
 * @date 2019/12/13
 */
@Slf4j
public class ExcelProcessUtil {
    /**
     * 默认初始化的Excel工作集对象
     * 若调用方传入则使用传入的工作集
     */
    private static Sheet initSheet;

    /**
     * Excel类型
     * 1-少量数据(少于1000行)
     * 2-大量数据(大于1000行)
     */
    private static Integer excelType;

    /**
     * Excel Sheet名称
     */
    private static String sheetName;

    public static void initSetConfig(Integer excelType, String sheetName) {
        ExcelProcessUtil.excelType = excelType;
        ExcelProcessUtil.sheetName = sheetName;

        initSheet = new Sheet(1, 0);
        initSheet.setSheetName(ExcelProcessUtil.sheetName);

        //设置自适应宽度
        initSheet.setAutoWidth(Boolean.TRUE);
    }

    /**
     * 处理读取待导出的Excel
     * @param filePath Excel文件路径
     * @param sheet Excel工作集
     * @return
     */
    public static List<Object> processDataExport(String filePath, Sheet sheet){
        if(!StringUtils.hasText(filePath)){
            return Collections.EMPTY_LIST;
        }

        sheet = sheet != null ? sheet : initSheet;

        InputStream fileStream = null;
        try {
            fileStream = new FileInputStream(filePath);

            /** 根据文件内容类型来执行不同数据读取策略 */
            if(FileConstant.PROCESS_EXCEL_TYPE_FEW_CONTENT_ROW.equals(ExcelProcessUtil.excelType)){
                //读取少量内容行的工作集(少于1000行)
                return EasyExcelFactory.read(fileStream, sheet);
            }else if(FileConstant.PROCESS_EXCEL_TYPE_BULK_CONTENT_ROW.equals(ExcelProcessUtil.excelType)){
                //读取大量内容行的工作集(大于1000行)
                ExcelListener excelListener = new ExcelListener();
                EasyExcelFactory.readBySax(fileStream, sheet, excelListener);
                return excelListener.getProcessDataList();
            }
        } catch (FileNotFoundException e){
            log.error(FileProcessErrorEnum.NOT_FOUND_FILE_EXCEPTION.getMessage(), filePath);
        } finally {
            try {
                if(fileStream != null){
                    fileStream.close();
                }
            }catch (IOException e){
                log.error(FileProcessErrorEnum.FILE_READ_EXCEPTION.getMessage(), e);
            }
        }

        return null;
    }

    /**
     * 生成Excel
     * @param filePath 绝对路径,如/home/test/123.xlsx
     * @param normalDataList 普通内容源数据集合
     * @param headerList Excel表头集合
     * @param sheet Excel指定工作集
     * @param hasTemplate 是否带有模板生成Excel
     * @param templateDataList 带模板样式内容数据集合
     * @param multipleSheetModelList 多个Excel工作集(带模板样式)内容集合
     */
    public static void writeExcelBySheet(String filePath, List<List<Object>> normalDataList,
                                          List<String> headerList, Sheet sheet,
                                          Boolean hasTemplate, List<? extends BaseRowModel> templateDataList,
                                          List<MultipleSheetModel> multipleSheetModelList){
        sheet = sheet != null ? sheet : initSheet;

        if(CollectionUtil.isNotEmpty(headerList)){
            List<List<String>> contentList = new ArrayList();
            headerList.forEach(h -> contentList.add(Collections.singletonList(h)));
            sheet.setHead(contentList);
        }

        OutputStream outputStream = null;
        ExcelWriter excelWriter = null;

        try {
            outputStream = new FileOutputStream(filePath);
            excelWriter = EasyExcelFactory.getWriter(outputStream);

            if(CollectionUtil.isEmpty(multipleSheetModelList)) {
                //根据是否模板写入Excel
                if (hasTemplate) {
                    sheet.setClazz(templateDataList.get(0).getClass());
                    excelWriter.write(templateDataList, sheet);
                } else {
                    excelWriter.write1(normalDataList, sheet);
                }
            }else {
                for (MultipleSheetModel multipleSheetModel : multipleSheetModelList) {
                    Sheet curSheet = multipleSheetModel.getSheet() != null ? multipleSheetModel.getSheet() : initSheet;
                    if(!CollectionUtil.isEmpty(multipleSheetModel.getDataList())) {
                        curSheet.setClazz(multipleSheetModel.getDataList().get(0).getClass());
                    }

                    excelWriter.write(multipleSheetModel.getDataList(), curSheet);
                }
            }
        }catch (FileNotFoundException e){
            log.error(FileProcessErrorEnum.NOT_FOUND_FILE_EXCEPTION.getMessage(), filePath);
        }finally {
            try {
                if(excelWriter != null){
                    excelWriter.finish();
                }

                if(outputStream != null){
                    outputStream.close();
                }
            }catch (IOException e){
                log.error(FileProcessErrorEnum.FILE_EXPORT_FAILED.getMessage(), e);
            }
        }
    }

    /**
     * 测试方法
     * @param args
     */
    public static void main(String[] args) {
        ExcelProcessUtil.initSetConfig(1,"Test11");

        String filePath = "D:\\Project\\Baozun\\Test\\TT.xlsx";
        List<List<Object>> normalDataList = new ArrayList();
        List<Object> testData = Arrays.asList(1,2,3,4,5);
        normalDataList.add(testData);

        testData = Arrays.asList("t1",22,"tg3","tg4","tg6");
        normalDataList.add(testData);

        testData = Arrays.asList("t2",36,"tP4","GHT","FTH");
        normalDataList.add(testData);

        testData = Arrays.asList("t3","4re",12,"bb12","rrr32");
        normalDataList.add(testData);

        List<String> headerList = Arrays.asList("No", "T2","T3","T4","T5");

//        //1.创建新单个Sheet测试
//        ExcelProcessUtil.writeExcelBySheet(filePath, normalDataList, headerList, null,
//                false, null, null);
//
        List<ExcelHeaderTestModel> tmpDataList = new ArrayList();
        ExcelHeaderTestModel excelHeaderTestModel;

//        //2.创建带模板单个Sheet测试
//        for (int i = 0; i < 4; i++){
//            excelHeaderTestModel = new ExcelHeaderTestModel("Test" + i, 12 + i,"Sch0" + i);
//            tmpDataList.add(excelHeaderTestModel);
//        }
//
//        ExcelProcessUtil.writeExcelBySheet(filePath, null, headerList, null,
//                true, tmpDataList, null);

        //3.创建带模板单个Sheet测试
        List<MultipleSheetModel> multipleSheetModelList = new ArrayList();

        for (int j = 0; j < 4; j++) {
            tmpDataList = new ArrayList<>();

            for (int i = 0; i < 4; i++){
                excelHeaderTestModel = new ExcelHeaderTestModel(j + "-Test" + i, 12 + i + j,j + "-Sch0" + i);
                tmpDataList.add(excelHeaderTestModel);
            }

            Sheet sheet = new Sheet(j, 0);
            sheet.setSheetName("Test" + j);
            MultipleSheetModel multipleSheetModel = new MultipleSheetModel();
            multipleSheetModel.setDataList(tmpDataList);
            multipleSheetModel.setSheet(sheet);
            multipleSheetModelList.add(multipleSheetModel);
        }

        ExcelProcessUtil.writeExcelBySheet(filePath, null, null, null,
                true, null, multipleSheetModelList);

        //读取Excel数据测试
//        Sheet sheet = new Sheet(1,1);
//        ExcelProcessUtil.processDataExport(filePath, sheet);
//        List<Object> objData = ExcelProcessUtil.processDataExport(filePath, sheet);
//        objData.forEach(System.out::println);
    }
}
