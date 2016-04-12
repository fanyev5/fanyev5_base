package cn.com.fanyev5.basecommons.codec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;

/**
 * XML工具
 * 
 * @author fanqi427@gmail.com
 * @since 2013-6-18
 */
public final class XMLUtil {

    private XMLUtil() {
    }

    /**
     * Object 转换为 XML字符串
     * 
     * @param obj
     * @return
     */
    public static String obj2Xml(Object obj) {
        try {
            return getInstance().writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * XML字符串 转换为 Object
     * 
     * @param xml
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T xml2Obj(String xml, Class<T> clazz) {
        try {
            return getInstance().readValue(xml, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * XML字符串 转换为 Object
     * 
     * @param xml
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T xml2Obj(String xml, TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(xml, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static ObjectMapper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final ObjectMapper INSTANCE = new XmlMapper();
    }

}
