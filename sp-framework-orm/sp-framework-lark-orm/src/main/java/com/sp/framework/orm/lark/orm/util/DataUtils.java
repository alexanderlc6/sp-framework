package com.sp.framework.orm.lark.orm.util;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 数据生成工具类.
 * 
 * @author abba
 */
public class DataUtils {
	private static Random random = new Random();

	/**
	 * 返回随机ID.
	 */
	public static long randomId() {
		return random.nextLong();
	}

	/**
	 * 返回随机ID.
	 */
	public static boolean randomBoolean() {
		return random.nextBoolean();
	}

	/**
	 * 返回随机名称, prefix字符串+随机数字.
	 */
	public static String randomName(String prefix) {
		return prefix + random.nextInt(10000);
	}

	/**
	 * 返回随机名称, prefix字符串+随机数字.
	 */
	public static String randomName(String prefix, int length) {
		return prefix + random.nextInt(10000);
	}

	/**
	 * 从输入list中随机返回一个对象.
	 */
	public static <T> T randomOne(List<T> list) {
		return randomSome(list, 1).get(0);
	}

	/**
	 * 从输入list中随机返回随机个对象.
	 */
	public static <T> List<T> randomSome(List<T> list) {
		return randomSome(list, random.nextInt(list.size()));
	}

	/**
	 * 从输入list中随机返回count个对象.
	 */
	public static <T> List<T> randomSome(List<T> list, int count) {
		Collections.shuffle(list);
		return list.subList(0, count);
	}
}
