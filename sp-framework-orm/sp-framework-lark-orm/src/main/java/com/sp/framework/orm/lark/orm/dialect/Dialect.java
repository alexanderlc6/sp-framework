package com.sp.framework.orm.lark.orm.dialect;

/**
 * 类似hibernate的Dialect,但只精简出分页部分
 * @author badqiu
 */
public abstract class Dialect {

	public abstract boolean supportsLimit();

	public abstract boolean supportsLimitOffset();

	/**
	 * 将sql变成分页sql语句,直接使用offset,limit的值作为占位符.</br>
	 * 源代码为: getLimitString(sql,offset,String.valueOf(offset),limit,String.valueOf(limit))
	 */
	public String getLimitString(String sql, int offset, int limit) {
		return getLimitString(sql, offset, Integer.toString(offset), limit, Integer.toString(limit));
	}

	/**
	 * 将sql变成分页sql语句,提供将offset及limit使用占位符(placeholder)替换.
	 * <pre>
	 * 如mysql
	 * com.yh.common.lark.orm.dialect.getLimitString("select * from user", 12, ":offset",0,":limit") 将返回
	 * select * from user limit :offset,:limit
	 * </pre>
	 * @return 包含占位符的分页sql
	 */
	public abstract String getLimitString(String sql, int offset, String offsetPlaceholder, int limit,
			String limitPlaceholder);

}
