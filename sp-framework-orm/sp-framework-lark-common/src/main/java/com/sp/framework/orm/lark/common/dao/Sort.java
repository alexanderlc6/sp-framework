package com.sp.framework.orm.lark.common.dao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApiModel("分页查询排序条件对象")
public class Sort implements Serializable{
	private static final long serialVersionUID = -1392714341585793291L;
	
	public static final String ASC = "asc";
	public static final String DESC = "desc";	
	
	public Sort(){
		this.field = "";
		this.type = ASC;
	}
	
	public Sort(String sortStr) {
		String[] strs = sortStr.trim().split(" ");
		this.field = strs[0];
		if(strs.length > 1){
			if(ASC.equalsIgnoreCase(strs[1]))
				this.type = ASC;
			else if(DESC.equalsIgnoreCase(strs[1]))
				this.type = DESC;
			else
				throw new RuntimeException("Wrong sort type definition, only asc or desc are supported.");
		}			
	}
	
	public Sort(String field, String type) {
		this.field = field;
		this.type = type;
	}

	@ApiModelProperty(value = "排序字段")
	private String field;

	@ApiModelProperty(value = "排序类型(asc-升序,desc-降序)", required = true)
	private String type = ASC;
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sort other = (Sort) obj;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return field + " " + type;
	}		
	
	public static Sort[] parse(String sortStr){
		if(sortStr == null || sortStr.trim().length() == 0) return null;
		String[] sorts = sortStr.split(",");
		List<Sort> list = new ArrayList<Sort>();
		for(String s: sorts){
			String[] strs = s.trim().split(" ");
			String order = strs.length > 1? strs[1] : ASC;
			if((!order.equals(ASC)) && (!order.equals(DESC)))
				throw new RuntimeException();
			list.add(new Sort(strs[0],order));
		}
		return list.toArray(new Sort[0]);
	}
	
	public static String toSortStr(Sort[] sorts ){
		StringBuffer sbSortStr=new StringBuffer();
		for(int i=0;i<sorts.length;i++){
			sbSortStr.append(sorts[i].toString());
			if(i!=sorts.length-1){
				sbSortStr.append(",");
			}
		}
		
		return sbSortStr.toString();
	}
}
