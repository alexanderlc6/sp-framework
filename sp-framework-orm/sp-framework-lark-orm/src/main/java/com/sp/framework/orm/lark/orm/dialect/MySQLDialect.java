package com.sp.framework.orm.lark.orm.dialect;

/**
 * @author abba
 */
public class MySQLDialect extends Dialect {

	@Override
	public boolean supportsLimitOffset() {
		return true;
	}

	@Override
	public boolean supportsLimit() {
		return true;
	}

	@Override
	public String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder) {
		if (offset > 0) {
			//在limit第一个值大的时候，offset速度最快，在第一个参数值小的时候，比较接近
			return sql + " limit " + limitPlaceholder + " offset " + offsetPlaceholder;
		} else {
			return sql + " limit " + limitPlaceholder;
		}
	}

}
