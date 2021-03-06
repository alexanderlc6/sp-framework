//===================================================================
// Created on Apr 23, 2008
//===================================================================
package com.sp.framework.utilities.type;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
@SuppressWarnings( { "deprecation"})
public class ExcelUtil {
	
	private String dataFormat = "m/d/yy h:mm";

	private Map<HSSFWorkbook, HSSFCellStyle> dateStyleMaps = new HashMap<HSSFWorkbook, HSSFCellStyle>();

	private String[] heanders; // 表头集

	private String[] beannames; // bean的名称集

	public ExcelUtil() {

	}

	public ExcelUtil(String[] heanders) {
		this.heanders = heanders;
	}

	public void createXLSHeader(HSSFSheet sheet) {
		for (int i = 0; i < heanders.length; i++) {
			setStringValue(sheet, (short) 0, (short) i, heanders[i]);
		}
	}

	public void createXLS(HSSFWorkbook wb, HSSFSheet sheet, List<Object[]> dateList) {
		for (int i = 1; i <= dateList.size(); i++) {
			Object[] object = dateList.get(i - 1);
			for (int j = 0; j < object.length; j++) {
				this.doSetCell(wb, sheet, (short) i, (short) j, object[j]);
			}
		}
	}


	public void doSetCell(HSSFWorkbook wb, HSSFSheet sheet, int rowNum, int colNum, Object value) {
		if (value != null) {
			if (value instanceof Number) {
				setDoubleValue(sheet, rowNum, colNum, Double.valueOf(value.toString()));
			} else if (value instanceof String) {
				setStringValue(sheet, rowNum, colNum, value.toString());
			} else if (value instanceof Date) {
				// 样式有数量限制，重用相同的样式
				HSSFCellStyle dateStyle = null;
				if (dateStyleMaps.containsKey(wb)) {
					dateStyle = dateStyleMaps.get(wb);
				} else {
					dateStyle = wb.createCellStyle();
					dateStyleMaps.put(wb, dateStyle);
				}
				setDateValue(sheet, dateStyle, rowNum, colNum, (Date) value);
			}
		}
	}

	public void setDoubleValue(HSSFSheet sheet, int rowNum, int colNum, Double value) {
		HSSFCell cell = this.getMyCell(sheet, rowNum, colNum);
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
	}

	public void setDateValue(HSSFSheet sheet, HSSFCellStyle dateStyle, int rowNum, int colNum, Date value) {
		HSSFCell cell = this.getMyCell(sheet, rowNum, colNum);
		// 设定单元格日期显示格式
		// 指定日期显示格式
		dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(dataFormat));
		cell.setCellStyle(dateStyle);
		cell.setCellValue(value);
	}

	public void setStringValue(HSSFSheet sheet, int rowNum, int colNum, String value) {
		HSSFCell cell = this.getMyCell(sheet, rowNum, colNum);
		// 单元格汉字编码转换
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		HSSFRichTextString str = new HSSFRichTextString(value);
		cell.setCellValue(str);
	}

	/**
	 * 获得指定Cell
	 * 
	 * @return HSSFCell
	 */
	private HSSFCell getMyCell(HSSFSheet sheet, int rowNum, int colNum) {
		HSSFRow row = sheet.getRow(rowNum);
		if (null == row) {
			row = sheet.createRow(rowNum);
		}
		HSSFCell cell = row.getCell((short) colNum);
		if (null == cell) {
			cell = row.createCell((short) colNum);
		}
		return cell;
	}

	public String[] getBeannames() {
		return beannames;
	}

	public void setBeannames(String[] beannames) {
		this.beannames = beannames;
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}
	

	/**
	 * <p>
	 * 判断excel是否为标准的格式，
	 * </p>
	 * 
	 * @param EXLPath
	 *            exl的路径
	 * @param protyName
	 *            title要求顺序
	 * @return
	 */
	public static boolean checkTitleExl(File file, String[] protyName) {
		boolean checkFlag = false;
		try {
			Workbook wb = WorkbookFactory.create(new FileInputStream(file));
			Sheet sheet = wb.getSheetAt(0); // 获得第一个sheet的内容
			Row row = sheet.getRow(0); // 获得第一个行的内容
			if (null == row) {
				checkFlag = false;
			} else {
				for (int i = 0; i < protyName.length; i++) {
					Cell cell = row.getCell((short) i);
					if (cell != null) {
						if (!protyName[i].equals(cell.getRichStringCellValue().toString())) {
							checkFlag = false;
							break;
						}
					} else {
						checkFlag = false;
						break;
					}
					if (i == protyName.length - 1)
						checkFlag = true;
				}
			}
		} catch (FileNotFoundException e) {
			checkFlag = false;
		} catch (IOException e) {
			checkFlag = false;
		} catch (EncryptedDocumentException e) {
			checkFlag = false;
		} catch (InvalidFormatException e) {
			checkFlag = false;
		}
		return checkFlag;
	}

	/**
	 * 传入类名,和该类中的属性名,通过类的放射来实现填充数据,返回该对象的集合. 从excel导入数据.
	 * 
	 * @param className
	 *            类名,全路径
	 * @param protyName
	 *            属性集合 map中必须要有errorInfo，该行错误信息存贮
	 * @param EXLPath
	 *            excel文件的路径
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static <T> List<T> genEXLToObject(File file, Class<T> clazz, String[] protyName) throws FileNotFoundException {
		return genEXLToObject(new FileInputStream(file), clazz, protyName);
	}
	
	/**
	 * 传入类名,和该类中的属性名,通过类的放射来实现填充数据,返回该对象的集合. 从excel导入数据.
	 * 
	 * @param className
	 *            类名,全路径
	 * @param protyName
	 *            属性集合 map中必须要有errorInfo，该行错误信息存贮
	 * @param EXLPath
	 *            excel文件的路径
	 * @return
	 */
	public static <T> List<T> genEXLToObject(InputStream is, Class<T> clazz, String[] protyName) {
		List<T> reList = new ArrayList<T>();
		int j = 1;
		try {
			Workbook wb = WorkbookFactory.create(is);
			Sheet sheet = wb.getSheetAt(0); // 获得第一个sheet的内容.
			Row row = sheet.getRow(j); // 获得第一个行的内容
			Map<String, Object> protyMap = new HashMap<String, Object>();
			while (row != null) { // 判断是否是最后一行记录
				// 实例化类,根据传入的类路径
				T obj = clazz.newInstance();
				j++;
				for (int i = 0; i < protyName.length; i++) {// 第一行内容
					Cell cell = row.getCell((short) i);
					if (cell != null) {
						protyMap.put(protyName[i], conversionCell(cell));
					}
				}
				BeanUtils.populate(obj, protyMap);
				reList.add(obj);
				row = sheet.getRow(j);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reList;
	}
	
	
	/**
	 * 传入类名,和该类中的属性名,通过类的放射来实现填充数据,返回该对象的集合. 从excel导入数据，
	 * 可以公用，提供类型转换
	 * @param is excel文件
	 * @param className 类全名
	 * @param protyName 属性名
	 * @param format 数据格式
	 * @return
	 */
	public static <T> List<T> genEXLToObject(InputStream is, Class<T> clazz, String[] protyName, String[] format) {
		List<T> reList = new ArrayList<T>();

		int j = 1;
		try {
			Workbook wb = WorkbookFactory.create(is);
			Sheet sheet = wb.getSheetAt(0); // 获得第一个sheet的内容.
			Row row = sheet.getRow(j); // 获得第一个行的内容
			while (row != null) { // 判断是否是最后一行记录
				// 实例化类,根据传入的类路径
				T obj = clazz.newInstance();
				// 设置对象的属性
				setPropertyValues(obj,protyName,format,row);
				reList.add(obj);
				j++;
				row = sheet.getRow(j);
			}
		} catch (IOException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (EncryptedDocumentException e) {
		} catch (InvalidFormatException e) {
		}
		return reList;
	}
	/**
	 * 设置对象的属性
	 * @param bean 对象bean
	 * @param protyName 属性列表
	 * @param format 格式列表
	 * @param row excel行
	 * @return 错误信息
	 */
	private static String setPropertyValues(Object bean , String[] protyName, String[] format, Row row){
		for (int i = 0; i < protyName.length; i++) {// 第一行内容
			Cell cell = row.getCell((short) i);
			if (cell != null) {
				Object value = conversionCell(cell);
				try {
					//获得属性描述
					PropertyDescriptor property = PropertyUtils.getPropertyDescriptor(bean, protyName[i]);
					//日期单独处理
					if(property.getPropertyType().isAssignableFrom(Date.class)){
						if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
							//获得cell的日期值
							BeanUtils.setProperty(bean, protyName[i], cell.getDateCellValue());
						}else if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
							//根据格式获得日期
							if(format!=null && format.length > i && StringUtils.isNotBlank(format[i])){
								BeanUtils.setProperty(bean, protyName[i], TimeUtil.toCalendar((String)value, format[i]).getTime());
							}
						}
					}else{
						//设置属性，会自动匹配类型
						BeanUtils.setProperty(bean, protyName[i], value);
					}
				} catch (Exception e) {
					try {
						//将错误消息写进errorInfo属性
						BeanUtils.setProperty(bean, "errorInfo", e.getMessage());
					} catch (Exception e1) {
					}
					return e.getMessage();
				}
			}
		}
		return "";
	}

	/**
	 * 单元格类型转换
	 * 
	 * @param cell
	 * @return
	 */
	private static Object conversionCell(Cell cell) {
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
			return cell.getNumericCellValue();
		else
			return cell.getRichStringCellValue().toString();
	}
}


