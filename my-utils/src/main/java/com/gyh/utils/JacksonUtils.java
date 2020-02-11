package com.gyh.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * 
 * @author guoyanhong
 * @date 2018/9/29 9:52
 */
@Slf4j
public class JacksonUtils {

	private static ObjectMapper mapper;

	public static ObjectMapper getMapper() {
		if (mapper == null) {
			mapper = new ObjectMapper();
			// null值不返回
			mapper.setSerializationInclusion(Include.NON_NULL);
			// 空的 ArrayList() 之类的对象也不返回
			// mapper.setSerializationInclusion(Include.NON_EMPTY);

			// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		}
		return mapper;
	}

	/**
	 * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合. json转换为对象
	 * 
	 * @param jsonString
	 * @param clazz
	 * @return
	 */
	public static <T> T fromJson(String jsonString, Class<T> clazz) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}
		try {
			return getMapper().readValue(jsonString, clazz);
		} catch (JsonParseException e) {
			e.printStackTrace();
			log.info(e.getMessage());
		} catch (JsonMappingException e) {
			log.info(e.getMessage());
		} catch (IOException e) {
			log.info(e.getMessage());
		}
		return null;
	}

	/**
	 * 如果对象为Null, 返回"null". 如果集合为空集合, 返回"[]". 对象转换为Json
	 */
	public static String toJson(Object object) {
		try {
			return getMapper().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.info(e.getMessage());
		}
		return null;
	}

	/**
	 * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合. 如需读取集合如List/Map, 且不是List<String>这种简单类型时,先使用函數constructParametricType构造类型.
	 */
	private static <T> T fromJson(String jsonString, JavaType javaType) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}
		try {
			return getMapper().readValue(jsonString, javaType);
		} catch (JsonParseException e) {
			log.info(e.getMessage());
		} catch (JsonMappingException e) {
			log.info(e.getMessage());
		} catch (IOException e) {
			log.info(e.getMessage());
		}
		return null;
	}

	/**
	 * @param parametrized
	 * @param parameterClasses
	 * @return
	 */
	private static JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
		return getMapper().getTypeFactory().constructParametricType(parametrized, parameterClasses);
	}

	/**
	 * @param jsonString
	 * @param parametrized
	 * @param parameterClasses
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromJson(String jsonString, Class<?> parametrized, Class<?>... parameterClasses) {
		return (T) fromJson(jsonString, constructParametricType(parametrized, parameterClasses));
	}

	// 读文件，返回字符串
	public static String readFile(File file) {
		BufferedReader reader = null;
		String laststr = "";
		try {
			// System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				System.out.println("line " + line + ": " + tempString);
				laststr = laststr + tempString;
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return laststr;
	}

}
