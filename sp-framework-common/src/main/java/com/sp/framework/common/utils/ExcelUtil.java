package com.sp.framework.common.utils;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    /**
     * 创建模板化工作集
     *
     * @param response
     * @param dataList
     * @param headers
     * @param fname
     * @throws Exception
     */
    public static void createFixationSheet(HttpServletResponse response,
                                           List<Object> dataList, List<String> headers, String fname) throws Exception {
        // 创建工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        // 在工作薄上建一张工作表
        HSSFSheet sheet = wb.createSheet();
        HSSFRow row = sheet.createRow((short) 0);
        sheet.createFreezePane(0, 1);
        HSSFCellStyle cel = wb.createCellStyle();
        //设置表格头信息
        for (int i = 0; i < headers.size(); i++) {
            createCell(cel, row, (short) i, headers.get(i));
        }
        //填充数据
        if (dataList != null && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                Object obj = dataList.get(i);
                Field[] fields = obj.getClass().getDeclaredFields();
                HSSFRow rowi = sheet.createRow((short) (i + 1));
                for (int j = 0; j < 4; j++) {
                    reflect(cel, rowi, obj);
                }
            }
        }

        //清空输出流
        response.reset();
        //设定输出文件头,该方法有两个参数，分别表示应答头的名字和值。
        response.setHeader("Content-Disposition", "attachment; filename="
                + fname + ".xls");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //取得输出流
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    /**
     * 创建单元格
     *
     * @param cel
     * @param row
     * @param col
     * @param val
     */
    @SuppressWarnings("deprecation")
    private static void createCell(HSSFCellStyle cel, HSSFRow row, short col,
                                   String val) {
        HSSFCell cell = row.createCell(col);
        cell.setCellValue(val);

        cel.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        cell.setCellStyle(cel);
    }

    /**
     * 映射填充数据
     *
     * @param cel
     * @param row
     * @param obj
     */
    public static void reflect(HSSFCellStyle cel, HSSFRow row,
                               Object obj) {
        if (obj == null) {
            return;
        }

        Field[] fields = obj.getClass().getDeclaredFields();
        for (int j = 0; j < fields.length; j++) {
            fields[j].setAccessible(true);
            if (fields[j].getType().getName().equals(
                    String.class.getName())) {
                try {
                    // 字段值
                    if (fields[j].get(obj) != null) {
                        createCell(cel, row, (short) j, fields[j].get(obj).toString());
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (fields[j].getType().getName().equals(
                    Integer.class.getName())
                    || fields[j].getType().getName().equals("int")) {
                try {
                    if (fields[j].get(obj) != null) {
                        createCell(cel, row, (short) j, fields[j].get(obj).toString());
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            // 其他类型..
        }
    }

    /**
     * 拼装单个对象
     *
     * @param obj
     * @param row
     * @return
     * @throws Exception
     */
    private static Map<String, Object> dataObj(Object obj, HSSFRow row) throws Exception {
        Class<?> rowClazz = obj.getClass();
        Field[] fields = FieldUtils.getAllFields(rowClazz);
        if (fields == null || fields.length < 1) {
            return null;
        }

        //容器
        Map<String, Object> map = new HashMap<String, Object>();

        //注意excel表格字段顺序要和obj字段顺序对齐 （如果有多余字段请另作特殊下标对应处理）
        for (int j = 0; j < fields.length; j++) {
            map.put(fields[j].getName(), getVal(row.getCell(j)));
        }

        return map;
    }

    /**
     * 导入Excel
     *
     * @param file
     * @param obj
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> importExcel(MultipartFile file, Object obj) throws Exception {
        //装载流
        POIFSFileSystem fs = new POIFSFileSystem(file.getInputStream());
        HSSFWorkbook hw = new HSSFWorkbook(fs);

        //获取第一个sheet页
        HSSFSheet sheet = hw.getSheetAt(0);

        //容器
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();

        //遍历行 从下标第一行开始（去除标题）
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            HSSFRow row = sheet.getRow(i);
            if (row != null) {
                //装载obj
                ret.add(dataObj(obj, row));
            }
        }
        return ret;
    }

    /**
     * 处理单元格值（暂时只处理string和number，可以自己添加自己需要的val类型）
     *
     * @param hssfCell
     * @return
     */
    public static String getVal(HSSFCell hssfCell) {
        if (hssfCell != null) {
            if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                return hssfCell.getStringCellValue();
            } else {
                return String.valueOf(hssfCell.getNumericCellValue());
            }
        }
        return null;
    }
}
