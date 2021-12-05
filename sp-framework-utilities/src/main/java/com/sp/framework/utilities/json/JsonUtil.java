package com.sp.framework.utilities.json;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json转换的工具类
 * @author alexlu
 *
 */
public class JsonUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

	private static ObjectMapper mapper;
	static{
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 忽略没有定义在属性

	}
	
	/**
	 * json返回到string
	 * 因编译报错，先暂时注掉
	 * @param str
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> readList(String str, Class<T> cls) {
		
		
		JavaType javaType =mapper.getTypeFactory().constructParametricType(List.class, cls);
		
		try {
			return (List<T>)mapper.readValue(str, javaType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.debug(e.getMessage());
		} 
		
		return null;
	}

	/**
	 * 把json字符串转换为对象
	 * 
	 * @param json
	 * @param parametrized
	 * @param parameterClasses
	 * @return 如果为空串，将返回null
	 * @throws ClientException
	 * @since
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readValue(String json, Class<?> parametrized,
			Class<?>... parameterClasses) {
		if (json == null || json=="" ) {
			return null;
		}
		try {
			JavaType jt = mapper.getTypeFactory().constructParametricType(
					parametrized, parameterClasses);
			return (T)mapper.readValue(json, jt);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 将json串转换成对象
	 * @param json
	 * @param parametrized
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readValue(String json, Class<?> parametrized) {
		if (json == null || json=="" ) {
			return null;
		}
		try {
			return (T) mapper.readValue(json, parametrized);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 将对象转成json串
	 * @param obj
	 * @return
	 */
	public static String writeValue(Object obj) {
		if (obj == null) {
			return "";
		}
		try {
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 按格式输出JSON文本
	 * @param obj
	 * @return
	 */
	public static String writeFormattedValue(Object obj) {
		if (obj == null) {
			return "";
		} else {
			try {
				return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
			} catch (Exception var2) {
				throw new RuntimeException(var2.getMessage(), var2);
			}
		}
	}
}
